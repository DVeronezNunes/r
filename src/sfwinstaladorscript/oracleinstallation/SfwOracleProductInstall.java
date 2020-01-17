/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwOracleProductInstall.java
 *
 * Created on 19/11/2008, 13:02:08
 */
package sfwinstaladorscript.oracleinstallation;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.SfwInstaladorScriptView;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.oracleinstallation.components.SfwOraclePackageInstall;

/**
 * Painel de instalação de pacotes de um produto.
 */
public class SfwOracleProductInstall extends javax.swing.JPanel {

    private SfwWizardOracleInstall _wzoracleinstall;
    private ArrayList _pkgcomponent;
    private SfwOracleProductDetail _productdetail;
    private int _currentindex = -1;
    private SfwInstaladorScriptView _view;
    private Task _installpackagetask;

    /** Creates new form SfwOracleProductInstall */
    public SfwOracleProductInstall() {
        initComponents();
    }

    public SfwOracleProductInstall(SfwOracleProductDetail pd, SfwWizardOracleInstall woi, SfwInstaladorScriptView view) {
        Object v_object_obj;
        Iterator v_iterator_it;
        ArrayList v_arraylist_pkglist;
        ArrayList v_arraylist_pkgcomponent;

        initComponents();

        this._productdetail = pd;
        this._wzoracleinstall = woi;
        this._view = view;

        v_arraylist_pkgcomponent = new ArrayList();
        v_arraylist_pkglist = (ArrayList) Install.get_productpackageinstall().get(pd.getProduct().get_name());
        v_iterator_it = v_arraylist_pkglist.iterator();
        while (v_iterator_it.hasNext()) {
            v_object_obj = v_iterator_it.next();

            if (v_object_obj.getClass().getName().equals("productinfo.impl.DiffImpl")) {
                v_arraylist_pkgcomponent.add(0, new sfwinstaladorscript.oracleinstallation.components.SfwOraclePackageInstall((productinfo.Diff) v_object_obj, pd));
            } else {
                v_arraylist_pkgcomponent.add(0, new sfwinstaladorscript.oracleinstallation.components.SfwOraclePackageInstall((productinfo.Full) v_object_obj, pd));
            }
        }

        this._pkgcomponent = v_arraylist_pkgcomponent;
        this.sfwListPackages.bind(this._pkgcomponent);
        this.nextPackageInstall();
    }

    public SfwOracleProductInstall(SfwOracleProductDetail pd, SfwWizardOracleInstall woi, SfwInstaladorScriptView view, boolean toggled) {
        this(pd, woi, view);
    }

    public int get_currentindex() {
        return _currentindex;
    }

    public ArrayList get_pkgcomponent() {
        return _pkgcomponent;
    }

    /**
     * Muda foco para o próximo pacote de instalação.
     */
    private void nextPackageInstall() {
        this._currentindex++;

        if (this._pkgcomponent.size() > this._currentindex) {
            this.sfwListPackages.setSelectedIndex(this._currentindex);
        } else if (this._pkgcomponent.size() == this._currentindex) {
            this._wzoracleinstall.nextProductInstall(false);
        }

    }

    /**
     * Instala pacote selecionado.
     * @return TRUE- instalou FALSE-não instalou
     */
    public boolean installPackage() {
        File v_file_log;

        try {

            Utils.setBusyCursor();

            //verifica se pasta do log existe, do contrario cria
            v_file_log = new File("log");
            if (!v_file_log.exists()) {
                v_file_log.mkdir();
            }

            ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).install();
            ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getJTextFieldStatus().setText(Utils.getDefaultBundle(this).getString("installed"));
            ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getJTextFieldStatus().setForeground(new Color(0, 150, 0));

            nextPackageInstall();

            Utils.setDefaultCursor();
            return true;

        } catch (Exception ex) {
            Utils.setDefaultCursor();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    Utils.getDefaultBundle(this).getString("JOptionPane.text.erroInstalarPacote")
                    + ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getPackageName()
                    + System.getProperty("line.separator")
                    + Utils.getDefaultBundle(this).getString("JOptionPane.text.error")
                    + ex.getMessage(),
                    Utils.getDefaultBundle(this).getString("JOptionPane.text.erro"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Instala pacote selecionado com task.
     */
    public void installPackageBackground() {
        TaskMonitor taskMonitor = new TaskMonitor(this._view.getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    _view.getBusyIconTimer().start();
                    if (_pkgcomponent.size() > _currentindex) {
                        _view.getStatusAnimationLabel().setText(String.format(Utils.getDefaultBundle().getString("PackageInstall.installingpackage"), ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getPackageName()));
                    }
                } else if ("done".equals(propertyName)) {
                    _view.getBusyIconTimer().stop();
                    _view.getStatusAnimationLabel().setIcon(_view.getIdleIcon());
                    _view.getStatusAnimationLabel().setText("");
                } else if ("message".equals(propertyName)) {
                } else if ("progress".equals(propertyName)) {
                    if (_wzoracleinstall.isProductsFinished()) {
                        _view.getStatusAnimationLabel().setText(Utils.getDefaultBundle().getString("PackageInstall.finalvalidation"));
                    }
                }
            }
        });

        this._installpackagetask = new Task(this._view.getApplication()) {

            @Override
            protected Object doInBackground() throws Exception {

                Utils.setBusyCursor();

                if (installPackage()) {
                    this.firePropertyChange("progress", null, null);
                    //setInstallButtons();
                    _wzoracleinstall.CMschema();
                    _wzoracleinstall.SendLog();
                    _wzoracleinstall.executeFinalPackage();
                    _wzoracleinstall.setInstallButtons();
                } else {
                    //setInstallButtons();
                    _wzoracleinstall.setInstallButtons();

                    this.cancel(true);
                    Utils.setDefaultCursor();
                    return null;
                }

                Utils.setDefaultCursor();
                return null;
            }
        };

        taskMonitor.setForegroundTask(this._installpackagetask);
        this._wzoracleinstall.getJButtonInstallAll().setEnabled(false);
        this._installpackagetask.execute();
    }

    /**
     * Instala todos pacotes restantes.
     */
    public void installAllPackageBackground() {
        TaskMonitor taskMonitor = new TaskMonitor(this._view.getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    _view.getBusyIconTimer().start();
                    if (_pkgcomponent.size() > _currentindex) {
                        _view.getStatusAnimationLabel().setText(String.format(Utils.getDefaultBundle().getString("PackageInstall.installingpackage"), ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getPackageName()));
                    }
                } else if ("done".equals(propertyName)) {
                    _view.getBusyIconTimer().stop();
                    _view.getStatusAnimationLabel().setIcon(_view.getIdleIcon());
                    _view.getStatusAnimationLabel().setText("");
                } else if ("message".equals(propertyName)) {
                } else if ("progress".equals(propertyName)) {
                    if (_pkgcomponent.size() > _currentindex) {
                        _view.getStatusAnimationLabel().setText(String.format(Utils.getDefaultBundle().getString("PackageInstall.installingpackage"), ((SfwOraclePackageInstall) _pkgcomponent.get(_currentindex)).getPackageName()));
                    } else if (_pkgcomponent.size() == _currentindex) {
                        if (_wzoracleinstall.isProductsFinished()) {
                            _view.getStatusAnimationLabel().setText(Utils.getDefaultBundle().getString("PackageInstall.finalvalidation"));
                        }
                    }
                }
            }
        });

        this._installpackagetask = new Task(this._view.getApplication()) {

            @Override
            protected Object doInBackground() throws Exception {

                Utils.setBusyCursor();

                for (int i = _currentindex; i < _pkgcomponent.size(); i++) {
                    if (installPackage()) {
                        this.firePropertyChange("progress", null, null);
                    } else {
                        //setInstallButtons();
                        _wzoracleinstall.setInstallButtons();

                        this.cancel(true);
                        Utils.setDefaultCursor();
                        return null;
                    }
                }

                _wzoracleinstall.CMschema();
                _wzoracleinstall.SendLog();
                _wzoracleinstall.executeFinalPackage();
                _wzoracleinstall.setInstallButtons();
                Utils.setDefaultCursor();
                return null;
            }
        };

        taskMonitor.setForegroundTask(this._installpackagetask);
        this._wzoracleinstall.getJButtonInstallAll().setEnabled(false);
        this._installpackagetask.execute();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        sfwListPackages = new sfwinstaladorscript.components.SfwList();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(370, 247));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwOracleProductInstall.class);
        sfwListPackages.setBackground(resourceMap.getColor("sfwListPackages.background")); // NOI18N
        sfwListPackages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sfwListPackages.setEnabled(false);
        sfwListPackages.setName("sfwListPackages"); // NOI18N
        jScrollPane1.setViewportView(sfwListPackages);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .add(29, 29, 29))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void installClick() {

        this._view.getBackButton().setEnabled(false);
        this._view.getNextButton().setText(Utils.getDefaultBundle().getString("PackageInstall.buttonfinal"));
        this._view.getNextButton().setEnabled(false);

        this.installAllPackageBackground();
    }

    /**
     * Retorna se a instalação foi totalmente completada.
     * @return TRUE- finalizada FALSE- não finalizada
     */
    public boolean isFinished() {
        if (this._currentindex == this._pkgcomponent.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Altera estado dos botões de instalação.
     */
   /* public void setInstallButtons() {
        if (this.isFinished()) {
            this.jButtonInstall.setEnabled(false);
            this.jCheckBoxStep.setEnabled(false);
        } else {
            this.jButtonInstall.setEnabled(true);
            this.jCheckBoxStep.setEnabled(true);
        }
    }*/

    /**
     * Altera estado dos botões de instalação.
     */
   /* public void setInstallButtons(boolean enabled) {
        this.jButtonInstall.setEnabled(enabled);
        this.jCheckBoxStep.setEnabled(enabled);
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private sfwinstaladorscript.components.SfwList sfwListPackages;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the _productdetail
     */
    public SfwOracleProductDetail getProductdetail() {
        return _productdetail;
    }

    /**
     * @param productdetail the _productdetail to set
     */
    public void setProductdetail(SfwOracleProductDetail productdetail) {
        this._productdetail = productdetail;
    }
}
