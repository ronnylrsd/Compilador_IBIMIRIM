package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
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
        chaveAbre(); // Caracter Especial
        declaracaoVar(); // Declaração_Variável
        chaveFecha(); // Caracter Especial
        comando(); // Comando
        chaveFecha(); // Caracter Especial
    }

    // Separar caracteres especiais
    public void declaracaoVar() {// Declaração_variável
        tipoIFC();// tipo INT, FLOAT, CHAR
        identificador(); // identificador
        pontoEvirgula(); // caracter_especial
        declaracaoVarLoop(); // Declaração_variável_loop
    }

    public void declaracaoVarLoop() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_RESERVED && (token.getText().compareTo("int") == 0
                || token.getText().compareTo("float") == 0 || token.getText().compareTo("char") == 0)) {
            identificador(); // identificador
            pontoEvirgula(); // caracter_especial
            declaracaoVarLoop(); // Declaração_variável_loop
        } else {
            scanner.back();
        }
    }

    // tipo INT, FLOAT, CHAR
    public void tipoIFC() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || (token.getText().compareTo("int") != 0
                && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0)) {
            throw new ibiSyntaxException("INT OR FLOAT or CHAR expected!, found " + Token.TK_TEXT[token.getType()]
                    + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // Identificador
    public void identificador() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new ibiSyntaxException("IDENTIFIER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
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
            } else {
                scanner.back();
                numAritmetica();
                aritmeticaLoop();
            }
        } else {
            scanner.back();
        }
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
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INT) {
            throw new ibiSyntaxException("ID or NUMBER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // OPERADOR ARITMETICO
    public void operadorAritmetico() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // // IF ELSE
    // public void comando() {
    // token = scanner.nextToken();
    // if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("if")
    // != 0) {
    // throw new ibiSyntaxException("Reserved Word expected!, found " +
    // Token.TK_TEXT[token.getType()] + " ("
    // + token.getText() + ") at Line " + token.getLine() + " and column " +
    // token.getColumn());
    // } else {
    // parenteseAbre();
    // relacional();
    // parenteseFecha();
    // comandoLoop();
    // comandoElse();
    // }
    // }

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

    // // WHILE
    // public void palavraWhile() {
    // token = scanner.nextToken();
    // if (token.getType() != Token.TK_RESERVED ||
    // token.getText().compareTo("while") != 0) {
    // throw new ibiSyntaxException("Reserved Word expected!, found " +
    // Token.TK_TEXT[token.getType()] + " ("
    // + token.getText() + ") at Line " + token.getLine() + " and column " +
    // token.getColumn());
    // }
    // }

}
