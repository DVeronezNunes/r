package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Procedure implements Objects {

    private String NAME;
    private int CARACTER_COUNT;

    public Procedure() {
    }

    /**
     * Retorna o nome da procedure;
     *
     * @return String
     */
    public String get_procedureName() {
        return NAME;
    }

    /**
     * Seta o nome da procedure;
     *
     * @param _procedureName
     */
    public void set_procedureName(String _procedureName) {
        this.NAME = _procedureName;
    }

    /**
     * Retorna a quantidade de caracter da procedure;
     *
     * @return String Quantidade de caracter
     */
    public int getCARACTER_COUNT() {
        return CARACTER_COUNT;
    }

    /**
     * Seta a quantidade de caracter da procedure;
     *
     * @param cARACTER_COUNT
     */
    public void setCARACTER_COUNT(int cARACTER_COUNT) {
        CARACTER_COUNT = cARACTER_COUNT;
    }

    /**
     * Compara o atributo nome da Procedure;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Procedure) {
            Procedure proc = (Procedure) obj;
            return proc.get_procedureName().equals(this.get_procedureName());
        } else {
            return false;
        }
    }

    /**
     * Compara o TEXT_LENGTH de cada Procedure
     *
     * @param obj
     * @return true ou false
     */
    public boolean equalsTextLength(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Procedure) {
            Procedure proc = (Procedure) obj;
            return proc.getCARACTER_COUNT() == this.getCARACTER_COUNT();
        } else {
            return false;
        }
    }
}
