/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import sfwinstaladorscript.objects.Client;
import sfwinstaladorscript.objects.Database;
import sfwinstaladorscript.objects.PackageList;
import sfwinstaladorscript.objects.SystemOS;
import sfwinstaladorscript.oracleinstallation.components_interfaces_define.SfwOracleDefineInterface;

/**
 * Classe estática com valores selecionados durante instalação.
 */
public class Install {

    private static Client _client = null;
    private static String _shortname = "";
    private static SystemOS _system = null;
    private static String _tns = null;
    private static Database _database = null;
    private static PackageList _packagelist = null;
    private static sfwinstaladorscript.objects.Package _package = null;
    private static ArrayList _productsdetail = new ArrayList();
    private static ArrayList _productsdetailinstall = new ArrayList();
    private static Hashtable _productpackageinstall = new Hashtable();
    private static ArrayList _productdefine = new ArrayList();
    private static String _rolesfw = "";
    private static ArrayList _product;
    private static List<SfwOracleDefineInterface> defines_interface;
    private static boolean _userole = true;
    private static Locale _default_location;
    private static Locale _us_location;

    /**
     * Retorna lista de produtos que serão efetivamente instalados.
     * @return Lista de detalhes dos produtos.
     */
    public static ArrayList get_productsdetailinstall() {
        return _productsdetailinstall;
    }

    /**
     * Altera lista de produtos que serão efetivamente instalados.
     * @param _productsdetailinstall Lista de detalhes dos produtos.
     */
    public static void set_productsdetailinstall(ArrayList _productsdetailinstall) {
        Install._productsdetailinstall = _productsdetailinstall;
    }

    /**
     * Retorna lista de pacotes de instalação.
     * @return Lista de pacotes.
     */
    public static PackageList get_packagelist() {
        return _packagelist;
    }

    /**
     * Altera lista de pacotes de instalação.
     * @param _packagelist Lista de pacotes.
     */
    public static void set_packagelist(PackageList _packagelist) {
        Install._packagelist = _packagelist;
    }

    /**
     * Busca cliente selecionado na tela de configuração inicial.
     * @return Objeto Cliente
     */
    public static Client get_client() {
        return _client;
    }

    /**
     * Altera cliente selecionado.
     * @param _client Cliente selecionado
     */
    public static void set_client(Client _client) {
        Install._client = _client;
    }

    /**
     * Retorna sistema operacional selecionado na tela de configuração inicial.
     * @return Sistema operacional selecionado.
     */
    public static SystemOS get_system() {
        return _system;
    }

    /**
     * Altera sistema operacional selecionado.
     * @param _system Sistema operacional selecionado.
     */
    public static void set_system(SystemOS _system) {
        Install._system = _system;
    }

    /**
     * Busca sigla do cliente digitada na tela de configuração inicial.
     * @return Sigla do sistema
     */
    public static String get_shortname() {
        return _shortname;
    }

    /**
     * Altera sigla do sistema.
     * @param _shortname String com a sigla.
     */
    public static void set_shortname(String _shortname) {
        Install._shortname = _shortname;
    }

    /**
     * Retorna lista de produtos selecionada para instalação.
     * @return Lista de produtos.
     */
    public static ArrayList get_productsdetail() {
        return _productsdetail;
    }

    /**
     * Altera lista de produtos selecionada para instalação.
     * @param _productsdetail Lista de produtos.
     */
    public static void set_productsdetail(ArrayList _productsdetail) {
        Install._productsdetail = _productsdetail;
    }

    /**
     * Retorna TNS escolhido para instalação dos produtos.
     * @return String de conexão.
     */
    public static String get_tns() {
        return _tns;
    }

    /**
     * Altera TNS escolhido para instalação dos produtos.
     * @param _tns String de conexão.
     */
    public static void set_tns(String _tns) {
        Install._tns = _tns;
    }

    /**
     * Retorna banco de dados escolhido para instalação dos produtos.
     * @return Banco de dados.
     */
    public static Database get_database() {
        return _database;
    }

    /**
     * Altera banco de dados escolhido para instalação dos produtos.
     * @param _database Banco de dados.
     */
    public static void set_database(Database _database) {
        Install._database = _database;
    }

    /**
     * Retorna pacote escolhido na configuração inicial.
     * @return Pacote de instalação.
     */
    public static sfwinstaladorscript.objects.Package get_package() {
        return _package;
    }

    /**
     * Altera pacote escolhida na configuração inicial.
     * @param _package Pacote de instalação.
     */
    public static void set_package(sfwinstaladorscript.objects.Package _package) {
        Install._package = _package;
    }

    /**
     * Mapeamento de pacotes para instalar o produto.
     * @return Mapeamento do pacotes.
     */
    public static Hashtable get_productpackageinstall() {
        return _productpackageinstall;
    }

    /**
     * Lista com usuários de produtos não selecionados para instalação.
     * @return Lista com usuários de produtos.
     */
    public static ArrayList get_productdefine() {
        return _productdefine;
    }

    /**
     * Altera a lista com usuários de produtos não selecionados para instalação.
     * @param _productdefine Lista com usuários de produtos.
     */
    public static void set_productdefine(ArrayList _productdefine) {
        Install._productdefine = _productdefine;
    }

    /**
     * Retorna string com a role Softway para o define.
     * @return Role Softway.
     */
    public static String get_rolesfw() {
        return _rolesfw;
    }

    /**
     * Altera role Softway.
     * @param _rolesfw String com a role Softway.
     */
    public static void set_rolesfw(String _rolesfw) {
        Install._rolesfw = _rolesfw;
    }

    public static boolean is_userole() {
        return _userole;
    }

    public static void set_userole(boolean _userole) {
        Install._userole = _userole;
    }

    /** Retorna todos os produtos disponíveis no instalador
     * @return the _product
     */
    public static ArrayList get_product() {
        return _product;
    }

    /** Altera a lista de produtos disponíveis no instalador
     * @param aProduct the _product to set
     */
    public static void set_product(ArrayList Product) {
        _product = Product;
    }

    /**
     * 
     * @return 
     */
    public static List<SfwOracleDefineInterface> getDefines_interface() {
        return defines_interface;
    }

    /**
     * 
     * @param defines_interface 
     */
    public static void setDefines_interface(List<SfwOracleDefineInterface> defines_interface) {
        Install.defines_interface = defines_interface;
    }

    /**
     * 
     * @return 
     */
    public static Locale get_default_location() {
        return _default_location;
    }

    /**
     * 
     * @param _default_location 
     */
    public static void set_default_location(Locale _default_location) {
        Install._default_location = _default_location;
    }

    /**
     * 
     * @return 
     */
    public static Locale get_us_location() {
        return _us_location;
    }

    /**
     * 
     * @param _us_location 
     */
    public static void set_us_location(Locale _us_location) {
        Install._us_location = _us_location;
    }
}
