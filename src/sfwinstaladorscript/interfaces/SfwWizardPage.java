/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.interfaces;

import sfwinstaladorscript.SfwInstaladorScriptView;
import java.util.Hashtable;

/**
 * Interface com os métodos de uma página do wizard.
 */
public interface SfwWizardPage {

    //public void flowSetup(SfwInstaladorScriptView view);
    public void nextClick(Hashtable wzPages, SfwInstaladorScriptView view);
    public void backClick(Hashtable wzPages, SfwInstaladorScriptView view);

    public void flowSetup(SfwInstaladorScriptView aThis);

}
