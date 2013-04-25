package br.ufal.ic.ctree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ufal.ic.compiladores.tabela.TabelaDeAnalise;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;

public class CTree implements ICTree {

	private NodeType nodeType;
	private final ICTree nodePai;
	private final int profundidade;
	private List<ICTree> nosFilhos;
	
	public CTree(){
		this.nodeType = NodeType.ROOT;
		nodePai = null;
		profundidade = 0;
		nosFilhos = new LinkedList<ICTree>();
	}

	public CTree(ICTree nodePai, NodeType type) {
		this.nodePai = nodePai;
		this.profundidade = nodePai.getProfundidade() + 1;
		this.nodeType = type;
		nosFilhos = new LinkedList<ICTree>();
	}
	
	public List<ICTree> getNosFilhos(){
		return nosFilhos;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void addNode(ICTree node) {
		this.nosFilhos.add(node);

	}

	@Override
	public void print() {
		System.out.print("\nNoPai =>" + nodeType + " - ");
		Iterator<ICTree> itr = nosFilhos.iterator();
		while(itr.hasNext()){
			ICTree node = itr.next();
			node.print();

		}
		

	}

	public int getProfundidade() {
		return profundidade;
	}
	
	public int getQuantidadeFilhos(){
		return nosFilhos.size();
	}

	@Override
	public void analyze(TabelaDeAnalise tabela) {
		for(ICTree node : nosFilhos){
			switch(node.getNodeType()){
			case DCL_MAIN:
			case DCL_FUNC:
				tabela.incrementeQuantidadeDeFuncoes();
				break;
			case FOR:
				tabela.incrementeQuantidadeDeFor();
				break;
			case WHILE:
				tabela.incrementeQuantidadeDeWhiles();
				break;
			case IF:
				tabela.incrementeQuantidadeDeIf();
				break;
			case ELSE_IF:
				tabela.incrementeQuantidadeDeElseIf();
				break;
			case ELSE:
				tabela.incrementeQuantidadeDeElse();
				break;
			case DCL_VAR:
				List<ICTree> dclNode = ((CTree) node).getNosFilhos();
				int quant = 0;
				for(ICTree aux : dclNode){
					if(aux instanceof CNode){
						Token token = ((CNode) aux).getNodeToken();
						if(token.getClasseToken() == ClasseToken.ID){
							quant++;
						}
					}
				}
				tabela.incrementeQuantidadeDeVariaveis(quant);
				break;
			case PRINTF:
				tabela.incrementeQuantidadeDePrinf();
				break;
			case SCANF:
				tabela.incrementeQuantidadeDeScanf();
				break;
			default:
				tabela.setProfundidade(profundidade);
				tabela.incrementeQuantidadeDeNos();
				break;
			}
			node.analyze(tabela);
		}
		
	}

}
