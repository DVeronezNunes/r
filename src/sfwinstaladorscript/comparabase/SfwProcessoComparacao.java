package sfwinstaladorscript.comparabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DefaultCaret;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.comparabase.objects.BaseCompare;
import sfwinstaladorscript.comparabase.objects.Info;
import sfwinstaladorscript.comparabase.objects.Usuario;
import sfwinstaladorscript.database.OracleConnection;

/**
 *
 * @author jopaulo
 */
public class SfwProcessoComparacao extends javax.swing.JDialog implements Runnable {

    private Usuario _usuario;
    private Info _info;
    private String _arquivoOrigem;
    private String _arquivoDestino;
    private String _caminhoComp;
    private DefaultCaret caret;

    public SfwProcessoComparacao(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void executeCompare() {
        // Executa a comparação.
        try {
            BaseCompare v_baseCompareOrigem;
            BaseCompare v_baseCompareDestino;
            OracleConnection con = new OracleConnection();
            con.set_username(this.getUsuario().get_usuario());
            con.set_password(this.getUsuario().get_password());
            con.set_tns(this.getUsuario().get_tns());
            SfwXMLReader v_reader = new SfwXMLReader();
            SfwXMLBuilder v_build = new SfwXMLBuilder(this.getUsuario().get_usuario(), this.getUsuario().get_password(), this.getUsuario().get_tns());
            SfwComparaObjetos compara = new SfwComparaObjetos();
            jTextPaneComparacao.setText(jTextPaneComparacao.getText() + "\n##################### EXECUTANDO COMPARACAO #####################\r\n");
            SfwProcessoComparacao.sysout("-- Escrevendo XML Destino");
            v_build.WriteXML(this.getInfo(), "comparacao\\comparacao_" + this.getInfo().getVERSAO() + ".xml");
            SfwProcessoComparacao.sysout("-- Lendo XML Origem.");
            v_baseCompareOrigem = v_reader.ReadXML(this.getArquivoOrigem());
            SfwProcessoComparacao.sysout("-- Lendo XML Destino");
            v_baseCompareDestino = v_reader.ReadXML(this.getArquivoDestino());
            SfwProcessoComparacao.sysout("-- Comparando.");
            compara.DataBaseCompare(v_baseCompareOrigem, v_baseCompareDestino, this.getCaminhoComp(), con);
            jTextPaneComparacao.setText(jTextPaneComparacao.getText() + "##################### FIM DA EXECUÇÃO #####################");
            this.dispose();
        } catch (Exception e) {
            Utils.setDefaultCursor();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneComparacao = new javax.swing.JTextArea();

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jEditorPane1.setName("jEditorPane1"); // NOI18N
        jScrollPane2.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwProcessoComparacao.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextPaneComparacao.setColumns(20);
        jTextPaneComparacao.setFont(resourceMap.getFont("jTextPaneComparacao.font")); // NOI18N
        jTextPaneComparacao.setRows(5);
        caret = (DefaultCaret) jTextPaneComparacao.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jTextPaneComparacao.setCaret(caret);
        jTextPaneComparacao.setName("jTextPaneComparacao"); // NOI18N
        jScrollPane1.setViewportView(jTextPaneComparacao);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTextArea jTextPaneComparacao;
    // End of variables declaration//GEN-END:variables

    /**
     * Método para imprimir log
     * 
     * @return
     */
    public static void sysout(String mensagem) {
        try {
            SfwProcessoComparacao.jTextPaneComparacao.setText(SfwProcessoComparacao.jTextPaneComparacao.getText() + "[" + new SimpleDateFormat("k:mm:ss").format(new Date()) + "] " + mensagem + "\r\n");
        } catch (Exception e) {
        }
    }

    /**
     * @return the _usuario
     */
    public Usuario getUsuario() {
        return _usuario;
    }

    /**
     * @param usuario the _usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this._usuario = usuario;
    }

    /**
     * @return the _info
     */
    public Info getInfo() {
        return _info;
    }

    /**
     * @param info the _info to set
     */
    public void setInfo(Info info) {
        this._info = info;
    }

    /**
     * @return the _arquivoOrigem
     */
    public String getArquivoOrigem() {
        return _arquivoOrigem;
    }

    /**
     * @param arquivoOrigem the _arquivoOrigem to set
     */
    public void setArquivoOrigem(String arquivoOrigem) {
        this._arquivoOrigem = arquivoOrigem;
    }

    /**
     * @return the _arquivoDestino
     */
    public String getArquivoDestino() {
        return _arquivoDestino;
    }

    /**
     * @param arquivoDestino the _arquivoDestino to set
     */
    public void setArquivoDestino(String arquivoDestino) {
        this._arquivoDestino = arquivoDestino;
    }

    /**
     * @return the _caminhoComp
     */
    public String getCaminhoComp() {
        return _caminhoComp;
    }

    /**
     * @param caminhoComp the _caminhoComp to set
     */
    public void setCaminhoComp(String caminhoComp) {
        this._caminhoComp = caminhoComp;
    }

    @Override
    public void run() {
        this.executeCompare();
    }
}
