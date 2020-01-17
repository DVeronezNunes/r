/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.config;

import sfwinstaladorscript.objects.Database;
import sfwinstaladorscript.objects.SystemOS;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Trata arquivo de configuração configuração geral do instalador.
 */
public class InstallIni {

    private ArrayList _systems;
    private ArrayList _databases;
    
    public InstallIni(InputStream in) throws IOException
    {
        Set v_set_s;
        Iterator v_iterator_it;
        String v_string_key;
        Properties v_properties_p;
        SystemOS v_systemos_newos;
        Database v_database_newdb;
        
        v_properties_p = new Properties();
        v_properties_p.load(in);
        
        this._systems = new ArrayList();
        this._databases = new ArrayList();
        
        v_set_s = v_properties_p.keySet();
        v_iterator_it = v_set_s.iterator();
        while(v_iterator_it.hasNext())
        {
            v_string_key = (String)v_iterator_it.next();
            
            if(v_string_key.startsWith("OS"))
            {
              v_systemos_newos = new SystemOS();
              v_systemos_newos.set_name(v_properties_p.getProperty(v_string_key));
              v_systemos_newos.set_exec_ext(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key).toUpperCase()+"_EXECUTABLE",""));
              this._systems.add(v_systemos_newos);
            }
            else if(v_string_key.startsWith("BD"))
            {
                v_database_newdb = new Database();
                v_database_newdb.set_name(v_properties_p.getProperty(v_string_key));
                v_database_newdb.set_class(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key).toUpperCase()+"_CLASS",""));
                v_database_newdb.set_flowwizard(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key).toUpperCase()+"_WIZARD",""));
                v_database_newdb.set_jdbcpath(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key).toUpperCase()+"_CLIENT_JDBC",""));
                v_database_newdb.set_label(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key).toUpperCase()+"_LABEL",""));
                this._databases.add(v_database_newdb);
            }
        }
    }

    /**
     * Retorna lista de sistemas operacionais suportados.
     * @return Lista de sistemas operacionais.
     */
    public ArrayList get_systems() {
        return this._systems;
    }

    /**
     * Retorna lista de banco de dados suportados.
     * @return Lista de banco de dados.
     */
    public ArrayList get_databases() {
        return this._databases;
    }
}
