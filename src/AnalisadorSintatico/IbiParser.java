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
        TI(); //tipo 'INT'
        PRM(); //palavra_reservada 'main'
        PA(); //caracter_especial '('
        PF(); //caracter_especial ')'
        B(); //bloco
    }

    public void TI() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("int") != 0) {
            throw new ibiSyntaxException("INT expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void PRM() {//Palavra Reservada (main)
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED || token.getText().compareTo("main") != 0) {
            throw new ibiSyntaxException("MAIN expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void PA() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("(") != 0) {
            throw new ibiSyntaxException("Caracter Special '(' expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // )
    public void PF() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo(")") != 0) {
            throw new ibiSyntaxException("Caracter Special ')' expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }
    //Adicionar mais de uma decl_var e mais de um comando
    public void B(){//Bloco
        CA(); //Caracter Especial
        DV(); //Declaração_Variável
        C(); //Comando
        CF(); //Caracter Especial 
    }

    // {
    public void CA() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("{") != 0) {
            throw new ibiSyntaxException("Caracter Special '{' expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // }
    public void CF() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo("}") != 0) {
            throw new ibiSyntaxException("Caracter Special '}' expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void C(){//Comando

    }
    //Separar caracteres especiais
    public void DV() {//Declaração_variável
        TIFC();//tipo INT, FLOAT, CHAR
        I(); //identificador
        PV(); //caracter_especial
    }

    public void TIFC(){
        token = scanner.nextToken();
        if(token.getType() != Token.TK_RESERVED || (token.getText().compareTo("int") != 0 && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0)){
            throw new ibiSyntaxException("INT OR FLOAT or CHAR expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    // ;
    public void PV() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL || token.getText().compareTo(";") != 0) {
            throw new ibiSyntaxException("Caracter Special ';' expected!, found " + Token.TK_TEXT[token.getType()] + " ("
                    + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());
        }
    }

    public void I(){//Identificador
        token = scanner.nextToken();
        if(token.getType() != Token.TK_IDENTIFIER){
            throw new ibiSyntaxException("IDENTIFIER expected!, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ") at Line " + token.getLine() + " and column " + token.getColumn());  
        }
    }


}