/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwPackageDetail.java
 *
 * Created on 19/11/2008, 15:07:58
 */
package sfwinstaladorscript.oracleinstallation.components;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTextField;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.objects.Package;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwListItem;
import sfwinstaladorscript.objects.PackageFinal;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.Version;
import sfwinstaladorscript.oracleinstallation.SfwOracleProductDetail;
import sfwinstaladorscript.oracleinstallation.components_interfaces_define.SfwWizardDefineInterfacesBDLINK;
import sfwinstaladorscript.oracleinstallation.components_interfaces_define.SfwWizardDefineInterfacesR12;

/**
 * Componente que representa um pacote de instalação.
 */
@SuppressWarnings("CallToThreadDumpStack")
public class SfwOraclePackageInstall extends javax.swing.JPanel implements SfwListItem {

    private Product _product;
    private productinfo.Diff _diff;
    private productinfo.Full _full;
    private SfwOracleProductDetail _productdetail;
    SfwWizardDefineInterfacesR12 interfaces_r12;
    private static String DATE_FORMAT_DIR_LOG = "yyyyMMdd";
    private static String DATE_FORMAT_HOUR_LOG = "HHmmss";
    private List<String> roots = new ArrayList<String>();
    String unit = "";

    /** Creates new form SfwPackageDetail */
    public SfwOraclePackageInstall() {
        initComponents();
        this._diff = null;
        this._full = null;
    }

    public SfwOraclePackageInstall(productinfo.Diff diff, SfwOracleProductDetail pd) {
        initComponents();
        this._diff = diff;
        this._full = null;
        this._productdetail = pd;
        this.jLabelType.setText("DIFF");
        this.jTextFieldName.setText(diff.getName());
    }

    public SfwOraclePackageInstall(productinfo.Full full, SfwOracleProductDetail pd) {
        initComponents();
        this._diff = null;
        this._full = full;
        this._productdetail = pd;
        this.jLabelType.setText("FULL");
        this.jTextFieldName.setText(full.getName());
    }

    /**
     * Retorna campo de status do componente.
     * @return Campo de status.
     */
    public JTextField getJTextFieldStatus() {
        return jTextFieldStatus;
    }

    /**
     * Retorna nome do pacote.
     * @return String com o nome do pacote.
     */
    public String getPackageName() {
        if (this._diff != null) {
            return this._diff.getName();
        }

        if (this._full != null) {
            return this._full.getName();
        }

        return "";
    }

    /**
     * Instala pacote.
     */
    public void install() throws IOException, InterruptedException, SQLException, Exception {
        if (this._diff != null) {
            this.installDiff();
        }

        if (this._full != null) {
            this.installFull();
        }
    }

    /**
     * Instala pacote full.
     */
    private void installFull() throws IOException, InterruptedException, SQLException, Exception {
        String v_string_prc_compila_invalido;
        Package v_package_pkgfull;
        PackageFinal v_packagefinal_pkt;
        File v_file_define;
        File v_file_extraction_point;
        productinfo.Oracle v_oracle_db;
        productinfo.Executable v_executable_exec;
        OracleConnection v_oracleconnection_conn;

        // busca pacote final
        v_packagefinal_pkt = Install.get_packagelist().get_final_package();

        // extrai compila invalidos
        //v_string_prc_compila_invalido = v_packagefinal_pkt.extractZipObject("prc_compila_invalido.sql");

        // inicia uma conexao com banco so para pegar a versao
        // e rodar compila invalidos depois de cada executavel
        v_oracleconnection_conn = new OracleConnection();
        v_oracleconnection_conn.set_tns(Install.get_tns());
        v_oracleconnection_conn.set_username(this._productdetail.getUser());
        v_oracleconnection_conn.set_password(this._productdetail.getPassword());
        v_oracleconnection_conn.Connect();
        //v_oracleconnection_conn.Object(v_string_prc_compila_invalido);

        // pega o pacote e inicializa ponto de extracao
        v_package_pkgfull = Install.get_packagelist().getPackageByName(this._full.getPkg());
        v_file_extraction_point = new File("temp" + System.getProperty("file.separator") + this._full.getPkg());
        if (!v_file_extraction_point.exists()) {
            v_file_extraction_point.mkdirs();
        }

        // copia define para ponto de extracao
        v_file_define = new File("temp" + System.getProperty("file.separator") + "define.sql");

        // extrai arquivos necessarios do pacote
        Version.extractZip(v_package_pkgfull.get_zip(), v_file_extraction_point, this._productdetail.getProduct().get_folder());

        //copia comparador para temporaria de comparador
        //this.copyComparer(v_file_extraction_point);        

        // loop para pegar executavel compativel com a versao do oracle
        for (int i = 0; i < this._full.getOracleArray().length; i++) {

            v_oracle_db = this._full.getOracleArray()[i];

            if (v_oracle_db.getTag().toLowerCase().equals(v_oracleconnection_conn.get_tag().toLowerCase())) {
                // se for windows
                if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                    for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());

                        // compila invalidos
                        //v_oracleconnection_conn.ExecStoredProcedure("{call prc_compila_invalido}", null);
                    }
                } // se for linux
                else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                    for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());

                        // compila invalidos
                        //v_oracleconnection_conn.ExecStoredProcedure("{call prc_compila_invalido}", null);
                    }
                }
            }
        }

        // loop para pegar executaveis de qualquer versao do oracle
        for (int i = 0; i < this._full.getOracleArray().length; i++) {

            v_oracle_db = this._full.getOracleArray()[i];

            if (v_oracle_db.getTag().toLowerCase().equals("any")) {
                // se for windows
                if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                    for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define e executa
                        if (this._productdetail.get_description().toString().toUpperCase().equals("INTERFACES EBS 12")) {
                            if (SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && SfwWizardDefineInterfacesR12.jCheckEBS.isSelected()) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && !SfwWizardDefineInterfacesR12.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("integracao-user") || v_executable_exec.getPath().contains("soaconsole-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (!SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && SfwWizardDefineInterfacesR12.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("apps-user") || v_executable_exec.getPath().contains("xxisv-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            }
                        } else if (this._productdetail.get_description().toString().toUpperCase().equals("INTERFACES EBS 12 DBLINK")) {
                            if (SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected()) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && !SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("integracao-user") || v_executable_exec.getPath().contains("inout-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (!SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("apps-user") || v_executable_exec.getPath().contains("xxisv-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            }
                        } else {
                            this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                            this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                        }

                        // compila invalidos
                        //v_oracleconnection_conn.ExecStoredProcedure("{call prc_compila_invalido}", null);
                    }
                } // se for linux
                else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                    for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());

                        // compila invalidos
                        //v_oracleconnection_conn.ExecStoredProcedure("{call prc_compila_invalido}", null);
                    }
                }
            }
        }

        v_oracleconnection_conn.Close();

        //limpa ponto de extracao da temp
        Utils.deleteDirectory(v_file_extraction_point);
    }

    /**
     * Instala pacote diff.
     */
    private void installDiff() throws IOException, InterruptedException, SQLException, Exception {
        String v_string_prc_compila_invalido;
        Package v_package_pkgdiff;
        PackageFinal v_packagefinal_pkt;
        File v_file_define;
        File v_file_extraction_point;
        productinfo.Oracle v_oracle_db;
        productinfo.Executable v_executable_exec;
        OracleConnection v_oracleconnection_conn;

        // busca pacote final
        v_packagefinal_pkt = Install.get_packagelist().get_final_package();

        // extrai compila invalidos
        //v_string_prc_compila_invalido = v_packagefinal_pkt.extractZipObject("prc_compila_invalido.sql");

        // inicia uma conexao com banco so para pegar a versao
        v_oracleconnection_conn = new OracleConnection();
        v_oracleconnection_conn.set_tns(Install.get_tns());
        v_oracleconnection_conn.set_username(this._productdetail.getUser());
        v_oracleconnection_conn.set_password(this._productdetail.getPassword());
        v_oracleconnection_conn.Connect();
        //v_oracleconnection_conn.Object(v_string_prc_compila_invalido);

        // pega o pacote e inicializa ponto de extracao
        v_package_pkgdiff = Install.get_packagelist().getPackageByName(this._diff.getPkg());
        v_file_extraction_point = new File("temp" + System.getProperty("file.separator") + this._diff.getPkg());
        if (!v_file_extraction_point.exists()) {
            v_file_extraction_point.mkdirs();
        }

        // copia define para ponto de extracao
        v_file_define = new File("temp" + System.getProperty("file.separator") + "define.sql");

        // extrai arquivos necessarios do pacote
        Version.extractZip(v_package_pkgdiff.get_zip(), v_file_extraction_point, this._productdetail.getProduct().get_folder());

        //copia comparador para temporaria de comparador
        //this.copyComparer(v_file_extraction_point);

        // loop para pegar executavel compativel com a versao do oracle
        for (int i = 0; i < this._diff.getOracleArray().length; i++) {

            v_oracle_db = this._diff.getOracleArray()[i];

            if (v_oracle_db.getTag().toLowerCase().equals(v_oracleconnection_conn.get_tag().toLowerCase())) {
                if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                    for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                    }
                } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                    for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                    }
                }
            }
        }

        // loop para pegar executaveis de qualquer versao do oracle
        for (int i = 0; i < this._diff.getOracleArray().length; i++) {

            v_oracle_db = this._diff.getOracleArray()[i];

            if (v_oracle_db.getTag().toLowerCase().equals("any")) {
                if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                    for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }
                        
                        // copia define e executa
                        if (this._productdetail.get_description().toString().toUpperCase().equals("INTERFACES EBS 12")) {
                            if (SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && SfwWizardDefineInterfacesR12.jCheckEBS.isSelected()) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && !SfwWizardDefineInterfacesR12.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("integracao-user") || v_executable_exec.getPath().contains("soaconsole-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (!SfwWizardDefineInterfacesR12.jCheckIT.isSelected() && SfwWizardDefineInterfacesR12.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("apps-user") || v_executable_exec.getPath().contains("xxisv-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            }
                        } else if (this._productdetail.get_description().toString().toUpperCase().equals("INTERFACES EBS 12 DBLINK")) {
                            if (SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected()) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && !SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("integracao-user") || v_executable_exec.getPath().contains("inout-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            } else if (!SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected() && SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected() && (v_executable_exec.getPath().contains("apps-user") || v_executable_exec.getPath().contains("xxisv-user"))) {
                                this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                                this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                            }
                        } else {
                            this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());
                            this.runExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                        }
                    }
                    
                } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                    for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                        v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                        // verifica se executavel precisa de um produto em especifico
                        if (!v_executable_exec.getProductcode().equals("all") && !v_executable_exec.getProductcode().equals("XX")) {
                            if (!Product.productExists(this._productdetail.getUser(), this._productdetail.getPassword(), v_executable_exec.getProductcode())) {
                                continue;
                            }
                        }

                        // copia define
                        this.copyDefineToExec(v_file_define, v_file_extraction_point, v_executable_exec.getPath());

                        // executa
                        this.runExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                    }
                }
            }
        }

        // compila invalidos
        //v_oracleconnection_conn.ExecStoredProcedure("{call prc_compila_invalido}", null);
        v_oracleconnection_conn.Close();

        //limpa ponto de extracao da temp
        Utils.deleteDirectory(v_file_extraction_point);
    }

    /**
     * Copia define para local do executável.
     * @param define
     * @param extractionpoint
     * @param path
     * @throws java.io.IOException
     */
    private void copyDefineToExec(File define, File extractionpoint, String path) throws IOException {
        File v_file_define_copy;
        File v_file_dirpath = new File(extractionpoint, this._productdetail.getProduct().get_folder() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1));

        if (v_file_dirpath.exists()) {
            v_file_define_copy = new File(v_file_dirpath, "define.sql");

            if (v_file_define_copy.exists()) {
                v_file_define_copy.delete();
            }

            v_file_define_copy.createNewFile();
            Utils.copyFile(define, v_file_define_copy);
        }
    }

    /**
     * Copia comparador de base para temporaria.
     * @param p_file_extraction_point
     * @throws java.io.IOException
     */
    private void copyComparer(File p_file_extraction_point) throws IOException {
        File v_file_comparer;
        File v_file_comparer_tmp;
        File v_file_comparer_version = null;

        v_file_comparer = new File("temp" + System.getProperty("file.separator") + "basecomparer" + System.getProperty("file.separator") + this._productdetail.getProduct().get_shortname().toLowerCase() + "_comparer.sql");

        if (v_file_comparer.exists()) {
            v_file_comparer.delete();
        }

        if (this._full != null) {
            if (this._full.getBasecomparer() != null) {
                if (!this._full.getBasecomparer().equals("")) {
                    v_file_comparer_tmp = new File("temp" + System.getProperty("file.separator") + "basecomparer");

                    if (!v_file_comparer_tmp.exists()) {
                        v_file_comparer_tmp.mkdirs();
                    }

                    v_file_comparer_version = new File(p_file_extraction_point, this._productdetail.getProduct().get_folder() + System.getProperty("file.separator") + this._full.getBasecomparer());

                    if (v_file_comparer_version != null) {
                        if (v_file_comparer_version.exists()) {
                            v_file_comparer.createNewFile();
                            Utils.copyFile(v_file_comparer_version, v_file_comparer);
                        }
                    }
                }
            }
        }

        if (this._diff != null) {
            if (this._diff.getBasecomparer() != null) {
                if (!this._diff.getBasecomparer().equals("")) {
                    v_file_comparer_tmp = new File("temp" + System.getProperty("file.separator") + "basecomparer");

                    if (!v_file_comparer_tmp.exists()) {
                        v_file_comparer_tmp.mkdirs();
                    }

                    v_file_comparer_version = new File(p_file_extraction_point, this._productdetail.getProduct().get_folder() + System.getProperty("file.separator") + this._diff.getBasecomparer());

                    if (v_file_comparer_version != null) {
                        if (v_file_comparer_version.exists()) {
                            v_file_comparer.createNewFile();
                            Utils.copyFile(v_file_comparer_version, v_file_comparer);
                        }
                    }
                }
            }
        }
    }

    /**
     * Inicia executavel windows.
     * @param extractionpoint Ponto de extração temporária do pacote.
     * @param productfolder Pasta do produto.
     * @param path Caminho do executável.
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    private void runExecWindows(File extractionpoint, String path, String logfile) throws IOException, InterruptedException, Exception {
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

        /**
         * Mapeia o ponto de extração para não ocorrer o estouro de limite de caracter
         * para um script com nome muito grande.
         */
        if (this.FindFreeUnit()) {
            extractionpoint = this.UnitMap(extractionpoint.toString());
            // parse dos caminhos
            v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
            v_string_dir_path = extractionpoint.getAbsolutePath() + this._productdetail.getProduct().get_folder() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);
        } else {
            // parse dos caminhos
            v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
            v_string_dir_path = extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + this._productdetail.getProduct().get_folder() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);
        }

        // cria bat de settings iniciais
        Utils.createWindowsStartInstallBat(new File(v_string_dir_path), v_string_exec);

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("cmd /C start \"" + Utils.getDefaultBundle().getString("PackageInstallCmd.installation") + " " + this._productdetail.getProduct().get_label() + " " + this.getPackageName() + "\" /D \"" + v_string_dir_path + "\" /WAIT startinstall.bat");
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + logfile);
            } else {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + "script_" + this._productdetail.getProduct().get_name().toLowerCase().replaceAll("_", "") + "_criacao.log");
            }

            if (v_file_log.exists()) {
                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
                v_calendar_cld = Calendar.getInstance();
                v_file_dir_log = new File("log" + System.getProperty("file.separator") + v_simpledateformat_sdf.format(v_calendar_cld.getTime()));

                if (!v_file_dir_log.exists()) {
                    v_file_dir_log.mkdirs();
                }

                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
                v_string_log_name = v_simpledateformat_sdf.format(v_calendar_cld.getTime()) + "_" + this._productdetail.getProduct().get_name().toLowerCase().replaceAll("_", "") + "_" + this.getPackageName().replaceAll("\\.", "_").replaceAll(" > ", "_para_") + ".log";
                v_file_log_copy = new File(v_file_dir_log, v_string_log_name);

                if (!v_file_log_copy.exists()) {
                    v_file_log_copy.createNewFile();
                }

                Utils.copyFile(v_file_log, v_file_log_copy);
            }
            if (!this.DeleteUnit(extractionpoint.toString())) {
                this.DeleteUnit(unit);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inicia executavel linux.
     * @param extractionpoint Ponto de extração temporária do pacote.
     * @param productfolder Pasta do produto.
     * @param path Caminho do executável.
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

        // parse dos caminhos
        v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
        v_string_dir_path = extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + this._productdetail.getProduct().get_folder() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);

        // chama executavel
        v_process_installexec = java.lang.Runtime.getRuntime().exec("/bin/bash -c start \"" + Utils.getDefaultBundle().getString("PackageCmd.installation") + " " + this._productdetail.getProduct().get_label() + " " + this.getPackageName() + "\" /D \"" + v_string_dir_path + "\" /WAIT " + v_string_exec);
        v_int_return = v_process_installexec.waitFor();

        if (v_int_return != 0) {
            throw new Exception(Utils.getDefaultBundle().getString("PackageInstall.exceptioninstallation"));
        }

        try {
            // copia log
            if (logfile != null && !logfile.equals("")) {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + logfile);
            } else {
                v_file_log = new File(v_string_dir_path + System.getProperty("file.separator") + "sfw_" + this._productdetail.getProduct().get_name().toLowerCase().replaceAll("_", "") + "_criacao.log");
            }

            if (v_file_log.exists()) {
                v_calendar_cld = Calendar.getInstance();
                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_DIR_LOG);
                v_file_dir_log = new File("log" + System.getProperty("file.separator") + v_simpledateformat_sdf.format(v_calendar_cld.getTime()));

                if (!v_file_dir_log.exists()) {
                    v_file_dir_log.mkdirs();
                }

                v_simpledateformat_sdf = new SimpleDateFormat(DATE_FORMAT_HOUR_LOG);
                v_string_log_name = v_simpledateformat_sdf.format(v_calendar_cld.getTime()) + "_" + this._productdetail.getProduct().get_name().toLowerCase().replaceAll("_", "") + "_" + this.getPackageName().replaceAll("\\.", "_").replaceAll(" > ", "para") + ".log";
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldName = new javax.swing.JTextField();
        jLabelType = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldStatus = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        jTextFieldName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldName.setEnabled(false);
        jTextFieldName.setName("jTextFieldName"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwOraclePackageInstall.class);
        jLabelType.setFont(resourceMap.getFont("jLabelType.font")); // NOI18N
        jLabelType.setForeground(resourceMap.getColor("jLabelType.foreground")); // NOI18N
        jLabelType.setText(resourceMap.getString("jLabelType.text")); // NOI18N
        jLabelType.setName("jLabelType"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldStatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldStatus.setText(resourceMap.getString("jTextFieldStatus.text")); // NOI18N
        jTextFieldStatus.setEnabled(false);
        jTextFieldStatus.setName("jTextFieldStatus"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelType)
                .add(7, 7, 7)
                .add(jTextFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldStatus)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabelType)
                .add(jLabel2)
                .add(jTextFieldStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldStatus;
    // End of variables declaration//GEN-END:variables

    /**
     * Descri��o para o SfwList.
     * @return Objeto para usar como Descri��o.
     */
    public Object get_description() {
        return this;
    }

    /**
     * 
     */
    public void addRootsAvailable() {
        try {
            roots.add("A:\\");
            roots.add("B:\\");
            roots.add("C:\\");
            roots.add("D:\\");
            roots.add("E:\\");
            roots.add("F:\\");
            roots.add("G:\\");
            roots.add("H:\\");
            roots.add("I:\\");
            roots.add("J:\\");
            roots.add("K:\\");
            roots.add("L:\\");
            roots.add("M:\\");
            roots.add("N:\\");
            roots.add("O:\\");
            roots.add("P:\\");
            roots.add("Q:\\");
            roots.add("R:\\");
            roots.add("S:\\");
            roots.add("T:\\");
            roots.add("U:\\");
            roots.add("V:\\");
            roots.add("W:\\");
            roots.add("X:\\");
            roots.add("Y:\\");
            roots.add("Z:\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Boolean FindFreeUnit() {
        Set<String> teste = new HashSet<String>();
        File raizes[] = File.listRoots();
        try {
            this.addRootsAvailable();
            for (int j = 0; j < raizes.length; j++) {
                teste.add(raizes[j].toString());
            }
            for (int i = 0; i < roots.size(); i++) {
                if (!teste.contains(roots.get(i))) {
                    unit = roots.get(i);
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @param extractionpoint
     * @return
     */
    public File UnitMap(String extractionpoint) {
        String unitMap = "";
        try {
            unitMap = unit;
            unitMap = unitMap.replace("\\", "");
            Process p = Runtime.getRuntime().exec("SUBST " + unitMap + " \"" + extractionpoint + "\"");
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(unitMap);
    }

    /**
     * 
     * @param extractionpoint
     * @return
     */
    public boolean DeleteUnit(String extractionpoint) {
        try {
            String stringComando = "SUBST " + extractionpoint + " /D ";
            Process p = Runtime.getRuntime().exec(stringComando);
            int intError = p.waitFor();
            if (intError != 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
