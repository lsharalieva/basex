package org.deepfs.fsml.parsers;

import java.io.EOFException;
import java.io.IOException;
import org.basex.util.Util;
import org.deepfs.fsml.BufferedFileChannel;
import org.deepfs.fsml.DeepFile;
import org.deepfs.fsml.FileType;
import org.deepfs.fsml.MetaElem;
import org.deepfs.fsml.MimeType;
import org.deepfs.fsml.ParserRegistry;

/**
 * Parser for JPG files.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 * @author Bastian Lemke
 */
public final class JPGParser implements IFileParser {

  static {
    ParserRegistry.register("jpg", JPGParser.class);
    ParserRegistry.register("jpeg", JPGParser.class);
  }

  /** Exif header. Null terminated ASCII representation of 'Exif'. */
  private static final byte[] HEADER_EXIF =
      { 0x45, 0x78, 0x69, 0x66, 0x00, 0x00 };
  /**
   * <p>
   * JFIF header.
   * </p>
   * <ul>
   * <li>5 bytes: ASCII representation of 'JFIF' (null terminated)</li>
   * <li>1 byte: major JFIF version (only v1 supported)</li>
   * </ul>
   */
  private static final byte[] HEADER_JFIF = //
    { 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01 };
  /** Extended JFIF header. Null terminated ASCII representation of 'JFXX'. */
  private static final byte[] HEADER_JFXX = { 0x4A, 0x46, 0x58, 0x58, 0x00 };
  /** DeepFile instance to store metadata and file contents. */
  private DeepFile deepFile;
  /** Parser for Exif data. */
  private final ExifParser exifParser = new ExifParser();
  /** The {@link BufferedFileChannel} to read from. */
  private BufferedFileChannel bfc;

  @Override
  public boolean check(final DeepFile df) throws IOException {
    final BufferedFileChannel f = df.getBufferedFileChannel();
    try {
      f.buffer(6);
    } catch(final EOFException ex) {
      return false;
    }
    int b;
    // 0xFFD8FFE0 or 0xFFD8FFE1
    return f.getShort() == 0xFFD8
        && ((b = f.getShort()) == 0xFFE0 || b == 0xFFE1);
  }

  @Override
  public void extract(final DeepFile df) throws IOException {
    if(df.extractMeta()) {
      deepFile = df;
      if(!check(deepFile)) return;

      deepFile.setFileType(FileType.PICTURE);
      deepFile.setFileFormat(MimeType.JPG);

      bfc = deepFile.getBufferedFileChannel();
      bfc.skip(-2);
      while(bfc.get() == 0xFF) {
        final int b = bfc.get(); // segment marker
        final int size = readSize();
        switch(b) {
          case 0xE0: // JFIF
            readJFIF(size);
            break;
          case 0xE1: // Exif
            readExif(size);
            break;
          case 0xFE: // Comment
            readComment(size);
            break;
          default:
            if(b >= 0xC0 && b <= 0xC3) {
              readDimensions();
              return;
            }
            bfc.skip(size);
        }
        bfc.buffer(4); // next segment marker and size value
      }
    }
  }

  /**
   * Reads two bytes from the {@link BufferedFileChannel} and returns the value
   * as integer.
   * @return the size value
   * @throws IOException if any error occurs
   */
  private int readSize() throws IOException {
    return bfc.getShort() - 2;
  }

  /**
   * <p>
   * Reads {@code a.length} bytes from the {@link BufferedFileChannel} and
   * checks if they are equals to the array content.
   * </p>
   * <p>
   * <b>Assure that at least {@code a.length} bytes are buffered, before
   * calling this method.</b>
   * <p>
   * @param a the array content to check
   * @return true if the next bytes are equals to the array content, false
   *         otherwise
   * @throws IOException if any error occurs
   */
  private boolean checkNextBytes(final byte[] a) throws IOException {
    final int len = a.length;
    final byte[] a2 = new byte[len];
    bfc.get(a2, 0, len);
    for(int i = 0; i < len; ++i)
      if(a[i] != a2[i]) return false;
    return true;
  }

  /**
   * Reads image width and height.
   * @throws IOException if any error occurs while reading from the file
   *           channel
   */
  private void readDimensions() throws IOException {
    bfc.buffer(9);
    if(bfc.get() == 8) {
      final int height = bfc.getShort();
      final int width = bfc.getShort();
      MetaElem e = MetaElem.PIXEL_HEIGHT;
      if(!deepFile.isMetaSet(e)) deepFile.addMeta(e, height);
      e = MetaElem.PIXEL_WIDTH;
      if(!deepFile.isMetaSet(e)) deepFile.addMeta(e, width);
    } else {
      deepFile.debug("JPGParser: Wrong data precision field.");
    }
  }

  /**
   * Reads an Exif segment.
   * @param size the size of the segment
   * @throws IOException if any error occurs while reading from the file
   * @see <a href="http://www.Exif.org/Exif2-2.PDF">Exif 2.2</a>
   */
  private void readExif(final int size) throws IOException {
    final int len = HEADER_EXIF.length;
    bfc.buffer(len);
    if(!checkNextBytes(HEADER_EXIF)) bfc.skip(size - len);
    final DeepFile subFile = deepFile.subfile(size - len);
    try {
      exifParser.extract(subFile);
    } finally {
      try {
        subFile.finish();
      } catch(final Exception ex) {
        deepFile.debug("JPGParser: Failed to parse Exif data.", ex);
      }
    }
  }

  /**
   * Reads a JFIF (JPEG File Interchange Format) segment.
   * @param size the size of the segment
   * @throws IOException if any error occurs while reading from the file
   * @see <a href="http://www.w3.org/Graphics/JPEG/jfif3.pdf">JFIF 1.02 spec</a>
   */
  private void readJFIF(final int size) throws IOException {
    bfc.buffer(16); // standard JFIF segment has 16 bytes
    final long pos = bfc.position();
    if(!checkNextBytes(HEADER_JFIF)) {
      bfc.position(pos);
      readJFXX(size);
      return;
    }
    final int s = size - HEADER_JFIF.length;
    bfc.skip(s);
  }

  /**
   * Reads an extended JFIF segment.
   * @param size the size of the segment
   * @throws IOException if any error occurs while reading from the file
   */
  private void readJFXX(final int size) throws IOException {
    // method is only called from readJFIF() which cares about buffering
    final long pos = bfc.position();
    if(!checkNextBytes(HEADER_JFXX)) {
      bfc.position(pos + size);
      return;
    }
    int s = size - HEADER_JFXX.length - 1;
    final DeepFile content = deepFile.newContentSection("Thumbnail",
        bfc.absolutePosition(), s);
    switch(bfc.get()) { // extension code
      case 0x10: // Thumbnail coded using JPEG
        content.setFileType(FileType.PICTURE);
        content.setFileFormat(MimeType.JPG);
        content.addMeta(MetaElem.DESCRIPTION, "Thumbnail coded using JPEG.");
        break;
      case 0x11: // Thumbnail coded using 1 byte/pixel
        s -= 2;
        content.setFileType(FileType.PICTURE);
        content.addMeta(MetaElem.DESCRIPTION,
            "Thumbnail coded using 1 byte/pixel.");
        content.addMeta(MetaElem.PIXEL_WIDTH, bfc.get());
        content.addMeta(MetaElem.PIXEL_HEIGHT, bfc.get());
        break;
      case 0x13: // Thumbnail coded using 3 bytes/pixel
        s -= 2;
        content.setFileType(FileType.PICTURE);
        content.addMeta(MetaElem.DESCRIPTION,
            "Thumbnail coded using 3 bytes/pixel.");
        content.addMeta(MetaElem.PIXEL_WIDTH, bfc.get());
        content.addMeta(MetaElem.PIXEL_HEIGHT, bfc.get());
        break;
      default:
        deepFile.debug("JPGParser: Illegal or unsupported JFIF header.");
    }
    bfc.skip(s);
  }

  /**
   * Reads an comment segment.
   * @param size the size of the segment
   * @throws IOException if any error occurs while reading from the file
   */
  private void readComment(final int size) throws IOException {
    deepFile.addMeta(MetaElem.DESCRIPTION, bfc.get(new byte[size]));
  }

  @Override
  public void propagate(final DeepFile df) {
    Util.notimplemented();
  }
}
