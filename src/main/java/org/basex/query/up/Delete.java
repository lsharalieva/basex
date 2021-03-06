package org.basex.query.up;

import static org.basex.query.util.Err.*;
import static org.basex.query.QueryTokens.*;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.item.Item;
import org.basex.query.item.ANode;
import org.basex.query.iter.Iter;
import org.basex.query.up.primitives.DeletePrimitive;
import org.basex.util.InputInfo;

/**
 * Delete expression.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Lukas Kircher
 */
public final class Delete extends Update {
  /**
   * Constructor.
   * @param ii input info
   * @param r return expression
   */
  public Delete(final InputInfo ii, final Expr r) {
    super(ii, r);
  }

  @Override
  public Item item(final QueryContext ctx, final InputInfo ii)
      throws QueryException {
    final Iter t = ctx.iter(expr[0]);
    Item i;
    while((i = t.next()) != null) {
      if(!(i instanceof ANode)) UPTRGDELEMPT.thrw(input);
      final ANode n = (ANode) i;
      // nodes without parents are ignored
      if(n.parent() == null) continue;
      ctx.updates.add(new DeletePrimitive(input, n), ctx);
    }
    return null;
  }

  @Override
  public String toString() {
    return DELETE + ' ' + NODES + ' ' + expr[0];
  }
}
