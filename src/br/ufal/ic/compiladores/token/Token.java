package br.ufal.ic.compiladores.token;

public class Token {

	private final ClasseToken classeToken;
	private final String valorToken;
	private final int posLinha;
	private final int posColuna;

	public Token(ClasseToken classe, String valor, int posLinha, int posColuna) {
		this.classeToken = classe;
		this.valorToken = valor;
		this.posLinha = posLinha;
		this.posColuna = posColuna;
	}

	public ClasseToken getClasseToken() {
		return classeToken;
	}

	public int getPosColuna() {
		return posColuna;
	}

	public int getPosLinha() {
		return posLinha;
	}

	public String getValorToken() {
		return valorToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classeToken == null) ? 0 : classeToken.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (classeToken != other.classeToken)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + classeToken + ", " + valorToken + ", Linha = " + posLinha
				+ ", Coluna = " + posColuna + "]";
	}
}
