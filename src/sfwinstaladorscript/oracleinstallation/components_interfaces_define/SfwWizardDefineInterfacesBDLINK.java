/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwWizardDefineInterfacesBDLINK.java
 *
 * Created on 18/01/2013, 10:43:39
 */
package sfwinstaladorscript.oracleinstallation.components_interfaces_define;

import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.rmi.CORBA.Util;
import javax.swing.*;

/**
 *
 * @author jopaulo
 */
public class SfwWizardDefineInterfacesBDLINK extends javax.swing.JPanel {

    /** Creates new form SfwWizardDefineInterfacesBDLINK */
    public SfwWizardDefineInterfacesBDLINK() {
        initComponents();

        this.jCheckIT.setSelected(true);
        this.jCheckEBS.setSelected(true);
        this.jPanel2.setVisible(true);
        this.jPanel3.setVisible(true);

    }

    /**
     * Valida se existe campos em branco
     * @return 
     */
    public boolean validaCampos() {
        boolean valida = true;
        if (this.jTextAPPS_PASS.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextAPPS_TNS.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextAPPS_USER.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextXXISV_PASS.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextXXISV_TNS.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextXXISV_USER.getText().equals("") && this.jCheckEBS.isSelected()) {
            valida = false;
        }
        if (this.jTextDBLINK1.getText().equals("") && this.jCheckIT.isSelected()) {
            valida = false;
        }
        if (!this.jCheckEBS.isSelected() && !this.jCheckIT.isSelected()) {
            valida = false;
        }
        return valida;
    }

    /**
     * Valida se os usuarios digitados estão corretos
     * @return
     */
    public boolean validaConexao() {

        boolean valida = true;
        String connectionFailed = "";
        OracleConnection connection = null;
        Utils.setBusyCursor();

        if((this.jCheckEBS.isSelected() && this.jCheckIT.isSelected()) || (this.jCheckEBS.isSelected() && !this.jCheckIT.isSelected())){

            connection = new OracleConnection();

            connection.set_tns(this.jTextAPPS_TNS.getText());
            connection.set_username(this.jTextAPPS_USER.getText());
            connection.set_password(this.jTextAPPS_PASS.getText());

            try {
                connection.Connect();
                connection.Close();
            } catch (Exception e) {
                connection.Close();
                connectionFailed += "\nFailed to connect APPS user! Please check user/password!";
                valida = false;
            }

            connection.set_tns(this.jTextXXISV_TNS.getText());
            connection.set_username(this.jTextXXISV_USER.getText());
            connection.set_password(this.jTextXXISV_PASS.getText());

            try {
                connection.Connect();
                connection.Close();
            } catch (Exception e) {
                connection.Close();
                connectionFailed += "\nFailed to connect XXIV user! Please check user/password!";
                valida = false;
            }

        }

        Utils.setDefaultCursor();

        if(!"".equals(connectionFailed)){

            connectionFailed += "\n\n " + Utils.getDefaultBundle().getString("Validation.connection.continue");

            String[] questionButton = { Utils.getDefaultBundle().getString("yes"), Utils.getDefaultBundle().getString("no") };

            int pressionedOption = JOptionPane.showOptionDialog(null, connectionFailed,
                    Utils.getDefaultBundle().getString("Validation.warn"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, questionButton, null);

            if (pressionedOption == 0)
                valida = true;

        }

        return valida;
    }

    /**
     * 
     * @param valores
     * @return 
     */
    public boolean populaCampos(List<SfwOracleDefineInterface> valores) {


        for (int i = 0; i < valores.size(); i++) {
            SfwOracleDefineInterface sfwOracleDefineInterface = valores.get(i);
            if (sfwOracleDefineInterface.getNome().equals("XXISV")) {
                this.jTextXXISV_USER.setText(sfwOracleDefineInterface.getUser());
                this.jTextXXISV_PASS.setText(sfwOracleDefineInterface.getPass());
                this.jTextXXISV_TNS.setText(sfwOracleDefineInterface.getTns());
            } else if (sfwOracleDefineInterface.getNome().equals("APPS")) {
                this.jTextAPPS_USER.setText(sfwOracleDefineInterface.getUser());
                this.jTextAPPS_PASS.setText(sfwOracleDefineInterface.getPass());
                this.jTextAPPS_TNS.setText(sfwOracleDefineInterface.getTns());
            } else if (sfwOracleDefineInterface.getNome().equals("DBLINK")) {
                this.jTextDBLINK1.setText(sfwOracleDefineInterface.getUser());
            }
        }
        return true;
    }

    /**
     * 
     * @return 
     */
    public List<SfwOracleDefineInterface> retornaUsuariosDefine() {
        List<SfwOracleDefineInterface> oracle_defines = new ArrayList<SfwOracleDefineInterface>();

        SfwOracleDefineInterface XXISV = new SfwOracleDefineInterface();
        XXISV.setNome("XXISV");
        XXISV.setUser(this.jTextXXISV_USER.getText());
        XXISV.setPass(this.jTextXXISV_PASS.getText());
        XXISV.setTns(this.jTextXXISV_TNS.getText());
        oracle_defines.add(XXISV);

        SfwOracleDefineInterface APPS = new SfwOracleDefineInterface();
        APPS.setNome("APPS");
        APPS.setUser(this.jTextAPPS_USER.getText());
        APPS.setPass(this.jTextAPPS_PASS.getText());
        APPS.setTns(this.jTextAPPS_TNS.getText());
        oracle_defines.add(APPS);

        SfwOracleDefineInterface DBLINK = new SfwOracleDefineInterface();
        DBLINK.setNome("DBLINK");
        DBLINK.setUser(this.jTextDBLINK1.getText());
        DBLINK.setPass(this.jTextDBLINK1.getText());
        DBLINK.setTns(this.jTextDBLINK1.getText());
        oracle_defines.add(DBLINK);

        return oracle_defines;
    }

    public static JCheckBox getjCheckBox1() {
        return jCheckIT;
    }

    public void setjCheckBox1(JCheckBox jCheckBox1) {
        this.jCheckIT = jCheckBox1;
    }

    public static JCheckBox getjCheckBox2() {
        return jCheckEBS;
    }

    public void setjCheckBox2(JCheckBox jCheckBox2) {
        this.jCheckEBS = jCheckBox2;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCheckEBS = new javax.swing.JCheckBox();
        jCheckIT = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextAPPS_USER = new javax.swing.JTextField();
        jTextAPPS_PASS = new javax.swing.JPasswordField();
        jTextAPPS_TNS = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextXXISV_USER = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextXXISV_PASS = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jTextXXISV_TNS = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jTextDBLINK1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwWizardDefineInterfacesBDLINK.class);
        jCheckEBS.setFont(resourceMap.getFont("jCheckEBS.font")); // NOI18N
        jCheckEBS.setForeground(resourceMap.getColor("jCheckEBS.foreground")); // NOI18N
        jCheckEBS.setText(resourceMap.getString("jCheckEBS.text")); // NOI18N
        jCheckEBS.setName("jCheckEBS"); // NOI18N
        jCheckEBS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckEBSMousePressed(evt);
            }
        });

        jCheckIT.setFont(resourceMap.getFont("jCheckIT.font")); // NOI18N
        jCheckIT.setForeground(resourceMap.getColor("jCheckIT.foreground")); // NOI18N
        jCheckIT.setText(resourceMap.getString("jCheckIT.text")); // NOI18N
        jCheckIT.setName("jCheckIT"); // NOI18N
        jCheckIT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckITMousePressed(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel5.setForeground(resourceMap.getColor("jLabel5.foreground")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel6.setForeground(resourceMap.getColor("jLabel6.foreground")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel7.setForeground(resourceMap.getColor("jLabel7.foreground")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jTextAPPS_USER.setName("jTextAPPS_USER"); // NOI18N

        jTextAPPS_PASS.setName("jTextAPPS_PASS"); // NOI18N

        jTextAPPS_TNS.setName("jTextAPPS_TNS"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextAPPS_USER, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextAPPS_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jTextAPPS_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextAPPS_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextAPPS_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(3, 3, 3)
                        .addComponent(jTextAPPS_USER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setName("jPanel4"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextXXISV_USER.setName("jTextXXISV_USER"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jTextXXISV_PASS.setName("jTextXXISV_PASS"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel4.setForeground(resourceMap.getColor("jLabel4.foreground")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jTextXXISV_TNS.setName("jTextXXISV_TNS"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jTextXXISV_USER, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextXXISV_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextXXISV_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextXXISV_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextXXISV_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextXXISV_USER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckEBS)
                .addGap(18, 18, 18)
                .addComponent(jCheckIT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckEBS)
                    .addComponent(jCheckIT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jPanel3.setName("jPanel3"); // NOI18N

        jTextDBLINK1.setName("jTextDBLINK1"); // NOI18N

        jLabel15.setFont(resourceMap.getFont("jLabel15.font")); // NOI18N
        jLabel15.setForeground(resourceMap.getColor("jLabel15.foreground")); // NOI18N
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextDBLINK1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                    .addComponent(jLabel15))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextDBLINK1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 0, 545, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckEBSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckEBSMousePressed
        if (this.jCheckEBS.isSelected() && this.jCheckIT.isSelected()) {
            this.jPanel2.setVisible(false);
            this.jTextXXISV_PASS.setEnabled(false);
            this.jTextXXISV_TNS.setEnabled(false);
        } else if (this.jCheckEBS.isSelected() && !this.jCheckIT.isSelected()) {
            this.jPanel2.setVisible(false);
            this.jPanel4.setVisible(false);
        } else {
            this.jPanel2.setVisible(true);
            this.jPanel4.setVisible(true);
            this.jTextXXISV_PASS.setEnabled(true);
            this.jTextXXISV_TNS.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckEBSMousePressed

    private void jCheckITMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckITMousePressed
        if (this.jCheckIT.isSelected() && this.jCheckEBS.isSelected()) {
            this.jPanel3.setVisible(false);
        } else if (this.jCheckIT.isSelected() && !this.jCheckEBS.isSelected()) {
            this.jPanel3.setVisible(false);
            this.jPanel4.setVisible(false);
        } else if (!this.jCheckIT.isSelected() && this.jCheckEBS.isSelected()) {
            this.jPanel3.setVisible(true);
            this.jPanel4.setVisible(true);
            this.jTextXXISV_PASS.setEnabled(true);
            this.jTextXXISV_TNS.setEnabled(true);
        } else {
            this.jPanel3.setVisible(true);
            this.jPanel4.setVisible(true);
            this.jTextXXISV_PASS.setEnabled(false);
            this.jTextXXISV_TNS.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckITMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JCheckBox jCheckEBS;
    public static javax.swing.JCheckBox jCheckIT;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextAPPS_PASS;
    private javax.swing.JTextField jTextAPPS_TNS;
    private javax.swing.JTextField jTextAPPS_USER;
    private javax.swing.JTextField jTextDBLINK1;
    private javax.swing.JTextField jTextXXISV_PASS;
    private javax.swing.JTextField jTextXXISV_TNS;
    private javax.swing.JTextField jTextXXISV_USER;
    // End of variables declaration//GEN-END:variables
}
