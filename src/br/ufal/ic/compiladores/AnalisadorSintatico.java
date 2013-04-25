package br.ufal.ic.compiladores;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CNode;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.NodeType;

public class AnalisadorSintatico {
	private Token token;

	private void lerProximoToken() {
		try {
			token = AnalisadorLexico.nextToken();
			if (token != null) {
				//System.out.println(token.getClasseToken());
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
	public void runAnaliseSintatica(CTree rootNode)
			throws UndefinedSintaxeException {
		CTree nodeDclFunc = null;
		lerProximoToken();
		while (token != null) {
			if (token.getClasseToken() == ClasseToken.TIPO
					|| token.getClasseToken() == ClasseToken.FUNC_MAIN
					|| token.getClasseToken() == ClasseToken.VOID) {

				nodeDclFunc = new CTree(rootNode,NodeType.DCL_FUNC);
				rootNode.addNode(nodeDclFunc);
				nodeDclFunc.addNode(new CNode(nodeDclFunc, token));

				lerProximoToken();

				if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
					lerProximoToken();
				}
				if (token.getClasseToken() == ClasseToken.ID_FUNCTION
						|| token.getClasseToken() == ClasseToken.FUNC_MAIN) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
					lerProximoToken();
				}
				analisarBloco(nodeDclFunc);
				if (token.getClasseToken() == ClasseToken.RETURN) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
					lerProximoToken();
					CTree nodeExpr = new CTree(nodeDclFunc, NodeType.EXPR);
					nodeDclFunc.addNode(nodeExpr);
					exprLogica(nodeExpr);
					if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
						nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
						lerProximoToken();
					} else {
						throw new UndefinedSintaxeException(token.toString());
					}
				}
			} else if (token.getClasseToken() == ClasseToken.INCLUDE) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.OP_MENOR
						|| token.getClasseToken() == ClasseToken.ASPA_DUPLA) {
					lerProximoToken();
					if (token.getClasseToken() == ClasseToken.ID) {
						lerProximoToken();
						if (token.getClasseToken() == ClasseToken.PONTO) {
							lerProximoToken();
							if (token.getClasseToken() == ClasseToken.ID) {
								lerProximoToken();
								if (token.getClasseToken() == ClasseToken.OP_MAIOR
										|| token.getClasseToken() == ClasseToken.ASPA_DUPLA) {
									lerProximoToken();
									continue;
								} else {
									throw new UndefinedSintaxeException(
											token.toString());
								}
							} else {
								throw new UndefinedSintaxeException(
										token.toString());
							}
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
			}
			if (token.getClasseToken() == ClasseToken.FCHAVE) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
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
	 * 
	 * @param nodeDclFunc
	 */
	private void analisarBloco(CTree nodeDclFunc) {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
			lerProximoToken();
			dclParamList(nodeDclFunc);
			if (token.getClasseToken() == ClasseToken.FPARENTESE) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc, token));
					lerProximoToken();
					CTree instrucaoList = new CTree(nodeDclFunc, NodeType.INTRUCOES);
					nodeDclFunc.addNode(instrucaoList);
					instucaoList(instrucaoList);
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
	 * @param nodeInstrucaoList 
	 * 
	 */
	private void instucaoList(CTree nodeInstrucaoList) {
		switch (token.getClasseToken()) {
		case TIPO:
			CTree nodeDclVariavel = new CTree(nodeInstrucaoList, NodeType.DCL_VAR);
			nodeInstrucaoList.addNode(nodeDclVariavel);
			nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
			lerProximoToken();
			dclVariavel(nodeDclVariavel);
			instucaoList(nodeInstrucaoList);
			break;
		case ID_FUNCTION:
			CTree nodeChamadaFunc = new CTree(nodeInstrucaoList, NodeType.CHAMADA_FUNC);
			nodeChamadaFunc.addNode(new CNode(nodeChamadaFunc, token));
			nodeInstrucaoList.addNode(nodeChamadaFunc);
			lerProximoToken();
			chamadaFuncao(nodeChamadaFunc);
			if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeChamadaFunc.addNode(new CNode(nodeChamadaFunc, token));
			lerProximoToken();
			instucaoList(nodeInstrucaoList);
			break;
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_ADICAO_ADICAO:
		case OP_SUBTRACAO_SUBTRACAO:
			nodeInstrucaoList.addNode(new CNode(nodeInstrucaoList,token));
			lerProximoToken();
		case ID:
			// Aqui foi fatorado a esquerda para identificar adequadamente qual
			// producao usar..
			nodeInstrucaoList.addNode(new CNode(nodeInstrucaoList, token));
			instrucaoFatoradaId(nodeInstrucaoList);
			instucaoList(nodeInstrucaoList);
			break;
		case IF:
			CTree nodeIf = new CTree(nodeInstrucaoList,NodeType.IF);
			nodeIf.addNode(new CNode(nodeIf,token));
			nodeInstrucaoList.addNode(nodeIf);
			lerProximoToken();
			instrucaoIf(nodeIf);
			instucaoList(nodeInstrucaoList);
			break;
		case WHILE:
			CTree nodeWhile = new CTree(nodeInstrucaoList,NodeType.WHILE);
			nodeWhile.addNode(new CNode(nodeWhile,token));
			nodeInstrucaoList.addNode(nodeWhile);
			lerProximoToken();
			instrucaoWhile(nodeWhile);
			instucaoList(nodeInstrucaoList);
			break;
		case FOR:
			CTree nodeFor = new CTree(nodeInstrucaoList,NodeType.FOR);
			nodeFor.addNode(new CNode(nodeFor,token));
			nodeInstrucaoList.addNode(nodeFor);
			lerProximoToken();
			instrucaoFor(nodeFor);
			instucaoList(nodeInstrucaoList);
			break;
		case FUNC_PRINTF:
			CTree nodePrintf = new CTree(nodeInstrucaoList, NodeType.PRINTF);
			nodePrintf.addNode(new CNode (nodePrintf, token));
			nodeInstrucaoList.addNode(nodePrintf);
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.APARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			lerProximoToken();
			funcaoImprimir();
			instucaoList(nodeInstrucaoList);
			break;
		case FUNC_SCANF:
			CTree nodeScanf = new CTree(nodeInstrucaoList, NodeType.SCANF);
			nodeScanf.addNode(new CNode (nodeScanf, token));
			nodeInstrucaoList.addNode(nodeScanf);
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.APARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			funcaoLer();
			instucaoList(nodeInstrucaoList);
			break;
		default:
			break;
		}
	}

	/**
	 * Reconhece a funcao ler declarada na linguagem
	 */
	private void funcaoLer() {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.CADEIA_CARACTER) {
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.VIRGULA) {
					lerProximoToken();
					while (token.getClasseToken() == ClasseToken.OP_REFERENCIA) {
						lerProximoToken();
						if (token.getClasseToken() == ClasseToken.ID) {
							lerProximoToken();
							if (token.getClasseToken() == ClasseToken.VIRGULA) {
								lerProximoToken();
							}
						} else {
							throw new UndefinedSintaxeException(
									token.toString());
						}
					}
					if (token.getClasseToken() == ClasseToken.FPARENTESE) {
						lerProximoToken();
						if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
							throw new UndefinedSintaxeException(
									token.toString());
						}
						lerProximoToken();
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
		while (token.getClasseToken() == ClasseToken.VIRGULA) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ID) {
				lerProximoToken();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
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
	 * @param nodeFor 
	 */
	private void instrucaoFor(CTree nodeFor) {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			nodeFor.addNode(new CNode(nodeFor, token));
			lerProximoToken();
			atribuicao(nodeFor);
			if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
				nodeFor.addNode(new CNode(nodeFor,token));
				lerProximoToken();
				CTree exprL = new CTree(nodeFor,NodeType.EXPR);
				nodeFor.addNode(exprL);
				exprLogica(exprL);
				if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
					nodeFor.addNode(new CNode(nodeFor,token));
					lerProximoToken();
					CTree expr = new CTree(nodeFor,NodeType.EXPR);
					nodeFor.addNode(expr);
					exprAritmetica(expr);
					if (token.getClasseToken() == ClasseToken.FPARENTESE) {
						nodeFor.addNode(new CNode(nodeFor,token));
						lerProximoToken();
						if (token.getClasseToken() == ClasseToken.ACHAVE) {
							nodeFor.addNode(new CNode(nodeFor,token));
							lerProximoToken();
							CTree instrucao = new CTree(nodeFor, NodeType.INTRUCOES);
							nodeFor.addNode(instrucao);
							instucaoList(instrucao);
							if (token.getClasseToken() != ClasseToken.FCHAVE) {
								throw new UndefinedSintaxeException(
										token.toString());
							}
							nodeFor.addNode(new CNode(nodeFor,token));
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
	 * Reconhece a instrucao While
	 * @param nodeWhile 
	 */
	private void instrucaoWhile(CTree nodeWhile) {
		CTree exprL = new CTree(nodeWhile, NodeType.EXPR);
		nodeWhile.addNode(exprL);
		exprLogica(exprL);
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
			nodeWhile.addNode(new CNode(nodeWhile,token));
			lerProximoToken();
			CTree instrucao = new CTree(nodeWhile, NodeType.INTRUCOES);
			nodeWhile.addNode(instrucao);
			instucaoList(instrucao);
			if (token.getClasseToken() != ClasseToken.FCHAVE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeWhile.addNode(new CNode(nodeWhile,token));
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhece instrucao If-ElseIf-Else
	 * @param nodeIf 
	 */
	private void instrucaoIf(CTree nodeIf) {
		CTree nodeExpr = new CTree(nodeIf,NodeType.EXPR);
		nodeIf.addNode(nodeExpr);
		exprLogica(nodeExpr);
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
			nodeIf.addNode(new CNode(nodeIf,token));
			lerProximoToken();
			CTree nodeInstrucao = new CTree(nodeIf,NodeType.INTRUCOES);
			nodeIf.addNode(nodeInstrucao);
			instucaoList(nodeInstrucao);
			if (token.getClasseToken() != ClasseToken.FCHAVE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeIf.addNode(new CNode(nodeIf,token));
			lerProximoToken();
			while (token.getClasseToken() == ClasseToken.ELSE_IF) {
				CTree nodeElseIf = new CTree(nodeIf,NodeType.ELSE_IF);
				nodeIf.addNode(nodeElseIf);
				nodeElseIf.addNode(new CNode(nodeElseIf,token));
				lerProximoToken();
				CTree nodeExprL = new CTree(nodeElseIf,NodeType.EXPR);
				nodeElseIf.addNode(nodeExprL);
				exprLogica(nodeExprL);
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeElseIf.addNode(new CNode(nodeElseIf,token));
					lerProximoToken();
					CTree nodeInstrucao1 = new CTree(nodeElseIf,NodeType.INTRUCOES);
					nodeElseIf.addNode(nodeInstrucao1);
					instucaoList(nodeInstrucao1);
					if (token.getClasseToken() != ClasseToken.FCHAVE) {
						throw new UndefinedSintaxeException(token.toString());
					}
					nodeElseIf.addNode(new CNode(nodeElseIf,token));
					lerProximoToken();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			}

			if (token.getClasseToken() == ClasseToken.ELSE) {
				CTree nodeElse = new CTree(nodeIf,NodeType.ELSE);
				nodeIf.addNode(nodeElse);
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeElse.addNode(new CNode(nodeElse,token));
					lerProximoToken();
					CTree nodeInstrucao2 = new CTree(nodeElse,NodeType.INTRUCOES);
					nodeElse.addNode(nodeInstrucao2);
					instucaoList(nodeInstrucao2);
					if (token.getClasseToken() != ClasseToken.FCHAVE) {
						throw new UndefinedSintaxeException(token.toString());
					}
					nodeElse.addNode(new CNode(nodeElse,token));
					lerProximoToken();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Existem algumas instrucoes que comecam com ID; Houve o fatoramento a
	 * esquerda para a correta identificacao da instrucao
	 * @param instrucaoList 
	 */
	private void instrucaoFatoradaId(CTree instrucaoList) {
		atribuicao(instrucaoList);
		if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
			instrucaoList.addNode(new CNode(instrucaoList,token));
			lerProximoToken();
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/*
	 * Distingue entre uma Funcao, Array e uma atribuicao
	 * 
	 * private void intrucaoFatoradaIdAux() { if (token.getClasseToken() ==
	 * ClasseToken.APARENTESE) { lerProximoToken(); chamadaFuncao(); } else if
	 * (token.getClasseToken() == ClasseToken.ACOLCHETE) { lerProximoToken();
	 * arrayTerminalExpressoes(); } else if (token.getClasseToken() ==
	 * ClasseToken.ATRIBUICAO) { lerProximoToken(); exprLogica(); } else { throw
	 * new UndefinedSintaxeException(token.toString()); }
	 * 
	 * }
	 */

	/**
	 * Fator aritmetico
	 * @param nodeExpr 
	 */
	private void fatorAritmetico(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case CADEIA_CARACTER:
		case CARACTER:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			break;
		case OP_NEGACAO:
		case OP_SUBTRACAO:
		case APARENTESE:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			exprLogica(nodeExpr);
			if (token.getClasseToken() != ClasseToken.FPARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			break;
		case CONST_NUM:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			break;
		case ID_FUNCTION:
			CTree nodeChamadaFuncao = new CTree(nodeExpr,NodeType.CHAMADA_FUNC);
			CNode node = new CNode(nodeChamadaFuncao,token);
			lerProximoToken();
			nodeChamadaFuncao.addNode(node);
			nodeExpr.addNode(nodeChamadaFuncao);
			chamadaFuncao(nodeChamadaFuncao);
			break;
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_REFERENCIA:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.ID) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			break;
		case ID:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
				nodeExpr.addNode(new CNode(nodeExpr,token));
				lerProximoToken();				
				exprAritmetica(nodeExpr);
				if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
					throw new UndefinedSintaxeException(token.toString());
				}
				nodeExpr.addNode(new CNode(nodeExpr,token));
				lerProximoToken();
			}
			break;
		default:
			throw new UndefinedSintaxeException(token.toString());

		}
	}

	/**
	 * Reconhece chamada de funcoes
	 * @param nodeChamadaFuncao 
	 */
	private void chamadaFuncao(CTree nodeChamadaFuncao) {
		if (token.getClasseToken() == ClasseToken.APARENTESE) {
			nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
			lerProximoToken();
			
			funcParamList(nodeChamadaFuncao);
			if (token.getClasseToken() == ClasseToken.FPARENTESE) {
				nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
				lerProximoToken();
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else {
			throw new UndefinedSintaxeException(token.toString());
		}
	}

	/**
	 * Reconhecimento dos parametros de uma funcao. Aceita funcoes sem
	 * parametros
	 * @param nodeChamadaFuncao 
	 */
	private void funcParamList(CTree nodeChamadaFuncao) {
		CTree nodeExpr = new CTree(nodeChamadaFuncao,NodeType.EXPR);
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			//AKI TAMBÉM NÃO TINHA O LER TOKEN. ADICIONEI!
			nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
			lerProximoToken();
			return;
		}
		if (token.getClasseToken() == ClasseToken.OP_REFERENCIA
				|| token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
			nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.ID) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
			lerProximoToken();
		} else {
			nodeChamadaFuncao.addNode(nodeExpr);
			exprLogica(nodeExpr);
		}
		if (token.getClasseToken() == ClasseToken.VIRGULA) {
			nodeChamadaFuncao.addNode(new CNode(nodeChamadaFuncao,token));
			lerProximoToken();
			nodeChamadaFuncao.addNode(nodeExpr);
			exprLogica(nodeExpr);
			funcParamList(nodeChamadaFuncao);
		}
	}

	/**
	 * Reconhece declaracao de variaveis
	 * @param nodeDclVariavel 
	 */
	private void dclVariavel(CTree nodeDclVariavel) {
		dclList(nodeDclVariavel);
		if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
			throw new UndefinedSintaxeException(token.toString());
		}
		nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
		lerProximoToken();

	}

	/**
	 * Reconhece lista de variaveis
	 * @param nodeDclVariavel 
	 */
	private void dclList(CTree nodeDclVariavel) {
		atribuicao(nodeDclVariavel);
		dclL(nodeDclVariavel);
	}

	/**
	 * Mais de uma variavel declarada
	 * @param nodeDclVariavel 
	 */
	private void dclL(CTree nodeDclVariavel) {
		if (token.getClasseToken() == ClasseToken.VIRGULA) {
			nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
			lerProximoToken();
			dclList(nodeDclVariavel);
		}
	}

	private void atribuicao(CTree nodeDclVariavel) {
		atribuicaoAux(nodeDclVariavel);
		if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
			nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
			lerProximoToken();
			CTree nodeExpr = new CTree(nodeDclVariavel,NodeType.EXPR);
			nodeDclVariavel.addNode(nodeExpr);
			exprLogica(nodeExpr);
		} else {
			termoUnarioAritmeticoPos(nodeDclVariavel);
		}

	}

	private void atribuicaoAux(CTree nodeDclVariavel) {
		if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
			nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
			lerProximoToken();
		}
		if (token.getClasseToken() == ClasseToken.ID) {
			nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
				nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
				lerProximoToken();
				CTree nodeExpr = new CTree(nodeDclVariavel,NodeType.EXPR);
				nodeDclVariavel.addNode(nodeExpr);
				exprAritmetica(nodeExpr);
				if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
					throw new UndefinedSintaxeException(token.toString());
				}
				nodeDclVariavel.addNode(new CNode(nodeDclVariavel,token));
				lerProximoToken();
			}
		}

	}

	private void exprLogica(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			/**
			 * MODIFIQUEI AKI, NAO TINHA ESSE LER PROXIMO TOKEN
			 */
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			return;
		}
		termoLogico(nodeExpr);
		exprLogicaAux(nodeExpr);
	}

	private void termoLogico(CTree nodeExpr) {
		exprRelacional(nodeExpr);
		termoLogicoAux(nodeExpr);
	}

	private void exprRelacional(CTree nodeExpr) {
		termoRelacional(nodeExpr);
		exprRelacionalAux(nodeExpr);

	}

	private void termoRelacional(CTree nodeExpr) {
		fatorRelacional(nodeExpr);
	}

	private void fatorRelacional(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case OP_NEGACAO:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			fatorRelacional(nodeExpr);
			break;
		case BOOLEAN:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			break;
		default:
			exprAritmetica(nodeExpr);
			termoRelacionalAux(nodeExpr);
		}
	}

	private void termoRelacionalAux(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case OP_MAIOR:
		case OP_MENOR:
		case OP_MAIOR_IGUAL:
		case OP_MENOR_IGUAL:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			exprAritmetica(nodeExpr);
			break;
		default:
			break;
		}
	}

	private void exprRelacionalAux(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_IGUAL_IGUAL
				|| token.getClasseToken() == ClasseToken.OP_DIFERENTE) {
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			termoRelacional(nodeExpr);
		}
	}

	private void termoLogicoAux(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_E) {
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			exprRelacional(nodeExpr);
			termoLogicoAux(nodeExpr);
		}
	}

	private void exprLogicaAux(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_OU) {
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			termoLogico(nodeExpr);
			exprLogicaAux(nodeExpr);
		}
	}

	private void exprAritmetica(CTree nodeExpr) {
		termoAritmetico(nodeExpr);
		exprAritmeticaAux(nodeExpr);
	}

	private void exprAritmeticaAux(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case OP_ADICAO:
		case OP_SUBTRACAO:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			termoAritmetico(nodeExpr);
			exprAritmeticaAux(nodeExpr);
		default:
			termoUnarioAritmeticoPos(nodeExpr);
		}
	}

	private void termoAritmetico(CTree nodeExpr) {
		termoUnarioAritmeticoPre(nodeExpr);
		termoArtimeticoAux(nodeExpr);
	}

	private void termoUnarioAritmeticoPos(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_ADICAO_ADICAO
				|| token.getClasseToken() == ClasseToken.OP_SUBTRACAO_SUBTRACAO) {
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
		}
	}

	private void termoUnarioAritmeticoPre(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_SUBTRACAO
				|| token.getClasseToken() == ClasseToken.OP_ADICAO
				|| token.getClasseToken() == ClasseToken.OP_ADICAO_ADICAO
				|| token.getClasseToken() == ClasseToken.OP_SUBTRACAO_SUBTRACAO) {
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
		}
		fatorAritmetico(nodeExpr);
	}

	private void termoArtimeticoAux(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_RESTO:
		case OP_DIVISAO:
			nodeExpr.addNode(new CNode(nodeExpr,token));
			lerProximoToken();
			termoUnarioAritmeticoPre(nodeExpr);
			termoArtimeticoAux(nodeExpr);
		default:
			break;
		}
	}

	/**
	 * Declaracao dos parametros em uma declaracao de funcao
	 * 
	 * @param nodeDclFunc
	 */
	private void dclParamList(CTree nodeDclFunc) {
		if (token.getClasseToken() == ClasseToken.TIPO) {
			nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
				lerProximoToken();
			}
			if (token.getClasseToken() == ClasseToken.ID) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
				lerProximoToken();
				dclParam(nodeDclFunc);
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}

		}

	}

	/**
	 * Declaracao do parametro
	 * 
	 * @param nodeDclFunc
	 */
	private void dclParam(CTree nodeDclFunc) {
		if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
			nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.FCOLCHETE) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
				lerProximoToken();
				dclParam(nodeDclFunc);
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else if (token.getClasseToken() == ClasseToken.VIRGULA) {
			nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.TIPO) {
				nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
					lerProximoToken();
				}
				if (token.getClasseToken() == ClasseToken.ID) {
					nodeDclFunc.addNode(new CNode(nodeDclFunc,token));
					lerProximoToken();
					dclParam(nodeDclFunc);
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}

		}
	}

}
