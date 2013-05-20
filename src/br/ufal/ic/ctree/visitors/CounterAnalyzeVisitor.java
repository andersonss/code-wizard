package br.ufal.ic.ctree.visitors;

import java.util.Iterator;

import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.ctree.CTree;
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
import br.ufal.ic.ctree.visitors.analyzeobjects.TabelaDeAnalise;

public class CounterAnalyzeVisitor implements CTreeVisitor {
	
	private TabelaDeAnalise tabela;
	
	
	
	public TabelaDeAnalise getTabelaDeAnalise(){
		return tabela;
	}
	
	public CounterAnalyzeVisitor() {
		tabela = new TabelaDeAnalise();
	}

	@Override
	public void visit(RootNode node) {
		
	}

	@Override
	public void visit(ElseIfNode node) {
		tabela.incrementeQuantidadeDeElseIf();
	}

	@Override
	public void visit(ElseNode node) {
		tabela.incrementeQuantidadeDeElse();
		
	}

	@Override
	public void visit(ExpressionNode node) {

		
	}

	@Override
	public void visit(ForNode node) {
		tabela.incrementeQuantidadeDeFor();		
	}

	@Override
	public void visit(FunctionCallNode node) {
		tabela.incrementeQuantidadeDeChamadasDeFuncoes();
		
	}

	@Override
	public void visit(FunctionDeclarationNode node) {
		tabela.incrementeQuantidadeDeFuncoes();
		
	}

	@Override
	public void visit(IfNode node) {
		tabela.incrementeQuantidadeDeIf();
		
	}

	@Override
	public void visit(PrintNode node) {
		tabela.incrementeQuantidadeDePrintf();
		
	}

	@Override
	public void visit(ScanNode node) {
		tabela.incrementeQuantidadeDeScanf();
		
	}

	@Override
	public void visit(StatementsNode node) {
		
	}

	@Override
	public void visit(TerminalNode node) {
		tabela.incrementeQuantidadeDeNosTerminais();
		tabela.setProfundidade(node.getProfundidade());
		tabela.addLineOfCode(node.getToken().getPosLinha());
	}

	@Override
	public void visit(VariableDeclarationNode node) {
		Iterator<CTree> itr = node.iterator();
		CTree aux = null;
		while(itr.hasNext()){
			aux = itr.next();
			if(aux instanceof TerminalNode){
				if(((TerminalNode) aux).getClasseToken() == ClasseToken.ID){
					tabela.incrementeQuantidadeDeVariaveis();
				}
			}
		}
		
		
	}

	@Override
	public void visit(WhileNode node) {
		tabela.incrementeQuantidadeDeWhiles();
		
	}

}
