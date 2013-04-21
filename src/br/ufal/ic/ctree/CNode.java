package br.ufal.ic.ctree;

import br.ufal.ic.compiladores.token.Token;

public class CNode implements ICTree {
	
	private Token token;
	private final ICTree nodePai;
	private final int profundidade;
	
	public CNode(ICTree nodePai, Token token) {
		this.nodePai = nodePai;
		this.profundidade = nodePai.getProfundidade();
		this.token = token;
	}

	@Override
	public void print() {
		System.out.print(this.token.getClasseToken()+":"+profundidade + " | ");
	}
	
	@Override
	public int getProfundidade() {
		return profundidade;
	}

}
