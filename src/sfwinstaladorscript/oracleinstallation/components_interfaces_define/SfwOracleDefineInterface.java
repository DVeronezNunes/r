/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.oracleinstallation.components_interfaces_define;

/**
 *
 * @author jopaulo
 */
public class SfwOracleDefineInterface {

    private String nome;
    private String user;
    private String pass;
    private String tns;
    private boolean EBS;
    private boolean Integration;

    
    /**
     * 
     * @return 
     */
    public String getPass() {
        return pass;
    }

    /**
     * 
     * @param pass 
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * 
     * @return 
     */
    public String getTns() {
        return tns;
    }

    /**
     * 
     * @param tns 
     */
    public void setTns(String tns) {
        this.tns = tns;
    }

    /**
     * 
     * @return 
     */
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user 
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return 
     */
    public String getNome() {
        return nome;
    }

    /**
     * 
     * @param nome 
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
