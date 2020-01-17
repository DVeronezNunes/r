/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.objects;

/**
 *
 * @author jopaulo
 */
public class VersionCompability {

    private String _cod_sistema;
    private int _version;
    private int _release;
    private int _patch;

    public VersionCompability() {
    }

    /**
     * @return the _version
     */
    public int getVersion() {
        return _version;
    }

    /**
     * @param version the _version to set
     */
    public void setVersion(int version) {
        this._version = version;
    }

    /**
     * @return the _release
     */
    public int getRelease() {
        return _release;
    }

    /**
     * @param release the _release to set
     */
    public void setRelease(int release) {
        this._release = release;
    }

    /**
     * @return the _patch
     */
    public int getPatch() {
        return _patch;
    }

    /**
     * @param patch the _patch to set
     */
    public void setPatch(int patch) {
        this._patch = patch;
    }

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
}
