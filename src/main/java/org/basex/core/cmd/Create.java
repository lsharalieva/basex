package org.basex.core.cmd;

import static org.basex.core.Text.*;
import org.basex.build.Parser;

/**
 * This class allows developers to define their own parser implementations.
 * The class can be instantiated like all other database commands.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public class Create extends ACreate {
  /** Parser instance. */
  protected final Parser parser;

  /**
   * Convenience constructor for specifying a parser, input path and
   * database name.
   * @param p parser instance
   * @param name name of database
   */
  public Create(final Parser p, final String name) {
    super(name);
    parser = p;
  }

  @Override
  protected boolean run() {
    // check if file exists
    if(!parser.file.exists()) return error(FILEWHICH, parser.file);
    // run the build process
    return build(parser, args[0]);
  }
}
