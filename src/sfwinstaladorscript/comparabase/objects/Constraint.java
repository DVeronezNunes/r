package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Constraint implements Objects {
	private String	CONSTRAINT_NAME;
	private String	CONSTRAINT_TYPE;

	public Constraint() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da constraint;
	 * 
	 * @return String
	 */
	public String get_constraintName() {
		return CONSTRAINT_NAME;
	}

	/**
	 * Seta o nome da constraint
	 * 
	 * @param _constraintName
	 */
	public void set_constraintName(String _constraintName) {
		this.CONSTRAINT_NAME = _constraintName;
	}

	/**
	 * Retorna o tipo da constraint;
	 * 
	 * @return String
	 */
	public String get_constraintType() {
		return CONSTRAINT_TYPE;
	}

	/**
	 * Seta o tipo da constraint;
	 * 
	 * @param _constraintType
	 */
	public void set_constraintType(String _constraintType) {
		this.CONSTRAINT_TYPE = _constraintType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Constraint) {
			Constraint proc = (Constraint) obj;
			return proc.get_constraintName().equals(this.get_constraintName());
		} else {
			return false;
		}
	}

	/**
	 * Compara o tipo da constraint
	 * 
	 * @param obj
	 * @return true ou false
	 */
	public boolean equalsType(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Constraint) {
			Constraint proc = (Constraint) obj;
			return proc.get_constraintType().equals(this.get_constraintType());
		} else {
			return false;
		}
	}
}
