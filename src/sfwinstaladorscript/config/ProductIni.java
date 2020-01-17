/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.config;

import sfwinstaladorscript.objects.Product;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Trata arquivo de configuração de produtos Softway.
 */
public class ProductIni {    

    private ArrayList _products;

    public ProductIni(InputStream in) throws IOException
    {
        Set v_set_s;
        String v_string_key;
        String v_string_dependency_name;
        Iterator v_iterator_it;
        Properties v_properties_p;
        Product v_product_current;
        Product v_product_newproduct;
        Enumeration v_enumeration_e;
        Hashtable v_hashtable_mod;
        ArrayList v_arraylist_childs;


        v_hashtable_mod = new Hashtable();
        v_properties_p = new Properties();
        v_properties_p.load(in);
        
        this._products = new ArrayList();

        // monta produto e suas propriedades
        v_set_s = v_properties_p.keySet();
        v_iterator_it = v_set_s.iterator();
        while(v_iterator_it.hasNext())
        {
            v_string_key = (String)v_iterator_it.next();
            
            if(v_string_key.startsWith("PRODUCT"))
            {
              v_product_newproduct = new Product();
              v_product_newproduct.set_name(v_properties_p.getProperty(v_string_key));
              v_product_newproduct.set_label(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_LABEL",""));
              v_product_newproduct.set_folder(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_FOLDER",""));
              v_product_newproduct.set_shortname(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_SHORTNAME",""));
              v_product_newproduct.set_defineprefix(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_DEFINEPREFIX",""));
              v_product_newproduct.set_tablespaceprefix(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_TABLESPACEPREFIX",""));
              v_product_newproduct.set_code(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_CODE",""));
              v_product_newproduct.set_order(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_ORDER",""));
              v_product_newproduct.set_icon(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_ICON",""));
              v_product_newproduct.set_modtype("NONMODULAR");
              v_product_newproduct.set_haschilds(false);

              if(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_INSTALLABLE","").toUpperCase().equals("YES"))
                v_product_newproduct.set_installable(true);
              else
                v_product_newproduct.set_installable(false);

              this._products.add(v_product_newproduct);
            }
        }

        // monta lista de dependencias do produto
        v_iterator_it = this._products.iterator();
        while(v_iterator_it.hasNext())
        {
            v_product_current = (Product) v_iterator_it.next();
            v_arraylist_childs = new ArrayList();

            v_enumeration_e = v_properties_p.propertyNames();
            while(v_enumeration_e.hasMoreElements())
            {
                v_string_key = (String)v_enumeration_e.nextElement();

                if(v_string_key.startsWith(v_product_current.get_name()+"_DEPENDENCY"))
                {
                    v_string_dependency_name = v_properties_p.getProperty(v_string_key);
                    v_product_current.get_dependencies().put(v_string_dependency_name, this.getProductByName(v_string_dependency_name));
                }

                if(v_string_key.startsWith(v_product_current.get_name()+"_MOD_PRODUCT"))
                {
                      v_product_newproduct = new Product();
                      v_product_newproduct.set_name(v_properties_p.getProperty(v_string_key));
                      v_product_newproduct.set_label(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_LABEL",""));
                      v_product_newproduct.set_folder(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_FOLDER",""));
                      v_product_newproduct.set_shortname(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_SHORTNAME",""));
                      v_product_newproduct.set_defineprefix(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_DEFINEPREFIX",""));
                      v_product_newproduct.set_tablespaceprefix(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_TABLESPACEPREFIX",""));
                      v_product_newproduct.set_code(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_CODE",""));
                      v_product_newproduct.set_order(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_ORDER",""));
                      v_product_newproduct.set_icon(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_ICON",""));
                      v_product_newproduct.set_modtype(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_MODTYPE",""));
                      v_product_newproduct.set_father(v_product_current);
                      v_product_newproduct.set_haschilds(false);
                      v_product_current.set_haschilds(true);
                      v_product_current.get_childs().add(v_product_newproduct);

                      if(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_INSTALLABLE","").toUpperCase().equals("YES"))
                        v_product_newproduct.set_installable(true);
                      else
                        v_product_newproduct.set_installable(false);

                      v_arraylist_childs.add(v_product_newproduct);
                }
            }

            v_hashtable_mod.put(v_product_current, v_arraylist_childs);
        }

        Enumeration v_enum_keys = v_hashtable_mod.keys();
        while(v_enum_keys.hasMoreElements())
        {
            v_product_current = (Product) v_enum_keys.nextElement();
            this._products.addAll(this._products.indexOf(v_product_current),(ArrayList)v_hashtable_mod.get(v_product_current));

        }
    }

    /**
     * Retorna lista de produtos Softway.
     * @return Lista de produtos.
     */
    public ArrayList get_products() {
        return _products;
    }

    /**
     * Retorna um produto pelo nome.
     */
    private Product getProductByName(String name)
    {
        Product v_product_current;
        Iterator v_iterator_it;

        v_iterator_it = this._products.iterator();
        while(v_iterator_it.hasNext())
        {
            v_product_current = (Product)v_iterator_it.next();

            if(v_product_current.get_name().equals(name))
                return v_product_current;
        }

        return null;
    }
}
