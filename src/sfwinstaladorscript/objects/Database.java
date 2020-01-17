/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;

/**
 * Representa um banco de dados suportado.
 */
public class Database implements SfwComboBoxItem {

    /**
     * Nome do banco de dados.
     */
    private String _name;
    /**
     * Label do banco de dados.
     */
    private String _label;
    /**
     * Classe do banco de dados.
     */
    private String _class;
    /**
     * Tela que será chamada no fluxo do Wizard.
     */
    private String _flowwizard;
    /**
     * Caminho da pasta JDBC (Oracle)
     */
    private String _jdbcpath;
    /**
     * Arquivo ojdbc14.jar da pasta JDBC (Oracle)
     */
    private File v_file_oracle_jdbc;
    /**
     * Arquivo ojdbc14.jar da pasta lib do Instalador de Scripts.
     */
    private File v_file_lib_jdbc;

    /**
     * Get the value of _jdbcpath
     *
     * @return the value of _jdbcpath
     */
    public String get_jdbcpath() {
        return _jdbcpath;
    }

    /**
     * Set the value of _jdbcpath
     *
     * @param _jdbcpath new value of _jdbcpath
     */
    public void set_jdbcpath(String _jdbcpath) {
        this._jdbcpath = _jdbcpath;
    }

    /**
     * Retorna nome do sistema operacional.
     * @return Nome do OS.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera nome do sistema operacional.
     * @param _name Nome do OS.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Retorna nome da classe do banco de dados.
     * @return Nome da classe do banco de dados.
     */
    public String get_class() {
        return _class;
    }

    /**
     * Altera nome da classe do banco de dados.
     * @param _class Nome da classe do banco de dados.
     */
    public void set_class(String _class) {
        this._class = _class;
    }

    /**
     * Descrição para o SfwComboBox.
     * @return Nome do banco de dados.
     */
    public String get_description() {
        return this.get_label();
    }

    /**
     * Retorna o pacote com fluxo do wizard para este banco de dados.
     * @return String com o pacote.
     */
    public String get_flowwizard() {
        return _flowwizard;
    }

    /**
     * Altera o pacote com fluxo do wizard para este banco de dados.
     * @param _wizardpackage String com o pacote.
     */
    public void set_flowwizard(String _flowwizard) {
        this._flowwizard = _flowwizard;
    }

    /**
     * Busca chave do Oracle Home no registro do Windows.
     * @param regpath Caminho a ser procurado.
     * @return String com o valor do ORACLE_HOME ou null se não encontrar.
     */

    /**Retorna o Label do banco de dados.
     * @return Label do banco de dados
     */
    public String get_label() {
        return _label;
    }

    /** Altera o label do banco de dados.
     * @param _label label do banco de dados.
     */
    public void set_label(String label) {
        this._label = label;
    }

}
