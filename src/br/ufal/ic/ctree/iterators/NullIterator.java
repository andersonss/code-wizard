package br.ufal.ic.ctree.iterators;

import java.util.List;

import br.ufal.ic.ctree.CTree;

public class NullIterator extends CTreeIterator {

	public NullIterator(List<CTree> nodeList) {
		super(nodeList);
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

}
