/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import sfwinstaladorscript.objects.ExportInt;

/**
 *
 * @author jopaulo
 */
public class ExInt {

    private Vector _exints;

    public ExInt(InputStream in) throws IOException {

        Set v_set_s;
        String v_string_key;
        Iterator v_iterator_it;
        Properties v_properties_p;
        ExportInt v_exint_export;
        Hashtable v_hashtable_mod;


        v_hashtable_mod = new Hashtable();
        v_properties_p = new Properties();
        v_properties_p.load(in);

        this._exints = new Vector();

        // monta os Exports Internacionais existentes e suas propriedades
        v_set_s = v_properties_p.keySet();
        v_iterator_it = v_set_s.iterator();
        while (v_iterator_it.hasNext()) {
            v_string_key = (String) v_iterator_it.next();

            if (v_string_key.startsWith("EXPORT")) {
                v_exint_export = new ExportInt();
                v_exint_export.setName(v_properties_p.getProperty(v_string_key));
                v_exint_export.setCod_sistema(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key) + "_CODSISTEMA", ""));
                v_exint_export.setLabel(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key) + "_LABEL", ""));

                this._exints.add(v_exint_export);
            }
        }

        Enumeration v_enum_keys = v_hashtable_mod.keys();
        while (v_enum_keys.hasMoreElements()) {
            v_exint_export = (ExportInt) v_enum_keys.nextElement();
            this._exints.addAll(this._exints.indexOf(v_exint_export), (ArrayList) v_hashtable_mod.get(v_exint_export));
        }
    }

    /**
     * Retorna lista de produtos Softway.
     * @return Lista de produtos.
     */
    public Vector get_exints() {
        return _exints;
    }

    public Vector getListExportInt() {

        ExportInt v_exportint_current;
        Iterator v_iterator_it;
        Vector v_vector_exints;

        try {
            v_vector_exints = new Vector();
            v_iterator_it = this._exints.iterator();
            while (v_iterator_it.hasNext()) {
                v_exportint_current = (ExportInt) v_iterator_it.next();
                v_vector_exints.add(v_exportint_current.getLabel());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Collections.sort(v_vector_exints);
        return v_vector_exints;

    }

    public String getCodeByName(String name) {
        ExportInt v_product_current;
        Iterator v_iterator_it;

        v_iterator_it = this._exints.iterator();
        while (v_iterator_it.hasNext()) {
            v_product_current = (ExportInt) v_iterator_it.next();

            if (v_product_current.getLabel().equals(name)) {
                return v_product_current.getCod_sistema();
            }
        }
        return null;
    }
}
