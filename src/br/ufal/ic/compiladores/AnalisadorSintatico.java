package br.ufal.ic.compiladores;

import br.ufal.ic.compiladores.exceptions.UndefinedSintaxeException;
import br.ufal.ic.compiladores.exceptions.UndefinedTokenException;
import br.ufal.ic.compiladores.token.ClasseToken;
import br.ufal.ic.compiladores.token.Token;
import br.ufal.ic.ctree.CNode;
import br.ufal.ic.ctree.CTree;
import br.ufal.ic.ctree.ICTree;
import br.ufal.ic.ctree.NodeType;

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
    public void runAnaliseSintatica(CTree rootNode)
	    throws UndefinedSintaxeException {
	lerProximoToken();
	while (token != null) {
	    if (token.getClasseToken() == ClasseToken.TIPO
		    || token.getClasseToken() == ClasseToken.FUNC_MAIN
		    || token.getClasseToken() == ClasseToken.VOID) {

		switch (token.getClasseToken()) {
		case TIPO:
		case VOID:
		    rootNode.addNode(new CTree(NodeType.DCL_FUNC));
		    break;
		case FUNC_MAIN:
		    rootNode.addNode(new CTree(NodeType.DCL_MAIN));
		default:
		    break;
		}

		lerProximoToken();

		if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
		    CNode node = new CNode(token);
		    lerProximoToken();
		}
		if (token.getClasseToken() == ClasseToken.ID_FUNCTION
			|| token.getClasseToken() == ClasseToken.FUNC_MAIN) {
		    lerProximoToken();
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
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
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
	case ID_FUNCTION:
	    lerProximoToken();
	    chamadaFuncao();
	    if (token.getClasseToken() != ClasseToken.PONTO_VIRGULA) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	    instucaoList();
	    break;
	case OP_MULTIPLICACAO_OU_DESREFERENCIA:
	case OP_ADICAO_ADICAO:
	case OP_SUBTRACAO_SUBTRACAO:
	    lerProximoToken();
	case ID:
	    // Aqui foi fatorado a esquerda para identificar adequadamente qual
	    // producao usar..
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
	case FUNC_PRINTF:
	    lerProximoToken();
	    if (token.getClasseToken() != ClasseToken.APARENTESE) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	    funcaoImprimir();
	    instucaoList();
	    break;
	case FUNC_SCANF:
	    lerProximoToken();
	    if (token.getClasseToken() != ClasseToken.APARENTESE) {
		throw new UndefinedSintaxeException(token.toString());
	    }
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
     */
    private void instrucaoFor() {
	if (token.getClasseToken() == ClasseToken.APARENTESE) {
	    lerProximoToken();
	    atribuicao();
	    if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
		lerProximoToken();
		exprLogica();
		if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
		    lerProximoToken();
		    exprAritmetica();
		    if (token.getClasseToken() == ClasseToken.FPARENTESE) {
			lerProximoToken();
			if (token.getClasseToken() == ClasseToken.ACHAVE) {
			    lerProximoToken();
			    instucaoList();
			    if (token.getClasseToken() != ClasseToken.FCHAVE) {
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

    /*
     * Reconhece atribuicoes especificamente para isntrucoes FOR onde nao eh
     * permitido atribuicoes que nao sejam aritmeticas
     * 
     * private void instrucaoAtribuicaoInstrucaoFor() { if
     * (token.getClasseToken() == ClasseToken.ID) { lerProximoToken(); if
     * (token.getClasseToken() == ClasseToken.ATRIBUICAO) { lerProximoToken();
     * exprAritmetica(); } else if (token.getClasseToken() ==
     * ClasseToken.ACOLCHETE) { lerProximoToken(); arrayTerminalExpressoes(); }
     * else { throw new UndefinedSintaxeException(token.toString()); } } else {
     * throw new UndefinedSintaxeException(token.toString()); } }
     */

    /**
     * Reconhece a instrucao While
     */
    private void instrucaoWhile() {
	exprLogica();
	if (token.getClasseToken() == ClasseToken.ACHAVE) {
	    lerProximoToken();
	    instucaoList();
	    if (token.getClasseToken() != ClasseToken.FCHAVE) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	} else {
	    throw new UndefinedSintaxeException(token.toString());
	}
    }

    /**
     * Reconhece instrucao If-IfElse-Else
     */
    private void instrucaoIf() {
	exprLogica();
	if (token.getClasseToken() == ClasseToken.ACHAVE) {
	    lerProximoToken();
	    instucaoList();
	    if (token.getClasseToken() != ClasseToken.FCHAVE) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	    while (token.getClasseToken() == ClasseToken.ELSE_IF) {
		lerProximoToken();
		exprLogica();
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
		    lerProximoToken();
		    instucaoList();
		    if (token.getClasseToken() != ClasseToken.FCHAVE) {
			throw new UndefinedSintaxeException(token.toString());
		    }
		    lerProximoToken();
		} else {
		    throw new UndefinedSintaxeException(token.toString());
		}
	    }

	    if (token.getClasseToken() == ClasseToken.ELSE) {
		lerProximoToken();
		if (token.getClasseToken() == ClasseToken.ACHAVE) {
		    lerProximoToken();
		    instucaoList();
		    if (token.getClasseToken() != ClasseToken.FCHAVE) {
			throw new UndefinedSintaxeException(token.toString());
		    }
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
     */
    private void instrucaoFatoradaId() {
	atribuicao();
	if (token.getClasseToken() == ClasseToken.PONTO_VIRGULA) {
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
     */
    private void fatorAritmetico() {
	switch (token.getClasseToken()) {
	case CADEIA_CARACTER:
	case CARACTER:
	    lerProximoToken();
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
	case ID_FUNCTION:
	    lerProximoToken();
	    chamadaFuncao();
	    break;
	case OP_MULTIPLICACAO_OU_DESREFERENCIA:
	case OP_REFERENCIA:
	    lerProximoToken();
	    if (token.getClasseToken() != ClasseToken.ID) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	    break;
	case ID:
	    lerProximoToken();
	    if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
		lerProximoToken();
		exprAritmetica();
		if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
		    throw new UndefinedSintaxeException(token.toString());
		}
		lerProximoToken();
	    }
	    break;
	default:
	    throw new UndefinedSintaxeException(token.toString());

	}
    }

    /**
     * Reconhece chamada de funcoes
     */
    private void chamadaFuncao() {
	if (token.getClasseToken() == ClasseToken.APARENTESE) {
	    lerProximoToken();
	    funcParamList();
	    if (token.getClasseToken() == ClasseToken.FPARENTESE) {
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
     */
    private void funcParamList() {
	if (token.getClasseToken() == ClasseToken.FPARENTESE) {
	    return;
	}
	if (token.getClasseToken() == ClasseToken.OP_REFERENCIA
		|| token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
	    lerProximoToken();
	    if (token.getClasseToken() != ClasseToken.ID) {
		throw new UndefinedSintaxeException(token.toString());
	    }
	    lerProximoToken();
	} else {
	    exprLogica();
	}
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
	atribuicao();
	dclL();
    }

    /**
     * Mais de uma variavel declarada
     */
    private void dclL() {
	if (token.getClasseToken() == ClasseToken.VIRGULA) {
	    lerProximoToken();
	    dclList();
	}
    }

    private void atribuicao() {
	atribuicaoAux();
	if (token.getClasseToken() == ClasseToken.ATRIBUICAO) {
	    lerProximoToken();
	    exprLogica();
	} else {
	    termoUnarioAritmeticoPos();
	}

    }

    private void atribuicaoAux() {
	if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
	    lerProximoToken();
	}
	if (token.getClasseToken() == ClasseToken.ID) {
	    lerProximoToken();
	    if (token.getClasseToken() == ClasseToken.ACOLCHETE) {
		lerProximoToken();
		exprAritmetica();
		if (token.getClasseToken() != ClasseToken.FCOLCHETE) {
		    throw new UndefinedSintaxeException(token.toString());
		}
		lerProximoToken();
	    }
	}

    }

    private void exprLogica() {
	if (token.getClasseToken() == ClasseToken.FPARENTESE) {
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
	    termoUnarioAritmeticoPos();
	}
    }

    private void termoAritmetico() {
	termoUnarioAritmeticoPre();
	termoArtimeticoAux();
    }

    private void termoUnarioAritmeticoPos() {
	if (token.getClasseToken() == ClasseToken.OP_ADICAO_ADICAO
		|| token.getClasseToken() == ClasseToken.OP_SUBTRACAO_SUBTRACAO) {
	    lerProximoToken();
	}
    }

    private void termoUnarioAritmeticoPre() {
	if (token.getClasseToken() == ClasseToken.OP_SUBTRACAO
		|| token.getClasseToken() == ClasseToken.OP_ADICAO
		|| token.getClasseToken() == ClasseToken.OP_ADICAO_ADICAO
		|| token.getClasseToken() == ClasseToken.OP_SUBTRACAO_SUBTRACAO) {
	    lerProximoToken();
	}
	fatorAritmetico();
    }

    private void termoArtimeticoAux() {
	switch (token.getClasseToken()) {
	case OP_MULTIPLICACAO_OU_DESREFERENCIA:
	case OP_RESTO:
	case OP_DIVISAO:
	    lerProximoToken();
	    termoUnarioAritmeticoPre();
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
	    if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
		lerProximoToken();
	    }
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
		if (token.getClasseToken() == ClasseToken.OP_MULTIPLICACAO_OU_DESREFERENCIA) {
		    lerProximoToken();
		}
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
