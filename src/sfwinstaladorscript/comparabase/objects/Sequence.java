package sfwinstaladorscript.comparabase.objects;

public class Sequence {

    private String SEQUENCE_NAME;

    public Sequence() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retorna o nome da Sequence;
     *
     * @return String
     */
    public String get_sequenceName() {
        return SEQUENCE_NAME;
    }

    /**
     * Seta o nome da sequence;
     *
     * @param _sequenceName
     */
    public void set_sequenceName(String _sequenceName) {
        this.SEQUENCE_NAME = _sequenceName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Sequence) {
            Sequence proc = (Sequence) obj;
            return proc.get_sequenceName().equals(this.get_sequenceName());
        } else {
            return false;
        }
    }
}
