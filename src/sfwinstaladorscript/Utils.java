/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript;

import at.jta.Key;
import at.jta.Regor;
import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Classe com funções úteis.
 */
public class Utils {

    public static SfwInstaladorScriptView _view;
    private static boolean _proxyActive = false;
    private static boolean _objects_validade = false;

    /**
     * Retorna nome do m�s.
     * @param month n�mero do m�s (1-12).
     * @return String com o nome do m�s.
     */
    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return Utils.getDefaultBundle().getString("Month.january");
            case 2:
                return Utils.getDefaultBundle().getString("Month.february");
            case 3:
                return Utils.getDefaultBundle().getString("Month.march");
            case 4:
                return Utils.getDefaultBundle().getString("Month.april");
            case 5:
                return Utils.getDefaultBundle().getString("Month.may");
            case 6:
                return Utils.getDefaultBundle().getString("Month.june");
            case 7:
                return Utils.getDefaultBundle().getString("Month.july");
            case 8:
                return Utils.getDefaultBundle().getString("Month.august");
            case 9:
                return Utils.getDefaultBundle().getString("Month.september");
            case 10:
                return Utils.getDefaultBundle().getString("Month.october");
            case 11:
                return Utils.getDefaultBundle().getString("Month.november");
            case 12:
                return Utils.getDefaultBundle().getString("Month.december");
            default:
                return Utils.getDefaultBundle().getString("Month.undentified");
        }
    }

    /**
     * Retorna resouce padrão do aplicativo.
     * @return Objeto resource.
     */
    public static ResourceBundle getDefaultBundle() {
        return java.util.ResourceBundle.getBundle("sfwinstaladorscript/resources/SfwInstaladorScriptDefault");
    }

    /**
     * Retorna resouce padrão do objeto passado.
     * @param o Objeto.
     * @return Objeto resource.
     */
    public static ResourceBundle getDefaultBundle(Object o) {
        String v_string_path = o.getClass().getCanonicalName();
        v_string_path = v_string_path.replace(".", "/");
        v_string_path = v_string_path.substring(0, v_string_path.lastIndexOf("/")) + "/resources" + v_string_path.substring(v_string_path.lastIndexOf("/"), v_string_path.length());
        return java.util.ResourceBundle.getBundle(v_string_path);
    }

    /**
     * Função NVL para retornar outro valor quando o texto passado for nulo ou vazio.
     * @param text Texto a ser verificado.
     * @param nullvalue Valor a ser colocado em caso de nulidade.
     * @return Texto ou valor de nulo.
     */
    public static String nvl(String text, String nullvalue) {
        if (text == null || text.equals("")) {
            return nullvalue;
        } else {
            return text;
        }
    }

    /**
     * Função NVL para retornar outro valor quando o texto passado for nulo ou vazio.
     * Acrescenta prefixo ou sufixo no texto caso não seja nulo.
     * @param text Texto a ser verificado.
     * @param nullvalue Valor a ser colocado em caso de nulidade.
     * @param prefix Prefixo a ser colocado no texto em caso de não ser nulo.
     * @param sufix Sufixo a ser colocado no texto em caso de não ser nulo.
     * @return Texto com prefixo e sufixo ou valor de nulo.
     */
    public static String nvl(String text, String nullvalue, String prefix, String sufix) {
        if (text == null || text.equals("")) {
            return nullvalue;
        } else {
            return prefix + text + sufix;
        }
    }

    /**
     * Função para copiar um arquivo.
     * @param in Arquivo de entrada.
     * @param out Arquivo de saída.
     * @throws java.io.IOException
     */
    public static void copyFile(File in, File out)
            throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * Apaga um diretório com arquivos.
     * @param path Diretório a ser apagado.
     * @return TRUE- se apagou com sucesso FALSE- se não apagou
     */
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    Utils.deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    /**
     * Lê uma chave do registro do Windows.
     * @param regpath Caminho
     * @param regkey Nome da chave
     * @return Valor da chave lida.
     */
    public static String readRegistry(String regpath, String regkey) {
        try {
            Process process = Runtime.getRuntime().exec("reg query " + regpath + " /v " + regkey);
            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String result = reader.getResult();
            int p = result.indexOf("REG_SZ");
            int p2 = result.indexOf("REG_EXPAND_SZ");

            if (p == -1 && p2 == -1) {
                return null;
            }

            if (p2 == -1) {
                return result.substring(p + "REG_SZ".length()).trim();
            } else {
                return result.substring(p2 + "REG_EXPAND_SZ".length()).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lê uma chave do registro do Windows.
     * @param regpath Caminho
     * @return Lista com subdiretórios do caminho.
     */
    public static ArrayList readRegistry(String regpath) {
        ArrayList v_arraylist_subdirs = new ArrayList();

        try {
            Process process = Runtime.getRuntime().exec("reg query " + regpath);
            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String result = reader.getResult();

            String[] subdirs = result.split(System.getProperty("line.separator"));

            for (int i = 0; i < subdirs.length; i++) {
                if (subdirs[i].indexOf(regpath) != -1) {
                    v_arraylist_subdirs.add(subdirs[i].trim());
                }
            }

            return v_arraylist_subdirs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Altera cursor da aplicação para ocupado.
     */
    public static void setBusyCursor() {
        Utils._view.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    /**
     * Altera cursor da aplicação para padrão.
     */
    public static void setDefaultCursor() {
        Utils._view.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     *  Cria bat com settings iniciais antes de chamar executavel do produto.
     * @param extractionpoint
     * @param execname
     * @throws IOException
     */
    public static void createWindowsStartInstallBat(File createpoint, String execname) throws IOException {
        File v_file_startinstall = new File(createpoint, "startinstall.bat");
        BufferedWriter v_bufferedwriter_startinstall = null;

        try {
            if (!v_file_startinstall.exists()) {
                v_file_startinstall.createNewFile();
            }
            v_bufferedwriter_startinstall = new BufferedWriter(new FileWriter(v_file_startinstall));

            v_bufferedwriter_startinstall.write("echo off");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("cls");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo Starting...");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo ==============================================");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo.");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo set nls_lang=AMERICAN_AMERICA.WE8ISO8859P1");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo.");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("set nls_lang=AMERICAN_AMERICA.WE8ISO8859P1");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("echo on");
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.write("CALL " + execname);
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.newLine();
            v_bufferedwriter_startinstall.close();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                v_bufferedwriter_startinstall.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return the _proxyActive
     */
    public static boolean isProxyActive() {
        return _proxyActive;
    }

    /**
     * @param aProxyActive the _proxyActive to set
     */
    public static void setProxyActive(boolean aProxyActive) {
        _proxyActive = aProxyActive;
    }

    /**
     * Leitura por StreamReader
     */
    static class StreamReader extends Thread {

        private InputStream is;
        private StringWriter sw;

        StreamReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String getResult() {
            return sw.toString();
        }
    }

    /**
     * Descriptografa o texto passado como parâmetro.
     *
     * @param String Texto a ser descriptografado.
     */
    static public String decrypt(String p_s_senha) {
        String s_senha;
        int n_tamanho;
        int n_indice;
        int n_incr_pos;
        int n_incr_char;
        char s_char[];
        int n_posicao;
        int temp;
        char s_caracter;

        String s_senha_deco = "";

        try {
            s_senha = p_s_senha;
            n_tamanho = (s_senha.length() - 2) / 2;

            s_char = new char[n_tamanho];

            n_indice = 0;
            n_incr_pos = ((int) s_senha.charAt(0)) - 33;
            n_incr_char = ((int) s_senha.charAt(1)) - 33;
            s_senha = s_senha.substring(2, p_s_senha.length());
            while (n_indice <= n_tamanho - 1) {
                n_posicao = ((int) s_senha.charAt(0)) - 90 + n_incr_pos;
                temp = ((int) s_senha.charAt(1));
                temp = temp - n_incr_char;
                s_caracter = (char) temp;
                s_char[n_posicao] = s_caracter;
                s_senha = s_senha.substring(2, s_senha.length());
                n_indice++;
            }
            n_indice = 0;

            while (n_indice <= n_tamanho - 1) {
                s_senha_deco = s_senha_deco + s_char[n_indice];
                n_indice++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(s_senha_deco.trim());
    }

    /**
     * Criptografa o texto passado como parâmetro.
     *
     * @param String Texto a ser criptografado.
     */
    static public String crypt(String s_senha, int n_tamanho) {
        boolean b_ok;

        int n_array[] = new int[255];
        int n_index;
        int n_verify;
        int n_valor = 0;
        int n_char[] = new int[255];
        int n_incr_pos;
        int n_incr_char;

        Character car1;
        String s_senha_crypto = new String();
        Random r_random = new Random();
        n_index = 1;
        n_incr_char = r_random.nextInt();

        if (n_incr_char < 0) {
            n_incr_char = n_incr_char * (-1);
        }

        n_incr_char = (n_incr_char % 4) + 1;
        n_incr_pos = r_random.nextInt();

        if (n_incr_pos < 0) {
            n_incr_pos = n_incr_pos * (-1);
        }

        n_incr_pos = n_incr_pos % 10;

        s_senha = s_senha + repeatChar(" ", n_tamanho);
        s_senha = s_senha.substring(0, n_tamanho);

        while (n_index <= n_tamanho) {
            b_ok = false;
            while (!b_ok) {
                n_valor = r_random.nextInt();

                if (n_valor < 0) {
                    n_valor = n_valor * (-1);
                }

                n_valor = n_valor % n_tamanho;
                b_ok = true;
                n_verify = 1;
                while (n_verify < n_index) {
                    if (n_array[n_verify] == n_valor) {
                        b_ok = false;
                    }
                    n_verify++;
                }
            }

            n_array[n_index] = n_valor;
            n_index++;
        }

        n_index = 0;

        while (n_index <= n_tamanho - 1) {
            n_char[n_index] = (int) s_senha.charAt(0);
            n_index++;
            s_senha = s_senha.substring(1, s_senha.length());
        }

        car1 = new Character((char) (n_incr_pos + 33));
        s_senha_crypto = car1.toString();
        car1 = new Character((char) (n_incr_char + 33));
        s_senha_crypto = s_senha_crypto + car1.toString();

        n_index = 1;

        while (n_index <= n_tamanho) {
            car1 = new Character((char) (n_array[n_index] + 90 - n_incr_pos));
            s_senha_crypto = s_senha_crypto + car1.toString();
            car1 = new Character((char) (n_char[n_array[n_index]] + n_incr_char));
            s_senha_crypto = s_senha_crypto + car1.toString();

            n_index++;
        }

        return s_senha_crypto;
    }

    /**
     * Criptografa o texto passado como parâmetro.
     *
     * @param String Texto a ser criptografado.
     */
    private static String repeatChar(String p_s_letra, int p_i_tamanho) {
        String s_concatenada = "";
        for (int i = 0; i <= p_i_tamanho; i++) {
            s_concatenada = s_concatenada + p_s_letra;
        }

        return s_concatenada;
    }

    /**
     * Trata a senha para inserir no oracle retirando aspas simples.
     * @param v_senha
     * @return
     */
    public static String RetornaSenhaCryptoSemAspas(String v_senha) {
        String v_senhaCrypto = "";
        boolean geraSenha = true;
        try {
            while (geraSenha) {
                v_senhaCrypto = Utils.crypt(v_senha, 20);
                if (!v_senhaCrypto.contains("'")) {
                    return v_senhaCrypto;
                }
            }
            return v_senhaCrypto;
        } catch (Exception e) {
            return v_senhaCrypto;
        }
    }

    /**
     * Ativa ou desativa o proxy para estabelecer conex�o com o banco.
     * @param ativo true ou false
     * @return true se nao ocorrer nenhum erro.
     */
    public static boolean ProxyServer(boolean ativo) {
        String REGISTRY_KEY;
        Regor obj;
        try {
            if (System.getProperty("os.arch").equalsIgnoreCase("x86")) { // Verifica qual a vers�o do windows
                REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
            } else {
                REGISTRY_KEY = "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
            }

            obj = new Regor();
            Key key = obj.openKey(Regor.HKEY_CURRENT_USER, REGISTRY_KEY);

            if (ativo) {
                if (Utils.isProxyActive()) {
                    obj.saveDword(key, "ProxyEnable", "1");
                }
            } else {
                obj.saveDword(key, "ProxyEnable", "0");
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 
     */
    public static void ActiveProxy() {
        String REGISTRY_KEY;
        Regor obj;
        try {
            if (System.getProperty("os.arch").equalsIgnoreCase("x86")) {
                REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
            } else {
                REGISTRY_KEY = "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
            }

            obj = new Regor();
            Key key = obj.openKey(Regor.HKEY_CURRENT_USER, REGISTRY_KEY);

            if (obj.readDword(key, "ProxyEnable").equals("0x1")) {
                Utils.setProxyActive(true);
            } else {
                Utils.setProxyActive(false);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 
     * @param dirImp
     * @param dirTmp
     * @param filenames
     * @param outFilename
     */
    public static String zipFiles(String dirImp, String dirTmp, String outFilename) throws FileNotFoundException, IOException {
        String arquivo = "";
        List<String> zipados = new ArrayList<String>();
        File[] filenames = new File(dirImp).listFiles();
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dirTmp + "\\" + outFilename));
        for (int i = 0; i < filenames.length; i++) {
            FileInputStream in = new FileInputStream(dirImp + "\\" + filenames[i].getName());
            out.putNextEntry(new ZipEntry(filenames[i].getName()));

            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.closeEntry();
            in.close();
        }
        out.close();
        arquivo = dirTmp + "\\" + outFilename;
        return arquivo;
    }

    /**
     * 
     * @param pasta
     */
    public static void limpaPasta(String pasta) {
        //
        try {
            File[] pst = new File(pasta).listFiles();

            for (File f : pst) {
                if (f.isDirectory()) {
                    if ((f.listFiles().length > 0)) {
                        limpaPasta(f.getAbsolutePath());
                    }
                }
                f.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //
    }

    /**
     *
     * @return
     */
    public static boolean isObjects_validade() {
        return _objects_validade;
    }

    /**
     * 
     * @param _objects_validade
     */
    public static void setObjects_validade(boolean _objects_validade) {
        Utils._objects_validade = _objects_validade;
    }
}
