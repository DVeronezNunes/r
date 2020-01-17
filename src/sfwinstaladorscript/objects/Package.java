package sfwinstaladorscript.objects;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;
import sfwinstaladorscript.Utils;

/**
 * Representa um pacote de instalação.
 */
public class Package implements SfwComboBoxItem
{

    /**
     * Nome do pacote de instalação.
     */
    private String _name;

    /**
     * Extensão do pacote de instalação.
     */
    private String _extension;

    /**
     * Data de liberação do pacote de instalação.
     */
    private Calendar _date;

    /**
     * Arquivo zip do pacote de instalação.
     */
    private ZipFile _zip;
    private Hashtable _versions;

    /**
     * Comparador para a classe Package.
     */
    public static class PackageComparator
      implements Comparator {
    public int compare(Object element1,
        Object element2) {
      return ((Package)element2).get_date().compareTo(((Package)element1).get_date());
    }
  }

    public Package()
    {
        this._extension = "zip";
    }

    /**
     * Retorna nome do pacote.
     * @return Nome do pacote.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera nome do pacote.
     * @param _name Nome do pacote.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Retorna data do pacote.
     * @return Data do pacote.
     */
    public Calendar get_date() {
        return _date;
    }

    /**
     * Altera data do pacote.
     * @param _date Data do pacote.
     */
    public void set_date(Calendar _date) {
        this._date = _date;
    }

    /**
     * Retorna arquivo zip do pacote.
     * @return Arquivo zip.
     */
    public ZipFile get_zip() {
        return _zip;
    }

    /**
     * Altera arquivo zip do pacote.
     * @param _zip Arquivo zip.
     */
    public void set_zip(ZipFile _zip) {
        this._zip = _zip;
    }

    /**
     * Descrição para o SfwComboBox.
     * @return Nome do cliente.
     */
    public String get_description() {
        return Utils.getMonthName((this._date.get(Calendar.MONTH)+1)).toUpperCase()+" "+this._date.get(Calendar.YEAR);
    }

    /**
     * Lê versões disponíveis no pacote por produto.
     * @param products Lista de produtos.
     */
    public void readVersions(ArrayList products)
    {
        Product v_product_p;
        Version v_version_v;
        Iterator v_iterator_it;
        VersionList v_versionlist_list;
        InputStream v_inputstream_xml;
        productinfo.ProductinfoDocument v_productinfodocument_info;
        productinfo.Version[] v_arr_version_versions;

        this._versions = new Hashtable();

        v_iterator_it = products.iterator();
        while(v_iterator_it.hasNext())
        {
            v_product_p = (Product)v_iterator_it.next();

            v_versionlist_list = new VersionList();

            try {
                v_inputstream_xml = this._zip.getInputStream(new ZipEntry(v_product_p.get_folder()+"/info.xml"));
                v_productinfodocument_info = productinfo.ProductinfoDocument.Factory.parse(v_inputstream_xml);
                v_arr_version_versions = v_productinfodocument_info.getProductinfo().getVersionArray();
                for(int i=0; i < v_arr_version_versions.length; i++)
                {
                    v_version_v = new Version(v_arr_version_versions[i], this.get_name(), this.get_extension());
                    v_versionlist_list.addVersion(v_version_v);
                }

            }catch (FileNotFoundException ex) {            
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

            this._versions.put(v_product_p.get_name(), v_versionlist_list);
        }
    }

    /**
     * Retorna hashtable de versões por produto do pacote.
     * @return Hashtable de versões.
     */
    public Hashtable get_versions() {
        return _versions;
    }

    /**
     * Retorna comparador para ordenação de lista.
     * @return Comparador dos clientes.
     */
    public static Comparator get_comparator()
    {
        return new PackageComparator();
    }

    /**
     * Retorna extensão do pacote.
     * @return Extensão do pacote.
     */
    public String get_extension() {
        return _extension;
    }

    /**
     * Altera extensão do pacote
     * @param _extension Extensão do pacote.
     */
    public void set_extension(String _extension) {
        this._extension = _extension;
    }


}
