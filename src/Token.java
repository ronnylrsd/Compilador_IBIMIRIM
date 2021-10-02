public class Token {

    public static final int TK_IDENTIFIER = 0; // identificador
    public static final int TK_RESERVED = 1; // palavras reservadas
    public static final int TK_SPECIAL = 2; // caracteres especiais
    public static final int TK_PRIVATE = 3; // identificador privado
    public static final int TK_IF_ELSE = 4; // condicional IF ELSE
    public static final int TK_ARITMETICO = 5; // operadores aritmeticos
    public static final int TK_RELACIONAL = 6; // operadores relacionais
    public static final int TK_LONG = 7; // tipo long
    public static final int TK_POW = 8; // potencia
    public static final int TK_INT = 9; // tipo inteiro
    public static final int TK_FLOAT = 10; // tipo float
    public static final int TK_BREAK = 11; // break
    public static final int TK_COMMITLINE = 12; // comentário de linha
    public static final int TK_COMMITPARAGRAPH = 13; // comentário de paragrafo
    public static final int TK_CHARACTER = 14; // tipo char
    public static final int TK_FINAL = 99; // marcador final

    private int type;
    private String text;

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

    public String toString() {
        switch (this.type) {
            case 0:
                return this.text + " - IDENTIFICADOR";
            case 1:
                return this.text + " - PALAVRA RESERVADA";
            case 2:
                return this.text + " - CARACTERE ESPECIAL";
            case 3:
                return this.text + " - IDENTIFICADOR PRIVADO";
            case 4:
                return this.text + " - CONDICIONAL";
            case 5:
                return this.text + " - OPERADOR ARITMETICO";
            case 6:
                return this.text + " - OPERADOR RELACIONAL";
            case 7:
                return this.text + " - LONG";
            case 8:
                return this.text + " - POTÊNCIA";
            case 9:
                return this.text + " - INT";
            case 10:
                return this.text + " - FLOAT";
            case 11:
                return this.text + " - BREAK";
            case 12:
                return this.text + " - COMENTÁRIO DE LINHA";
            case 13:
                return this.text + " - COMENTÁRIO DE PARAGRAFO";
            case 14:
                return this.text + " - CHAR";
            case 99:
                return this.text + " - FINAL DE ARQUIVO";
            default:
                return "";
        }
    }
}