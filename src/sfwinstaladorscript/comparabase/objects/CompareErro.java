package sfwinstaladorscript.comparabase.objects;

/**
 *
 * @author jopaulo
 */
public class CompareErro {

    private String object_name;
    private String object_erro;
    private String object_type;

    /**
     *
     * @return
     */
    public String getObject_erro() {
        return object_erro;
    }

    /**
     *
     * @param object_erro
     */
    public void setObject_erro(String object_erro) {
        this.object_erro = object_erro;
    }

    /**
     *
     * @return
     */
    public String getObject_name() {
        return object_name;
    }

    /**
     *
     * @param object_name
     */
    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    /**
     * 
     * @return
     */
    public String getObject_type() {
        return object_type;
    }

    /**
     *
     * @param object_type
     */
    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }
}
