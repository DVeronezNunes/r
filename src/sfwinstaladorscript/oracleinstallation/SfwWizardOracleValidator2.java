/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwWizardValidator.java
 *
 * Created on 30/10/2008, 17:22:31
 */
package sfwinstaladorscript.oracleinstallation;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import org.jdom.JDOMException;
import sfwinstaladorscript.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import sfwinstaladorscript.comparabase.SfwXMLBuilder;
import sfwinstaladorscript.comparabase.SfwXMLReader;
import sfwinstaladorscript.comparabase.objects.BaseCompare;
import sfwinstaladorscript.comparabase.objects.CompareErro;
import sfwinstaladorscript.comparabase.objects.Info;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwWizardPage;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.Version;
import sfwinstaladorscript.oracleinstallation.components.SfwOracleDefineUser;

/**
 * PÃ¡gina de validaÃ§Ãµes de base de dados por produto.
 */
public class SfwWizardOracleValidator2 extends javax.swing.JPanel implements SfwWizardPage {

    private OracleConnection _oraconn;
    private SfwInstaladorScriptView _view;
    private Task _validatebase;
    private boolean _validation_ok;

    /** Creates new form SfwWizardValidator */
    public SfwWizardOracleValidator2(SfwInstaladorScriptView view) {
        initComponents();

        this._view = view;
        this._oraconn = new OracleConnection();

        final SfwWizardOracleValidator2 sfwWizardOracleValidator2 = this;

        this.sfwListProducts.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    SfwOracleFrameProductDetail fpd = new SfwOracleFrameProductDetail((SfwOracleProductDetail) sfwListProducts.getSelectedValue());
                    fpd.setLocationRelativeTo(null);
                    fpd.setVisible(true);
                    jButtonValidate.setEnabled(true);
                    _view.getNextButton().setEnabled(false);
                }
            }

            public void mousePressed(MouseEvent e) {
                int idx;
                JPopupMenu contextMenu;
                JMenuItem menuItRetirar;

                if (e.getButton() == MouseEvent.BUTTON3) {
                    idx = sfwListProducts.locationToIndex(e.getPoint());
                    sfwListProducts.setSelectedIndex(idx);
                }

                contextMenu = new JPopupMenu();
                UIManager.put("MenuItem.selectionBackground", new Color(210, 211, 249));
                menuItRetirar = new JMenuItem(String.format(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleValidator2").getString("removefrominstall"), ((SfwOracleProductDetail) sfwListProducts.getSelectedValue()).getProduct().get_label()));
                menuItRetirar.setBackground(new Color(229, 229, 229));
                menuItRetirar.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mousePressed(MouseEvent e) {
                        SfwOracleProductDetail v_sfworacleproduct_detal_selected;
                        SfwOracleDefineUser v_sfworacledefineuser_new;

                        if (Install.get_productsdetail().size() == 1) {
                            JOptionPane.showMessageDialog(null,
                                    Utils.getDefaultBundle(sfwWizardOracleValidator2).getString("JOptionPane.text.retirarTodosProdutos")
                                    + System.getProperty("line.separator")
                                    + Utils.getDefaultBundle(sfwWizardOracleValidator2).getString("JOptionPane.text.retornarTelaSelecao"),
                                    Utils.getDefaultBundle(sfwWizardOracleValidator2).getString("validationlabel"),
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        v_sfworacleproduct_detal_selected = ((SfwOracleProductDetail) sfwListProducts.getSelectedValue());
                        v_sfworacledefineuser_new = new SfwOracleDefineUser(v_sfworacleproduct_detal_selected.getProduct());
                        v_sfworacledefineuser_new.setUser(v_sfworacleproduct_detal_selected.getUser());
                        v_sfworacledefineuser_new.setPassword(v_sfworacleproduct_detal_selected.getPassword());

                        Install.get_productdefine().add(v_sfworacledefineuser_new);
                        Install.get_productsdetailinstall().remove(v_sfworacleproduct_detal_selected);
                        Install.get_productsdetail().remove(v_sfworacleproduct_detal_selected);

                        sfwListProducts.repaint();
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });

                if (contextMenu.isPopupTrigger(e)) {
                    contextMenu.add(menuItRetirar);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                mousePressed(e); // cheesy hack.
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        sfwPageTitle = new sfwinstaladorscript.components.SfwWizardPageTitle();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        sfwListProducts = new sfwinstaladorscript.components.SfwList();
        jButtonValidate = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(599, 372));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwWizardOracleValidator2.class);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextAreaLog.setBackground(resourceMap.getColor("jTextAreaLog.background")); // NOI18N
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setEditable(false);
        jTextAreaLog.setFont(resourceMap.getFont("jTextAreaLog.font")); // NOI18N
        jTextAreaLog.setLineWrap(true);
        jTextAreaLog.setRows(5);
        jTextAreaLog.setName("jTextAreaLog"); // NOI18N
        jScrollPane1.setViewportView(jTextAreaLog);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
        );

        sfwPageTitle.setName("sfwPageTitle"); // NOI18N
        sfwPageTitle.setSubtitulo(resourceMap.getString("sfwPageTitle.subtitulo")); // NOI18N
        sfwPageTitle.setTitulo(resourceMap.getString("sfwPageTitle.titulo")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"), resourceMap.getColor("jPanel1.border.titleColor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        sfwListProducts.setBackground(resourceMap.getColor("sfwListProducts.background")); // NOI18N
        sfwListProducts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sfwListProducts.setFixedCellHeight(20);
        sfwListProducts.setName("sfwListProducts"); // NOI18N
        jScrollPane3.setViewportView(sfwListProducts);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getActionMap(SfwWizardOracleValidator2.class, this);
        jButtonValidate.setAction(actionMap.get("validateClick")); // NOI18N
        jButtonValidate.setText(resourceMap.getString("jButtonValidate.text")); // NOI18N
        jButtonValidate.setName("jButtonValidate"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jButtonValidate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .add(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonValidate)
                .add(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * ImplementaÃ§Ã£o de setup inicial da pÃ¡gina.
     * @param view Frame principal da aplicaÃ§Ã£o.
     */
    public void flowSetup(SfwInstaladorScriptView view) {
        view.getBackButton().setEnabled(true);
        view.getNextButton().setEnabled(false);
        this.sfwListProducts.bind(Install.get_productsdetail(), new SfwOracleProductDetail.SfwOracleProductDetailComparator());
    }

    public void nextClick(Hashtable wzPages, SfwInstaladorScriptView view) {

        if (Install.get_productsdetailinstall().size() > 0) {
            view.setWizardPage((JPanel) wzPages.get("wzInstall"));
        } else {
            JOptionPane.showMessageDialog(null,
                    Utils.getDefaultBundle(this).getString("JOptionPane.text.naoProsseguirInstalacao")
                    + System.getProperty("line.separator")
                    + Utils.getDefaultBundle(this).getString("JOptionPane.text.produtosUtilizados"),
                    Utils.getDefaultBundle(this).getString("JOptionPane.text.erro"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void backClick(Hashtable wzPages, SfwInstaladorScriptView view) {

        if (this._validatebase != null && (this._validatebase.isPending() || this._validatebase.isStarted())) {
            Utils.setDefaultCursor();
            this._validatebase.cancel(false);
            _view.getBusyIconTimer().stop();
            _view.getStatusAnimationLabel().setIcon(_view.getIdleIcon());
            _view.getStatusAnimationLabel().setText("");
        }

        this.jTextAreaLog.setText("");
        view.setWizardPage((JPanel) wzPages.get("wzValidator"));
    }

    @Action
    public void validateClick() {

        final String v_string_msg_alreadyinstalled;
        TaskMonitor taskMonitor = new TaskMonitor(this._view.getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    _view.getBusyIconTimer().start();
                    _view.getStatusAnimationLabel().setText(Utils.getDefaultBundle().getString("Validation.statusconnecting"));
                } else if ("done".equals(propertyName)) {
                    _view.getBusyIconTimer().stop();
                    _view.getStatusAnimationLabel().setIcon(_view.getIdleIcon());
                    _view.getStatusAnimationLabel().setText("");
                } else if ("message".equals(propertyName)) {
                } else if ("progress".equals(propertyName)) {
                }
            }
        });

        Install.get_productsdetailinstall().clear();

        this._oraconn = new OracleConnection();
        v_string_msg_alreadyinstalled = Utils.getDefaultBundle(this).getString("alreadyinstalled");

        this._validatebase = new Task(this._view.getApplication()) {

            @Override
            protected Object doInBackground() throws Exception {

                Iterator v_iterator_it;
                String v_string_vversion;
                boolean v_boolean_version_ok = false;
                boolean v_boolean_product_ok = false;
                SfwOracleProductDetail v_sfworacleproductdetail_pd;

                Utils.setBusyCursor();
                jTextAreaLog.setText("");
                _oraconn.set_tns(Install.get_tns());

                try {
                    v_iterator_it = Install.get_productsdetail().iterator();

                    _validation_ok = true;

                    while (v_iterator_it.hasNext()) {
                        v_boolean_version_ok = true;
                        v_sfworacleproductdetail_pd = (SfwOracleProductDetail) v_iterator_it.next();

                        jTextAreaLog.setText(jTextAreaLog.getText() + v_sfworacleproductdetail_pd.getProduct().get_label() + ":" + System.getProperty("line.separator") + "--------------------------------------------" + System.getProperty("line.separator"));

                        // valida versao selecionada para instalação.
                        try {
                            v_string_vversion = v_sfworacleproductdetail_pd.validateVersion();
                            if (!v_string_vversion.equals("") && !v_string_vversion.equals("EQUAL")) {
                                _validation_ok = false;
                                v_boolean_version_ok = false;
                                jTextAreaLog.setText(jTextAreaLog.getText() + v_string_vversion + System.getProperty("line.separator") + System.getProperty("line.separator"));
                            } else if (v_string_vversion.equals("EQUAL")) {
                                v_boolean_version_ok = false;
                                jTextAreaLog.setText(jTextAreaLog.getText() + "" + String.format(v_string_msg_alreadyinstalled, System.getProperty("line.separator"), System.getProperty("line.separator") + "" + System.getProperty("line.separator")));
                            }
                        } catch (Exception ex) {
                            _validation_ok = false;
                            v_boolean_version_ok = false;
                            jTextAreaLog.setText(jTextAreaLog.getText() + System.getProperty("line.separator") + ex.getMessage());
                        }

                        // realiza demais validações do produto.
                        if (v_boolean_version_ok) {
                            v_boolean_product_ok = ValidateProduct(v_sfworacleproductdetail_pd);
                            if (!v_boolean_product_ok) {
                                if (Utils.isObjects_validade()) {
                                    _validation_ok = false;
                                }
                            }
                        }

                        // se estão tudo certo, estão produto estão OK para continuar
                        if (v_boolean_version_ok && v_boolean_product_ok) {
                            jTextAreaLog.setText(jTextAreaLog.getText() + "OK!" + System.getProperty("line.separator"));
                        }

                        jTextAreaLog.setText(jTextAreaLog.getText() + System.getProperty("line.separator") + System.getProperty("line.separator"));
                    }

                    jButtonValidate.setEnabled(true);

                    // se todos os produtos estão ok, pode continuar com instalação.
                    if (_validation_ok) {
                        _view.getNextButton().setEnabled(true);
                    } else {
                        _view.getNextButton().setEnabled(false);
                    }


                    _oraconn.Close();
                    Utils.setDefaultCursor();

                    return null;

                } catch (Exception ex) {
                    Utils.setDefaultCursor();
                    jButtonValidate.setEnabled(true);
                    _view.getNextButton().setEnabled(false);
                    _oraconn.Close();
                    ex.printStackTrace();
                    jTextAreaLog.setText(jTextAreaLog.getText() + System.getProperty("line.separator") + ex.getMessage());
                    return null;
                }
            }
        };

        taskMonitor.setForegroundTask(this._validatebase);
        jButtonValidate.setEnabled(false);
        this._validatebase.execute();
    }

    /**
     * Valida se produto estão OK para instalação.
     * @param pd Detalhes do produto.
     * @return TRUE- está tudo certo, FALSE- não está certo.
     */
    private boolean ValidateProduct(SfwOracleProductDetail pd) {
        boolean v_boolean_product_ok;
        String v_string_msg_tablespace;
        BaseCompare v_base_origem;
        BaseCompare v_base_destino;
        this._oraconn.set_username(pd.getUser());
        this._oraconn.set_password(pd.getPassword());

        try {
            this._oraconn.Connect();

            v_boolean_product_ok = true;
            v_string_msg_tablespace = Utils.getDefaultBundle(this).getString("tablespacenotexists");
            //
            if (!this._oraconn.TablespaceExists(pd.getData4k())) {
                jTextAreaLog.setText(jTextAreaLog.getText() + "" + String.format(v_string_msg_tablespace, pd.getData4k()) + "" + System.getProperty("line.separator"));
                this._validation_ok = false;
                v_boolean_product_ok = false;
            }
            //
            if (!this._oraconn.TablespaceExists(pd.getIndex4k())) {
                jTextAreaLog.setText(jTextAreaLog.getText() + "" + String.format(v_string_msg_tablespace, pd.getIndex4k()) + "" + System.getProperty("line.separator"));
                this._validation_ok = false;
                v_boolean_product_ok = false;
            }
            //
            /*if (!pd.getProduct().get_code().equals("500")) {
                SfwComparaObjetos compara = new SfwComparaObjetos();
                if (v_boolean_product_ok) {
                    File caminho = null;
                    try {
                        caminho = new File("comparacao\\" + new SimpleDateFormat("yyyyMMddk").format(new Date()));
                        if (!caminho.exists()) {
                            caminho.mkdirs();
                        }
                        v_base_origem = this.returnObjectsFromXML(pd, _oraconn);
                        v_base_destino = this.returnObjectsFromDatabase(pd, _oraconn);
                        if (v_base_origem != null && v_base_destino != null) {
                            compara.DataBaseCompare(v_base_origem, v_base_destino, "comparacao\\" + new SimpleDateFormat("yyyyMMddk").format(new Date()));
                        }
                    } catch (Exception e) {
                        Utils.setDefaultCursor();
                        e.printStackTrace();
                    } finally {
                        Utils.limpaPasta("comparacao");
                        new File("comparacao").delete();
                    }

                    //
                    this.GeraObjetoDiferente(compara.getCompareErro(), _oraconn, pd);
                    //
                    if (!compara.getCompareErro().isEmpty()) {
                        for (int i = 0; i < compara.getCompareErro().size(); i++) {
                            CompareErro erros = compara.getCompareErro().get(i);
                            jTextAreaLog.setText(jTextAreaLog.getText() + erros.getObject_type() + " " + erros.getObject_erro() + ": " + erros.getObject_name() + System.getProperty("line.separator"));
                        }
                        v_boolean_product_ok = false;
                    }
                }
            }
            //
            this._oraconn.Close();*/
            //
            return v_boolean_product_ok;

        } catch (SQLException ex) {
            ex.printStackTrace();
            jTextAreaLog.setText(jTextAreaLog.getText() + ex.getMessage() + System.getProperty("line.separator"));
            return false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonValidate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaLog;
    private sfwinstaladorscript.components.SfwList sfwListProducts;
    private sfwinstaladorscript.components.SfwWizardPageTitle sfwPageTitle;
    // End of variables declaration//GEN-END:variables

    /**
     * 
     * @param source
     * @param erro
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void SalvaArquivoDiferente(StringBuilder source, CompareErro erro, SfwOracleProductDetail pd) throws FileNotFoundException, IOException {
        BufferedWriter bw;
        OutputStreamWriter osw;
        FileOutputStream fos;
        File object;
        File pasta;
        //Verifica se a pasta para salvar a comparação existe.
        pasta = new File("comparacaoArquivos\\" + pd.getProduct().get_label() + "\\" + pd.getProduct().getSelectedversion() + "\\" + erro.getObject_type());
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        object = new File(pasta + "\\" + erro.getObject_name() + ".sql");


        if (!object.exists()) {
            object.createNewFile();
        }
        // Escreve o objeto.
        fos = new FileOutputStream(object);
        osw = new OutputStreamWriter(fos);
        bw = new BufferedWriter(osw);
        //
        bw.write(source.toString());
        bw.close();
    }

    /**
     * 
     */
    private void GeraObjetoDiferente(List<CompareErro> erros, OracleConnection con, SfwOracleProductDetail pd) {
        StringBuilder object;
        try {
            object = new StringBuilder();
            for (int i = 0; i < erros.size(); i++) {
                object.delete(0, object.length());
                CompareErro compareErro = erros.get(i);
                if (compareErro.getObject_type().equals("PROCEDURE")
                        || compareErro.getObject_type().equals("PACKAGE BODY")
                        || compareErro.getObject_type().equals("PACKAGE")
                        || compareErro.getObject_type().equals("TRIGGER")
                        || compareErro.getObject_type().equals("FUNCTION")
                        || compareErro.getObject_type().equals("VIEW")) {
                    // Se for view faz select na user_views.
                    if (compareErro.getObject_type().equals("VIEW")) {
                        object = this.returnObjectCode("select text "
                                + "from user_views "
                                + "where view_name = '" + compareErro.getObject_name() + "'", con);
                    } else {
                        // se não faz select na user_source.
                        object = this.returnObjectCode("select text "
                                + "from user_source "
                                + "where name = '" + compareErro.getObject_name() + "' "
                                + "order by line", con);
                    }
                    // Método que grava o arquivo.
                    this.SalvaArquivoDiferente(object, compareErro, pd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param query
     * @param con
     * @return
     */
    private StringBuilder returnObjectCode(String query, OracleConnection con) {
        StringBuilder object = null;
        ResultSet rs;
        try {
            object = new StringBuilder();
            rs = con.Query(query);
            while (rs.next()) {
                object.append(rs.getString("text"));
            }
            con.CloseResultSet();
            con.CloseStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     *
     * @param product
     * @param con
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    private BaseCompare returnObjectsFromDatabase(SfwOracleProductDetail product, OracleConnection con) throws IOException, JDOMException {

        BaseCompare v_qtdeObjectsFromXML = new BaseCompare();
        SfwXMLBuilder builder = new SfwXMLBuilder(product.getUser(), product.getPassword(), Install.get_tns());
        SfwXMLReader xmlReader = new SfwXMLReader();
        //
        Info v_info = new Info();
        v_info.setVERSAO(product.getProduct().getSelectedversion());
        v_info.setCOD_PRODUTO(Integer.parseInt(product.getCodSistema()));
        //
        String arquivoCompare = "comparacao\\comparacao_" + v_info.getVERSAO() + ".xml";
        //
        builder.WriteXML(v_info, arquivoCompare);
        //
        v_qtdeObjectsFromXML = xmlReader.ReadXML(arquivoCompare);

        return v_qtdeObjectsFromXML;
    }

    /**
     * Retorna os objetos do XML para comparação
     * @param product
     * @param con
     * @return
     * @throws Exception
     */
    private BaseCompare returnObjectsFromXML(SfwOracleProductDetail product, OracleConnection con) throws Exception {
        BaseCompare v_qtdeObjectsFromXML = new BaseCompare();
        ArrayList v_arraylist_pkg_product;
        Iterator v_iterator_percorrerVersao;
        Version v_version_versao = null;
        String versao = "", release = "", patch = "", build = "";
        SfwXMLReader xmlReader = new SfwXMLReader();
        SfwCompareProduct _compareProduct = new SfwCompareProduct();
        boolean v_boolean_base_comparer = false;
        try {
            try {
                ResultSet rs = con.Query("SELECT VERSAO, RELEASE, PATCH, BUILD FROM SFW_SISTEMA_VERSAO WHERE VALIDO = 'S' AND COD_SISTEMA = '" + product.getCodSistema() + "' and ROWNUM = 1");
                while (rs.next()) {
                    versao = Utils.nvl(rs.getString("VERSAO"), "");
                    release = Utils.nvl(rs.getString("RELEASE"), "");
                    patch = Utils.nvl(rs.getString("PATCH"), "");
                    build = Utils.nvl(rs.getString("BUILD"), "");
                }
            } catch (Exception e) {
                ResultSet rs = con.Query("SELECT VERSAO, RELEASE, PATCH FROM SFW_SISTEMA_VERSAO WHERE VALIDO = 'S' AND COD_SISTEMA = '" + product.getCodSistema() + "' and ROWNUM = 1");
                while (rs.next()) {
                    versao = Utils.nvl(rs.getString("VERSAO"), "");
                    release = Utils.nvl(rs.getString("RELEASE"), "");
                    patch = Utils.nvl(rs.getString("PATCH"), "");
                }
            }
            v_arraylist_pkg_product = Install.get_packagelist().getProductVersions(product.getProduct().get_name());
            v_iterator_percorrerVersao = v_arraylist_pkg_product.iterator();
            while (v_iterator_percorrerVersao.hasNext()) {
                v_version_versao = (Version) v_iterator_percorrerVersao.next();
                if (!build.equals("")) {
                    if (v_version_versao.getVersionNumber() == Integer.parseInt(versao) && v_version_versao.getReleaseNumber() == Integer.parseInt(release) && v_version_versao.getPatchNumber() == Integer.parseInt(patch) && v_version_versao.getBuildNumber() == Integer.parseInt(build)) {
                        for (int i = 0; i < v_version_versao.get_xmlversion().getDiffArray().length; i++) {
                            if (v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                _compareProduct.setCaminho(v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                _compareProduct.setPacote(v_version_versao.get_packagename());
                                _compareProduct.setVersao(v_version_versao);
                                v_boolean_base_comparer = true;
                                break;
                            } else {
                                v_boolean_base_comparer = false;
                            }
                        }
                        if (v_boolean_base_comparer == false) {
                            for (int i = 0; i < v_version_versao.get_xmlversion().getFullArray().length; i++) {
                                if (v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                    v_boolean_base_comparer = true;
                                    _compareProduct.setCaminho(v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                    _compareProduct.setPacote(v_version_versao.get_packagename());
                                    _compareProduct.setVersao(v_version_versao);
                                    break;
                                } else {
                                    v_boolean_base_comparer = false;
                                }
                            }
                        }
                    }
                } else {
                    if (v_version_versao.getVersionNumber() == Integer.parseInt(versao) && v_version_versao.getReleaseNumber() == Integer.parseInt(release) && v_version_versao.getPatchNumber() == Integer.parseInt(patch) && v_version_versao.getBuildNumber() == Integer.parseInt(build)) {
                        for (int i = 0; i < v_version_versao.get_xmlversion().getDiffArray().length; i++) {
                            if (v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                _compareProduct.setCaminho(v_version_versao.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                _compareProduct.setPacote(v_version_versao.get_packagename());
                                _compareProduct.setVersao(v_version_versao);
                                v_boolean_base_comparer = true;
                                break;
                            } else {
                                v_boolean_base_comparer = false;
                            }
                        }
                        if (v_boolean_base_comparer == false) {
                            for (int i = 0; i < v_version_versao.get_xmlversion().getFullArray().length; i++) {
                                if (v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1) {
                                    v_boolean_base_comparer = true;
                                    _compareProduct.setCaminho(v_version_versao.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray()[0].getPath());
                                    _compareProduct.setPacote(v_version_versao.get_packagename());
                                    _compareProduct.setVersao(v_version_versao);
                                    break;
                                } else {
                                    v_boolean_base_comparer = false;
                                }
                            }
                        }
                    }
                }
            }
            _compareProduct.setProduto(product.getProduct());

            File v_file_extractcompare = new File("temp");
            if (!v_file_extractcompare.exists()) {
                v_file_extractcompare.mkdirs();
            }

            this.Extraircomparacao(Install.get_packagelist().getPackageByName(_compareProduct.getPacote()).get_zip(), v_file_extractcompare, _compareProduct.getCaminho(), _compareProduct.getProduto().get_folder());

            v_qtdeObjectsFromXML = xmlReader.ReadXML("temp\\comparacao.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v_qtdeObjectsFromXML;
    }

    /**
     * Funcao para extrair apenas o arquivo .sql de compara??o
     * @param zipfile PKG do produto selecionado
     * @param extractionpoint Ponto de extração da compara??o
     * @param caminhoComp Caminho da comparação no XML
     * @param productfolder Nome da pasta do produto
     * @param user usuário do produto selecionado para conectar na base
     * @param pass senha do produto selecionado para conectar na base
     * @param tns tns do produto selecionado para conectar na base
     * @

    throws FileNotFoundException
     * @


    throws IOException



     */
    public void Extraircomparacao(ZipFile zipfile, File extractionpoint, String caminhoComp, String productfolder) throws FileNotFoundException, IOException {

        String v_s_caminhoComparaBase = productfolder + caminhoComp;

        //Verifica todos arquivos do ZIP
        for (Enumeration entries = zipfile.entries(); entries.hasMoreElements();) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            //comparação de cada arquivo zip com o arquivo de comparação
            if (zipEntry.getName().replaceAll("/", "\\\\").equals(v_s_caminhoComparaBase)) {
                String zipEntryName = zipEntry.getName().replaceAll("/", "\\\\");

                //Cria o arquivo de compara??o
                File _compBaseSQL = new File(extractionpoint.getAbsolutePath() + System.getProperty("file.separator") + zipEntryName.substring(zipEntryName.lastIndexOf(System.getProperty("file.separator")), zipEntryName.length()));

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
}
