package org.basex.query.expr;

import static org.basex.query.util.Err.*;
import static org.basex.query.QueryTokens.*;
import static org.basex.util.Token.*;
import org.basex.core.User;
import org.basex.core.Commands.CmdPerm;
import org.basex.data.Data;
import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.item.Bln;
import org.basex.query.item.DBNode;
import org.basex.query.item.Empty;
import org.basex.query.item.Item;
import org.basex.query.item.ANode;
import org.basex.query.item.SeqType;
import org.basex.query.item.Type;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.util.Err;
import org.basex.util.InputInfo;

/**
 * Abstract parse expression. All non-value expressions are derived from
 * this class.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public abstract class ParseExpr extends Expr {
  /** Input information. */
  public final InputInfo input;
  /** Cardinality of result; unknown if set to -1. */
  public long size = -1;
  /** Data type. */
  public SeqType type;

  /**
   * Constructor.
   * @param ii input info
   */
  public ParseExpr(final InputInfo ii) {
    input = ii;
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Item it = item(ctx, input);
    return it != null ? it.iter() : Empty.ITER;
  }

  @Override
  public Item item(final QueryContext ctx, final InputInfo ii)
      throws QueryException {

    final Iter ir = iter(ctx);
    final Item it = ir.next();
    if(it == null || ir.size() == 1) return it;

    final Item n = ir.next();
    if(n != null) XPSEQ.thrw(input, PAR1 + it + SEP + n +
        (ir.next() != null ? SEP + DOTS : "") + PAR2);
    return it;
  }

  @Override
  public Value value(final QueryContext ctx) throws QueryException {
    if(type().zeroOrOne()) {
      final Value v = item(ctx, input);
      return v == null ? Empty.SEQ : v;
    }
    return ctx.iter(this).finish();
  }

  @Override
  public final Item ebv(final QueryContext ctx, final InputInfo ii)
      throws QueryException {

    Item it = null;
    if(type().zeroOrOne()) {
      it = item(ctx, input);
    } else {
      final Iter ir = iter(ctx);
      it = ir.next();
      if(it != null && !it.node() && ir.next() != null)
        CONDTYPE.thrw(input, this);
    }
    return it == null ? Bln.FALSE : it;
  }

  @Override
  public final Item test(final QueryContext ctx, final InputInfo ii)
      throws QueryException {
    final Item it = ebv(ctx, input);
    return (it.num() ? it.dbl(input) == ctx.pos : it.bool(input)) ? it : null;
  }

  @Override
  public SeqType type() {
    return type != null ? type : SeqType.ITEM_ZM;
  }

  @Override
  public final long size() {
    return size == -1 ? type().occ() : size;
  }

  // OPTIMIZATIONS ============================================================

  /**
   * Pre-evaluates the specified expression.
   * @param ctx query context
   * @return optimized expression
   * @throws QueryException query exception
   */
  public final Expr preEval(final QueryContext ctx) throws QueryException {
    return optPre(item(ctx, input), ctx);
  }

  /**
   * Adds an optimization info for pre-evaluating the specified expression.
   * @param opt optimized expression
   * @param ctx query context
   * @return optimized expression
   */
  protected final Expr optPre(final Expr opt, final QueryContext ctx) {
    if(opt != this) ctx.compInfo(QueryText.OPTPRE, this);
    return opt == null ? Empty.SEQ : opt;
  }

  // VALIDITY CHECKS ==========================================================

  /**
   * Checks if the specified expressions is no updating expression.
   * @param e expression
   * @param ctx query context
   * @return the specified expression
   * @throws QueryException query exception
   */
  public final Expr checkUp(final Expr e, final QueryContext ctx)
      throws QueryException {
    if(e != null && ctx.updating && e.uses(Use.UPD)) UPNOT.thrw(input, desc());
    return e;
  }

  /**
   * Tests if the specified expressions are updating or vacuous.
   * @param ctx query context
   * @param expr expression array
   * @throws QueryException query exception
   */
  public void checkUp(final QueryContext ctx, final Expr... expr)
      throws QueryException {

    if(!ctx.updating) return;
    int s = 0;
    for(final Expr e : expr) {
      if(e.vacuous()) continue;
      final boolean u = e.uses(Use.UPD);
      if(u && s == 2 || !u && s == 1) UPNOT.thrw(input, desc());
      s = u ? 1 : 2;
    }
  }

  /**
   * Checks if the specified expression yields a string.
   * Returns a token representation or an exception.
   * @param e expression to be checked
   * @param ctx query context
   * @return item
   * @throws QueryException query exception
   */
  public final double checkDbl(final Expr e, final QueryContext ctx)
      throws QueryException {

    final Item it = checkEmptyType(e.item(ctx, input), Type.DBL);
    if(!it.unt() && !it.num()) Err.number(this, it);
    return it.dbl(input);
  }

  /**
   * Checks if the specified expression is an integer.
   * Returns a token representation or an exception.
   * @param e expression to be checked
   * @param ctx query context
   * @return integer value
   * @throws QueryException query exception
   */
  public final long checkItr(final Expr e, final QueryContext ctx)
      throws QueryException {
    return checkItr(checkEmptyType(e.item(ctx, input), Type.ITR));
  }

  /**
   * Checks if the specified item is no empty sequence.
   * @param it item to be checked
   * @param t expected type
   * @return specified item
   * @throws QueryException query exception
   */
  private Item checkEmptyType(final Item it, final Type t)
      throws QueryException {
    if(it == null) XPEMPTYPE.thrw(input, desc(), t);
    return it;
  }

  /**
   * Checks if the specified item is a number.
   * Returns a token representation or an exception.
   * @param it item to be checked
   * @return item
   * @throws QueryException query exception
   */
  public final long checkItr(final Item it) throws QueryException {
    if(!it.unt() && !it.type.instance(Type.ITR)) Err.type(this, Type.ITR, it);
    return it.itr(input);
  }

  /**
   * Checks if the specified expression is a node.
   * Returns the node or an exception.
   * @param it item to be checked
   * @return item
   * @throws QueryException query exception
   */
  public final ANode checkNode(final Item it) throws QueryException {
    if(!it.node()) Err.type(this, Type.NOD, it);
    return (ANode) it;
  }

  /**
   * Checks if the specified expression is a database node.
   * Returns the node or an exception.
   * @param it item to be checked
   * @return item
   * @throws QueryException query exception
   */
  public final DBNode checkDBNode(final Item it) throws QueryException {
    if(!(it instanceof DBNode)) NODBCTX.thrw(input, this);
    return (DBNode) it;
  }

  /**
   * Checks if the specified collation is supported.
   * @param e expression to be checked
   * @param ctx query context
   * @throws QueryException query exception
   */
  public final void checkColl(final Expr e, final QueryContext ctx)
      throws QueryException {

    final Item it = e instanceof Item ? (Item) e : checkItem(e, ctx);
    if(!it.str() || !eq(URLCOLL, it.atom())) IMPLCOL.thrw(input, e);
  }

  /**
   * Checks if the specified expression yields a string.
   * Returns a token representation or an exception.
   * @param e expression to be evaluated
   * @param ctx query context
   * @return item
   * @throws QueryException query exception
   */
  public final byte[] checkStr(final Expr e, final QueryContext ctx)
      throws QueryException {
    final Item it = checkItem(e, ctx);
    if(!it.str() && !it.unt()) Err.type(this, Type.STR, it);
    return it.atom();
  }

  /**
   * Checks if the specified item is a string or an empty sequence.
   * Returns a token representation or an exception.
   * @param it item to be checked
   * @return item
   * @throws QueryException query exception
   */
  public final byte[] checkEStr(final Item it) throws QueryException {
    if(it == null) return EMPTY;
    if(!it.str() && !it.unt()) Err.type(this, Type.STR, it);
    return it.atom();
  }

  /**
   * Throws an exception if the context item is not set.
   * @param ctx query context
   * @return result of check
   * @throws QueryException query exception
   */
  public final Value checkCtx(final QueryContext ctx) throws QueryException {
    final Value v = ctx.value;
    if(v == null) XPNOCTX.thrw(input, this);
    return v;
  }

  /**
   * Returns the data reference.
   * @param ctx query context
   * @return data reference
   * @throws QueryException query exception
   */
  public final Data checkData(final QueryContext ctx) throws QueryException {
    final Data data = ctx.resource.data();
    if(data == null) NODBCTX.thrw(input, this);
    return data;
  }

  /**
   * Checks if the specified expression yields a non-empty item.
   * @param e expression to be evaluated
   * @param ctx query context
   * @return item
   * @throws QueryException query exception
   */
  public final Item checkItem(final Expr e, final QueryContext ctx)
      throws QueryException {
    return checkEmpty(e.item(ctx, input));
  }

  /**
   * Checks if the specified item is an empty sequence; if yes, throws
   * an exception.
   * @param it item to be checked
   * @return specified item
   * @throws QueryException query exception
   */
  public final Item checkEmpty(final Item it) throws QueryException {
    if(it == null) XPEMPTY.thrw(input, desc());
    return it;
  }

  /**
   * Checks if the specified expression yields a string or empty sequence.
   * Returns a token representation or an exception.
   * @param e expression to be evaluated
   * @param ctx query context
   * @return item
   * @throws QueryException query exception
   */
  public final byte[] checkEStr(final Expr e, final QueryContext ctx)
      throws QueryException {
    return checkEStr(e.item(ctx, input));
  }

  /**
   * Checks if an expression yields a valid and existing {@link IO} instance.
   * Returns the instance or an exception.
   * @param e expression to be evaluated
   * @param ctx query context
   * @return io instance
   * @throws QueryException query exception
   */
  public final IO checkIO(final Expr e, final QueryContext ctx)
      throws QueryException {

    checkAdmin(ctx);
    final byte[] name = checkStr(e, ctx);
    final IO io = IO.get(string(name));
    if(!io.exists()) DOCERR.thrw(input, name);
    return io;
  }

  /**
   * Checks if the current user has admin permissions. If negative, an
   * exception is thrown.
   * @param ctx query context
   * @throws QueryException query exception
   */
  public final void checkAdmin(final QueryContext ctx) throws QueryException {
    if(!ctx.context.user.perm(User.ADMIN)) PERMNO.thrw(input, CmdPerm.ADMIN);
  }
}
