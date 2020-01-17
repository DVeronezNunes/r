package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class View implements Objects {
	private String	VIEW_NAME;
	private int			TEXT_LENGTH;

	public View() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da View;
	 * 
	 * @return String
	 */
	public String get_viewName() {
		return VIEW_NAME;
	}

	/**
	 * Seta o nome da View;
	 * 
	 * @param _viewName
	 */
	public void set_viewName(String _viewName) {
		this.VIEW_NAME = _viewName;
	}

	/**
	 * Retorna a quantidade de caracter da view;
	 * 
	 * @return int
	 */
	public int get_textLength() {
		return TEXT_LENGTH;
	}

	/**
	 * Seta a quantidade de caracter da view;
	 * 
	 * @param _textLength
	 */
	public void set_textLength(int _textLength) {
		this.TEXT_LENGTH = _textLength;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof View) {
			View proc = (View) obj;
			return proc.get_viewName().equals(this.get_viewName());
		} else {
			return false;
		}
	}

	/**
	 * Compara o TEXT_LENGTH de cada view
	 * 
	 * @param obj
	 * @return true ou false
	 */
	public boolean equalsTextLength(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof View) {
			View proc = (View) obj;
			return proc.get_textLength() == this.get_textLength();
		} else {
			return false;
		}
	}
}
