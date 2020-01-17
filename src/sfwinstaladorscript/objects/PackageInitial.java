/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.oracleinstallation.SfwOracleProductDetail;
import sfwinstaladorscript.oracleinstallation.components.SfwOracleDefineUser;

/**
 * Classe para manipulaÁ„o do pacote inicial executado apÛs a instalaÁ„o dos produtos.
 */
@SuppressWarnings("CallToThreadDumpStack")
public class PackageInitial {

    private ZipFile _zip;
    private static String DATE_FORMAT_DIR_LOG = "yyyyMMdd";
    private static String DATE_FORMAT_HOUR_LOG = "HHmmss";

    /**
     * Atribui arquivo zip com o conte˙do do pacote inicial.
     * @param _zip Arquivo zip do pacote inicial.
     */
    public void set_zip(ZipFile _zip) {
        this._zip = _zip;
    }

    /**
     * Executa pacote de validaÁıes iniciais.
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
                this.runExecWindows(extractionpoint, "\\instala.bat", "initial.log");
            } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                this.runExecLinux(extractionpoint, "/instala.sh", "initial.log");
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

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("cmd /C start \"" + Utils.getDefaultBundle().getString("PackageInstallCmd.initialvalidation") + "\" /D \"" + v_string_dir_path + "\" /WAIT " + v_string_exec);
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                this.copyLog(v_string_dir_path, logfile);
            } else {
                this.copyLog(v_string_dir_path, "initial.log");
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

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("/bin/bash -c start \"" + Utils.getDefaultBundle().getString("PackageCmd.initialvalidation") + "\" /D \"" + v_string_dir_path + "\" /WAIT " + v_string_exec);
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + logfile);
            } else {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + "initial.log");
            }

            if (v_file_log.exists()) {
                v_calendar_cld = Calendar.getInstance();
                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
                v_file_dir_log = new File("log" + System.getProperty("file.separator") + v_simpledateformat_sdf.format(v_calendar_cld.getTime()));

                if (!v_file_dir_log.exists()) {
                    v_file_dir_log.mkdirs();
                }

                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
                v_string_log_name = v_simpledateformat_sdf.format(v_calendar_cld.getTime()) + "_initial.log";
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
}
