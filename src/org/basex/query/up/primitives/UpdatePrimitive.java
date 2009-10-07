package org.basex.query.up.primitives;

import org.basex.query.QueryException;
import org.basex.query.item.Nod;

/**
 * Abstract XQuery Update Primitive.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Lukas Kircher
 */
public abstract class UpdatePrimitive {
  /** Target node of update expression. */
  public final Nod node;
  /** Multiple updates are applied on this target node. */
  boolean mult;
  
  /**
   * Update primitive type enumeration.
   *
   * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
   * @author Lukas Kircher
   *
   */
  public enum Type { 
    /** Type Insert into. */ INSERTINTO,
    /** Type Insert attribute. */ INSERTATTR,
    /** Type Replace value. */ REPLACEVALUE,
    /** Type Rename. */ RENAME,
    /** Type Insert before. */ INSERTBEFORE,
    /** Type Insert after. */ INSERTAFTER,
    /** Type Insert into as first. */ INSERTINTOFI,
    /** Type Insert into as last. */ INSERTINTOLA,
    /** Type Replace node. */ REPLACENODE,
    /** Type Replace element content. */ REPLACEELEMCONT,
    /** Type Delete. */ DELETE;
  };
  
  /**
   * Constructor.
   * @param n DBNode reference
   */
  protected UpdatePrimitive(final Nod n) {
    node = n;
    mult = false;
  }
  
  /**
   * Returns the type of the update primitive.
   * @return type
   */
  public abstract Type type();
  
  /**
   * Checks for constraints, etc.
   * @throws QueryException query exception 
   */
  public abstract void check() throws QueryException;
  
  /**
   * Applies the update operation represented by this primitive to the 
   * database. 
   */
  public abstract void apply();
  
  /**
   * Merges if possible two update primitives of the same type if they are
   * applied on the same target.
   * @param p primitive to be merged with 
   * @throws QueryException query exception
   */
  public abstract void merge(final UpdatePrimitive p) throws QueryException;
}