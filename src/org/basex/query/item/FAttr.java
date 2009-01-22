package org.basex.query.item;

import static org.basex.query.QueryTokens.*;
import java.io.IOException;
import org.basex.data.Serializer;
import org.basex.util.Token;

/**
 * Attribute Node Fragment.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class FAttr extends FNode {
  /** Attribute name. */
  private final QNm name;
  /** Attribute value. */
  private final byte[] val;

  /**
   * Constructor.
   * @param n name
   * @param v value
   * @param p parent
   */
  public FAttr(final QNm n, final byte[] v, final Nod p) {
    super(Type.ATT);
    name = n;
    val = v;
    par = p;
  }

  @Override
  public byte[] str() {
    return val;
  }

  @Override
  public String toString() {
    return Token.string(name()) + "(" + Token.string(name.str()) + "=\"" +
      Token.string(val) + "\")";
  }
  
  @Override
  public QNm qname() {
    return name;
  }
  
  @Override
  public byte[] nname() {
    return name.str();
  }
  
  @Override
  public FAttr copy() {
    return new FAttr(name, val, par);
  }

  @Override
  public void serialize(final Serializer ser) throws IOException {
    ser.attribute(name.str(), val);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.emptyElement(this, NAM, name.str(), VAL, val);
  }
}