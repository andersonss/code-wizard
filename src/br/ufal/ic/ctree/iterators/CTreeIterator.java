package br.ufal.ic.ctree.iterators;

import java.util.Iterator;

import br.ufal.ic.ctree.CTree;

public abstract class CTreeIterator implements Iterator<CTree> {


	protected CTree rootNode;
	protected CTree startNode;
	
	public CTreeIterator(CTree rootNode) {
		this.rootNode = rootNode;
	}

	public abstract void setStartNode(int nodeId);
	
}
