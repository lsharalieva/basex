package org.deepfs.fsml.extractors;

import java.io.IOException;
import org.basex.core.Main;

/**
 * MetaData exception.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Alexander Holupirek
 */
public final class MetaDataException extends IOException {
  /**
   * Constructor.
   * @param message message
   */
  public MetaDataException(final String message) {
    super(message);
  }

  /**
   * Constructor.
   * @param s message
   * @param e message extension
   */
  public MetaDataException(final String s, final Object... e) {
    super(Main.info(s, e));
  }
}