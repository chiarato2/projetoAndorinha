package model.dto;

import java.util.Calendar;

public class TweetDTO {
	
	private int id;
	private int idUsuario;
	private Calendar data;
	private String nomeUsuario;
	private String conteudo;
	
	public TweetDTO(int id, int idUsuario, Calendar data, String nomeUsuario, String conteudo) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.data = data;
		this.nomeUsuario = nomeUsuario;
		this.conteudo = conteudo;
	}

	public TweetDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	
}
