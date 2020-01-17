package sfwinstaladorscript.comparabase.objects;

public class Info {
	private int			COD_PRODUTO;
	private String	VERSAO;

	public Info() {
	}

	/**
	 * Retorna o Código do Produto;
	 * 
	 * @return int
	 */
	public int getCOD_PRODUTO() {
		return COD_PRODUTO;
	}

	/**
	 * Seta o código do Produto;
	 * 
	 * @param cOD_PRODUTO
	 */
	public void setCOD_PRODUTO(int COD_PRODUTO) {
		this.COD_PRODUTO = COD_PRODUTO;
	}

	/**
	 * Retorna a versão da comparação;
	 * 
	 * @return
	 */
	public String getVERSAO() {
		return VERSAO;
	}

	/**
	 * Seta a versão da comparação;
	 * 
	 * @param VERSAO
	 */
	public void setVERSAO(String VERSAO) {
		this.VERSAO = VERSAO;
	}
}
