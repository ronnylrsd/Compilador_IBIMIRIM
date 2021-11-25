package AnalisadorSintatico;

import AnalisadorLexico.*;
import AnalisadorSemantico.Semantica;
import AnalisadorSemantico.ibiSemanticException;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual
    private Semantica[] semantica;
    private int contadorSemantica;
    private int contadorEscopo;
    private boolean[] isInt;
    private int contadorAritmetico;
    private String varAtribui;

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
        semantica = new Semantica[100];
        contadorSemantica = 0;
        contadorEscopo = 0;
        isInt = new boolean[100];
        contadorAritmetico = 0;
        varAtribui = null;
    }

    // Programa
    public void programa() {
        tipoInteiro(); // tipo 'INT'
        palavraMain(); // palavra_reservada 'main'
        parenteseAbre(); // caracter_especial '('
        parenteseFecha(); // caracter_especial ')'
        bloco(); // bloco
    }

    // Tipo Inteiro
    public void tipoInteiro() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("int") != 0) {
            throw new ibiSyntaxException("INT expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void palavraMain() {// Palavra Reservada (main)
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("main") != 0) {
            throw new ibiSyntaxException("MAIN expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // Bloco
    public void bloco() {
        chaveAbre(); // Caracter Especial
        contadorEscopo++; // abre escopo
        chaveAbre(); // Caracter Especial
        declaracaoVar(); // Declaração_Variável
        chaveFecha(); // Caracter Especial
        comando(); // Comando
        chaveFecha(); // Caracter Especial
        contadorEscopo--; // fecha escopo
    }

    // Separar caracteres especiais
    public void declaracaoVar() {// Declaração_variável
        semantica[contadorSemantica] = new Semantica();
        tipoIFC();// tipo INT, FLOAT, CHAR
        identificador(); // identificador
        pontoEvirgula(); // caracter_especial
        semantica[contadorSemantica].setEscopo(contadorEscopo); // setando escopo da variavel
        contadorSemantica++;
        declaracaoVarLoop(); // Declaração_variável_loop
    }

    public void declaracaoVarLoop() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_RESERVED && (token.getText().compareTo("int") == 0
                || token.getText().compareTo("float") == 0 || token.getText().compareTo("char") == 0)) {
            semantica[contadorSemantica] = new Semantica();
            semantica[contadorSemantica].setTipo(token.getText()); // setando tipo da variavel
            identificador(); // identificador
            pontoEvirgula(); // caracter_especial
            semantica[contadorSemantica].setEscopo(contadorEscopo); // setando escopo da variavel
            contadorSemantica++;
            declaracaoVarLoop(); // Declaração_variável_loop
        } else {
            scanner.back();
        }
    }

    // verifica se a variavel já existe (semantico)
    private boolean isExists(String nome) {
        if (contadorSemantica == 0) {
            return false;
        }
        for (int i = 0; i < contadorSemantica; i++) {
            if (semantica[i].getNome().compareTo(nome) == 0 && semantica[i].getEscopo() == contadorEscopo) {
                return true;
            }
        }
        return false;
    }

    // tipo INT, FLOAT, CHAR
    public void tipoIFC() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || (token.getText().compareTo("int") != 0
                && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0)) {
            throw new ibiSyntaxException("INT OR FLOAT or CHAR expected!, found " + Token.TK_TEXT[token.getType()]
                    + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
        semantica[contadorSemantica].setTipo(token.getText()); // setando tipo da variavel
    }

    // Identificador
    public void identificador() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new ibiSyntaxException("IDENTIFIER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
        if (isExists(token.getText())) {
            throw new ibiSemanticException("Variable " + token.getText() + " already exists.");
        }
        semantica[contadorSemantica].setNome(token.getText());
    }

    // ATRIBUICAO
    public void atribuicao() {
        atribui();
        aritmetica();
        pontoEvirgula();
    }

    // EXPRESSAO ARITMETICA
    public void aritmetica() {
        numAritmetica();
        aritmeticaLoop();
        verificaAritmetica();
    }

    // EXPRESSAO ARITMETICA LOOP
    public void aritmeticaLoop() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL && token.getType() != Token.TK_RELATIONAL) {
            operadorAritmetico();
            token = scanner.nextToken();
            if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("(") == 0) {
                numAritmetica();
                aritmeticaLoop();
                parenteseFecha();
            } else if (token.getType() == Token.TK_INT || token.getType() == Token.TK_FLOAT) {
                // salvando o tipo do token: INT ou FLOAT
                if (token.getType() == Token.TK_INT) {
                    isInt[contadorAritmetico] = true;
                    contadorAritmetico++;
                } else {
                    isInt[contadorAritmetico] = false;
                    contadorAritmetico++;
                }

                aritmeticaLoop();
            } else {
                scanner.back();
            }
        } else {
            scanner.back();
        }
    }

    // verifica se o tipo é compativel (semantica)
    public void verificaAritmetica() {
        for (int i = 1; i < contadorAritmetico; i++) {
            if (isInt[i - 1] != isInt[i]) { // se os tipos forem diferentes lanca o erro
                throw new ibiSemanticException("Variable type is not supported for arithmetic expression.");
            }
        }

        if(varAtribui != null) {
            for(int i = 0; i < contadorSemantica; i++) {
                if(semantica[i].getNome().compareTo(varAtribui) == 0 && semantica[i].getEscopo() == contadorEscopo) {
                    varAtribui = semantica[i].getTipo();
                }
            }
            for (int i = 1; i < contadorAritmetico; i++) {
                if(varAtribui.compareTo("int") == 0) {
                    if(isInt[i] == false) {
                        throw new ibiSemanticException("Variable type int is not supported for arithmetic expression float.");
                    }
                } else {
                    if(isInt[i] == true) {
                        throw new ibiSemanticException("Variable type float is not supported for arithmetic expression int.");
                    }
                }
            }
        }
        contadorAritmetico = 0;
        varAtribui = null;
    }

    // =
    public void atribui() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("= expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText()
                    + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // ;
    public void pontoEvirgula() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL) {
            throw new ibiSyntaxException("; expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText()
                    + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // NUMEROS EXPRESSAO ARITMETICA
    public void numAritmetica() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INT
                && token.getType() != Token.TK_FLOAT) {
            throw new ibiSyntaxException("ID or NUMBER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }

        // salvando o tipo do token: INT ou FLOAT
        if (token.getType() == Token.TK_INT) {
            isInt[contadorAritmetico] = true;
            contadorAritmetico++;
        } else {
            isInt[contadorAritmetico] = false;
            contadorAritmetico++;
        }
    }

    // OPERADOR ARITMETICO
    public void operadorAritmetico() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // comandos
    public void comando() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_RESERVED && token.getText().compareTo("if") == 0) { // IF ELSE
            parenteseAbre();
            relacional();
            parenteseFecha();
            comando();
            comandoElse();
        } else if (token.getType() == Token.TK_IDENTIFIER) { // atribuicao
            if (!isExists(token.getText())) { // se a variavel nao existir lanca o erro
                throw new ibiSemanticException("Variable " + token.getText() + " doesn't exists.");
            }
            verificaAtribuicao(token.getText());
            varAtribui = token.getText();
            atribuicao();
        } else if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("{") == 0) {
            scanner.back();
            bloco();
        } else if (token.getType() == Token.TK_RESERVED && token.getText().compareTo("while") == 0) {
            iteracao();
        } else {
            scanner.back();
        }
    }

    // verifica se o tipo da variavel é compativel com o tipo da atribuicao
    public void verificaAtribuicao(String nome) {
        for (int i = 0; i < contadorSemantica; i++) {
            if (semantica[i].getNome().compareTo(nome) == 0 && semantica[i].getEscopo() == contadorEscopo) {
                if (semantica[i].getTipo().compareTo("char") == 0) {
                    throw new ibiSemanticException("Variable type " + token.getText() + " is not compatible.");
                }
            }
        }
    }

    // PALAVRA RESERVADA ELSE
    public void palavraElse() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("else") != 0) {
            throw new ibiSyntaxException("ELSE expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // (
    public void parenteseAbre() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("(") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // )
    public void parenteseFecha() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo(")") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // }
    public void chaveAbre() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("{") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // }
    public void chaveFecha() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("}") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // EXPRESSAO RELACIONAL
    public void relacional() {
        aritmetica();
        operadorRelacional();
        aritmetica();
    }

    // OPERADOR RELACIONAL
    public void operadorRelacional() {
        if (token.getType() != Token.TK_RELATIONAL) {
            throw new ibiSyntaxException("Operator Relational expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // ELSE
    public void comandoElse() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("{") == 0) {
            palavraElse();
            comando();
            chaveFecha();
        } else {
            scanner.back();
        }
    }

    // ITERACAO
    public void iteracao() {
        parenteseAbre();
        relacional();
        parenteseFecha();
        comando();
    }
}
