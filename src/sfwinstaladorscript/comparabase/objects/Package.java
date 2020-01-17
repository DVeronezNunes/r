package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Package implements Objects {
	private String	NAME;
	private int			LINE_COUNT;

	public Package() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da Package;
	 * 
	 * @return String
	 */
	public String get_packageName() {
		return NAME;
	}

	/**
	 * Seta o nome da package;
	 * 
	 * @param _packageName
	 */
	public void set_packageName(String _packageName) {
		this.NAME = _packageName;
	}

	/**
	 * Retorna a quantidade de linha;
	 * 
	 * @return String
	 */
	public int get_lineCount() {
		return LINE_COUNT;
	}

	/**
	 * Seta a quantidade de linhas da package;
	 * 
	 * @param _lineCount
	 */
	public void set_lineCount(int _lineCount) {
		this.LINE_COUNT = _lineCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Package) {
			Package proc = (Package) obj;
			return proc.get_packageName().equals(this.get_packageName());
		} else {
			return false;
		}
	}
	
	public boolean equalsLineCount(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Package) {
			Package proc = (Package) obj;
			return proc.get_lineCount() == this.get_lineCount();
		} else {
			return false;
		}
	}
}
