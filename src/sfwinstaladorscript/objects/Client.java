package sfwinstaladorscript.objects;

import java.util.Comparator;
import sfwinstaladorscript.interfaces.SfwComboBoxItem;

/**
 * Representa um Cliente Softway.
 */
public class Client implements SfwComboBoxItem
{
    /**
     * Nome do Cliente.
     */
    private String _name;

    /**
     * Abreviação do Cliente.
     */
    private String _shortname;

    /**
     * Comparador para a classe Client.
     */
    public static class ClientComparator
      implements Comparator {
    public int compare(Object element1,
        Object element2) {
      String v_string_lower1 = ((Client)element1).get_name().
        toLowerCase();
      String v_string_lower2 = ((Client)element2).get_name().
        toLowerCase();
      return v_string_lower1.compareTo(v_string_lower2);
    }
  }

    /**
     * Retorna nome do cliente.
     * @return Nome do cliente.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera nome do cliente.
     * @param _name Nome do cliente.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Retorna sigla padrão do cliente.
     * @return Sigla padrão do cliente.
     */
    public String get_shortname() {
        return _shortname;
    }

    /**
     * Altera sigla padrão do cliente.
     * @param _shortname Sigla do cliente.
     */
    public void set_shortname(String _shortname) {
        this._shortname = _shortname;
    }

    /**
     * Descrição para o SfwComboBox.
     * @return Nome do cliente.
     */
    public String get_description()
    {
        return this.get_name();
    }

    /**
     * Retorna comparador para ordenação de lista.
     * @return Comparador dos clientes.
     */
    public static Comparator get_comparator()
    {
        return new ClientComparator();
    }

}
