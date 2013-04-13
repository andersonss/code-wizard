package br.ufal.ic.compiladores;

import java.io.BufferedReader;
import java.io.IOException;

import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.tabela.TabelaInterna;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;

/**
 * 
 * Retorna o Token contendo a classe, valor e posicao.
 * 
 */
public class AnalisadorLexico {

	private static int posLinha = 1;
	private static int posColuna = 1;
	private static BufferedReader buffer;
	private static char charBuff;
	private static final char EOLR = '\r';
	private static final char EOLN = '\n';
	private static final char EOF = (char) -1;

	/* delimitar os caracteres aceitos pela linguagem */
	private static final int ASCII_INI = 32;
	private static final int ASCII_FIM = 126;

	public static void setBuffer(BufferedReader buffer) throws IOException {
		AnalisadorLexico.buffer = buffer;
		charBuff = (char) buffer.read();
	}

	/**
	 * le o proximo caracter e incrementa o contador de coluna
	 * 
	 * @throws IOException
	 */
	private static void lerBuffer() {
		posColuna++;
		try {
			charBuff = (char) buffer.read();
		} catch (IOException e) {
			System.err.println("Erro na leitura do arquivo!");
			e.printStackTrace();
		}
	}
	/**
	 * remove os espacos em branco, enter e outros.
	 * funciona apenas sobre demanda, nao limpa o arquivo inteiro.
	 */
	public static boolean clearWhiteSpace(){
		while (charBuff == EOLR || charBuff == EOLN
				|| Character.isWhitespace(charBuff) || charBuff == EOF) {
			// Windows = \r\n, mac = \r, unix = \n
			if (charBuff == EOLR) {
				posColuna = 0;
				posLinha++;
				lerBuffer();
				if (charBuff == EOLN) {
					posColuna = 0;
					lerBuffer();
				}
			} else if (charBuff == EOLN) {
				posColuna = 0;
				posLinha++;
				lerBuffer();
			} else if (charBuff == EOF) {
				return false;
			} else {
				lerBuffer();
			}
		}
		return true;
	}

	/**
	 * 
	 * @return Token
	 * @throws UndefinedTokenException
	 */
	public static Token nextToken() throws UndefinedTokenException {

		int posXInit;
		int posYInit;
		ClasseToken classe = null;
		String lexema = "";

		// percorre ate encontrar algo diferente de espaco ou enter ou chegar ao
		// final do arquivo.
		if(!clearWhiteSpace()) return null;


		// posicao do token
		posXInit = posColuna;
		posYInit = posLinha;

		if (Character.isLetter(charBuff) || charBuff == '_') {
			while (Character.isLetterOrDigit(charBuff)) {
				lexema += charBuff;
				lerBuffer();
			}

			if (lexema.equals("else")){
				if(!clearWhiteSpace()) return null;
				if (charBuff == 'i') {
					lexema += ' ';
					while (Character.isLetterOrDigit(charBuff)) {
						lexema += charBuff;
						lerBuffer();
					}
				}
			}

			if (TabelaInterna.contains(lexema)) {
				classe = TabelaInterna.getTokenClass(lexema.toLowerCase());
			} else {
				classe = ClasseToken.ID;
			}
		} else if (Character.isDigit(charBuff)) {
			int quantPonto = 0;
			while (Character.isDigit(charBuff) || charBuff == '.') {
				if (charBuff == '.') {
					if (++quantPonto > 1) {
						throw new UndefinedTokenException(
								String.valueOf(charBuff), posLinha, posColuna);
					}
				}
				lexema += charBuff;
				lerBuffer();
			}
			classe = ClasseToken.CONST_NUM;
		} else if (charBuff == '"') {
			// captura cadeia de caracteres
			lexema += charBuff;
			lerBuffer();
			while (charBuff >= ASCII_INI && charBuff <= ASCII_FIM) {
				if (charBuff == '"') {
					lexema += charBuff;
					classe = ClasseToken.CADEIA_CARACTER;
					lerBuffer();
					break;
				}
				lexema += charBuff;
				lerBuffer();
			}
		} else if (charBuff == '\'') {
			// captura char
			lexema += charBuff;
			lerBuffer();
			if (Character.isLetter(charBuff)) {
				lexema += charBuff;
				lerBuffer();
				if (charBuff == '\'') {
					lexema += charBuff;
					classe = ClasseToken.CARACTER;
					lerBuffer();
				} else {
					throw new UndefinedTokenException(lexema, posLinha,
							posColuna);
				}

			}
		} else {
			// captura operadores e delimitadores e outras coisas
			lexema += charBuff;
			lerBuffer();
			if (TabelaInterna.contains(lexema + charBuff)) {
				lexema += charBuff;
				lerBuffer();
			}
			classe = TabelaInterna.getTokenClass(lexema);
		}

		if (classe == null) {
			// captura coisas q nao foram classificadas
			throw new UndefinedTokenException(lexema, posXInit, posYInit);
		}

		return new Token(classe, lexema, posYInit, posXInit);
	}
}
