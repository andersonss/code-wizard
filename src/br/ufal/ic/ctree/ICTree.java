package br.ufal.ic.ctree;

import br.ufal.ic.compiladores.tabela.TabelaDeAnalise;

public interface ICTree {
	
	public void print();
	public int getProfundidade();
	public void analyze(TabelaDeAnalise tabela);
	public NodeType getNodeType();


}
