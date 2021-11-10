package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }

    public void A() {
        Al();
        B();
        E();
        C();
    }

    public void E() {
        T();
        El();
    }

    public void El() {
        token = scanner.nextToken();
        if (token != null) {
            OP();
            T();
            El();
        }
    }

    public void B() {
        token = scanner.nextToken();
        if (token.getText().compareTo("=") != 0) {
            throw new ibiSyntaxException("= expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void C() {
        token = scanner.nextToken();
        if (token.getText().compareTo(";") != 0) {
            throw new ibiSyntaxException("; expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void Al() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new ibiSyntaxException("ID expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void T() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_INT) {
            throw new ibiSyntaxException("ID or NUMBER expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void OP() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() +  ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

}
