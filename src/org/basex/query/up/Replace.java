package org.basex.query.up;

import static org.basex.query.QueryText.*;
import static org.basex.query.QueryTokens.*;
import java.util.HashSet;
import java.util.Set;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.item.DBNode;
import org.basex.query.item.FNode;
import org.basex.query.item.FTxt;
import org.basex.query.item.Item;
import org.basex.query.item.Nod;
import org.basex.query.item.Type;
import org.basex.query.iter.Iter;
import org.basex.query.iter.SeqIter;
import org.basex.query.up.primitives.ReplaceElemContent;
import org.basex.query.up.primitives.ReplacePrimitive;
import org.basex.query.up.primitives.ReplaceValue;
import org.basex.query.util.Err;
import org.basex.util.Token;

/**
 * Replace expression.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Lukas Kircher
 */
public final class Replace extends Update {
  /** 'Value of' flag. */
  private final boolean value;

  /**
   * Constructor.
   * @param t target expression
   * @param r source expression
   * @param v replace value of
   */
  public Replace(final Expr t, final Expr r, final boolean v) {
    super(t, r);
    value = v;
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Iter t = SeqIter.get(expr[0].iter(ctx));
    Item i = t.next();
    
    // check target constraints
    if(i == null) Err.or(UPSEQEMP, i);
    if(t.size() > 1 || !(i instanceof Nod) || i.type == Type.DOC) 
      Err.or(UPTRGMULT, i);
    final Nod n = (Nod) i;
    final Nod p = n.parent();
    final boolean a = n.type == Type.ATT;
    if(p == null) Err.or(UPNOPAR, i);
    
    // check replace constraints
    // non attribute non value
    final Iter r = SeqIter.get(expr[1].iter(ctx));
    if(!a && !value) {
      final SeqIter seq = new SeqIter();
      i = r.next();
      while(i != null) {
        if(i.type.num || i.type.str) seq.add(new FTxt(i.str(), null));
        else if(i instanceof Nod) {
          final Nod tn = (Nod) i;
          if(tn.type == Type.ATT) Err.or(UPWRELM, i);
          if(tn.type == Type.DOC) seq.add(tn.child());
          else seq.add(tn);
        } else Err.or(UPMISS1, this);
        i = r.next();
      }
      seq.reset();
      // [LK] evtl merge text nodes in sequence iterator here?
      ctx.updates.add(new ReplacePrimitive(n, seq, false));
      return Iter.EMPTY;
    }
    
    // replace attribute node
    if(a && !value) {
      i = r.next();
      final HashSet<String> set = new HashSet<String>();
      // bpar states if ns constraints for target parent are hurt. 
      // Error results in XUDY0023.
      boolean bpar = false;
      // brep states if a ns constraint is hurt by the replacing node set ...
      // ... this results in XUDY0024.
      boolean brep = false;
      while(i != null) {
        if(i.type != Type.ATT) Err.or(UPWRATTR, i);
        // check namespace constraints for replace node set (dupl. attributes..)
        brep = checkNS(set, (Nod) i) | brep;
        i = r.next();
      }
      // check attributes of parent of target node for namespace constraints
      final SeqIter tAttr = SeqIter.get(n.parent().attr());
      i = tAttr.next();
      while(i != null) {
        bpar = checkNS(set, (Nod) i) | bpar;
        i = tAttr.next();
      }
      if(bpar) Err.or(UPCONFNSPAR, i);
      if(brep) Err.or(UPCONFNS, i);
      r.reset();
      ctx.updates.add(new ReplacePrimitive(n, r, true));
      
    // replace value / element content
    } else {
      // [LK] if value is replaced source expression is evaluated like a
      // text node constructor
      i = r.next();
      if(i == null) Err.or(UPMISS2, this);
      if(i.type.num || i.type.str)
        ctx.updates.add(n.type == Type.ELM ? 
          new ReplaceElemContent(n, i.str()) :
          new ReplaceValue(n, i.str()));
      
      else Err.or(UPMISS3, this);
    }
    return Iter.EMPTY;
  }
  
  /**
   * Checks for duplicates/namespace conflicts in the given set. 
   * @param s set
   * @param n node ns to add
   * @return true if duplicates exist
   */
  private static boolean checkNS(final Set<String> s, final Nod n) {
    if(n instanceof FNode) return !s.add(Token.string(((FNode) n).nname()));
    final DBNode dn = (DBNode) n;
    return !s.add(Token.string(dn.data.attName(dn.pre)));
  }

  @Override
  public String toString() {
    return REPLACE + ' ' + (value ? VALUEE + ' ' + OF : "") +
      ' ' + NODE +  ' ' + expr[0] + ' ' + WITH + ' ' + expr[1];
  }
}