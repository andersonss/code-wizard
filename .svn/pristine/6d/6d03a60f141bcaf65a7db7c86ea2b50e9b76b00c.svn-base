package br.ufal.ic.compiladores;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;

public class AnalisadorSintatico {
	private Token token;

	private void lerProximoToken() {
		try {
			token = AnalisadorLexico.nextToken();
			if (token != null) {
				System.out.println(token.getClasseToken());
			}
		} catch (UndefinedTokenException e) {
			System.err
					.println("\n** Falha na analise Lexica! **\n->Proximo Token Invalido! "
							+ e.getMessage());
		}
	}

	/**
	 * lê as declaracoes de cada bloco, a funcao begin e as demais funcoes...
	 * 
	 * @throws UndefinedSintaxeException
	 */
	public void runAnaliseSintatica() throws UndefinedSintaxeException {
		lerProximoToken();
		while (token != null) {
			if (token.getClasseToken() == ClasseToken.FUNC_BEGIN) {
				lerProximoToken();
				analisarBloco();
			} else if (token.getClasseToken() == ClasseToken.VOID) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ID) {
					lerProximoToken();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
				analisarBloco();
			} else if (token.getClasseToken() == ClasseToken.TIPO) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ID) {
					lerProximoToken();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
				analisarBloco();
				if (token.getClasseToken() == ClasseToken.RETURN) {
					lerProximoToken();
					exprLogica();
					if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
						lerProximoToken();
					} else {
						throw new UndefinedSintaxeException(token.toString());
					}
				}
			}
			if (token.getClasseToken() == ClasseToken.END) {
				lerProximoToken();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		}
		System.out
				.println("\n** Analise Sintatica concluida com sucesso! **\n-> Programa reconhecido!");

	}

	/**
	 * Captura a abertura e fechamento de parenteses na declaracao dos
	 * parametros de cada funcao
	 */
	private void analisarBloco() {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			lerProximoToken();
			dclParamList();
			if (token.getClasseToken() == ClasseToken.FPARENTESE) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.DO) {
					lerProximoToken();
					instucaoList();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhecimento das intrucoes da linguagen
	 * 
	 */
	private void instucaoList() {
		switch (token.getClasseToken()) {
		case TIPO:
			lerProximoToken();
			dclVariavel();
			instucaoList();
			break;
		case ID:
			// Aqui foi fatorado a esquerda para identificar adequadamente qual
			// producao usar..
			lerProximoToken();
			instrucaoFatoradaId();
			instucaoList();
			break;
		case IF:
			lerProximoToken();
			instrucaoIf();
			instucaoList();
			break;
		case WHILE:
			lerProximoToken();
			instrucaoWhile();
			instucaoList();
			break;
		case FOR:
			lerProximoToken();
			instrucaoFor();
			instucaoList();
			break;
		case FUNC_PRINT:
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.APARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
			funcaoImprimir();
			instucaoList();
			break;
		case FUNC_READ:
			lerProximoToken();
			funcaoLer();
			instucaoList();
			break;
		default:
			break;
		}
	}

	/**
	 * Reconhece a funcao ler declarada na linguagem
	 */
	private void funcaoLer() {
		lerProximoToken();
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ID) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.FPARENTESE) {
					lerProximoToken();
					if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
						throw new UndefinedSintaxeException(token.toString());
					}
				}
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece a funcao imprimir
	 */
	private void funcaoImprimir() {
		switch (token.getClasseToken()) {
		case CADEIA_CARACTER:
		case CARACTER:
		case CONST_NUM:
		case ID:
			lerProximoToken();
			break;
		default:
			throw new UndefinedSintaxeException(token.toString());
		}
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}

	}

	/**
	 * Reconhece a instrucao FOR
	 */
	private void instrucaoFor() {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			lerProximoToken();
			instrucaoAtribuicaoInstrucaoFor();
			if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
				lerProximoToken();
				exprLogica();
				if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
					lerProximoToken();
					instrucaoAtribuicaoInstrucaoFor();
					if (token.getClasseToken() == ClasseToken.FPARENTESE) {
						lerProximoToken();
						if (token.getClasseToken() == ClasseToken.DO) {
							lerProximoToken();
							instucaoList();
							if (token.getClasseToken() != ClasseToken.END) {
								throw new UndefinedSintaxeException(
										token.toString());
							}
							lerProximoToken();
						} else {
							throw new UndefinedSintaxeException(
									token.toString());
						}
					} else {
						throw new UndefinedSintaxeException(token.toString());
					}
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}

	}

	/**
	 * Reconhece atribuicoes especificamente para isntrucoes FOR onde nao eh
	 * permitido atribuicoes que nao sejam aritmeticas
	 */
	private void instrucaoAtribuicaoInstrucaoFor() {
		if (token.getClasseToken() == ClasseToken.ID) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
				lerProximoToken();
				exprAritmetica();
			} else if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
				lerProximoToken();
				arrayTerminalExpressoes();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece a instrucao While
	 */
	private void instrucaoWhile() {
		exprLogica();
		if (token.getClasseToken() == ClasseToken.DO) {
			lerProximoToken();
			instucaoList();
			if (token.getClasseToken() != ClasseToken.END) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece instrucao If-Else
	 */
	private void instrucaoIf() {
		exprLogica();
		if (token.getClasseToken() == ClasseToken.DO) {
			lerProximoToken();
			instucaoList();
			if (token.getClasseToken() == ClasseToken.ELSE) {
				lerProximoToken();
				instucaoList();
				if (token.getClasseToken() != ClasseToken.END) {
					throw new UndefinedSintaxeException(token.toString());
				}
				lerProximoToken();
			} else if (token.getClasseToken() != ClasseToken.END) {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Existem algumas instrucoes que comecam com ID; Houve o fatoramento a
	 * esquerda para a correta identificacao da instrucao
	 */
	private void instrucaoFatoradaId() {
		intrucaoFatoradaIdAux();
		if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Distingue entre uma Funcao, Array e uma atribuicao
	 */
	private void intrucaoFatoradaIdAux() {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			lerProximoToken();
			chamadaFuncao();
		} else if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
			lerProximoToken();
			arrayTerminalExpressoes();
		} else if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
			lerProximoToken();
			exprLogica();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}

	}

	/**
	 * Fator aritmetico
	 */
	private void fatorAritmetico() {
		switch (token.getClasseToken()) {
		case CADEIA_CARACTER:
		case CARACTER:
			lerProximoToken();
			// reconhece concatenacao de caracteres/strings/ids
			exprCadeiaCaracter();
			break;
		case OP_NEGACAO:
		case OP_SUBTRACAO:
		case APARENTESE:
			lerProximoToken();
			exprLogica();
			if (token.getClasseToken() != ClasseToken.FPARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
			break;
		case CONST_NUM:
			lerProximoToken();
			break;
		case ID:
			lerProximoToken();
			idTerminalExpressoes();
			// reconhece concatenacao de caracteres/strings/ids
			exprCadeiaCaracter();
			break;
		default:
			throw new UndefinedSintaxeException(token.toString());

		}
	}
	/**
	 * Reconhece terminais que comecam com ID, sem possibilitar atribuicoes e sem precisar de ; no final
	 */
	private void idTerminalExpressoes() {
		switch (token.getClasseToken()) {
		case APARENTESE:
			lerProximoToken();
			chamadaFuncao();
			break;
		case ACOLCHETE:
			lerProximoToken();
			arrayTerminalExpressoes();
		default:
			break;
		}
	}

	/**
	 * Reconhece atribucoes com arrays ou acesso ao array
	 */
	private void arrayTerminalExpressoes() {
		exprAritmetica();
		if (token.getClasseToken() == ClasseToken.FCOLCHETE) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
				lerProximoToken();
				exprLogica();
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece chamada de funcoes
	 */
	private void chamadaFuncao() {
		funcParamList();
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece concatenacoes de ids,numeros,retorno de funcoes
	 * Concatenacao esta aceitando mais tipos do que definida na EBNF, ja que uma funcao mais completa eh melhor...
	 */
	private void exprCadeiaCaracter() {
		if (token.getClasseToken() == ClasseToken.OP_CONCATENACAO) {
			lerProximoToken();
			fatorAritmetico();
			exprCadeiaCaracter();
		}
	}


	/**
	 * Reconhecimento dos parametros de uma funcao.
	 * Aceita funcoes sem parametros
	 */
	private void funcParamList() {
		if(token.getClasseToken() == ClasseToken.FPARENTESE){
			return;
		}
		exprLogica();
		if (token.getClasseToken() == ClasseToken.VIRGULA) {
			lerProximoToken();
			exprLogica();
			funcParamList();

		}

	}

	/**
	 * Reconhece declaracao de variaveis
	 */
	private void dclVariavel() {
		dclList();
		if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
			throw new UndefinedSintaxeException(token.toString());
		}
		lerProximoToken();

	}
	/**
	 * Reconhece lista de variaveis
	 */
	private void dclList() {
		if (token.getClasseToken() == ClasseToken.ID) {
			lerProximoToken();
			dclT();
			dclL();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}
	/**
	 * Declaracao da variavel, podendo ser a[] ou a = expr_logica
	 */
	private void dclT() {
		if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
			lerProximoToken();
			exprLogica();
			if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
		} else if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
			lerProximoToken();
			exprLogica();
		}
	}
	
	/**
	 * Mais de uma variavel declarada
	 */
	private void dclL() {
		if (token.getClasseToken() == ClasseToken.VIRGULA) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ID) {
				lerProximoToken();
				dclT();
				dclL();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		}
	}

	private void exprLogica() {
		if(token.getClasseToken() == ClasseToken.FPARENTESE){
			return;
		}
		termoLogico();
		exprLogicaAux();
	}

	private void termoLogico() {
		exprRelacional();
		termoLogicoAux();
	}

	private void exprRelacional() {
		termoRelacional();
		exprRelacionalAux();

	}

	private void termoRelacional() {
		fatorRelacional();
	}

	private void fatorRelacional() {
		switch (token.getClasseToken()) {
		case OP_NEGACAO:
			lerProximoToken();
			fatorRelacional();
			break;
		case BOOLEAN:
			lerProximoToken();
			break;
		default:
			exprAritmetica();
			termoRelacionalAux();
		}
	}

	private void termoRelacionalAux() {
		switch (token.getClasseToken()) {
		case OP_MAIOR:
		case OP_MENOR:
		case OP_MAIOR_IGUAL:
		case OP_MENOR_IGUAL:
			lerProximoToken();
			exprAritmetica();
			break;
		default:
			break;
		}
	}

	private void exprRelacionalAux() {
		if (token.getClasseToken() == ClasseToken.OP_IGUAL_IGUAL
				|| token.getClasseToken() == ClasseToken.OP_DIFERENTE) {
			lerProximoToken();
			termoRelacional();
		}
	}

	private void termoLogicoAux() {
		if (token.getClasseToken() == ClasseToken.OP_E) {
			lerProximoToken();
			exprRelacional();
			termoLogicoAux();
		}
	}

	private void exprLogicaAux() {
		if (token.getClasseToken() == ClasseToken.OP_OU) {
			lerProximoToken();
			termoLogico();
			exprLogicaAux();
		}
	}

	private void exprAritmetica() {
		termoAritmetico();
		exprAritmeticaAux();
	}

	private void exprAritmeticaAux() {
		switch (token.getClasseToken()) {
		case OP_ADICAO:
		case OP_SUBTRACAO:
			lerProximoToken();
			termoAritmetico();
			exprAritmeticaAux();
		default:
			break;
		}
	}

	private void termoAritmetico() {
		termoUnarioAritmetico();
		termoArtimeticoAux();
	}

	private void termoUnarioAritmetico() {
		if (token.getClasseToken() == ClasseToken.OP_SUBTRACAO) {
			lerProximoToken();
		}
		fatorAritmetico();
	}

	private void termoArtimeticoAux() {
		switch (token.getClasseToken()) {
		case OP_MULTIPLICACAO:
		case OP_RESTO:
		case OP_DIVISAO:
			lerProximoToken();
			termoUnarioAritmetico();
			termoArtimeticoAux();
		default:
			break;
		}
	}
	/**
	 * Declaracao dos parametros em uma declaracao de funcao
	 */
	private void dclParamList() {
		if (token.getClasseToken() == ClasseToken.TIPO) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ID) {
				lerProximoToken();
				dclParam();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}

		}

	}
	/**
	 * Declaracao do parametro
	 */
	private void dclParam() {
		if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.FCOLCHETE) {
				lerProximoToken();
				dclParam();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else if (token.getClasseToken() == ClasseToken.VIRGULA) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.TIPO) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ID) {
					lerProximoToken();
					dclParam();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}

		}
	}

}
