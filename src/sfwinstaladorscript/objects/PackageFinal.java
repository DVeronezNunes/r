package sfwinstaladorscript.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.oracleinstallation.SfwOracleProductDetail;
import sfwinstaladorscript.oracleinstallation.components.SfwOracleDefineUser;

/**
 * Classe para manipulaÁ„o do pacote final executado apÛs a instalaÁ„o dos produtos.
 */
@SuppressWarnings("CallToThreadDumpStack")
public class PackageFinal {

    private ZipFile _zip;
    private static String DATE_FORMAT_DIR_LOG = "yyyyMMdd";
    private static String DATE_FORMAT_HOUR_LOG = "HHmmss";

    /**
     * Atribui arquivo zip com o conte˙do do pacote final.
     * @param _zip Arquivo zip do pacote final.
     */
    public void set_zip(ZipFile _zip) {
        this._zip = _zip;
    }

    /**
     * Executa pacote de validaÁıes finais.
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.lang.Exception
     */
    public void execute(File extractionpoint) throws IOException, InterruptedException, Exception {
        File v_file_define;
        File v_file_define_copy;

        // validacoes oracle
        if (Install.get_database().get_name().toUpperCase().startsWith("ORACLE")) {
            // copia define para FINAL temporario
            v_file_define = new File("temp" + System.getProperty("file.separator") + "define.sql");
            v_file_define_copy = new File(extractionpoint, "define.sql");

            if (v_file_define_copy.exists()) {
                v_file_define_copy.delete();
            }

            v_file_define_copy.createNewFile();
            Utils.copyFile(v_file_define, v_file_define_copy);

            this.extractZip(this._zip, extractionpoint);

            if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                this.runExecWindows(extractionpoint, "\\instala.bat", "final.log");
            } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                this.runExecLinux(extractionpoint, "/instala.sh", "final.log");
            }
        }
    }

    /**
     * Extrai informaÁıes do pacote de instalaÁ„o para o produto sendo instalado.
     * @param zipfile
     * @param extractionpoint
     * @throws java.io.IOException
     */
    private void extractZip(ZipFile zipfile, File extractionpoint) throws IOException {
        // Enumerate each entry
        for (Enumeration entries = zipfile.entries(); entries.hasMoreElements();) {

            // Get the entry and its name
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();

            if (zipEntry.isDirectory()) {
                boolean success = (new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntry.getName())).mkdir();
            } else {
                String zipEntryName = zipEntry.getName();

                OutputStream out = new FileOutputStream(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntryName);
                InputStream in = zipfile.getInputStream(zipEntry);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Close streams
                out.close();
                in.close();
            }
        }
    }

    /**
     * Extrai apenas um √∫nico objeto do zip e retorna seu conte√∫do em uma String.
     * @param objectname Nome do objeto a ser extra√≠do.
     * @return Conte√∫do do objeto extra√≠do.
     * @throws IOException
     */
    public String extractZipObject(String objectname) throws IOException {
        String v_string_object = "";

        // Enumerate each entry
        for (Enumeration entries = this._zip.entries(); entries.hasMoreElements();) {

            // Get the entry and its name
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();

            if (objectname.equals(zipEntry.getName())) {
                if (!zipEntry.isDirectory()) {
                    String byteToString;
                    String zipEntryName = zipEntry.getName();
                    InputStream in = this._zip.getInputStream(zipEntry);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        byteToString = new String(buf, 0, len);
                        v_string_object = v_string_object + byteToString;
                        byteToString = null;
                    }

                    // Close streams
                    in.close();

                    return v_string_object;
                }
            }
        }

        return "";
    }

    /**
     * Inicia execuÁ„o windows.
     * @param extractionpoint Ponto de extraÁ„o tempor·ria do pacote.
     * @param productfolder Pasta do produto.
     * @param path Caminho do execut·vel.
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    private void runExecWindows(File extractionpoint, String path, String logfile) throws IOException, InterruptedException, Exception {
        int v_int_return;
        String v_string_exec;
        String v_string_dir_path;
        Process v_process_installexec;
        File v_file_comparer;
        Iterator v_iterator_it;
        SfwOracleProductDetail v_sfworacleproductdetail_current;

        // parse dos caminhos
        v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
        v_string_dir_path = extractionpoint.getAbsolutePath() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);

        //Executa a criaÁ„o dos Logs de CM
        v_iterator_it = Install.get_productsdetail().iterator();
        while (v_iterator_it.hasNext()) {
            v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_it.next();
            try {
                this.createCMLog(v_sfworacleproductdetail_current.getUser(), v_sfworacleproductdetail_current.getPassword(), v_sfworacleproductdetail_current.getProduct().get_label());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.executeGrants(v_sfworacleproductdetail_current);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((!v_sfworacleproductdetail_current.getProduct().get_code().equals("1000")
                    || v_sfworacleproductdetail_current.getProduct().get_code().equals("500"))
                    && !v_sfworacleproductdetail_current.getProduct().get_code().equals("10")
                    && !v_sfworacleproductdetail_current.getProduct().get_code().equals("11")) {
                try {
                    //this.executaCarregaDD(v_sfworacleproductdetail_current);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                this.desabilitaEstatisticas(v_sfworacleproductdetail_current);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("cmd /C start \"" + Utils.getDefaultBundle().getString("PackageInstallCmd.finalvalidation") + "\" /D \"" + v_string_dir_path + "\" /WAIT " + v_string_exec);
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                this.copyLog(v_string_dir_path, logfile);
            } else {
                this.copyLog(v_string_dir_path, "final.log");
            }

            // copia log do brokerreport
            this.copyLog(v_string_dir_path, "report.log");

            // copia logs de comparacao de base se existirem
            v_iterator_it = Install.get_productsdetailinstall().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_it.next();

                v_file_comparer = new File(extractionpoint, v_sfworacleproductdetail_current.getProduct().get_name().toLowerCase().replace("_", "") + "_comparacao.log");
                if (v_file_comparer.exists()) {
                    this.copyLog(v_string_dir_path, v_sfworacleproductdetail_current.getProduct().get_name().toLowerCase().replace("_", "") + "_comparacao.log");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inicia execut√°vel linux.
     * @param extractionpoint Ponto de extraÁ„o tempor·ria do pacote.
     * @param productfolder Pasta do produto.
     * @param path Caminho do execut√°vel.
     */
    private void runExecLinux(File extractionpoint, String path, String logfile) throws IOException, InterruptedException, Exception {
        int v_int_return;
        File v_file_log;
        File v_file_dir_log;
        File v_file_log_copy;
        String v_string_exec;
        String v_string_dir_path;
        String v_string_log_name;
        Process v_process_installexec;
        Calendar v_calendar_cld;
        SimpleDateFormat v_simpledateformat_sdf;
        SfwOracleProductDetail v_sfworacleproductdetail_current;
        Iterator v_iterator_it;


        // parse dos caminhos
        v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
        v_string_dir_path = extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);

        //Executa a cria√ß√£o dos Logs de CM
        v_iterator_it = Install.get_productsdetail().iterator();
        while (v_iterator_it.hasNext()) {
            v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_it.next();
            this.createCMLog(v_sfworacleproductdetail_current.getUser(), v_sfworacleproductdetail_current.getPassword(), v_sfworacleproductdetail_current.getProduct().get_label());
            this.executeGrants(v_sfworacleproductdetail_current);
        }

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("/bin/bash -c start \"" + Utils.getDefaultBundle().getString("PackageCmd.finalvalidation") + "\" /D \"" + v_string_dir_path + "\" /WAIT " + v_string_exec);
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + logfile);
            } else {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + "final.log");
            }

            if (v_file_log.exists()) {
                v_calendar_cld = Calendar.getInstance();
                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
                v_file_dir_log = new File("log" + System.getProperty("file.separator") + v_simpledateformat_sdf.format(v_calendar_cld.getTime()));

                if (!v_file_dir_log.exists()) {
                    v_file_dir_log.mkdirs();
                }

                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
                v_string_log_name = v_simpledateformat_sdf.format(v_calendar_cld.getTime()) + "_final.log";
                v_file_log_copy = new File(v_file_dir_log, v_string_log_name);

                if (!v_file_log_copy.exists()) {
                    v_file_log_copy.createNewFile();
                }

                Utils.copyFile(v_file_log, v_file_log_copy);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Procedimento para copiar um arquivo de log da execu√ß√£o.
     * @param dirpath Diret√≥rio onde o log est√° localizado.
     * @param logfile Nome do arquivo de log.
     * @throws java.io.IOException
     */
    private void copyLog(String dirpath, String logfile) throws IOException {
        File v_file_log;
        File v_file_dir_log;
        File v_file_log_copy;
        String v_string_log_name;
        Calendar v_calendar_cld;
        SimpleDateFormat v_simpledateformat_sdf;

        // copia log
        if (logfile != null && !logfile.equals("")) {
            v_file_log = new File(dirpath + System.getProperty("file.separator") + logfile);

            if (v_file_log.exists()) {
                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
                v_calendar_cld = Calendar.getInstance();
                v_file_dir_log = new File("log" + System.getProperty("file.separator") + v_simpledateformat_sdf.format(v_calendar_cld.getTime()));

                if (!v_file_dir_log.exists()) {
                    v_file_dir_log.mkdirs();
                }

                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
                v_string_log_name = v_simpledateformat_sdf.format(v_calendar_cld.getTime()) + "_" + logfile;
                v_file_log_copy = new File(v_file_dir_log, v_string_log_name);

                if (!v_file_log_copy.exists()) {
                    v_file_log_copy.createNewFile();
                }

                Utils.copyFile(v_file_log, v_file_log_copy);
            }
        }
    }

    /**
     * Cria o arquivo contendo o LOG da tabela CM_LOG_SCRIPTS.
     * Cria um novo diretÛrio chamado cm_log_scripts.
     * Cria um log para cada produto.
     * @throws IOException
     */
    private void createCMLog(String user, String pass, String label) {
        ArrayList<CMVersion> v_list_cmversion = new ArrayList<CMVersion>();
        File v_file_cm_log;
        Calendar v_calendar_cld;
        SimpleDateFormat v_simpledateformat_sdf;
        String v_string_date;
        String v_string_hora;
        String sysdate;
        BufferedWriter v_bw_log;
        v_calendar_cld = Calendar.getInstance();
        v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
        v_string_date = v_simpledateformat_sdf.format(v_calendar_cld.getTime());
        v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
        v_string_hora = v_simpledateformat_sdf.format(v_calendar_cld.getTime());
        v_simpledateformat_sdf = new SimpleDateFormat("dd/MM/yyyy");
        sysdate = v_simpledateformat_sdf.format(v_calendar_cld.getTime());
        try {
            File v_file_cm_log_scripts = new File("log" + System.getProperty("file.separator") + v_string_date);
            if (!v_file_cm_log_scripts.exists()) {
                v_file_cm_log_scripts.mkdirs();
            }
            CMProduct v_currentproductcm = new CMProduct();
            v_file_cm_log = new File(v_file_cm_log_scripts + System.getProperty("file.separator") + v_string_hora + "_" + label + ".log");
            if (!v_file_cm_log.exists()) {
                v_file_cm_log.createNewFile();
            }
            try {
                OracleConnection conn = new OracleConnection();
                conn.set_username(user);
                conn.set_password(pass);
                conn.set_tns(Install.get_tns());
                conn.Connect();
                ResultSet rs = conn.Query("SELECT A.VERSAO, "
                        + "A.RM, "
                        + "A.TIPO, "
                        + "A.OBJETO, "
                        + "A.ERRO, "
                        + "TO_DATE(A.DATA, 'DD/MM/RRRR') AS DATA "
                        + "FROM CM_LOG_SCRIPTS A "
                        + "WHERE A.DATA >= TO_DATE(SYSDATE-1, 'DD/MM/RRRR') "
                        + "ORDER BY A.VERSAO");
                while (rs.next()) {
                    CMVersion v_cmversion = new CMVersion();
                    v_cmversion.setCodVersao(rs.getString("VERSAO"));
                    v_cmversion.setRm(rs.getString("RM"));
                    v_cmversion.setTipo(rs.getString("TIPO"));
                    v_cmversion.setObjeto(rs.getString("OBJETO"));
                    v_cmversion.setErro(rs.getString("ERRO"));
                    v_cmversion.setData(String.valueOf(rs.getDate("DATA")));
                    v_list_cmversion.add(v_cmversion);
                }
                v_currentproductcm.setVersao(v_list_cmversion);
            } catch (Exception e) {
                e.printStackTrace();
            }
            v_bw_log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(v_file_cm_log)));
            v_bw_log.write("----------------------------------------");
            v_bw_log.newLine();
            v_bw_log.write("-- Produto:          " + label);
            v_bw_log.newLine();
            v_bw_log.write("-- Resumo:          CM_LOG_SCRIPTS");
            v_bw_log.newLine();
            v_bw_log.write("----------------------------------------");
            v_bw_log.newLine();
            v_bw_log.newLine();
            for (int i = 0; i < v_currentproductcm.getVersao().size(); i++) {
                v_bw_log.write("-- VERS√O:        " + v_currentproductcm.getVersao().get(i).getCodVersao());
                v_bw_log.newLine();
                v_bw_log.write("-- RM:            " + v_currentproductcm.getVersao().get(i).getRm());
                v_bw_log.newLine();
                v_bw_log.write("-- TIPO:          " + v_currentproductcm.getVersao().get(i).getTipo());
                v_bw_log.newLine();
                v_bw_log.write("-- OBJETO:        " + v_currentproductcm.getVersao().get(i).getObjeto());
                v_bw_log.newLine();
                v_bw_log.write("-- ERRO:          " + v_currentproductcm.getVersao().get(i).getErro());
                v_bw_log.newLine();
                v_bw_log.write("-- DATA:          " + v_currentproductcm.getVersao().get(i).getData());
                v_bw_log.newLine();
                v_bw_log.newLine();
            }
            v_bw_log.write("----------------------------------------");
            v_bw_log.newLine();
            v_bw_log.close();
        } catch (Exception e) {
        }
    }

    /**
     * 
     * @param pd
     */
    public void executeGrants(SfwOracleProductDetail v_this_productdetails) {
        Iterator v_iterator_it;
        OracleConnection v_oracleconnection_conn = null;
        SfwOracleDefineUser v_sfworacledefineuser_current;
        SfwOracleProductDetail v_sfworacleproductdetail_current;

        try {
            v_oracleconnection_conn = new OracleConnection();
            v_oracleconnection_conn.set_tns(Install.get_tns());
            v_oracleconnection_conn.set_username(v_this_productdetails.getUser());
            v_oracleconnection_conn.set_password(v_this_productdetails.getPassword());
            v_oracleconnection_conn.ConnectAux();

            v_iterator_it = Install.get_productsdetail().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacleproductdetail_current = (SfwOracleProductDetail) v_iterator_it.next();

                if (!v_sfworacleproductdetail_current.getProduct().get_name().equals(v_this_productdetails.getName())) {
                    /* Se for tracking n„o pode dar os grants */
                    if (!v_sfworacleproductdetail_current.getProduct().get_name().equals("TRACKING_SYS")) {
                        this.sfw_processa_grants_final(v_this_productdetails.getUser(), v_sfworacleproductdetail_current.getUser(), "U", v_oracleconnection_conn);
                    }
                }
            }

            v_iterator_it = Install.get_productdefine().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacledefineuser_current = (SfwOracleDefineUser) v_iterator_it.next();

                if (Product.productExists(v_sfworacledefineuser_current.getUser(), v_sfworacledefineuser_current.getPassword(), v_sfworacledefineuser_current.getProduct().get_code())) {

                    // Processa Grants
                    /* Se for tracking n„o pode dar os grants */
                    if (!v_sfworacledefineuser_current.getProduct().get_name().equals("TRACKING_SYS")) {
                        this.sfw_processa_grants_final(v_this_productdetails.getUser(), v_sfworacledefineuser_current.getUser(), "U", v_oracleconnection_conn);
                        try {
                            //this.executaCarregaDD(v_this_productdetails);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            // Processa Grants Para Role
            if (Install.is_userole()) {
                this.sfw_processa_grants_final(v_this_productdetails.getUser(), Utils.nvl(Install.get_rolesfw(), "ROLE_SOFTWAY"), "R", v_oracleconnection_conn);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            v_oracleconnection_conn.Close();
        }
    }

    /**
     *
     * @param pd
     * @param p_grantee
     * @param p_tipo
     */
    private void sfw_processa_grants_final(String user, String p_grantee, String p_tipo, OracleConnection con) {
        String v_string_grant = "";
        try {
            ResultSet rs = con.Query("SELECT UO.OBJECT_TYPE, UO.OBJECT_NAME "
                    + "FROM USER_OBJECTS UO "
                    + "WHERE UO.OBJECT_TYPE IN ('PROCEDURE', "
                    + "'FUNCTION', "
                    + "'PACKAGE', "
                    + "'TABLE', "
                    + "'VIEW', "
                    + "'SEQUENCE') "
                    + "AND UO.OBJECT_NAME NOT LIKE '%BIN$%' "
                    + "AND UO.OBJECT_NAME <> 'SFW_PROCESSA_GRANTS' "
                    + "AND UO.OBJECT_NAME <> 'SFW_PROCESSA_GRANTS_CM' "
                    + "AND UO.OBJECT_NAME <> 'SFW_PROCESSA_GRANTS_FINAL' "
                    + "AND NOT EXISTS "
                    + "(SELECT DISTINCT TABLE_NAME "
                    + "FROM USER_TAB_PRIVS UTP "
                    + "WHERE UTP.TABLE_NAME = UO.OBJECT_NAME "
                    + "AND OWNER = '" + user + "' "
                    + "AND GRANTEE = '" + p_grantee + "')");
            while (rs.next()) {
                String v_object_type = rs.getString("OBJECT_TYPE");
                String v_object_name = rs.getString("OBJECT_NAME");
                if (v_object_type.equals("TABLE")) {
                    if (!p_tipo.equalsIgnoreCase("R")) {
                        v_string_grant = "grant select, insert, update, delete, references on "
                                + v_object_name + " to " + p_grantee + " with grant option";
                    } else {
                        v_string_grant = "grant select, insert, update, delete on " + v_object_name + " to " + p_grantee;
                    }
                } else if (v_object_type.equals("PROCEDURE") || v_object_type.equals("FUNCTION") || v_object_type.equals("PACKAGE")) {
                    if (!p_tipo.equalsIgnoreCase("R")) {
                        v_string_grant = "grant execute on " + v_object_name + " to " + p_grantee + " with grant option";
                    } else {
                        v_string_grant = "grant execute on " + v_object_name + " to " + p_grantee;
                    }
                } else if (v_object_type.equals("SEQUENCE")) {
                    if (!p_tipo.equalsIgnoreCase("R")) {
                        v_string_grant = "grant select on " + v_object_name + " to " + p_grantee + " with grant option";
                    } else {
                        v_string_grant = "grant select on " + v_object_name + " to " + p_grantee;
                    }
                } else if (v_object_type.equals("VIEW")) {
                    String option = this.viewOption(v_object_name, con);
                    if (!p_tipo.equalsIgnoreCase("R")) {
                        v_string_grant = "grant " + option + " on " + v_object_name + " to " + p_grantee + " with grant option";
                    } else {
                        v_string_grant = "grant " + option + " on " + v_object_name + " to " + p_grantee;
                    }
                }
                if (!v_string_grant.equals("")) {
                    try {
                        con.Update(v_string_grant);
                    } catch (SQLException ex) {
                        con.CloseStatement();
                        if (ex.getErrorCode() == 604) {
                            con.Update(v_string_grant);
                        } else if (ex.getErrorCode() == 1720) {
                            if (v_object_type.equals("VIEW")) {
                                if (!p_tipo.equalsIgnoreCase("R")) {
                                    v_string_grant = "grant select on " + v_object_name + " to " + p_grantee + " with grant option";
                                } else {
                                    v_string_grant = "grant select on " + v_object_name + " to " + p_grantee;
                                }
                                con.Update(v_string_grant);
                            }
                        }
                    } catch (Exception e) {
                    } finally {
                        con.CloseStatement();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.CloseStatement();
        }
    }

    /**
     * 
     * @param object_name
     * @param con
     * @return
     */
    private String viewOption(String object_name, OracleConnection con) {
        String option = null;
        ResultSet rs = null;
        String query = "select 'select'||(select ',update' "
                + "from user_updatable_columns u "
                + "where u.table_name='" + object_name + "' and UPDATABLE='YES' and rownum<=1) "
                + "||(select ',delete' "
                + "from user_updatable_columns u "
                + "where u.table_name='" + object_name + "' and DELETABLE='YES' and rownum<=1) "
                + "||(select ',insert' "
                + "from user_updatable_columns u "
                + "where u.table_name='" + object_name + "' and INSERTABLE='YES' and rownum<=1) \"OPTION\" "
                + "from dual";
        try {
            rs = con.Query(query);
            if (rs.next()) {
                option = rs.getString("OPTION");
            }
        } catch (Exception e) {
        } finally {
            con.CloseStatement();
        }
        option = Utils.nvl(option, "select, insert, update");
        return option;
    }

    /**
     * Desabilita as estatÌsticas no banco de dados
     * @return
     */
    private boolean desabilitaEstatisticas(SfwOracleProductDetail v_this_productdetails) throws SQLException {

        OracleConnection con = new OracleConnection();
        try {
            con.set_username(v_this_productdetails.getUser());
            con.set_password(v_this_productdetails.getPassword());
            con.set_tns(Install.get_tns());
            con.ConnectAux();
            con.ExecStoredProcedure("{ call dbms_stats.unlock_schema_stats('" + v_this_productdetails.getUser() + "') }", null);
            con.ExecStoredProcedure("{ call dbms_stats.delete_schema_stats('" + v_this_productdetails.getUser() + "') }", null);
            con.ExecStoredProcedure("{ call dbms_stats.lock_schema_stats('" + v_this_productdetails.getUser() + "') }", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return true;
    }

    /**
     * 
     * @return
     */
    private boolean executaCarregaDD(SfwOracleProductDetail v_this_productdetails) throws SQLException {

        OracleConnection con = new OracleConnection();
        try {
            con.set_username(v_this_productdetails.getUser());
            con.set_password(v_this_productdetails.getPassword());
            con.set_tns(Install.get_tns());
            con.ConnectAux();
            con.ExecStoredProcedure("{ call prc_sfw_carrega_dd }", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return true;
    }
}
