package sfwinstaladorscript.objects;

/**
 * Classe representando um usu·rio de define salvo no profile de instala√ß√£o.
 */
public class ProductProfileDefine
{
    
    /**
     * Nome do produto.
     */
    private String _name;

    /**
     * Usu·rio de banco.
     */
    private String _user;

    /**
     * Senha do Usu·rio.
     */
    private String _pass;

        /**
     * Usu·rio SOMBRA de banco.
     */
    private String _userSombra;

    /**
     * Senha do Usu·rio SOMBRA.
     */
    private String _passSombra;

    /**
     * Retorna o nome do produto.
     * @return Nome do produto.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera o nome do produto.
     * @param _name Nome do produto.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Altera a senha do usu·rio.
     * @return Senha
     */
    public String get_pass() {
        return _pass;
    }

    /**
     * Altera a senha do usu·rio.
     * @param _pass
     */
    public void set_pass(String _pass) {
        this._pass = _pass;
    }

    /**
     * Altera a senha do usu·rio.
     * @return Senha
     */
    public String get_passSombra() {
        return _passSombra;
    }

    /**
     * Altera a senha do usu·rio.
     * @param _pass
     */
    public void set_passSombra(String _passSombra) {
        this._passSombra = _passSombra;
    }

    /**
     * Retorna o nome do usu·rio de banco.
     * @return Nome do usu·rio de banco.
     */
    public String get_user() {
        return _user;
    }

    /**
     * Altera o nome do usu·rio de banco.
     * @param _name Nome do usu·rio de banco.
     */
    public void set_user(String _user) {
        this._user = _user;
    }
}