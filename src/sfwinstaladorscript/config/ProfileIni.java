/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import sfwinstaladorscript.Profile;
import sfwinstaladorscript.objects.ProductProfileDefine;
import sfwinstaladorscript.objects.ProductProfileInstall;

/**
 * Trata arquivo de configuração de produtos Softway.
 */
public class ProfileIni {

    private Hashtable _productsinstall;
    private Hashtable _productsdefine;

    public ProfileIni(InputStream in) throws IOException {
        Set v_set_s;
        String v_string_key;
        String v_string_productname;
        Iterator v_iterator_it;
        Properties v_properties_p;
        ProductProfileDefine v_productprofiledefine_new;
        ProductProfileInstall v_productprofileinstall_new;

        v_properties_p = new Properties();
        v_properties_p.load(in);

        this._productsinstall = new Hashtable();
        this._productsdefine = new Hashtable();

        // monta produto e suas propriedades
        v_set_s = v_properties_p.keySet();
        v_iterator_it = v_set_s.iterator();
        while (v_iterator_it.hasNext()) {
            v_string_key = (String) v_iterator_it.next();

            if (v_string_key.startsWith("PRODUCT_INSTALL")) {
                v_productprofileinstall_new = new ProductProfileInstall();
                v_string_productname = v_properties_p.getProperty(v_string_key);

                v_productprofileinstall_new.set_name(v_string_productname);
                v_productprofileinstall_new.set_user(v_properties_p.getProperty(v_string_productname + "_USER"));
                v_productprofileinstall_new.set_pass(v_properties_p.getProperty(v_string_productname + "_PASS"));
                v_productprofileinstall_new.set_userSombra(v_properties_p.getProperty(v_string_productname + "_USERSOMBRA"));
                v_productprofileinstall_new.set_passSombra(v_properties_p.getProperty(v_string_productname + "_PASSSOMBRA"));
                v_productprofileinstall_new.set_tbl_data_4k(v_properties_p.getProperty(v_string_productname + "_TBL_DATA_4K"));
                v_productprofileinstall_new.set_tbl_data_64k(v_properties_p.getProperty(v_string_productname + "_TBL_DATA_64K"));
                v_productprofileinstall_new.set_tbl_data_128k(v_properties_p.getProperty(v_string_productname + "_TBL_DATA_128K"));
                v_productprofileinstall_new.set_tbl_data_512k(v_properties_p.getProperty(v_string_productname + "_TBL_DATA_512K"));
                v_productprofileinstall_new.set_tbl_data_1m(v_properties_p.getProperty(v_string_productname + "_TBL_DATA_1M"));
                v_productprofileinstall_new.set_tbl_index_4k(v_properties_p.getProperty(v_string_productname + "_TBL_INDEX_4K"));
                v_productprofileinstall_new.set_tbl_index_64k(v_properties_p.getProperty(v_string_productname + "_TBL_INDEX_64K"));
                v_productprofileinstall_new.set_tbl_index_128k(v_properties_p.getProperty(v_string_productname + "_TBL_INDEX_128K"));
                v_productprofileinstall_new.set_tbl_index_512k(v_properties_p.getProperty(v_string_productname + "_TBL_INDEX_512K"));
                v_productprofileinstall_new.set_tbl_index_1m(v_properties_p.getProperty(v_string_productname + "_TBL_INDEX_1M"));

                this._productsinstall.put(v_string_productname, v_productprofileinstall_new);
            } else if (v_string_key.startsWith("PRODUCT_DEFINE")) {
                v_productprofiledefine_new = new ProductProfileDefine();
                v_string_productname = v_properties_p.getProperty(v_string_key);

                v_productprofiledefine_new.set_name(v_string_productname);
                v_productprofiledefine_new.set_user(v_properties_p.getProperty(v_string_productname + "_USER"));
                v_productprofiledefine_new.set_pass(v_properties_p.getProperty(v_string_productname + "_PASS"));

                this._productsdefine.put(v_string_productname, v_productprofiledefine_new);
            } else if (v_string_key.equals("OS")) {
                Profile.set_system(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("DB")) {
                Profile.set_database(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("CLIENT")) {
                Profile.set_client(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("CLIENT_SHORT")) {
                Profile.set_shortname(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("TNS")) {
                Profile.set_tns(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("CHECKROLE")) {
                if (v_properties_p.getProperty(v_string_key).equals("CHECKED")) {
                    Profile.set_userole(true);
                } else {
                    Profile.set_userole(false);
                }
            } else if (v_string_key.equals("ROLE")) {
                Profile.set_role(v_properties_p.getProperty(v_string_key));
            } else if (v_string_key.equals("USUARIOSOMBRA")) {
                Profile.setUsuariosombra(v_properties_p.getProperty(v_string_key));
            }else if (v_string_key.equals("SENHASOMBRA")) {
                Profile.setSenhasombra(v_properties_p.getProperty(v_string_key));
            }
        }

        Profile.set_productsinstall(this.get_productsinstall());
        Profile.set_productsdefine(this.get_productsdefine());
        Profile.isSet(true);
    }

    /**
     * Retorna lista de produtos de instalacao Softway.
     * @return Lista de produtos.
     */
    public Hashtable get_productsinstall() {
        return _productsinstall;
    }

    /**
     * Retorna lista de produtos de define Softway.
     * @return Lista de produtos.
     */
    public Hashtable get_productsdefine() {
        return _productsdefine;
    }
}
