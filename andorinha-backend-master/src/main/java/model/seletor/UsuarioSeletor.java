package model.seletor;

public class UsuarioSeletor extends AbstractBaseSeletor{

	private Integer id;
	private String nome;

	private Integer limite;
	private Integer pagina;

	public boolean possuiFiltro() {
		return this.id != null || (this.nome != null && !this.nome.trim().isEmpty() );
	}

	public boolean possuiPaginacao() {
		return this.pagina > 0 && this.limite > 0;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getLimite() {
		return limite;
	}

	public void setLimite(int limite) {
		this.limite = limite;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

}
