package br.ufal.ic.ctree.visitors.analyzeobjects;

public class ForTabelaDeAnalise {
	
	private String initialization;
	private String condition;
	private String afterthought;
	public String expression;
	private int profundidade;
	private boolean stateReading = true;
	
	public ForTabelaDeAnalise(int profundidade) {
		this.profundidade = profundidade;
	}
	
	public String getInitialization() {
		return initialization;
	}
	public void setInitialization(String initialization) {
		this.initialization = initialization;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getAfterthought() {
		return afterthought;
	}
	public void setAfterthought(String afterthought) {
		this.afterthought = afterthought;
		stateReading = false;
	}
	
	public boolean getStateReading(){
		return stateReading;
	}
	
	public int getProfundidade(){
		return profundidade;
	}

	public void print() {
		System.out.println(expression + "\n");
		
	}
	

}
