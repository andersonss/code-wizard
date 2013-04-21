package br.ufal.ic.ctree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class CTree implements ICTree {

	private NodeType nodeType;
	private final ICTree nodePai;
	private final int profundidade;
	private Deque<ICTree> stack;
	
	public CTree(){
		this.nodeType = NodeType.ROOT;
		nodePai = null;
		profundidade = 0;
		stack = new ArrayDeque<ICTree>();
	}

	public CTree(ICTree nodePai, NodeType type) {
		this.nodePai = nodePai;
		this.profundidade = nodePai.getProfundidade() + 1;
		this.nodeType = type;
		stack = new ArrayDeque<ICTree>();
	}

	public void addNode(ICTree node) {
		this.stack.add(node);

	}

	@Override
	public void print() {
		System.out.print("\nNoPai =>" + nodeType + " - ");
		Iterator<ICTree> itr = stack.iterator();
		while(itr.hasNext()){
			ICTree node = itr.next();
			node.print();

		}
		

	}

	public int getProfundidade() {
		return profundidade;
	}

}
