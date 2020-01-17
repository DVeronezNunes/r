package sfwinstaladorscript.comparabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import sfwinstaladorscript.comparabase.objects.Column;
import sfwinstaladorscript.comparabase.objects.Constraint;
import sfwinstaladorscript.comparabase.objects.Function;
import sfwinstaladorscript.comparabase.objects.Index;
import sfwinstaladorscript.comparabase.objects.Info;
import sfwinstaladorscript.comparabase.objects.Procedure;
import sfwinstaladorscript.comparabase.objects.Sequence;
import sfwinstaladorscript.comparabase.objects.Table;
import sfwinstaladorscript.comparabase.objects.Trigger;
import sfwinstaladorscript.comparabase.objects.View;
import sfwinstaladorscript.comparabase.objects.Package;
import sfwinstaladorscript.comparabase.objects.Package_Body;
import sfwinstaladorscript.database.OracleConnection;

public class SfwXMLBuilder {

    private OracleConnection con;
    private String username;
    private String password;
    private String tns;

    public SfwXMLBuilder(String username, String password, String tns) {
        this.username = username;
        this.password = password;
        this.tns = tns;
    }

    @SuppressWarnings({"rawtypes", "null"})
    public boolean WriteXML(Info info, String comparacao) throws IOException {
        // Criando um objeto XStream
        XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
        // Declarando variáveis uteis
        List v_listTable = null;
        List v_listColumn = null;
        List v_listView = null;
        List v_listSequence = null;
        List v_listTrigger = null;
        List v_listConstraint = null;
        List v_listIndex = null;
        List v_listProcedure = null;
        List v_listPackage = null;
        List v_listPackage_Body = null;
        List v_listFunction = null;
        //
        List<Object> contatos = new ArrayList<Object>();

        con = new OracleConnection();
        con.set_username(this.username);
        con.set_password(this.password);
        con.set_tns(this.tns);

        contatos.add(info);
        // Listando todas tabelas da base;
        try {
            v_listTable = this.BuildXML("TABLE");
            for (int i = 0; i < v_listTable.size(); i++) {
                contatos.add(v_listTable.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (TABLES)");
            e.printStackTrace();
        }
        //
        // Listando todas views da base;
        try {
            v_listView = this.BuildXML("VIEW");
            for (int i = 0; i < v_listView.size(); i++) {
                contatos.add(v_listView.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (VIEW)");
            e.printStackTrace();
        }
        //
        // Listando todas colunas da base;
        try {
            v_listColumn = this.BuildXML("COLUMN");
            for (int i = 0; i < v_listColumn.size(); i++) {
                contatos.add(v_listColumn.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (COLUMN)");
            e.printStackTrace();
        }
        //
        // Listando todas constraints da base;
        try {
            v_listConstraint = this.BuildXML("CONSTRAINT");
            for (int i = 0; i < v_listConstraint.size(); i++) {
                contatos.add(v_listConstraint.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (CONSTRAINT)");
            e.printStackTrace();
        }
        //
        // Listando todos os index da base;
        try {
            v_listIndex = this.BuildXML("INDEX");
            for (int i = 0; i < v_listIndex.size(); i++) {
                contatos.add(v_listIndex.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (INDEX)");
            e.printStackTrace();
        }
        //
        // Listando todas packages da base;
        try {
            v_listPackage = this.BuildXML("PACKAGE");
            for (int i = 0; i < v_listPackage.size(); i++) {
                contatos.add(v_listPackage.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (PACKAGE)");
            e.printStackTrace();
        }
        //
        // Listando todas packages da base;
        try {
            v_listPackage_Body = this.BuildXML("PACKAGE_BODY");
            for (int i = 0; i < v_listPackage_Body.size(); i++) {
                contatos.add(v_listPackage_Body.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (PACKAGE_BODY)");
            e.printStackTrace();
        }
        //
        // Listando todas procedures da base;
        try {
            v_listProcedure = this.BuildXML("PROCEDURE");
            for (int i = 0; i < v_listProcedure.size(); i++) {
                contatos.add(v_listProcedure.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (PROCEDURE)");
            e.printStackTrace();
        }
        //
        // Listando todas sequences da base;
        try {
            v_listSequence = this.BuildXML("SEQUENCE");
            for (int i = 0; i < v_listSequence.size(); i++) {
                contatos.add(v_listSequence.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (SEQUENCE)");
            e.printStackTrace();
        }
        //
        // Listando todas triggers da base;
        try {
            v_listTrigger = this.BuildXML("TRIGGER");
            for (int i = 0; i < v_listTrigger.size(); i++) {
                contatos.add(v_listTrigger.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (TRIGGER)");
            e.printStackTrace();
        }
        //
        // Listando todas functions da base;
        try {
            v_listFunction = this.BuildXML("FUNCTION");
            for (int i = 0; i < v_listFunction.size(); i++) {
                contatos.add(v_listFunction.get(i));
            }
        } catch (Exception e) {
            System.out.println("erro ao gerar XML (FUNCTION)");
            e.printStackTrace();
        }
        //
        xstream.alias("TABLE", Table.class);
        xstream.alias("PROCEDURE", Procedure.class);
        xstream.alias("VIEW", View.class);
        xstream.alias("CONSTRAINT", Constraint.class);
        xstream.alias("COLUMN", Column.class);
        xstream.alias("INDEX", Index.class);
        xstream.alias("PACKAGE", Package.class);
        xstream.alias("PACKAGE_BODY", Package_Body.class);
        xstream.alias("SEQUENCE", Sequence.class);
        xstream.alias("TRIGGER", Trigger.class);
        xstream.alias("FUNCTION", Function.class);
        xstream.alias("SchemaInfo", List.class);
        xstream.alias("INFO", Info.class);

        // Passando os dados de Objetos Java para XML
        String contatosEmXML = xstream.toXML(contatos);

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(comparacao)));
        bw.write(contatosEmXML);
        bw.flush();
        bw.close();

        return true;
    }

    @SuppressWarnings("rawtypes")
    private List BuildXML(String object_type) throws Exception {
        try {
            con.Connect();
            if (object_type.equals("TABLE")) {
                SfwProcessoComparacao.sysout("Lendo tabelas.");
                List<Table> v_listTable = new ArrayList<Table>();
                Table v_table;
                ResultSet rs = con.Query("SELECT TABLE_NAME FROM USER_TABLES ORDER BY TABLE_NAME");
                while (rs.next()) {
                    v_table = new Table();
                    v_table.set_tableName(rs.getString("TABLE_NAME"));
                    v_listTable.add(v_table);
                }
                return v_listTable;
            } else if (object_type.equals("COLUMN")) {
                SfwProcessoComparacao.sysout("Lendo Colunas.");
                List<Column> v_listColumn = new ArrayList<Column>();
                Column v_column;
                ResultSet rs = con.Query("SELECT TABLE_NAME || '.' || COLUMN_NAME COLUMN_NAME, DATA_TYPE "
                        + "FROM USER_TAB_COLUMNS A "
                        + "WHERE A.TABLE_NAME NOT IN (SELECT B.VIEW_NAME FROM USER_VIEWS B) "
                        + "ORDER BY TABLE_NAME, COLUMN_ID");
                while (rs.next()) {
                    v_column = new Column();
                    v_column.set_columnName(rs.getString("COLUMN_NAME"));
                    v_column.set_dataType(rs.getString("DATA_TYPE"));
                    v_listColumn.add(v_column);
                }
                this.con.CloseStatement();
                return v_listColumn;
            } else if (object_type.equals("CONSTRAINT")) {
                SfwProcessoComparacao.sysout("Lendo Constraints.");
                List<Constraint> v_listConstraint = new ArrayList<Constraint>();
                Constraint v_constraint;
                ResultSet rs = con.Query("SELECT A.TABLE_NAME || '.' || A.CONSTRAINT_NAME CONSTRAINT_NAME, A.CONSTRAINT_TYPE "
                        + "FROM USER_CONSTRAINTS A "
                        + "WHERE A.CONSTRAINT_NAME NOT LIKE '%SYS_%' "
                        + "ORDER BY 1");
                while (rs.next()) {
                    v_constraint = new Constraint();
                    v_constraint.set_constraintName(rs.getString("CONSTRAINT_NAME"));
                    v_constraint.set_constraintType(rs.getString("CONSTRAINT_TYPE"));
                    v_listConstraint.add(v_constraint);
                }
                this.con.CloseStatement();
                return v_listConstraint;
            } else if (object_type.equals("INDEX")) {
                SfwProcessoComparacao.sysout("Lendo Indexs.");
                List<Index> v_listIndex = new ArrayList<Index>();
                Index v_index;
                ResultSet rs = con.Query("SELECT TABLE_NAME||'.'||INDEX_NAME INDEX_NAME FROM USER_INDEXES B WHERE B.INDEX_TYPE != 'LOB' AND B.INDEX_NAME NOT LIKE '%SYS_%' AND INDEX_NAME NOT IN (SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS)");
                while (rs.next()) {
                    v_index = new Index();
                    v_index.set_indexName(rs.getString("INDEX_NAME"));
                    v_listIndex.add(v_index);
                }
                this.con.CloseStatement();
                return v_listIndex;
            } else if (object_type.equals("PACKAGE")) {
                SfwProcessoComparacao.sysout("Lendo Packages.");
                List<Package> v_listPackage = new ArrayList<Package>();
                Package v_package;
                ResultSet rs = con.Query("SELECT OBJECT_NAME NAME FROM USER_OBJECTS WHERE OBJECT_TYPE IN ('PACKAGE') ORDER BY 1");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_pkgName = rs.getString("NAME");
                    v_package = new Package();
                    v_package.set_packageName(rs.getString("NAME"));
                    ResultSet rs_qtde = con.Query("SELECT SUM(LENGTH(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(A.TEXT, CHR(10), ''),CHR(32),''),'\"',''),chr(9))))) AS CARACTER_COUNT FROM USER_SOURCE A WHERE A.NAME = '"
                            + v_pkgName + "' AND A.TYPE = 'PACKAGE' AND A.LINE > 1");
                    if (rs_qtde.next()) {
                        v_package.set_lineCount(rs_qtde.getInt("CARACTER_COUNT"));
                    }
                    rs_qtde.close();
                    con.CloseStatement();
                    v_listPackage.add(v_package);
                }
                this.con.CloseStatement();
                return v_listPackage;
            } else if (object_type.equals("PACKAGE_BODY")) {
                SfwProcessoComparacao.sysout("Lendo Packages Body.");
                List<Package_Body> v_listPackage = new ArrayList<Package_Body>();
                Package_Body v_package;
                ResultSet rs = con.Query("SELECT OBJECT_NAME NAME FROM USER_OBJECTS WHERE OBJECT_TYPE IN ('PACKAGE BODY') ORDER BY 1");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_pkgName = rs.getString("NAME");
                    v_package = new Package_Body();
                    v_package.setNAME(rs.getString("NAME"));
                    ResultSet rs_qtde = con.Query("SELECT SUM(LENGTH(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(A.TEXT, CHR(10), ''),CHR(32),''),'\"',''),chr(9))))) AS CARACTER_COUNT FROM USER_SOURCE A WHERE A.NAME = '"
                            + v_pkgName + "' AND A.TYPE = 'PACKAGE BODY' AND A.LINE > 1");
                    if (rs_qtde.next()) {
                        v_package.setLINE_COUNT(rs_qtde.getInt("CARACTER_COUNT"));
                    }
                    rs_qtde.close();
                    con.CloseStatement();
                    v_listPackage.add(v_package);
                }
                this.con.CloseStatement();
                return v_listPackage;
            } else if (object_type.equals("PROCEDURE")) {
                SfwProcessoComparacao.sysout("Lendo Procedures.");
                List<Procedure> v_listProcedure = new ArrayList<Procedure>();
                Procedure v_procedure;
                ResultSet rs = con.Query("SELECT OBJECT_NAME NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PROCEDURE' ORDER BY 1");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_procName = rs.getString("NAME");
                    v_procedure = new Procedure();
                    v_procedure.set_procedureName(v_procName);
                    ResultSet rs_qtde = con.Query("SELECT SUM(LENGTH(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(A.TEXT, CHR(10), ''),CHR(32),''),'\"',''),chr(9))))) AS CARACTER_COUNT FROM USER_SOURCE A WHERE A.NAME = '"
                            + v_procName + "' AND A.LINE > 1");
                    if (rs_qtde.next()) {
                        v_procedure.setCARACTER_COUNT(rs_qtde.getInt("CARACTER_COUNT"));
                    }
                    rs_qtde.close();
                    con.CloseStatement();
                    v_listProcedure.add(v_procedure);
                }
                con.Close();
                this.con.CloseStatement();
                return v_listProcedure;
            } else if (object_type.equals("SEQUENCE")) {
                SfwProcessoComparacao.sysout("Lendo Sequences.");
                List<Sequence> v_listSequence = new ArrayList<Sequence>();
                Sequence v_sequence;
                ResultSet rs = con.Query("SELECT SEQUENCE_NAME FROM USER_SEQUENCES ORDER BY 1");
                while (rs.next()) {
                    v_sequence = new Sequence();
                    v_sequence.set_sequenceName(rs.getString("SEQUENCE_NAME"));
                    v_listSequence.add(v_sequence);
                }
                this.con.CloseStatement();
                return v_listSequence;
            } else if (object_type.equals("TRIGGER")) {

                SfwProcessoComparacao.sysout("Lendo Triggers.");
                List<Trigger> v_listTrigger = new ArrayList<Trigger>();
                Trigger v_trigger;
                ResultSet rs = con.Query("SELECT TRIGGER_NAME, TRIGGER_TYPE, TRIGGERING_EVENT, TABLE_NAME FROM USER_TRIGGERS ORDER BY TABLE_NAME");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_trigger_name = rs.getString("TRIGGER_NAME");
                    v_trigger = new Trigger();
                    v_trigger.set_triggerName(v_trigger_name);
                    v_trigger.set_triggerType(rs.getString("TRIGGER_TYPE"));
                    v_trigger.set_triggerEvent(rs.getString("TRIGGERING_EVENT"));
                    v_trigger.set_tableName(rs.getString("TABLE_NAME"));
                    ResultSet rs_qtde = con.Query("SELECT SUM(LENGTH(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(A.TEXT, CHR(10), ''),CHR(32),''),'\"',''),chr(9))))) AS CARACTER_COUNT FROM USER_SOURCE A WHERE A.NAME = '"
                            + v_trigger_name + "' AND A.LINE > 1");
                    if (rs_qtde.next()) {
                        v_trigger.setCARACTER_COUNT(rs_qtde.getInt("CARACTER_COUNT"));
                    }
                    rs_qtde.close();
                    con.CloseStatement();
                    v_listTrigger.add(v_trigger);
                }
                con.Close();
                this.con.CloseStatement();
                return v_listTrigger;
            } else if (object_type.equals("VIEW")) {
                SfwProcessoComparacao.sysout("Lendo Views.");
                List<View> v_listView = new ArrayList<View>();
                View v_view;
                ResultSet rs = con.Query("SELECT VIEW_NAME FROM USER_VIEWS");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_viewName = rs.getString("VIEW_NAME");
                    v_view = new View();
                    v_view.set_viewName(v_viewName);
                    v_view.set_textLength(this.returnViewCaracter(con, v_viewName));
                    v_listView.add(v_view);
                }
                this.con.CloseStatement();
                return v_listView;
            } else if (object_type.equals("FUNCTION")) {
                SfwProcessoComparacao.sysout("Lendo Functions.");
                List<Function> v_listFunction = new ArrayList<Function>();
                Function v_function;
                ResultSet rs = con.Query("SELECT OBJECT_NAME NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'FUNCTION' ORDER BY 1");
                OracleConnection con = new OracleConnection();
                con.set_username(this.username);
                con.set_password(this.password);
                con.set_tns(this.tns);
                con.Connect();
                while (rs.next()) {
                    String v_funcName = rs.getString("NAME");
                    v_function = new Function();
                    v_function.set_functionName(v_funcName);
                    ResultSet rs_qtde = con.Query("SELECT SUM(LENGTH(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(A.TEXT, CHR(10), ''),CHR(32),''),'\"',''),chr(9))))) AS CARACTER_COUNT FROM USER_SOURCE A WHERE A.NAME = '"
                            + v_funcName + "' AND A.LINE > 1");
                    while (rs_qtde.next()) {
                        v_function.setCARACTER_COUNT(rs_qtde.getInt("CARACTER_COUNT"));
                    }
                    rs_qtde.close();
                    con.CloseStatement();
                    v_listFunction.add(v_function);
                }
                con.Close();
                this.con.CloseStatement();
                return v_listFunction;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return null;
    }

    private int returnViewCaracter(OracleConnection con, String view_name) throws SQLException {
        String v_view_text = "";
        ResultSet rs_qtde = con.Query("select text from user_views where view_name = '" + view_name + "'");
        while (rs_qtde.next()) {
            v_view_text = rs_qtde.getString("text").replaceAll("\\s+", "");
        }
        rs_qtde.close();
        con.CloseStatement();
        v_view_text = v_view_text.trim();
        return v_view_text.length();
    }
}
