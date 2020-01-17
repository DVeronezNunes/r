/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.components;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import sfwinstaladorscript.interfaces.SfwCheckNodeValueChangedListener;

/**
 *
 * @author clmonaco
 */
public class SfwCheckNode extends DefaultMutableTreeNode implements Serializable
{
  private transient Vector listeners;

  public final static int SINGLE_SELECTION = 0;

  public final static int DIG_IN_SELECTION = 4;

  protected int selectionMode;

  protected boolean isSelected;

  private boolean _value;

  private boolean _changewithfathertrue;

  private boolean _changewithfatherfalse;

  private Icon _nodeicon;

  public SfwCheckNode() {
    this(null);
    this._changewithfatherfalse = false;
    this._changewithfathertrue = false;
  }

  public SfwCheckNode(Object userObject) {
    this(userObject, true, false);
  }

  public SfwCheckNode(Object userObject, boolean allowsChildren,
      boolean isSelected) {
    super(userObject, allowsChildren);
    this.isSelected = isSelected;
    setSelectionMode(DIG_IN_SELECTION);
    this._changewithfatherfalse = false;
    this._changewithfathertrue = false;
  }

  public void setSelectionMode(int mode) {
    selectionMode = mode;
  }

  public int getSelectionMode() {
    return selectionMode;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;

    if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
      Enumeration e = children.elements();
      while (e.hasMoreElements()) {
        SfwCheckNode node = (SfwCheckNode) e.nextElement();
        node.setSelected(isSelected);
      }
    }

  }

  public boolean isSelected() {
    return isSelected;
  }

  public boolean isValue() {
        return _value;
    }

  public void setValue(boolean _value) {
        SfwCheckNode v_sfwchecknode_child;

        if(this._value != _value)
            fireSfwCheckNodeValueChanged();

        this._value = _value;

        for(int i=0; i<this.getChildCount(); i++)
        {
            v_sfwchecknode_child = (SfwCheckNode) this.getChildAt(i);

            if((this._value && !v_sfwchecknode_child.isValue() && v_sfwchecknode_child.isChangewithfathertrue())
              || (!this._value && v_sfwchecknode_child.isValue() && v_sfwchecknode_child.isChangewithfatherfalse()))
                v_sfwchecknode_child.setValue(_value);
        }
  }

  public boolean isChangewithfatherfalse() {
        return _changewithfatherfalse;
    }

  public void setChangewithfatherfalse(boolean _changewithfatherfalse) {
        this._changewithfatherfalse = _changewithfatherfalse;
    }

  public boolean isChangewithfathertrue() {
        return _changewithfathertrue;
    }

  public void setChangewithfathertrue(boolean _changewithfathertrue) {
        this._changewithfathertrue = _changewithfathertrue;
    }

  synchronized public void addSfwCheckNodeValueChangedListener(SfwCheckNodeValueChangedListener l) {
    if (listeners == null)
      listeners = new Vector();
    listeners.addElement(l);
  }

  protected void fireSfwCheckNodeValueChanged() {
    // if we have no listeners, do nothing...
    if (listeners != null && !listeners.isEmpty()) {
      // create the event object to send
      SfwCheckNodeValueChangedEvent event =
        new SfwCheckNodeValueChangedEvent(this);

      // make a copy of the listener list in case
      //   anyone adds/removes listeners
      Vector targets;
      synchronized (this) {
        targets = (Vector) listeners.clone();
      }

      // walk through the listener list and
      //   call the sunMoved method in each
      Enumeration e = targets.elements();
      while (e.hasMoreElements()) {
        SfwCheckNodeValueChangedListener l = (SfwCheckNodeValueChangedListener) e.nextElement();
        l.CheckNodeValueChanged(event);
      }
    }
  }

  public Icon getNodeicon() {
        return _nodeicon;
    }

  public void setNodeicon(Icon _nodeicon) {
        this._nodeicon = _nodeicon;
    }

}
