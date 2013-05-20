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

	@Override
	public void setStartNode(int nodeId) {
		queue.clear();
		queue.add(rootNode);
		while(this.hasNext()){
			CTree aux = this.next();
			if(aux.getNodeId() == nodeId){
				queue.clear();
				queue.add(aux);
				break;
			}
		}	
	}

}
