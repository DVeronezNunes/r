package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

public class Column implements Objects {
	private String	COLUMN_NAME;
	private String	DATA_TYPE;

	// Construtor da classe column.
	public Column() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da coluna;
	 * 
	 * @return String
	 */
	public String get_columnName() {
		return COLUMN_NAME;
	}

	/**
	 * Seta o nome da coluna;
	 * 
	 * @param _columnName
	 */
	public void set_columnName(String _columnName) {
		this.COLUMN_NAME = _columnName;
	}

	/**
	 * Retorna o tipo da coluna;
	 * 
	 * @return String
	 */
	public String get_dataType() {
		return DATA_TYPE;
	}

	/**
	 * Seta o tipo da coluna;
	 * 
	 * @param _dataType
	 */
	public void set_dataType(String _dataType) {
		this.DATA_TYPE = _dataType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Column) {
			Column proc = (Column) obj;
			return proc.get_columnName().equals(this.get_columnName());
		} else {
			return false;
		}
	}

	/**
	 * Compara o tipo das colunas
	 * 
	 * @param obj
	 * @return true ou false
	 */
	public boolean equalsDataType(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Column) {
			Column proc = (Column) obj;
			return proc.get_dataType().equals(this.get_dataType());
		} else {
			return false;
		}
	}

}
