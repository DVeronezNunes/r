/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import oracle.jdbc.OracleDriver;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Profile;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.oracleinstallation.SfwOracleProductDetail;
import sfwinstaladorscript.oracleinstallation.components.SfwOracleDefineUser;

/**
 * Classe de conexao com o banco de dados Oracle.
 */
public class OracleConnection extends SfwConnection {

    private Connection _conn;
    private String _tag;
    private String _version;
    private ResultSet v_resultset_result;
    private PreparedStatement v_prepared_statement_st;

    public String get_tag() {
        return _tag;
    }

    public String get_version() {
        return _version;
    }

    private void set_version() throws SQLException {
        CallableStatement v_callablestatement_cst;

        v_callablestatement_cst = this._conn.prepareCall("begin dbms_utility.db_version(?,?); end;");
        v_callablestatement_cst.registerOutParameter(1, java.sql.Types.VARCHAR);
        v_callablestatement_cst.registerOutParameter(2, java.sql.Types.VARCHAR);
        v_callablestatement_cst.executeQuery();

        this._version = v_callablestatement_cst.getString(1);

        v_callablestatement_cst.close();
    }

    private void set_tag() throws SQLException {
        String v_string_oracle;

        v_resultset_result = this.Query("select * from V$VERSION where BANNER like '%Oracle%' and rownum=1");

        while (v_resultset_result.next()) {
            v_string_oracle = v_resultset_result.getString("BANNER").toUpperCase();

            if (v_string_oracle.contains("12C")) {
                this._tag = "11g";
            }
            if (v_string_oracle.contains("11G")) {
                this._tag = "11g";
            }
            if (v_string_oracle.contains("10G")) {
                this._tag = "10g";
            }
            if (v_string_oracle.contains("9I")) {
                this._tag = "9i";
            }
            if (v_string_oracle.contains("8I")) {
                this._tag = "8i";
            }
        }
        v_resultset_result.close();
    }

    /**
     *
     */
    public void CloseResultSet() {
        try {
            if (this.v_resultset_result != null) {
                this.v_resultset_result.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Connection Connect() throws SQLException {

        Properties v_properties_prop;

        v_properties_prop = new Properties();
        v_properties_prop.put("user", this._username);
        v_properties_prop.put("password", this._password);

        Locale.setDefault(Install.get_us_location());

        DriverManager.registerDriver(new OracleDriver());

        Utils.ProxyServer(false);
        _conn = DriverManager.getConnection(getURL(), get_username(), get_password());
        Utils.ProxyServer(true);

        Locale.setDefault(Install.get_default_location());

        this.set_tag();
        this.set_version();

        return _conn;

    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Connection ConnectAux() throws SQLException {

        Properties v_properties_prop;

        v_properties_prop = new Properties();
        v_properties_prop.put("user", this._username);
        v_properties_prop.put("password", this._password);

        Locale.setDefault(Install.get_us_location());

        DriverManager.registerDriver(new OracleDriver());

        Utils.ProxyServer(false);
        _conn = DriverManager.getConnection(getURL(), get_username(), get_password());
        Utils.ProxyServer(true);

        Locale.setDefault(Install.get_default_location());

        return _conn;

    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public Connection ConnectAuxSemAutoCommit() throws SQLException {

        Properties v_properties_prop;

        v_properties_prop = new Properties();
        v_properties_prop.put("user", this._username);
        v_properties_prop.put("password", this._password);

        Locale.setDefault(Install.get_us_location());

        DriverManager.registerDriver(new OracleDriver());

        Utils.ProxyServer(false);
        _conn = DriverManager.getConnection(getURL(), get_username(), get_password());
        Utils.ProxyServer(true);

        Locale.setDefault(Install.get_default_location());

        _conn.setAutoCommit(false);

        return _conn;

    }

    public void commit() throws SQLException {
        this._conn.commit();
    }

    public void rollback() throws SQLException {
        this._conn.rollback();
    }

    /**
     * Fecha conexão com Oracle
     * 
     * @throws SQLException
     */
    public void CloseStatement() {
        try {
            if (v_prepared_statement_st != null) {
                this.v_prepared_statement_st.close();
                v_prepared_statement_st = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fecha conexao com o banco.
     */
    @Override
    public void Close() {
        try {
            if (this._conn != null && !this._conn.isClosed()) {
                this._conn.rollback();
                this._conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executa SELECT na base.
     * @param query
     * @return
     * @throws SQLException
     */
    public ResultSet Query(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        v_resultset_result = v_prepared_statement_st.executeQuery();
        return v_resultset_result;
    }

    /**
     * Executa update na base.
     * @param query
     * @return
     * @throws SQLException
     */
    public int Update(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     * Executa Insert na base.
     * @param query
     * @return
     * @throws SQLException
     */
    public int Insert(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     * 
     */
    public int Object(String query) throws SQLException {
        String v_string_object;
        String[] v_string_linha;

        // retira quebras de linhas e comentarios para poder compilar
        v_string_linha = query.split("[\n\r]");
        v_string_object = "";
        for (int i = 0; i < (v_string_linha.length - 1); i++) {
            if (!v_string_linha[i].contains("--") && !v_string_linha[i].equals("")) {
                v_string_object = v_string_object + " " + v_string_linha[i];
            }
        }

        v_prepared_statement_st = this._conn.prepareStatement(v_string_object);
        v_prepared_statement_st.setEscapeProcessing(false);

        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     * 
     * @param query
     * @param parameters
     * @throws SQLException 
     */
    public void ExecStoredProcedure(String query, ArrayList parameters) throws SQLException {

        int index = 0;
        OracleStoredProcedureParameter v_oraclestparam_param;
        CallableStatement v_callablestatement_st;

        System.out.println();
        System.out.println("DB Stored Call: " + query);

        if (parameters == null) {
            v_callablestatement_st = this._conn.prepareCall(query);
            v_callablestatement_st.execute();
        } else {
            Iterator e = parameters.iterator();
            v_callablestatement_st = this._conn.prepareCall(query);

            while (e.hasNext()) {
                index++;

                v_oraclestparam_param = (OracleStoredProcedureParameter) e.next();

                if (v_oraclestparam_param._type == Types.VARCHAR) {
                    v_callablestatement_st.setString(index, (String) v_oraclestparam_param.getValue());
                } else if (v_oraclestparam_param._type == Types.NUMERIC) {
                    v_callablestatement_st.setInt(index, (Integer) v_oraclestparam_param.getValue());
                }

                System.out.println("Param" + index + ": " + v_oraclestparam_param.getValue());
            }

            v_callablestatement_st.execute();
        }
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public OracleValidationResult ValidateVersion() throws SQLException {
        int v_int_version;
        String[] v_arr_string_part;
        String v_string_retorno = "";
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        v_arr_string_part = this.get_version().split("\\.");
        v_int_version = Integer.parseInt(v_arr_string_part[0] + v_arr_string_part[1] + v_arr_string_part[2] + v_arr_string_part[3]);

        if (this._tag.equals("8i")) {
            if (v_int_version < 8174) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + this._version + " (" + Utils.getDefaultBundle().getString("Database.required_oracle") + " 8.1.0.4)";
            }
        } else if (this._tag.equals("9i")) {
            if (v_int_version < 9204) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + this._version + " (" + Utils.getDefaultBundle().getString("Database.required_oracle") + " 9.2.0.4)";
            }
        } else if (this._tag.equals("10g")) {
            if (v_int_version < 10203) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + this._version + " (" + Utils.getDefaultBundle().getString("Database.required_oracle") + " 10.2.0.3)";
            }
        } else if (this._tag.equals("11g")) {
            if (v_int_version < 11106) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + this._version + " (" + Utils.getDefaultBundle().getString("Database.required_oracle") + " 11.1.0.6)";
            }
        } else if (this._tag.equals("12c")) {
            if (v_int_version < 12101) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + this._version + " (" + Utils.getDefaultBundle().getString("Database.required_oracle") + " 12.1.0.2)";
            }

        }

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public OracleValidationResult ValidateJava() throws SQLException {
        String v_string_retorno = "";
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        v_resultset_result = this.Query("select * from V$OPTION where PARAMETER in ('Java','Bit-mapped indexes')");

        while (v_resultset_result.next()) {
            if (v_resultset_result.getString("PARAMETER").equals("Java") && !v_resultset_result.getString("VALUE").equals("TRUE")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + v_resultset_result.getString("PARAMETER") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.required_oracle_java") + ")" + System.getProperty("line.separator");
            }
            if (v_resultset_result.getString("PARAMETER").equals("Bit-mapped indexes") && !v_resultset_result.getString("VALUE").equals("TRUE")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + v_resultset_result.getString("PARAMETER") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.required_oracle_bitmap") + ")" + System.getProperty("line.separator");
            }
        }
        v_resultset_result.close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * 
     * @return
     * @throws SQLException 
     */
    public OracleValidationResult ValidateParameters() throws SQLException {
        String v_string_retorno = "";
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");
        try {
            v_resultset_result = this.Query("SELECT NAME, VALUE FROM V$PARAMETER WHERE NAME IN ('cursor_sharing', 'open_cursors', 'query_rewrite_enabled', 'optimizer_mode', 'optimizer_index_caching', 'optimizer_index_cost_adj', 'optimizer_dynamic_sampling', 'db_file_multiblock_read_count')");

            while (v_resultset_result.next()) {
                if (v_resultset_result.getString("NAME").equals("cursor_sharing") && !v_resultset_result.getString("VALUE").equals("EXACT")) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.required") + " EXACT)" + System.getProperty("line.separator");
                }

                if (v_resultset_result.getString("NAME").equals("open_cursors") && v_resultset_result.getInt("VALUE") < 500) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " >= 500)" + System.getProperty("line.separator");
                }

                if (v_resultset_result.getString("NAME").equals("query_rewrite_enabled") && !v_resultset_result.getString("VALUE").equals("TRUE")) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " TRUE)" + System.getProperty("line.separator");
                }

                if ((this.get_tag().equals("10g") || this.get_tag().equals("11g")) && v_resultset_result.getString("NAME").equals("optimizer_mode") && !v_resultset_result.getString("VALUE").toUpperCase().equals("FIRST_ROWS_10")) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " FIRST_ROWS_10)" + System.getProperty("line.separator");
                }

                if ((this.get_tag().equals("10g") || this.get_tag().equals("11g")) && v_resultset_result.getString("NAME").equals("optimizer_index_caching") && v_resultset_result.getInt("VALUE") < 95) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " >= 95)" + System.getProperty("line.separator");
                }

                if ((this.get_tag().equals("10g") || this.get_tag().equals("11g")) && v_resultset_result.getString("NAME").equals("optimizer_index_cost_adj") && v_resultset_result.getInt("VALUE") < 5) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " >= 5 )" + System.getProperty("line.separator");
                }
                if ((this.get_tag().equals("10g") || this.get_tag().equals("11g")) && v_resultset_result.getString("NAME").equals("db_file_multiblock_read_count") && v_resultset_result.getInt("VALUE") != 8) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " = 8 )" + System.getProperty("line.separator");
                }

                if ((this.get_tag().equals("10g") || this.get_tag().equals("11g")) && v_resultset_result.getString("NAME").equals("optimizer_dynamic_sampling") && v_resultset_result.getInt("VALUE") != 1) {
                    v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    v_string_retorno = v_string_retorno + "> " + v_resultset_result.getString("NAME") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " = 1 )" + System.getProperty("line.separator");
                }

            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 942) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + "> " + Utils.getDefaultBundle().getString("Validation.v$parameter") + System.getProperty("line.separator");
            } else {
                throw e;
            }
        } finally {
            if (v_resultset_result != null) {
                v_resultset_result.close();
            }
        }


        v_resultset_result = this.Query("select * from nls_database_parameters where PARAMETER in ('NLS_LANGUAGE','NLS_TERRITORY','NLS_CHARACTERSET')");
        while (v_resultset_result.next()) {
            if (v_resultset_result.getString("PARAMETER").equals("NLS_LANGUAGE") && !v_resultset_result.getString("VALUE").contains("AMERICA")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + "> " + "database " + v_resultset_result.getString("PARAMETER") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " %AMERICA%)" + System.getProperty("line.separator");
            }

            if (v_resultset_result.getString("PARAMETER").equals("NLS_TERRITORY") && !v_resultset_result.getString("VALUE").contains("AMERICA")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + "> " + "database " + v_resultset_result.getString("PARAMETER") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " %AMERICA%)" + System.getProperty("line.separator");
            }

            if (v_resultset_result.getString("PARAMETER").equals("NLS_CHARACTERSET") && !v_resultset_result.getString("VALUE").contains("WE8")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                v_string_retorno = v_string_retorno + "> " + "database " + v_resultset_result.getString("PARAMETER") + " = " + v_resultset_result.getString("VALUE") + " (" + Utils.getDefaultBundle().getString("Database.recommended") + " %WE8%)" + System.getProperty("line.separator");
            }
        }
        v_resultset_result.close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * Método para validação das permissões.
     * @param pd
     * @return
     * @throws SQLException
     */
    public OracleValidationResult ValidatePermissionSombra(String userSombra, String passSombra) throws SQLException {
        String v_string_retorno = "";
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        OracleConnection v_connection = new OracleConnection();
        v_connection.set_tns(this.get_tns());
        v_connection.set_username(userSombra);
        v_connection.set_password(passSombra);

        try {
            v_connection.Connect();
        } catch (Exception e) {
            v_connection.Close();
            System.out.println("Error Connect " + userSombra + ": " + e.getMessage());
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            v_oraclevalidationresult_retorno.set_text("> N�o foi poss�vel conectar em " + userSombra + ".\n Verificar exist�ncia do usu�rio/senha e permiss�o de CREATE SESSION.\n" + System.getProperty("line.separator"));
            return v_oraclevalidationresult_retorno;
        }

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        v_connection.Close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * M�todo para valida��o das permiss�es.
     * @param pd
     * @return
     * @throws SQLException
     */
    public OracleValidationResult ValidatePermission(SfwOracleProductDetail pd) throws SQLException {
        String v_string_retorno = "";
        Enumeration v_enumeration_privilege;
        String v_string_current_privilege, v_string_current_priv_sql, v_string_current_priv_drop;
        Hashtable v_hashtable_privilege, v_hashtable_priv_sql, v_hashtable_priv_drop, v_hashtable_priv_level;
        boolean v_boolean_current_privilege;
        boolean v_boolean_inout_create_table = false;
        boolean v_boolean_cambio_vsession = false;
        boolean v_boolean_dbms_crypto = false;
        OracleConnection v_oracleconnection_produto;
        OracleValidationResult.Result v_oraresult_current;
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        v_oracleconnection_produto = new OracleConnection();
        v_oracleconnection_produto.set_tns(this.get_tns());
        v_oracleconnection_produto.set_username(pd.getUser());
        v_oracleconnection_produto.set_password(pd.getPassword());

        try {
            v_oracleconnection_produto.Connect();
        } catch (Exception e) {
            System.out.println("Error Connect " + pd.getUser() + ": " + e.getMessage());
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            v_oraclevalidationresult_retorno.set_text("> N�o foi poss�vel conectar em " + pd.getUser() + ".\n Verificar exist�ncia do usu�rio/senha e permiss�o de CREATE SESSION.\n" + System.getProperty("line.separator"));
            return v_oraclevalidationresult_retorno;
        }

        /*if (pd.getProduct().get_name().equals("SOFTCOMEX")) {
        String v_string_result = this.TrackingValidate();
        if (!v_string_result.equals("")) {
        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
        v_oraclevalidationresult_retorno.set_text("> Usu�rio TrackingSYS: " + v_string_result + " inv�lido.\n Verificar exist�ncia do u/senha e permiss�o de CREATE SESSION.\n" + System.getProperty("line.separator"));
        return v_oraclevalidationresult_retorno;
        }
        }*/

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        // hashtable com as permissoes necessarias
        v_hashtable_privilege = new Hashtable();
        v_hashtable_priv_level = new Hashtable();
        v_hashtable_priv_sql = new Hashtable();
        v_hashtable_priv_drop = new Hashtable();
        //-- Permissoes a serem validadas
        v_hashtable_privilege.put("CREATE PROCEDURE", new Boolean(false));
        v_hashtable_privilege.put("CREATE SEQUENCE", new Boolean(false));
        v_hashtable_privilege.put("CREATE SYNONYM", new Boolean(false));
        v_hashtable_privilege.put("CREATE TABLE", new Boolean(false));
        v_hashtable_privilege.put("CREATE TRIGGER", new Boolean(false));
        v_hashtable_privilege.put("CREATE TYPE", new Boolean(false));
        v_hashtable_privilege.put("CREATE VIEW", new Boolean(false));
        //-- Select de teste de cada permissao
        v_hashtable_priv_sql.put("CREATE PROCEDURE", "create or replace procedure PRC_TESTE_INSTALADOR\n"
                + "is\n"
                + "begin\n"
                + "  null;\n"
                + "end;\n");

        v_hashtable_priv_sql.put("CREATE SEQUENCE", "create sequence SEQ_TESTE_INSTALADOR nocache");
        v_hashtable_priv_sql.put("CREATE SYNONYM", "create or replace synonym SYN_TESTE_INSTALADOR for DUAL");
        v_hashtable_priv_sql.put("CREATE TABLE", "create table TESTE_INSTALADOR (X number)");
        v_hashtable_priv_sql.put("CREATE TRIGGER", "begin\n"
                + "  execute immediate 'create table TESTE_INSTALADOR (X number)';\n"
                + "  execute immediate 'create or replace trigger TRG_TESTE_INSTALADOR before insert on TESTE_INSTALADOR declare begin null; end;';\n"
                + "end;\n");
        v_hashtable_priv_sql.put("CREATE TYPE", "create or replace type TP_TESTE_INSTALADOR as object ( x number )");
        v_hashtable_priv_sql.put("CREATE VIEW", "create or replace view VW_INSTALADOR_TESTE as select * from dual");
        //--
        v_hashtable_priv_drop.put("CREATE PROCEDURE", "drop procedure PRC_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE SEQUENCE", "drop sequence SEQ_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE SYNONYM", "drop synonym SYN_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TABLE", "drop table TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TRIGGER", "drop table TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TYPE", "drop type TP_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE VIEW", "drop view VW_INSTALADOR_TESTE");
        //-- Nivel de validacao de cada permissao
        v_hashtable_priv_level.put("CREATE PROCEDURE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE SEQUENCE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE SYNONYM", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TABLE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TRIGGER", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TYPE", OracleValidationResult.Result.WARN);
        v_hashtable_priv_level.put("CREATE VIEW", OracleValidationResult.Result.ERROR);

        // permissoes para o usuario
        v_enumeration_privilege = v_hashtable_privilege.keys();
        while (v_enumeration_privilege.hasMoreElements()) {
            v_string_current_privilege = (String) v_enumeration_privilege.nextElement();
            v_string_current_priv_sql = (String) v_hashtable_priv_sql.get(v_string_current_privilege);

            if (!v_string_current_priv_sql.equals("")) {
                try {
                    if (v_hashtable_priv_drop.containsKey(v_string_current_privilege)) {
                        v_string_current_priv_drop = (String) v_hashtable_priv_drop.get(v_string_current_privilege);
                        v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_drop);
                        v_resultset_result.close();
                    }
                } catch (Exception e) {
                }
                //executa comando de teste para ver se tem permissao
                try {
                    v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_sql);
                    v_resultset_result.close();

                    // se tiver que dropar objeto
                    if (v_hashtable_priv_drop.containsKey(v_string_current_privilege)) {
                        v_string_current_priv_drop = (String) v_hashtable_priv_drop.get(v_string_current_privilege);
                        v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_drop);
                        v_resultset_result.close();
                    }

                    //se tudo correu bem, marca como tem permissao no comando
                    v_hashtable_privilege.put(v_string_current_privilege, new Boolean(true));
                } catch (SQLException e) {
                    System.out.println("Error " + pd.getUser() + " Privilege " + v_string_current_privilege + ": " + e.getMessage());
                }
            } else {
                v_hashtable_privilege.put(v_string_current_privilege, new Boolean(true));
            }
        }

        // verifica se teve alguma permissao faltante
        if (v_hashtable_privilege.contains(false)) {
            // verifica quais permissoes faltaram e quais sao necessarias ou desejaveis apenas
            v_enumeration_privilege = v_hashtable_privilege.keys();
            while (v_enumeration_privilege.hasMoreElements()) {
                v_string_current_privilege = (String) v_enumeration_privilege.nextElement();
                v_boolean_current_privilege = (Boolean) v_hashtable_privilege.get(v_string_current_privilege);
                v_oraresult_current = ((OracleValidationResult.Result) v_hashtable_priv_level.get(v_string_current_privilege));

                if (!v_boolean_current_privilege) {
                    if (v_oraresult_current.equals(OracleValidationResult.Result.ERROR)) {
                        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                    } else if (!v_oraclevalidationresult_retorno.get_result().equals(OracleValidationResult.Result.ERROR)) {
                        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    }

                    if (v_oraresult_current.equals(OracleValidationResult.Result.ERROR)) {
                        v_string_retorno = v_string_retorno + Utils.getDefaultBundle().getString("Database.required_permission1") + " " + v_string_current_privilege + " " + Utils.getDefaultBundle().getString("Database.required_permission2") + " " + pd.getUser().toUpperCase() + "." + System.getProperty("line.separator");
                    } else {
                        v_string_retorno = v_string_retorno + Utils.getDefaultBundle().getString("Database.desired_permission1") + " " + v_string_current_privilege + " " + Utils.getDefaultBundle().getString("Database.desired_permission2") + " " + pd.getUser().toUpperCase() + "." + System.getProperty("line.separator");
                    }
                }
            }
        }

        // validacao grant CREATE TABLE direto para o usuario do InOut
        if (pd.getProduct().get_name().equals("INOUT_SYS")) {
            try {
                v_resultset_result = v_oracleconnection_produto.Query("select count(*) as QTDE from USER_SYS_PRIVS where PRIVILEGE in ('CREATE TABLE','CREATE ANY TABLE')");

                while (v_resultset_result.next()) {
                    if (v_resultset_result.getInt("QTDE") > 0) {
                        v_boolean_inout_create_table = true;
                    }
                }
                v_resultset_result.close();

            } catch (SQLException e) {
                System.out.println("Error InOut Create: " + e.getMessage());
            }

            if (!v_boolean_inout_create_table) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                v_string_retorno = v_string_retorno + String.format(Utils.getDefaultBundle().getString("Validation.createtableinout"), pd.getUser().toUpperCase()) + "" + System.getProperty("line.separator");
            }
        }

        // Valida permiss�es para os owners na DBMS_CRYPTO
        try {
            v_oracleconnection_produto.Query("SELECT LOWER(DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW('cm'), 3)) FROM DUAL");

            v_boolean_dbms_crypto = true;

        } catch (SQLException e) {
            System.out.println("Error DBMS_CRYPTO: " + e.getMessage());
        }

        if (!v_boolean_dbms_crypto) {
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            v_string_retorno = v_string_retorno + String.format(Utils.getDefaultBundle().getString("Validation.dbmscrypto"), pd.getUser().toUpperCase()) + "" + System.getProperty("line.separator");
        }



        // se for cambio, tem que ter grant de select na v$session
        if (pd.getProduct().get_name().equals("CAMBIO_IMP_SYS") || pd.getProduct().get_name().equals("CAMBIO_EXP_SYS")) {
            try {
                v_oracleconnection_produto.Query("select * from V$SESSION where rownum = 1");

                v_boolean_cambio_vsession = true;

            } catch (SQLException e) {
                System.out.println("Error Cambio Session: " + e.getMessage());
            }

            if (!v_boolean_cambio_vsession) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                v_string_retorno = v_string_retorno + String.format(Utils.getDefaultBundle().getString("Validation.cambiov$session"), pd.getUser().toUpperCase()) + "" + System.getProperty("line.separator");
            }
        }

        v_oracleconnection_produto.Close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    public OracleValidationResult ValidatePermission(SfwOracleDefineUser pd) throws SQLException {
        String v_string_retorno = "";
        Enumeration v_enumeration_privilege;
        String v_string_current_privilege, v_string_current_priv_sql, v_string_current_priv_drop;
        Hashtable v_hashtable_privilege, v_hashtable_priv_sql, v_hashtable_priv_drop, v_hashtable_priv_level;
        boolean v_boolean_current_privilege;
        boolean v_boolean_inout_create_table = false;
        boolean v_boolean_cambio_vsession = false;
        OracleConnection v_oracleconnection_produto;
        OracleValidationResult.Result v_oraresult_current;
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        v_oracleconnection_produto = new OracleConnection();
        v_oracleconnection_produto.set_tns(this.get_tns());
        v_oracleconnection_produto.set_username(pd.getUser());
        v_oracleconnection_produto.set_password(pd.getPassword());

        try {
            v_oracleconnection_produto.Connect();
        } catch (Exception e) {
            System.out.println("Error Connect " + pd.getUser() + ": " + e.getMessage());
            if (pd.getProduct().get_name().equalsIgnoreCase("CST")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            } else if (!pd.getProduct().is_installable() && !pd.getProduct().get_name().equals("TRACKING_SYS")) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
            } else {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            }
            v_oraclevalidationresult_retorno.set_text("> N�o foi poss�vel conectar em " + pd.getUser() + ".\n Verificar exist�ncia do usu�rio/senha e permiss�o de CREATE SESSION.\n" + System.getProperty("line.separator"));
            return v_oraclevalidationresult_retorno;
        }

        /*if (pd.getProduct().get_name().equals("SOFTCOMEX")) {
        String v_string_result = this.TrackingValidate();
        if (!v_string_result.equals("")) {
        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
        v_oraclevalidationresult_retorno.set_text("> Usu�rio TrackingSYS: " + v_string_result + " inv�lido.\n Verificar exist�ncia do u/senha e permiss�o de CREATE SESSION.\n" + System.getProperty("line.separator"));
        return v_oraclevalidationresult_retorno;
        }
        }
        
        if (pd.getProduct().get_name().equals("SOFTCOMEX")) {
        String v_string_result = this.ValidadeProductNotSelected();
        if (!v_string_result.equals("")) {
        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
        v_oraclevalidationresult_retorno.set_text(String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgcreatesession"), v_string_result, System.getProperty("line.separator")));
        return v_oraclevalidationresult_retorno;
        }
        }*/

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        // hashtable com as permissoes necessarias
        v_hashtable_privilege = new Hashtable();
        v_hashtable_priv_level = new Hashtable();
        v_hashtable_priv_sql = new Hashtable();
        v_hashtable_priv_drop = new Hashtable();
        //-- Permissoes a serem validadas
        v_hashtable_privilege.put("CREATE PROCEDURE", new Boolean(false));
        v_hashtable_privilege.put("CREATE SEQUENCE", new Boolean(false));
        v_hashtable_privilege.put("CREATE SYNONYM", new Boolean(false));
        v_hashtable_privilege.put("CREATE TABLE", new Boolean(false));
        v_hashtable_privilege.put("CREATE TRIGGER", new Boolean(false));
        v_hashtable_privilege.put("CREATE TYPE", new Boolean(false));
        v_hashtable_privilege.put("CREATE VIEW", new Boolean(false));
        //-- Select de teste de cada permissao
        v_hashtable_priv_sql.put("CREATE PROCEDURE", "create or replace procedure PRC_TESTE_INSTALADOR\n"
                + "is\n"
                + "begin\n"
                + "  null;\n"
                + "end;\n");

        v_hashtable_priv_sql.put("CREATE SEQUENCE", "create sequence SEQ_TESTE_INSTALADOR nocache");
        v_hashtable_priv_sql.put("CREATE SYNONYM", "create or replace synonym SYN_TESTE_INSTALADOR for DUAL");
        v_hashtable_priv_sql.put("CREATE TABLE", "create table TESTE_INSTALADOR (X number)");
        v_hashtable_priv_sql.put("CREATE TRIGGER", "begin\n"
                + "  execute immediate 'create table TESTE_INSTALADOR (X number)';\n"
                + "  execute immediate 'create or replace trigger TRG_TESTE_INSTALADOR before insert on TESTE_INSTALADOR declare begin null; end;';\n"
                + "end;\n");
        v_hashtable_priv_sql.put("CREATE TYPE", "create or replace type TP_TESTE_INSTALADOR as object ( x number )");
        v_hashtable_priv_sql.put("CREATE VIEW", "create or replace view VW_INSTALADOR_TESTE as select * from dual");
        //--
        v_hashtable_priv_drop.put("CREATE PROCEDURE", "drop procedure PRC_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE SEQUENCE", "drop sequence SEQ_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE SYNONYM", "drop synonym SYN_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TABLE", "drop table TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TRIGGER", "drop table TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE TYPE", "drop type TP_TESTE_INSTALADOR");
        v_hashtable_priv_drop.put("CREATE VIEW", "drop view VW_INSTALADOR_TESTE");
        //-- Nivel de validacao de cada permissao
        v_hashtable_priv_level.put("CREATE PROCEDURE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE SEQUENCE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE SYNONYM", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TABLE", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TRIGGER", OracleValidationResult.Result.ERROR);
        v_hashtable_priv_level.put("CREATE TYPE", OracleValidationResult.Result.WARN);
        v_hashtable_priv_level.put("CREATE VIEW", OracleValidationResult.Result.ERROR);

        // permissoes para o usuario
        v_enumeration_privilege = v_hashtable_privilege.keys();
        while (v_enumeration_privilege.hasMoreElements()) {
            v_string_current_privilege = (String) v_enumeration_privilege.nextElement();
            v_string_current_priv_sql = (String) v_hashtable_priv_sql.get(v_string_current_privilege);

            if (!v_string_current_priv_sql.equals("")) {
                try {
                    if (v_hashtable_priv_drop.containsKey(v_string_current_privilege)) {
                        v_string_current_priv_drop = (String) v_hashtable_priv_drop.get(v_string_current_privilege);
                        v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_drop);
                        v_resultset_result.close();
                    }
                } catch (Exception e) {
                }
                //executa comando de teste para ver se tem permissao
                try {
                    v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_sql);
                    v_resultset_result.close();

                    // se tiver que dropar objeto
                    if (v_hashtable_priv_drop.containsKey(v_string_current_privilege)) {
                        v_string_current_priv_drop = (String) v_hashtable_priv_drop.get(v_string_current_privilege);
                        v_resultset_result = v_oracleconnection_produto.Query(v_string_current_priv_drop);
                        v_resultset_result.close();
                    }

                    //se tudo correu bem, marca como tem permissao no comando
                    v_hashtable_privilege.put(v_string_current_privilege, new Boolean(true));
                } catch (SQLException e) {
                    System.out.println("Error " + pd.getUser() + " Privilege " + v_string_current_privilege + ": " + e.getMessage());
                }
            } else {
                v_hashtable_privilege.put(v_string_current_privilege, new Boolean(true));
            }
        }

        // verifica se teve alguma permissao faltante
        if (v_hashtable_privilege.contains(false)) {
            // verifica quais permissoes faltaram e quais sao necessarias ou desejaveis apenas
            v_enumeration_privilege = v_hashtable_privilege.keys();
            while (v_enumeration_privilege.hasMoreElements()) {
                v_string_current_privilege = (String) v_enumeration_privilege.nextElement();
                v_boolean_current_privilege = (Boolean) v_hashtable_privilege.get(v_string_current_privilege);
                v_oraresult_current = ((OracleValidationResult.Result) v_hashtable_priv_level.get(v_string_current_privilege));

                if (!v_boolean_current_privilege) {
                    if (v_oraresult_current.equals(OracleValidationResult.Result.ERROR)) {
                        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                    } else if (!v_oraclevalidationresult_retorno.get_result().equals(OracleValidationResult.Result.ERROR)) {
                        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
                    }

                    if (v_oraresult_current.equals(OracleValidationResult.Result.ERROR)) {
                        v_string_retorno = v_string_retorno + Utils.getDefaultBundle().getString("Database.required_permission1") + " " + v_string_current_privilege + " " + Utils.getDefaultBundle().getString("Database.required_permission2") + " " + pd.getUser().toUpperCase() + "." + System.getProperty("line.separator");
                    } else {
                        v_string_retorno = v_string_retorno + Utils.getDefaultBundle().getString("Database.desired_permission1") + " " + v_string_current_privilege + " " + Utils.getDefaultBundle().getString("Database.desired_permission2") + " " + pd.getUser().toUpperCase() + "." + System.getProperty("line.separator");
                    }
                }
            }
        }

        // validacao grant CREATE TABLE direto para o usuario do InOut
        if (pd.getProduct().get_name().equals("INOUT_SYS")) {
            try {
                v_resultset_result = v_oracleconnection_produto.Query("select count(*) as QTDE from USER_SYS_PRIVS where PRIVILEGE in ('CREATE TABLE','CREATE ANY TABLE')");

                while (v_resultset_result.next()) {
                    if (v_resultset_result.getInt("QTDE") > 0) {
                        v_boolean_inout_create_table = true;
                    }
                }
                v_resultset_result.close();

            } catch (SQLException e) {
                System.out.println("Error InOut Create: " + e.getMessage());
            }

            if (!v_boolean_inout_create_table) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                v_string_retorno = v_string_retorno + String.format(Utils.getDefaultBundle().getString("Validation.createtableinout"), pd.getUser().toUpperCase()) + "" + System.getProperty("line.separator");
            }
        }

        // se for cambio, tem que ter grant de select na v$session
        if (pd.getProduct().get_name().equals("CAMBIO_IMP_SYS") || pd.getProduct().get_name().equals("CAMBIO_EXP_SYS")) {
            try {
                v_oracleconnection_produto.Query("select * from V$SESSION where rownum = 1");

                v_boolean_cambio_vsession = true;

            } catch (SQLException e) {
                System.out.println("Error Cambio Session: " + e.getMessage());
            }

            if (!v_boolean_cambio_vsession) {
                v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
                v_string_retorno = v_string_retorno + String.format(Utils.getDefaultBundle().getString("Validation.cambiov$session"), pd.getUser().toUpperCase()) + "" + System.getProperty("line.separator");
            }
        }

        v_oracleconnection_produto.Close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * 
     */
    public OracleValidationResult ValidatePermission(OracleConnection v_oracleconnection_produto, SfwOracleDefineUser pd) throws SQLException {
        String v_string_retorno = "";
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();
        try {
            v_oracleconnection_produto.Connect();
        } catch (Exception e) {
            System.out.println("Error Connect " + pd.getUser() + ": " + e.getMessage());
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.WARN);
            v_oraclevalidationresult_retorno.set_text("> " + String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgcreatesession") + System.getProperty("line.separator"), pd.getUser(), System.getProperty("line.separator")));
            return v_oraclevalidationresult_retorno;
        }

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");

        v_oracleconnection_produto.Close();

        v_oraclevalidationresult_retorno.set_text(v_string_retorno);

        return v_oraclevalidationresult_retorno;
    }

    /**
     * Verifica se a tablespace existe.
     * @param tablespace_name
     * @return
     * @throws SQLException
     */
    public boolean TablespaceExists(String tablespace_name) throws SQLException {
        boolean v_boolean_ret = true;
        v_resultset_result = this.Query("select count(*) as RESULT from USER_TABLESPACES where STATUS = 'ONLINE' and TABLESPACE_NAME = '" + tablespace_name + "'");

        while (v_resultset_result.next()) {
            if (v_resultset_result.getInt("RESULT") < 1) {
                v_boolean_ret = false;
            }
        }
        v_resultset_result.close();

        return v_boolean_ret;
    }

    /**
     * Executa Stored Procedure
     */
    public class OracleStoredProcedureParameter {

        public int _type;
        public String _varchar_value;
        public int _number_value;

        public Object getValue() {
            if (_type == Types.VARCHAR) {
                return _varchar_value;
            } else if (_type == Types.NUMERIC) {
                return _number_value;
            }

            return null;
        }
    }

    /**
     * Retorna os parametros da Stored Procedure
     * @param value
     * @return
     */
    public OracleStoredProcedureParameter getStoredParameter(String value) {
        OracleStoredProcedureParameter param;

        param = new OracleStoredProcedureParameter();
        param._type = Types.VARCHAR;
        param._varchar_value = value;

        return param;
    }

    /**
     * Retorna os parametros da Stored Procedure
     * @param value
     * @return
     */
    public OracleStoredProcedureParameter getStoredParameter(int value) {
        OracleStoredProcedureParameter param;

        param = new OracleStoredProcedureParameter();
        param._type = Types.NUMERIC;
        param._number_value = value;

        return param;
    }

    /**
     * Retorna a instance na qual será conectado.
     * @return
     */
    public String getURL() {
        try {
            String v_s_host = "";
            String v_s_tnsping;
            Process p = Runtime.getRuntime().exec("tnsping " + this._tns);
            InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

            StringBuilder buffer = new StringBuilder();
            for (;;) {
                int c = stdoutStream.read();
                if (c == -1) {
                    break;
                }
                buffer.append((char) c);
            }
            v_s_tnsping = buffer.toString().toUpperCase();

            stdoutStream.close();

            v_s_tnsping = v_s_tnsping.replaceAll(" ", "");

            if (v_s_tnsping.contains("(DESCRIPTION")) {
                v_s_host = v_s_tnsping.substring(v_s_tnsping.indexOf("(DESCRIPTION"), v_s_tnsping.indexOf("OK"));
            }

            return v_s_host = "jdbc:oracle:thin:@" + v_s_host;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insere na tabela SFW_CM_SCHEMA
     * @param p_oracleproductdetail
     * @return
     */
    public boolean insertCMschema(SfwOracleProductDetail p_oracleproductdetail) {
        try {

            String icone = "";
            icone = p_oracleproductdetail.getProduct().get_icon();
            icone = icone.substring(0, icone.lastIndexOf("."));
            if (p_oracleproductdetail.getProduct().get_code().equals("0") || p_oracleproductdetail.getProduct().get_code().equals("500")) {
                icone = icone.concat(".ico");
            } else {
                icone = icone.concat(".bmp");
            }

            ResultSet rs = this.Query("select count(1) as existe from sfw_cm_schema where cod_sistema = " + p_oracleproductdetail.getCodSistema() + "");
            while (rs.next()) {
                if (rs.getString("existe").equals("0")) {
                    this.Insert("insert into SFW_CM_SCHEMA (cod_sistema, "
                            + "s_icone_login_centralizado, "
                            + "s_user, "
                            + "s_pass, "
                            + "s_schema_owner, "
                            + "s_owner_pass, "
                            + "s_role, "
                            + "s_cripto, "
                            + "S_TABLESPACE_DATA_4K, "
                            + "S_TABLESPACE_DATA_64K, "
                            + "S_TABLESPACE_DATA_128K, "
                            + "S_TABLESPACE_DATA_512K, "
                            + "S_TABLESPACE_DATA_1M, "
                            + "S_TABLESPACE_INDEX_4K, "
                            + "S_TABLESPACE_INDEX_64K, "
                            + "S_TABLESPACE_INDEX_128K, "
                            + "S_TABLESPACE_INDEX_512K, "
                            + "S_TABLESPACE_INDEX_1M, "
                            + "S_SIGLA, "
                            + "S_TNS) "
                            + "values "
                            + "(" + p_oracleproductdetail.getCodSistema() + ","
                            + "'" + icone + "',"
                            + "'" + Profile.getUsuariosombra() + "',"
                            + "'" + Utils.RetornaSenhaCryptoSemAspas(Profile.getSenhasombra()) + "',"
                            + "'" + p_oracleproductdetail.getUser() + "',"
                            + "'" + Utils.RetornaSenhaCryptoSemAspas(p_oracleproductdetail.getPassword()) + "',"
                            + "'" + Utils.nvl(Install.get_rolesfw(), "") + "',"
                            + "'S',"
                            + "'" + p_oracleproductdetail.getData4k() + "',"
                            + "'" + p_oracleproductdetail.getData4k() + "',"
                            + "'" + p_oracleproductdetail.getData4k() + "',"
                            + "'" + p_oracleproductdetail.getData4k() + "',"
                            + "'" + p_oracleproductdetail.getData4k() + "',"
                            + "'" + p_oracleproductdetail.getIndex4k() + "',"
                            + "'" + p_oracleproductdetail.getIndex4k() + "',"
                            + "'" + p_oracleproductdetail.getIndex4k() + "',"
                            + "'" + p_oracleproductdetail.getIndex4k() + "',"
                            + "'" + p_oracleproductdetail.getIndex4k() + "',"
                            + "'" + Install.get_shortname() + "',"
                            + "'" + Install.get_tns() + "')");
                } else {
                    this.Update("updat SFW_CM_SCHEMA set "
                            + "s_user = '" + Profile.getUsuariosombra() + "',"
                            + "s_pass = '" + Utils.RetornaSenhaCryptoSemAspas(Profile.getSenhasombra()) + "',"
                            + "s_schema_owner = '" + p_oracleproductdetail.getUser() + "',"
                            + "s_owner_pass = '" + Utils.RetornaSenhaCryptoSemAspas(p_oracleproductdetail.getPassword()) + "',"
                            + "s_role = '" + Utils.nvl(Install.get_rolesfw(), "") + "',"
                            + "s_cripto = 'S',"
                            + "S_TABLESPACE_DATA_4K = '" + p_oracleproductdetail.getData4k() + "',"
                            + "S_TABLESPACE_DATA_64K = '" + p_oracleproductdetail.getData4k() + "',"
                            + "S_TABLESPACE_DATA_128K = '" + p_oracleproductdetail.getData4k() + "',"
                            + "S_TABLESPACE_DATA_512K = '" + p_oracleproductdetail.getData4k() + "',"
                            + "S_TABLESPACE_DATA_1M = '" + p_oracleproductdetail.getData4k() + "',"
                            + "S_TABLESPACE_INDEX_4K = '" + p_oracleproductdetail.getIndex4k() + "',"
                            + "S_TABLESPACE_INDEX_64K = '" + p_oracleproductdetail.getIndex4k() + "',"
                            + "S_TABLESPACE_INDEX_128K = '" + p_oracleproductdetail.getIndex4k() + "',"
                            + "S_TABLESPACE_INDEX_512K = '" + p_oracleproductdetail.getIndex4k() + "',"
                            + "S_TABLESPACE_INDEX_1M =  '" + p_oracleproductdetail.getIndex4k() + "',"
                            + "S_SIGLA = '" + Install.get_shortname() + "', "
                            + "S_TNS =  '" + Install.get_tns() + "' "
                            + "WHERE COD_SISTEMA = " + p_oracleproductdetail.getCodSistema() + " ");
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *  Cria o profile de acordo com os registros do banco
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    public boolean createProfileFromDataBase() throws SQLException, IOException {
        String v_string_product_name;
        String v_string_product_shortname;
        File v_file_profile;
        Properties v_properties_profile;
        OutputStream v_outputstream_profile;

        ResultSet rs = this.Query("select * from SFW_CM_SCHEMA A where cod_sistema <> 1000 order by cod_sistema");

        try {
            v_file_profile = new File("profile.ini");
            v_properties_profile = new Properties();

            if (v_file_profile.exists()) {
                v_file_profile.delete();
            }
            v_file_profile.createNewFile();
            v_properties_profile.setProperty("SO", "WINDOWS");
            v_properties_profile.setProperty("BD", "ORACLE");
            int v_int_contador = 1;
            try {
                while (rs.next()) {
                    if (v_int_contador == 1) {
                        v_properties_profile.setProperty("CLIENT", "BASE");
                        v_properties_profile.setProperty("CLIENT_SHORT", Utils.nvl(rs.getString("S_SIGLA"), "TR"));
                        v_properties_profile.setProperty("TNS", Utils.nvl(rs.getString("S_TNS"), ""));
                        if (!Utils.nvl(rs.getString("S_ROLE"), "").equals("")) {
                            v_properties_profile.setProperty("CHECKROLE", "CHECKED");
                        } else {
                            v_properties_profile.setProperty("CHECKROLE", "UNCHECKED");
                        }

                        // Role Softway
                        v_properties_profile.setProperty("ROLE", Utils.nvl(rs.getString("S_ROLE"), ""));

                        // Usu�rio/Senha Sombra
                        v_properties_profile.setProperty("USUARIOSOMBRA", Utils.nvl(rs.getString("S_USER"), ""));
                        if (rs.getString("S_CRIPTO").equals("S")) {
                            v_properties_profile.setProperty("SENHASOMBRA", Utils.nvl(Utils.decrypt(rs.getString("S_PASS")), ""));
                        } else {
                            v_properties_profile.setProperty("SENHASOMBRA", Utils.nvl(rs.getString("S_PASS"), ""));
                        }
                    }

                    v_string_product_name = this.ReturnProductByCod(rs.getInt("COD_SISTEMA") == 1000 ? 500 : rs.getInt("COD_SISTEMA"));
                    v_string_product_shortname = this.ReturnSiglaByCod(rs.getInt("COD_SISTEMA") == 1000 ? 500 : rs.getInt("COD_SISTEMA"));

                    v_properties_profile.setProperty("PRODUCT_INSTALL" + v_int_contador++, v_string_product_name);
                    v_properties_profile.setProperty(v_string_product_name + "_USER", Utils.nvl(rs.getString("S_SCHEMA_OWNER"), ""));
                    if (rs.getString("S_CRIPTO").equals("S")) {
                        v_properties_profile.setProperty(v_string_product_name + "_PASS", Utils.nvl(Utils.decrypt(rs.getString("S_OWNER_PASS")), ""));
                    } else {
                        v_properties_profile.setProperty(v_string_product_name + "_PASS", Utils.nvl(rs.getString("S_OWNER_PASS"), ""));
                    }
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_DATA_4K", Utils.nvl(rs.getString("S_TABLESPACE_DATA_4K"), "TR_" + v_string_product_shortname + "_DATA_4K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_DATA_64K", Utils.nvl(rs.getString("S_TABLESPACE_DATA_64K"), "TR_" + v_string_product_shortname + "_DATA_64K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_DATA_128K", Utils.nvl(rs.getString("S_TABLESPACE_DATA_128K"), "TR_" + v_string_product_shortname + "_DATA_128K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_DATA_512K", Utils.nvl(rs.getString("S_TABLESPACE_DATA_512K"), "TR_" + v_string_product_shortname + "_DATA_512K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_DATA_1M", Utils.nvl(rs.getString("S_TABLESPACE_DATA_1M"), "TR_" + v_string_product_shortname + "_DATA_1M"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_INDEX_4K", Utils.nvl(rs.getString("S_TABLESPACE_INDEX_4K"), "TR_" + v_string_product_shortname + "_INDEX_4K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_INDEX_64K", Utils.nvl(rs.getString("S_TABLESPACE_INDEX_64K"), "TR_" + v_string_product_shortname + "_INDEX_64K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_INDEX_128K", Utils.nvl(rs.getString("S_TABLESPACE_INDEX_128K"), "TR_" + v_string_product_shortname + "_INDEX_128K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_INDEX_512K", Utils.nvl(rs.getString("S_TABLESPACE_INDEX_512K"), "TR_" + v_string_product_shortname + "_INDEX_512K"));
                    v_properties_profile.setProperty(v_string_product_name + "_TBL_INDEX_1M", Utils.nvl(rs.getString("S_TABLESPACE_INDEX_1M"), "TR_" + v_string_product_shortname + "_INDEX_1M"));

                }
            } catch (Exception e) {
                return false;
            }

            v_outputstream_profile = new FileOutputStream(v_file_profile);
            v_properties_profile.store(v_outputstream_profile, "");

            v_outputstream_profile.close();

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param p_int_codproduct
     * @return 
     */
    private String ReturnProductByCod(int p_int_codproduct) {
        String v_string_productname = "";
        int v_int_product_name;
        Product v_sfwproduct_current;
        try {
            Iterator v_iterator_it = Install.get_product().iterator();

            while (v_iterator_it.hasNext()) {
                v_sfwproduct_current = (Product) v_iterator_it.next();
                v_int_product_name = Integer.parseInt(v_sfwproduct_current.get_code());

                if (v_int_product_name == p_int_codproduct) {
                    if (!v_sfwproduct_current.get_name().equals("TRACKING_SYS")) {
                        v_string_productname = v_sfwproduct_current.get_name();
                        break;
                    }
                }
            }
            return v_string_productname;
        } catch (Exception e) {
            return v_string_productname;
        }
    }

    /**
     * 
     * @param p_int_codproduct
     * @return 
     */
    private int ReturnCodByName(String p_string_productname) {
        int v_int_productcod = 999;
        Product v_sfwproduct_current;
        try {
            Iterator v_iterator_it = Install.get_product().iterator();

            while (v_iterator_it.hasNext()) {
                v_sfwproduct_current = (Product) v_iterator_it.next();

                if (p_string_productname.equals(v_sfwproduct_current.get_name())) {
                    v_int_productcod = Integer.parseInt(v_sfwproduct_current.get_code());
                    break;
                }
            }
            return v_int_productcod;
        } catch (Exception e) {
            return v_int_productcod;
        }
    }

    /**
     * 
     * @param p_int_codproduct
     * @return 
     */
    private String ReturnSiglaByCod(int p_int_codproduct) {
        String v_string_productsigla = "";
        int v_int_product_name;
        Product v_sfwproduct_current;
        try {
            Iterator v_iterator_it = Install.get_product().iterator();

            while (v_iterator_it.hasNext()) {
                v_sfwproduct_current = (Product) v_iterator_it.next();
                v_int_product_name = Integer.parseInt(v_sfwproduct_current.get_code());

                if (v_int_product_name == p_int_codproduct) {
                    if (!v_sfwproduct_current.get_name().equals("TRACKING_SYS")) {
                        v_string_productsigla = v_sfwproduct_current.get_shortname();
                        break;
                    }
                }
            }
            return v_string_productsigla;
        } catch (Exception e) {
            return v_string_productsigla;
        }
    }

    /**
     * Valida se existe o usuário tracking.
     * @return
     */
    private String ValidadeProductNotSelected() {
        String v_string_result = "";
        OracleConnection v_conn = null;
        SfwOracleDefineUser v_sfworacleproductdefine_pd = null;
        Iterator v_iterator_it;
        try {
            v_iterator_it = Install.get_productdefine().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacleproductdefine_pd = (SfwOracleDefineUser) v_iterator_it.next();
                if (!v_sfworacleproductdefine_pd.getProduct().get_name().equals("TRACKING_SYS")) {
                    v_conn = new OracleConnection();
                    v_conn.set_tns(this.get_tns());
                    v_conn.set_username(v_sfworacleproductdefine_pd.getUser());
                    v_conn.set_password(v_sfworacleproductdefine_pd.getPassword());
                    v_conn.ConnectAux();
                    break;
                }
            }
        } catch (Exception e) {
            v_string_result = v_sfworacleproductdefine_pd.getUser();
            return v_string_result;
        } finally {
            v_conn.Close();
        }
        return v_string_result;
    }

    /**
     * Valida se existe o usuário tracking.
     * @return 
     */
    private String TrackingValidate() {
        String v_string_result = "";
        OracleConnection v_conn = null;
        SfwOracleDefineUser v_sfworacleproductdefine_pd = null;
        Iterator v_iterator_it;
        try {
            v_iterator_it = Install.get_productdefine().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacleproductdefine_pd = (SfwOracleDefineUser) v_iterator_it.next();
                if (v_sfworacleproductdefine_pd.getProduct().get_name().equals("TRACKING_SYS")) {
                    v_conn = new OracleConnection();
                    v_conn.set_tns(this.get_tns());
                    v_conn.set_username(v_sfworacleproductdefine_pd.getUser());
                    v_conn.set_password(v_sfworacleproductdefine_pd.getPassword());
                    v_conn.ConnectAux();
                    break;
                }
            }
        } catch (Exception e) {
            v_string_result = v_sfworacleproductdefine_pd.getUser();
            return v_string_result;
        } finally {
            v_conn.Close();
        }
        return v_string_result;
    }

    /**
     * Verifica se os usu�rios n�o escolhidos na instala��o
     * mais que existem na sfw_cm_schema s�o v�lidos.
     * @return 
     */
    public OracleValidationResult ValidateRole(SfwOracleProductDetail pd, String tns) {
        OracleValidationResult v_oraclevalidationresult_retorno = new OracleValidationResult();

        try {
            int aux = this.Insert("set role " + Install.get_rolesfw());
        } catch (Exception e) {
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            v_oraclevalidationresult_retorno.set_text("> A role " + Install.get_rolesfw() + " n�o existe.\n Verificar exist�ncia da mesma na tela de \"ROLE SOFTWAY\".\n" + System.getProperty("line.separator"));
            return v_oraclevalidationresult_retorno;
        }

        OracleConnection con = null;
        try {
            con = new OracleConnection();
            con.set_username(Profile.getUsuariosombra());
            con.set_password(Profile.getSenhasombra());
            con.set_tns(tns);
            con.ConnectAux();
            int aux = con.Insert("set role " + Install.get_rolesfw());
            ResultSet rs = con.Query("SELECT LOWER(DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW('cm'), 3)) FROM DUAL");
        } catch (Exception e) {
            v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.ERROR);
            v_oraclevalidationresult_retorno.set_text(String.format(Utils.getDefaultBundle().getString("Validation.dbmscrypto"), Install.get_rolesfw().toUpperCase()) + "" + System.getProperty("line.separator"));
            return v_oraclevalidationresult_retorno;
        } finally {
            con.Close();
        }

        v_oraclevalidationresult_retorno.set_result(OracleValidationResult.Result.SUCCESS);
        v_oraclevalidationresult_retorno.set_text("");
        return v_oraclevalidationresult_retorno;
    }

    /**
     * Cria tabela SFW_CM_SCHEMA caso n�o exista!
     */
    public void createTableSfwCmSchema() throws SQLException {
        //
        // String para cria��o da tabela SFW_CM_SCHEMA
        //
        String v_createtable_sfwcmschema = "create table SFW_CM_SCHEMA ( "
                + "COD_SISTEMA                NUMBER not null, "
                + "S_ICONE_LOGIN_CENTRALIZADO VARCHAR2(250), "
                + "S_USER                     VARCHAR2(100), "
                + "S_PASS                     VARCHAR2(100), "
                + "S_ROLE                     VARCHAR2(100), "
                + "S_ROLE_PASS                VARCHAR2(100), "
                + "S_SCHEMA_OWNER             VARCHAR2(100), "
                + "S_OWNER_PASS               VARCHAR2(100), "
                + "S_TABLESPACE_DATA_4K       VARCHAR2(100), "
                + "S_TABLESPACE_DATA_64K      VARCHAR2(100), "
                + "S_TABLESPACE_DATA_128K     VARCHAR2(100), "
                + "S_TABLESPACE_DATA_512K     VARCHAR2(100), "
                + "S_TABLESPACE_DATA_1M       VARCHAR2(100), "
                + "S_TABLESPACE_INDEX_4K      VARCHAR2(100), "
                + "S_TABLESPACE_INDEX_64K     VARCHAR2(100), "
                + "S_TABLESPACE_INDEX_128K    VARCHAR2(100), "
                + "S_TABLESPACE_INDEX_512K    VARCHAR2(100), "
                + "S_TABLESPACE_INDEX_1M      VARCHAR2(100), "
                + "S_TNS                      VARCHAR2(100), "
                + "S_SIGLA                    VARCHAR2(30), "
                + "S_CRIPTO                   CHAR(1) default 'N')";
        //
        // Alterando a tabela para criar a PK
        //
        String v_createpk = "alter table SFW_CM_SCHEMA "
                + "add constraint PK_SFW_CM_SCHEMA primary key (COD_SISTEMA)";
        //
        // Alterando a tabela para criar a FK
        //
        String v_createfk = "alter table SFW_CM_SCHEMA "
                + "add constraint FK_SFW_SIS_TO_SFW_CM_SC foreign key (COD_SISTEMA) "
                + "references SFW_SISTEMA (COD_SISTEMA)";
        //
        // Procura pela tabela, caso não exista cria.
        //
        ResultSet rs = this.Query("SELECT COUNT(1) COUNT FROM USER_TABLES UT WHERE UT.TABLE_NAME = 'SFW_CM_SCHEMA'");
        if (rs.next()) {
            if (rs.getInt("COUNT") == 0) {
                //
                // executa a cria��o da tabela
                //
                this.Update(v_createtable_sfwcmschema);
                //
                // executa a cria��o da pk
                //
                this.Update(v_createpk);
                //
                // executa a cria��o da fk
                //
                this.Update(v_createfk);
            }
        }
    }

    public Connection connectBeforeAfterValidations() throws SQLException {
        Iterator v_iterator_it;
        try {
            v_iterator_it = Install.get_productsdetail().iterator();
            while (v_iterator_it.hasNext()) {
                SfwOracleProductDetail v_sfworacleproduct = (SfwOracleProductDetail) v_iterator_it.next();
                if (v_sfworacleproduct.getProduct().get_name().equals("SOFTCOMEX")) {
                    this.set_username(v_sfworacleproduct.getUser());
                    this.set_password(v_sfworacleproduct.getPassword());
                    this.set_tns(Install.get_tns());
                    break;
                }
            }
            return this.ConnectAux();
        } catch (Exception e) {
            v_iterator_it = Install.get_productdefine().iterator();
            while (v_iterator_it.hasNext()) {
                SfwOracleDefineUser v_sfworacledefineuser = (SfwOracleDefineUser) v_iterator_it.next();
                if (v_sfworacledefineuser.getProduct().get_name().equals("SOFTCOMEX")) {
                    this.set_username(v_sfworacledefineuser.getUser());
                    this.set_password(v_sfworacledefineuser.getPassword());
                    this.set_tns(Install.get_tns());
                    break;
                }
            }
            return this.ConnectAux();
        }
    }
}
