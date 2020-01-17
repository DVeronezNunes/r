package sfwinstaladorscript;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sfwinstaladorscript.config.ProfileIni;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwWizardPage;
import sfwinstaladorscript.objects.Client;
import sfwinstaladorscript.objects.Database;
import sfwinstaladorscript.objects.SystemOS;

public class SfwWizardSetup extends javax.swing.JPanel implements SfwWizardPage {

    private ArrayList _clients;
    private ArrayList _systems;
    private ArrayList _databases;
    private ProfileIni _cfgProfileIni;
    private boolean v_boolean_houveverificacao = false;

    public SfwWizardSetup(ArrayList clients, ArrayList systems, ArrayList databases) {

        initComponents();

        this._systems = systems;
        this.sfwComboBoxSistema.bind(this._systems);

        this._databases = databases;
        this.sfwComboBoxDatabase.bind(this._databases);

        this._clients = clients;
        this.sfwComboBoxClient.bind(this._clients, Client.get_comparator());

        this.sfwComboBoxPackage.bind(Install.get_packagelist().get_packagelist(), sfwinstaladorscript.objects.Package.get_comparator());

        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            this.sfwComboBoxSistema.setSelectedItemInsensitive("WINDOWS");
        } else if (System.getProperty("os.name").toUpperCase().contains("LINUX")) {
            this.sfwComboBoxSistema.setSelectedItemInsensitive("LINUX");
        }

        this.sfwComboBoxDatabase.setSelectedItemInsensitive("ORACLE");

        // Guarda a informação de proxy da máquina
        Utils.ActiveProxy();

        this.sfwComboBoxClient.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Client c = null;

                //procura o cliente selecionado
                c = (Client) sfwComboBoxClient.getBindedSelectedItem();

                if (c != null) {
                    jTextFieldSigla.setText(c.get_shortname());
                } else {
                    jTextFieldSigla.setText("");
                }
            }
        });

        this.jTextFieldSigla.setToolTipText(Utils.getDefaultBundle().getString("Database.msghint"));

        if (jRadioPrimeira.isSelected()) {
            Profile.isFirstInstall(true);
            jLabelUsuario.setVisible(false);
            jTextUsuario.setVisible(false);
            jLabelSenha.setVisible(false);
            jTextSenha.setVisible(false);
            jButtonProcurar.setVisible(false);
            //jPanelGenerica.setVisible(false);
        }
        else
        {
            Profile.isFirstInstall(false);
        }

        if (Profile.isSet()) {
            this.sfwComboBoxSistema.setSelectedItemInsensitive(Profile.get_system());
            this.sfwComboBoxDatabase.setSelectedItemInsensitive(Profile.get_database());
            this.sfwComboBoxClient.setSelectedItemInsensitive(Profile.get_client());
            this.jTextFieldSigla.setText(Profile.get_shortname());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        sfwComboBoxDatabase = new sfwinstaladorscript.components.SfwComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sfwComboBoxSistema = new sfwinstaladorscript.components.SfwComboBox();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldSigla = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sfwComboBoxClient = new sfwinstaladorscript.components.SfwComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        sfwComboBoxPackage = new sfwinstaladorscript.components.SfwComboBox();
        jLabel6 = new javax.swing.JLabel();
        sfwPageTitle = new sfwinstaladorscript.components.SfwWizardPageTitle();
        jPanel4 = new javax.swing.JPanel();
        jRadioPrimeira = new javax.swing.JRadioButton();
        jRadioMigracao = new javax.swing.JRadioButton();
        jPanelGenerica = new javax.swing.JPanel();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelSenha = new javax.swing.JLabel();
        jTextUsuario = new javax.swing.JTextField();
        jTextSenha = new javax.swing.JPasswordField();
        jLabelTNS = new javax.swing.JLabel();
        jTextTNS = new javax.swing.JTextField();
        jButtonProcurar = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(599, 372));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwWizardSetup.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"), resourceMap.getColor("jPanel1.border.titleColor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        sfwComboBoxDatabase.setName("sfwComboBoxDatabase"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        sfwComboBoxSistema.setName("sfwComboBoxSistema"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jLabel5))
                .add(3, 3, 3)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(sfwComboBoxDatabase, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(sfwComboBoxSistema, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(sfwComboBoxSistema, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sfwComboBoxDatabase, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jTextFieldSigla.setText(resourceMap.getString("jTextFieldSigla.text")); // NOI18N
        jTextFieldSigla.setName("jTextFieldSigla"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        sfwComboBoxClient.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        sfwComboBoxClient.setName("sfwComboBoxClient"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabelClient.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabelClient.text")); // NOI18N
        jLabel2.setName("jLabelClient"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sfwComboBoxClient, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 213, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldSigla, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(sfwComboBoxClient, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldSigla, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        sfwComboBoxPackage.setName("sfwComboBoxPackage"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(122, 122, 122)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sfwComboBoxPackage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(147, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(sfwComboBoxPackage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sfwPageTitle.setName("sfwPageTitle"); // NOI18N
        sfwPageTitle.setSubtitulo(resourceMap.getString("sfwPageTitle.subtitulo")); // NOI18N
        sfwPageTitle.setTitulo(resourceMap.getString("sfwPageTitle.titulo")); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel4.border.titleFont"), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        buttonGroup1.add(jRadioPrimeira);
        jRadioPrimeira.setFont(resourceMap.getFont("jRadioButton2.font")); // NOI18N
        jRadioPrimeira.setSelected(true);
        jRadioPrimeira.setText(resourceMap.getString("jRadioPrimeira.text")); // NOI18N
        jRadioPrimeira.setName("jRadioPrimeira"); // NOI18N
        jRadioPrimeira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPrimeiraActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioMigracao);
        jRadioMigracao.setFont(resourceMap.getFont("jRadioMigracao.font")); // NOI18N
        jRadioMigracao.setText(resourceMap.getString("jRadioMigracao.text")); // NOI18N
        jRadioMigracao.setName("jRadioMigracao"); // NOI18N
        jRadioMigracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioMigracaoActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioPrimeira)
                    .add(jRadioMigracao))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jRadioPrimeira)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jRadioMigracao))
        );

        jPanelGenerica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanelGenerica.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanelGenerica.border.titleFont"), resourceMap.getColor("jPanelGenerica.border.titleColor"))); // NOI18N
        jPanelGenerica.setName("jPanelGenerica"); // NOI18N

        jLabelUsuario.setFont(resourceMap.getFont("jLabelUsuario.font")); // NOI18N
        jLabelUsuario.setForeground(resourceMap.getColor("jLabelUsuario.foreground")); // NOI18N
        jLabelUsuario.setText(resourceMap.getString("jLabelUsuario.text")); // NOI18N
        jLabelUsuario.setName("jLabelUsuario"); // NOI18N

        jLabelSenha.setFont(resourceMap.getFont("jLabelUsuario.font")); // NOI18N
        jLabelSenha.setForeground(resourceMap.getColor("jLabelUsuario.foreground")); // NOI18N
        jLabelSenha.setText(resourceMap.getString("jLabelSenha.text")); // NOI18N
        jLabelSenha.setName("jLabelSenha"); // NOI18N

        jTextUsuario.setText(resourceMap.getString("jTextUsuario.text")); // NOI18N
        jTextUsuario.setToolTipText(resourceMap.getString("jTextUsuario.toolTipText")); // NOI18N
        jTextUsuario.setName("jTextUsuario"); // NOI18N
        jTextUsuario.setNextFocusableComponent(jTextSenha);
        jTextUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextUsuarioFocusGained(evt);
            }
        });

        jTextSenha.setText(resourceMap.getString("jTextSenha.text")); // NOI18N
        jTextSenha.setName("jTextSenha"); // NOI18N
        jTextSenha.setNextFocusableComponent(jTextTNS);
        jTextSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextSenhaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextSenhaFocusLost(evt);
            }
        });

        jLabelTNS.setFont(resourceMap.getFont("jLabelUsuario.font")); // NOI18N
        jLabelTNS.setForeground(resourceMap.getColor("jLabelUsuario.foreground")); // NOI18N
        jLabelTNS.setText(resourceMap.getString("jLabelTNS.text")); // NOI18N
        jLabelTNS.setName("jLabelTNS"); // NOI18N

        jTextTNS.setText(resourceMap.getString("jTextTNS.text")); // NOI18N
        jTextTNS.setName("jTextTNS"); // NOI18N
        jTextTNS.setNextFocusableComponent(sfwComboBoxSistema);
        jTextTNS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextTNSFocusGained(evt);
            }
        });

        jButtonProcurar.setText(resourceMap.getString("jButtonProcurar.text")); // NOI18N
        jButtonProcurar.setName("jButtonProcurar"); // NOI18N
        jButtonProcurar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonProcurarMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelGenericaLayout = new org.jdesktop.layout.GroupLayout(jPanelGenerica);
        jPanelGenerica.setLayout(jPanelGenericaLayout);
        jPanelGenericaLayout.setHorizontalGroup(
            jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelGenericaLayout.createSequentialGroup()
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelUsuario)
                    .add(jLabelSenha))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextSenha, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .add(jTextUsuario, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanelGenericaLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelTNS)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextTNS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelGenericaLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButtonProcurar)
                        .add(19, 19, 19))))
        );
        jPanelGenericaLayout.setVerticalGroup(
            jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelGenericaLayout.createSequentialGroup()
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextTNS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelTNS))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelSenha)
                    .add(jTextSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonProcurar))
                .add(19, 19, 19))
            .add(jPanelGenericaLayout.createSequentialGroup()
                .add(jPanelGenericaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelUsuario)
                    .add(jTextUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .add(56, 56, 56)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelGenerica, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(76, 76, 76))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanelGenerica, 0, 73, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(42, 42, 42))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioMigracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioMigracaoActionPerformed
        //JOptionPane.showMessageDialog(null, "Para migração, é necessário já existir a Base Genérica instalada. ", "Atenção", JOptionPane.WARNING_MESSAGE);
        jLabelUsuario.setVisible(true);
        jTextUsuario.setVisible(true);
        jLabelSenha.setVisible(true);
        jTextSenha.setVisible(true);
        jButtonProcurar.setVisible(true);
        jTextUsuario.requestFocus();
        Profile.isFirstInstall(false);
    }//GEN-LAST:event_jRadioMigracaoActionPerformed

    private void jRadioPrimeiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPrimeiraActionPerformed
        jLabelUsuario.setVisible(false);
        jTextUsuario.setVisible(false);
        jLabelSenha.setVisible(false);
        jTextSenha.setVisible(false);
        jButtonProcurar.setVisible(false);
        jTextTNS.setText("");
        jTextFieldSigla.setText("");
        Profile.isSet(false);
        Profile.isFirstInstall(true);
    }//GEN-LAST:event_jRadioPrimeiraActionPerformed

    private void jTextUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextUsuarioFocusGained
        jTextUsuario.selectAll();
    }//GEN-LAST:event_jTextUsuarioFocusGained

    private void jTextSenhaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextSenhaFocusGained
        jTextSenha.selectAll();
    }//GEN-LAST:event_jTextSenhaFocusGained

    private void jTextSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextSenhaFocusLost
    }//GEN-LAST:event_jTextSenhaFocusLost

    private void jButtonProcurarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonProcurarMouseClicked
        if (this.BaseGenericaValidation()) {
            v_boolean_houveverificacao = true;
        } else {
            v_boolean_houveverificacao = false;
            return;
        }
    }//GEN-LAST:event_jButtonProcurarMouseClicked

    private void jTextTNSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextTNSFocusGained
        jTextTNS.selectAll();
    }//GEN-LAST:event_jTextTNSFocusGained
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonProcurar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelSenha;
    private javax.swing.JLabel jLabelTNS;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelGenerica;
    private javax.swing.JRadioButton jRadioMigracao;
    public static javax.swing.JRadioButton jRadioPrimeira;
    private javax.swing.JTextField jTextFieldSigla;
    public static javax.swing.JTextField jTextSenha;
    public static javax.swing.JTextField jTextTNS;
    public static javax.swing.JTextField jTextUsuario;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxClient;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxDatabase;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxPackage;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxSistema;
    private sfwinstaladorscript.components.SfwWizardPageTitle sfwPageTitle;
    // End of variables declaration//GEN-END:variables

    /**
     * ImplementaÃ§Ã£o da aÃ§Ã£o do botÃ£o "PrÃ³ximo" para esta pÃ¡gina.
     * @param wzPages Hashtable com todas as pÃ¡ginas do wizard.
     * @param view Frame principal da aplicaÃ§Ã£o.
     */
    public void nextClick(Hashtable wzPages, SfwInstaladorScriptView view) {

        Client v_client_selectedclient = null;
        Database v_database_selecteddb = null;
        SystemOS v_systemos_selectedos = null;
        sfwinstaladorscript.objects.Package v_package_selectedpkg = null;
        
        Utils.setBusyCursor();

        if (jRadioMigracao.isSelected()) {
            if (!v_boolean_houveverificacao) {
                if (!this.BaseGenericaValidation()) {
                    return;
                }
            }
        }

        //procura e salva o sistema selecionado
        v_systemos_selectedos = (SystemOS) this.sfwComboBoxSistema.getBindedSelectedItem();

        if (v_systemos_selectedos == null || v_systemos_selectedos.get_name().equals("")) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("systemvalidation"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            this.sfwComboBoxSistema.requestFocus();
            return;
        }

        Install.set_system(v_systemos_selectedos);

        //procura e salva o banco de dados selecionado
        v_database_selecteddb = (Database) this.sfwComboBoxDatabase.getBindedSelectedItem();

        if (v_database_selecteddb == null || v_database_selecteddb.get_name().equals("")) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("databasevalidation"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            this.sfwComboBoxDatabase.requestFocus();
            return;
        }

        Install.set_database(v_database_selecteddb);

        if (jPanelGenerica.isVisible() && jTextUsuario.getText().equals("") && jTextSenha.getText().equals("") && jTextTNS.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Por favor, digite o usuário, senha e TNS da Base Genérica. ", "Atenção", JOptionPane.ERROR_MESSAGE);
            jTextUsuario.requestFocus();
            return;
        }

        Install.set_tns(jTextTNS.getText());

        //procura e salva o cliente selecionado
        v_client_selectedclient = (Client) this.sfwComboBoxClient.getBindedSelectedItem();

        if (v_client_selectedclient == null || v_client_selectedclient.get_name().equals("")) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("clientvalidation"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            this.sfwComboBoxClient.requestFocus();
            return;
        }

        Install.set_client(v_client_selectedclient);

        //salva sigla
        if (this.jTextFieldSigla.getText().equals("")) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("clienttagvalidation"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            this.jTextFieldSigla.requestFocus();
            return;
        }

        Install.set_shortname(this.jTextFieldSigla.getText());

        //procura e salva o pacote selecionado
        v_package_selectedpkg = (sfwinstaladorscript.objects.Package) this.sfwComboBoxPackage.getBindedSelectedItem();

        if (v_package_selectedpkg == null || v_package_selectedpkg.get_name().equals("")) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("packagevalidation"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            this.sfwComboBoxPackage.requestFocus();
            return;
        }

        try {
            String v_string_packageversion = this.extractZipObject(v_package_selectedpkg.get_zip(), "versao.txt");
            String v_string_version = java.util.ResourceBundle.getBundle("sfwinstaladorscript/resources/SfwInstaladorScriptApp").getString("Application.version");
            int v_int_version = Integer.parseInt(v_string_version.replace(".", ""));
            int v_int_packageversion = Integer.parseInt(v_string_packageversion.replace(".", ""));
            if (v_int_packageversion != v_int_version) {
                JOptionPane.showMessageDialog(null, String.format(Utils.getDefaultBundle(this).getString("validationinstaladorversion"), v_string_packageversion, v_string_version), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception e) {
        }

        Install.set_package(v_package_selectedpkg);

        // instancia nova pagina do wizard de acordo com o banco de dados escolhido
        view.setWizardDatabase(v_database_selecteddb.get_flowwizard());
        view.setWizardPage((JPanel) wzPages.get("wzFlowDatabase"));

        Utils.setDefaultCursor();
    }

    /**
     * Implementação da ação do botÃ£o "Anterior" para esta página.
     * @param wzPages Hashtable com todas as páginas do wizard.
     * @param view Frame principal da aplicaÃ§Ã£o.
     */
    public void backClick(Hashtable wzPages, SfwInstaladorScriptView view) {
        Install.set_system(null);
        Install.set_client(null);
        Install.set_shortname("");
        v_boolean_houveverificacao = false;
        Profile.isSet(false);

        view.setWizardPage((JPanel) wzPages.get("wzWelcome"));
    }

    /**
     * Implementação de setup inicial da página.
     * @param view Frame principal da aplicação.
     */
    public void flowSetup(SfwInstaladorScriptView view) {
        view.getBackButton().setEnabled(true);
        view.getNextButton().setEnabled(true);
        v_boolean_houveverificacao = false;
        Profile.isSet(false);
    }

    /**
     *
     * @param Objeto que você quer extrair
     * @return 
     * @throws IOException
     */
    public String extractZipObject(ZipFile zip, String objectname) throws IOException {
        String v_string_object = "";

        // Enumerate each entry
        for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {

            // Get the entry and its name
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();

            if (objectname.equals(zipEntry.getName())) {
                if (!zipEntry.isDirectory()) {
                    String byteToString;
                    String zipEntryName = zipEntry.getName();
                    InputStream in = zip.getInputStream(zipEntry);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        byteToString = new String(buf, 0, len);
                        v_string_object = v_string_object + byteToString;
                        byteToString = null;
                    }
                    // Close streams
                    in.close();
                    return v_string_object;
                }
            }
        }
        return "";
    }

    /**
     * 
     * @return
     */
    public boolean BaseGenericaValidation() {
        if (!jTextUsuario.getText().equals("") && !jTextSenha.getText().equals("") && !jTextSenha.getText().equals("")) {
            Utils.setBusyCursor();
            this.setEnabled(false);
            OracleConnection v_conn = new OracleConnection();
            try {
                v_conn.set_username(jTextUsuario.getText());
                v_conn.set_password(jTextSenha.getText());
                v_conn.set_tns(jTextTNS.getText());
                v_conn.ConnectAux();
                FileInputStream v_fileinputstream_profile;
                if (v_conn.createProfileFromDataBase()) {
                    File v_file_profile = new File("profile.ini");
                    v_fileinputstream_profile = new FileInputStream(v_file_profile);
                    this._cfgProfileIni = new ProfileIni(v_fileinputstream_profile);
                }
                if (Profile.isSet()) {
                    this.sfwComboBoxSistema.setSelectedItemInsensitive(Profile.get_system());
                    this.sfwComboBoxDatabase.setSelectedItemInsensitive(Profile.get_database());
                    this.sfwComboBoxClient.setSelectedItemInsensitive(Profile.get_client());
                    this.jTextFieldSigla.setText(Profile.get_shortname());
                }
                v_conn.Close();
                Utils.setDefaultCursor();
                this.setEnabled(true);
                return true;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1017 || e.getErrorCode() == 17002) {
                    Utils.setDefaultCursor();
                    this.setEnabled(true);
                    v_conn.Close();
                    JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgcreatesession"), v_conn.get_username(), System.getProperty("line.separator")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
                    jTextUsuario.requestFocus();
                    return false;
                }
                if (e.getErrorCode() == 12154) {
                    Utils.setDefaultCursor();
                    this.setEnabled(true);
                    v_conn.Close();
                    JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgtnserror")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
                    jTextUsuario.requestFocus();
                    return false;
                }
                if (e.getErrorCode() == 0) {
                    Utils.setDefaultCursor();
                    this.setEnabled(true);
                    v_conn.Close();
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgtnserror")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
                    jTextTNS.requestFocus();
                    return false;
                }
                if (e.getErrorCode() == 17006 || e.getErrorCode() == 904) {
                    Utils.setDefaultCursor();
                    this.setEnabled(true);
                    v_conn.Close();
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgversaogenerica")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
                    jTextTNS.requestFocus();
                    return false;
                }
                if (e.getErrorCode() == 942) {
                    Utils.setDefaultCursor();
                    v_conn.Close();
                    return true;
                }

            } catch (Exception e) {
                Utils.setDefaultCursor();
                this.setEnabled(true);
                v_conn.Close();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgcreatesession"), v_conn.get_username(), System.getProperty("line.separator")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        Utils.setDefaultCursor();
        this.setEnabled(true);
        JOptionPane.showMessageDialog(null, String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("msgcamposnecessarios")), String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator").getString("validationlabel")), JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
