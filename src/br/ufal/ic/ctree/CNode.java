package br.ufal.ic.ctree;

import br.ufal.ic.compiladores.token.Token;

public class CNode implements ICTree {
	
	private Token token;
	
	public CNode(Token token) {
		this.token = token;
	}

}
