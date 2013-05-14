package br.ufal.ic.ctree.visitors;

import java.util.Stack;

import br.ufal.ic.ctree.nosconcretos.ElseIfNode;
import br.ufal.ic.ctree.nosconcretos.ElseNode;
import br.ufal.ic.ctree.nosconcretos.ExpressionNode;
import br.ufal.ic.ctree.nosconcretos.ForNode;
import br.ufal.ic.ctree.nosconcretos.FunctionCallNode;
import br.ufal.ic.ctree.nosconcretos.FunctionDeclarationNode;
import br.ufal.ic.ctree.nosconcretos.IfNode;
import br.ufal.ic.ctree.nosconcretos.PrintNode;
import br.ufal.ic.ctree.nosconcretos.RootNode;
import br.ufal.ic.ctree.nosconcretos.ScanNode;
import br.ufal.ic.ctree.nosconcretos.StatementsNode;
import br.ufal.ic.ctree.nosconcretos.TerminalNode;
import br.ufal.ic.ctree.nosconcretos.VariableDeclarationNode;
import br.ufal.ic.ctree.nosconcretos.WhileNode;
import br.ufal.ic.ctree.visitors.analyzeobjects.ForTabelaDeAnalise;

public class ForAnalyzeVisitor implements CTreeVisitor {

	private Stack<ForTabelaDeAnalise> forStackAnalyzing;
	private Stack<ForTabelaDeAnalise> forStack;
	
	public ForAnalyzeVisitor() {
		forStackAnalyzing = new Stack<ForTabelaDeAnalise>();
		forStack = new Stack<ForTabelaDeAnalise>();
	}
	@Override
	public void visit(RootNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ElseIfNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ElseNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExpressionNode node) {
		if(forStackAnalyzing.peek().getStateReading()){
			forStackAnalyzing.peek().expression += node.getExpression();
		}
		
	}

	@Override
	public void visit(ForNode node) {
		if(!forStackAnalyzing.isEmpty()){
			if(!forStackAnalyzing.peek().getStateReading()){
				forStack.push(forStackAnalyzing.pop());
			}
		}
		ForTabelaDeAnalise forAux = new ForTabelaDeAnalise(node.getProfundidade());
		forStackAnalyzing.push(forAux);
		
		
	}

	@Override
	public void visit(FunctionCallNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FunctionDeclarationNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IfNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PrintNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ScanNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StatementsNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TerminalNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VariableDeclarationNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhileNode node) {
		// TODO Auto-generated method stub
		
	}
	

}
