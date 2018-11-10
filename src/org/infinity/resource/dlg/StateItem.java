// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2018 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource.dlg;

import java.util.ArrayList;
import static java.util.Collections.enumeration;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import org.infinity.gui.BrowserMenuBar;

import org.infinity.icon.Icons;

/** Encapsulates a dialog state entry. */
final class StateItem extends ItemBase implements Iterable<TransitionItem>
{
  private static final ImageIcon ICON = Icons.getIcon(Icons.ICON_STOP_16);

  /** Tree item that represent visual parent of this state in the tree. */
  private final StateOwnerItem parent;
  /** Item to which need go to in break cycles tree view mode. */
  private final StateItem main;
  /** Items that represents transition tree nodes from this state. */
  ArrayList<TransitionItem> trans;

  private final State state;

  public StateItem(State state, StateOwnerItem parent, StateItem main)
  {
    this.parent = parent;
    this.main = main;
    this.state = state;
  }

  public State getState()
  {
    return state;
  }

  @Override
  public StateItem getMain() { return main; }

  @Override
  public DlgResource getDialog() { return (DlgResource)state.getParent(); }

  @Override
  public Icon getIcon() { return ICON; }

  //<editor-fold defaultstate="collapsed" desc="TreeNode">
  @Override
  public TransitionItem getChildAt(int childIndex)
  {
    return getAllowsChildren() ? trans.get(childIndex) : null;
  }

  @Override
  public int getChildCount() { return getAllowsChildren() ? trans.size() : 0; }

  @Override
  public ItemBase getParent() { return parent; }

  @Override
  public int getIndex(TreeNode node)
  {
    return getAllowsChildren() ? trans.indexOf(node) : -1;
  }

  @Override
  public boolean getAllowsChildren()
  {
    return main == null || !BrowserMenuBar.getInstance().breakCyclesInDialogs();
  }

  @Override
  public boolean isLeaf() { return getAllowsChildren() ? trans.isEmpty() : true; }

  @Override
  public Enumeration<? extends TransitionItem> children() { return enumeration(trans); }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Iterable">
  @Override
  public Iterator<TransitionItem> iterator() { return trans.iterator(); }
  //</editor-fold>

  @Override
  public String toString() { return getText(state); }
}
