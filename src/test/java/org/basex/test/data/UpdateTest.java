package org.basex.test.data;

import static org.basex.util.Token.*;
import static org.junit.Assert.*;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.Command;
import org.basex.core.Prop;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.Open;
import org.basex.util.Util;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the update features of the Data class.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Tim Petrowsky
 */
public abstract class UpdateTest {
  /** Test file we do updates with. */
  private static final String TESTFILE = "etc/xml/test.xml";
  /** Test database name. */
  private static final String DBNAME = Util.name(UpdateTest.class);
  /** Main memory flag. */
  private static boolean mainmem = true;
  /** Table memory flag. */
  private static boolean tablemem = true;

  /** JUnit tag. */
  protected static final byte[] JUNIT = token("junit");
  /** JUnit tag. */
  protected static final byte[] FOO = token("foo");
  /** JUnit tag. */
  protected static final byte[] NAME = token("name");
  /** JUnit tag. */
  protected static final byte[] PARENTNODE = token("parentnode");
  /** JUnit tag. */
  protected static final byte[] CONTEXTNODE = token("contextnode");
  /** JUnit tag. */
  protected static final byte[] ID = token("id");
  /** JUnit tag. */
  protected static final byte[] B = token("b");
  /** Database context. */
  protected static final Context CONTEXT = new Context();
  /** Test file size in nodes. */
  protected int size;

  /**
   * Initializes the test class.
   */
  @BeforeClass
  public static final void setUpBeforeClass() {
    final Prop prop = CONTEXT.prop;
    prop.set(Prop.TEXTINDEX, false);
    prop.set(Prop.ATTRINDEX, false);
    prop.set(Prop.MAINMEM, mainmem);
    prop.set(Prop.TABLEMEM, tablemem);
  }

  /**
   * Closes the test database.
   */
  @AfterClass
  public static void finish() {
    CONTEXT.close();
  }

  /**
   * Creates the database.
   */
  @Before
  public void setUp() {
    exec(new CreateDB(DBNAME, TESTFILE));
    size = CONTEXT.data.meta.size;
  }

  /**
   * Deletes the test database.
   */
  @After
  public void tearDown() {
    if(mainmem) return;
    exec(new Close());
    exec(new DropDB(DBNAME));
  }

  /**
   * Reloads the database.
   */
  protected void reload() {
    if(mainmem) return;
    exec(new Close());
    exec(new Open(DBNAME));
  }

  /**
   * Tests byte-arrays for equality.
   * @param exp expected value
   * @param act actual value
   */
  protected void assertByteArraysEqual(final byte[] exp, final byte[] act) {
    assertEquals("array lengths don't equal", exp.length, act.length);
    for(int i = 0; i < exp.length; ++i) assertEquals(exp[i], act[i]);
  }

  /**
   * Tests for correct data size.
   */
  @Test
  public void testSize() {
    assertEquals("Unexpected size!", size, CONTEXT.data.meta.size);
    reload();
    assertEquals("Unexpected size!", size, CONTEXT.data.meta.size);
  }

  /**
   * Executes the specified command. Gives feedback and stops the test
   * if errors occur.
   * @param cmd command reference
   */
  private void exec(final Command cmd) {
    try {
      cmd.execute(CONTEXT);
    } catch(final BaseXException ex) {
      Util.errln(ex.getMessage());
    }
  }
}
