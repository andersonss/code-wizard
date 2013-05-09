package br.ufal.ic.ctree.nosconcretos;

import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.visitors.CTreeVisitor;

public class IfNode extends CTree{

	public IfNode(CTree noPai) {
		super(noPai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(CTreeVisitor visitor) {
		visitor.visit(this);		
	}

}
