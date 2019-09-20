package controller;

/** Classe para o iniciar o objeto Poltrona.
 * @author raquelms203
 *
 */
public class Poltrona {
	
	private String nome;
	private int posicao;
	private boolean status;

	public Poltrona(String nome, int posicao, boolean status) {
		this.nome = nome;
		this.posicao = posicao;
		this.status = status;
	}

	/**
	 * Getters e Setters.
	 */
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
