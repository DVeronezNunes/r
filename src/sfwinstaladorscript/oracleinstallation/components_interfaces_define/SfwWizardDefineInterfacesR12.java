/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwWizardDefineInterfacesR12.java
 *
 * Created on 18/01/2013, 10:30:57
 */
package sfwinstaladorscript.oracleinstallation.components_interfaces_define;

import sfwinstaladorscript.Utils;
import sfwinstaladorscript.database.OracleConnection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author jopaulo
 */
public class SfwWizardDefineInterfacesR12 extends javax.swing.JPanel {

    /** Creates new form SfwWizardDefineInterfacesR12 */
    public SfwWizardDefineInterfacesR12() {
        initComponents();

        this.jCheckIT.setSelected(true);
        this.jCheckEBS.setSelected(true);
        this.jPanel1.setVisible(true);
        this.jPanel2.setVisible(true);
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
        if (this.jTextSOA_PASS1.getText().equals("") && this.jCheckIT.isSelected()) {
            valida = false;
        }
        if (this.jTextSOA_USER1.getText().equals("") && this.jCheckIT.isSelected()) {
            valida = false;
        }
        if (this.jTextSOA_TNS1.getText().equals("") && this.jCheckIT.isSelected()) {
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

        if(this.jCheckEBS.isSelected() && this.jCheckIT.isSelected()){

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

            connection.set_tns(this.jTextSOA_TNS1.getText());
            connection.set_username(this.jTextSOA_USER1.getText());
            connection.set_password(this.jTextSOA_PASS1.getText());

            try {
                connection.Connect();
                connection.Close();
            } catch (Exception e) {
                connection.Close();
                connectionFailed += "\nFailed to connect SOA user! Please check user/password!";
                valida = false;
            }
        } else if(this.jCheckEBS.isSelected() && !this.jCheckIT.isSelected()){

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

        } else if(!this.jCheckEBS.isSelected() && this.jCheckIT.isSelected()){

            connection = new OracleConnection();

            connection.set_tns(this.jTextSOA_TNS1.getText());
            connection.set_username(this.jTextSOA_USER1.getText());
            connection.set_password(this.jTextSOA_PASS1.getText());

            try {
                connection.Connect();
                connection.Close();
            } catch (Exception e) {
                connection.Close();
                connectionFailed += "\nFailed to connect SOA user! Please check user/password!";
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
            } else if (sfwOracleDefineInterface.getNome().equals("SOACONSOLE")) {
                this.jTextSOA_USER1.setText(sfwOracleDefineInterface.getUser());
                this.jTextSOA_PASS1.setText(sfwOracleDefineInterface.getPass());
                this.jTextSOA_TNS1.setText(sfwOracleDefineInterface.getTns());
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


        SfwOracleDefineInterface SOACONSOLE = new SfwOracleDefineInterface();
        SOACONSOLE.setNome("SOACONSOLE");
        SOACONSOLE.setUser(this.jTextSOA_USER1.getText());
        SOACONSOLE.setPass(this.jTextSOA_PASS1.getText());
        SOACONSOLE.setTns(this.jTextSOA_TNS1.getText());
        oracle_defines.add(SOACONSOLE);

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextXXISV_USER = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextXXISV_PASS = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jTextXXISV_TNS = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextAPPS_USER = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextAPPS_PASS = new javax.swing.JPasswordField();
        jLabel13 = new javax.swing.JLabel();
        jTextAPPS_TNS = new javax.swing.JTextField();
        jCheckIT = new javax.swing.JCheckBox();
        jCheckEBS = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextSOA_USER1 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextSOA_PASS1 = new javax.swing.JPasswordField();
        jLabel31 = new javax.swing.JLabel();
        jTextSOA_TNS1 = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwWizardDefineInterfacesR12.class);
        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel8.setForeground(resourceMap.getColor("jLabel8.foreground")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jTextXXISV_USER.setName("jTextXXISV_USER"); // NOI18N

        jLabel9.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel9.setForeground(resourceMap.getColor("jLabel9.foreground")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jTextXXISV_PASS.setName("jTextXXISV_PASS"); // NOI18N

        jLabel10.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel10.setForeground(resourceMap.getColor("jLabel10.foreground")); // NOI18N
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jTextXXISV_TNS.setName("jTextXXISV_TNS"); // NOI18N

        jLabel11.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel11.setForeground(resourceMap.getColor("jLabel11.foreground")); // NOI18N
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jTextAPPS_USER.setName("jTextAPPS_USER"); // NOI18N

        jLabel12.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel12.setForeground(resourceMap.getColor("jLabel12.foreground")); // NOI18N
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jTextAPPS_PASS.setName("jTextAPPS_USER"); // NOI18N

        jLabel13.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel13.setForeground(resourceMap.getColor("jLabel13.foreground")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jTextAPPS_TNS.setName("jTextAPPS_TNS"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(193, 193, 193)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextAPPS_PASS, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jTextAPPS_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(192, 192, 192)
                        .addComponent(jLabel3)
                        .addGap(18, 316, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextAPPS_USER, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel7)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(jTextXXISV_USER, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(193, 193, 193)
                            .addComponent(jTextXXISV_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(193, 193, 193)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(398, Short.MAX_VALUE)
                    .addComponent(jTextXXISV_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(34, 34, 34)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel4))))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextAPPS_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(3, 3, 3)
                                .addComponent(jTextAPPS_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(3, 3, 3)
                                .addComponent(jTextAPPS_USER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel5)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextXXISV_TNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextXXISV_USER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextXXISV_PASS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(57, Short.MAX_VALUE)))
        );

        jCheckIT.setFont(resourceMap.getFont("jCheckIT.font")); // NOI18N
        jCheckIT.setForeground(resourceMap.getColor("jCheckIT.foreground")); // NOI18N
        jCheckIT.setText(resourceMap.getString("jCheckIT.text")); // NOI18N
        jCheckIT.setName("jCheckIT"); // NOI18N
        jCheckIT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckITMousePressed(evt);
            }
        });

        jCheckEBS.setFont(resourceMap.getFont("jCheckEBS.font")); // NOI18N
        jCheckEBS.setForeground(resourceMap.getColor("jCheckEBS.foreground")); // NOI18N
        jCheckEBS.setText(resourceMap.getString("jCheckEBS.text")); // NOI18N
        jCheckEBS.setName("jCheckEBS"); // NOI18N
        jCheckEBS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckEBSMousePressed(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel17.setFont(resourceMap.getFont("jLabel17.font")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel18.setFont(resourceMap.getFont("jLabel18.font")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel19.setFont(resourceMap.getFont("jLabel19.font")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel20.setFont(resourceMap.getFont("jLabel20.font")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel21.setFont(resourceMap.getFont("jLabel21.font")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel22.setFont(resourceMap.getFont("jLabel22.font")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel29.setFont(resourceMap.getFont("jLabel29.font")); // NOI18N
        jLabel29.setForeground(resourceMap.getColor("jLabel29.foreground")); // NOI18N
        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        jTextSOA_USER1.setName("jTextSOA_USER1"); // NOI18N

        jLabel30.setFont(resourceMap.getFont("jLabel30.font")); // NOI18N
        jLabel30.setForeground(resourceMap.getColor("jLabel30.foreground")); // NOI18N
        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N

        jTextSOA_PASS1.setName("jTextSOA_PASS1"); // NOI18N

        jLabel31.setFont(resourceMap.getFont("jLabel31.font")); // NOI18N
        jLabel31.setForeground(resourceMap.getColor("jLabel31.foreground")); // NOI18N
        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setName("jLabel31"); // NOI18N

        jTextSOA_TNS1.setName("jTextSOA_TNS1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addGap(151, 151, 151)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(jLabel21)
                        .addGap(267, 267, 267))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextSOA_USER1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jTextSOA_PASS1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(jTextSOA_TNS1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)))
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel22)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel29)
                .addGap(102, 102, 102))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel19))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel17)))
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextSOA_USER1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextSOA_PASS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextSOA_TNS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel21))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckEBS)
                .addGap(18, 18, 18)
                .addComponent(jCheckIT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckEBS)
                    .addComponent(jCheckIT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jCheckIT.getAccessibleContext().setAccessibleName(resourceMap.getString("jCheckBox1.AccessibleContext.accessibleName")); // NOI18N
        jCheckEBS.getAccessibleContext().setAccessibleName(resourceMap.getString("jCheckBox2.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckEBSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckEBSMousePressed
        if (this.jCheckEBS.isSelected()) {
            this.jPanel1.setVisible(false);
        } else {
            this.jPanel1.setVisible(true);
        }
    }//GEN-LAST:event_jCheckEBSMousePressed

    private void jCheckITMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckITMousePressed
        if (this.jCheckIT.isSelected()) {
            this.jPanel2.setVisible(false);
        } else {
            this.jPanel2.setVisible(true);
        }
    }//GEN-LAST:event_jCheckITMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JCheckBox jCheckEBS;
    public static javax.swing.JCheckBox jCheckIT;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextAPPS_PASS;
    private javax.swing.JTextField jTextAPPS_TNS;
    private javax.swing.JTextField jTextAPPS_USER;
    private javax.swing.JTextField jTextSOA_PASS1;
    private javax.swing.JTextField jTextSOA_TNS1;
    private javax.swing.JTextField jTextSOA_USER1;
    private javax.swing.JTextField jTextXXISV_PASS;
    private javax.swing.JTextField jTextXXISV_TNS;
    private javax.swing.JTextField jTextXXISV_USER;
    // End of variables declaration//GEN-END:variables
}
