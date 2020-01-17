package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Function implements Objects {
	private String	FUNCTION_NAME;
	private int			CARACTER_COUNT;

	/**
	 * Retorna a quantidade de caracter da procedure;
	 * 
	 * @return int A quantidade de caracter da function
	 */
	public int getCARACTER_COUNT() {
		return CARACTER_COUNT;
	}

	/**
	 * Seta a quantidade de caracter da function
	 * 
	 * @param cARACTER_COUNT
	 */
	public void setCARACTER_COUNT(int cARACTER_COUNT) {
		CARACTER_COUNT = cARACTER_COUNT;
	}

	/**
	 * Retorna o nome da function;
	 * 
	 * @return String
	 * 
	 *         Nome da função
	 */
	public String get_functionName() {
		return FUNCTION_NAME;
	}

	/**
	 * Seta o nome da function;
	 * 
	 * @param String
	 *          nome da função
	 */
	public void set_functionName(String _functionName) {
		this.FUNCTION_NAME = _functionName;
	}

	/**
	 * Compara o nome da function;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Function) {
			Function proc = (Function) obj;
			return proc.get_functionName().equals(this.get_functionName());
		} else {
			return false;
		}
	}

	/**
	 * Compara o TEXT_LENGTH de cada Function
	 * 
	 * @param obj
	 * @return true ou false
	 */
	public boolean equalsTextLength(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Function) {
			Function proc = (Function) obj;
			return proc.getCARACTER_COUNT() == this.getCARACTER_COUNT();
		} else {
			return false;
		}
	}
}
