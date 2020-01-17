package sfwinstaladorscript.objects;

import sfwinstaladorscript.interfaces.SfwComboBoxItem;

/**
 * Representa um sistema operacional suportado.
 */
public class SystemOS implements SfwComboBoxItem
{

    /**
     * Nome do sistema operacional.
     */
    private String _name;

    /**
     * Extensão do sistema operacional.
     */
    private String _exec_ext;

    /**
     * Retorna nome do sistema operacional.
     * @return Nome do OS.
     */
    public String get_name() {
        return _name;
    }

    /**
     * Altera nome do sistema operacional.
     * @param _name Nome do OS.
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Descrição para o SfwComboBox.
     * @return Nome do sistema operacional.
     */
    public String get_description() {
        return this.get_name();
    }

    /**
     * Retorna extensão do executável do Sistema Operacional.
     * @deprecated
     */
    public String get_exec_ext() {
        return _exec_ext;
    }

    /**
     * Altera extensão do executável do Sistema Operacional.
     * @deprecated
     */
    public void set_exec_ext(String _exec_ext) {
        this._exec_ext = _exec_ext;
    }
}
