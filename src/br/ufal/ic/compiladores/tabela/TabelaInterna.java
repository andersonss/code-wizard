package br.ufal.ic.compiladores.tabela;

import java.util.HashMap;

import br.ufal.ic.compiladores.token.ClasseToken;

public class TabelaInterna {

	private final static HashMap<String, ClasseToken> tabelaInterna = new HashMap<String, ClasseToken>();

	static {

		// tipos
		tabelaInterna.put("int", ClasseToken.TIPO);
		tabelaInterna.put("float", ClasseToken.TIPO);
		tabelaInterna.put("double", ClasseToken.TIPO);
		tabelaInterna.put("char", ClasseToken.TIPO);

		// bool literais
		tabelaInterna.put("false", ClasseToken.BOOLEAN);
		tabelaInterna.put("true", ClasseToken.BOOLEAN);

		// metodos proprios
		tabelaInterna.put("main", ClasseToken.FUNC_MAIN);
		tabelaInterna.put("#include", ClasseToken.INCLUDE);
		tabelaInterna.put("printf", ClasseToken.FUNC_PRINTF);
		tabelaInterna.put("scanf", ClasseToken.FUNC_SCANF);

		// palavras chave
		tabelaInterna.put("void", ClasseToken.VOID);
		tabelaInterna.put("if", ClasseToken.IF);
		tabelaInterna.put("else", ClasseToken.ELSE);
		tabelaInterna.put("else if", ClasseToken.ELSE_IF);
		tabelaInterna.put("for", ClasseToken.FOR);
		tabelaInterna.put("return", ClasseToken.RETURN);
		tabelaInterna.put("while", ClasseToken.WHILE);

		// separadores
		tabelaInterna.put("{", ClasseToken.ACHAVE);
		tabelaInterna.put("}", ClasseToken.FCHAVE);
		tabelaInterna.put("[", ClasseToken.ACOLCHETE);
		tabelaInterna.put("]", ClasseToken.FCOLCHETE);
		tabelaInterna.put("(", ClasseToken.APARENTESE);
		tabelaInterna.put(")", ClasseToken.FPARENTESE);
		tabelaInterna.put(",", ClasseToken.VIRGULA);
		tabelaInterna.put(".", ClasseToken.PONTO);
		tabelaInterna.put(";", ClasseToken.PONTO_VIRGULA);

		// atribuicao
		tabelaInterna.put("=", ClasseToken.ATRIBUICAO);

		// negacao
		tabelaInterna.put("!", ClasseToken.OP_NEGACAO);

		// pre-processador
		tabelaInterna.put("#", ClasseToken.PREPROCESSADOR);

		// operadores relacionais
		tabelaInterna.put("==", ClasseToken.OP_IGUAL_IGUAL);
		tabelaInterna.put("!=", ClasseToken.OP_DIFERENTE);
		tabelaInterna.put(">", ClasseToken.OP_MAIOR);
		tabelaInterna.put("<", ClasseToken.OP_MENOR);
		tabelaInterna.put(">=", ClasseToken.OP_MAIOR_IGUAL);
		tabelaInterna.put("<=", ClasseToken.OP_MENOR_IGUAL);

		// operadores logicos
		tabelaInterna.put("||", ClasseToken.OP_OU);
		tabelaInterna.put("&&", ClasseToken.OP_E);

		//operadores epesciais
		tabelaInterna.put("&", ClasseToken.OP_E_COMERCIAL);
		tabelaInterna.put("|", ClasseToken.OP_OU_BINARIO);

		// operadores aritimeticos
		tabelaInterna.put("+", ClasseToken.OP_ADICAO);
		tabelaInterna.put("++", ClasseToken.OP_ADICAO_ADICAO);
		tabelaInterna.put("-", ClasseToken.OP_SUBTRACAO);
		tabelaInterna.put("--", ClasseToken.OP_SUBTRACAO_SUBTRACAO);
		tabelaInterna.put("/", ClasseToken.OP_DIVISAO);
		tabelaInterna.put("*", ClasseToken.OP_MULTIPLICACAO);
		tabelaInterna.put("%", ClasseToken.OP_RESTO);

	}

	public static boolean contains(String lexema) {
		return tabelaInterna.containsKey(String.valueOf(lexema));
	}

	public static ClasseToken getTokenClass(String lexema) {
		ClasseToken classe = tabelaInterna.get(lexema);
		return classe;
	}
}
