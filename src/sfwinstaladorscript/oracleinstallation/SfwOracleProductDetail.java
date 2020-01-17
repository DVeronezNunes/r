/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwProductDetail.java
 *
 * Created on 27/10/2008, 18:35:22
 */
package sfwinstaladorscript.oracleinstallation;

import java.awt.event.MouseEvent;
import java.sql.SQLException;
import sfwinstaladorscript.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import oracle.jdbc.OracleResultSet;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import sfwinstaladorscript.components.SfwCheckNode;
import sfwinstaladorscript.config.ExInt;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.database.SfwConnection;
import sfwinstaladorscript.objects.Package;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.Version;
import sfwinstaladorscript.objects.VersionList;
import sfwinstaladorscript.interfaces.SfwListItem;
import sfwinstaladorscript.objects.ProductCompatibility;
import sfwinstaladorscript.objects.VersionCompability;

/**
 * Representa√ß√£o de um produto selecionado para instalaÁ„o.
 */
public class SfwOracleProductDetail extends javax.swing.JPanel implements SfwListItem {

    private Product _product;
    private ArrayList _dependenciesnotok;
    public ExInt cfgExIntini;

    /** Creates new form SfwProductDetail */
    public SfwOracleProductDetail(Product p) {
        Version v_version_maxpkgversion;
        String v_string_client = "";
        Package v_package_previous;

        initComponents();

        this._product = p;
        this._dependenciesnotok = new ArrayList();

        if (SfwWizardSetup.jRadioPrimeira.isSelected()) {
            jLabelInstalada.setVisible(false);
            jLabelInstalled.setVisible(false);
        } else {
            jLabelInstalled.setText(this.getProduct().getInstalledversion());
        }

        // se for softcomex n„o coloca inicial do produto
       if (this._product.get_name().equals("SOFTCOMEX")) {
            this.jTextFieldUser.setText("TR" + Install.get_shortname().toUpperCase());
            this.jTextFieldPwd.setText("TR" + Install.get_shortname().toUpperCase());
        } else {
            this.jTextFieldUser.setText("TR" + p.get_shortname() + Install.get_shortname().toUpperCase());
            this.jTextFieldPwd.setText("TR" + p.get_shortname() + Install.get_shortname().toUpperCase());
        }

        this.jTextFieldCodSistema.setText(p.get_code());
        this.jLabelCode.setVisible(false);
        this.jTextFieldCodSistema.setVisible(false);

        this.jTextFieldUser.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));
        this.jTextFieldPwd.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));
        this.jTextFieldData4k.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));
        this.jTextFieldIndex4k.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));

        this.sfwComboBoxVersion.bind(Install.get_packagelist().getProductVersions(p.get_name()), Version.get_comparator());

        // busca maior versao do pacote escolhido
        v_version_maxpkgversion = ((VersionList) Install.get_package().get_versions().get(p.get_name())).getMaxVersion();
        if (v_version_maxpkgversion != null) {
            this.sfwComboBoxVersion.setSelectedItemInsensitive(v_version_maxpkgversion.get_description());
        } // caso pacote nao tenha versao
        // faz um loop ate achar a primeira versao nos pacotes anterior mais imediato que tiver
        else {
            v_package_previous = Install.get_packagelist().getPreviousPackage(Install.get_package());

            while (v_package_previous != null && v_version_maxpkgversion == null) {
                v_version_maxpkgversion = ((VersionList) v_package_previous.get_versions().get(p.get_name())).getMaxVersion();
                if (v_version_maxpkgversion != null) {
                    this.sfwComboBoxVersion.setSelectedItemInsensitive(v_version_maxpkgversion.get_description());
                } else {
                    v_package_previous = Install.get_packagelist().getPreviousPackage(v_package_previous);
                }
            }
        }

        if (Install.get_client() != null && !Install.get_shortname().equals("")) {
            v_string_client = Install.get_shortname();
        }

        this.jTextFieldData4k.setText("TR_" + v_string_client + "_DATA");
        this.jTextFieldIndex4k.setText("TR_" + v_string_client + "_INDEX");

        final SfwOracleProductDetail sfwOracleProductDetail = this;

        this.jButtonAlterarTodas.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String prefix = JOptionPane.showInputDialog(null,
                        Utils.getDefaultBundle(sfwOracleProductDetail).getString("JOptionPane.text.digitePrefixo"),
                        Utils.getDefaultBundle(sfwOracleProductDetail).getString("JOptionPane.text.alterarTablespaces"),
                        JOptionPane.QUESTION_MESSAGE);

                if (prefix != null) {
                    Object[] values = {Utils.getDefaultBundle(sfwOracleProductDetail).getString("values.text.sim"),
                        Utils.getDefaultBundle(sfwOracleProductDetail).getString("values.text.nao")};
                    int option = JOptionPane.showOptionDialog(null,
                            Utils.getDefaultBundle(sfwOracleProductDetail).getString("JOptionPane.text.dividirTamanho"),
                            Utils.getDefaultBundle(sfwOracleProductDetail).getString("JOptionPane.text.confirmar"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            values,
                            values[0]);
                    if (option == JOptionPane.OK_OPTION) {
                        jTextFieldData4k.setText(prefix + "_DATA_4K");
                        jTextFieldIndex4k.setText(prefix + "_INDEX_4K");
                    } else {
                        jTextFieldData4k.setText(prefix + "_DATA");
                        jTextFieldIndex4k.setText(prefix + "_INDEX");
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextFieldUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldPwd = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldData4k = new javax.swing.JTextField();
        jTextFieldIndex4k = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButtonAlterarTodas = new javax.swing.JButton();
        jTextFieldCodSistema = new javax.swing.JTextField();
        jLabelCode = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        sfwComboBoxVersion = new sfwinstaladorscript.components.SfwComboBox();
        jLabelInstalada = new javax.swing.JLabel();
        jLabelInstalled = new javax.swing.JLabel();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextPane1.setName("jTextPane1"); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(403, 288));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwOracleProductDetail.class);
        jTextFieldUser.setText(resourceMap.getString("jTextFieldUser.text")); // NOI18N
        jTextFieldUser.setName("jTextFieldUser"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldPwd.setName("jTextFieldPwd"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"), resourceMap.getColor("jPanel1.border.titleColor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(399, 179));

        jTextFieldData4k.setName("jTextFieldData4k"); // NOI18N
        jTextFieldData4k.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldData4kFocusGained(evt);
            }
        });

        jTextFieldIndex4k.setName("jTextFieldIndex4k"); // NOI18N
        jTextFieldIndex4k.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldIndex4kFocusGained(evt);
            }
        });

        jLabel8.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jButtonAlterarTodas.setFont(resourceMap.getFont("jButtonAlterarTodas.font")); // NOI18N
        jButtonAlterarTodas.setText(resourceMap.getString("jButtonAlterarTodas.text")); // NOI18N
        jButtonAlterarTodas.setName("jButtonAlterarTodas"); // NOI18N

        jTextFieldCodSistema.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCodSistema.setText(resourceMap.getString("jTextFieldCodSistema.text")); // NOI18N
        jTextFieldCodSistema.setName("jTextFieldCodSistema"); // NOI18N

        jLabelCode.setFont(resourceMap.getFont("jLabelCode.font")); // NOI18N
        jLabelCode.setText(resourceMap.getString("jLabelCode.text")); // NOI18N
        jLabelCode.setName("jLabelCode"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(22, 22, 22)
                        .add(jTextFieldData4k, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldIndex4k, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(88, 88, 88)
                        .add(jLabel8)
                        .add(148, 148, 148)
                        .add(jLabel9)))
                .addContainerGap(33, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(177, Short.MAX_VALUE)
                .add(jButtonAlterarTodas)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabelCode)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldCodSistema, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(38, 38, 38))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(41, 41, 41)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(jLabel8))
                .add(3, 3, 3)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextFieldData4k, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldIndex4k, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonAlterarTodas)
                    .add(jLabelCode)
                    .add(jTextFieldCodSistema, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        sfwComboBoxVersion.setEditable(true);
        sfwComboBoxVersion.setEnabled(false);
        sfwComboBoxVersion.setName("sfwComboBoxVersion"); // NOI18N

        jLabelInstalada.setFont(resourceMap.getFont("jLabelInstalada.font")); // NOI18N
        jLabelInstalada.setText(resourceMap.getString("jLabelInstalada.text")); // NOI18N
        jLabelInstalada.setName("jLabelInstalada"); // NOI18N

        jLabelInstalled.setFont(resourceMap.getFont("jLabelInstalled.font")); // NOI18N
        jLabelInstalled.setText(resourceMap.getString("jLabelInstalled.text")); // NOI18N
        jLabelInstalled.setName("jLabelInstalled"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sfwComboBoxVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 299, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabelInstalada)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelInstalled, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)))
                .add(93, 93, 93))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(sfwComboBoxVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelInstalada)
                    .add(jLabelInstalled, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel1)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jTextFieldPwd)
                            .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTextFieldPwd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldData4kFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldData4kFocusGained
        jTextFieldData4k.selectAll();
    }//GEN-LAST:event_jTextFieldData4kFocusGained

    private void jTextFieldIndex4kFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIndex4kFocusGained
        jTextFieldIndex4k.selectAll();
    }//GEN-LAST:event_jTextFieldIndex4kFocusGained

    /**
     * Realiza valida√ß√µes de consist√™ncia de preenchimento do produto.
     * @return TRUE - se as informa√ß√µes do produto est√£o consistentes e FALSE caso contr√°rio.
     */
    public boolean Validate() {
        Version v_version_selectedversion;

        if (this.jTextFieldUser.getText().equals("")) {
            this.jTextFieldUser.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationuser") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (this.jTextFieldPwd.getText().equals("")) {
            this.jTextFieldPwd.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationpwd") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (this.jTextFieldData4k.getText().equals("")) {
            this.jTextFieldData4k.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationtablespace") + " tablespace data 4k " + Utils.getDefaultBundle(this).getString("validationtablespace2") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }


        if (this.jTextFieldIndex4k.getText().equals("")) {
            this.jTextFieldIndex4k.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationtablespace") + " tablespace index  " + Utils.getDefaultBundle(this).getString("validationtablespace2") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }


        if (this.jTextFieldCodSistema.getText().equals("")) {
            this.jTextFieldCodSistema.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationcode") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        v_version_selectedversion = (Version) this.sfwComboBoxVersion.getBindedSelectedItem();
        if (v_version_selectedversion == null) {
            this.sfwComboBoxVersion.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationversion") + " " + Utils.getDefaultBundle(this).getString("validationtablespace2") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        this.getProduct().setSelectedversion(((Version) sfwComboBoxVersion.getBindedSelectedItem()).get_description());

        return true;
    }

    /**
     * Rertorna senha de conex√£o do produto.
     * @return String com a senha.
     */
    public String getPassword() {
        return jTextFieldPwd.getText();
    }

    /**
     * Altera senha de conex√£o do produto.
     * @param password String com a senha.
     */
    public void setPassword(String password) {
        this.jTextFieldPwd.setText(password);
    }

    /**
     * Retorna usu√°rio da base do produto.
     * @return String com o usu√°rio.
     */
    public String getUser() {
        return jTextFieldUser.getText();
    }

    /**
     * Altera usu√°rio da base do produto.
     * @param user String com o usu√°rio.
     */
    public void setUser(String user) {
        this.jTextFieldUser.setText(user);
    }

    /**
     * Retorna objeto produto.
     * @return Produto.
     */
    public Product getProduct() {
        return this._product;
    }

    /**
     * Retorna tablespace data 4k digitada.
     * @return String da tablespace data 4k.
     */
    public String getData4k() {
        return jTextFieldData4k.getText();
    }

    /**
     * Retorna tablespace index 4k digitada.
     * @return String da tablespace index 4k .
     */
    public String getIndex4k() {
        return jTextFieldIndex4k.getText();
    }

    /**
     * Retorna string com o c√≥digo do sistema digitado.
     * @return String do c√≥digo do sistema.
     */
    public String getCodSistema() {
        return jTextFieldCodSistema.getText();
    }

    /**
     * 
     * @param codsistema 
     */
    public void setCodSistema(String codsistema) {
        this.jTextFieldCodSistema.setText(codsistema);
    }

    /**
     * 
     * @param data4k 
     */
    public void setData4k(String data4k) {
        this.jTextFieldData4k.setText(data4k);
    }

    /**
     * 
     * @param index4k 
     */
    public void setIndex4k(String index4k) {
        this.jTextFieldIndex4k.setText(index4k);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAlterarTodas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCode;
    private javax.swing.JLabel jLabelInstalada;
    private javax.swing.JLabel jLabelInstalled;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldCodSistema;
    private javax.swing.JTextField jTextFieldData4k;
    private javax.swing.JTextField jTextFieldIndex4k;
    private javax.swing.JTextField jTextFieldPwd;
    private javax.swing.JTextField jTextFieldUser;
    private javax.swing.JTextPane jTextPane1;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxVersion;
    // End of variables declaration//GEN-END:variables

    /**
     * DescriÁ„o para o SfwList.
     * @return  Descri√ß√£o para o SfwList
     */
    public Object get_description() {
        return this._product.get_label();
    }

    /**
     * Valida se a vers„o selecionada para o produto pode ser instalada.
     * @return String com mensagem de erro se houver.
     * @throws java.sql.SQLException
     */
    public String validateVersion() throws SQLException {
        String v_string_retorno = "";
        ResultSet v_resultset_result;
        ResultSet v_resultset_custom;
        ResultSet v_resultset_verifybuild;
        ResultSet v_resultset_verifycustom;
        String v_string_tipoversao = "";
        OracleConnection v_oracleconnection_conn;
        Version v_version_selectedversion;
        int v_int_baseversion = 0, v_int_baserelease = 0, v_int_basepatch = 0, v_int_basecustom = 0, v_int_basebuild = 0;
        ArrayList v_arraylist_pacotes;
        boolean v_boolean_no_version = true;

        v_oracleconnection_conn = new OracleConnection();
        Product.connectToSoftcomex(this.getUser(), this.getPassword(), v_oracleconnection_conn);

        v_arraylist_pacotes = new ArrayList();
        v_version_selectedversion = (Version) this.sfwComboBoxVersion.getBindedSelectedItem();

        // se tiver versao na SFW_SISTEMA_VERSAO procura pacotes de diferenÁa
        v_oracleconnection_conn.Connect();

        try {
            v_resultset_result = v_oracleconnection_conn.Query("select VERSAO, RELEASE, PATCH from SFW_SISTEMA_VERSAO where COD_SISTEMA = " + this.getCodSistema() + " and VALIDO = 'S' and ROWNUM = 1");
            if (this.getCodSistema().equals("500")) {
                v_resultset_result = v_oracleconnection_conn.Query("select VERSAO, RELEASE, PATCH from SFW_SISTEMA_VERSAO where COD_SISTEMA IN (1000, 500) and VALIDO = 'S' and ROWNUM = 1");
            }
            while (v_resultset_result.next()) {
                v_boolean_no_version = false;

                if (SfwWizardSetup.jRadioPrimeira.isSelected()) {
                    v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgalreadyinstalledfull"), v_version_selectedversion.getVersionNumber(), v_version_selectedversion.getReleaseNumber(), v_version_selectedversion.getPatchNumber());
                    break;
                }

                v_int_baseversion = v_resultset_result.getInt("VERSAO");
                v_int_baserelease = v_resultset_result.getInt("RELEASE");
                v_int_basepatch = v_resultset_result.getInt("PATCH");

                try {
                    v_resultset_verifycustom = v_oracleconnection_conn.Query("SELECT CUSTOM FROM SFW_SISTEMA_VERSAO A WHERE  A.COD_SISTEMA = " + this.getCodSistema() + " AND A.VALIDO = 'S' AND ROWNUM = 1");
                    while (v_resultset_verifycustom.next()) {
                        v_int_basecustom = v_resultset_verifycustom.getInt("CUSTOM");
                    }
                    v_resultset_verifycustom.close();
                    /*if (v_int_basecustom == 0 && v_version_selectedversion.getCustomNumber() != 0) {
                    v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgnocustom"), v_int_baseversion, v_int_baserelease, v_int_basepatch, 02, 20, 07);
                    return v_string_retorno;
                    } else if (v_int_basecustom != 0 && v_version_selectedversion.getCustomNumber() == 0) {
                    v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgnocustom"), v_int_baseversion, v_int_baserelease, v_int_basepatch, 02, 20, 07);
                    return v_string_retorno;
                    }*/
                } catch (Exception e) {
                }

                //
                //
                //
                try {
                    v_resultset_verifybuild = v_oracleconnection_conn.Query("SELECT BUILD, TIPO_VERSAO FROM SFW_SISTEMA_VERSAO A WHERE A.COD_SISTEMA = " + this.getCodSistema() + " AND A.VALIDO = 'S' AND ROWNUM = 1");
                    if (this.getCodSistema().equals("500")) {
                        v_resultset_verifybuild = v_oracleconnection_conn.Query("SELECT BUILD, TIPO_VERSAO FROM SFW_SISTEMA_VERSAO A WHERE A.COD_SISTEMA in (500, 1000) AND A.VALIDO = 'S' AND ROWNUM = 1");
                    }
                    while (v_resultset_verifybuild.next()) {
                        v_int_basebuild = v_resultset_verifybuild.getInt("BUILD");
                        v_string_tipoversao = v_resultset_verifybuild.getString("TIPO_VERSAO");
                    }
                    v_resultset_verifybuild.close();
                    if (v_string_tipoversao == null) {
                        v_string_tipoversao = "";
                    }
                } catch (Exception e) {
                }


                String v_string_version = String.valueOf(v_int_baseversion);

                //
                // Converte a vers„o V2011 para V11 ou V2010 para V10
                //

                if (v_string_version.length() >= 4 && !v_string_version.contains("20")) {
                    v_int_baseversion = Integer.parseInt(v_string_version.substring(1));
                }

                /*if (v_int_baseversion >= 20) {
                String v_string_version_2011 = String.valueOf(v_int_baseversion);
                v_int_baseversion = Integer.parseInt(v_string_version_2011.substring(0, 1));
                }*/

                //
                //
                //
                if (this.isVersionEqual(v_version_selectedversion, v_int_baseversion, v_int_baserelease, v_int_basepatch, v_int_basebuild)) {
                    v_string_retorno = "EQUAL";
                    return v_string_retorno;
                } else {
                    this.findPackages(v_version_selectedversion, v_int_baseversion, v_int_baserelease, v_int_basepatch, v_int_basecustom, v_oracleconnection_conn);
                }


                if (this.isVersionGreater(v_version_selectedversion, v_int_baseversion, v_int_baserelease, v_int_basepatch, v_int_basebuild)) {
                    v_arraylist_pacotes = this.findPackages(v_version_selectedversion, v_int_baseversion, v_int_baserelease, v_int_basepatch, v_int_basebuild, v_oracleconnection_conn);
                    //
                    if(v_arraylist_pacotes.size() == 0){
                        while((v_arraylist_pacotes.size() == 0) && (v_int_basepatch != 0)){
                              if(v_int_basepatch != 0){
                                 v_int_basepatch--; 
                              }
                              //
                              v_arraylist_pacotes = this.findPackages(v_version_selectedversion, v_int_baseversion, v_int_baserelease, v_int_basepatch, v_int_basebuild, v_oracleconnection_conn);
                        }
                    }
                } else {
                    v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgversiongreater"), v_int_baseversion, v_int_baserelease, v_int_basepatch, v_version_selectedversion.getVersionNumber(), v_version_selectedversion.getReleaseNumber(), v_version_selectedversion.getPatchNumber());
                }

                break;
            }

            v_resultset_result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        v_oracleconnection_conn.Close();

        //se nao achou versao na SFW_SISTEMA_VERSAO e selecionou uma instalaÁ„o completa, procura pacote full
        if (v_boolean_no_version && SfwWizardSetup.jRadioPrimeira.isSelected() && v_string_retorno.equals("")) {
            v_arraylist_pacotes = this.findPackages(v_version_selectedversion, v_oracleconnection_conn);

            // - se > 0  √© possivel instalar
            if (v_arraylist_pacotes.size() > 0) {
                Install.get_productsdetailinstall().add(this);
                Install.get_productpackageinstall().put(this._product.get_name(), v_arraylist_pacotes);
            } // - caso contrario atribuir erro
            else {
                v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgnopackage1"), v_version_selectedversion.getVersionNumber(), v_version_selectedversion.getReleaseNumber(), v_version_selectedversion.getPatchNumber());
            }

            return v_string_retorno;
        }

        if (v_string_retorno.equals("")) {
            // - se > 0 n„o possivel instalar
            if (v_arraylist_pacotes.size() > 0) {
                Install.get_productsdetailinstall().add(this);
                Install.get_productpackageinstall().put(this._product.get_name(), v_arraylist_pacotes);
            } // - caso contrario atribuir erro
            else {
                v_string_retorno = String.format(Utils.getDefaultBundle(this).getString("msgnopackage5"),
                        v_int_baseversion,
                        v_int_baserelease,
                        v_int_basepatch,
                        v_string_tipoversao,
                        v_int_basebuild,
                        v_version_selectedversion.getVersionNumber(),
                        v_version_selectedversion.getReleaseNumber(),
                        v_version_selectedversion.getPatchNumber(),
                        v_version_selectedversion.getVersionType(),
                        v_version_selectedversion.getBuildNumber());
            }
        }
        return v_string_retorno;
    }

    /**
     * Verifica se a vers„o selecionada È maior que a vers„o base.
     * @param selectedversion vers„o selecionada.
     * @param vbbase Parte vers„o da vers„o instalada.
     * @param vbrelease Parte release da vers„o instalada.
     * @param vbpatch Parte patch da vers„o instalada.
     * @return TRUE - se for maior, FALSE - se for menor ou igual
     */
    private boolean isVersionGreater(Version selectedversion, int vbbase, int vbrelease, int vbpatch, int vbbuild) {
        if (vbbase > selectedversion.getVersionNumber()) {
            return false;
        } else if (vbbase == selectedversion.getVersionNumber()) {
            if (vbrelease > selectedversion.getReleaseNumber()) {
                return false;
            } else if (vbrelease == selectedversion.getReleaseNumber()) {
                // Se o patch (da base) foi maior que a vers„o sendo instalada,
                // e o patch da versao sendo instalada for 0 (zero)
                // mas o build da vers„o sendo instalada for maior que 0 (zero)
                // Ent„o libera a atualizaÁ„o, porque o build da vers„o sendo instalada È maior que 0 (zero) e significa que teve atualizaÁ„o na vers„o base depois do patch
                if (vbpatch > selectedversion.getPatchNumber() && selectedversion.getPatchNumber() == 0 && selectedversion.getBuildNumber() > 0) {
                    return true;
                } else if (vbpatch > selectedversion.getPatchNumber()) {
                    return false;
                } else if (vbpatch == selectedversion.getPatchNumber()) {
                    if (vbbuild > selectedversion.getBuildNumber()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param selectedversion
     * @param vbbase
     * @param vbrelease
     * @param vbpatch
     * @param vbcustom
     * @return
     */
    private boolean isVersionGreater(Version selectedversion, int vbbase, int vbrelease, int vbpatch, int vbbuild, int vbcustom) {
        if (vbbase > selectedversion.getVersionNumber()) {
            return false;
        } else if (vbbase == selectedversion.getVersionNumber()) {
            if (vbrelease > selectedversion.getReleaseNumber()) {
                return false;
            } else if (vbrelease == selectedversion.getReleaseNumber()) {
                if (vbpatch > selectedversion.getPatchNumber()) {
                    return false;
                } else if (vbpatch == selectedversion.getPatchNumber()) {
                    if (vbbuild > selectedversion.getBuildNumber()) {
                        return false;
                    } else if (vbbuild == selectedversion.getBuildNumber()) {
                        if (vbcustom > selectedversion.getCustomNumber()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Verifica se vers√£o selecionada √© igual que vers√£o base.
     * @param selectedversion Vers√£o selecionada.
     * @param vbbase Parte vers√£o da vers√£o instalada.
     * @param vbrelease Parte release da vers√£o instalada.
     * @param vbpatch Parte patch da vers√£o instalada.
     * @return TRUE - se for igual, FALSE - se n√£o for igual
     */
    private boolean isVersionEqual(Version selectedversion, int vbbase, int vbrelease, int vbpatch, int vbbuild) {
        if (vbbase == selectedversion.getVersionNumber() && vbrelease == selectedversion.getReleaseNumber() && vbpatch == selectedversion.getPatchNumber() && vbbuild == selectedversion.getBuildNumber()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Rotira recursiva para encontrar pacotes necess√°rios para a instala√ß√£o de uma vers√£o.
     * @param selectedversion Versao selecionada para qual se deseja ir.
     * @param vbbase Parte vers„o da vers„o instalada.
     * @param vbrelease Parte release da vers„o instalada.
     * @param vbpatch Parte patch da vers„o instalada.
     * @return Lista com pacotes necess·rios.
     */
    private ArrayList findPackages(Version selectedversion, int vbbase, int vbrelease, int vbpatch, int vbbuild, SfwConnection conn) {
        Version v_version_versiondiff;
        productinfo.Diff v_diff_versiondiff, v_diff_versiondiff_ret;
        ArrayList v_arraylist_retorno,
                v_arraylist_pacotes, v_arraylist_menor_pacotes;
        VersionList v_versionlist_productlist;

        v_arraylist_retorno =
                new ArrayList();
        int v_int_menor_caminho = 999999999;

        // - verifica se tem o diff para a versao existente na base
        v_diff_versiondiff =
                selectedversion.getDiff(vbbase, vbrelease, vbpatch, vbbuild, conn);
        if (v_diff_versiondiff != null) {
            // adiciona pacote no retorno
            v_arraylist_retorno.add(v_diff_versiondiff);
        } // - caso contrario continua procurando nos diffs dos diffs
        else {
            v_diff_versiondiff_ret = null;
            v_arraylist_menor_pacotes =
                    new ArrayList();
            v_versionlist_productlist =
                    new VersionList(this.sfwComboBoxVersion.get_list());

            for (int i = 0; i
                    < selectedversion.getDiffs().length; i++) {
                // procura se os diffs tem versao selecionavel que tenha diffs
                v_version_versiondiff = v_versionlist_productlist.getVersionByNumber(selectedversion.getDiffs()[i].getV(), selectedversion.getDiffs()[i].getR(), selectedversion.getDiffs()[i].getP(), selectedversion.getDiffs()[i].getB());
                if (v_version_versiondiff != null) {
                    // continuando procurando os pacotes necess√°rios
                    v_arraylist_pacotes = this.findPackages(v_version_versiondiff, vbbase, vbrelease, vbpatch, vbbuild, conn);

                    // se achou caminho at√© a vers√£o a ser atualizada > 0
                    if (v_arraylist_pacotes.size() > 0) {
                        // se achou um caminho menor que o anterior
                        if (v_int_menor_caminho > v_arraylist_pacotes.size()) {
                            v_diff_versiondiff_ret = selectedversion.getDiffs()[i];
                            v_arraylist_menor_pacotes =
                                    v_arraylist_pacotes;
                        }

                    }
                }
            }

            // adiciona pacotes no retorno
            if (v_diff_versiondiff_ret != null) {
                v_arraylist_retorno.add(v_diff_versiondiff_ret);
                v_arraylist_retorno.addAll(v_arraylist_menor_pacotes);
            }

        }

        return v_arraylist_retorno;
    }

    /**
     *
     * @param selectedversion vers„o selecionada para qual se deseja ir.
     * @param vbbase Parte vers„o da vers„o instalada.
     * @param vbrelease Parte release da vers„o instalada.
     * @param vbpatch Parte patch da vers„o instalada.
     * @param vbcustom Parte custom da vers„o instalada.
     * @return Lista de Pacotes necess·rios.
     */
    private ArrayList findPackages(Version selectedversion, int vbbase, int vbrelease, int vbpatch, int vbbuild, int vbcustom, SfwConnection conn) {
        int v_int_xmlcustom = 0;
        Version v_version_versiondiff;
        productinfo.Diff v_diff_versiondiff, v_diff_versiondiff_ret;
        ArrayList v_arraylist_retorno,
                v_arraylist_pacotes, v_arraylist_menor_pacotes;
        VersionList v_versionlist_productlist;

        v_arraylist_retorno =
                new ArrayList();
        int v_int_menor_caminho = 999999999;

        // - verifica se tem o diff para a versao existente na base
        v_diff_versiondiff =
                selectedversion.getDiff(vbbase, vbrelease, vbpatch, vbbuild, vbcustom, conn);
        if (v_diff_versiondiff != null) {
            // adiciona pacote no retorno
            v_arraylist_retorno.add(v_diff_versiondiff);
        } // - caso contrario continua procurando nos diffs dos diffs
        else {
            v_diff_versiondiff_ret = null;
            v_arraylist_menor_pacotes =
                    new ArrayList();
            v_versionlist_productlist =
                    new VersionList(this.sfwComboBoxVersion.get_list());

            for (int i = 0; i
                    < selectedversion.getDiffs().length; i++) {
                try {
                    v_int_xmlcustom = selectedversion.getDiffs()[i].getC();
                } catch (Exception e) {
                }
                // procura se os diffs tem versao selecionavel que tenha diffs
                v_version_versiondiff = v_versionlist_productlist.getVersionByNumber(selectedversion.getDiffs()[i].getV(), selectedversion.getDiffs()[i].getR(), selectedversion.getDiffs()[i].getP(), v_int_xmlcustom);
                if (v_version_versiondiff != null) {
                    // continuando procurando os pacotes necess√°rios
                    v_arraylist_pacotes = this.findPackages(v_version_versiondiff, vbbase, vbrelease, vbpatch, vbbuild, vbcustom, conn);

                    // se achou caminho at√© a vers√£o a ser atualizada > 0
                    if (v_arraylist_pacotes.size() > 0) {
                        // se achou um caminho menor que o anterior
                        if (v_int_menor_caminho > v_arraylist_pacotes.size()) {
                            v_diff_versiondiff_ret = selectedversion.getDiffs()[i];
                            v_arraylist_menor_pacotes =
                                    v_arraylist_pacotes;
                        }

                    }
                }
            }

            // adiciona pacotes no retorno
            if (v_diff_versiondiff_ret != null) {
                v_arraylist_retorno.add(v_diff_versiondiff_ret);
                v_arraylist_retorno.addAll(v_arraylist_menor_pacotes);
            }

        }

        return v_arraylist_retorno;
    }

    /**
     * Rotira recursiva para encontrar pacotes necess·rios para a instalaÁ„o de uma vers„o completa.
     * @param selectedversion Versao selecionada para instalaÁ„o completa.
     * @return Lista com pacotes necess·rios.
     */
    private ArrayList findPackages(Version selectedversion, SfwConnection conn) {
        Version v_version_versiondiff;
        productinfo.Diff v_diff_versiondiff_ret;
        productinfo.Full v_full_versionfull;
        ArrayList v_arraylist_retorno,
                v_arraylist_pacotes, v_arraylist_menor_pacotes;
        VersionList v_versionlist_productlist;

        v_arraylist_retorno =
                new ArrayList();
        int v_int_menor_caminho = 999999999;

        // - verifica se tem o diff para a versao existente na base
        v_full_versionfull =
                selectedversion.getFull(conn);
        if (v_full_versionfull != null) {
            // adiciona pacote no retorno
            v_arraylist_retorno.add(v_full_versionfull);
        } // - caso contrario continua procurando nos diffs dos diffs
        else {
            v_diff_versiondiff_ret = null;
            v_arraylist_menor_pacotes =
                    new ArrayList();
            v_versionlist_productlist =
                    new VersionList(this.sfwComboBoxVersion.get_list());

            for (int i = 0; i
                    < selectedversion.getDiffs().length; i++) {
                // procura se os diffs tem versao selecionavel que tenha diffs
                if (selectedversion.getDiffs()[i].getC() != 0) {
                    v_version_versiondiff = v_versionlist_productlist.getVersionByNumber(selectedversion.getDiffs()[i].getV(), selectedversion.getDiffs()[i].getR(), selectedversion.getDiffs()[i].getP(), selectedversion.getDiffs()[i].getB(), selectedversion.getDiffs()[i].getC());
                } else {
                    v_version_versiondiff = v_versionlist_productlist.getVersionByNumber(selectedversion.getDiffs()[i].getV(), selectedversion.getDiffs()[i].getR(), selectedversion.getDiffs()[i].getP(), selectedversion.getDiffs()[i].getB());
                }
                if (v_version_versiondiff != null) {
                    // continuando procurando os pacotes necess√°rios
                    v_arraylist_pacotes = this.findPackages(v_version_versiondiff, conn);

                    // se achou caminho ent„o a vers„o a ser atualizada > 0
                    if (v_arraylist_pacotes.size() > 0) {
                        // se achou um caminho menor que o anterior
                        if (v_int_menor_caminho > v_arraylist_pacotes.size()) {
                            v_diff_versiondiff_ret = selectedversion.getDiffs()[i];
                            v_arraylist_menor_pacotes =
                                    v_arraylist_pacotes;
                        }

                    }
                }
            }

            if (v_diff_versiondiff_ret != null) {
                // adiciona pacotes no retorno
                v_arraylist_retorno.add(v_diff_versiondiff_ret);
                v_arraylist_retorno.addAll(v_arraylist_menor_pacotes);
            }

        }

        return v_arraylist_retorno;
    }

    /**
     * Comparador para a classe SfwOracleProductDetail.
     */
    public static class SfwOracleProductDetailComparator
            implements Comparator {

        public int compare(Object element1,
                Object element2) {
            SfwOracleProductDetail v_sfworacleproductdetail_v1 = (SfwOracleProductDetail) element1;
            SfwOracleProductDetail v_sfworacleproductdetail_v2 = (SfwOracleProductDetail) element2;

            return v_sfworacleproductdetail_v1.compare(v_sfworacleproductdetail_v2);
        }
    }

    /**
     * Compara os produtos.
     * @param pd
     * @return
     */
    public int compare(SfwOracleProductDetail pd) {
        return this._product.compare(pd.getProduct());
    }

    /**
     * Verifica se o produto tem dependencia mal resolvidas com outros produtos.
     * @param checkedproducts Lista de checkbox de produtos.
     * @return Lista de produtos que precisariam estar checkados.
     */
    public ArrayList checkDependency(ArrayList checkedproducts) {
        Product v_product_current;
        SfwCheckNode v_sfwchecknode_current;
        Iterator v_iterator_check, v_iterator_dependency;
        boolean dependency_ok;

        this._dependenciesnotok = new ArrayList();

        v_iterator_dependency = this._product.get_dependencies().values().iterator();

        while (v_iterator_dependency.hasNext()) {
            v_product_current = (Product) v_iterator_dependency.next();

            dependency_ok = false;
            v_iterator_check = checkedproducts.iterator();

            while (v_iterator_check.hasNext()) {
                v_sfwchecknode_current = (SfwCheckNode) v_iterator_check.next();

                if (v_sfwchecknode_current.toString().equals(v_product_current.get_label()) && v_sfwchecknode_current.isValue()) {
                    dependency_ok = true;
                }
            }

            if (!dependency_ok) {
                this._dependenciesnotok.add(v_product_current);
            }
        }

        return this._dependenciesnotok;
    }

    /**
     * Compara a vers„o selecionada ou a vers„o da base de dados com o XML
     * @param jTabbedPane1 Todos produtos selecionados.
     * @return 
     */
    public List<ProductCompatibility> checkCompatibility(JTabbedPane jTabbedPane1) throws Exception {
        HashMap<String, VersionCompability> v_vc_compability = this.getCompability();
        List<ProductCompatibility> v_list_product = null;
        VersionCompability v_currentversion;
        String v_string_codSistema = "";
        if (v_vc_compability != null) {
            ProductCompatibility v_pc_product = null;
            v_list_product = new ArrayList<ProductCompatibility>();
            for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
                if (!((SfwOracleProductDetail) jTabbedPane1.getComponentAt(i)).getProduct().get_code().equals(this._product.get_code())) {
                    // Versıes dos Produtos selecionados
                    Version v_selectedversion = (Version) ((SfwOracleProductDetail) jTabbedPane1.getComponentAt(i)).sfwComboBoxVersion.getBindedSelectedItem();
                    // Versıes do prÛprio Produto
                    v_currentversion = v_vc_compability.get(((SfwOracleProductDetail) jTabbedPane1.getComponentAt(i)).getProduct().get_code());
                    // Verifica se a vers„o selecionada È diferente da vers„o do XML
                    if (v_selectedversion.getVersionNumber() != v_currentversion.getVersion() || v_selectedversion.getReleaseNumber() != v_currentversion.getRelease() || v_selectedversion.getPatchNumber() != v_currentversion.getPatch()) {
                        v_pc_product = new ProductCompatibility();
                        v_pc_product.setProduct(((SfwOracleProductDetail) jTabbedPane1.getComponentAt(i)).getProduct());
                        v_pc_product.setVersion(v_selectedversion.getVersionNumber() + "." + v_selectedversion.getReleaseNumber() + "." + v_selectedversion.getPatchNumber());
                        v_pc_product.setVersioncompability(v_currentversion.getVersion() + "." + v_currentversion.getRelease() + "." + v_currentversion.getPatch());
                        v_list_product.add(v_pc_product);
                    }
                }
            }

            if (!SfwWizardSetup.jRadioPrimeira.isSelected()) {
                // Trata o cÛdigo sistema dos produtos selecionados
                for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
                    v_string_codSistema = v_string_codSistema + ((SfwOracleProductDetail) jTabbedPane1.getComponentAt(i)).getProduct().get_code() + ",";
                }
                // Tira a virgula que sobrou
                v_string_codSistema = v_string_codSistema.substring(0, v_string_codSistema.length() - 1);
                //
                try {
                    // Estabelece a conex„o com o banco de dados
                    OracleConnection con = new OracleConnection();
                    con.set_username(SfwWizardSetup.jTextUsuario.getText());
                    con.set_password(SfwWizardSetup.jTextSenha.getText());
                    con.set_tns(SfwWizardSetup.jTextTNS.getText());
                    con.ConnectAux();
                    OracleResultSet rs = (OracleResultSet) con.Query("SELECT A.COD_SISTEMA, "
                            + "DECODE(B.NOME, "
                            + "'Login Generico', "
                            + "'Base GenÈrica', "
                            + "'C‚mbio Sys ImportaÁ„o', "
                            + "'C‚mbioImpSys', "
                            + "'C‚mbio Sys ExportaÁ„o', "
                            + "'C‚mbioExpSys', "
                            + "B.NOME) AS SISTEMA, "
                            + "A.VERSAO, "
                            + "A.RELEASE, "
                            + "A.PATCH "
                            + "FROM SFW_SISTEMA_VERSAO A, SFW_SISTEMA B "
                            + "WHERE A.VALIDO = 'S' "
                            + "AND A.COD_SISTEMA NOT IN (" + v_string_codSistema + ") "
                            + "AND A.COD_SISTEMA = B.COD_SISTEMA");
                    while (rs.next()) {
                        try {
                            v_currentversion = v_vc_compability.get(rs.getString(1));
                            int v_versao = rs.getInt(3);
                            int v_release = rs.getInt(4);
                            int v_patch = rs.getInt(5);
                            if (v_versao != v_currentversion.getVersion() || v_release != v_currentversion.getRelease() || v_patch != v_currentversion.getPatch()) {
                                v_pc_product = new ProductCompatibility();
                                v_pc_product.setProduct(this.ReturnProductByCod(rs.getString(1)));
                                v_pc_product.setVersion(v_versao + "." + v_release + "." + v_patch);
                                v_pc_product.setVersioncompability(v_currentversion.getVersion() + "." + v_currentversion.getRelease() + "." + v_currentversion.getPatch());
                                v_list_product.add(v_pc_product);
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return v_list_product;
    }

    /**
     * Extrai e pega as informaÁıes necess·rios 
     * do XML para comparaÁ„o das versıes na instalaÁ„o
     * @return true ou false
     * @throws Exception 
     */
    private HashMap<String, VersionCompability> getCompability() {
        HashMap<String, VersionCompability> v_list_compability;
        boolean v_boolean_achou = false;
        String v_string_xmlpatch = "";
        List v_arraylist_pkg_product = Install.get_packagelist().getProductVersions(this._product.get_name());
        Iterator v_iterator_percorrerVersao = v_arraylist_pkg_product.iterator();
        Version v_version_selected = (Version) this.sfwComboBoxVersion.getBindedSelectedItem();

        try {
            // Busca do XML de compatibilidade de acordo com a vers„o selecionada.
            while (v_iterator_percorrerVersao.hasNext()) {
                Version v_version_versao = (Version) v_iterator_percorrerVersao.next();
                if (v_version_versao.getVersionNumber() == v_version_selected.getVersionNumber() && v_version_versao.getReleaseNumber() == v_version_selected.getReleaseNumber() && v_version_versao.getPatchNumber() == v_version_selected.getPatchNumber()) {
                    // Procura no pacote DIFF
                    for (int i = 0; i < v_version_versao.get_xmlversion().getDiffArray().length; i++) {
                        if (v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray().length == 1) {
                            v_string_xmlpatch = v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray()[0].getPath();
                            v_boolean_achou = true;
                            ExtractCompabilityXML(Install.get_packagelist().getPackageByName(v_version_versao.get_packagename()).get_zip(), new File("temp"), v_string_xmlpatch, this._product.get_folder());
                            break;
                        }
                    }

                    // Procura no pacote FULL
                    if (!v_boolean_achou) {
                        for (int i = 0; i < v_version_versao.get_xmlversion().getFullArray().length; i++) {
                            if (v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray().length == 1) {
                                v_string_xmlpatch = v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getCompversionArray()[0].getPath();
                                break;
                            }
                        }
                    }
                }
            }

            // Leitura do XML para verificar a compatibilidade entre os produtos.
            v_list_compability = new HashMap<String, VersionCompability>();
            SAXBuilder builder = new SAXBuilder();
            File v_file_xml = new File("temp\\compatibilidade.xml");
            Document document = builder.build(v_file_xml);
            Element root = document.getRootElement();
            List v_list_dependencys = root.getChildren("dependency");
            for (int j = 0; j < v_list_dependencys.size(); j++) {
                VersionCompability v_vc_compability = new VersionCompability();
                Element v_element_dependecy = (Element) v_list_dependencys.get(j);
                v_vc_compability.setCod_sistema(v_element_dependecy.getAttributeValue("id"));
                v_vc_compability.setVersion(Integer.parseInt(v_element_dependecy.getAttributeValue("v")));
                v_vc_compability.setRelease(Integer.parseInt(v_element_dependecy.getAttributeValue("r")));
                v_vc_compability.setPatch(Integer.parseInt(v_element_dependecy.getAttributeValue("p")));
                v_list_compability.put(v_vc_compability.getCod_sistema(), v_vc_compability);
            }
            v_file_xml.delete();
        } catch (Exception e) {
            return null;
        }
        return v_list_compability;
    }

    /**
     * 
     * @param zipfile
     * @param extractionpoint
     * @param caminhoComp
     * @param productfolder
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public File ExtractCompabilityXML(ZipFile zipfile, File extractionpoint, String caminhoComp, String productfolder) throws FileNotFoundException, IOException {

        String v_s_caminhoComparaBase = productfolder + caminhoComp;
        File v_file_comp = null;

        //Verifica todos arquivos do ZIP
        for (Enumeration entries = zipfile.entries(); entries.hasMoreElements();) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            //ComparaÁ„o de cada arquivo zip com o arquivo de compatibilidade
            if (zipEntry.getName().replaceAll("/", "\\\\").equals(v_s_caminhoComparaBase)) {
                String zipEntryName = zipEntry.getName().replaceAll("/", "\\\\");

                //Cria o arquivo de compatibilidade
                v_file_comp = new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntryName.substring(zipEntryName.lastIndexOf(System.getProperty("file.separator")), zipEntryName.length()));

                if (!v_file_comp.exists()) {
                    v_file_comp.createNewFile();
                } else {
                    v_file_comp.delete();
                    v_file_comp.createNewFile();
                }

                FileOutputStream out = new FileOutputStream(v_file_comp, true);
                InputStream in = zipfile.getInputStream(zipEntry);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
            }
        }

        return v_file_comp;
    }

    /**
     * 
     * @return 
     */
    public boolean isFirstInstall() {
        return SfwWizardSetup.jRadioPrimeira.isSelected();
    }

    /**
     * 
     * @param cod_sistema
     * @return 
     */
    private Product ReturnProductByCod(String cod_sistema) {
        for (int i = 0; i < Install.get_product().size(); i++) {
            if (((Product) Install.get_product().get(i)).get_code().equals(cod_sistema)) {
                return ((Product) Install.get_product().get(i));
            }
        }
        return null;
    }
}
