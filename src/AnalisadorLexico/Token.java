package AnalisadorLexico;

public class Token {

    public static final int TK_INT = 0; // tipo inteiro
    public static final int TK_FLOAT = 1; // tipo float
    public static final int TK_CHARACTER = 2; // tipo char
    public static final int TK_IDENTIFIER = 3; // identificador
    public static final int TK_RELATIONAL = 4; // operadores relacionais
    public static final int TK_ARITHMETIC = 5; // operadores aritmeticos
    public static final int TK_SPECIAL = 6; // caracteres especiais
    public static final int TK_RESERVED = 7; // palavras reservadas
    public static final int TK_PRIVATE = 8; // identificador privado
    public static final int TK_IF_ELSE = 9; // condicional IF ELSE
    public static final int TK_LONG = 10; // tipo long
    public static final int TK_POW = 11; // potencia
    public static final int TK_BREAK = 12; // break
    public static final int TK_COMENT_INLINE = 13; // comentário de linha
    public static final int TK_COMENT_VARIOUS_LINES = 14; // comentário de varias linhas
    public static final int TK_FINAL = 15; // marcador final

    public static final String TK_TEXT[] = {
        "INT","FLOAT","CHARACTER","IDENTIFIER", "RELACIONAL", "ARITHMETIC", "SPECIAL", "RESERVED", "PRIVATE", "IF_ELSE", "LONG", "POW", "BREAK", "COMENT_INLINE", "COMENT_VARIOUS_LINES", "FINAL"
    };

    private int type;
    private String text;

    private int line;
    private int column;

    public Token(int type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public Token() {
        super();
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        switch (this.type) {
        case 0:
            return this.text + " - INTEIRO";
        case 1:
            return this.text + " - FLOAT";
        case 2:
            return this.text + " - CHAR";
        case 3:
            return this.text + " - IDENTIFICADOR";
        case 4:
            return this.text + " - OPERADOR RELACIONAL";
        case 5:
            return this.text + " - OPERADOR ARITMETICO";
        case 6:
            return this.text + " - CARACTERE ESPECIAL";
        case 7:
            return this.text + " - PALAVRA RESERVADA";
        case 8:
            return this.text + " - IDENTIFICADOR PRIVADO";
        case 9:
            return this.text + " - CONDICIONAL IF ELSE";
        case 10:
            return this.text + " - LONG";
        case 11:
            return this.text + " - POTÊNCIA";
        case 12:
            return this.text + " - BREAK";
        case 13:
            return this.text + " - COMENTÁRIO DE LINHA";
        case 14:
            return this.text + " - COMENTÁRIO DE VÁRIAS LINHAS";
        case 15:
            return this.text + " - FINAL DE ARQUIVO";
        default:
            return "";
        }
    }
}