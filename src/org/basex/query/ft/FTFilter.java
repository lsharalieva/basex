package org.basex.query.ft;

import org.basex.ft.Tokenizer;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Arr;
import org.basex.query.item.FTItem;
import org.basex.util.BoolList;
import org.basex.util.IntList;

/**
 * Abstract FTFilter expression.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 */
public abstract class FTFilter extends Arr {
  /** Units. */
  public enum FTUnit {
    /** Word unit. */      WORD,
    /** Sentence unit. */  SENTENCE,
    /** Paragraph unit. */ PARAGRAPH;

    /**
     * Returns a string representation.
     * @return string representation
     */
    @Override
    public String toString() { return name().toLowerCase(); }
  }

  /** Optional unit. */
  FTUnit unit = FTUnit.WORD;

  /**
   * Evaluates the filter expression.
   * @param ctx query context
   * @param node full-text node
   * @param ft tokenizer
   * @return result of check
   * @throws QueryException query exception
   */
  abstract boolean filter(final QueryContext ctx, final FTItem node,
      final Tokenizer ft) throws QueryException;

  /**
   * Checks if the filter needs the whole text node to be parsed.
   * Is overwritten by some filters to perform other checks.
   * @return result of check
   */
  boolean content() {
    return unit != FTUnit.WORD;
  }
  
  /**
   * Checks if each token is reached by the distance query.
   * @param mn minimum distance
   * @param mx maximum distance
   * @param dst distance/window flag
   * @param pos position list
   * @param ft tokenizer
   * @return result of check
   */
  // [CG] reduce #arguments
  final boolean checkDist(final long mn, final long mx, final boolean dst,
      final IntList[] pos, final Tokenizer ft) {
    final IntList[] il = sortPositions(pos);
    for(int z = 0; z < il[1].size; z++) {
      if(checkDist(z, il[0], il[1], mn, mx, new BoolList(pos.length), dst, ft))
        return true;
    }
    return false;
  }

  /**
   * Checks if each token is reached by the distance query.
   * @param x current position value
   * @param p pos list
   * @param pp pointer list
   * @param mn minimum number
   * @param mx maximum number
   * @param bl boolean list for each token
   * @param dst distance/window flag
   * @param ft tokenizer
   * @return boolean result
   */
  // [CG] reduce #arguments
  private boolean checkDist(final int x, final IntList p,  final IntList pp,
      final long mn, final long mx, final BoolList bl, final boolean dst,
      final Tokenizer ft) {

    if(bl.all(true)) return true;
    int i = x + 1;

    final int p1 = pos(p.list[x], unit, ft);
    while(i < p.size) {
      final int p2 = pos(p.list[i], unit, ft);

      if(dst) {
        // distance
        final int d = p2 - p1 - 1;
        if(d >= mn && d <= mx && !bl.list[pp.list[i]]) {
          bl.list[pp.list[x]] = true;
          bl.list[pp.list[i]] = true;
          if(checkDist(i, p, pp, mn, mx, bl, dst, ft)) return true;
        }
      } else {
        // window
        final int d = p2 - p1;
        if(mn + d <= mx && !bl.list[pp.list[i]]) {
          bl.list[pp.list[x]] = true;
          bl.list[pp.list[i]] = true;
          if(checkDist(i, p, pp, mn + d, mx, bl, dst, ft)) return true;
        }
      }
      i++;
    }
    return false;
  }

  /**
   * Calculates a position value, dependent on the specified unit.
   * @param p word position
   * @param u unit
   * @param ft tokenizer
   * @return new position
   */
  final int pos(final int p, final FTUnit u, final Tokenizer ft) {
    if(u == FTUnit.WORD) return p;
    ft.init();
    while(ft.more() && ft.pos != p);
    return u == FTUnit.SENTENCE ? ft.sent : ft.para;
  }

  /**
   * Sorts the position values in numeric order.
   * IntList[0] = position values sorted
   * IntList[1] = pointer to the position values.
   * Each pos value has a pointer, showing which token
   * from the query could be found at that pos.
   * @param pos position list
   * @return IntList[] position values and pointer
   */
  final IntList[] sortPositions(final IntList[] pos) {
    final IntList[] il = { new IntList(), new IntList() };
    final int[] k = new int[pos.length];
    int min = 0;

    while(true) {
      min = 0;
      boolean q = true;
      for(int j = 0; j < pos.length; j++) {
        if(k[j] > -1) {
          if(k[min] == -1) min = j;
          q = false;
          if(pos[min].list[k[min]] > pos[j].list[k[j]]) min = j;
        }
      }
      if(q) break;

      il[0].add(pos[min].list[k[min]]);
      il[1].add(min);
      if(++k[min] == pos[min].size) k[min] = -1;
    }
    return il;
  }
}