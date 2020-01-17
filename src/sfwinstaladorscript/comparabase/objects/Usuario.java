package sfwinstaladorscript.comparabase.objects;

public class Usuario {
	// Espaço para Variáveis
	private String							_usuario;
	private String							_password;
	private String							_tns;	
	// Fim do Espaço para Variáveis
	//
	// Construtor da Classe
	public Usuario() {
	}
	public String get_usuario() {
		return _usuario;
	}
	public void set_usuario(String _usuario) {
		this._usuario = _usuario;
	}
	public String get_password() {
		return _password;
	}
	public void set_password(String _password) {
		this._password = _password;
	}
	public String get_tns() {
		return _tns;
	}
	public void set_tns(String _tns) {
		this._tns = _tns;
	}
	
}
