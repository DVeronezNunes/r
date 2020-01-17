package sfwinstaladorscript.oracleinstallation;

import java.awt.GridLayout;
import java.util.List;
import sfwinstaladorscript.objects.ProductCompatibility;

public class SfwWizardProductCompatibility extends javax.swing.JPanel {

    public List<ProductCompatibility> _compatibility;

    /** Creates new form SfwWizardProductCompatibility */
    public SfwWizardProductCompatibility(List<ProductCompatibility> compatibility) {
        this._compatibility = compatibility;
        initComponents();
        jPanel1.setLayout(new GridLayout(6, 1));
        for (int i = 0; i < compatibility.size(); i++) {
            ProductCompatibility productCompatibility = compatibility.get(i);
            SfwWizardProductCompatibility2 v_productcomp = new SfwWizardProductCompatibility2(productCompatibility);
            jPanel1.add(v_productcomp);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(370, 300));
        setLayout(null);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(420, 360));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 360, 300);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
