package br.ufal.ic.ctree.nosconcretos;

import java.util.Iterator;

import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.iterators.NullIterator;
import br.ufal.ic.ctree.visitors.CTreeVisitor;

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
	public Iterator<CTree> iterator() {
		return new NullIterator(null);
	}

	@Override
	public void accept(CTreeVisitor visitor) {
		visitor.visit(this);		
	}
}
