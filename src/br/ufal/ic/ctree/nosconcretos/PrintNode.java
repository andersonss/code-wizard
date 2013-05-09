package br.ufal.ic.ctree.nosconcretos;

import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.visitors.CTreeVisitor;

public class PrintNode extends CTree{

	public PrintNode(CTree noPai) {
		super(noPai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(CTreeVisitor visitor) {
		visitor.visit(this);		
	}

}
