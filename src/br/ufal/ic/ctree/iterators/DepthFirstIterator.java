package br.ufal.ic.ctree.iterators;

import java.util.Iterator;
import java.util.Stack;

import br.ufal.ic.ctree.CTree;

public class DepthFirstIterator extends CTreeIterator {
	
	private Stack<CTree> stack;
	
	public DepthFirstIterator(CTree node) {
		super(node);
		stack = new Stack<CTree>();
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}
	

	@Override
	public CTree next() {
		CTree head = stack.pop();
		Iterator<CTree> iterator = head.iterator();
		while(iterator.hasNext()){
			stack.push(iterator.next());
			
		}
		return head;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartNode(int nodeId) {
		stack.clear();
		stack.push(rootNode);
		while(this.hasNext()){
			CTree aux = this.next();
			if(aux.getNodeId() == nodeId){
				stack.clear();
				stack.push(aux);
				break;
			}
		}
	}

}
