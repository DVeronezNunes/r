package sfwinstaladorscript.objects;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.ZipFile;

/**
 * Lista de pacotes para instalação.
 */
public class PackageList
{

    /**
     * Máscara utilizada nos pacotes zip.
     */
    private final String _datemask = "yyyyMM";
    
    /**
     * Lista com todos os pacotes lidos.
     */
    private ArrayList _packagelist;

    /**
     * Pacote final (final.zip) lido.
     */
    private PackageFinal _finalpkt;
    
    /**
     * Pacote Inicial (initial.zip) lido.
     */
    private PackageInitial _initialpkt;

    /**
     * Lê pacotes disponíveis para instalação
     */
    public void read(ArrayList products) throws IOException
    {
        PackageFinal v_packagefinal_pkt;
        PackageInitial v_packageinitial_pkt;
        Package v_package_pkt;
        int v_int_dotplace;
        File v_file_pkt;
        File v_file_pktdir;
        File[] v_arr_file_pktlist;
        SimpleDateFormat v_simpledateformat_sdf;

        this._packagelist = new ArrayList();

        v_file_pktdir = new File("pacotes");
        v_arr_file_pktlist = v_file_pktdir.listFiles();

        for(int i=0; i < v_arr_file_pktlist.length; i++)
        {            
            v_package_pkt = new Package();
            v_simpledateformat_sdf = new SimpleDateFormat(this._datemask);

            try
            {
                v_file_pkt = v_arr_file_pktlist[i];
                
                if(!v_file_pkt.isDirectory())
                {
                	if(v_file_pkt.getName().toLowerCase().equals("final.zip") || v_file_pkt.getName().toLowerCase().equals("final.ipkg"))
                	{
                		v_packagefinal_pkt = new PackageFinal();
                		v_packagefinal_pkt.set_zip(new ZipFile(v_file_pkt));
                		this._finalpkt = v_packagefinal_pkt;
                	} else if(v_file_pkt.getName().toLowerCase().equals("initial.zip") || v_file_pkt.getName().toLowerCase().equals("initial.ipkg"))
                	{
                		v_packageinitial_pkt = new PackageInitial();
                		v_packageinitial_pkt.set_zip(new ZipFile(v_file_pkt));
                		this._initialpkt = v_packageinitial_pkt;
                	}
                	else
                	{
                        v_int_dotplace = v_file_pkt.getName().lastIndexOf('.');
                        v_package_pkt.set_name(v_file_pkt.getName().substring(0, v_int_dotplace));
                        v_package_pkt.set_extension(v_file_pkt.getName().substring(v_int_dotplace+1, v_file_pkt.getName().length()));
                        v_simpledateformat_sdf.parse(v_file_pkt.getName().substring(0, v_int_dotplace));
                        v_package_pkt.set_date(v_simpledateformat_sdf.getCalendar());
                        v_package_pkt.set_zip(new ZipFile(v_file_pkt));
                        v_package_pkt.readVersions(products);
                        this._packagelist.add(v_package_pkt);
                	}
                }
            } catch (ParseException ex)
            {
            }
        }
    }

    /**
     * Get the value of _packagelist
     * @return the value of _packagelist
     */
    public ArrayList get_packagelist() {
        return _packagelist;
    }

    /**
     * Set the value of _packagelist
     * @param _packagelist new value of _packagelist
     */
    public void set_packagelist(ArrayList _packagelist) {
        this._packagelist = _packagelist;
    }

    /**
     * Retorna todas as versões de um produto disponíveis nos pacotes.
     * @param product_name Nome do produto.
     * @return Lista com as versões.
     */
    public ArrayList getProductVersions(String product_name)
    {
        Iterator v_iterator_it, v_iterator_it2, v_iterator_it3;
        Package v_package_pkg;
        Version v_version_pkgversion, v_version_v;
        ArrayList v_arraylist_versions = new ArrayList();
        ArrayList v_arraylist_packageversions;
        boolean v_boolean_merged;

        v_iterator_it = this._packagelist.iterator();
        while(v_iterator_it.hasNext())
        {
            v_package_pkg = (Package)v_iterator_it.next();

            v_arraylist_packageversions = ((VersionList)v_package_pkg.get_versions().get(product_name)).get_versions();
            v_iterator_it2 = v_arraylist_packageversions.iterator();
            while(v_iterator_it2.hasNext())
            {
                v_version_pkgversion = (Version)v_iterator_it2.next();
                v_iterator_it3 = v_arraylist_versions.iterator();
                v_boolean_merged = false;
                while(v_iterator_it3.hasNext())
                {
                    v_version_v = (Version)v_iterator_it3.next();

                    if(v_version_pkgversion.compare(v_version_v) == 0)
                    {
                        v_version_v.merge(v_version_pkgversion);
                        v_boolean_merged = true;
                    }
                }

                if(!v_boolean_merged)
                    v_arraylist_versions.add(v_version_pkgversion);
            }
        }

        return v_arraylist_versions;
    }

    /**
     * Procura e retorna pacote dado um nome.
     * @param name Nome do pacote.
     * @return Pacote.
     */
    public Package getPackageByName(String name)
    {
        Package v_package_pkg;
        Iterator v_iterator_it;

        v_iterator_it = this._packagelist.iterator();
        while(v_iterator_it.hasNext())
        {
            v_package_pkg = (Package)v_iterator_it.next();

            if(v_package_pkg.get_name().equals(name))
              return v_package_pkg;
        }

        return null;
    }


    /**
     * Retorna a maior versão existente na lista.
     * @return Objeto versão.
     */
    public Package getPreviousPackage(Package p)
    {
        int v_int_index_p;
        ArrayList v_arraylist_sortedpackages;

        if(this._packagelist.isEmpty())
            return null;

        v_arraylist_sortedpackages = new ArrayList(this._packagelist);
        Collections.sort(v_arraylist_sortedpackages,Package.get_comparator());

        v_int_index_p = v_arraylist_sortedpackages.indexOf(p);

        if(v_int_index_p < v_arraylist_sortedpackages.size()-1)
        {
           return (Package) v_arraylist_sortedpackages.get((v_int_index_p+1));
        }
        else
            return null;
    }

    /**
     * Retorna pacote de execução para o final da instalação.
     * @return Pacote Final.
     */
    public PackageFinal get_final_package() {
        return _finalpkt;
    }
    
    /**
     * Retorna pacote de execu��o para o inicio da instala��o.
     * @return Pacote Inicial.
     */
    public PackageInitial get_initial_package() {
        return _initialpkt;
    }
}
