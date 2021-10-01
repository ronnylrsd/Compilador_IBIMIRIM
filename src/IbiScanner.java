import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IbiScanner {

    private char[] content;
    private int estado;
    private int pos;

    public IbiScanner(String filename) {
        try {
            String txtConteudo;
            txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            System.out.println("--- INPUT ---");
            System.out.println(txtConteudo);
            System.out.println("-------------");
            content = txtConteudo.toCharArray();
            pos = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Token nextToken() {
        char currentChar;
        Token token = null;
        String term = "";
        if (isEOF()) {
            return null;
        }

        estado = 0;
        while (pos < content.length) {
            if (isEOF()) {
                return null;
            }
            currentChar = nextChar();

            switch (estado) {
                case 0:
                    if (isChar(currentChar)) {
                        term += currentChar;
                        estado = 1;
                    } else if (isDigit(currentChar)) {
                        estado = 3;
                        term += currentChar;
                    } else if (isSpace(currentChar)) {
                        estado = 0;
                    } else if (isOperator(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_OPERATOR);
                        token.setText(term);
                        return token;
                    } else if (isSpecial(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_SPECIAL);
                        token.setText(term);
                        return token;
                    } else if (isPrivate(currentChar)) {
                        term += currentChar;
                        estado = 5;
                    } else if (isConditional(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_CONDITIONAL);
                        token.setText(term);
                        return token;
                    } else {
                        throw new ibiLexicalException("Simbolo mal construido");
                    }
                    break;
                case 1:
                    if (isChar(currentChar) || isDigit(currentChar)) {
                        estado = 1;
                        term += currentChar;
                    } else if (isSpace(currentChar) || isOperator(currentChar)) {
                        estado = 2;
                    } else {
                        throw new ibiLexicalException("Identificador mal formado");
                    }
                    break;
                case 2:
                    token = new Token();
                    if (isReserved(term)) {
                        token.setType(Token.TK_RESERVED);
                    } else {
                        token.setType(Token.TK_IDENTIFIER);
                    }
                    token.setText(term);
                    back();
                    return token;
                case 3:
                    if (isDigit(currentChar)) {
                        estado = 3;
                        term += currentChar;
                    } else if (!isChar(currentChar)) {
                        estado = 4;
                    } else {
                        throw new ibiLexicalException("Número mal construido");
                    }
                    break;
                case 4:
                    token = new Token();
                    token.setType(Token.TK_NUMBER);
                    token.setText(term);
                    back();
                    return token;
                case 5:
                    if (isChar(currentChar)) {
                        term += currentChar;
                        estado = 6;
                    } else {
                        throw new ibiLexicalException("Identificador privado mal formado");
                    }
                    break;
                case 6:
                    if (isChar(currentChar) || isDigit(currentChar)) {
                        estado = 6;
                        term += currentChar;
                    } else if (isSpace(currentChar) || isOperator(currentChar)) {
                        estado = 7;
                    } else {
                        throw new ibiLexicalException("Identificador privado mal formado");
                    }
                    break;
                case 7:
                    token = new Token();
                    token.setType(Token.TK_PRIVATE);
                    token.setText(term);
                    back();
                    return token;
            }
        }
        return token;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isOperator(char c) {
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean isReserved(String c) {
        if(c.compareTo("main") == 0 || c.compareTo("if")  == 0 || c.compareTo("else") == 0 || c.compareTo("while") == 0 || c.compareTo("do") == 0 || c.compareTo("for") == 0 || c.compareTo("int") == 0 || c.compareTo("float") == 0 || c.compareTo("char") == 0){
            return true;
        } else {
            return false;
        }
    }

    private boolean isSpecial(char c) {
        return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';';
    }

    private boolean isPrivate(char c) {
        return c == '#';
    }

    private boolean isConditional(char c) {
        return c == '?' || c == ':';
    }

    private char nextChar() {
        return content[pos++];
    }

    private void back() {
        pos--;
    }

    private boolean isEOF() {
        return pos == content.length;
    }

}