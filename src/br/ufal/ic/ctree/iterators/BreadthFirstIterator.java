package br.ufal.ic.ctree.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.ctree.CTree;

public class BreadthFirstIterator extends CTreeIterator {
	
	private Queue<CTree> queue;
	
	public BreadthFirstIterator(CTree node) {
		super(node);
		queue = new LinkedList<CTree>();
		queue.add(rootNode);
	}

	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
	}
	

	@Override
	public CTree next() {
		CTree head = queue.poll();
		Iterator<CTree> iterator = head.iterator();
		while(iterator.hasNext()){
			queue.add(iterator.next());
			
		}
		return head;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
