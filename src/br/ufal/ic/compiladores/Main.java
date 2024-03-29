package br.ufal.ic.compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.iterators.CTreeIterator;
import br.ufal.ic.ctree.iterators.DepthFirstIterator;
import br.ufal.ic.ctree.nosconcretos.RootNode;
import br.ufal.ic.ctree.visitors.CounterAnalyzeVisitor;
import br.ufal.ic.ctree.visitors.ForAnalyzeVisitor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> path = new ArrayList<String>();
		path.add("rsc/3-numeros-em-ordem-crescente.c");
		path.add("rsc/classificacao-triangulos-1.c");
		path.add("rsc/classificacao-triangulos-2.c");
		path.add("rsc/figurinhas-dos-irmaos.c");
		path.add("rsc/soma-fracoes.c");
		path.add("rsc/test.txt");

		rodarAnalise(path);

	}

	private static void rodarAnalise(List<String> path) {
		try {
			FileReader reader = new FileReader(new File(path.get(3)));
			BufferedReader buffer = new BufferedReader(reader);
			AnalisadorLexico.setBuffer(buffer);
			AnalisadorSintatico analisador = new AnalisadorSintatico();
			CTree tree = new RootNode(null);
			analisador.runAnaliseSintatica(tree);
			//CTreeIterator iterator = new BreadthFirstIterator(tree);
			CTreeIterator iterator = new DepthFirstIterator(tree);
			iterator.setStartNode(1);
			CounterAnalyzeVisitor visitor1 = new CounterAnalyzeVisitor();
			while(iterator.hasNext()){
				CTree aux = iterator.next();
				aux.print();
				aux.accept(visitor1);
			}
			visitor1.getTabelaDeAnalise().printLog();


		} catch (FileNotFoundException e) {
			System.err.println("Arquivo Nao Encontrado!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erro na leitura do arquivo!");
		} catch (UndefinedSintaxeException e) {
			e.printStackTrace();
			System.err
					.println("\n** Falha na analise sintatica! **\n->Programa nao reconhecido!\nAlguma outra coisa esperada no lugar de "
							+ e.getMessage());
		}
	}

}
