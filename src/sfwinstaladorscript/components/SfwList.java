/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import sfwinstaladorscript.interfaces.SfwListItem;

/**
 * Componente List extendido.
 */
public class SfwList extends javax.swing.JList {

        private ArrayList _list;

        public void bind(ArrayList list)
        {
            this._list = list;

            this.setListData(this._list.toArray());

            this.setCellRenderer(new ListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component v_component_c = null;
                  if (value == null) {
                    v_component_c = new JLabel("(null)");
                  } else if (((SfwListItem)value).get_description() instanceof Component) {
                    v_component_c = (Component) value;
                  } else {
                    v_component_c = new JLabel((String)((SfwListItem)value).get_description());
                  }

                  if (isSelected) {
                    v_component_c.setBackground(list.getSelectionBackground());
                    v_component_c.setForeground(list.getSelectionForeground());
                  } else {
                    v_component_c.setBackground(list.getBackground());
                    v_component_c.setForeground(list.getForeground());
                  }

                  if (v_component_c instanceof JComponent) {
                    ((JComponent) v_component_c).setOpaque(true);
                  }
                  return v_component_c;
            }
        });

        this.setModel(new javax.swing.AbstractListModel() {
             public int getSize() { return _list.size(); }
            public Object getElementAt(int i) { return _list.get(i); }
        });

        }

        public void bind(ArrayList list, Comparator c)
        {
            Collections.sort(list, c);
            this.bind(list);
        }
}
