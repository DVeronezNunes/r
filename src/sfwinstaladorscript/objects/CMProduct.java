/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.objects;

import java.util.ArrayList;

/**
 *
 * @author jopaulo
 */
public class CMProduct {

    private ArrayList<CMVersion> _versao;

    /**
     * @return the _versao
     */
    public ArrayList<CMVersion> getVersao() {
        return _versao;
    }

    /**
     * @param versao the _versao to set
     */
    public void setVersao(ArrayList<CMVersion> versao) {
        this._versao = versao;
    }
}
