/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwProdutoSchema.java
 *
 * Created on Feb 10, 2014, 2:04:58 PM
 */
package sfwinstaladorscript.atualizacmschema;

/**
 *
 * @author jopaulo
 */
public class SfwProdutoSchema extends javax.swing.JPanel {

    private String _cod_sistema;
    private String _icone;
    private String _usuario;
    private String _senha;
    private String _table_space_data;
    private String _table_space_index;

    public SfwProdutoSchema() {
        initComponents();
    }

    /**
     * 
     */
    public void populaCampos() {
        jTextCodSistema.setText(this._cod_sistema);
        jTextIcone.setText(this._icone);
        jTextUsuario.setText(this._usuario);
        jTextSenha.setText(this._senha);
        jTextTableSpaceData.setText(this._table_space_data);
        jTextTableSpaceIndex.setText(this._table_space_index);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelCodSistema = new javax.swing.JLabel();
        jTextCodSistema = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jTextUsuario = new javax.swing.JTextField();
        jTextSenha = new javax.swing.JPasswordField();
        jLabelSenha = new javax.swing.JLabel();
        jTextTableSpaceData = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextTableSpaceIndex = new javax.swing.JTextField();
        jTextIcone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwProdutoSchema.class);
        jLabelCodSistema.setFont(resourceMap.getFont("jLabelCodSistema.font")); // NOI18N
        jLabelCodSistema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCodSistema.setText(resourceMap.getString("jLabelCodSistema.text")); // NOI18N
        jLabelCodSistema.setName("jLabelCodSistema"); // NOI18N

        jTextCodSistema.setBackground(resourceMap.getColor("jTextCodSistema.background")); // NOI18N
        jTextCodSistema.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextCodSistema.setText(resourceMap.getString("jTextCodSistema.text")); // NOI18N
        jTextCodSistema.setEnabled(false);
        jTextCodSistema.setName("jTextCodSistema"); // NOI18N

        jLabelUsuario.setFont(resourceMap.getFont("jLabelCodSistema.font")); // NOI18N
        jLabelUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuario.setText(resourceMap.getString("jLabelUsuario.text")); // NOI18N
        jLabelUsuario.setName("jLabelUsuario"); // NOI18N

        jTextUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextUsuario.setText(resourceMap.getString("jTextUsuario.text")); // NOI18N
        jTextUsuario.setName("jTextUsuario"); // NOI18N

        jTextSenha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextSenha.setName("jTextSenha"); // NOI18N

        jLabelSenha.setFont(resourceMap.getFont("jLabelSenha.font")); // NOI18N
        jLabelSenha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSenha.setText(resourceMap.getString("jLabelSenha.text")); // NOI18N
        jLabelSenha.setName("jLabelSenha"); // NOI18N

        jTextTableSpaceData.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextTableSpaceData.setText(resourceMap.getString("jTextTableSpaceData.text")); // NOI18N
        jTextTableSpaceData.setName("jTextTableSpaceData"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextTableSpaceIndex.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextTableSpaceIndex.setName("jTextTableSpaceIndex"); // NOI18N

        jTextIcone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextIcone.setText(resourceMap.getString("jTextIcone.text")); // NOI18N
        jTextIcone.setName("jTextIcone"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextCodSistema)
                    .addComponent(jLabelCodSistema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelSenha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTableSpaceData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextTableSpaceIndex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(jTextIcone, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextTableSpaceData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextTableSpaceIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextIcone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario)
                            .addComponent(jLabelCodSistema))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextCodSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelCodSistema;
    private javax.swing.JLabel jLabelSenha;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JTextField jTextCodSistema;
    private javax.swing.JTextField jTextIcone;
    private javax.swing.JTextField jTextSenha;
    private javax.swing.JTextField jTextTableSpaceData;
    private javax.swing.JTextField jTextTableSpaceIndex;
    private javax.swing.JTextField jTextUsuario;
    // End of variables declaration//GEN-END:variables

    public String getCod_sistema() {
        return _cod_sistema;
    }

    public void setCod_sistema(String _cod_sistema) {
        this._cod_sistema = _cod_sistema;
    }

    public String getIcone() {
        return _icone;
    }

    public void setIcone(String _icone) {
        this._icone = _icone;
    }

    public String getSenha() {
        return _senha;
    }

    public void setSenha(String _senha) {
        this._senha = _senha;
    }

    public String getTable_space_data() {
        return _table_space_data;
    }

    public void setTable_space_data(String _table_space_data) {
        this._table_space_data = _table_space_data;
    }

    public String getTable_space_index() {
        return _table_space_index;
    }

    public void setTable_space_index(String _table_space_index) {
        this._table_space_index = _table_space_index;
    }

    public String getUsuario() {
        return _usuario;
    }

    public void setUsuario(String _usuario) {
        this._usuario = _usuario;
    }
}
