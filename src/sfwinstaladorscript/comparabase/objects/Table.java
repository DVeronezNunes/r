package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Table implements Objects{
	private String	TABLE_NAME;

	public Table() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da tabela;
	 * 
	 * @return
	 */
	public String get_tableName() {
		return TABLE_NAME;
	}

	/**
	 * Seta o nome da tabela;
	 * 
	 * @param _tableName
	 */
	public void set_tableName(String _tableName) {
		this.TABLE_NAME = _tableName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Table) {
			Table proc = (Table) obj;
			return proc.get_tableName().equals(this.get_tableName());
		} else {
			return false;
		}
	}
}
