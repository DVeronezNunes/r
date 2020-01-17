/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwShowLogPage.java
 *
 * Created on May 12, 2011, 11:32:36 AM
 */
package sfwinstaladorscript.showlogs;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author jopaulo
 */
public class SfwShowLogPage extends javax.swing.JDialog {

    private DefaultMutableTreeNode root;
    int v_int_posInicial = 0;
    String v_string_pesquisa;
    int v_int_res;
    private int v_int_ora = 0;
    private int v_int_sp2 = 0;

    /** Creates new form SfwShowLogPage */
    public SfwShowLogPage(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("tr_ogt.png"));

        jTreeLog.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeLog.getLastSelectedPathComponent();
                if (node != null && node.isLeaf()) {
                    TreePath tp = e.getNewLeadSelectionPath();
                    if (tp != null) {
                        try {
                            jTextPaneLog.setText("");
                            jTextPaneLog.setText(LeArquivoLog(node.getParent().toString() + System.getProperty("file.separator") + tp.getLastPathComponent()).toString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        root = new DefaultMutableTreeNode("Lista de Logs", true);
        File v_file_root = new File("log");
        if (!v_file_root.exists()){
            v_file_root.mkdirs();
        }
        getList(root, v_file_root);
        jTreeLog = new javax.swing.JTree(root);
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneLog = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldBuscar = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldOra = new javax.swing.JTextField();
        jTextFieldSp2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwShowLogPage.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setIconImage(null);
        setName("Form"); // NOI18N
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"), resourceMap.getColor("jPanel1.border.titleColor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTreeLog.setName("jTreeLog"); // NOI18N
        jScrollPane1.setViewportView(jTreeLog);
        ImageIcon leafIcon = new ImageIcon("icon/log.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer =
            new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            jTreeLog.setCellRenderer(renderer);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextPaneLog.setColumns(20);
        jTextPaneLog.setEditable(false);
        jTextPaneLog.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTextPaneLog.setRows(5);
        jTextPaneLog.setName("jTextPaneLog"); // NOI18N
        jScrollPane2.setViewportView(jTextPaneLog);

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldBuscar.setText(resourceMap.getString("jTextFieldBuscar.text")); // NOI18N
        jTextFieldBuscar.setName("jTextFieldBuscar"); // NOI18N

        jButtonBuscar.setText(resourceMap.getString("jButtonBuscar.text")); // NOI18N
        jButtonBuscar.setName("jButtonBuscar"); // NOI18N
        jButtonBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonBuscarMouseClicked(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldOra.setEditable(false);
        jTextFieldOra.setFont(resourceMap.getFont("jTextFieldOra.font")); // NOI18N
        jTextFieldOra.setForeground(resourceMap.getColor("jTextFieldOra.foreground")); // NOI18N
        jTextFieldOra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldOra.setText(resourceMap.getString("jTextFieldOra.text")); // NOI18N
        jTextFieldOra.setName("jTextFieldOra"); // NOI18N

        jTextFieldSp2.setEditable(false);
        jTextFieldSp2.setFont(resourceMap.getFont("jTextFieldSp2.font")); // NOI18N
        jTextFieldSp2.setForeground(resourceMap.getColor("jTextFieldSp2.foreground")); // NOI18N
        jTextFieldSp2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldSp2.setText(resourceMap.getString("jTextFieldSp2.text")); // NOI18N
        jTextFieldSp2.setName("jTextFieldSp2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setForeground(resourceMap.getColor("jLabel4.foreground")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jButtonBuscar))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldOra, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSp2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldOra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jTextFieldSp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBuscarMouseClicked
        v_string_pesquisa = "";
        v_string_pesquisa = jTextFieldBuscar.getText().toUpperCase();

        if (v_string_pesquisa != null && !v_string_pesquisa.equals("")) {
            v_int_res = jTextPaneLog.getText().toUpperCase().indexOf(v_string_pesquisa, v_int_posInicial);

            if (v_int_res < 0) {
                JOptionPane.showMessageDialog(null, "Texto não encontrado");
                v_int_posInicial = 0;
            } else {
                jTextPaneLog.requestFocus();
                jTextPaneLog.select(v_int_res, v_int_res + v_string_pesquisa.length());
                v_int_posInicial = v_int_res + v_string_pesquisa.length();
            }
        }
    }//GEN-LAST:event_jButtonBuscarMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JTextField jTextFieldOra;
    private javax.swing.JTextField jTextFieldSp2;
    private javax.swing.JTextArea jTextPaneLog;
    private javax.swing.JTree jTreeLog;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings({"rawtypes", "unchecked", "UseOfObsoleteCollectionType"})
    private void getList(DefaultMutableTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (curPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = curPath + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {
                getList(curDir, f);
            } else {
                files.addElement(thisObject);
            }
        }
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++) {
            curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
        }
    }

    /**
     * 
     * @param caminho
     * @return
     * @throws Exception 
     */
    private StringBuilder LeArquivoLog(String caminho) throws Exception {
        StringBuilder v_log;
        v_log = new StringBuilder();
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(caminho)), "ISO-8859-1"));
        String linha;
        v_int_ora = 0;
        v_int_sp2 = 0;
        while ((linha = buffReader.readLine()) != null) {
            v_log.append(linha).append("\r\n");
            if (linha.contains("ORA-")) {
                v_int_ora = v_int_ora + 1;
            } else if (linha.contains("SP2-")) {
                v_int_sp2 = v_int_sp2 + 1;
            }
        }
        buffReader.close();
        jTextFieldOra.setText(String.valueOf(v_int_ora));
        jTextFieldSp2.setText(String.valueOf(v_int_sp2));
        return v_log;
    }
}
