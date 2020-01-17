/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript;

import java.util.Hashtable;

/**
 * Classe estática com o profile lido.
 */
public class Profile {

    private static String _client = "";
    private static String _shortname = "";
    private static String _system = "";
    private static String _tns = "";
    private static String _database = "";
    private static String _role = "";
    private static String _usuariosombra = "";
    private static String _senhasombra = "";
    private static boolean _userole = false;
    private static boolean  _isSet = false;
    private static boolean  _isFirstInstall = false;
    private static Hashtable _productsinstall;
    private static Hashtable _productsdefine;

    /**
     *
     * @return
     */
    public static boolean isSet() {
        return _isSet;
    }

    /**
     *
     * @param _isSet
     */
    public static void isSet(boolean _isSet) {
        Profile._isSet = _isSet;
    }
    
    /**
     *
     * @return
     */
    public static boolean isFirstInstall() {
        return _isFirstInstall;
    }

    
    /**
     *
     * @param _isSet
     */
    public static void isFirstInstall(boolean _isFirstInstall) {
        Profile._isFirstInstall = _isFirstInstall;
    }

    /**
     *
     * @return
     */
    public static String get_client() {
        return _client;
    }

    /**
     *
     * @param _client
     */
    public static void set_client(String _client) {
        Profile._client = _client;
    }

    /**
     *
     * @return
     */
    public static String get_database() {
        return _database;
    }

    /**
     *
     * @param _database
     */
    public static void set_database(String _database) {
        Profile._database = _database;
    }

    /**
     *
     * @return
     */
    public static String get_shortname() {
        return _shortname;
    }

    /**
     *
     * @param _shortname
     */
    public static void set_shortname(String _shortname) {
        Profile._shortname = _shortname;
    }

    /**
     *
     * @return
     */
    public static String get_system() {
        return _system;
    }

    /**
     *
     * @param _system
     */
    public static void set_system(String _system) {
        Profile._system = _system;
    }

    /**
     *
     * @return
     */
    public static String get_tns() {
        return _tns;
    }

    /**
     *
     * @param _tns
     */
    public static void set_tns(String _tns) {
        Profile._tns = _tns;
    }

    /**
     *
     * @return
     */
    public static Hashtable get_productsdefine() {
        return _productsdefine;
    }

    /**
     *
     * @param _productsdefine
     */
    public static void set_productsdefine(Hashtable _productsdefine) {
        Profile._productsdefine = _productsdefine;
    }

    /**
     *
     * @return
     */
    public static Hashtable get_productsinstall() {
        return _productsinstall;
    }

    /**
     *
     * @param _productsinstall
     */
    public static void set_productsinstall(Hashtable _productsinstall) {
        Profile._productsinstall = _productsinstall;
    }

    /**
     *
     * @return
     */
    public static String get_role() {
        return _role;
    }

    /**
     *
     * @param _role
     */
    public static void set_role(String _role) {
        Profile._role = _role;
    }

    /**
     *
     * @return
     */
    public static boolean is_userole() {
        return _userole;
    }

    /**
     * 
     * @param _userole
     */
    public static void set_userole(boolean _userole) {
        Profile._userole = _userole;
    }

    /**
     * @return the _usuariosombra
     */
    public static String getUsuariosombra() {
        return _usuariosombra;
    }

    /**
     * @param aUsuariosombra the _usuariosombra to set
     */
    public static void setUsuariosombra(String aUsuariosombra) {
        _usuariosombra = aUsuariosombra;
    }

    /**
     * @return the _senhasombra
     */
    public static String getSenhasombra() {
        return _senhasombra;
    }

    /**
     * @param aSenhasombra the _senhasombra to set
     */
    public static void setSenhasombra(String aSenhasombra) {
        _senhasombra = aSenhasombra;
    }
}
