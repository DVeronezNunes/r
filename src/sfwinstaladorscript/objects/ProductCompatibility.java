/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.objects;

/**
 *
 * @author jopaulo
 */
public class ProductCompatibility {

    private Product _Product;
    private String _version;
    private String _versioncompability;

    /**
     * @return the _nome
     */
    public Product getProduct() {
        return _Product;
    }

    /**
     * @param nome the _nome to set
     */
    public void setProduct(Product Product) {
        this._Product = Product;
    }

    /**
     * @return the _version
     */
    public String getVersion() {
        return _version;
    }

    /**
     * @param version the _version to set
     */
    public void setVersion(String version) {
        this._version = version;
    }

    /**
     * @return the _versioncompability
     */
    public String getVersioncompability() {
        return _versioncompability;
    }

    /**
     * @param versioncompability the _versioncompability to set
     */
    public void setVersioncompability(String versioncompability) {
        this._versioncompability = versioncompability;
    }
}
