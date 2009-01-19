package org.basex.gui.view.folder;

import org.basex.data.Data;
import org.basex.data.Nodes;

/**
 * This is an iterator for the folder nodes.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
final class FolderIterator {
  /** Tree visualization reference. */
  private final FolderView view;

  /** Current y position. */
  int y;
  /** Current level. */
  int level;
  /** Current pre value. */
  int pre;

  /** Stack for parent nodes. */
  private final int[] parents = new int[128];
  /** Panel height. */
  private final int height;
  /** Flag for a found context node. */
  private boolean found;
  /** Current context set position. */
  private int cp = -1;
  /** Current parent node. */
  private int par;
  /** Current iterator mode. */
  private int mode;
  /** Temporary level. */
  private int ll;

  /**
   * Default Constructor.
   * @param v view reference
   */
  FolderIterator(final FolderView v) {
    this(v, 0, 0x7FFFFFFF);
  }

  /**
   * Constructor, specifying the start y position and the maximum height.
   * @param v view reference
   * @param yy start y value
   * @param h panel height
   */
  FolderIterator(final FolderView v, final int yy, final int h) {
    height = h;
    view = v;
    view.focusedPos = -1;
    y = yy;
  }

  /**
   * Returns the total view height.
   * @return view height
   */
  int height() {
    while(more());
    return y;
  }

  /**
   * Checks if there are further nodes to iterate.
   * @return true for more data
   */
  boolean more() {
    if(view.opened == null) return false;
    if(!found) {
      view.focusedPos++;
      found = pre == view.gui.focused;
    }
    if(mode == 0) return moreCS();
    y += view.lineH;
    if(y > height) return false;

    final Data data = view.gui.context.data();
    if(data == null) return false;
    final int kind = data.kind(pre);
    return mode == 2 || (kind == Data.ELEM || kind == Data.DOC) &&
      view.opened[pre] ? moreData(data) : moreCS();
  }

  /**
   * Checks if there are further context nodes to check.
   * @return true for more data
   */
  boolean moreCS() {
    final Nodes current = view.gui.context.current();
    if(current == null || ++cp >= current.size) return false;
    par = current.nodes[cp];
    pre = par;
    level = 0;
    ll = 0;
    mode = 1;
    return true;
  }

  /**
   * Checks if there are further data nodes to check.
   * @param data data reference
   * @return true for more data
   */
  boolean moreData(final Data data) {
    level = ll;
    int kind = data.kind(pre);
    final boolean fs = data.fs != null;

    final boolean[] open = view.opened;
    if(open == null) return false;

    pre += open[pre] ? fs ? data.attSize(pre, kind) : 1 : data.size(pre, kind);
    while(pre != data.meta.size) {
      kind = data.kind(pre);
      final int p = data.parent(pre, kind);
      // search current root
      while(p < par && level > 0) par = parents[--level];
      if(p < par) break;
      if(p == par) {
        // store child as new root
        kind = data.kind(par);

        if((kind == Data.ELEM || kind == Data.DOC) && open[pre]) {
          parents[level++] = par;
          par = pre;
          ll = level;
        } else {
          ll = level++;
        }
        mode = 2;
        return true;
      }
      pre += fs ? data.attSize(pre, kind) : 1;
    }
    return moreCS();
  }
}