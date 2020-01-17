package sfwinstaladorscript.atualizacmschema;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;

public class SfwInstaladorAtualizaCmSchema extends javax.swing.JDialog {

    private String vSiglaOrigem;
    private String vTNS;
    private OracleConnection con;

    public SfwInstaladorAtualizaCmSchema(java.awt.Frame parent) {
        super(parent);
        initComponents();

        con = new OracleConnection();

        jTextUsuarioSoftcomex.requestFocus();
        jPanelProdutos.setLayout(new java.awt.GridLayout(1, 1, 0, 2));
        this.desabilitaCampos();

        jPanelProdutos.removeAll();
        jPanelProdutos.repaint();
        jPanelProdutos.revalidate();

        jTextUsuarioSombra.setText("");
        jTextSenhaSombra.setText("");
        jTextRole.setText("");
    }

    @Action
    public void listaCmSchema() {
        Thread listaCmSchema = new Thread("listaCmSchema") {

            @Override
            public void run() {

                setLoader();

                desabilitaCampos();
                jPanelProdutos.removeAll();
                jPanelProdutos.repaint();
                jPanelProdutos.revalidate();

                jTextUsuarioSombra.setText("");
                jTextSenhaSombra.setText("");
                jTextRole.setText("");

                if (jTextUsuarioSoftcomex.getText().equals("")) {
                    setLoaderDisables();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaUsuario"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    jTextUsuarioSoftcomex.requestFocus();
                    return;
                }

                if (jTextSenhaSoftcomex.getText().equals("")) {
                    setLoaderDisables();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaSenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    jTextSenhaSoftcomex.requestFocus();
                    return;
                }

                if (jTextTNS.getText().equals("")) {
                    setLoaderDisables();
                    JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validaTNS"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    jTextTNS.requestFocus();
                    return;
                }

                try {
                    //variáveis.
                    ResultSet rs;
                    Integer intExiste = 0;


                    // seta dados para conexão
                    con.set_username(jTextUsuarioSoftcomex.getText());
                    con.set_password(jTextSenhaSoftcomex.getText());
                    con.set_tns(jTextTNS.getText());

                    // conecta no usuário da base genérica.
                    con.ConnectAux();

                    // verifica se a base está limpa.
                    rs = con.Query("select count(1) as existe from user_tables where table_name = 'SFW_SISTEMA_VERSAO'");

                    // grava o resultado do select
                    while (rs.next()) {
                        intExiste = rs.getInt("existe");
                    }

                    rs.close();

                    // se não existir a tabela não faz nada.
                    if (intExiste == 1) {
                        // recupera informações de usuário sombra, senha do sombra e role.
                        try {
                            rs = con.Query("select s_user, s_pass, s_role, s_cripto from sfw_cm_schema where cod_sistema = 0");
                            while (rs.next()) {
                                jTextUsuarioSombra.setText(rs.getString("s_user"));
                                if (rs.getString("s_cripto").equals("S")) {
                                    jTextSenhaSombra.setText(Utils.nvl(Utils.decrypt(rs.getString("s_pass")), ""));
                                } else {
                                    jTextSenhaSombra.setText(Utils.nvl(rs.getString("s_pass"), ""));
                                }
                                jTextRole.setText(rs.getString("s_role"));
                            }
                        } catch (Exception e) {
                            setLoaderDisables();
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, e.getMessage(), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // recupera as informações dos demais produtos.
                        GridLayout v_gridlayout_g = (GridLayout) jPanelProdutos.getLayout();
                        v_gridlayout_g.setRows(0);
                        try {
                            rs = con.Query("select scs.cod_sistema, "
                                    + "scs.s_icone_login_centralizado, "
                                    + "scs.s_schema_owner, "
                                    + "scs.s_owner_pass, "
                                    + "scs.s_tablespace_data_4k, "
                                    + "scs.s_tablespace_index_4k, "
                                    + "scs.s_cripto "
                                    + "from sfw_cm_schema scs "
                                    + "order by scs.cod_sistema");

                            while (rs.next()) {
                                SfwProdutoSchema produto_schema = new SfwProdutoSchema();
                                produto_schema.setCod_sistema(rs.getString("cod_sistema"));
                                produto_schema.setIcone(rs.getString("s_icone_login_centralizado"));
                                produto_schema.setUsuario(rs.getString("s_schema_owner"));

                                // lógico para campo criptografado ou não.
                                if (rs.getString("s_cripto").equals("S")) {
                                    produto_schema.setSenha(Utils.nvl(Utils.decrypt(rs.getString("s_owner_pass")), ""));
                                } else {
                                    produto_schema.setSenha(Utils.nvl(rs.getString("s_owner_pass"), ""));
                                }
                                produto_schema.setTable_space_data(rs.getString("s_tablespace_data_4k"));
                                produto_schema.setTable_space_index(rs.getString("s_tablespace_index_4k"));

                                // lógica para pegar a sigla de origem
                                if (produto_schema.getCod_sistema().equals("0")) {
                                    vSiglaOrigem = produto_schema.getUsuario().substring(3, produto_schema.getUsuario().length());
                                }

                                // joga os valores nos campos de cada produto.
                                produto_schema.populaCampos();

                                // adiciona cada produto no painel existente.
                                jPanelProdutos.add(produto_schema);
                                v_gridlayout_g.setRows(v_gridlayout_g.getRows() + 1);
                            }

                            // abre os campos para edições e mostra os produtos selecionados.
                            habilitaCampos();
                            jPanelProdutos.repaint();
                            jPanelProdutos.revalidate();
                            vTNS = jTextTNS.getText();

                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, e.getMessage(), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        rs.close();
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuário inválido ou a base está limpa.\nVerifique se usuário da base genérica está correto.", Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                    }

                } catch (SQLException e) {
                    setLoaderDisables();
                    e.getErrorCode();
                    e.printStackTrace();
                    if (e.getErrorCode() == 1017) {
                        JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.validausuariosenha"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                        jTextUsuarioSoftcomex.requestFocus();
                    }
                    if (e.getErrorCode() == 12154 || e.getErrorCode() == 17002) {
                        setLoaderDisables();
                        JOptionPane.showMessageDialog(null, Utils.getDefaultBundle().getString("comparaBase.tnsinvalido"), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                        jTextTNS.requestFocus();
                    }
                    con.Close();
                }
                setLoaderDisables();
            }
        };
        listaCmSchema.start();
    }

    @Action
    public void atualizaCmSchema() {

        Thread atualizaCmSchema = new Thread("atualizaCmSchema") {

            @Override
            public void run() {
                setLoader();

                Integer atualiza = JOptionPane.showConfirmDialog(null, "Deseja realmente atualizar?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (atualiza == JOptionPane.YES_OPTION) {

                    desabilitaCampos();
                    jPanelProdutos.setEnabled(false);

                    try {
                        con.ConnectAuxSemAutoCommit();

                        // update das informações comuns
                        con.Update("update sfw_cm_schema set s_user = '" + jTextUsuarioSombra.getText() + "', "
                                + "s_pass = '" + Utils.nvl(Utils.RetornaSenhaCryptoSemAspas(jTextSenhaSombra.getText()), "") + "', "
                                + "s_role = '" + jTextRole.getText() + "',"
                                + "s_sigla = '" + vSiglaOrigem + "'");

                        for (int i = 0; i < jPanelProdutos.getComponentCount(); i++) {
                            SfwProdutoSchema produto_schema = (SfwProdutoSchema) jPanelProdutos.getComponents()[i];

                            con.Update("update sfw_cm_schema set s_icone_login_centralizado = '" + Utils.nvl(produto_schema.getIcone(), "") + "', "
                                    + "s_schema_owner = '" + Utils.nvl(produto_schema.getUsuario(), "") + "', "
                                    + "s_owner_pass = '" + Utils.nvl(Utils.RetornaSenhaCryptoSemAspas(produto_schema.getSenha()), "") + "', "
                                    + "s_tablespace_data_4k = '" + Utils.nvl(produto_schema.getTable_space_data(), "") + "', "
                                    + "s_tablespace_data_64k = '" + Utils.nvl(produto_schema.getTable_space_data(), "") + "', "
                                    + "s_tablespace_data_128k = '" + Utils.nvl(produto_schema.getTable_space_data(), "") + "', "
                                    + "s_tablespace_data_512k = '" + Utils.nvl(produto_schema.getTable_space_data(), "") + "', "
                                    + "s_tablespace_data_1m = '" + Utils.nvl(produto_schema.getTable_space_data(), "") + "', "
                                    + "s_tablespace_index_4k = '" + Utils.nvl(produto_schema.getTable_space_index(), "") + "', "
                                    + "s_tablespace_index_64k = '" + Utils.nvl(produto_schema.getTable_space_index(), "") + "', "
                                    + "s_tablespace_index_128k = '" + Utils.nvl(produto_schema.getTable_space_index(), "") + "', "
                                    + "s_tablespace_index_512k = '" + Utils.nvl(produto_schema.getTable_space_index(), "") + "', "
                                    + "s_tablespace_index_1m = '" + Utils.nvl(produto_schema.getTable_space_index(), "") + "', "
                                    + "s_tns = '" + vTNS + "', "
                                    + "s_cripto = 'S'"
                                    + "where cod_sistema = " + produto_schema.getCod_sistema());
                        }

                        con.commit();

                        JOptionPane.showMessageDialog(null, "Informações atualizadas com sucesso!!!.", Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception e) {
                        habilitaCampos();
                        jPanelProdutos.setEnabled(true);
                        e.printStackTrace();
                        setLoaderDisables();
                        JOptionPane.showMessageDialog(null, e.getMessage(), Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.WARNING_MESSAGE);
                        return;
                    } finally {
                        con.Close();
                    }
                }
                habilitaCampos();
                jPanelProdutos.setEnabled(true);
                setLoaderDisables();
            }
        };
        atualizaCmSchema.start();
    }

    /**
     *
     * @return
     */
    private void setLoaderDisables() {
        jLabelLoader.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("sfwinstaladorscript/atualizacmschema/resources/loading.png")));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     *
     * @return
     */
    private void setLoader() {
        jLabelLoader.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("sfwinstaladorscript/atualizacmschema/resources/loading.gif")));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void habilitaCampos() {
        jTextUsuarioSombra.setEnabled(true);
        jTextSenhaSombra.setEnabled(true);
        jTextRole.setEnabled(true);
        jButtonAtualizar.setEnabled(true);
    }

    private void desabilitaCampos() {
        jTextUsuarioSombra.setEnabled(false);
        jTextSenhaSombra.setEnabled(false);
        jTextRole.setEnabled(false);
        jButtonAtualizar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelMsg1 = new javax.swing.JLabel();
        jLabelMsg2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelUsuarioSombra = new javax.swing.JLabel();
        jTextUsuarioSombra = new javax.swing.JTextField();
        jLabelSenhaSombra = new javax.swing.JLabel();
        jTextSenhaSombra = new javax.swing.JPasswordField();
        jLabelRole = new javax.swing.JLabel();
        jTextRole = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextUsuarioSoftcomex = new javax.swing.JTextField();
        jLabelRole1 = new javax.swing.JLabel();
        jTextTNS = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextSenhaSoftcomex = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelProdutos = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabelLoader = new javax.swing.JLabel();
        jButtonAtualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        setResizable(false);

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwInstaladorAtualizaCmSchema.class);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabelMsg1.setFont(resourceMap.getFont("jLabelMsg1.font")); // NOI18N
        jLabelMsg1.setForeground(resourceMap.getColor("jLabelMsg1.foreground")); // NOI18N
        jLabelMsg1.setText(resourceMap.getString("jLabelMsg1.text")); // NOI18N
        jLabelMsg1.setName("jLabelMsg1"); // NOI18N

        jLabelMsg2.setFont(resourceMap.getFont("jLabelMsg1.font")); // NOI18N
        jLabelMsg2.setForeground(resourceMap.getColor("jLabelMsg2.foreground")); // NOI18N
        jLabelMsg2.setText(resourceMap.getString("jLabelMsg2.text")); // NOI18N
        jLabelMsg2.setName("jLabelMsg2"); // NOI18N

        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(199, 199, 199)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabelMsg1)
                    .addComponent(jLabelMsg2))
                .addContainerGap(238, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelMsg1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelMsg2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabelUsuarioSombra.setFont(resourceMap.getFont("jLabelUsuarioSombra.font")); // NOI18N
        jLabelUsuarioSombra.setText(resourceMap.getString("jLabelUsuarioSombra.text")); // NOI18N
        jLabelUsuarioSombra.setName("jLabelUsuarioSombra"); // NOI18N

        jTextUsuarioSombra.setText(resourceMap.getString("jTextUsuarioSombra.text")); // NOI18N
        jTextUsuarioSombra.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextUsuarioSombra.setEnabled(false);
        jTextUsuarioSombra.setName("jTextUsuarioSombra"); // NOI18N

        jLabelSenhaSombra.setFont(resourceMap.getFont("jLabelSenhaSombra.font")); // NOI18N
        jLabelSenhaSombra.setText(resourceMap.getString("jLabelSenhaSombra.text")); // NOI18N
        jLabelSenhaSombra.setName("jLabelSenhaSombra"); // NOI18N

        jTextSenhaSombra.setText(resourceMap.getString("jTextSenhaSombra.text")); // NOI18N
        jTextSenhaSombra.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextSenhaSombra.setEnabled(false);
        jTextSenhaSombra.setName("jTextSenhaSombra"); // NOI18N

        jLabelRole.setFont(resourceMap.getFont("jLabelRole.font")); // NOI18N
        jLabelRole.setText(resourceMap.getString("jLabelRole.text")); // NOI18N
        jLabelRole.setName("jLabelRole"); // NOI18N

        jTextRole.setText(resourceMap.getString("jTextRole.text")); // NOI18N
        jTextRole.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextRole.setEnabled(false);
        jTextRole.setName("jTextRole"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUsuarioSombra)
                    .addComponent(jLabelSenhaSombra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextUsuarioSombra, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextSenhaSombra, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabelRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextRole, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuarioSombra)
                    .addComponent(jTextUsuarioSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRole)
                    .addComponent(jTextRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSenhaSombra)
                    .addComponent(jTextSenhaSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel4.border.titleFont"), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextUsuarioSoftcomex.setText(resourceMap.getString("jTextUsuarioSoftcomex.text")); // NOI18N
        jTextUsuarioSoftcomex.setName("jTextUsuarioSoftcomex"); // NOI18N

        jLabelRole1.setFont(resourceMap.getFont("jLabelRole1.font")); // NOI18N
        jLabelRole1.setText(resourceMap.getString("jLabelRole1.text")); // NOI18N
        jLabelRole1.setName("jLabelRole1"); // NOI18N

        jTextTNS.setName("jTextTNS"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextSenhaSoftcomex.setText(resourceMap.getString("jTextSenhaSoftcomex.text")); // NOI18N
        jTextSenhaSoftcomex.setName("jTextSenhaSoftcomex"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getActionMap(SfwInstaladorAtualizaCmSchema.class, this);
        jButton1.setAction(actionMap.get("listaCmSchema")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabelRole1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextUsuarioSoftcomex, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTNS, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextSenhaSoftcomex, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addGap(266, 266, 266))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextUsuarioSoftcomex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextSenhaSoftcomex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRole1)
                    .addComponent(jTextTNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanelProdutos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanelProdutos.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanelProdutos.border.titleFont"), resourceMap.getColor("jPanelProdutos.border.titleColor"))); // NOI18N
        jPanelProdutos.setName("jPanelProdutos"); // NOI18N
        jPanelProdutos.setLayout(null);
        jScrollPane1.setViewportView(jPanelProdutos);

        jButton2.setFont(resourceMap.getFont("jButton2.font")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(844, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabelLoader.setIcon(resourceMap.getIcon("jLabelLoader.icon")); // NOI18N
        jLabelLoader.setText(resourceMap.getString("jLabelLoader.text")); // NOI18N
        jLabelLoader.setName("jLabelLoader"); // NOI18N

        jButtonAtualizar.setAction(actionMap.get("atualizaCmSchema")); // NOI18N
        jButtonAtualizar.setIcon(resourceMap.getIcon("jButtonAtualizar.icon")); // NOI18N
        jButtonAtualizar.setText(resourceMap.getString("jButtonAtualizar.text")); // NOI18N
        jButtonAtualizar.setName("jButtonAtualizar"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(796, Short.MAX_VALUE)
                .addComponent(jButtonAtualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelLoader)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelLoader, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAtualizar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        String vSigla = JOptionPane.showInputDialog(null,
                "Digite a sigla do cliente.",
                "Alterar todos os campos.",
                JOptionPane.QUESTION_MESSAGE);

        if (vSigla != null) {

            jTextUsuarioSombra.setText(jTextUsuarioSombra.getText().replace(vSiglaOrigem, vSigla));
            jTextSenhaSombra.setText(jTextSenhaSombra.getText().replace(vSiglaOrigem, vSigla));
            jTextRole.setText(jTextRole.getText().replace(vSiglaOrigem, vSigla));

            for (int i = 0; i < this.jPanelProdutos.getComponentCount(); i++) {
                SfwProdutoSchema produto_schema = (SfwProdutoSchema) this.jPanelProdutos.getComponents()[i];
                if (!produto_schema.getUsuario().equals("")) {
                    produto_schema.setUsuario(produto_schema.getUsuario().replace(vSiglaOrigem, vSigla));
                }
                if (!produto_schema.getSenha().equals("")) {
                    produto_schema.setSenha(produto_schema.getSenha().replace(vSiglaOrigem, vSigla));
                }
                // lógico para tablespace nula
                if (produto_schema.getTable_space_data() != null && !produto_schema.getTable_space_data().equals("")) {
                    produto_schema.setTable_space_data(produto_schema.getTable_space_data().replace(vSiglaOrigem, vSigla));
                } else {
                    produto_schema.setTable_space_data("TR_" + vSigla + "_DATA");
                }
                // lógico para tablespace nula
                if (produto_schema.getTable_space_index() != null && !produto_schema.getTable_space_index().equals("")) {
                    produto_schema.setTable_space_index(produto_schema.getTable_space_index().replace(vSiglaOrigem, vSigla));
                } else {
                    produto_schema.setTable_space_index("TR_" + vSigla + "_INDEX");
                }
                produto_schema.populaCampos();
            }
            vSiglaOrigem = vSigla;
        }
}//GEN-LAST:event_jButton2MousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAtualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelLoader;
    private javax.swing.JLabel jLabelMsg1;
    private javax.swing.JLabel jLabelMsg2;
    private javax.swing.JLabel jLabelRole;
    private javax.swing.JLabel jLabelRole1;
    private javax.swing.JLabel jLabelSenhaSombra;
    private javax.swing.JLabel jLabelUsuarioSombra;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelProdutos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextRole;
    private javax.swing.JTextField jTextSenhaSoftcomex;
    private javax.swing.JTextField jTextSenhaSombra;
    private javax.swing.JTextField jTextTNS;
    private javax.swing.JTextField jTextUsuarioSoftcomex;
    private javax.swing.JTextField jTextUsuarioSombra;
    // End of variables declaration//GEN-END:variables
}
