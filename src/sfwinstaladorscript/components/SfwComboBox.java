/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;

/**
 * Componente ComboBox extendido.
 */
public class SfwComboBox extends javax.swing.JComboBox implements TableCellRenderer {

    private ArrayList _list;

    public SfwComboBox() {
    }

    public SfwComboBox(String[] items) {
        super(items);
    }

    public void bind(ArrayList list) {
        Iterator v_iterator_it;
        SfwComboBoxItem v_sfwcomboboxitem_ci;

        this._list = list;
        v_iterator_it = this._list.iterator();
        while (v_iterator_it.hasNext()) {
            v_sfwcomboboxitem_ci = (SfwComboBoxItem) v_iterator_it.next();
            this.addItem(v_sfwcomboboxitem_ci.get_description());
        }
    }

    public void bind(ArrayList list, Comparator c) {
        Iterator v_iterator_it;
        SfwComboBoxItem v_sfwcomboboxitem_ci;

        this._list = list;
        Collections.sort(this._list, c);
        v_iterator_it = this._list.iterator();
        while (v_iterator_it.hasNext()) {
            v_sfwcomboboxitem_ci = (SfwComboBoxItem) v_iterator_it.next();
            this.addItem(v_sfwcomboboxitem_ci.get_description());
        }
    }

    public Object getBindedSelectedItem() {
        Iterator v_iterator_it;
        SfwComboBoxItem v_sfwcomboboxitem_obj = null;

        if (this._list != null) {
            v_iterator_it = this._list.iterator();
            while (v_iterator_it.hasNext()) {
                v_sfwcomboboxitem_obj = (SfwComboBoxItem) v_iterator_it.next();
                if (v_sfwcomboboxitem_obj.get_description().equals(this.getSelectedItem())) {
                    break;
                } else {
                    v_sfwcomboboxitem_obj = null;
                }
            }
        }
        return v_sfwcomboboxitem_obj;
    }

    public void setSelectedItemInsensitive(String description) {
        String v_string_objstr;

        for (int i = 0; i < this.getItemCount(); i++) {
            v_string_objstr = (String) this.getItemAt(i);

            if (v_string_objstr.toUpperCase().equals(description.toUpperCase())) {
                this.setSelectedItem(v_string_objstr);
            }
        }

    }

    public ArrayList get_list() {
        return _list;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            // Select the current value
            setSelectedItem(value);
            return this;
        }

    }
}
