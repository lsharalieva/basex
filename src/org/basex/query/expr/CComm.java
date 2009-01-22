package org.basex.query.expr;

import static org.basex.query.QueryText.*;
import static org.basex.util.Token.*;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.item.FComm;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;
import org.basex.query.util.Err;
import org.basex.util.TokenBuilder;

/**
 * Comment fragment.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class CComm extends Single {
  /** Two Dashes. */
  private static final byte[] DASHES = { '-', '-' };
  
  /**
   * Constructor.
   * @param c comment
   */
  public CComm(final Expr c) {
    super(c);
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Iter iter = ctx.iter(expr);

    final TokenBuilder tb = new TokenBuilder();
    boolean more = false;
    Item it;
    while((it = iter.next()) != null) {
      if(more) tb.add(' ');
      tb.add(it.str());
      more = true;
    }
    final byte[] atom = tb.finish();
    if(contains(atom, DASHES) || startsWith(atom, '-') || endsWith(atom, '-'))
      Err.or(COMINVALID, atom);

    return new FComm(atom, null).iter();
  }
  
  @Override
  public Return returned(final QueryContext ctx) {
    return Return.NOD;
  }
  
  @Override
  public String info() {
    return "Comment constructor";
  }

  @Override
  public String toString() {
    return "comment {" + expr + "}";
  }
}