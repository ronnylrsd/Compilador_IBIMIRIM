package AnalisadorSintatico;

import AnalisadorLexico.*;

public class IbiParser {

    private IbiScanner scanner;
    private Token token; // Token atual

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }
    //Separar o que é INT e o que é MAIN
    //Separar os caracteres especias
    public void P() {//programa
        T(); //tipo
        PR(); //palavra_reservada
        CE(); //caracter_especial
        CE(); //caracter_especial
        B(); //bloco
    }

    public void PR() {//Palavra Reservada (main)
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED) {
            throw new ibiSyntaxException("MAIN expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void CE() {//Caracter Especial
        token = scanner.nextToken();
        if(token.getType() != Token.TK_SPECIAL){
            throw new ibiSyntaxException("SPECIAL_CHARACTER expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());  
        } 
    }
    //Adicionar se for vazio
    //Adicionar mais de uma decl_var e mais de um comando
    //Separar caracteres especiais
    public void B(){//Bloco
        CE(); //Caracter Especial
        DV(); //Declaração_Variável
        C(); //Comando
        CE(); //Caracter Especial
    }

    public void C(){//Comando

    }
    //Separar caracteres especiais
    public void DV() {//Declaração_variável
        T(); //tipo
        I(); //identificador
        CE(); //caracter_especial
    }


    public void I(){//Identificador
        token = scanner.nextToken();
        if(token.getType() != Token.TK_IDENTIFIER){
            throw new ibiSyntaxException("IDENTIFIER expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());  
        }
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

    public void T() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED) {
            throw new ibiSyntaxException("INT OR FLOAT or CHAR expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void OP() {
        if (token.getType() != Token.TK_ARITHMETIC) {
            throw new ibiSyntaxException("Operator Expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() +  ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

}