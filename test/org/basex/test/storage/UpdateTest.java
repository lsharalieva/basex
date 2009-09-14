package org.basex.test.storage;

import static org.basex.util.Token.*;
import static org.junit.Assert.*;
import org.basex.core.Context;
import org.basex.core.Process;
import org.basex.core.Prop;
import org.basex.core.proc.Close;
import org.basex.core.proc.CreateDB;
import org.basex.core.proc.DropDB;
import org.basex.core.proc.Open;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the update features of the Data class.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Tim Petrowsky
 */
public abstract class UpdateTest {
  /** Test file we do updates with. */
  private static final String TESTFILE = "etc/xml/test.xml";
  /** Test database name. */
  private static final String DBNAME = UpdateTest.class.getSimpleName();

  /** JUnit tag. */
  protected static final byte[] JUNIT = token("junit");
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
    exec(new CreateDB(TESTFILE, DBNAME));
    size = CONTEXT.data().meta.size;
  }

  /**
   * Deletes the test database.
   */
  @After
  public void tearDown() {
    exec(new Close());
    exec(new DropDB(DBNAME));
  }

  /**
   * Reloads the database.
   */
  protected void reload() {
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
    for(int i = 0; i < exp.length; i++) assertEquals(exp[i], act[i]);
  }

  /**
   * Tests for correct data size.
   */
  @Test
  public void testSize() {
    assertEquals("Unexpected size!", size, CONTEXT.data().meta.size);
    reload();
    assertEquals("Unexpected size!", size, CONTEXT.data().meta.size);
  }

  /**
   * Executes the specified command. Gives feedback and stops the test
   * if errors occur.
   * @param proc process instance
   */
  private void exec(final Process proc) {
    if(!proc.execute(CONTEXT)) {
      System.out.println(proc.info());
      System.exit(1);
    }
  }
}