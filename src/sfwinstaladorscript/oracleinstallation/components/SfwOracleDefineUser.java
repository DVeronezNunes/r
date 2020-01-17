/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwOracleDefineUser.java
 *
 * Created on Nov 24, 2008, 11:41:46 AM
 */
package sfwinstaladorscript.oracleinstallation.components;

import javax.swing.JOptionPane;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.Profile;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.ProductProfileInstall;
import sfwinstaladorscript.oracleinstallation.components_interfaces_define.SfwWizardDefineInterfacesR12;
import sfwinstaladorscript.oracleinstallation.components_interfaces_define.SfwWizardDefineInterfacesBDLINK;

/**
 * Componente com para alteração de usuário e senha de banco de um produto.
 */
public class SfwOracleDefineUser extends javax.swing.JPanel {

    private Product _product;

    /** Creates new form SfwOracleDefineUser */
    public SfwOracleDefineUser() {
        initComponents();
    }

    public SfwOracleDefineUser(Product product) {
        initComponents();

        this._product = product;
        this.jTextFieldProductLabel.setText(this._product.get_label());
        this.jTextFieldUser.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));
        this.jTextFieldPass.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));
        jLabelProblemaRecupera.setVisible(true);

        if (Profile.isFirstInstall()) {
            boolean ebs = false;
            try {
                if (SfwWizardDefineInterfacesR12.jCheckEBS.isSelected() && !SfwWizardDefineInterfacesR12.jCheckIT.isSelected()) {
                    ebs = true;
                } else {
                    ebs = false;
                }
            } catch (NullPointerException a) {
                try {
                    if (SfwWizardDefineInterfacesBDLINK.jCheckEBS.isSelected() && !SfwWizardDefineInterfacesBDLINK.jCheckIT.isSelected()) {
                        ebs = true;
                    } else {
                        ebs = false;
                    }
                } catch (NullPointerException b) {
                    ebs = false;
                }
            }
            if (ebs) {

                this.jTextFieldUser.setEnabled(false);
                this.jTextFieldPass.setEnabled(false);
                jLabelProblemaRecupera.setVisible(false);

            } else {
                jLabelProblemaRecupera.setVisible(false);
                if (this._product.get_name().equals("SOFTCOMEX")) {
                    this.jTextFieldUser.setText("TR" + Install.get_shortname().toUpperCase());
                    this.jTextFieldPass.setText("TR" + Install.get_shortname().toUpperCase());
                    jLabelProblemaRecupera.setVisible(false);
                } else {
                    this.jTextFieldUser.setText("TR" + this._product.get_shortname() + Install.get_shortname().toUpperCase());
                    this.jTextFieldPass.setText("TR" + this._product.get_shortname() + Install.get_shortname().toUpperCase());
                    jLabelProblemaRecupera.setVisible(false);
                }
            }
        } else {

            ProductProfileInstall v_productprofileinstall;
            v_productprofileinstall = (ProductProfileInstall) Profile.get_productsinstall().get(this._product.get_name());

            if (v_productprofileinstall != null) {
                this.jTextFieldUser.setText(v_productprofileinstall.get_user());
                this.jTextFieldPass.setText(v_productprofileinstall.get_pass());
                jLabelProblemaRecupera.setVisible(false);
            } else if ("TRACKING_SYS".equals(this._product.get_name())) {
                // Se n�o achou informa��es do TrackingSys, ent�o busca da custom e troca o CST por TS
                v_productprofileinstall = (ProductProfileInstall) Profile.get_productsinstall().get("CST");
                if (v_productprofileinstall != null) {
                    String userTracking = v_productprofileinstall.get_user().replaceFirst("CST", "TS");
                    this.jTextFieldUser.setText(userTracking);
                }
            }

        }

    }

    /**
     * Valida se campos estão preenchidos.
     * @return TRUE- tudo ok FALSE-alguma coisa faltou
     */
    public boolean Validate() {
        if (this.jTextFieldUser.getText().equals("")) {
            this.jTextFieldUser.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationuser") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (this.jTextFieldPass.getText().equals("")) {
            this.jTextFieldPass.requestFocus();
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationpwd") + " " + this._product.get_label() + ".", Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Retorna objeto produto.
     * @return Produto.
     */
    public Product getProduct() {
        return this._product;
    }

    /**
     * Rertorna senha de conexão do produto.
     * @return String com a senha.
     */
    public String getPassword() {
        return jTextFieldPass.getText();
    }

    /**
     * Altera senha de conexão do produto.
     * @param password String com a senha.
     */
    public void setPassword(String password) {
        this.jTextFieldPass.setText(password);
    }

    /**
     * Retorna usuário da base do produto.
     * @return String com o usuário.
     */
    public String getUser() {
        return jTextFieldUser.getText();
    }

    /**
     * Altera usuário da base do produto.
     * @param user String com o usuário.
     */
    public void setUser(String user) {
        this.jTextFieldUser.setText(user);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldUser = new javax.swing.JTextField();
        jTextFieldPass = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldProductLabel = new javax.swing.JTextField();
        jLabelProblemaRecupera = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwOracleDefineUser.class);
        jTextFieldUser.setText(resourceMap.getString("jTextFieldUser.text")); // NOI18N
        jTextFieldUser.setName("jTextFieldUser"); // NOI18N

        jTextFieldPass.setText(resourceMap.getString("jTextFieldPass.text")); // NOI18N
        jTextFieldPass.setName("jTextFieldPass"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldProductLabel.setBackground(resourceMap.getColor("jTextFieldProductLabel.background")); // NOI18N
        jTextFieldProductLabel.setEditable(false);
        jTextFieldProductLabel.setFont(resourceMap.getFont("jTextFieldProductLabel.font")); // NOI18N
        jTextFieldProductLabel.setForeground(resourceMap.getColor("jTextFieldProductLabel.foreground")); // NOI18N
        jTextFieldProductLabel.setText(resourceMap.getString("jTextFieldProductLabel.text")); // NOI18N
        jTextFieldProductLabel.setBorder(null);
        jTextFieldProductLabel.setName("jTextFieldProductLabel"); // NOI18N

        jLabelProblemaRecupera.setFont(resourceMap.getFont("jLabelProblemaRecupera.font")); // NOI18N
        jLabelProblemaRecupera.setForeground(resourceMap.getColor("jLabelProblemaRecupera.foreground")); // NOI18N
        jLabelProblemaRecupera.setText(resourceMap.getString("jLabelProblemaRecupera.text")); // NOI18N
        jLabelProblemaRecupera.setToolTipText(resourceMap.getString("jLabelProblemaRecupera.toolTipText")); // NOI18N
        jLabelProblemaRecupera.setName("jLabelProblemaRecupera"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextFieldProductLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldPass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelProblemaRecupera)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jTextFieldProductLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel2)
                .add(jTextFieldPass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel1)
                .add(jLabelProblemaRecupera))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelProblemaRecupera;
    private javax.swing.JTextField jTextFieldPass;
    private javax.swing.JTextField jTextFieldProductLabel;
    private javax.swing.JTextField jTextFieldUser;
    // End of variables declaration//GEN-END:variables
}
