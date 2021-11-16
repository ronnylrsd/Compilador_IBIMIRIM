package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }

    // ATRIBUICAO
    public void atribuicao() {
        id();
        atribui();
        aritmetica();
        pontoEvirgula();
        comandoIf();
    }

    // EXPRESSAO ARITMETICA
    public void aritmetica() {
        numAritmetica();
        aritmeticaLoop();
    }

    // EXPRESSAO ARITMETICA LOOP
    public void aritmeticaLoop() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL) {
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

    // ID
    public void id() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new ibiSyntaxException("ID expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
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

    // IF ELSE
    public void comandoIf() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_RESERVED && token.getText().compareTo("if") == 0) {
            parenteseAbre();
            relacional();
            parenteseFecha();
            comandoIf();
            comandoElse();
        } else if (token.getType() == Token.TK_IDENTIFIER) {
            atribui();
            aritmetica();
            pontoEvirgula();
            comandoIf();
        } else {
            scanner.back();
        }
    }

    // PALAVRA RESERVADA ELSE
    public void palavraElse() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("else") != 0) {
            throw new ibiSyntaxException("Reserved Word expected!, found " + Token.TK_TEXT[token.getType()] + " ("
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
    public void chaveFecha() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("}") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // EXPRESSAO RELACIONAL
    public void relacional() {

    }

    // ELSE
    public void comandoElse() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("{") == 0) {
            palavraElse();
            comandoIf();
            chaveFecha();
        } else {
            scanner.back();
        }
    }

}
