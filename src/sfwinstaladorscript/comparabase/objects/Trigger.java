package sfwinstaladorscript.comparabase.objects;

public class Trigger {

    private String TRIGGER_NAME;
    private String TRIGGER_TYPE;
    private String TRIGGERING_EVENT;
    private String TABLE_NAME;
    private int CARACTER_COUNT;

    public Trigger() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retorna o nome da trigger;
     *
     * @return String
     */
    public String get_triggerName() {
        return TRIGGER_NAME;
    }

    /**
     * Seta o nome da trigger;
     *
     * @param _triggerName
     */
    public void set_triggerName(String _triggerName) {
        this.TRIGGER_NAME = _triggerName;
    }

    /**
     * Retorna o tipo da trigger;
     *
     * @return String
     */
    public String get_triggerType() {
        return TRIGGER_TYPE;
    }

    /**
     * Seta o tipo da trigger;
     *
     * @param _triggerType
     */
    public void set_triggerType(String _triggerType) {
        this.TRIGGER_TYPE = _triggerType;
    }

    /**
     * Retorna o evento da trigger;
     *
     * @return
     */
    public String get_triggerEvent() {
        return TRIGGERING_EVENT;
    }

    /**
     * Seta o evento da trigger;
     *
     * @param _triggerEvent
     */
    public void set_triggerEvent(String _triggerEvent) {
        this.TRIGGERING_EVENT = _triggerEvent;
    }

    /**
     * Retorna o nome da tabela que referencia a trigger;
     *
     * @return String
     */
    public String get_tableName() {
        return TABLE_NAME;
    }

    /**
     * 
     * @return
     */
    public int getCARACTER_COUNT() {
        return CARACTER_COUNT;
    }

    /**
     *
     * @param CARACTER_COUNT
     */
    public void setCARACTER_COUNT(int CARACTER_COUNT) {
        this.CARACTER_COUNT = CARACTER_COUNT;
    }

    /**
     * Seta o nome da tabela que referencia a trigger;
     *
     * @param _tableName
     */
    public void set_tableName(String _tableName) {
        this.TABLE_NAME = _tableName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Trigger) {
            Trigger proc = (Trigger) obj;
            return proc.get_triggerName().equals(this.get_triggerName());
        } else {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equalsType(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Trigger) {
            Trigger proc = (Trigger) obj;
            return proc.get_triggerType().equals(this.get_triggerType());
        } else {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equalsEvent(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Trigger) {
            Trigger proc = (Trigger) obj;
            return proc.get_triggerEvent().equals(this.get_triggerEvent());
        } else {
            return false;
        }
    }

    /**
     * 
     * @param obj
     * @return
     */
    public boolean equalsTextLength(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Trigger) {
            Trigger proc = (Trigger) obj;
            return proc.getCARACTER_COUNT() == this.getCARACTER_COUNT();
        } else {
            return false;
        }
    }
}
