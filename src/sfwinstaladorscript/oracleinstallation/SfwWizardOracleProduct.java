/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SfwWizardProduct.java
 *
 * Created on 27/10/2008, 16:48:12
 */
package sfwinstaladorscript.oracleinstallation;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.Profile;
import sfwinstaladorscript.SfwInstaladorScriptView;
import sfwinstaladorscript.SfwWizardSetup;
import sfwinstaladorscript.Utils;
import sfwinstaladorscript.components.SfwCheckNode;
import sfwinstaladorscript.components.SfwCheckNodeEditor;
import sfwinstaladorscript.components.SfwCheckNodeValueChangedEvent;
import sfwinstaladorscript.components.SfwCheckRenderer;
import sfwinstaladorscript.components.SfwCheckTree;
import sfwinstaladorscript.database.OracleConnection;
import sfwinstaladorscript.interfaces.SfwCheckNodeValueChangedListener;
import sfwinstaladorscript.interfaces.SfwWizardPage;
import sfwinstaladorscript.objects.Product;
import sfwinstaladorscript.objects.ProductCompatibility;
import sfwinstaladorscript.objects.ProductProfileDefine;
import sfwinstaladorscript.objects.ProductProfileInstall;

/**
 * P√°gina de sele√ß√£o de produtos.
 */
public class SfwWizardOracleProduct extends javax.swing.JPanel implements SfwWizardPage {

    private ArrayList _products;
    private ArrayList _productsnode;
    private ArrayList _productsdetail;
    private ArrayList _checkednodes;
    private ArrayList _productsdefine;

    class NodeSelectionListener extends MouseAdapter {

        JTree tree;

        NodeSelectionListener(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath path = tree.getPathForRow(row);
            //TreePath  path = tree.getSelectionPath();
            if (path != null) {
                SfwCheckNode node = (SfwCheckNode) path.getLastPathComponent();
                boolean isSelected = !(node.isSelected());
                node.setSelected(isSelected);
                if (node.getSelectionMode() == SfwCheckNode.DIG_IN_SELECTION) {
                    if (isSelected) {
                        tree.expandPath(path);
                    } else {
                        tree.collapsePath(path);
                    }
                }
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                // I need revalidate if node is root.  but why?
                if (row == 0) {
                    tree.revalidate();
                    tree.repaint();
                }
            }
        }
    }

    /** Creates new form SfwWizardProduct */
    public SfwWizardOracleProduct(ArrayList products) {

        Product v_product_current;
        SfwCheckNode v_sfwchecknode_current;
        SfwOracleProductDetail v_sfworacleproductdetail_current;
        ProductProfileInstall v_productprofileinstall_current;
        Iterator v_iterator_products, v_iterator_checks;

        initComponents();

        this._products = products;
        this._productsdetail = new ArrayList();
        this._productsdefine = new ArrayList();

        this.jScrollPanelProduct.setVisible(true);

        if (!SfwWizardSetup.jRadioPrimeira.isSelected()) {
            try {
                this.returnInstalledVersion();
            } catch (Exception e) {
            }
        }

        SfwCheckNode root = this.listProducts();

        this.sfwCheckTreeProduto = new SfwCheckTree(root);
        this.sfwCheckTreeProduto.setEditable(false);
        this.sfwCheckTreeProduto.setCellRenderer(new SfwCheckRenderer());
        this.sfwCheckTreeProduto.setCellEditor(new SfwCheckNodeEditor(this.sfwCheckTreeProduto, root, this.jTabbedPane1));
        this.sfwCheckTreeProduto.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.sfwCheckTreeProduto.putClientProperty("JTree.lineStyle", "Angled");
        this.sfwCheckTreeProduto.addMouseListener(new NodeSelectionListener(this.sfwCheckTreeProduto));
        this.sfwCheckTreeProduto.setRowHeight(25);
        this.sfwCheckTreeProduto.setBackground(null);
        this.sfwCheckTreeProduto.setRootVisible(false);
        this.sfwCheckTreeProduto.expandAll();
        this.sfwCheckTreeProduto.setToggleClickCount(2);
        this.jScrollPanelProduct.setViewportView(this.sfwCheckTreeProduto);

        this.sfwCheckTreeProduto.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                JPopupMenu contextMenu;
                JMenuItem menuItSelecionar;
                JMenuItem menuItDesSelecionar;

                contextMenu = new JPopupMenu();
                UIManager.put("MenuItem.selectionBackground", new Color(210, 211, 249));
                menuItSelecionar = new JMenuItem(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleProduct").getString("optselectall"));
                menuItSelecionar.setBackground(new Color(229, 229, 229));
                menuItSelecionar.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mousePressed(MouseEvent e) {
                        SfwCheckNode currentNode;
                        Iterator v_iterator_it = _productsnode.iterator();

                        while (v_iterator_it.hasNext()) {
                            currentNode = (SfwCheckNode) v_iterator_it.next();

                            if (!currentNode.isValue()) {
                                currentNode.setValue(true);
                            }

                            ((DefaultTreeModel) sfwCheckTreeProduto.getModel()).reload();
                            sfwCheckTreeProduto.expandAll();

                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });

                menuItDesSelecionar = new JMenuItem(java.util.ResourceBundle.getBundle("sfwinstaladorscript/oracleinstallation/resources/SfwWizardOracleProduct").getString("optunselectall"));
                menuItDesSelecionar.setBackground(new Color(229, 229, 229));
                menuItDesSelecionar.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mousePressed(MouseEvent e) {
                        SfwCheckNode currentNode;
                        Iterator v_iterator_it = _productsnode.iterator();

                        while (v_iterator_it.hasNext()) {
                            currentNode = (SfwCheckNode) v_iterator_it.next();

                            if (currentNode.isValue()) {
                                currentNode.setValue(false);
                            }

                            jTabbedPane1.doLayout();
                            ((DefaultTreeModel) sfwCheckTreeProduto.getModel()).reload();
                            sfwCheckTreeProduto.expandAll();

                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });

                if (contextMenu.isPopupTrigger(e)) {
                    contextMenu.add(menuItSelecionar);
                    contextMenu.add(menuItDesSelecionar);
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

        final SfwWizardOracleProduct sfwWizardOracleProduct = this;

        if (Profile.isSet()) {
            v_iterator_products = this._products.iterator();
            while (v_iterator_products.hasNext()) {
                v_product_current = (Product) v_iterator_products.next();

                if (v_product_current.is_installable()) {
                    if (Profile.get_productsinstall().containsKey(v_product_current.get_name())) {
                        v_productprofileinstall_current = (ProductProfileInstall) Profile.get_productsinstall().get(v_product_current.get_name());

                        v_iterator_checks = this._productsnode.iterator();
                        while (v_iterator_checks.hasNext()) {
                            v_sfwchecknode_current = (SfwCheckNode) v_iterator_checks.next();

                            if (v_sfwchecknode_current.toString().equals(v_product_current.get_label())) {
                                v_sfwchecknode_current.setValue(true);

                                v_sfworacleproductdetail_current = (SfwOracleProductDetail) this._productsdetail.get(this._productsdetail.size() - 1);

                                v_sfworacleproductdetail_current.setUser(v_productprofileinstall_current.get_user());
                                v_sfworacleproductdetail_current.setPassword(v_productprofileinstall_current.get_pass());
                                v_sfworacleproductdetail_current.setData4k(v_productprofileinstall_current.get_tbl_data_4k());
                                v_sfworacleproductdetail_current.setIndex4k(v_productprofileinstall_current.get_tbl_index_4k());

                            }
                        }
                    }
                }
            }
        } else {
            v_iterator_products = this._products.iterator();
            while (v_iterator_products.hasNext()) {
                v_product_current = (Product) v_iterator_products.next();

                if (v_product_current.is_installable()) {

                    v_iterator_checks = this._productsnode.iterator();
                    while (v_iterator_checks.hasNext()) {
                        v_sfwchecknode_current = (SfwCheckNode) v_iterator_checks.next();

                        if (v_sfwchecknode_current.toString().equals(v_product_current.get_label())) {
                            v_sfwchecknode_current.setValue(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Lista produtos com checkboxes correspondentes
     */
    private SfwCheckNode listProducts() {
        Iterator v_iterator_products;
        Iterator v_iterator_childs;
        Product v_product_current;
        Product v_product_child;
        SfwCheckNode v_sfwchecknode_newnode;
        SfwCheckNode v_sfwchecknode_newnodechild;
        SfwCheckNode v_sfwchecknode_root = new SfwCheckNode("", true, false);



        this._productsnode = new ArrayList();


        this._checkednodes = new ArrayList();

        v_iterator_products = this._products.iterator();


        while (v_iterator_products.hasNext()) {
            v_product_current = (Product) v_iterator_products.next();



            if (v_product_current.is_installable() && v_product_current.get_modtype().equals("NONMODULAR")) {
                v_sfwchecknode_newnode = new SfwCheckNode(v_product_current.get_label(), true, false);
                v_sfwchecknode_root.add(v_sfwchecknode_newnode);



                if (!v_product_current.get_icon().equals("")) {
                    v_sfwchecknode_newnode.setNodeicon(new ImageIcon(("icon" + System.getProperty("file.separator") + v_product_current.get_icon())));


                }

                v_sfwchecknode_newnode.addSfwCheckNodeValueChangedListener(new SfwCheckNodeValueChangedListener() {

                    public void CheckNodeValueChanged(SfwCheckNodeValueChangedEvent e) {
                        int v_int_index_tab, v_int_index_detail;
                        Iterator v_iterator_products;
                        SfwOracleProductDetail pd;
                        SfwCheckNode chkNode = (SfwCheckNode) e.getSource();



                        if (!chkNode.isValue()) {
                            //procura e salva o produto selecionado
                            Product p = null;
                            v_iterator_products = _products.iterator();


                            while (v_iterator_products.hasNext()) {
                                p = (Product) v_iterator_products.next();


                                if (p.get_label().equals(chkNode.toString())) {
                                    break;


                                } else {
                                    p = null;


                                }
                            }

                            pd = new SfwOracleProductDetail(p);
                            _productsdetail.add(pd);
                            jTabbedPane1.addTab(chkNode.toString(), chkNode.getNodeicon(), pd);
                            _checkednodes.add(chkNode);


                        } else {
                            v_int_index_tab = 0;
                            v_int_index_detail = 0;



                            for (int i = 0; i
                                    < jTabbedPane1.getTabCount(); i++) {
                                if (jTabbedPane1.getTitleAt(i).equals(chkNode.toString())) {
                                    v_int_index_tab = i;


                                    break;


                                }
                            }

                            for (int i = 0; i
                                    < _productsdetail.size(); i++) {
                                if (((SfwOracleProductDetail) _productsdetail.get(i)).getProduct().get_label().equals(chkNode.toString())) {
                                    v_int_index_detail = i;


                                    break;


                                }
                            }

                            jTabbedPane1.removeTabAt(v_int_index_tab);
                            _productsdetail.remove(v_int_index_detail);
                            _checkednodes.remove(chkNode);

                        }
                    }
                });

                this._productsnode.add(v_sfwchecknode_newnode);

                if (v_product_current.haschilds()) {
                    v_iterator_childs = v_product_current.get_childs().iterator();

                    while (v_iterator_childs.hasNext()) {
                        v_product_child = (Product) v_iterator_childs.next();

                        if (v_product_child.is_installable()) {
                            v_sfwchecknode_newnodechild = new SfwCheckNode(v_product_child.get_label(), false, false);
                            v_sfwchecknode_newnodechild.setChangewithfathertrue(true);

                            if (!v_product_child.get_icon().equals("")) {
                                v_sfwchecknode_newnodechild.setNodeicon(new ImageIcon(("icon" + System.getProperty("file.separator") + v_product_child.get_icon())));
                            }

                            if (v_product_child.get_modtype().equals("STRONGMODULAR")) {
                                v_sfwchecknode_newnodechild.setChangewithfatherfalse(true);
                            }

                            v_sfwchecknode_newnode.add(v_sfwchecknode_newnodechild);

                            v_sfwchecknode_newnodechild.addSfwCheckNodeValueChangedListener(new SfwCheckNodeValueChangedListener() {

                                public void CheckNodeValueChanged(SfwCheckNodeValueChangedEvent e) {
                                    int v_int_index_tab, v_int_index_detail;
                                    Iterator v_iterator_products;
                                    SfwOracleProductDetail pd;
                                    SfwCheckNode chkNode = (SfwCheckNode) e.getSource();

                                    if (!chkNode.isValue()) {
                                        //procura e salva o produto selecionado
                                        Product p = null;
                                        v_iterator_products = _products.iterator();

                                        while (v_iterator_products.hasNext()) {
                                            p = (Product) v_iterator_products.next();

                                            if (p.get_label().equals(chkNode.toString())) {
                                                break;
                                            } else {
                                                p = null;
                                            }
                                        }

                                        if (!p.get_modtype().equals("STRONGMODULAR")) {
                                            pd = new SfwOracleProductDetail(p);
                                            _productsdetail.add(pd);
                                            jTabbedPane1.addTab(chkNode.toString(), pd);
                                        }
                                    } else {
                                        v_int_index_tab = 0;
                                        v_int_index_detail = 0;

                                        //procura e salva o produto selecionado
                                        Product p = null;
                                        v_iterator_products = _products.iterator();


                                        while (v_iterator_products.hasNext()) {
                                            p = (Product) v_iterator_products.next();


                                            if (p.get_label().equals(chkNode.toString())) {
                                                break;


                                            } else {
                                                p = null;


                                            }
                                        }

                                        if (!p.get_modtype().equals("STRONGMODULAR")) {
                                            for (int i = 0; i
                                                    < jTabbedPane1.getTabCount(); i++) {
                                                if (jTabbedPane1.getTitleAt(i).equals(chkNode.toString())) {
                                                    v_int_index_tab = i;


                                                    break;


                                                }
                                            }

                                            for (int i = 0; i
                                                    < _productsdetail.size(); i++) {
                                                if (((SfwOracleProductDetail) _productsdetail.get(i)).getProduct().get_label().equals(chkNode.toString())) {
                                                    v_int_index_detail = i;


                                                    break;


                                                }
                                            }

                                            jTabbedPane1.removeTabAt(v_int_index_tab);
                                            _productsdetail.remove(v_int_index_detail);


                                        }
                                    }
                                }
                            });



                            this._productsnode.add(v_sfwchecknode_newnodechild);


                        }
                    }
                }
            }
        }

        return v_sfwchecknode_root;


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanelProductDetail = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPanelProduct = new javax.swing.JScrollPane();
        sfwCheckTreeProduto = new sfwinstaladorscript.components.SfwCheckTree();
        sfwPageTitle = new sfwinstaladorscript.components.SfwWizardPageTitle();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(605, 378));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwWizardOracleProduct.class);
        JPanelProductDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("JPanelProductDetail.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("JPanelProductDetail.border.titleFont"), resourceMap.getColor("JPanelProductDetail.border.titleColor"))); // NOI18N
        JPanelProductDetail.setName("JPanelProductDetail"); // NOI18N

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setAutoscrolls(true);
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        org.jdesktop.layout.GroupLayout JPanelProductDetailLayout = new org.jdesktop.layout.GroupLayout(JPanelProductDetail);
        JPanelProductDetail.setLayout(JPanelProductDetailLayout);
        JPanelProductDetailLayout.setHorizontalGroup(
            JPanelProductDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        JPanelProductDetailLayout.setVerticalGroup(
            JPanelProductDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
        );

        jScrollPanelProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jScrollPanelProduct.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollPanelProduct.border.titleFont"), resourceMap.getColor("jScrollPanelProduct.border.titleColor"))); // NOI18N
        jScrollPanelProduct.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPanelProduct.setName("jScrollPanelProduct"); // NOI18N

        sfwCheckTreeProduto.setBackground(resourceMap.getColor("sfwCheckTreeProduto.background")); // NOI18N
        sfwCheckTreeProduto.setName("sfwCheckTreeProduto"); // NOI18N
        jScrollPanelProduct.setViewportView(sfwCheckTreeProduto);

        sfwPageTitle.setName("sfwPageTitle"); // NOI18N
        sfwPageTitle.setSubtitulo(resourceMap.getString("sfwPageTitle.subtitulo")); // NOI18N
        sfwPageTitle.setTitulo(resourceMap.getString("sfwPageTitle.titulo")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPanelProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(JPanelProductDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(sfwPageTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(JPanelProductDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jScrollPanelProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 333, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * ImplementaÁ„o de setup inicial da p·gina.
     * @param view Frame principal da aplicaÁ„o.
     */
    public void flowSetup(SfwInstaladorScriptView view) {

        int i;
        Iterator v_iterator_it, v_iterator_it2;
        SfwCheckNode v_sfwchecknode_current;
        ArrayList v_arraylist_remove_idx;
        SfwOracleProductDetail v_sfworacleproductdetail_pd;

        view.getBackButton().setEnabled(true);
        view.getNextButton().setEnabled(true);
        view.getWzPages().put("wzInstalledProduct", new SfwWizardOracleProduct2(view, this._products));
        view.getWzPages().put("wzValidator", new SfwWizardOracleValidator(view));
        view.getWzPages().put("wzValidator2", new SfwWizardOracleValidator2(view));
        view.getWzPages().put("wzInstall", new SfwWizardOracleInstall(view));

        // se estiver voltando e um dos produtos n√£o estiver tabeado, cria a tab novamente
        if (Install.get_productsdetail().size() > 0) {
            v_arraylist_remove_idx = new ArrayList();

            // ver quais abas que foram retiradas da instalacao
            for (i = 0; i < this.jTabbedPane1.getTabCount(); i++) {
                if (!Install.get_productsdetail().contains(this.jTabbedPane1.getComponentAt(i))) {
                    v_arraylist_remove_idx.add(i);
                }
            }

            // remove as abas
            i = 0;
            v_iterator_it = v_arraylist_remove_idx.iterator();
            while (v_iterator_it.hasNext()) {
                this.jTabbedPane1.removeTabAt(((Integer) v_iterator_it.next()) - i);
                i++;
            }

            //desmarca checks
            v_iterator_it2 = this._productsnode.iterator();
            while (v_iterator_it2.hasNext()) {
                v_sfwchecknode_current = (SfwCheckNode) v_iterator_it2.next();
                v_sfwchecknode_current.setSelected(false);
            }

            // acrescentar abas que foram selecionadas para instalacao
            v_iterator_it = Install.get_productsdetail().iterator();
            while (v_iterator_it.hasNext()) {
                v_sfworacleproductdetail_pd = (SfwOracleProductDetail) v_iterator_it.next();

                //marca check correspondente
                v_iterator_it2 = this._productsnode.iterator();
                while (v_iterator_it2.hasNext()) {
                    v_sfwchecknode_current = (SfwCheckNode) v_iterator_it2.next();

                    if (v_sfwchecknode_current.toString().equals(v_sfworacleproductdetail_pd.getProduct().get_label())) {
                        v_sfwchecknode_current.setValue(true);
                    }
                }

                if (this.jTabbedPane1.indexOfTab(v_sfworacleproductdetail_pd.getProduct().get_label()) == -1) {
                    this.jTabbedPane1.addTab(v_sfworacleproductdetail_pd.getProduct().get_label(), v_sfworacleproductdetail_pd);
                }
            }
        }
    }

    /**
     * ImplementaÁ„o da aÁ„o do bot„o "PrÛximo" para esta p·gina.
     * @param wzPages Hashtable com todas as p·ginas do wizard.
     * @param view Frame principal da aplicaÁ„o.
     */
    public void nextClick(Hashtable wzPages, SfwInstaladorScriptView view) {
        Iterator v_iterator_it;
        SfwOracleProductDetail v_sfworacleproductdetail_pd;

        // valida se selecionou algum produto
        if (this._productsdetail.isEmpty()) {
            JOptionPane.showMessageDialog(null, Utils.getDefaultBundle(this).getString("validationproduct"), Utils.getDefaultBundle(this).getString("validationlabel"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        // valida produtos selecionados
        v_iterator_it = this._productsdetail.iterator();
        while (v_iterator_it.hasNext()) {
            v_sfworacleproductdetail_pd = (SfwOracleProductDetail) v_iterator_it.next();
            if (!v_sfworacleproductdetail_pd.Validate()) {
                this.jTabbedPane1.setSelectedComponent(v_sfworacleproductdetail_pd);
                return;
            }
        }

        if (!this.checkDependencies()) {
            return;
        }

        Install.set_productsdetail(this._productsdetail);

        if (!this._productsdetail.isEmpty()) {
            if (((SfwOracleProductDetail) this._productsdetail.get(0)).getProduct().get_name().equals("INTERFACES_R12")
                    || ((SfwOracleProductDetail) this._productsdetail.get(0)).getProduct().get_name().equals("IT_R12DBLINK")
                    || ((SfwOracleProductDetail) this._productsdetail.get(0)).getProduct().get_name().equals("IT_R11")) {
                wzPages.put("wzProduct3", new SfwWizardOracleProduct3(((SfwOracleProductDetail) this._productsdetail.get(0)).getProduct()));
                view.setWizardPage((JPanel) wzPages.get("wzProduct3"));
            } else {
                view.setWizardPage((JPanel) wzPages.get("wzInstalledProduct"));
            }
        }
    }

    /**
     * ImplementaÁ„o da aÁ„o do bot„o "Anterior" para esta pagina.
     * @param wzPages Hashtable com todas as pagina do wizard.
     * @param view Frame principal da aplicaÁ„o.
     */
    public void backClick(Hashtable wzPages, SfwInstaladorScriptView view) {
        Iterator v_iterator_it;
        this.jTabbedPane1.removeAll();
        this._productsdetail = new ArrayList();

        Install.set_productsdetail(this._productsdetail);

        view.setWizardPage((JPanel) wzPages.get("wzSetup"));
    }

    /**
     * Verifica dependencias dos produtos para colocar o aviso.
     */
    private boolean checkDependencies() {
        int confirm;
        Iterator v_iterator_it;
        String[] v_product_current;
        String v_string_products, v_string_version;
        HashMap<Product, List<ProductCompatibility>> v_hashmap_compability = new HashMap<Product, List<ProductCompatibility>>();
        List<ProductCompatibility> v_list_compatibility = new ArrayList<ProductCompatibility>();

        for (int i = 0; i < this.jTabbedPane1.getTabCount(); i++) {
            if (((SfwOracleProductDetail) this.jTabbedPane1.getComponentAt(i)) != null) {
                v_string_products = "";
                v_string_version = "";

                //v_arraylist_dependencies = ((SfwOracleProductDetail) this.jTabbedPane1.getComponentAt(i)).checkDependency(_checkednodes);

                if (!((SfwOracleProductDetail) this.jTabbedPane1.getComponentAt(i)).getProduct().get_code().equals("0")) {
                    try {
                        v_list_compatibility = ((SfwOracleProductDetail) this.jTabbedPane1.getComponentAt(i)).checkCompatibility(jTabbedPane1);
                        if (!v_list_compatibility.isEmpty()) {
                            v_hashmap_compability.put(((SfwOracleProductDetail) this.jTabbedPane1.getComponentAt(i)).getProduct(), v_list_compatibility);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        if (!v_hashmap_compability.isEmpty()) {
            SfwWizardCompatibility v_compatibilidade = new SfwWizardCompatibility(v_hashmap_compability);
            v_compatibilidade.setLocationRelativeTo(null);
            v_compatibilidade.setModal(true);
            v_compatibilidade.setVisible(true);
            return false;
        }

        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelProductDetail;
    private javax.swing.JScrollPane jScrollPanelProduct;
    public static javax.swing.JTabbedPane jTabbedPane1;
    private sfwinstaladorscript.components.SfwCheckTree sfwCheckTreeProduto;
    private sfwinstaladorscript.components.SfwWizardPageTitle sfwPageTitle;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param cod_produto
     * @return
     */
    public void returnInstalledVersion() throws Exception {
        OracleConnection v_conn = new OracleConnection();
        Iterator v_iterator_it;
        Set set;
        try {
            set = Profile.get_productsinstall().entrySet();
            v_iterator_it = set.iterator();
            while (v_iterator_it.hasNext()) {
                Map.Entry v_mapentry = (Map.Entry) v_iterator_it.next();
                ProductProfileInstall v_sfworacleproduct = ((ProductProfileInstall) v_mapentry.getValue());
                if (v_sfworacleproduct.get_name().equals("SOFTCOMEX")) {
                    v_conn.set_username(v_sfworacleproduct.get_user());
                    v_conn.set_password(v_sfworacleproduct.get_pass());
                    v_conn.set_tns(Profile.get_tns());
                    break;
                }
            }
            v_conn.ConnectAux();
        } catch (Exception e) {
            set = Profile.get_productsdefine().entrySet();
            v_iterator_it = set.iterator();
            while (v_iterator_it.hasNext()) {
                Map.Entry v_mapentry = (Map.Entry) v_iterator_it.next();
                ProductProfileDefine v_sfworacleproduct = ((ProductProfileDefine) v_mapentry.getValue());
                if (v_sfworacleproduct.get_name().equals("SOFTCOMEX")) {
                    v_conn.set_username(v_sfworacleproduct.get_user());
                    v_conn.set_password(v_sfworacleproduct.get_pass());
                    v_conn.set_tns(Profile.get_tns());
                    break;
                }
            }
            v_conn.ConnectAux();
        }
        ResultSet rs = v_conn.Query("SELECT COD_SISTEMA, VERSAO, RELEASE, PATCH, BUILD FROM SFW_SISTEMA_VERSAO WHERE VALIDO = 'S'");
        while (rs.next()) {
            for (int i = 0; i < _products.size(); i++) {
                if (rs.getString("COD_SISTEMA").equals("0")) {
                    if (((Product) _products.get(i)).get_name().equals("SOFTCOMEX")) {
                        ((Product) _products.get(i)).setInstalledversion(this.get_description(rs.getInt("VERSAO"), rs.getInt("RELEASE"), rs.getInt("PATCH"), rs.getInt("BUILD")));
                        break;
                    }
                } else {
                    if (((Product) _products.get(i)).get_code().equals(rs.getString("COD_SISTEMA"))) {
                        ((Product) _products.get(i)).setInstalledversion(this.get_description(rs.getInt("VERSAO"), rs.getInt("RELEASE"), rs.getInt("PATCH"), rs.getInt("BUILD")));
                        break;
                    }
                }
            }
        }
    }

    /**
     * DescriÁ„o para o SfwComboBox.
     * @return Nome da vers„o.
     */
    public String get_description(int versao, int release, int patch, int build) {
        String v_string_version, v_string_release, v_string_patch, v_string_build = "";

        if (versao < 10 || versao == 0) {
            v_string_version = "0" + versao;
        } else {
            v_string_version = "" + versao;
        }

        if (release < 10 || release == 0) {
            v_string_release = "0" + release;
        } else {
            v_string_release = "" + release;
        }

        if (patch < 10 || patch == 0) {
            v_string_patch = "0" + patch;
        } else {
            v_string_patch = "" + patch;
        }

        if (build != 0) {
            if (build < 10) {
                v_string_build = " B0" + build;
            } else {
                v_string_build = "B" + build;
            }
        }

        if (Integer.parseInt(v_string_version) > 9 && Integer.parseInt(v_string_version) <= 30) {
            return "" + v_string_version + "." + v_string_release + "." + v_string_patch + v_string_build;
        } else {
            return "" + v_string_version + "." + v_string_release + "." + v_string_patch + v_string_build;
        }
    }
}
