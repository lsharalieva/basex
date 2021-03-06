package org.basex.query.expr;

import static org.basex.query.util.Err.*;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.item.Item;
import org.basex.query.item.ANode;
import org.basex.query.item.SeqType;
import org.basex.query.item.Type;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.iter.NodeCache;
import org.basex.util.InputInfo;

/**
 * Root node.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public final class Root extends Simple {
  /**
   * Constructor.
   * @param ii input info
   */
  public Root(final InputInfo ii) {
    super(ii);
    type = SeqType.NOD_ZM;
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Iter iter = checkCtx(ctx).iter(ctx);
    final NodeCache ni = new NodeCache().random();
    Item i;
    while((i = iter.next()) != null) {
      final ANode n = root(i);
      if(n == null || n.type != Type.DOC) CTXNODE.thrw(input);
      ni.add(n);
    }
    return ni;
  }

  /**
   * Returns the root node of the specified item.
   * @param v input node
   * @return root node
   */
  public ANode root(final Value v) {
    if(!v.node()) return null;
    ANode n = (ANode) v;
    while(true) {
      final ANode p = n.parent();
      if(p == null) return n;
      n = p;
    }
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.CTX;
  }

  @Override
  public boolean duplicates() {
    return false;
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    return cmp instanceof Root;
  }

  @Override
  public String toString() {
    return "root()";
  }
}
