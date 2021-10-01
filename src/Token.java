public class Token {

    public static final int TK_IDENTIFIER = 0;
    public static final int TK_NUMBER = 1;
    public static final int TK_OPERATOR = 2;
    public static final int TK_RESERVED = 3;
    public static final int TK_SPECIAL = 4;
    public static final int TK_PRIVATE = 5;
    public static final int TK_CONDITIONAL = 6;
    
    private int type;
    private String text;

    public Token (int type, String text){
        super();
        this.type = type;
        this.text = text;
    }

    public Token () {
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
        switch(this.type) {
            case 0:
                return this.text + " - IDENTIFICADOR";
            case 1:
                return this.text + " - NÚMERO INTEIRO";
            case 2:
                return this.text + " - OPERADOR";
            case 3:
                return this.text + " - RESERVADO";
            case 4:
                return this.text + " - ESPECIAL";
            case 5:
                return this.text + " - PRIVADO";
            case 6:
                return this.text + " - CONDICIONAL";
            default:
                return "";
        }
    }
}