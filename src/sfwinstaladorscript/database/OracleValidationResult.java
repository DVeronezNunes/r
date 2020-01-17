/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.database;

import java.awt.Color;
import sfwinstaladorscript.Utils;

/**
 * Objeto que representa o resultado de uma validação na instalação Oracle.
 */
public class OracleValidationResult {

    private Result _result;
    private String _text;

    public enum Result {
    ERROR, WARN, SUCCESS
    }
    
    public Result get_result() {
        return _result;
    }

    public void set_result(Result _result) {
        this._result = _result;
    }

    public String get_text() {
        return _text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public Color get_result_color()
    {
        if(this._result.equals(OracleValidationResult.Result.SUCCESS))
            return new Color(0, 150, 0);
        else if(this._result.equals(OracleValidationResult.Result.WARN))
            return new Color(200, 187, 34);
        else if(this._result.equals(OracleValidationResult.Result.ERROR))
            return new Color(255, 0, 0);
        else
            return new Color(0, 0, 0);
    }

    public String get_result_text()
    {
        String v_string_msgok = "OK";
        String v_string_msgwarn = Utils.getDefaultBundle().getString("Validation.warn");
        String v_string_msgnotok = Utils.getDefaultBundle().getString("Validation.problem");

        if(this._result.equals(OracleValidationResult.Result.SUCCESS))
            return v_string_msgok;
        else if(this._result.equals(OracleValidationResult.Result.WARN))
            return v_string_msgwarn;
        else if(this._result.equals(OracleValidationResult.Result.ERROR))
            return v_string_msgnotok;
        else
            return "";
    }

}
