package sfwinstaladorscript.xmlvalidation;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.application.Action;

import sfwinstaladorscript.Install;
import sfwinstaladorscript.SfwInstaladorScriptApp;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.objects.Database;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.SystemOS;
import sfwinstaladorscript.objects.Version;

/**
 * Tela para visualizar todos os pacotes disponíveis para um produto.
 */
public class SfwXMLValidatorProduct extends javax.swing.JDialog
{

    /**
     * Cor padrão dos pacotes FULL na tabela.
     */
    private static Color FullRowBackColor = new Color(255,255,255);

    /**
     * Cor padrão dos pacotes DIFF na tabela.
     */
    private static Color DiffRowBackColor = new Color(255,255,254);

    /**
     * Lista de produtos exibidos no combo para filtro.
     */
    private ArrayList _products;

    private ArrayList _systems;
    private ArrayList _databases;

    /**
     * Lista com as versões adicionadas na tabela (linha da tabela).
     */
    private ArrayList _rowproduct;

    private Hashtable _rows_diff;
    private Hashtable _rows_full;

    private JPopupMenu _context_menu;

    /**
     * Renderizador com funcionalidade de cor para a tabela de versões exibida.
     */
    private ColorColumnRenderer _colorcolumn;

    /**
     * Classe para implementar a funcionalidade de aplicar cor em uma linha da tabela.
     */
    class ColorColumnRenderer extends DefaultTableCellRenderer
    {
       Hashtable _background;

       public ColorColumnRenderer()
       {
          super();
          this._background = new Hashtable();
       }

       public void changeColor(Color backgroundcolorold, Color backgroundcolornew)
       {
           ArrayList v_arraylist_rowstopain;

           if(this._background.containsKey(backgroundcolorold))
           {
               v_arraylist_rowstopain = (ArrayList) this._background.get(backgroundcolorold);
               this._background.remove(backgroundcolorold);
               this._background.put(backgroundcolornew, v_arraylist_rowstopain);
           }
       }

       public void setRowstoPaint(Color backgroundcolor, ArrayList rowstopaint) {            
           this._background.put(backgroundcolor, rowstopaint);
       }

       @Override
       public Component getTableCellRendererComponent
            (JTable table, Object value, boolean isSelected,
             boolean hasFocus, int row, int column)
       {
          Color v_color_background;
          Enumeration v_enumeration_background;

          setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

          v_enumeration_background = this._background.keys();
          while(v_enumeration_background.hasMoreElements())
          {
              v_color_background = (Color) v_enumeration_background.nextElement();

              if(((ArrayList)this._background.get(v_color_background)).contains(row))
              {
                setBackground(isSelected ? table.getSelectionBackground(): v_color_background );
              }
          }

          setText(value !=null ? value.toString() : "");

          return this;
       }
    }

    public SfwXMLValidatorProduct(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }

    /** Creates new form SfwXMLValidatorProduct */
    public SfwXMLValidatorProduct(java.awt.Frame parent, boolean modal, ArrayList products, ArrayList systems, ArrayList databases)
    {       
        this(parent, modal);

        this._systems = systems;
        this._databases = databases;

        this.jScrollPane1.setViewportView(this.jTableVersion);
        this.jScrollPane1.getViewport().setBackground(Color.white);
        
        Iterator v_iterator_it;
        Product v_product_current;
        ArrayList v_arraylist_products = new ArrayList();        

        v_iterator_it = products.iterator();

        while(v_iterator_it.hasNext())
        {
            v_product_current = (Product) v_iterator_it.next();
            
            if(v_product_current.is_installable())
                v_arraylist_products.add(v_product_current);
        }

        this._products = v_arraylist_products;
        this.sfwComboBoxProduct.bind(this._products, new Product.ProductComparator());

        this._colorcolumn = new ColorColumnRenderer();
        this.jTableVersion.setDefaultRenderer(Object.class, this._colorcolumn);
        this.jTableVersion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //TODO menu de contexto para validação na tela de Validacao > Produto
        //this.buildTableContextMenu();
        /*this.jTableVersion.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mousePressed(MouseEvent e) {
                        if (_context_menu.isPopupTrigger (e))
                        {
                         _context_menu.show (e.getComponent (), e.getX (), e.getY ());
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        mousePressed (e); // cheesy hack.
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });*/

        this.jTextFieldCorFull.setBackground(SfwXMLValidatorProduct.FullRowBackColor);
        this.jTextFieldCorFull.invalidate();
        this.jTextFieldCorFull.repaint();

        this.jTextFieldCorDiff.setBackground(SfwXMLValidatorProduct.DiffRowBackColor);
        this.jTextFieldCorDiff.invalidate();
        this.jTextFieldCorDiff.repaint();

        this.sfwComboBoxProduct.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Color v_color_full;
                Product v_product_current;
                String v_string_base_comparer;
                Iterator v_iterator_pkg_product;
                Version v_version_current;
                ArrayList v_arraylist_pkg_product;
                DefaultTableModel v_defaultablemodel_pkg = (DefaultTableModel)jTableVersion.getModel();
                SfwXMLValidatorProduct v_sfwxmlvalidatorproduct_obj = new SfwXMLValidatorProduct(null, rootPaneCheckingEnabled);

                _rowproduct = new ArrayList();
                _rows_full = new Hashtable();
                _rows_diff = new Hashtable();

                v_product_current = (Product)sfwComboBoxProduct.getBindedSelectedItem();

                if(v_product_current == null)
                {
                    v_defaultablemodel_pkg.getDataVector().removeAllElements();
                    jTableVersion.repaint();
                    jTableVersion.invalidate();
                    return;
                }

                v_arraylist_pkg_product = Install.get_packagelist().getProductVersions(v_product_current.get_name());

                v_defaultablemodel_pkg.getDataVector().removeAllElements();
                v_iterator_pkg_product = v_arraylist_pkg_product.iterator();
                while(v_iterator_pkg_product.hasNext())
                {
                    v_version_current = (Version)v_iterator_pkg_product.next();

                    for(int i=0; i<v_version_current.get_xmlversion().getDiffArray().length; i++)
                    {
                        if(v_version_current.get_xmlversion().getDiffArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1)
                            v_string_base_comparer = Utils.getDefaultBundle(v_sfwxmlvalidatorproduct_obj).getString("yes");
                        else
                            v_string_base_comparer = Utils.getDefaultBundle(v_sfwxmlvalidatorproduct_obj).getString("no");

                        v_defaultablemodel_pkg.addRow(new Object[] {Install.get_packagelist().getPackageByName(v_version_current.get_packagename()).get_description().toLowerCase(), v_version_current.get_packagename()+"."+v_version_current.get_packageextesion(),v_version_current.get_description(),"DIFF",v_version_current.get_xmlversion().getDiffArray()[i].getName(),v_string_base_comparer});
                        _rowproduct.add(v_version_current);
                        _rows_diff.put(_rowproduct.size()-1, v_version_current.get_xmlversion().getDiffArray()[i]);
                    }

                    for(int i=0; i<v_version_current.get_xmlversion().getFullArray().length; i++)
                    {
                        if(v_version_current.get_xmlversion().getFullArray()[i].getOracleArray()[0].getWindowsArray()[0].getBasecomparerArray().length == 1)
                            v_string_base_comparer = Utils.getDefaultBundle(v_sfwxmlvalidatorproduct_obj).getString("yes");
                        else
                            v_string_base_comparer = Utils.getDefaultBundle(v_sfwxmlvalidatorproduct_obj).getString("no");

                        v_defaultablemodel_pkg.addRow(new Object[] {Install.get_packagelist().getPackageByName(v_version_current.get_packagename()).get_description().toLowerCase(), v_version_current.get_packagename()+"."+v_version_current.get_packageextesion(),v_version_current.get_description(),"FULL",v_version_current.get_xmlversion().getFullArray()[i].getName(),v_string_base_comparer});
                        _rowproduct.add(v_version_current);
                        _rows_full.put(_rowproduct.size()-1, v_version_current.get_xmlversion().getFullArray()[i]);
                    }
                }

                if(SfwXMLValidatorProduct.DiffRowBackColor.getRGB() == SfwXMLValidatorProduct.FullRowBackColor.getRGB())
                    v_color_full = new Color(SfwXMLValidatorProduct.FullRowBackColor.getRed(),SfwXMLValidatorProduct.FullRowBackColor.getGreen(),SfwXMLValidatorProduct.FullRowBackColor.getBlue()-1);
                else
                    v_color_full = SfwXMLValidatorProduct.FullRowBackColor;

                _colorcolumn.setRowstoPaint(SfwXMLValidatorProduct.DiffRowBackColor, new ArrayList(_rows_diff.keySet()));
                _colorcolumn.setRowstoPaint(v_color_full, new ArrayList(_rows_full.keySet()));
                jTableVersion.invalidate();
                jTableVersion.repaint();
            }
        });

        this.jTableVersion.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e)
            {
               if (e.getClickCount() == 2)
               {
                  Product v_product_current;
                  Version v_version_current;

                  if(((JTable)e.getSource()).getRowCount() > 0)
                  {
                      JTable target = (JTable)e.getSource();
                      int row = target.getSelectedRow();
                      productinfo.Version v_pv;

                      v_version_current = ((Version)_rowproduct.get(row));
                      v_product_current = (Product)sfwComboBoxProduct.getBindedSelectedItem();

                      v_pv = (productinfo.Version) v_version_current.get_xmlversion().copy();

                      for(int i=0; i<v_pv.getDiffArray().length; i++)
                      {
                         v_pv.getDiffArray()[i].unsetPkg();
                         v_pv.getDiffArray()[i].unsetName();

                         if(v_pv.getDiffArray()[i].isSetBasecomparer())
                            v_pv.getDiffArray()[i].unsetBasecomparer();
                      }

                      for(int i=0; i<v_pv.getFullArray().length; i++)
                      {
                         v_pv.getFullArray()[i].unsetPkg();
                         v_pv.getFullArray()[i].unsetName();

                         if(v_pv.getFullArray()[i].isSetBasecomparer())
                            v_pv.getFullArray()[i].unsetBasecomparer();
                      }

                      SfwXMLText v_sfwxmltext_tela;
                      JFrame mainFrame = SfwInstaladorScriptApp.getApplication().getMainFrame();
                      v_sfwxmltext_tela = new SfwXMLText(mainFrame,true);
                      v_sfwxmltext_tela.setTitle("XML da versão "+v_version_current.get_description()+" do "+v_product_current.get_label());
                      v_sfwxmltext_tela.setXMLText("  "+v_pv.xmlText().replaceAll("<prod:", "<").replaceAll("</prod:", "</").replaceAll("xml-fragment", "version").replaceAll(" xmlns:prod=\"http://productinfo\"", ""));
                      SfwInstaladorScriptApp.getApplication().show(v_sfwxmltext_tela);
                  }
               }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

    }

    /**
     * Constrói menu de contexto da tabela.
     */
    private void buildTableContextMenu()
    {
        Iterator v_iterator_it, v_iterator_it2;
        SystemOS v_systemos_current;
        Database v_database_current;
        JMenu menuValidar, menuOS, menuDB;
        JMenuItem menuItValidar;

        this._context_menu = new JPopupMenu();
        UIManager.put("MenuItem.selectionBackground", new Color(210, 211, 249));

        menuValidar = new JMenu("Validar Selecionado");
        menuValidar.setBackground(new Color(255, 255, 255));

        v_iterator_it = this._systems.iterator();
        final SfwXMLValidatorProduct sfwXMLValidatorProduct = this;
        while(v_iterator_it.hasNext())
        {
            final String v_string_os;
            
            v_systemos_current = (SystemOS)v_iterator_it.next();
            v_string_os = v_systemos_current.get_name();

            menuOS = new JMenu(v_systemos_current.get_description());
            menuOS.setBackground(new Color(255, 255, 255));
            menuValidar.add(menuOS);

            v_iterator_it2 = this._databases.iterator();
            while(v_iterator_it2.hasNext())
            {
                final String v_string_db;

                v_database_current = (Database)v_iterator_it2.next();
                v_string_db = v_database_current.get_name();

                menuDB = new JMenu(v_database_current.get_description());
                menuDB.setBackground(new Color(255, 255, 255));

                menuItValidar = new JMenuItem ("Validar!");
                menuItValidar.setBackground(new Color(255, 255, 255));
                menuItValidar.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mousePressed(MouseEvent e)
                    {
                        int v_int_message_type = JOptionPane.INFORMATION_MESSAGE;
                        String v_string_validation_result = Utils.getDefaultBundle(sfwXMLValidatorProduct).getString("v_string_validation_result.nenhumaInconsitencia");

                        if(jTableVersion.getSelectedRow() >= 0)
                        {
                            try
                            {
                                if(_rows_diff.containsKey(jTableVersion.getSelectedRow()))
                                   v_string_validation_result = Version.testDiff(((productinfo.Diff)_rows_diff.get(jTableVersion.getSelectedRow())), ((Product)sfwComboBoxProduct.getBindedSelectedItem()), v_string_os, v_string_db);
                                else if(_rows_full.containsKey(jTableVersion.getSelectedRow()))
                                   v_string_validation_result = Version.testFull(((productinfo.Full)_rows_full.get(jTableVersion.getSelectedRow())), ((Product)sfwComboBoxProduct.getBindedSelectedItem()), v_string_os, v_string_db);

                                if(v_string_validation_result == null || v_string_validation_result.equals(""))
                                {
                                    v_int_message_type = JOptionPane.INFORMATION_MESSAGE;
                                    v_string_validation_result = Utils.getDefaultBundle(sfwXMLValidatorProduct).getString("v_string_validation_result.nenhumaInconsitencia");
                                }
                            }
                            catch(Exception exc)
                            {
                                exc.printStackTrace();
                                v_int_message_type = JOptionPane.ERROR_MESSAGE;
                                v_string_validation_result = Utils.getDefaultBundle(sfwXMLValidatorProduct).getString("v_string_validation_result.validarVersaoErro")+
                                                             "\n"+
                                                             Utils.getDefaultBundle(sfwXMLValidatorProduct).getString("v_string_validation_result.verficaViaDebug")+
                                							 exc.toString();
                            }

                            JOptionPane.showMessageDialog(null, 
                            		v_string_validation_result, 
                            		Utils.getDefaultBundle(sfwXMLValidatorProduct).getString("JOptionPane.text.resultadoValidação")+
                            		((Version)_rowproduct.get(jTableVersion.getSelectedRow())).get_description(), 
                            		v_int_message_type);
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });

                menuDB.add(menuItValidar);

                menuOS.add(menuDB);
            }
        }

        this._context_menu.add(menuValidar);
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
        jTableVersion = new javax.swing.JTable();
        sfwComboBoxProduct = new sfwinstaladorscript.components.SfwComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButtonCorFull = new javax.swing.JButton();
        jTextFieldCorFull = new javax.swing.JTextField();
        jButtonCorDiff = new javax.swing.JButton();
        jTextFieldCorDiff = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwXMLValidatorProduct.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableVersion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
            		resourceMap.getString("jTableVersion.title.mesAno"),
            		resourceMap.getString("jTableVersion.title.pacote"),
            		resourceMap.getString("jTableVersion.title.nomeVersao"),
            		resourceMap.getString("jTableVersion.title.tipo"),
            		resourceMap.getString("jTableVersion.title.descTipo"),
            		resourceMap.getString("jTableVersion.title.compBase")
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableVersion.setGridColor(resourceMap.getColor("jTableVersion.gridColor")); // NOI18N
        jTableVersion.setName("jTableVersion"); // NOI18N
        jTableVersion.setRowHeight(18);
        jTableVersion.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableVersion);

        sfwComboBoxProduct.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        sfwComboBoxProduct.setName("sfwComboBoxProduct"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getActionMap(SfwXMLValidatorProduct.class, this);
        jButtonCorFull.setAction(actionMap.get("jButtonColorFullAction")); // NOI18N
        jButtonCorFull.setFont(resourceMap.getFont("jButtonCorFull.font")); // NOI18N
        jButtonCorFull.setText(resourceMap.getString("jButtonCorFull.text")); // NOI18N
        jButtonCorFull.setToolTipText(resourceMap.getString("jButtonCorFull.toolTipText")); // NOI18N
        jButtonCorFull.setBorder(null);
        jButtonCorFull.setName("jButtonCorFull"); // NOI18N

        jTextFieldCorFull.setEditable(false);
        jTextFieldCorFull.setText(resourceMap.getString("jTextFieldCorFull.text")); // NOI18N
        jTextFieldCorFull.setName("jTextFieldCorFull"); // NOI18N

        jButtonCorDiff.setAction(actionMap.get("jButtonColorDiffAction")); // NOI18N
        jButtonCorDiff.setText(resourceMap.getString("jButtonCorDiff.text")); // NOI18N
        jButtonCorDiff.setToolTipText(resourceMap.getString("jButtonCorDiff.toolTipText")); // NOI18N
        jButtonCorDiff.setBorder(null);
        jButtonCorDiff.setName("jButtonCorDiff"); // NOI18N

        jTextFieldCorDiff.setEditable(false);
        jTextFieldCorDiff.setName("jTextFieldCorDiff"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sfwComboBoxProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 181, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(31, 31, 31)
                        .add(jButtonCorFull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldCorFull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonCorDiff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldCorDiff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(sfwComboBoxProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonCorFull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldCorFull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonCorDiff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldCorDiff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SfwXMLValidatorProduct dialog = new SfwXMLValidatorProduct(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    /**
     * Ação do botão de cor dos pacotes FULL.
     */
    @Action
    public void jButtonColorFullAction()
    {
        Color newColor = JColorChooser.showDialog(
                         this,
                         Utils.getDefaultBundle(this).getString("JColorChooser.text.colorFull"),
                         SfwXMLValidatorProduct.FullRowBackColor);


        if(newColor != null)
        {
            if(SfwXMLValidatorProduct.DiffRowBackColor.getRGB() == newColor.getRGB())
                    newColor = new Color(newColor.getRed(),newColor.getGreen(),newColor.getBlue()-1);
            
            this._colorcolumn.changeColor(SfwXMLValidatorProduct.FullRowBackColor, newColor);
            SfwXMLValidatorProduct.FullRowBackColor = newColor;
            this.jTableVersion.invalidate();
            this.jTableVersion.repaint();
            this.jTextFieldCorFull.setBackground(newColor);
            this.jTextFieldCorFull.invalidate();
            this.jTextFieldCorFull.repaint();
        }
    }

    /**
     * Ação do botão de cor dos pacotes DIFF.
     */
    @Action
    public void jButtonColorDiffAction()
    {
        Color newColor = JColorChooser.showDialog(
                         this,
                         Utils.getDefaultBundle(this).getString("JColorChooser.text.colorDiff"),
                         SfwXMLValidatorProduct.DiffRowBackColor);


        if(newColor != null)
        {
            if(SfwXMLValidatorProduct.FullRowBackColor.getRGB() == newColor.getRGB())
                    newColor = new Color(newColor.getRed(),newColor.getGreen(),newColor.getBlue()-1);

            this._colorcolumn.changeColor(SfwXMLValidatorProduct.DiffRowBackColor, newColor);
            SfwXMLValidatorProduct.DiffRowBackColor = newColor;
            this.jTableVersion.invalidate();
            this.jTableVersion.repaint();
            this.jTextFieldCorDiff.setBackground(newColor);
            this.jTextFieldCorDiff.invalidate();
            this.jTextFieldCorDiff.repaint();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCorDiff;
    private javax.swing.JButton jButtonCorFull;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVersion;
    private javax.swing.JTextField jTextFieldCorDiff;
    private javax.swing.JTextField jTextFieldCorFull;
    private sfwinstaladorscript.components.SfwComboBox sfwComboBoxProduct;
    // End of variables declaration//GEN-END:variables

}
