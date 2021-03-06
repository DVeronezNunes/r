/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwOracleFrameProductDetail.java
 *
 * Created on 13/11/2008, 17:59:41
 */

package sfwinstaladorscript.oracleinstallation;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JPanel;
import org.jdesktop.application.Action;

/**
 * Janela para alteração da configuração dos detalhes do produto.
 */
public class SfwOracleFrameProductDetail extends javax.swing.JFrame {

    private SfwOracleProductDetail _current;
    private String _user;
    private String _password;
    private String _tbldata4k;
    private String _tbldata64k;
    private String _tbldata128k;
    private String _tbldata512k;
    private String _tbldata1m;
    private String _tblindex4k;
    private String _tblindex64k;
    private String _tblindex128k;
    private String _tblindex512k;
    private String _tblindex1m;
    private String _codsistema;


    /** Creates new form SfwOracleFrameProductDetail */
    public SfwOracleFrameProductDetail()
    {
        initComponents();

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("softway.gif"));
    }

    public SfwOracleFrameProductDetail(SfwOracleProductDetail pd) {
        initComponents();

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("softway.gif"));

        this._current = pd;
        this.jPanelDetail.add((JPanel) this._current);
        this.setTitle(this._current.getProduct().get_label());
        this.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                cancelClick();
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });

        this._user = this._current.getUser();
        this._password = this._current.getPassword();
        this._codsistema = this._current.getCodSistema();
        this._tbldata4k = this._current.getData4k();
        this._tblindex4k = this._current.getIndex4k();
    }

    @Action
    public void cancelClick()
    {
        this._current.setUser(this._user);
        this._current.setPassword(this._password);
        this._current.setCodSistema(this._codsistema);
        this._current.setData4k(this._tbldata4k);
        this._current.setIndex4k(this._tblindex4k);
        this.setVisible(false);
    }

    @Action
    public void okClick()
    {
        if(this._current.Validate())
            this.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelButtons = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanelDetail = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        jPanelButtons.setName("jPanelButtons"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getActionMap(SfwOracleFrameProductDetail.class, this);
        jButtonOk.setAction(actionMap.get("okClick")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwOracleFrameProductDetail.class);
        jButtonOk.setText(resourceMap.getString("jButtonOk.text")); // NOI18N
        jButtonOk.setName("jButtonOk"); // NOI18N

        jButtonCancel.setAction(actionMap.get("cancelClick")); // NOI18N
        jButtonCancel.setText(resourceMap.getString("jButtonCancel.text")); // NOI18N
        jButtonCancel.setName("jButtonCancel"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanelButtonsLayout = new org.jdesktop.layout.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel)
                .add(118, 118, 118))
        );
        jPanelButtonsLayout.setVerticalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelButtonsLayout.createSequentialGroup()
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonOk)
                    .add(jButtonCancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDetail.setName("jPanelDetail"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanelDetailLayout = new org.jdesktop.layout.GroupLayout(jPanelDetail);
        jPanelDetail.setLayout(jPanelDetailLayout);
        jPanelDetailLayout.setHorizontalGroup(
            jPanelDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 413, Short.MAX_VALUE)
        );
        jPanelDetailLayout.setVerticalGroup(
            jPanelDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 316, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanelDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanelDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelDetail;
    // End of variables declaration//GEN-END:variables

}
