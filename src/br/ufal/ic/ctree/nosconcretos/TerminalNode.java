package br.ufal.ic.ctree.nosconcretos;

import java.util.Iterator;

import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.iterators.NullIterator;

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
	@Override
	public Iterator iterator() {
		return new NullIterator(null);
	}
}
