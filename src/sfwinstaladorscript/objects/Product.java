/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;
import sfwinstaladorscript.oracleinstallation.SfwOracleProductDetail;
import sfwinstaladorscript.oracleinstallation.components.SfwOracleDefineUser;

/**
 * Representa um produto Softway.
 */
public class Product implements SfwComboBoxItem {

    private String _name;
    private String _label;
    private String _folder;
    private String _shortname;
    private String _defineprefix;
    private String _tablespaceprefix;
    private String _code;
    private String _order;
    private String _modtype;
    private Product _father;
    private ArrayList _childs;
    private Hashtable _dependencies;
    private boolean _installable;
    private boolean _haschilds;
    private String _icon;
    private String _selectedversion;
    private String _installedversion;
    private boolean _EBS;
    private boolean _Integration;

    public Product() {
        this.setInstalledversion("00 00 00");
        this.setSelectedversion("");
        this._dependencies = new Hashtable();
        this._childs = new ArrayList();
    }

    /**
     * Retorna descrição do produto para visualização.
     * @return String com a descrição do produto.
     */
    public String get_description() {
        return this._label;
    }

    /**
     * @return the _selectedversion
     */
    public String getSelectedversion() {
        return _selectedversion;
    }

    /**
     * @param selectedversion the _selectedversion to set
     */
    public void setSelectedversion(String selectedversion) {
        this._selectedversion = selectedversion;
    }

    /**
     * @return the _installedversion
     */
    public String getInstalledversion() {
        return _installedversion;
    }

    /**
     * @param installedversion the _installedversion to set
     */
    public void setInstalledversion(String installedversion) {
        this._installedversion = installedversion;
    }

    /**
     * Comparador para a classe Produto.
     */
    public static class ProductComparator
            implements Comparator {

        public int compare(Object element1,
                Object element2) {
            Product v_product_v1 = (Product) element1;
            Product v_vproduct_v2 = (Product) element2;
            return v_product_v1.compare(v_vproduct_v2);
        }
    }

    /**
     * Retorna c�digo do produto.
     * @return c�digo do produto.
     */
    public String get_code() {
        return _code;
    }

    /**
     * Altera o código do produto.
     * @param _code Código do produto.
     */
    public void set_code(String _code) {
        this._code = _code;
    }

    /**
     * Retorna pasta de scripts do produto.
     * @return Nome da pasta de scripts do produto.
     */
    public String get_folder() {
        return _folder;
    }

    /**
     * Altera pasta de scripts do produto.
     * @param _folder Nome da pasta de scripts do produto.
     */
    public void set_folder(String _folder) {
        this._folder = _folder;
    }

    /**
     * Retorna label do produto.
     * @return Label do produto.
     */
    public String get_label() {
        return _label;
    }

    /**
     * Altera label do produto.
     * @param _label Label do produto.
     */
    public void set_label(String _label) {
        this._label = _label;
    }

    /**
     * Retorna nome do produto.
     * @return Nome do produto.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera nome do produto.
     * @param _name Nome do produto.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Retorna sigla do produto.
     * @return Sigla do produto.
     */
    public String get_shortname() {
        return _shortname;
    }

    /**
     * Altera sigla do produto.
     * @param _shortname Sigla do produto.
     */
    public void set_shortname(String _shortname) {
        this._shortname = _shortname;
    }

    /**
     * Retorna identificador para montagem do define.
     * @return Identificador do define.
     */
    public String get_defineprefix() {
        return _defineprefix;
    }

    /**
     * Altera identificador para montagem do define.
     * @param _tablespace Identificador do define.
     */
    public void set_defineprefix(String _defineprefix) {
        this._defineprefix = _defineprefix;
    }

    /**
     * Retorna identificador para montagem da tablespace.
     * @return Identificador tablespace.
     */
    public String get_tablespaceprefix() {
        return _tablespaceprefix;
    }

    /**
     * Altera identificador para montagem da tablespace.
     * @param _tablespace Identificador tablespace.
     */
    public void set_tablespaceprefix(String _tablespaceprefix) {
        this._tablespaceprefix = _tablespaceprefix;
    }

    /**
     * Retorna número de ordem do produto.
     * @return Número de ordem.
     */
    public String get_order() {
        return _order;
    }

    /**
     * Altera n�mero de ordem do produto.
     * @param _order n�mero de ordem.
     */
    public void set_order(String _order) {
        this._order = _order;
    }

    /**
     * Retorna mapeamento de dependencia de produtos.
     * @return Hashtable com mapeamento.
     */
    public Hashtable get_dependencies() {
        return _dependencies;
    }

    /**
     * Get the value of _installable
     *
     * @return the value of _installable
     */
    public boolean is_installable() {
        return _installable;
    }

    /**
     * Set the value of _installable
     *
     * @param _installable new value of _installable
     */
    public void set_installable(boolean _installable) {
        this._installable = _installable;
    }

    public boolean isEBS() {
        return _EBS;
    }

    public void setEBS(boolean _EBS) {
        this._EBS = _EBS;
    }

    public boolean isIntegration() {
        return _Integration;
    }

    public void setIntegration(boolean _Integration) {
        this._Integration = _Integration;
    }

    /**
     * Compara dois produtos
     * @param p Produto a ser comparado;
     * @return 0 - iguais, 1- maior, -1 menor
     */
    public int compare(Product p) {
        if (Integer.parseInt(this._order) > Integer.parseInt(p.get_order())) {
            return 1;
        }

        if (Integer.parseInt(this._order) < Integer.parseInt(p.get_order())) {
            return -1;
        }

        return 0;
    }

    /**
     * Função que verifica se determinado produto existe na base (SFW_SISTEMA_VERSAO).
     * @param codsistema Código do sistema.
     * @return TRUE- se existe FALSE- não existe
     */
    public static boolean productExists(String user, String pass, String codsistema) {
        boolean v_boolean_return = false;
        ResultSet v_resultset_result;
        OracleConnection v_oracleconnection_conn;
        Iterator v_iterator_productdetail;
        SfwOracleProductDetail v_sfworacleproductdetail_current;

        v_oracleconnection_conn = new OracleConnection();

        try {
            v_boolean_return = false;

            // verifica se produto está entre os que serão instalados
            v_iterator_productdetail = Install.get_productsdetail().iterator();
            while (v_iterator_productdetail.hasNext()) {
                v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_productdetail.next();

                if (v_sfworacleproductdetail_current.getProduct().get_code().equals(codsistema)) {
                    v_boolean_return = true;
                }
            }

            // se n�o tiver, verifica a exist�ncia do produto na SFW_SISTEMA_VERSAO
            if (!v_boolean_return) {
                Product.connectToSoftcomex(user, pass, v_oracleconnection_conn);

                v_resultset_result = v_oracleconnection_conn.Query("select count(*) as COUNT_EXISTS from SFW_SISTEMA_VERSAO where COD_SISTEMA = " + codsistema + " and VALIDO = 'S'");
                while (v_resultset_result.next()) {
                    if (v_resultset_result.getInt("COUNT_EXISTS") > 0) {
                        v_boolean_return = true;
                    }
                }
                v_resultset_result.close();
                v_oracleconnection_conn.Close();
            }

            return v_boolean_return;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Tenta conectar no Softcomex ou conecta no usuário passado por parâmetro.
     * @param user
     * @param pass
     * @param oracleconn
     * @throws java.sql.SQLException
     */
    public static void connectToSoftcomex(String user, String pass, OracleConnection oracleconn) throws SQLException {
        Iterator v_iterator_products;
        SfwOracleProductDetail v_sfworacleproductdetail_current;
        SfwOracleDefineUser v_sfworacledefineuser_current;
        String v_string_user = "", v_string_pass = "";

        v_iterator_products = Install.get_productsdetail().iterator();
        while (v_iterator_products.hasNext()) {
            v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_products.next();

            if (v_sfworacleproductdetail_current.getProduct().get_name().equals("SOFTCOMEX")) {
                v_string_user = v_sfworacleproductdetail_current.getUser();
                v_string_pass = v_sfworacleproductdetail_current.getPassword();
                break;
            }
        }

        if (v_string_user.equals("")) {
            v_iterator_products = Install.get_productdefine().iterator();
            while (v_iterator_products.hasNext()) {
                v_sfworacledefineuser_current = (SfwOracleDefineUser) v_iterator_products.next();

                if (v_sfworacledefineuser_current.getProduct().get_name().equals("SOFTCOMEX")) {
                    v_string_user = v_sfworacledefineuser_current.getUser();
                    v_string_pass = v_sfworacledefineuser_current.getPassword();
                    break;
                }
            }
        }

        if (v_string_user.equals("")) {
            v_string_user = user;
            v_string_pass = pass;
        }

        oracleconn.set_tns(Install.get_tns());
        oracleconn.set_username(v_string_user);
        oracleconn.set_password(v_string_pass);
        oracleconn.Connect();
    }

    /**
     * 
     * @return 
     */
    public Product get_father() {
        return _father;
    }

    /**
     * 
     */
    public void set_father(Product _father) {
        this._father = _father;
    }

    /**
     * 
     * @return 
     */
    public String get_modtype() {
        return _modtype;
    }

    /**
     * 
     * @param _modtype 
     */
    public void set_modtype(String _modtype) {
        this._modtype = _modtype;
    }

    /**
     * 
     * @return 
     */
    public boolean haschilds() {
        return _haschilds;
    }

    /**
     * 
     * @param _haschilds 
     */
    public void set_haschilds(boolean _haschilds) {
        this._haschilds = _haschilds;
    }

    /**
     * 
     * @return 
     */
    public ArrayList get_childs() {
        return _childs;
    }

    /**
     * 
     * @param _childs 
     */
    public void set_childs(ArrayList _childs) {
        this._childs = _childs;
    }

    /**
     * 
     * @return 
     */
    public String get_icon() {
        return _icon;
    }

    /**
     * 
     * @param _icon 
     */
    public void set_icon(String _icon) {
        this._icon = _icon;
    }
}