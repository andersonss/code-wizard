package br.ufal.ic.ctree.nosconcretos;

import java.util.Iterator;

import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.visitors.CTreeVisitor;

public class ExpressionNode extends CTree{

	public ExpressionNode(CTree noPai) {
		super(noPai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(CTreeVisitor visitor) {
		visitor.visit(this);		
	}
	
	public String getExpression(){
		String expression = "";
		Iterator<CTree> itr = this.iterator();
		CTree aux;
		while(itr.hasNext()){
			aux = itr.next();
			if(aux instanceof TerminalNode){
				expression += ((TerminalNode) aux).getToken().getValorToken();
			}else {
				expression +=((ExpressionNode) aux).getExpression();
			}
		}
		return expression;
	}

}
