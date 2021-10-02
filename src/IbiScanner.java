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
                    } else if (isSpace(currentChar)) {
                        estado = 0;
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
                    } else if (isOperatorAritmetico(currentChar)) {
                        if (currentChar == '=') {
                            term += currentChar;
                            estado = 3;
                            break;
                        }
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_ARITMETICO);
                        token.setText(term);
                        return token;
                    } else if (isLong(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_LONG);
                        token.setText(term);
                        return token;
                    } else if (isPow(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_POW);
                        token.setText(term);
                        return token;
                    } else if (isOperadorRelacional(currentChar)) {
                        term += currentChar;
                        estado = 4;
                    } else if (currentChar == '!') {
                        term += currentChar;
                        estado = 8;
                        // =======================================================
                    } else if (isDigit(currentChar)) {
                        term += currentChar;
                        estado = 9;
                    } else if (isBreak(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_BREAK);
                        token.setText(term);
                        return token;
                    } else if (isFinal(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_FINAL);
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
                    } else if (isSpace(currentChar) || isOperatorAritmetico(currentChar)) {
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
                    if (currentChar == '=') {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELACIONAL);
                        token.setText(term);

                        return token;
                    } else {
                        token = new Token();
                        token.setType(Token.TK_ARITMETICO);
                        token.setText(term);
                        back();
                        return token;
                    }
                case 4:
                    if (currentChar == '=') {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELACIONAL);
                        token.setText(term);
                        back();
                        return token;
                    } else {
                        token = new Token();
                        token.setType(Token.TK_RELACIONAL);
                        token.setText(term);

                        return token;
                    }
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
                    } else if (isSpace(currentChar) || isOperatorAritmetico(currentChar)) {
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
                case 8:
                    if (currentChar == '=') {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELACIONAL);
                        token.setText(term);
                        back();
                        return token;
                    } else {
                        throw new ibiLexicalException("Operador mal construido");
                    }
                case 9:
                    if (isDigit(currentChar)) {
                        estado = 9;
                        term += currentChar;
                    } else if (!isChar(currentChar) && !isDot(currentChar)) {
                        token = new Token();
                        token.setType(Token.TK_INT);
                        token.setText(term);
                        back();
                        return token;
                    } else if (isDot(currentChar)) {
                        estado = 10;
                        term += currentChar;
                    } else {
                        throw new ibiLexicalException("Número Inteiro mal construido");
                    }
                    break;
                case 10:
                    if (isDigit(currentChar)) {
                        estado = 10;
                        term += currentChar;
                    } else if (!isChar(currentChar)) {
                        token = new Token();
                        token.setType(Token.TK_FLOAT);
                        token.setText(term);
                        return token;
                    } else {
                        throw new ibiLexicalException("Número Float mal construido");
                    }
                    break;
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

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean isReserved(String c) {
        if (c.compareTo("main") == 0 || c.compareTo("if") == 0 || c.compareTo("else") == 0 || c.compareTo("while") == 0
                || c.compareTo("do") == 0 || c.compareTo("for") == 0 || c.compareTo("int") == 0
                || c.compareTo("float") == 0 || c.compareTo("char") == 0 || c.compareTo("ibimirim") == 0
                || c.compareTo("charrete") == 0) {
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

    private boolean isOperatorAritmetico(char c) {
        return c == '+' || c == '-' || c == '=' || c == '*' || c == '/';
    }

    private boolean isOperadorRelacional(char c) {
        return c == '>' || c == '<';
    }

    private boolean isLong(char c) {
        return c == '@';
    }

    private boolean isPow(char c) {
        return c == '^';

    }

    private boolean isFinal(char c) {
        return c == '$';
    }

    private boolean isDot(char c) {
        return c == '.';
    }

    private boolean isBreak(char c) {
        return c == '&';
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