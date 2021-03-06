package org.deepfs.fsml;

/**
 * An exception class used for signaling failure of parser operations.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Bastian Lemke
 */
public final class ParserException extends Exception {

  /**
   * Constructs a new exception with {@code null} as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a call
   * to {@link #initCause}.
   */
  public ParserException() {
    super();
  }

  /**
   * Constructs a new exception with the specified detail message. The cause is
   * not initialized, and may subsequently be initialized by a call to
   * {@link #initCause}.
   * @param message the detail message. The detail message is saved for later
   *          retrieval by the {@link #getMessage()} method
   */
  public ParserException(final String message) {
    super(message);
  }
}
