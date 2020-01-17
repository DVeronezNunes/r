/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;

/**
 *
 */
public class SfwCheckNodeEditor extends AbstractCellEditor implements TreeCellEditor, ActionListener {

    final JTree _tree;
    private SfwCheckNode _node;
    protected JCheckBox _check;
    protected TreeLabel _label;
    protected JPanel _panel;
    private JTabbedPane _tabpane;
    private JComponent editedComponent;

    public SfwCheckNodeEditor(final JTree tree, Object value) {
        this._tree = tree;
        _panel = new JPanel();
        _check = new JCheckBox();
        _label = new TreeLabel();
        _label.setFont(tree.getFont());
        _label.setBorder(null);
        _check.setBorder(null);
        _check.setBackground(null);
        _label.setBackground(null);
        _label.setForeground(UIManager.getColor("Tree.textForeground"));
        _panel.setOpaque(false);
        _panel.add(_check);
        _panel.add(_label);
        _check.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ((JCheckBox)e.getSource()).doClick();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
    }

    public SfwCheckNodeEditor(final JTree tree, Object value, JTabbedPane tabpane) {
        this(tree,value);
        this._tabpane = tabpane;
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

        if (value instanceof SfwCheckNode)
        {
            this._node = (SfwCheckNode) value;
            this._check.setSelected(_node.isValue());
            this._label.setFont(tree.getFont());
            this._label.setText(value.toString());
            this._label.setSelected(true);
            this._check.setToolTipText(value.toString());

            if (leaf) {
              if(this._node.getNodeicon() != null)
                this._label.setIcon(this._node.getNodeicon());
              else
                this._label.setIcon(UIManager.getIcon("Tree.leafIcon"));
            } else if (expanded) {
              this._label.setIcon(UIManager.getIcon("Tree.openIcon"));
            } else {
              this._label.setIcon(UIManager.getIcon("Tree.closedIcon"));
            }

            this._check.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                _node.setValue(_check.isSelected());
                ((DefaultTreeModel)_tree.getModel()).nodeStructureChanged(_node);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

            for(int i=0; i<this._tabpane.getTabCount(); i++)
              {
                  if(this._tabpane.getTitleAt(i).equals(this._node.toString()))
                  {
                      this._tabpane.setSelectedIndex(i);
                  }
              }

            return _panel;
       } else
       {
            this._label.setText(value.toString());
            return _panel;
       }
    }

    public Object getCellEditorValue() {
        return _node;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent)
    {
        if (anEvent instanceof MouseEvent)
        {
//            TreePath tp = this._tree.getPathForLocation(((MouseEvent)anEvent).getX(), ((MouseEvent)anEvent).getY());
//
//            if(tp != null)
//            {
//                SfwCheckNode cn = (SfwCheckNode)tp.getLastPathComponent();
//
//                if(cn != null)
//                {
//                    if(!cn.isLeaf() && ((MouseEvent)anEvent).getClickCount() < 2)
//                        return false;
//                }
//            }
        }
    return true;
    }

    public void actionPerformed(ActionEvent e) {
        editedComponent =(JComponent)e.getSource();
        super.stopCellEditing();
    }

    public class TreeLabel extends JLabel
    {
        boolean isSelected;
        boolean hasFocus;

        public TreeLabel() {
        }

        @Override
        public void setBackground(Color color) {
          if (color instanceof ColorUIResource)
            color = null;
          super.setBackground(color);
        }

        @Override
        public void paint(Graphics g) {
          String str;
          if ((str = getText()) != null) {
            if (0 < str.length()) {
              if (isSelected) {
                g.setColor(UIManager
                    .getColor("Tree.selectionBackground"));
              } else {
                g.setColor(UIManager.getColor("Tree.textBackground"));
              }
              Dimension d = getPreferredSize();
              int imageOffset = 0;
              Icon currentI = getIcon();
              if (currentI != null) {
                imageOffset = currentI.getIconWidth()
                    + Math.max(0, getIconTextGap() - 1);
              }
              g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
                  d.height);
              if (hasFocus) {
                g.setColor(UIManager
                    .getColor("Tree.selectionBorderColor"));
                g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
                    d.height - 1);
              }
            }
          }
          super.paint(g);
        }

        @Override
        public Dimension getPreferredSize() {
          Dimension retDimension = super.getPreferredSize();
          if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3,
                retDimension.height);
          }
          return retDimension;
        }

        public void setSelected(boolean isSelected) {
          this.isSelected = isSelected;
        }

        public void setFocus(boolean hasFocus) {
          this.hasFocus = hasFocus;
        }
    }
}
