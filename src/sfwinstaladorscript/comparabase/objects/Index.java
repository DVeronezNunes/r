package sfwinstaladorscript.comparabase.objects;

public class Index {
	
	private String	INDEX_NAME;

	public Index() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retorna o nome da index;
	 * 
	 * @return String
	 */
	public String get_indexName() {
		return INDEX_NAME;
	}

	/**
	 * Seta o nome da index;
	 * 
	 * @param _indexName
	 */
	public void set_indexName(String _indexName) {
		this.INDEX_NAME = _indexName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Index) {
			Index proc = (Index) obj;
			return proc.get_indexName().equals(this.get_indexName());
		} else {
			return false;
		}
	}
}
