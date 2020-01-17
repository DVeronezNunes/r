/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.objects;

/**
 *
 * @author jopaulo
 */
public class ExportInt {

    private String _cod_sistema;
    private String _name;
    private String _label;


    public ExportInt(){}

    /**
     * @return the _cod_sistema
     */
    public String getCod_sistema() {
        return _cod_sistema;
    }

    /**
     * @param cod_sistema the _cod_sistema to set
     */
    public void setCod_sistema(String cod_sistema) {
        this._cod_sistema = cod_sistema;
    }

    /**
     * @return the _label
     */
    public String getLabel() {
        return _label;
    }

    /**
     * @param label the _label to set
     */
    public void setLabel(String label) {
        this._label = label;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

}
