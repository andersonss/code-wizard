package br.ufal.ic.compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.token.Token;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> path = new ArrayList<String>();
		path.add("rsc/_aloMundo.alyn");
		path.add("rsc/_fibonacci.alyn");
		path.add("rsc/_shellsort.alyn");
		path.add("rsc/test.txt");

		
		//Rodar apenas a analise lexica ou a analise sintatica!
		//rodarAnaliseLexica(path);
		rodarAnaliseSintatica(path);


	}

	private static void rodarAnaliseSintatica(List<String> path) {
		try {
			FileReader reader = new FileReader(new File(path.get(3)));
			BufferedReader buffer = new BufferedReader(reader);
			AnalisadorLexico.setBuffer(buffer);
			AnalisadorSintatico analisador = new AnalisadorSintatico();
			analisador.runAnaliseSintatica();
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo Nao Encontrado!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erro na leitura do arquivo!");
		} catch (UndefinedSintaxeException e){
			e.printStackTrace();
			System.err.println("\n** Falha na analise sintatica! **\n->Programa nao reconhecido!\nAlguma outra coisa esperada no lugar de " + e.getMessage());
		}
	}

	private static void rodarAnaliseLexica(List<String> path) {
		try {
			FileReader reader = new FileReader(new File(path.get(2)));
			BufferedReader buffer = new BufferedReader(reader);
			AnalisadorLexico.setBuffer(buffer);
			Token token = AnalisadorLexico.nextToken();
			while (token != null) {
				System.out.println(token);
				token = AnalisadorLexico.nextToken();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo Nao Encontrador!");
		} catch (IOException e) {
			System.err.println("Erro na leitura do arquivo!");
		} catch (UndefinedTokenException e) {
			System.err.println("Token Invalido! " + e.getMessage());
		}
		
	}
}
