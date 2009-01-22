package org.basex.query.item;

import static org.basex.query.QueryTokens.*;
import static org.basex.query.QueryText.*;

import java.io.IOException;
import org.basex.BaseX;
import org.basex.data.Serializer;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Return;
import org.basex.query.iter.Iter;
import org.basex.query.iter.SeqIter;
import org.basex.query.util.Err;
import org.basex.util.Token;

/**
 * Item sequence.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public class Seq extends Item {
  /** Empty sequence. */
  public static final Seq EMPTY = new Seq() {
    @Override
    public Iter iter() { return Iter.EMPTY; }
  };
  /** Item array. */
  public Item[] val;
  /** Number of entries. */
  public int size;

  /**
   * Constructor.
   */
  protected Seq() {
    super(Type.EMP);
  }
  
  /**
   * Constructor.
   * @param v value
   * @param s size
   * @return resulting item or sequence
   */
  public static Item get(final Item[] v, final int s) {
    return s == 0 ? EMPTY : s == 1 ? v[0] : new Seq(v, s);
  }
  
  /**
   * Constructor.
   * @param v value
   * @param s size
   */
  private Seq(final Item[] v, final int s) {
    this();
    val = v;
    size = s;
  }

  @Override
  public final boolean i() {
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public byte[] str() {
    if(size == 0) return Token.EMPTY;
    BaseX.notexpected();
    return null;
  }

  @Override
  public boolean bool() throws QueryException {
    if(size != 0 && !val[0].node()) Err.or(FUNSEQ, this);
    return size != 0;
  }

  @Override
  public Iter iter() {
    return SeqIter.get(val, size);
  }

  @Override
  public boolean eq(final Item it) throws QueryException {
    castErr(it);
    return false;
  }

  @Override
  public int diff(final Item it) {
    BaseX.notimplemented();
    return 0;
  }

  @Override
  public void serialize(final Serializer ser) throws IOException {
    for(int i = 0; i < size; i++) {
      ser.openResult();
      val[i].serialize(ser);
      ser.closeResult();
    }
  }
  
  @Override
  public Return returned(final QueryContext ctx) {
    return Return.SEQ;
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(Token.token("sequence"), SIZE, Token.token(size));
    for(int v = 0; v != Math.min(size, 5); v++) val[v].plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("(");
    for(int v = 0; v != size; v++) {
      sb.append((v != 0 ? ", " : "") + val[v]);
      if(sb.length() > 15 && v + 1 != size) {
        sb.append(", ...");
        break;
      }
    }
    return sb.append(")").toString();
  }
}