package br.ufal.ic.ctree.iterators;

import java.util.Iterator;
import java.util.List;

import br.ufal.ic.ctree.CTree;

public abstract class CTreeIterator implements Iterator<CTree> {


	protected CTree rootNode;
	
	public CTreeIterator(CTree rootNode) {
		this.rootNode = rootNode;
	}
	
}
