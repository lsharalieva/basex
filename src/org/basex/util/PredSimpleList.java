package org.basex.util;

import org.basex.query.xpath.expr.FTAnd;
import org.basex.query.xpath.expr.FTArrayExpr;
import org.basex.query.xpath.expr.FTContains;
import org.basex.query.xpath.locpath.PredSimple;

/**
 * This is a simple container for PredSimple expressions.
 * Could be used to sum up same FTContains Expr in different PredSimples.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class PredSimpleList {
  /** Value array. */
  public PredSimple[] list;
  /** Current array size. */
  public int size;
  
  /**
   * Default constructor.
   */
  public PredSimpleList() {
    this(8);
  }
  
  /**
   * Constructor.
   * 
   * @param is initial size of the list
   */
  public PredSimpleList(final int is) {
    list = new PredSimple[is];
  }
  
  /**
   * Adds next value.
   * Adds sums p with an equal pred or adds
   * p at the end of the list.
   * 
   * @param p PredSimple to be added
   * @return i index of p
   */
  public int add(final PredSimple p) {
    if (p.expr instanceof FTContains) {
      final FTContains ftc1 = (FTContains) p.expr;
      FTContains ftc2;
      
      for (int i = 0; i < size; i++) {
        ftc2 = (FTContains) list[i].expr;
        if (ftc1.expr1.sameAs(ftc2.expr1)) {
          // sum 
          if (ftc2.expr2 instanceof FTAnd) {
            FTAnd ftand = (FTAnd) ftc2.expr2;
            ftand.add((FTArrayExpr) ftc1.expr2);
          } else {
            FTAnd fta = new FTAnd(new FTArrayExpr[]{
                (FTArrayExpr) ftc1.expr2, (FTArrayExpr) ftc2.expr2});
            // FTSelect could be summed as well???
            ftc2.expr2 = fta;
            
          }
          return i;
        }
      }
      
      if(size == list.length) list = Array.extend(list);
      list[size++] = p;
      return size - 1;
    }
    
    return -1;
  }
  
  /**
   * Finishes the int array.
   * @return int array
   */
  public PredSimple[] finish() {
    return size == list.length ? list : Array.finish(list, size);
  }
}