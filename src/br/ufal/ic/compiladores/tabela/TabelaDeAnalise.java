package br.ufal.ic.compiladores.tabela;

public class TabelaDeAnalise {
	
	private int quantidadeDeFor = 0;
	private int quantidadeDeWhile = 0;
	private int quantidadeDeIf = 0;
	private int quantidadeDeElseIf = 0;
	private int quantidadeDeElse = 0;
	private int quantidadeDeFuncoes = 0;
	private int quantidadeDeVariaveis = 0;
	private int quantidadeDeNos = 0;
	private int quantidadeDeScanf = 0;
	private int quantidadeDePrintf = 0;
	private int profundidade = 0;
	
	
	public int getQuantidadeDeVariaveis() {
		return quantidadeDeVariaveis;
	}
	public void incrementeQuantidadeDeVariaveis(int quant) {
		this.quantidadeDeVariaveis +=quant;
	}
	
	public int getQuantidadeDeScanf() {
		return quantidadeDeScanf;
	}
	public void incrementeQuantidadeDeScanf() {
		this.quantidadeDeScanf++;
	}
	
	public int getQuantidadeDePrinf() {
		return quantidadeDePrintf;
	}
	public void incrementeQuantidadeDePrinf() {
		this.quantidadeDePrintf++;
	}
	
	public int getQuantidadeDeFor() {
		return quantidadeDeFor;
	}
	public void incrementeQuantidadeDeFor() {
		this.quantidadeDeFor++;
	}
	public int getQuantidadeDeWhiles() {
		return quantidadeDeWhile;
	}
	
	public void incrementeQuantidadeDeWhiles() {
		this.quantidadeDeWhile++;
	}
	public int getQuantidadeDeIf() {
		return quantidadeDeIf;
	}
	public void incrementeQuantidadeDeIf() {
		this.quantidadeDeIf++;
	}
	public int getQuantidadeDeElseIf() {
		return quantidadeDeElseIf;
	}
	public void incrementeQuantidadeDeElseIf() {
		this.quantidadeDeElseIf++;
	}
	public int getQuantidadeDeElse() {
		return quantidadeDeElse;
	}
	public void incrementeQuantidadeDeElse() {
		this.quantidadeDeElse++;
	}
	public int getQuantidadeDeFuncoes() {
		return quantidadeDeFuncoes;
	}
	public void incrementeQuantidadeDeFuncoes() {
		this.quantidadeDeFuncoes++;
	}
	public int getQuantidadeDeNos() {
		return quantidadeDeNos;
	}
	public void incrementeQuantidadeDeNos() {
		this.quantidadeDeNos++;
	}
	public int getProfundidade() {
		return profundidade;
	}
	public void setProfundidade(int profundidade) {
		this.profundidade = this.profundidade < profundidade? profundidade : this.profundidade;
	}
	@Override
	public String toString() {
		return "TabelaDeAnalise [quantidadeDeFor=" + quantidadeDeFor
				+ ", quantidadeDeWhiles=" + quantidadeDeWhile
				+ ", quantidadeDeIf=" + quantidadeDeIf
				+ ", quantidadeDeElseIf=" + quantidadeDeElseIf
				+ ", quantidadeDeElse=" + quantidadeDeElse
				+ ", quantidadeDeFuncoes=" + quantidadeDeFuncoes
				+ ", quantidadeDeVariaveis=" + quantidadeDeVariaveis
				+ ", quantidadeDeNos=" + quantidadeDeNos
				+ ", quantidadeDeScanf=" + quantidadeDeScanf
				+ ", quantidadeDePrintf=" + quantidadeDePrintf
				+ ", profundidade=" + profundidade + "]";
	}

	
	
	
	

}
