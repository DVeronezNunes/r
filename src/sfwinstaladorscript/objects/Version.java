package sfwinstaladorscript.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.database.SfwConnection;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;

/**
 * Representa uma versÃ£o de um produto.
 */
public class Version implements SfwComboBoxItem {

    private productinfo.Version _xmlversion;
    private String _packagename;
    private String _packageextesion;
    private static String _testresult = "";

    /**
     * Comparador para a classe Version.
     */
    public static class VersionComparator
            implements Comparator {

        public int compare(Object element1,
                Object element2) {
            Version v_version_v1 = (Version) element1;
            Version v_version_v2 = (Version) element2;

            return v_version_v1.compare(v_version_v2);
        }
    }

    public Version(productinfo.Version xmlversion, String package_name, String package_extension) {
        this._xmlversion = xmlversion;
        this._packagename = package_name;
        this._packageextesion = package_extension;

        for (int i = 0; i < this._xmlversion.getDiffArray().length; i++) {
            this._xmlversion.getDiffArray()[i].setPkg(package_name);

            if (this.getBuildNumber() != 0) {
                if (this.getCustomNumber() != 0) {
                    this._xmlversion.getDiffArray()[i].setName(this._xmlversion.getDiffArray()[i].getV() + "." + this._xmlversion.getDiffArray()[i].getR() + "." + this._xmlversion.getDiffArray()[i].getP() + " " + this._xmlversion.getDiffArray()[i].getB() + "-C" + this._xmlversion.getDiffArray()[i].getC() + " > " + this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + this.getBuildNumber() + "-C" + this.getCustomNumber());
                } else {
                    this._xmlversion.getDiffArray()[i].setName(this._xmlversion.getDiffArray()[i].getV() + "." + this._xmlversion.getDiffArray()[i].getR() + "." + this._xmlversion.getDiffArray()[i].getP() + " " + this._xmlversion.getDiffArray()[i].getB() + " > " + this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + this.getBuildNumber());
                }
            } else {
                if (this.getCustomNumber() != 0) {
                    this._xmlversion.getDiffArray()[i].setName(this._xmlversion.getDiffArray()[i].getV() + "." + this._xmlversion.getDiffArray()[i].getR() + "." + this._xmlversion.getDiffArray()[i].getP() + " " + "-C" + this._xmlversion.getDiffArray()[i].getC() + " > " + this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + this.getBuildNumber() + "-C" + this.getCustomNumber());
                } else {
                    this._xmlversion.getDiffArray()[i].setName(this._xmlversion.getDiffArray()[i].getV() + "." + this._xmlversion.getDiffArray()[i].getR() + "." + this._xmlversion.getDiffArray()[i].getP() + " > " + this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber());
                }
            }

            if (this._xmlversion.getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                this._xmlversion.getDiffArray()[i].setBasecomparer(this._xmlversion.getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
            }

            if (this._xmlversion.getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray().length == 1) {
                this._xmlversion.getDiffArray()[i].setCompversion(this._xmlversion.getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray()[0].getPath());
            }
        }

        for (int i = 0; i < this._xmlversion.getFullArray().length; i++) {
            this._xmlversion.getFullArray()[i].setPkg(package_name);

            if (this.getBuildNumber() != 0) {
                if (this.getCustomNumber() != 0) {
                    this._xmlversion.getFullArray()[i].setName(this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + this.getBuildNumber() + "-C" + this.getCustomNumber());
                } else {
                    this._xmlversion.getFullArray()[i].setName(this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + this.getBuildNumber());
                }
            } else {
                if (this.getCustomNumber() != 0) {
                    this._xmlversion.getFullArray()[i].setName(this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber() + " " + "-C" + this.getCustomNumber());
                } else {
                    this._xmlversion.getFullArray()[i].setName(this.getVersionNumber() + "." + this.getReleaseNumber() + "." + this.getPatchNumber());
                }
            }

            if (this._xmlversion.getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                this._xmlversion.getFullArray()[i].setBasecomparer(this._xmlversion.getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
            }

            if (this._xmlversion.getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray().length == 1) {
                this._xmlversion.getFullArray()[i].setCompversion(this._xmlversion.getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray()[0].getPath());
            }
        }
    }

    /**
     * Retorna número da versão
     * @return número da versão
     */
    public int getVersionNumber() {
        return this._xmlversion.getV();
    }

    /**
     * Retorna nÃºmero do release.
     * @return NÃºmero do release.
     */
    public int getReleaseNumber() {
        return this._xmlversion.getR();
    }

    /**
     * Retorna número do patch.
     * @return número do patch.
     */
    public int getPatchNumber() {
        return this._xmlversion.getP();
    }

    /**
     * Retorna o número do custom
     * @return Retorna o número do custom
     */
    public int getCustomNumber() {
        try {
            return this._xmlversion.getC();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna o número do Build
     * @return Retorna o número do Build
     */
    public int getBuildNumber() {
        try {
            return this._xmlversion.getB();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna o número do Build
     * @return Retorna o número do Build
     */
    public String getVersionType() {
        try {
            if (this._xmlversion.getTv() == null) {
                return "";
            } else {
                return this._xmlversion.getTv();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retorna objeto versÃ£o XML.
     * @return Objeto VersÃ£o XML.
     */
    public productinfo.Version get_xmlversion() {
        return this._xmlversion;
    }

    /**
     * Realiza mescla de versÃµes.
     * @param v VersÃ£o a ser mesclada.
     */
    public void merge(Version v) {
        for (int i = 0; i < v.get_xmlversion().getDiffArray().length; i++) {
            this._xmlversion.addNewDiff();
            this._xmlversion.getDiffArray()[this._xmlversion.getDiffArray().length - 1] = v.get_xmlversion().getDiffArray()[i];
        }

        for (int i = 0; i < v.get_xmlversion().getFullArray().length; i++) {
            this._xmlversion.addNewFull();
            this._xmlversion.getFullArray()[this._xmlversion.getFullArray().length - 1] = v.get_xmlversion().getFullArray()[i];
        }
    }

    /**
     * Compara duas Vers�es
     * @param v Vers�o a ser comparada;
     * @return 0 - iguais, 1- maior, -1 menor
     */
    public int compare(Version v) {
        if(v.getVersionNumber() > this.getVersionNumber()){
            // v_version_v2.VersionNumber > v_version_v1.VersionNumber
            return 1;
        } else if(v.getVersionNumber() < this.getVersionNumber()){
            // v_version_v2.VersionNumber < v_version_v1.VersionNumber
            return -1;
        } else{
            if(v.getReleaseNumber() > this.getReleaseNumber()){
                // v_version_v2.ReleaseNumber > v_version_v1.ReleaseNumber
                return 1;
            } else if(v.getReleaseNumber() < this.getReleaseNumber()){
                // v_version_v2.ReleaseNumber < v_version_v1.ReleaseNumber
                return -1;
            } else{
                if(v.getPatchNumber() > this.getPatchNumber()){
                    // v_version_v2.PatchNumber > v_version_v1.PatchNumber
                    return 1;
                } else if(v.getPatchNumber() < this.getPatchNumber()){
                    // v_version_v2.PatchNumber < v_version_v1.PatchNumber
                    return -1;
                } else{
                    if(v.getBuildNumber() > this.getBuildNumber()){
                        // v_version_v2.BuildNumber > v_version_v1.BuildNumber
                        return 1;
                    } else if(v.getBuildNumber() < this.getBuildNumber()){
                        // v_version_v2.BuildNumber < v_version_v1.BuildNumber
                        return -1;
                    } else{
                        return 0;
                    }
                }
            }
        }
    }

    /**
     * Retorna comparador para ordena��o de lista.
     * @return Comparador das vers�es.
     */
    public static Comparator get_comparator() {
        return new VersionComparator();
    }

    /**
     * Descrição para o SfwComboBox.
     * @return Nome da versão.
     */
    public String get_description() {
        String v_string_version, v_string_release, v_string_patch, v_string_custom = "", v_string_versiontype = "", v_string_build = "";

        if (this.getVersionNumber() < 10 || this.getVersionNumber() == 0) {
            v_string_version = "0" + this.getVersionNumber();
        } else {
            v_string_version = "" + this.getVersionNumber();
        }

        if (this.getReleaseNumber() < 10 || this.getReleaseNumber() == 0) {
            v_string_release = "0" + this.getReleaseNumber();
        } else {
            v_string_release = "" + this.getReleaseNumber();
        }

        if (this.getPatchNumber() < 10 || this.getPatchNumber() == 0) {
            v_string_patch = "0" + this.getPatchNumber();
        } else {
            v_string_patch = "" + this.getPatchNumber();
        }

        if (this.getCustomNumber() != 0) {
            if (this.getCustomNumber() < 10) {
                v_string_custom = " C0" + this.getCustomNumber();
            } else {
                v_string_custom = " C" + this.getCustomNumber();
            }
        }

        if (this.getBuildNumber() != 0) {
            if (!this.getVersionType().equals("RTM")) {
                v_string_build = "" + this.getBuildNumber();
            }
        }

        if (!this.getVersionType().equals("")) {
            v_string_versiontype = " " + this.getVersionType();
        }

        return "" + v_string_version + "." + v_string_release + "." + v_string_patch + v_string_custom + v_string_versiontype + v_string_build;
    }

    /**
     * Retorna pacote de diferen�a para a vers�o se houver.
     * @param v Parte vers�o da vers�o da qual se quer achar a diferen�a.
     * @param r Parte release da vers�o da qual se quer achar a diferen�a.
     * @param p Parte patch da vers�o da qual se quer achar a diferen�a.
     * @return Pacote de diferen�a.
     */
    public productinfo.Diff getDiff(int v, int r, int p, int b, SfwConnection conn) {
        productinfo.Diff v_diff_version = null;

        /*if (b == 0) {
            for (int i = 0; i < this.get_xmlversion().getDiffArray().length; i++) {
                if (this._xmlversion.getDiffArray()[i].getV() == v
                        && this._xmlversion.getDiffArray()[i].getR() == r
                        && this._xmlversion.getDiffArray()[i].getP() == p) {
                    v_diff_version = this._xmlversion.getDiffArray()[i];

                    // verifica se pacote estão OK para instalacao (base e OS)
                    if (this.verifyPackage(v_diff_version, conn)) {
                        return v_diff_version;
                    } else {
                        return null;
                    }
                }
            }
        } else {*/
            for (int i = 0; i < this.get_xmlversion().getDiffArray().length; i++) {
                if (this._xmlversion.getDiffArray()[i].getV() == v
                        && this._xmlversion.getDiffArray()[i].getR() == r
                        && this._xmlversion.getDiffArray()[i].getP() == p
                        && this._xmlversion.getDiffArray()[i].getB() == b) {
                    v_diff_version = this._xmlversion.getDiffArray()[i];

                    // verifica se pacote estão OK para instalacao (base e OS)
                    if (this.verifyPackage(v_diff_version, conn)) {
                        return v_diff_version;
                    } else {
                        return null;
                    }
                }
            }
        //}
        return v_diff_version;
    }

    /**
     * 
     * @param v
     * @param r
     * @param p
     * @param c
     * @param conn
     * @return
     */
    public productinfo.Diff getDiff(int v, int r, int p, int b, int c, SfwConnection conn) {
        productinfo.Diff v_diff_version = null;

        for (int i = 0; i < this.get_xmlversion().getDiffArray().length; i++) {
            if (this._xmlversion.getDiffArray()[i].getV() == v
                    && this._xmlversion.getDiffArray()[i].getR() == r
                    && this._xmlversion.getDiffArray()[i].getP() == p
                    && this._xmlversion.getDiffArray()[i].getB() == b
                    && this._xmlversion.getDiffArray()[i].getC() == c) {
                v_diff_version = this._xmlversion.getDiffArray()[i];

                // verifica se pacote estão OK para instalacao (base e OS)
                if (this.verifyPackage(v_diff_version, conn)) {
                    return v_diff_version;
                } else {
                    return null;
                }
            }
        }

        return v_diff_version;
    }

    /**
     * Retorna array de pacotes de diferença da versão.
     * @return Array de pacotes de diferença.
     */
    public productinfo.Diff[] getDiffs() {
        return this._xmlversion.getDiffArray();
    }

    /**
     * Retorna pacote completo da versão se houver.
     * @return Pacote completo.
     */
    public productinfo.Full getFull(SfwConnection conn) {
        productinfo.Full v_full_version = null;

        if (this.get_xmlversion().getFullArray().length > 0) {
            v_full_version = this.get_xmlversion().getFullArray()[0];
        }

        // verifica se pacote est� OK para instalacao (base e OS)
        if (this.verifyPackage(v_full_version, conn)) {
            return v_full_version;
        } else {
            return null;
        }
    }

    /**
     * Verifica se pacote estão OK para instalação nas configurações escolhidas.
     * @param diff
     * @return TRUE - estão OK  FALSE - não tem pacote da base de dados ou do OS
     */
    private boolean verifyPackage(Object obj, SfwConnection conn) {
        OracleConnection v_oracleconnection_conn;
        productinfo.Oracle v_oracle_db;
        Method v_method_oraclearray;

        if (obj == null) {
            return false;
        }

        try {
            v_method_oraclearray = obj.getClass().getMethod("getOracleArray");

            if (Install.get_database().get_name().toUpperCase().startsWith("ORACLE")) {
                if (((productinfo.Oracle[]) v_method_oraclearray.invoke(obj)).length == 0) {
                    return false;
                }

                v_oracleconnection_conn = (OracleConnection) conn;

                for (int j = 0; j < ((productinfo.Oracle[]) v_method_oraclearray.invoke(obj)).length; j++) {
                    v_oracle_db = ((productinfo.Oracle[]) v_method_oraclearray.invoke(obj))[j];

                    if (v_oracle_db.getTag().toLowerCase().equals(v_oracleconnection_conn.get_tag().toLowerCase())) {
                        if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                            if (v_oracle_db.getWindowsArray().length == 0) {
                                return false;
                            } else {
                                return true;
                            }

                        } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                            if (v_oracle_db.getLinuxArray().length == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }

                for (int j = 0; j < ((productinfo.Oracle[]) v_method_oraclearray.invoke(obj)).length; j++) {
                    v_oracle_db = ((productinfo.Oracle[]) v_method_oraclearray.invoke(obj))[j];

                    if (v_oracle_db.getTag().toLowerCase().equals("any")) {
                        if (Install.get_system().get_name().toUpperCase().equals("WINDOWS")) {
                            if (v_oracle_db.getWindowsArray().length == 0) {
                                return false;
                            } else {
                                return true;
                            }

                        } else if (Install.get_system().get_name().toUpperCase().equals("LINUX")) {
                            if (v_oracle_db.getLinuxArray().length == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Retorna nome do pacote.
     * @return Nome do pacote.
     */
    public String get_packagename() {
        return _packagename;
    }

    /**
     * Retorna extensão do pacote.
     * @return String com extensão do pacote.
     */
    public String get_packageextesion() {
        return _packageextesion;
    }

    /**
     * Testa pacote full.
     */
    public static String testFull(productinfo.Full full, Product product, String systemos, String database) throws IOException, InterruptedException, SQLException, Exception {
        Package v_package_pkgfull;
        File v_file_extraction_point;
        productinfo.Oracle v_oracle_db;
        productinfo.Executable v_executable_exec;

        Version._testresult = "";

        // pega o pacote e inicializa ponto de extracao
        v_package_pkgfull = Install.get_packagelist().getPackageByName(full.getPkg());
        v_file_extraction_point = new File("test" + System.getProperty("file.separator") + full.getPkg());
        if (!v_file_extraction_point.exists()) {
            v_file_extraction_point.mkdirs();
        }

        // extrai arquivos necessarios do pacote
        Version.extractZip(v_package_pkgfull.get_zip(), v_file_extraction_point, product.get_folder());

        //copia comparador para temporaria de comparador
        Version.testComparer(v_file_extraction_point, full, null, product);

        if (database.toUpperCase().startsWith("ORACLE")) {
            // loop para pegar executaveis de qualquer versao do oracle
            for (int i = 0; i < full.getOracleArray().length; i++) {

                v_oracle_db = full.getOracleArray()[i];

                if (v_oracle_db.getTag().toLowerCase().equals("any")) {
                    // se for windows
                    if (systemos.toUpperCase().equals("WINDOWS")) {
                        for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                            v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                            // executa
                            Version.testExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog(), product);
                        }
                    } // se for linux
                    else if (systemos.toUpperCase().equals("LINUX")) {
                        for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                            v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                            // executa
                            //this.testExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                        }
                    }
                }
            }
        }

        //limpa ponto de extracao da temp
        Utils.deleteDirectory(new File("test"));

        return (Version._testresult);
    }

    /**
     * Testa pacote diff.
     */
    public static String testDiff(productinfo.Diff diff, Product product, String systemos, String database) throws IOException, InterruptedException, Exception {
        Package v_package_pkgdiff;
        File v_file_extraction_point;
        productinfo.Oracle v_oracle_db;
        productinfo.Executable v_executable_exec;

        Version._testresult = "";

        // pega o pacote e inicializa ponto de extracao
        v_package_pkgdiff = Install.get_packagelist().getPackageByName(diff.getPkg());
        v_file_extraction_point = new File("test" + System.getProperty("file.separator") + diff.getPkg());
        if (!v_file_extraction_point.exists()) {
            v_file_extraction_point.mkdirs();
        }

        // extrai arquivos necessarios do pacote
        Version.extractZip(v_package_pkgdiff.get_zip(), v_file_extraction_point, product.get_folder());

        //copia comparador para temporaria de comparador
        Version.testComparer(v_file_extraction_point, null, diff, product);

        if (database.toUpperCase().startsWith("ORACLE")) {
            // loop para pegar executaveis de qualquer versao do oracle
            for (int i = 0; i < diff.getOracleArray().length; i++) {

                v_oracle_db = diff.getOracleArray()[i];

                if (v_oracle_db.getTag().toLowerCase().equals("any")) {
                    if (systemos.toUpperCase().equals("WINDOWS")) {
                        for (int j = 0; j < v_oracle_db.getWindowsArray()[0].getExecutableArray().length; j++) {
                            v_executable_exec = v_oracle_db.getWindowsArray()[0].getExecutableArray()[j];

                            // executa
                            Version.testExecWindows(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog(), product);
                        }
                    } else if (systemos.toUpperCase().equals("LINUX")) {
                        for (int j = 0; j < v_oracle_db.getLinuxArray()[0].getExecutableArray().length; j++) {
                            v_executable_exec = v_oracle_db.getLinuxArray()[0].getExecutableArray()[j];

                            // executa
                            //this.testExecLinux(v_file_extraction_point, v_executable_exec.getPath(), v_executable_exec.getLog());
                        }
                    }
                }
            }
        }

        //limpa ponto de extracao da temp
        Utils.deleteDirectory(new File("test"));

        return (Version._testresult);
    }

    /**
     * Testa executavel windows.
     * @param extractionpoint Ponto de extraÃ§Ã£o temporÃ¡ria do pacote.
     * @param productfolder Pasta do produto.
     * @param path Caminho do executÃ¡vel.
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    private static void testExecWindows(File extractionpoint, String path, String logfile, Product product) throws IOException, InterruptedException, Exception {
        String v_string_exec;
        String v_string_dir_path;
        Process v_process_installexec = null;

        // parse dos caminhos
        v_string_exec = path.substring(path.lastIndexOf(System.getProperty("file.separator")) + 1, path.length());
        v_string_dir_path = extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + product.get_folder() + path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);

        // chama executavel
        //v_process_installexec = java.lang.Runtime.getRuntime().exec("cmd /C start \""++" "+this._productdetail.getProduct().get_label()+" "+this.getPackageName()+"\" /D \""+v_string_dir_path+"\" /WAIT "+v_string_exec);
    }

    /**
     * Testa comparador de base.
     * @param p_file_extraction_point
     * @throws java.io.IOException
     */
    private static void testComparer(File p_file_extraction_point, productinfo.Full full, productinfo.Diff diff, Product product) throws IOException {
        File v_file_comparer;
        File v_file_comparer_tmp;
        File v_file_comparer_version = null;

        v_file_comparer = new File("temp" + System.getProperty("file.separator") + "basecomparer" + System.getProperty("file.separator") + product.get_shortname().toLowerCase() + "_comparer.sql");

        if (v_file_comparer.exists()) {
            v_file_comparer.delete();
        }

        if (full != null) {
            if (full.getBasecomparer() != null) {
                if (!full.getBasecomparer().equals("")) {
                    v_file_comparer_tmp = new File("test" + System.getProperty("file.separator") + "basecomparer");

                    if (!v_file_comparer_tmp.exists()) {
                        v_file_comparer_tmp.mkdirs();
                    }

                    v_file_comparer_version = new File(p_file_extraction_point, product.get_folder() + System.getProperty("file.separator") + full.getBasecomparer());

                    if (v_file_comparer_version != null) {
                        if (v_file_comparer_version.exists()) {
                            v_file_comparer.createNewFile();
                            Utils.copyFile(v_file_comparer_version, v_file_comparer);
                        }
                    }
                }
            }
        }

        if (diff != null) {
            if (diff.getBasecomparer() != null) {
                if (!diff.getBasecomparer().equals("")) {
                    v_file_comparer_tmp = new File("test" + System.getProperty("file.separator") + "basecomparer");

                    if (!v_file_comparer_tmp.exists()) {
                        v_file_comparer_tmp.mkdirs();
                    }

                    v_file_comparer_version = new File(p_file_extraction_point, product.get_folder() + System.getProperty("file.separator") + diff.getBasecomparer());

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
     * Extrai informa��es do pacote de instala��o para o produto sendo testado/instalado.
     * @param zipfile
     * @param extractionpoint
     * @param productfolder
     * @throws java.io.IOException
     */
    public static void extractZip(ZipFile zipfile, File extractionpoint, String productfolder) throws IOException {
        // Enumerate each entry
        for (Enumeration entries = zipfile.entries(); entries.hasMoreElements();) {

            // Get the entry and its name
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();

            if (zipEntry.isDirectory()) {
                if (zipEntry.getName().startsWith(productfolder)) {
                    boolean success = (new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntry.getName())).mkdirs();
                }
            } else {
                String zipEntryName = zipEntry.getName();

                if (zipEntryName.startsWith(productfolder)) {
                    boolean success = (new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntryName.substring(0, zipEntryName.lastIndexOf("/")))).mkdirs();

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
    }
    /**
     * Busca o Oracle Instalador na maquina.
     * @return O Nome do banco instalado.
     */
}
