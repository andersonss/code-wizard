package br.ufal.ic.compiladores;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.nosconcretos.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;


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
	 * l� as declaracoes de cada bloco, a funcao begin e as demais funcoes...
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

				nodeDclFunc = new FunctionDeclarationNode(rootNode);
				rootNode.addNode(nodeDclFunc);
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));

				lerProximoToken();

				if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
					lerProximoToken();
				}
				if (token.getClasseToken() == ClasseToken.ID_FUNCTION
						|| token.getClasseToken() == ClasseToken.FUNC_MAIN) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
					lerProximoToken();
				}
				analisarBloco(nodeDclFunc);
				if (token.getClasseToken() == ClasseToken.RETURN) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
					lerProximoToken();
					CTree nodeExpr = new ExpressionNode(nodeDclFunc);
					nodeDclFunc.addNode(nodeExpr);
					exprLogica(nodeExpr);
					if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
						nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
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
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
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
			nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
			lerProximoToken();
			dclParamList(nodeDclFunc);
			if (token.getClasseToken() == ClasseToken.FPARENTESE) {
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc, token));
					lerProximoToken();
					CTree instrucaoList = new StatementsNode(nodeDclFunc);
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
			CTree nodeDclVariavel = new VariableDeclarationNode(nodeInstrucaoList);
			nodeInstrucaoList.addNode(nodeDclVariavel);
			nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
			lerProximoToken();
			dclVariavel(nodeDclVariavel);
			instucaoList(nodeInstrucaoList);
			break;
		case ID_FUNCTION:
			CTree nodeChamadaFunc = new FunctionCallNode(nodeInstrucaoList);
			nodeChamadaFunc.addNode(new TerminalNode(nodeChamadaFunc, token));
			nodeInstrucaoList.addNode(nodeChamadaFunc);
			lerProximoToken();
			chamadaFuncao(nodeChamadaFunc);
			if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeChamadaFunc.addNode(new TerminalNode(nodeChamadaFunc, token));
			lerProximoToken();
			instucaoList(nodeInstrucaoList);
			break;
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_ADICAO_ADICAO:
		case OP_SUBTRACAO_SUBTRACAO:
			nodeInstrucaoList.addNode(new TerminalNode(nodeInstrucaoList,token));
			lerProximoToken();
		case ID:
			// Aqui foi fatorado a esquerda para identificar adequadamente qual
			// producao usar..
			nodeInstrucaoList.addNode(new TerminalNode(nodeInstrucaoList, token));
			instrucaoFatoradaId(nodeInstrucaoList);
			instucaoList(nodeInstrucaoList);
			break;
		case IF:
			CTree nodeIf = new IfNode(nodeInstrucaoList);
			nodeIf.addNode(new TerminalNode(nodeIf,token));
			nodeInstrucaoList.addNode(nodeIf);
			lerProximoToken();
			instrucaoIf(nodeIf);
			instucaoList(nodeInstrucaoList);
			break;
		case WHILE:
			CTree nodeWhile = new WhileNode(nodeInstrucaoList);
			nodeWhile.addNode(new TerminalNode(nodeWhile,token));
			nodeInstrucaoList.addNode(nodeWhile);
			lerProximoToken();
			instrucaoWhile(nodeWhile);
			instucaoList(nodeInstrucaoList);
			break;
		case FOR:
			CTree nodeFor = new ForNode(nodeInstrucaoList);
			nodeFor.addNode(new TerminalNode(nodeFor,token));
			nodeInstrucaoList.addNode(nodeFor);
			lerProximoToken();
			instrucaoFor(nodeFor);
			instucaoList(nodeInstrucaoList);
			break;
		case FUNC_PRINTF:
			CTree nodePrintf = new PrintNode(nodeInstrucaoList);
			nodePrintf.addNode(new TerminalNode (nodePrintf, token));
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
			CTree nodeScanf = new ScanNode(nodeInstrucaoList);
			nodeScanf.addNode(new TerminalNode (nodeScanf, token));
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
			nodeFor.addNode(new TerminalNode(nodeFor, token));
			lerProximoToken();
			atribuicao(nodeFor);
			if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
				nodeFor.addNode(new TerminalNode(nodeFor,token));
				lerProximoToken();
				CTree exprL = new ExpressionNode(nodeFor);
				nodeFor.addNode(exprL);
				exprLogica(exprL);
				if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
					nodeFor.addNode(new TerminalNode(nodeFor,token));
					lerProximoToken();
					CTree expr = new ExpressionNode(nodeFor);
					nodeFor.addNode(expr);
					exprAritmetica(expr);
					if (token.getClasseToken() == ClasseToken.FPARENTESE) {
						nodeFor.addNode(new TerminalNode(nodeFor,token));
						lerProximoToken();
						if (token.getClasseToken() == ClasseToken.ACHAVE) {
							nodeFor.addNode(new TerminalNode(nodeFor,token));
							lerProximoToken();
							CTree instrucao = new StatementsNode(nodeFor);
							nodeFor.addNode(instrucao);
							instucaoList(instrucao);
							if (token.getClasseToken() != ClasseToken.FCHAVE) {
								throw new UndefinedSintaxeException(
										token.toString());
							}
							nodeFor.addNode(new TerminalNode(nodeFor,token));
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
		CTree exprL = new ExpressionNode(nodeWhile);
		nodeWhile.addNode(exprL);
		exprLogica(exprL);
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
			nodeWhile.addNode(new TerminalNode(nodeWhile,token));
			lerProximoToken();
			CTree instrucao = new StatementsNode(nodeWhile);
			nodeWhile.addNode(instrucao);
			instucaoList(instrucao);
			if (token.getClasseToken() != ClasseToken.FCHAVE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeWhile.addNode(new TerminalNode(nodeWhile,token));
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
		CTree nodeExpr = new ExpressionNode(nodeIf);
		nodeIf.addNode(nodeExpr);
		exprLogica(nodeExpr);
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
			nodeIf.addNode(new TerminalNode(nodeIf,token));
			lerProximoToken();
			CTree nodeInstrucao = new StatementsNode(nodeIf);
			nodeIf.addNode(nodeInstrucao);
			instucaoList(nodeInstrucao);
			if (token.getClasseToken() != ClasseToken.FCHAVE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeIf.addNode(new TerminalNode(nodeIf,token));
			lerProximoToken();
			while (token.getClasseToken() == ClasseToken.ELSE_IF) {
				CTree nodeElseIf = new ElseIfNode(nodeIf);
				nodeIf.addNode(nodeElseIf);
				nodeElseIf.addNode(new TerminalNode(nodeElseIf,token));
				lerProximoToken();
				CTree nodeExprL = new ExpressionNode(nodeElseIf);
				nodeElseIf.addNode(nodeExprL);
				exprLogica(nodeExprL);
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeElseIf.addNode(new TerminalNode(nodeElseIf,token));
					lerProximoToken();
					CTree nodeInstrucao1 = new StatementsNode(nodeElseIf);
					nodeElseIf.addNode(nodeInstrucao1);
					instucaoList(nodeInstrucao1);
					if (token.getClasseToken() != ClasseToken.FCHAVE) {
						throw new UndefinedSintaxeException(token.toString());
					}
					nodeElseIf.addNode(new TerminalNode(nodeElseIf,token));
					lerProximoToken();
				} else {
					throw new UndefinedSintaxeException(token.toString());
				}
			}

			if (token.getClasseToken() == ClasseToken.ELSE) {
				CTree nodeElse = new ElseNode(nodeIf);
				nodeIf.addNode(nodeElse);
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.ACHAVE) {
					nodeElse.addNode(new TerminalNode(nodeElse,token));
					lerProximoToken();
					CTree nodeInstrucao2 = new StatementsNode(nodeElse);
					nodeElse.addNode(nodeInstrucao2);
					instucaoList(nodeInstrucao2);
					if (token.getClasseToken() != ClasseToken.FCHAVE) {
						throw new UndefinedSintaxeException(token.toString());
					}
					nodeElse.addNode(new TerminalNode(nodeElse,token));
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
			instrucaoList.addNode(new TerminalNode(instrucaoList,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			break;
		case OP_NEGACAO:
		case OP_SUBTRACAO:
		case APARENTESE:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			exprLogica(nodeExpr);
			if (token.getClasseToken() != ClasseToken.FPARENTESE) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			break;
		case CONST_NUM:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			break;
		case ID_FUNCTION:
			CTree nodeChamadaFuncao = new FunctionCallNode(nodeExpr);
			CTree node = new TerminalNode(nodeChamadaFuncao,token);
			lerProximoToken();
			nodeChamadaFuncao.addNode(node);
			nodeExpr.addNode(nodeChamadaFuncao);
			chamadaFuncao(nodeChamadaFuncao);
			break;
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_REFERENCIA:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.ID) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			break;
		case ID:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
				nodeExpr.addNode(new TerminalNode(nodeExpr,token));
				lerProximoToken();				
				exprAritmetica(nodeExpr);
				if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
					throw new UndefinedSintaxeException(token.toString());
				}
				nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
			lerProximoToken();
			
			funcParamList(nodeChamadaFuncao);
			if (token.getClasseToken() == ClasseToken.FPARENTESE) {
				nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
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
		CTree nodeExpr = new ExpressionNode(nodeChamadaFuncao);
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			//AKI TAMB�M N�O TINHA O LER TOKEN. ADICIONEI!
			nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
			lerProximoToken();
			return;
		}
		if (token.getClasseToken() == ClasseToken.OP_REFERENCIA
				|| token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
			nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
			lerProximoToken();
			if (token.getClasseToken() != ClasseToken.ID) {
				throw new UndefinedSintaxeException(token.toString());
			}
			nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
			lerProximoToken();
		} else {
			nodeChamadaFuncao.addNode(nodeExpr);
			exprLogica(nodeExpr);
		}
		if (token.getClasseToken() == ClasseToken.VIRGULA) {
			nodeChamadaFuncao.addNode(new TerminalNode(nodeChamadaFuncao,token));
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
		nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
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
			nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
			lerProximoToken();
			dclList(nodeDclVariavel);
		}
	}

	private void atribuicao(CTree nodeDclVariavel) {
		ExpressionNode exprAtbrNode = new ExpressionNode(nodeDclVariavel);
		nodeDclVariavel.addNode(exprAtbrNode);
		atribuicaoAux(exprAtbrNode);
		if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
			exprAtbrNode.addNode(new TerminalNode(exprAtbrNode,token));
			lerProximoToken();
			CTree nodeExpr = new ExpressionNode(exprAtbrNode);
			exprAtbrNode.addNode(nodeExpr);
			exprLogica(nodeExpr);
		} else {
			termoUnarioAritmeticoPos(exprAtbrNode);
		}

	}

	private void atribuicaoAux(CTree nodeDclVariavel) {
		if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
			nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
			lerProximoToken();
		}
		if (token.getClasseToken() == ClasseToken.ID) {
			nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
				nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
				lerProximoToken();
				CTree nodeExpr = new ExpressionNode(nodeDclVariavel);
				nodeDclVariavel.addNode(nodeExpr);
				exprAritmetica(nodeExpr);
				if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
					throw new UndefinedSintaxeException(token.toString());
				}
				nodeDclVariavel.addNode(new TerminalNode(nodeDclVariavel,token));
				lerProximoToken();
			}
		}

	}

	private void exprLogica(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			/**
			 * MODIFIQUEI AKI, NAO TINHA ESSE LER PROXIMO TOKEN
			 */
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			fatorRelacional(nodeExpr);
			break;
		case BOOLEAN:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			termoRelacional(nodeExpr);
		}
	}

	private void termoLogicoAux(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_E) {
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
			exprRelacional(nodeExpr);
			termoLogicoAux(nodeExpr);
		}
	}

	private void exprLogicaAux(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_OU) {
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
		}
	}

	private void termoUnarioAritmeticoPre(CTree nodeExpr) {
		if (token.getClasseToken() == ClasseToken.OP_SUBTRACAO
				|| token.getClasseToken() == ClasseToken.OP_ADICAO
				|| token.getClasseToken() == ClasseToken.OP_ADICAO_ADICAO
				|| token.getClasseToken() == ClasseToken.OP_SUBTRACAO_SUBTRACAO) {
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
			lerProximoToken();
		}
		fatorAritmetico(nodeExpr);
	}

	private void termoArtimeticoAux(CTree nodeExpr) {
		switch (token.getClasseToken()) {
		case OP_MULTIPLICACAO_OU_DESREFERENCIA:
		case OP_RESTO:
		case OP_DIVISAO:
			nodeExpr.addNode(new TerminalNode(nodeExpr,token));
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
			nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
				lerProximoToken();
			}
			if (token.getClasseToken() == ClasseToken.ID) {
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
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
			nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.FCOLCHETE) {
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
				lerProximoToken();
				dclParam(nodeDclFunc);
			} else {
				throw new UndefinedSintaxeException(token.toString());
			}
		} else if (token.getClasseToken() == ClasseToken.VIRGULA) {
			nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.TIPO) {
				nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
				lerProximoToken();
				if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
					lerProximoToken();
				}
				if (token.getClasseToken() == ClasseToken.ID) {
					nodeDclFunc.addNode(new TerminalNode(nodeDclFunc,token));
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
