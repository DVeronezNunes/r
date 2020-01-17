/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfwinstaladorscript.config;

import sfwinstaladorscript.objects.Client;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Trata arquivo de configuração de clientes Softway.
 */
public class ClientIni {
    
    private ArrayList _clients;
    
    public ClientIni(InputStream in) throws IOException
    {
        Set v_set_s;
        Properties v_properties_p;
        String v_string_key;
        Iterator v_iterator_it;
        Client v_client_newclient;
        
        v_properties_p = new Properties();
        v_properties_p.load(in);
        
        this._clients = new ArrayList();
        
        v_set_s = v_properties_p.keySet();
        v_iterator_it = v_set_s.iterator();
        while(v_iterator_it.hasNext())
        {
            v_string_key = (String)v_iterator_it.next();
            
            if(v_string_key.startsWith("CLIENT"))
            {
              v_client_newclient = new Client();
              v_client_newclient.set_name(v_properties_p.getProperty(v_string_key));
              v_client_newclient.set_shortname(v_properties_p.getProperty(v_properties_p.getProperty(v_string_key)+"_SHORT",""));
              this._clients.add(v_client_newclient);
            }
        }
    }

    /**
     * Retorna lista de clientes Softway.
     * @return Lista de clientes.
     */
    public ArrayList get_clients() {
        return _clients;
    }    

}
