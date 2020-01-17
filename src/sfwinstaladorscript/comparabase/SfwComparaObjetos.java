package sfwinstaladorscript.comparabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sfwinstaladorscript.comparabase.objects.BaseCompare;
import sfwinstaladorscript.comparabase.objects.Column;
import sfwinstaladorscript.comparabase.objects.CompareErro;
import sfwinstaladorscript.comparabase.objects.Constraint;
import sfwinstaladorscript.comparabase.objects.Function;
import sfwinstaladorscript.comparabase.objects.Index;
import sfwinstaladorscript.comparabase.objects.Procedure;
import sfwinstaladorscript.comparabase.objects.Sequence;
import sfwinstaladorscript.comparabase.objects.Table;
import sfwinstaladorscript.comparabase.objects.Trigger;
import sfwinstaladorscript.comparabase.objects.View;
import sfwinstaladorscript.comparabase.objects.Package;
import sfwinstaladorscript.comparabase.objects.Package_Body;
import sfwinstaladorscript.database.OracleConnection;

public class SfwComparaObjetos {

    private List<String> v_list_tables_faltantes = new ArrayList<String>();
    private List<String> v_list_tables_amais = new ArrayList<String>();
    private List<CompareErro> v_list_CompareErro = new ArrayList<CompareErro>();
    private String caminhoComparacao;
    private OracleConnection con = null;

    public void DataBaseCompare(BaseCompare origem, BaseCompare destino, String caminhoComparacao, OracleConnection con) throws IOException {
        this.con = con;
        this.caminhoComparacao = caminhoComparacao + "\\Comparacao_" + new SimpleDateFormat("yyyyMMddkmm").format(new Date()) + "_"
                + origem.get_stringVersao();
        File v_fileComparacao = new File(this.caminhoComparacao);
        if (!v_fileComparacao.exists()) {
            v_fileComparacao.mkdirs();
        }
        v_fileComparacao.createNewFile();
        BufferedWriter v_bwLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(v_fileComparacao.getPath() + "\\resultado_comparacao.txt")), "ISO-8859-1"));
        StringBuilder v_sbLog = new StringBuilder();
        v_sbLog.append("-- Inicio da Comparacao de Base \r\n\n");
        SfwProcessoComparacao.sysout("-- Comparando PROCEDURES");
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listProcedure().size());
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaProcedure(origem.get_listProcedure(), destino.get_listProcedure(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Comparando FUNCTIONS");
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listFunction().size());
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaFunction(origem.get_listFunction(), destino.get_listFunction(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Comparando TABLES");
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listTable().size());
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaTable(origem.get_listTable(), destino.get_listTable(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Comparando VIEWS");
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listView().size());
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaView(origem.get_listView(), destino.get_listView(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listColumn().size());
        SfwProcessoComparacao.sysout("-- Comparando COLUMNS");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaColumn(origem.get_listColumn(), destino.get_listColumn(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listConstraint().size());
        SfwProcessoComparacao.sysout("-- Comparando CONSTRAINTS");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaConstraint(origem.get_listConstraint(), destino.get_listConstraint(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listIndex().size());
        SfwProcessoComparacao.sysout("-- Comparando INDEXS");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaIndex(origem.get_listIndex(), destino.get_listIndex(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listPackage().size());
        SfwProcessoComparacao.sysout("-- Comparando PACKAGES");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaPackage(origem.get_listPackage(), destino.get_listPackage(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listPackage_Body().size());
        SfwProcessoComparacao.sysout("-- Comparando PACKAGES BODY");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaPackageBody(origem.get_listPackage_Body(), destino.get_listPackage_Body(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listSequence().size());
        SfwProcessoComparacao.sysout("-- Comparando SEQUENCES");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaSequence(origem.get_listSequence(), destino.get_listSequence(), v_sbLog);
        SfwProcessoComparacao.sysout("-- Quantidade: " + origem.get_listTrigger().size());
        SfwProcessoComparacao.sysout("-- Comparando TRIGGERS");
        SfwProcessoComparacao.sysout("-- ");
        this.DiferencaTrigger(origem.get_listTrigger(), destino.get_listTrigger(), v_sbLog);
        v_sbLog.append("\n-- Fim da Comparação de Base \r\n");
        v_bwLog.write(v_sbLog.toString());
        v_bwLog.flush();
        v_bwLog.close();
    }

    /**
     * Função para verificar as diferenças das procedures
     * 
     * @param objetosOrigem
     *          Lista de Procedures da base de Origem
     * @param objetosDestino
     *          Lista de Procedures da base de Destino
     * @return
     */
    public boolean DiferencaProcedure(List<Procedure> objetosOrigem, List<Procedure> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Procedure procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Procedure procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureOrigem.equalsTextLength(procedureDestino)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureOrigem.get_procedureName());
                            compareErro.setObject_name(procedureOrigem.get_procedureName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("PROCEDURE");
                            this.copyObject(procedureDestino, compareErro.getObject_erro());
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_procedureName());
                    compareErro.setObject_name(procedureOrigem.get_procedureName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("PROCEDURE");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
        }
        //
        try {
            for (Procedure procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Procedure procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_procedureName());
                    compareErro.setObject_name(procedureDestino.get_procedureName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("PROCEDURE");
                    this.copyObject(procedureDestino, compareErro.getObject_erro());
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## PROCEDURES ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Função para verificar as diferenças das functions
     * 
     * @param objetosOrigem
     *          Lista de Function da base de Origem
     * @param objetosDestino
     *          Lista de Function da base de Destino
     * @return
     */
    public boolean DiferencaFunction(List<Function> objetosOrigem, List<Function> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Function procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Function procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureOrigem.equalsTextLength(procedureDestino)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureOrigem.get_functionName());
                            compareErro.setObject_name(procedureOrigem.get_functionName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("FUNCTION");
                            this.copyObject(procedureDestino, compareErro.getObject_erro());
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_functionName());
                    compareErro.setObject_name(procedureOrigem.get_functionName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("FUNCTION");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Function procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Function procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_functionName());
                    compareErro.setObject_name(procedureDestino.get_functionName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("FUNCTION");
                    this.copyObject(procedureDestino, compareErro.getObject_erro());
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## FUNCTIONS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }

        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Tables
     * 
     * @param objetosOrigem
     *          Lista de Tables da base de Origem
     * @param objetosDestino
     *          Lista de Tables da base de Destino
     * @return
     */
    public boolean DiferencaTable(List<Table> objetosOrigem, List<Table> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Table procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Table procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_tableName());
                    v_list_tables_faltantes.add(procedureOrigem.get_tableName());
                    compareErro.setObject_name(procedureOrigem.get_tableName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("TABLE");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Table procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Table procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_tableName());
                    v_list_tables_amais.add(procedureDestino.get_tableName());
                    compareErro.setObject_name(procedureDestino.get_tableName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("TABLE");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## TABLES ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das View
     * 
     * @param objetosOrigem
     *          Lista de View da base de Origem
     * @param objetosDestino
     *          Lista de View da base de Destino
     * @return
     */
    public boolean DiferencaView(List<View> objetosOrigem, List<View> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (View procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (View procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureOrigem.equalsTextLength(procedureDestino)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureOrigem.get_viewName());
                            compareErro.setObject_name(procedureOrigem.get_viewName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("VIEW");
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    if (!procedureOrigem.get_viewName().contains("V_IO_")) {
                        compareErro = new CompareErro();
                        v_listErros.add("FALTANTE: " + procedureOrigem.get_viewName());
                        compareErro.setObject_name(procedureOrigem.get_viewName());
                        compareErro.setObject_erro("FALTANTE");
                        compareErro.setObject_type("VIEW");
                        v_list_CompareErro.add(compareErro);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (View procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (View procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    if (!procedureDestino.get_viewName().contains("V_IO_")) {
                        compareErro = new CompareErro();
                        v_listErros.add("A MAIS: " + procedureDestino.get_viewName());
                        compareErro.setObject_name(procedureDestino.get_viewName());
                        compareErro.setObject_erro("A MAIS");
                        compareErro.setObject_type("VIEW");
                        v_list_CompareErro.add(compareErro);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## VIEWS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Column
     * 
     * @param objetosOrigem
     *          Lista de Column da base de Origem
     * @param objetosDestino
     *          Lista de Column da base de Destino
     * @return
     */
    public boolean DiferencaColumn(List<Column> objetosOrigem, List<Column> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Column procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Column procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    if (!v_list_tables_faltantes.contains(procedureOrigem.get_columnName().substring(0, procedureOrigem.get_columnName().indexOf(".")))) {
                        compareErro = new CompareErro();
                        v_listErros.add("FALTANTE: " + procedureOrigem.get_columnName());
                        compareErro.setObject_name(procedureOrigem.get_columnName());
                        compareErro.setObject_erro("FALTANTE");
                        compareErro.setObject_type("COLUMN");
                        v_list_CompareErro.add(compareErro);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Column procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Column procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        if (!procedureDestino.equalsDataType(procedureOrigem)) {
                            compareErro = new CompareErro();
                            v_listErros.add("TIPO DIVERGENTE: " + procedureDestino.get_columnName());
                            compareErro.setObject_name(procedureDestino.get_columnName());
                            compareErro.setObject_erro("TIPO DIVERGENTE");
                            compareErro.setObject_type("COLUMN");
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    if (!v_list_tables_amais.contains(procedureDestino.get_columnName().substring(0, procedureDestino.get_columnName().indexOf(".")))) {
                        compareErro = new CompareErro();
                        v_listErros.add("A MAIS: " + procedureDestino.get_columnName());
                        compareErro.setObject_name(procedureDestino.get_columnName());
                        compareErro.setObject_erro("A MAIS");
                        compareErro.setObject_type("COLUMN");
                        v_list_CompareErro.add(compareErro);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## COLUMNS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Constraint
     * 
     * @param objetosOrigem
     *          Lista de Constraint da base de Origem
     * @param objetosDestino
     *          Lista de Constraint da base de Destino
     * @return
     */
    public boolean DiferencaConstraint(List<Constraint> objetosOrigem, List<Constraint> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Constraint procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Constraint procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureDestino.equalsType(procedureOrigem)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureDestino.get_constraintName());
                            compareErro.setObject_name(procedureDestino.get_constraintName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("CONSTRAINT");
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_constraintName());
                    compareErro.setObject_name(procedureOrigem.get_constraintName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("CONSTRAINT");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Constraint procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Constraint procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_constraintName());
                    compareErro.setObject_name(procedureDestino.get_constraintName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("CONSTRAINT");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## CONSTRAINTS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Constraint
     * 
     * @param objetosOrigem
     *          Lista de Constraint da base de Origem
     * @param objetosDestino
     *          Lista de Constraint da base de Destino
     * @return
     */
    public boolean DiferencaIndex(List<Index> objetosOrigem, List<Index> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Index procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Index procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_indexName());
                    compareErro.setObject_name(procedureOrigem.get_indexName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("INDEX");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Index procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Index procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_indexName());
                    compareErro.setObject_name(procedureDestino.get_indexName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("INDEX");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## INDEXS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Package
     * 
     * @param objetosOrigem
     *          Lista de Package da base de Origem
     * @param objetosDestino
     *          Lista de Package da base de Destino
     * @return
     */
    public boolean DiferencaPackage(List<Package> objetosOrigem, List<Package> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Package procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Package procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureDestino.equalsLineCount(procedureOrigem)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureDestino.get_packageName());
                            compareErro.setObject_name(procedureDestino.get_packageName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("PACKAGE");
                            this.copyObject(procedureDestino, compareErro.getObject_erro());
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_packageName());
                    compareErro.setObject_name(procedureOrigem.get_packageName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("PACKAGE");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Package procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Package procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_packageName());
                    compareErro.setObject_name(procedureDestino.get_packageName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("PACKAGE");
                    this.copyObject(procedureDestino, compareErro.getObject_erro());
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## PACKAGES ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    public boolean DiferencaPackageBody(List<Package_Body> objetosOrigem, List<Package_Body> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Package_Body procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Package_Body procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureDestino.equalsLineCount(procedureOrigem)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureDestino.getNAME());
                            compareErro.setObject_name(procedureDestino.getNAME());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("PACKAGE BODY");
                            this.copyObject(procedureDestino, compareErro.getObject_erro());
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.getNAME());
                    compareErro.setObject_name(procedureOrigem.getNAME());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("PACKAGE BODY");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Package_Body procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Package_Body procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.getNAME());
                    compareErro.setObject_name(procedureDestino.getNAME());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("PACKAGE BODY");
                    this.copyObject(procedureDestino, compareErro.getObject_erro());
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## PACKAGES BODY ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Sequence
     * 
     * @param objetosOrigem
     *          Lista de Sequence da base de Origem
     * @param objetosDestino
     *          Lista de Sequence da base de Destino
     * @return
     */
    public boolean DiferencaSequence(List<Sequence> objetosOrigem, List<Sequence> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Sequence procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Sequence procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    v_listErros.add("SEQUENCE: " + procedureOrigem.get_sequenceName() + " não foi encontada");
                }
            }
        } catch (Exception e) {
        }
        //
        try {
            for (Sequence procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Sequence procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_sequenceName());
                    compareErro.setObject_name(procedureDestino.get_sequenceName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("SEQUENCE");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## SEQUENCES ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * Fun??o para verificar as diferen?as das Sequence
     * 
     * @param objetosOrigem
     *          Lista de Sequence da base de Origem
     * @param objetosDestino
     *          Lista de Sequence da base de Destino
     * @return
     */
    public boolean DiferencaTrigger(List<Trigger> objetosOrigem, List<Trigger> objetosDestino, StringBuilder v_log) {
        List<String> v_listErros;
        boolean v_boolean_exists = false;
        v_listErros = new ArrayList<String>();
        CompareErro compareErro;
        //
        try {
            v_listErros.add(0, "");
            for (Trigger procedureOrigem : objetosOrigem) {
                v_boolean_exists = false;
                for (Trigger procedureDestino : objetosDestino) {
                    if (procedureOrigem.equals(procedureDestino)) {
                        if (!procedureOrigem.equalsTextLength(procedureDestino)) {
                            compareErro = new CompareErro();
                            v_listErros.add("DIFERENTE: " + procedureDestino.get_triggerName());
                            compareErro.setObject_name(procedureDestino.get_triggerName());
                            compareErro.setObject_erro("DIFERENTE");
                            compareErro.setObject_type("TRIGGER");
                            this.copyObject(procedureDestino, compareErro.getObject_erro());
                            v_list_CompareErro.add(compareErro);
                        }
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("FALTANTE: " + procedureOrigem.get_triggerName());
                    compareErro.setObject_name(procedureOrigem.get_triggerName());
                    compareErro.setObject_erro("FALTANTE");
                    compareErro.setObject_type("TRIGGER");
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            for (Trigger procedureDestino : objetosDestino) {
                v_boolean_exists = false;
                for (Trigger procedureOrigem : objetosOrigem) {
                    if (procedureDestino.equals(procedureOrigem)) {
                        v_boolean_exists = true;
                        break;
                    }
                }
                if (!v_boolean_exists) {
                    compareErro = new CompareErro();
                    v_listErros.add("A MAIS: " + procedureDestino.get_triggerName());
                    compareErro.setObject_name(procedureDestino.get_triggerName());
                    compareErro.setObject_erro("A MAIS");
                    compareErro.setObject_type("TRIGGER");
                    this.copyObject(procedureDestino, compareErro.getObject_erro());
                    v_list_CompareErro.add(compareErro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            if (v_listErros.get(1) != null || !v_listErros.get(1).equals("")) {
                v_listErros.remove(0);
                Collections.sort(v_listErros);
                v_listErros.add(0, "--------------------------------------------------------");
                v_listErros.add(1, "                     ## TRIGGERS ##\r\n");
                v_listErros.add(v_listErros.size(), "--------------------------------------------------------");
            }
        } catch (Exception e) {
            v_listErros.remove(0);
        }
        for (int i = 0; i < v_listErros.size(); i++) {
            v_log.append(v_listErros.get(i) + "\r\n");
        }
        //
        return true;
    }

    /**
     * 
     * @return
     */
    public List<CompareErro> getCompareErro() {
        return v_list_CompareErro;
    }

    /**
     * 
     * @param objeto 
     */
    private void copyObject(Object objeto, String erro) {
        try {
            BufferedWriter bw;
            OutputStreamWriter osw;
            FileOutputStream fos;
            File arquivo = null;
            this.con.ConnectAux();
            String object_name = "";

            if (this.retornaObjetoType(objeto).equalsIgnoreCase("Function")) {
                File file = new File(this.caminhoComparacao + "\\" + erro + "\\Function");
                if (!file.exists()) {
                    file.mkdirs();
                }
                arquivo = new File(file.getPath() + "\\" + ((Function) objeto).get_functionName() + ".sql");
                object_name = ((Function) objeto).get_functionName();
            } else if (this.retornaObjetoType(objeto).equalsIgnoreCase("Procedure")) {
                File file = new File(this.caminhoComparacao + "\\" + erro + "\\Procedure");
                if (!file.exists()) {
                    file.mkdirs();
                }
                arquivo = new File(file.getPath() + "\\" + ((Procedure) objeto).get_procedureName() + ".sql");
                object_name = ((Procedure) objeto).get_procedureName();
            } else if (this.retornaObjetoType(objeto).equalsIgnoreCase("Trigger")) {
                File file = new File(this.caminhoComparacao + "\\" + erro + "\\Trigger");
                if (!file.exists()) {
                    file.mkdirs();
                }
                arquivo = new File(file.getPath() + "\\" + ((Trigger) objeto).get_triggerName() + ".sql");
                object_name = ((Trigger) objeto).get_triggerName();
            } else if (this.retornaObjetoType(objeto).equalsIgnoreCase("Package")) {
                File file = new File(this.caminhoComparacao + "\\" + erro + "\\Package");
                if (!file.exists()) {
                    file.mkdirs();
                }
                arquivo = new File(file.getPath() + "\\" + ((Package) objeto).get_packageName() + ".sql");
                object_name = ((Package) objeto).get_packageName();
            } else if (this.retornaObjetoType(objeto).equalsIgnoreCase("Package Body")) {
                File file = new File(this.caminhoComparacao + "\\" + erro + "\\Package Body");
                if (!file.exists()) {
                    file.mkdirs();
                }
                arquivo = new File(file.getPath() + "\\" + ((Package_Body) objeto).getNAME() + ".sql");
                object_name = ((Package_Body) objeto).getNAME();
            }

            fos = new FileOutputStream(arquivo);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            ResultSet rs = this.con.Query("SELECT TEXT "
                    + "FROM USER_SOURCE "
                    + "WHERE NAME = '" + object_name + "' "
                    + "AND TYPE = upper('" + this.retornaObjetoType(objeto) + "') "
                    + "ORDER BY LINE");
            bw.write("create or replace ");
            while (rs.next()) {
                bw.write(rs.getString("TEXT"));
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param objeto
     * @return 
     */
    private String retornaObjetoType(Object objeto) {
        if (objeto instanceof Function) {
            return "Function";
        } else if (objeto instanceof Procedure) {
            return "Procedure";
        } else if (objeto instanceof Trigger) {
            return "Trigger";
        } else if (objeto instanceof Package) {
            return "Package";
        } else if (objeto instanceof Package_Body) {
            return "Package Body";
        }
        return "";
    }
}
