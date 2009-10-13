package org.deepfs.jfuse;

import org.catacombae.jfuse.JNILoader;
import org.catacombae.jfuse.types.system.FileModeFlags;
import org.catacombae.jfuse.util.FUSEUtil;

/**
 * This class assembles all references to the external jfuse library.
 * 
 * In order to keep BaseX independent from external libraries this class
 * assembles all calls to the Java FUSE bindings and provides default values. In
 * case jfuse.jar is removed from the build path this class and the actual FUSE
 * implementation should be the only classes to be modified or removed. In fact,
 * it is intended to just exclude DeepFSImpl from the build path and to toggle
 * the (un)commented code here.
 * 
 * @author Alexander Holupirek <alex@holupirek.de>
 * 
 */
public final class JFUSEAdapter implements FileModeFlags {

//  /** Directory bit. */
//  private static final int DFS_S_IFDIR = 0040000;
//  /** Regular file bit. */
//  private static final int DFS_S_IFREG = 0100000;
//  /** File bit mask. */
//  private static final int DFS_S_IFMT = 0170000;

  /** Private utility constructor. */
  private JFUSEAdapter() {
    throw new UnsupportedOperationException();
  }

  /**
   * Check (and load) native Java FUSE bindings.
   * @return true in case of success.
   */
  public static boolean loadJFUSELibrary() {
    try {
      JNILoader.ensureLoaded();
      return true;
    } catch(final UnsatisfiedLinkError ex) {
      return false;
    }
    // return false;
  }

  /**
   * Get (native) user id or default value.
   * @return user id
   */
  public static long getUID() {
    if(loadJFUSELibrary()) {
      return FUSEUtil.getProcessUid();
    }
    return 0;
  }

  /**
   * Get (native) group id or default value.
   * @return group id
   */
  public static long getGID() {
    if(loadJFUSELibrary()) {
      return FUSEUtil.getProcessGid();
    }
    return 0;
  }

  /**
   * Returns directory bit.
   * 
   * @return bitmask indicating a directory
   */
  public static int getSIFDIR() {
    return S_IFDIR;
    // return DFS_S_IFDIR;
  }

  /**
   * Returns regular file bit.
   * 
   * @return bitmask indicating a directory
   */
  public static int getSIFREG() {
    return S_IFREG;
    // return DFS_S_IFREG;
  }

  /**
   * Test mode for regular file flag.
   * @param mode of file
   * @return true if mode is regular file
   */
  public static boolean isReg(final int mode) {
    return (mode & S_IFMT) == S_IFREG;
    // return (mode & DFS_S_IFMT) == DFS_S_IFREG;
  }
}