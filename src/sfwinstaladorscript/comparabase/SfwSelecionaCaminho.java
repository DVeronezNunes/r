package sfwinstaladorscript.comparabase;

import javax.swing.*;
import sfwinstaladorscript.SfwInstaladorScriptApp;
import sfwinstaladorscript.Utils;

/**
 *
 * @author jopaulo
 */
public class SfwSelecionaCaminho extends javax.swing.JFrame {

    final SfwSelecionaCaminho sfwSelecionaCaminho  = this;

    /** Creates new form JFrameFileEditor */
    public SfwSelecionaCaminho() {
        initComponents();
    }
    /**
     * FileChooser para escolha do caminho para salvar a comparação
     * @return String com o caminho selecionado
     */
    public static String escolheArquivo() {

        SfwSelecionaCaminho SfwSelecionaCaminho = new SfwSelecionaCaminho();
        String arquivoSelecionado;
        JFrame mainFrame = SfwInstaladorScriptApp.getApplication().getMainFrame();
        JFileChooser arquivo = new JFileChooser();
        arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        arquivo.setDialogTitle(Utils.getDefaultBundle(SfwSelecionaCaminho).getString("JSelecionaCaminho.approveButtonToolTipText"));
        arquivo.setApproveButtonText(Utils.getDefaultBundle(SfwSelecionaCaminho).getString("JSelecionaCaminho.approveButtonText"));
        arquivo.setApproveButtonToolTipText(Utils.getDefaultBundle(SfwSelecionaCaminho).getString("JSelecionaCaminho.approveButtonToolTipText"));

        if (arquivo.showSaveDialog(mainFrame) == JFileChooser.CANCEL_OPTION) {
            arquivoSelecionado = "";
        } else {
            arquivoSelecionado = arquivo.getSelectedFile().getPath();
        }
        return arquivoSelecionado;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JSelecionaCaminho = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwSelecionaCaminho.class);
        JSelecionaCaminho.setApproveButtonToolTipText(resourceMap.getString("JSelecionaCaminho.approveButtonToolTipText")); // NOI18N
        JSelecionaCaminho.setDialogTitle(resourceMap.getString("JSelecionaCaminho.dialogTitle")); // NOI18N
        JSelecionaCaminho.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        JSelecionaCaminho.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        JSelecionaCaminho.setName("JSelecionaCaminho"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(JSelecionaCaminho, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(JSelecionaCaminho, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 390, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        JSelecionaCaminho.getAccessibleContext().setAccessibleName(resourceMap.getString("jFileChooser1.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser JSelecionaCaminho;
    // End of variables declaration//GEN-END:variables
}
