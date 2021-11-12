package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }

    // ATRIBUICAO
    public void A() {
        Al();
        B();
        E();
        P();
        C();
    }

    // EXPRESSAO ARITMETICA
    public void E() {
        T();
        El();
    }

    // EXPRESSAO ARITMETICA LOOP
    public void El() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL) {
            OP();
            token = scanner.nextToken();
            if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("(") == 0) {
                T();
                El();
                PAF();
            } else {
                scanner.back();
                T();
                El();
            }
        } else {
            scanner.back();
        }
    }

    // =
    public void B() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("= expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText()
                    + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // ;
    public void P() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL) {
            throw new ibiSyntaxException("; expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText()
                    + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // ID
    public void Al() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new ibiSyntaxException("ID expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // NUMEROS EXPRESSAO ARITMETICA
    public void T() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INT) {
            throw new ibiSyntaxException("ID or NUMBER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // OPERADOR ARITMETICO
    public void OP() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // IF ELSE
    public void C() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_RESERVED && token.getText().compareTo("if") == 0) {
            PAA();
            ER();
            PAF();
            C();
            ES();
        } else if (token.getType() == Token.TK_IDENTIFIER) {
            B();
            E();
            P();
            C();
        } else {
            scanner.back();
        }
    }

    // PALAVRA RESERVADA
    public void PR() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED) {
            throw new ibiSyntaxException("Reserved Word expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // (
    public void PAA() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL && token.getText().compareTo("(") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // )
    public void PAF() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL && token.getText().compareTo(")") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // }
    public void PE() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL && token.getText().compareTo("}") != 0) {
            throw new ibiSyntaxException("Caracter Special expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // EXPRESSAO RELACIONAL
    public void ER() {

    }

    // ELSE
    public void ES() {
        token = scanner.nextToken();
        if (token.getType() == Token.TK_SPECIAL && token.getText().compareTo("{") == 0) {
            PR();
            C();
            PE();
        } else {
            scanner.back();
        }
    }

}
