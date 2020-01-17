/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.components;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 */
public class SfwCheckTree extends JTree
{
    public SfwCheckTree() {        
    }

    public SfwCheckTree(Object[] value) {
        super(value);
    }

    public SfwCheckTree(TreeNode root) {
        super(root);
    }

    public void expandAll() {
    int row = 0;
    while (row < this.getRowCount()) {
      this.expandRow(row);
      row++;
      }
    }

    public void expandToLast() {
        // expand to the last leaf from the root
        DefaultMutableTreeNode  root;
        root = (DefaultMutableTreeNode) this.getModel().getRoot();
        this.scrollPathToVisible(new TreePath(root.getLastLeaf().getPath()));
    }

    public void collapseAll() {
    int row = this.getRowCount() - 1;
    while (row >= 0) {
      this.collapseRow(row);
      row--;
      }
    }
}
