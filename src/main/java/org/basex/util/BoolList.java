package org.basex.util;

import java.util.Arrays;

/**
 * This is a simple container for boolean values.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public final class BoolList extends ElementList {
  /** Element container. */
  private boolean[] list;

  /**
   * Default constructor.
   */
  public BoolList() {
    this(CAP);
  }

  /**
   * Constructor, specifying the initial array size.
   * @param c initial size
   */
  public BoolList(final int c) {
    list = new boolean[c];
  }

  /**
   * Adds an element.
   * @param e element to be added
   */
  public void add(final boolean e) {
    if(size == list.length) list = Arrays.copyOf(list, newSize());
    list[size++] = e;
  }

  /**
   * Returns the element at the specified index.
   * @param i index
   * @return element
   */
  public boolean get(final int i) {
    return list[i];
  }

  /**
   * Sets an element at the specified index.
   * @param e element to be set
   * @param i index
   */
  public void set(final boolean e, final int i) {
    if(i >= list.length) list = Arrays.copyOf(list, newSize(i + 1));
    list[i] = e;
    size = Math.max(size, i + 1);
  }

  /**
   * Finishes the int array.
   * @return int array
   */
  public boolean[] toArray() {
    return Arrays.copyOf(list, size);
  }
}
