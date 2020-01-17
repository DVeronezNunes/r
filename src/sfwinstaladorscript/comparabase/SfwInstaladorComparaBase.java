package sfwinstaladorscript.comparabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JFrame;
import org.jdesktop.application.Action;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Task;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.SfwInstaladorScriptApp;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.comparabase.objects.Info;
import sfwinstaladorscript.comparabase.objects.InstallCompare;
import sfwinstaladorscript.comparabase.objects.Usuario;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.Version;

/**
 *
 * @author jopaulo
 */
@SuppressWarnings("empty-statement")
/**
 *Tela de comparaÃ§Ã£o de base
 */
public class SfwInstaladorComparaBase extends javax.swing.JDialog implements Runnable {

    private OracleConnection _oraconn;
    private ArrayList _listaProdutos;
    private Hashtable _hashProduto;
    private SfwCompareProduct _compareProduct;
    File _compBaseBAT;
    File _compBaseSQL;
    final SfwInstaladorComparaBase SfwInstaladorComparaBase = this;
    SfwProcessoComparacao frame;

    @Override
    public void run() {
        frame.setVisible(true);
    }

    /**
     * Classe para salvar produtos na hashTable
     */
    public class SfwCompareProduct {

        private Product Produto;
        private String Pacote;
        private String Caminho;
        private Version Versao;

        public String getCaminho() {
            return Caminho;
        }

        public void setCaminho(String Caminho) {
            this.Caminho = Caminho;
        }

        public String getPacote() {
            return Pacote;
        }

        public void setPacote(String Pacote) {
            this.Pacote = Pacote;
        }

        public Product getProduto() {
            return Produto;
        }

        public void setProduto(Product Produto) {
            this.Produto = Produto;
        }

        public Version getVersao() {
            return Versao;
        }

        public void setVersao(Version Versao) {
            this.Versao = Versao;
        }
    }
    List v_list_oracle = new ArrayList();

    /** Creates new form SfwInstaladorComparaBase */
    public SfwInstaladorComparaBase(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * 
     * @param parent
     * @param modal
     * @param listaProdutos 
     */
    public SfwInstaladorComparaBase(java.awt.Frame parent, boolean modal, ArrayList listaProdutos) {
        this(parent, modal);
        _listaProdutos = listaProdutos;
    }

    /**
     * Função para pegar o nome do produto pelo código
     * @param codigo do produto
     * @return nome do produto
     */
    public Product getProductByCode(int codigo) {
        Product produto;
        Iterator percorreLista;
        percorreLista = this._listaProdutos.iterator();
        while (percorreLista.hasNext()) {
            produto = (Product) percorreLista.next();
            if (produto.get_code().equals(Integer.toString(codigo))) {
                if (!produto.get_name().equals("TRACKING_SYS")) {
                    return produto;
                }
            }
        }
        return null;
    }

    /**
     * Busca produtos instalados na base genérica.
     */
    private class ConectaBaseGenericaTask extends org.jdesktop.application.Task<Object, Void> {

        ConectaBaseGenericaTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() throws SQLException, Exception {

            DefaultTableModel v_tablecompbase_pkg = (DefaultTableModel) jTableCompBase.getModel();
            v_tablecompbase_pkg.setNumRows(0);
            _oraconn = new OracleConnection();
            _hashProduto = new Hashtable();
            ResultSet v_resultset_result = null;
            Product v_Product_Produtos;
            Version v_version_versao = null;
            Iterator v_iterator_percorrerVersao;
            ArrayList v_arraylist_pkg_product;
            String v_string_base_comparer = "";

            Utils.setBusyCursor();

            if (jTextUsuario.getText().equals("")) {
                Utils.setDefaultCursor();
                JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaUsuario"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                jTextUsuario.requestFocus();
                return null;
            }

            if (jTextSenha.getText().equals("")) {
                Utils.setDefaultCursor();
                JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaSenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                jTextSenha.requestFocus();
                return null;
            }

            if (jTextTNS.getText().equals("")) {
                Utils.setDefaultCursor();
                JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaTNS"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                jTextTNS.requestFocus();
                return null;
            }

            _oraconn.set_tns(jTextTNS.getText());
            _oraconn.set_username(jTextUsuario.getText());
            _oraconn.set_password(jTextSenha.getText());

            try {
                _oraconn.Connect();
            } catch (SQLException e) {
                Utils.setDefaultCursor();
                System.out.println(e.getMessage());
                if (e.getErrorCode() == 1017) {
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validausuariosenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    jTextUsuario.requestFocus();
                }
                if (e.getErrorCode() == 12154) {
                    Utils.setDefaultCursor();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.tnsinvalido"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    jTextTNS.requestFocus();
                }
                _oraconn.Close();
                return null;
            }

            //Select dos produtos instalador na base genérica
            try {
                v_resultset_result = _oraconn.Query("SELECT SFV.VERSAO, "
                        + "SFV.RELEASE, "
                        + "SFV.PATCH, "
                        + "SFV.BUILD, "
                        + "SCS.COD_SISTEMA, "
                        + "SCS.S_SCHEMA_OWNER, "
                        + "SCS.S_OWNER_PASS, "
                        + "SCS.S_CRIPTO, "
                        + "SCS.S_TNS "
                        + "FROM SFW_SISTEMA_VERSAO SFV, SFW_CM_SCHEMA SCS "
                        + "WHERE SFV.VALIDO = 'S' "
                        //+ "AND SFV.COD_SISTEMA not in (0, 2, 3, 6, 9, 10, 11, 21, 500, 607, 500, 19, 1000) "
                        + "AND SFV.COD_SISTEMA = SCS.COD_SISTEMA");
            } catch (SQLException e) {
                Utils.setDefaultCursor();
                e.printStackTrace();
                if (e.getErrorCode() == 942) {
                    Utils.setDefaultCursor();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("basenaoinstalada") + " para o usuário \"" + jTextUsuario.getText().toUpperCase() + "\".", Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            }

            Utils.setBusyCursor();

            //Verifica se existe comparação de base
            while (v_resultset_result.next()) {
                _compareProduct = new SfwCompareProduct();
                try {
                    v_Product_Produtos = getProductByCode(v_resultset_result.getInt("COD_SISTEMA"));
                    v_arraylist_pkg_product = Install.get_packagelist().getProductVersions(v_Product_Produtos.get_name());
                    v_iterator_percorrerVersao = v_arraylist_pkg_product.iterator();
                    while (v_iterator_percorrerVersao.hasNext()) {
                        v_version_versao = (Version) v_iterator_percorrerVersao.next();
                        if (v_version_versao.getVersionNumber() == v_resultset_result.getInt("VERSAO") && v_version_versao.getReleaseNumber() == v_resultset_result.getInt("RELEASE") && v_version_versao.getPatchNumber() == v_resultset_result.getInt("PATCH") && v_version_versao.getBuildNumber() == v_resultset_result.getInt("BUILD")) {
                            for (int i = 0; i < v_version_versao.get_xmlversion().getDiffArray().length; i++) {
                                if (v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                    v_string_base_comparer = Utils.getDefaultBundle().getString("yes");
                                    _compareProduct.setCaminho(v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                    _compareProduct.setPacote(v_version_versao.get_packagename());
                                    _compareProduct.setVersao(v_version_versao);
                                    break;
                                } else {
                                    v_string_base_comparer = Utils.getDefaultBundle().getString("no");
                                }
                            }
                            if (v_string_base_comparer.equals(Utils.getDefaultBundle().getString("no"))) {
                                for (int i = 0; i < v_version_versao.get_xmlversion().getFullArray().length; i++) {
                                    if (v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                        v_string_base_comparer = Utils.getDefaultBundle().getString("yes");
                                        _compareProduct.setCaminho(v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                        _compareProduct.setPacote(v_version_versao.get_packagename());
                                        _compareProduct.setVersao(v_version_versao);
                                        break;
                                    } else {
                                        v_string_base_comparer = Utils.getDefaultBundle().getString("no");
                                    }
                                }
                            }
                        }
                    }

                    //Adiciona linha na tabela de acordo com o select
                    if (v_resultset_result.getString("S_CRIPTO").equals("N")) {
                        v_tablecompbase_pkg.addRow(new Object[]{v_Product_Produtos.get_label(), v_resultset_result.getInt("VERSAO") + "." + v_resultset_result.getInt("RELEASE") + "." + v_resultset_result.getInt("PATCH") + "." + v_resultset_result.getInt("BUILD"), v_string_base_comparer, v_resultset_result.getString("S_SCHEMA_OWNER"), v_resultset_result.getString("S_OWNER_PASS"), v_resultset_result.getString("S_TNS")});
                    } else {
                        v_tablecompbase_pkg.addRow(new Object[]{v_Product_Produtos.get_label(), v_resultset_result.getInt("VERSAO") + "." + v_resultset_result.getInt("RELEASE") + "." + v_resultset_result.getInt("PATCH") + "." + v_resultset_result.getInt("BUILD"), v_string_base_comparer, v_resultset_result.getString("S_SCHEMA_OWNER"), Utils.decrypt(v_resultset_result.getString("S_OWNER_PASS")), v_resultset_result.getString("S_TNS")});
                    }
                    _compareProduct.setProduto(v_Product_Produtos);
                    _hashProduto.put(v_Product_Produtos.get_label(), _compareProduct);

                } catch (Exception e) {
                    Utils.setDefaultCursor();
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("erroCodSistema") + " " + v_resultset_result.getInt("COD_SISTEMA"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                }
            }
            _oraconn.Close();
            Utils.setDefaultCursor();

            return true;
        }

        @Override
        protected void succeeded(
                Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCompBase = new javax.swing.JTable();
        jPanelBaseGenerica = new javax.swing.JPanel();
        jTextSenha = new javax.swing.JTextField();
        Usuario = new java.awt.Label();
        Senha = new java.awt.Label();
        TNS = new java.awt.Label();
        jTextTNS = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jTextUsuario = new javax.swing.JTextField();
        JButtonExecutaComp = new javax.swing.JButton();
        jButtonSelecionartodas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwInstaladorComparaBase.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableCompBase.setModel(new javax.swing.table.DefaultTableModel(

            new Object [][] {

            },

            new String [] {
                Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaProduto"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaVersao"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaComp"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaUsuario"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaSenha"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaTNS"), Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("colunaExec")
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true
            };
            boolean[] canNotEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                Object obj = jTableCompBase.getValueAt(rowIndex, 2);
                String _comp = String.valueOf(obj);
                if (_comp == Utils.getDefaultBundle().getString("yes")) {
                    return canEdit [columnIndex];
                }else{
                    return canNotEdit [columnIndex];
                }
            };

            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

        });
        jTableCompBase.setName("jTableCompBase"); // NOI18N
        jTableCompBase.getTableHeader().setReorderingAllowed(false);
        jTableCompBase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCompBaseMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCompBase);

        jPanelBaseGenerica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanelBaseGenerica.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanelBaseGenerica.border.titleFont"), resourceMap.getColor("jPanelBaseGenerica.border.titleColor"))); // NOI18N
        jPanelBaseGenerica.setName("jPanelBaseGenerica"); // NOI18N

        jTextSenha.setText(resourceMap.getString("jTextSenha.text")); // NOI18N
        jTextSenha.setToolTipText(resourceMap.getString("jTextSenha.toolTipText")); // NOI18N
        jTextSenha.setName("jTextSenha"); // NOI18N

        Usuario.setName("usuario"); // NOI18N
        Usuario.setText(resourceMap.getString("usuario.text")); // NOI18N

        Senha.setName("senha"); // NOI18N
        Senha.setText(resourceMap.getString("senha.text")); // NOI18N

        TNS.setName("tns"); // NOI18N
        TNS.setText(resourceMap.getString("tns.text")); // NOI18N

        jTextTNS.setToolTipText(resourceMap.getString("jTextTNS.toolTipText")); // NOI18N
        jTextTNS.setName("jTextTNS"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getActionMap(SfwInstaladorComparaBase.class, this);
        jButtonBuscar.setAction(actionMap.get("conectaBaseGenerica")); // NOI18N
        jButtonBuscar.setText(resourceMap.getString("jButtonBuscar.text")); // NOI18N
        jButtonBuscar.setName("jButtonBuscar"); // NOI18N

        jTextUsuario.setToolTipText(resourceMap.getString("jTextUsuario.toolTipText")); // NOI18N
        jTextUsuario.setName("jTextUsuario"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanelBaseGenericaLayout = new org.jdesktop.layout.GroupLayout(jPanelBaseGenerica);
        jPanelBaseGenerica.setLayout(jPanelBaseGenericaLayout);
        jPanelBaseGenericaLayout.setHorizontalGroup(
            jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBaseGenericaLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(Usuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(TNS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jTextTNS)
                    .add(jTextUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Senha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonBuscar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .add(jTextSenha, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelBaseGenericaLayout.setVerticalGroup(
            jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBaseGenericaLayout.createSequentialGroup()
                .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(Usuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Senha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonBuscar)
                    .add(jPanelBaseGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jTextTNS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(TNS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        Usuario.getAccessibleContext().setAccessibleName(resourceMap.getString("label1.AccessibleContext.accessibleName")); // NOI18N
        Senha.getAccessibleContext().setAccessibleName(resourceMap.getString("label2.AccessibleContext.accessibleName")); // NOI18N

        JButtonExecutaComp.setAction(actionMap.get("executaComparaBase")); // NOI18N
        JButtonExecutaComp.setText(resourceMap.getString("JButtonExecutaComp.text")); // NOI18N
        JButtonExecutaComp.setActionCommand(resourceMap.getString("JButtonExecutaComp.actionCommand")); // NOI18N
        JButtonExecutaComp.setName("JButtonExecutaComp"); // NOI18N

        jButtonSelecionartodas.setText(resourceMap.getString("jButtonSelecionartodas.text")); // NOI18N
        jButtonSelecionartodas.setName("jButtonSelecionartodas"); // NOI18N
        jButtonSelecionartodas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonSelecionartodasMousePressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 694, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jPanelBaseGenerica, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jButtonSelecionartodas)
                            .add(JButtonExecutaComp, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(JButtonExecutaComp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonSelecionartodas))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelBaseGenerica, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(7, 7, 7)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableCompBaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCompBaseMouseClicked
        jTableCompBase.isCellEditable(jTableCompBase.getSelectedRow(), 2);
    }//GEN-LAST:event_jTableCompBaseMouseClicked

    private void jButtonSelecionartodasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSelecionartodasMousePressed
        for (int i = 0; i < jTableCompBase.getRowCount(); i++) {
            if (jTableCompBase.getValueAt(i, 2).equals(Utils.getDefaultBundle().getString("yes"))) {
                jTableCompBase.setValueAt(true, i, 6);
            }
        }
    }//GEN-LAST:event_jButtonSelecionartodasMousePressed

    /**
     * Funcao para extrair apenas o arquivo .sql de comparaï¿½ï¿½o
     * @param zipfile PKG do produto selecionado
     * @param extractionpoint Ponto de extraÃ§Ã£o da comparaï¿½ï¿½o
     * @param caminhoComp Caminho da comparaÃ§Ã£o no XML
     * @param productfolder Nome da pasta do produto
     * @param user usuÃ¡rio do produto selecionado para conectar na base
     * @param pass senha do produto selecionado para conectar na base
     * @param tns tns do produto selecionado para conectar na base
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void Extraircomparacao(ZipFile zipfile, File extractionpoint, String caminhoComp, String productfolder, String user, String pass, String tns) throws FileNotFoundException, IOException {

        String v_s_caminhoComparaBase = productfolder + caminhoComp;

        //Verifica todos arquivos do ZIP
        for (Enumeration entries = zipfile.entries(); entries.hasMoreElements();) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            //comparação de cada arquivo zip com o arquivo de comparação
            if (zipEntry.getName().replaceAll("/", "\\\\").equals(v_s_caminhoComparaBase)) {
                String zipEntryName = zipEntry.getName().replaceAll("/", "\\\\");

                //Cria o arquivo de comparação
                _compBaseSQL = new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntryName.substring(zipEntryName.lastIndexOf(System.getProperty("file.separator")), zipEntryName.length()));

                if (!_compBaseSQL.exists()) {
                    _compBaseSQL.createNewFile();
                } else {
                    _compBaseSQL.delete();
                    _compBaseSQL.createNewFile();
                }

                FileOutputStream out = new FileOutputStream(_compBaseSQL, true);
                InputStream in = zipfile.getInputStream(zipEntry);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.close();
            }
        }
    }

    @Action
    public Task executaComparaBase() {
        return new ExecutaComparaBaseTask(org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class));
    }

    /**
     * Executa a comparaÃ§Ã£o de base
     */
    public class ExecutaComparaBaseTask extends org.jdesktop.application.Task<Object, Void> {

        ExecutaComparaBaseTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ExecutaComparaBaseTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            File v_s_caminhoSalvaComp;
            Boolean v_b_checkSelecionado = false;
            SfwCompareProduct v_hashProduto;
            OracleConnection oracleconn2 = new OracleConnection();
            Boolean executaComp = false;
            Process v_process_installexec;

            try {
                Utils.setBusyCursor();
                if (jTableCompBase.getRowCount() == 0) {
                    Utils.setDefaultCursor();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("buscaBaseGenerica"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    return null;
                } else {

                    //Percorre todas as linhas da tabela e verifica os produtos selecionados
                    for (int i = 0; i < jTableCompBase.getRowCount(); i++) {
                        v_b_checkSelecionado = (Boolean) jTableCompBase.getValueAt(i, 6);
                        if (v_b_checkSelecionado != null && v_b_checkSelecionado) {
                            if (jTableCompBase.getValueAt(i, 3) != "" && jTableCompBase.getValueAt(i, 4) != "" && jTableCompBase.getValueAt(i, 5) != "") {
                                try {
                                    oracleconn2.set_username(jTableCompBase.getValueAt(i, 3).toString());
                                    oracleconn2.set_password(jTableCompBase.getValueAt(i, 4).toString());
                                    oracleconn2.set_tns(jTableCompBase.getValueAt(i, 5).toString());
                                    oracleconn2.Connect();
                                    executaComp = true;
                                    oracleconn2.Close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    if (e.getErrorCode() == 1017) {
                                        Utils.setDefaultCursor();
                                        JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validausuariosenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                                        executaComp = false;
                                        jTableCompBase.setCellSelectionEnabled(true);
                                        jTableCompBase.changeSelection(i, 4, false, false);
                                        jTableCompBase.requestFocus();
                                        _oraconn.Close();
                                        return null;
                                    }
                                    if (e.getErrorCode() == 12154) {
                                        Utils.setDefaultCursor();
                                        JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.tnsinvalido"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                                        executaComp = false;
                                        jTableCompBase.setCellSelectionEnabled(true);
                                        jTableCompBase.changeSelection(i, 5, false, false);
                                        jTableCompBase.requestFocus();
                                        _oraconn.Close();
                                    }
                                } catch (Exception e) {
                                    Utils.setDefaultCursor();
                                    e.printStackTrace();
                                    executaComp = false;
                                    return null;
                                }
                            } else {
                                if (jTableCompBase.getValueAt(i, 3) == "") {
                                    Utils.setDefaultCursor();
                                    executaComp = false;
                                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaUsuario"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                                    jTableCompBase.setCellSelectionEnabled(true);
                                    jTableCompBase.changeSelection(i, 3, false, false);
                                    jTableCompBase.requestFocus();
                                    return null;
                                } else if (jTableCompBase.getValueAt(i, 4) == "") {
                                    Utils.setDefaultCursor();
                                    executaComp = false;
                                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaSenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                                    jTableCompBase.setCellSelectionEnabled(true);
                                    jTableCompBase.changeSelection(i, 4, false, false);
                                    jTableCompBase.requestFocus();
                                    return null;
                                } else if (jTableCompBase.getValueAt(i, 5) == "") {
                                    Utils.setDefaultCursor();
                                    executaComp = false;
                                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaSenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                                    jTableCompBase.setCellSelectionEnabled(true);
                                    jTableCompBase.changeSelection(i, 5, false, false);
                                    jTableCompBase.requestFocus();
                                    return null;
                                }
                            }
                        }
                    }
                    if (executaComp) {

                        //Abre um FileChooser para selecionar o caminho para salvar a comparaÃ§Ã£o
                        v_s_caminhoSalvaComp = new File(SfwSelecionaCaminho.escolheArquivo());
                        for (int i = 0; i < jTableCompBase.getRowCount(); i++) {
                            v_b_checkSelecionado = (Boolean) jTableCompBase.getValueAt(i, 6);
                            if (v_b_checkSelecionado != null && v_b_checkSelecionado) {
                                try {
                                    if (!v_s_caminhoSalvaComp.toString().equals("")) {
                                        new Thread(this, "Executando comparação de Base").start();
                                        JButtonExecutaComp.setEnabled(false);
                                        jButtonBuscar.setEnabled(false);
                                        v_hashProduto = (SfwCompareProduct) _hashProduto.get(jTableCompBase.getValueAt(i, 0));
                                        File v_file_extractcompare = new File("comparacao");
                                        if (!v_file_extractcompare.exists()) {
                                            v_file_extractcompare.mkdirs();
                                        }
                                        Extraircomparacao(Install.get_packagelist().getPackageByName(v_hashProduto.getPacote()).get_zip(), v_file_extractcompare, v_hashProduto.getCaminho(), v_hashProduto.getProduto().get_folder(), jTableCompBase.getValueAt(i, 3).toString(), jTableCompBase.getValueAt(i, 4).toString(), jTableCompBase.getValueAt(i, 5).toString());
                                        File arquivoOrigem = null;
                                        // Executa a comparação.
                                        Info v_info = null;
                                        try {
                                            Usuario usuario = new Usuario();
                                            usuario.set_usuario(jTableCompBase.getValueAt(i, 3).toString());
                                            usuario.set_password(jTableCompBase.getValueAt(i, 4).toString());
                                            usuario.set_tns(jTableCompBase.getValueAt(i, 5).toString());
                                            InstallCompare.set_usuario(usuario);
                                            v_info = new Info();
                                            v_info.setVERSAO(jTableCompBase.getValueAt(i, 1).toString());
                                            v_info.setCOD_PRODUTO(Integer.parseInt(v_hashProduto.getProduto().get_code()));
                                            File comparacao = new File("comparacao");
                                            if (!comparacao.exists()) {
                                                comparacao.mkdirs();
                                            }
                                            JFrame mainFrame = SfwInstaladorScriptApp.getApplication().getMainFrame();
                                            arquivoOrigem = new File("comparacao\\comparacao_" + v_info.getVERSAO() + ".xml");
                                            SfwProcessoComparacao compare = new SfwProcessoComparacao(mainFrame, true);
                                            compare.setUsuario(usuario);
                                            compare.setInfo(v_info);
                                            compare.setArquivoOrigem(_compBaseSQL.getPath());
                                            compare.setArquivoDestino(String.valueOf(arquivoOrigem));
                                            compare.setCaminhoComp(v_s_caminhoSalvaComp.getPath());
                                            compare.setTitle("Comparação de base: Produto " + jTableCompBase.getValueAt(i, 0).toString() + "Versão: " + v_info.getVERSAO());
                                            compare.setLocationRelativeTo(null);
                                            new Thread(compare, "Executando Comparação").start();
                                            compare.setVisible(true);
                                        } catch (Exception e) {
                                            Utils.setDefaultCursor();
                                            JOptionPane.showMessageDialog(null,
                                                    "Erro na comparação de base",
                                                    "Atenção",
                                                    JOptionPane.ERROR_MESSAGE);
                                            e.printStackTrace();
                                            return null;
                                        }
                                        _compBaseSQL.delete();
                                        arquivoOrigem.delete();
                                    }
                                } catch (Exception e) {
                                    Utils.setDefaultCursor();
                                    e.printStackTrace();
                                }
                            }
                        }
                        JButtonExecutaComp.setEnabled(true);
                        jButtonBuscar.setEnabled(true);
                        if (!v_s_caminhoSalvaComp.toString().equals("")) {
                            Utils.setDefaultCursor();
                            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("comparacaoOK"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        Utils.setDefaultCursor();
                        JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(SfwInstaladorComparaBase).getString("selecionarProduto"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                        return null;
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    @Action
    public Task conectaBaseGenerica() {
        return new ConectaBaseGenericaTask(org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonExecutaComp;
    private java.awt.Label Senha;
    private java.awt.Label TNS;
    private java.awt.Label Usuario;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonSelecionartodas;
    private javax.swing.JPanel jPanelBaseGenerica;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCompBase;
    private javax.swing.JTextField jTextSenha;
    private javax.swing.JTextField jTextTNS;
    private javax.swing.JTextField jTextUsuario;
    // End of variables declaration//GEN-END:variables
}
