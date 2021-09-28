public class Token {

    public static final int TK_IDENTIFIER = 0;
    public static final int TK_NUMBER = 1;
    public static final int TK_OPERATOR = 2;
    public static final int TK_RESERVED = 3;
    public static final int TK_ASSIGN = 4;
    public static final int TK_SPECIAL = 5;
    public static final int TK_PRIVATE = 6;
    public static final int TK_CONDITIONAL = 7;
    
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
        return "Token [type=" + type + ", text=" + text + "]";
    }
}
