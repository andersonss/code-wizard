package br.ufal.ic.ctree.iterators;

import br.ufal.ic.ctree.CTree;

public class NullIterator extends CTreeIterator {



	public NullIterator(CTree rootNode) {
		super(rootNode);
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public CTree next() {
		return null;
	}

	@Override
	public void remove() {

	}

	@Override
	public void setStartNode(int nodeId) {
		
	}

}
