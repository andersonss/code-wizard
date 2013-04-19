package br.ufal.ic.ctree;

import java.util.ArrayDeque;
import java.util.Deque;

public class CTree implements ICTree {
		
	private NodeType nodeType; 
	
	private Deque<ICTree> stack;
	
	public CTree(NodeType type) {
		this.nodeType = type;
		stack = new ArrayDeque<ICTree>();
	}

	public void addNode(ICTree node) {
		this.stack.add(node);

	}

}
