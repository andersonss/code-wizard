package br.ufal.ic.ctree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ufal.ic.ctree.visitors.CTreeVisitor;


/**
 * Essa classe implementa os atributos e metodos basicos para serem utilizados na arvore do codigo.
 * A partir dessa classe, sera criado uma arvore usando padrão composite
 * 
 * @author dalton
 *
 */
public abstract class CTree {
	
	protected CTree noPai;
	protected int profundidade = 0;
	protected List<CTree> nodeList;
	public abstract void accept(CTreeVisitor visitor);
	
	public CTree(CTree noPai){
		if(noPai != null){
			this.profundidade = noPai.profundidade++;
		}
		this.noPai = noPai;
		nodeList = new LinkedList<CTree>();
	}
	
	public void print(){
		System.out.println("No = " + this.getClass() + "profundidade = " + profundidade);
		
	}
	
	public int getProfundidade(){
		return this.profundidade;
	}
	
	public void addNode(CTree node){
		this.nodeList.add(node);
	}
	
	public Iterator<CTree> iterator(){
		return nodeList.iterator();
	}

}
