package br.ufal.ic.ctree;

import br.ufal.ic.compiladores.tabela.TabelaDeAnalize;

public interface ICTree {
	
	public void print();
	public int getProfundidade();
	public void analyze(TabelaDeAnalize tabela);
	public NodeType getNodeType();


}
