package br.ufal.ic.ctree.nosconcretos;

import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CTree;

public class TerminalNode extends CTree{

	private Token token;
	
	public TerminalNode(CTree noPai, Token token) {
		super(noPai);
		this.token = token;
	}
	
	@Override
	public void print() {
		System.out.println("Token = " + token.getClasseToken());
		super.print();
	}

}
