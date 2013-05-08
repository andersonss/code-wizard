package br.ufal.ic.ctree.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import br.ufal.ic.ctree.CTree;

public class DepthFirstIterator extends CTreeIterator {
	
	private Stack<CTree> stack;
	
	public DepthFirstIterator(CTree node) {
		super(node);
		stack = new Stack<CTree>();
		stack.add(rootNode);
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

}
