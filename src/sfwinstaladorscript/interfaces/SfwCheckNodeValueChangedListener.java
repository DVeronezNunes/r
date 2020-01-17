/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.interfaces;

import java.util.EventListener;
import sfwinstaladorscript.components.SfwCheckNodeValueChangedEvent;

/**
 *
 * @author clmonaco
 */
public interface SfwCheckNodeValueChangedListener extends EventListener {
    public void CheckNodeValueChanged(SfwCheckNodeValueChangedEvent e);
}
