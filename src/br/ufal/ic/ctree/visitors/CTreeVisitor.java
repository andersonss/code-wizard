package br.ufal.ic.ctree.visitors;

import br.ufal.ic.ctree.nosconcretos.*;

public interface CTreeVisitor {
	
	public void visit(RootNode node);
	public void visit(ElseIfNode node);
	public void visit(ElseNode node);
	public void visit(ExpressionNode node);
	public void visit(ForNode node);
	public void visit(FunctionCallNode node);
	public void visit(FunctionDeclarationNode node);
	public void visit(IfNode node);
	public void visit(PrintNode node);
	public void visit(ScanNode node);
	public void visit(StatementsNode node);
	public void visit(TerminalNode node);
	public void visit(VariableDeclarationNode node);
	public void visit(WhileNode node);


}
