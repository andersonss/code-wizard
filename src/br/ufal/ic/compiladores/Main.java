package br.ufal.ic.compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.iterators.CTreeIterator;
import br.ufal.ic.ctree.nosconcretos.RootNode;

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

		rodarAnalise(path);

	}

	private static void rodarAnalise(List<String> path) {
		try {
			FileReader reader = new FileReader(new File(path.get(4)));
			BufferedReader buffer = new BufferedReader(reader);
			AnalisadorLexico.setBuffer(buffer);
			AnalisadorSintatico analisador = new AnalisadorSintatico();
			CTree tree = new RootNode(null);
			analisador.runAnaliseSintatica(tree);
			tree.print();


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
