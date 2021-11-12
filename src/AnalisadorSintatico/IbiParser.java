package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }

    public void E() {
        // T();
        // El();
        // Er();
        // Et();
    }

    public void Et() {
        abreParenteses();
        token = scanner.nextToken();
        T();
        El();
        fechaParenteses();

    }

    public void ER() {
        T();
        token = scanner.nextToken();
        operadorRelacional();
        // scanner.back();
        T();

    }

    public void El() {
        T();
        token = scanner.nextToken();
        if (token.getType() != Token.TK_FINAL ) {
            operadorAritmetico();
            T();
            El();
        }
    }

    public void T() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INT) {
            throw new ibiSyntaxException("ID or NUMBER expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void operadorAritmetico() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Aritmetico Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void operadorRelacional() {
        if (token.getType() != Token.TK_RELATIONAL) {
            throw new ibiSyntaxException("Operator relacional Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void abreParenteses() {
        if (token.getType() != Token.TK_SPECIAL && token.getText() != "(") {
           
            throw new ibiSyntaxException("Abre Parenteses Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void fechaParenteses() {
        if (token.getType() != Token.TK_SPECIAL && token.getText() != ")") {
            throw new ibiSyntaxException("Fecha Parenteses Expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }
}
