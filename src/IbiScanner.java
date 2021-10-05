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
                        token.setType(Token.TK_IF_ELSE);
                        token.setText(term);
                        return token;
                    } else if (isArithmeticOperator(currentChar)) {
                        if (currentChar == '=') {
                            term += currentChar;
                            estado = 3;
                            break;
                        }
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_ARITHMETIC);
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
                    } else if (isRelationalOperator(currentChar)) {
                        term += currentChar;
                        estado = 4;
                    } else if (currentChar == '!') {
                        term += currentChar;
                        estado = 8;
                    } else if (isDigit(currentChar)) {
                        term += currentChar;
                        estado = 9;
                    } else if (isBreak(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_BREAK);
                        token.setText(term);
                        return token;
                    } else if (isComentInLine(currentChar)) {
                        term += currentChar;
                        estado = 11;
                    } else if (isComentVariousLines(currentChar)) {
                        term += currentChar;
                        estado = 12;
                    } else if (isCaracter(String.valueOf(currentChar))) {
                        term += currentChar;
                        estado = 13;
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
                    if (isChar(currentChar) || isDigit(currentChar) || isUnderline(currentChar)) {
                        estado = 1;
                        term += currentChar;
                    } else if (isSpace(currentChar) || isArithmeticOperator(currentChar)) {
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
                        token.setType(Token.TK_RELATIONAL);
                        token.setText(term);

                        return token;
                    } else {
                        token = new Token();
                        token.setType(Token.TK_ARITHMETIC);
                        token.setText(term);
                        back();
                        return token;
                    }
                case 4:
                    if (currentChar == '=') {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_RELATIONAL);
                        token.setText(term);
                        return token;
                    } else {
                        token = new Token();
                        token.setType(Token.TK_RELATIONAL);
                        token.setText(term);
                        return token;
                    }
                case 5:
                    if (isChar(currentChar)) {
                        term += currentChar;
                        estado = 6;
                    } else {
                        throw new ibiLexicalException("Identificador PRIVADO mal formado");
                    }
                    break;
                case 6:
                    if (isChar(currentChar) || isDigit(currentChar)) {
                        estado = 6;
                        term += currentChar;
                    } else if (isSpace(currentChar) || isArithmeticOperator(currentChar)) {
                        estado = 7;
                    } else {
                        throw new ibiLexicalException("Identificador PRIVADO mal formado");
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
                        token.setType(Token.TK_RELATIONAL);
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
                case 11:
                    if (isComentInLine(currentChar) || isDigit(currentChar) || isChar(currentChar)
                            || isOperator(currentChar) || isPrivate(currentChar) || isConditional(currentChar)
                            || isUnderline(currentChar)) {
                        estado = 11;
                        term += currentChar;
                    } else if (isSpace(currentChar)) {
                        token = new Token();
                        token.setType(Token.TK_COMENT_INLINE);
                        token.setText(term);
                        return token;
                    } else {
                        throw new ibiLexicalException("Comentário de linha mal construido");
                    }
                    break;
                case 12:
                    if (isSpace(currentChar) || isDigit(currentChar) || isChar(currentChar) || isOperator(currentChar)
                            || isPrivate(currentChar) || isConditional(currentChar) || isUnderline(currentChar)) {
                        estado = 12;
                        term += currentChar;
                    } else if (isComentVariousLines(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_COMENT_VARIOUS_LINES);
                        token.setText(term);
                        return token;
                    } else {
                        throw new ibiLexicalException("Comentário de Parágrafo mal construido");
                    }
                    break;
                case 13:
                    if (isDigit(currentChar) || isChar(currentChar)) {
                        term += currentChar;
                        estado = 14;
                    } else {
                        throw new ibiLexicalException("Char mal construido");
                    }
                    break;
                case 14:
                    if (isCaracter(String.valueOf(currentChar))) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.TK_CHARACTER);
                        token.setText(term);
                        return token;
                    } else {
                        throw new ibiLexicalException("Char mal construido");
                    }
            }
        }
        return token;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isChar(char c) {
        return (c >= 'a' && c <= 'z');
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

    private boolean isArithmeticOperator(char c) {
        return c == '+' || c == '-' || c == '=' || c == '*' || c == '/';
    }

    private boolean isRelationalOperator(char c) {
        return c == '>' || c == '<';
    }

    private boolean isLong(char c) {
        return c == '@';
    }

    private boolean isPow(char c) {
        return c == '^';

    }

    private boolean isComentInLine(char c) {
        return c == '`';
    }

    private boolean isComentVariousLines(char c) {
        return (c == '~');
    }

    private boolean isOperator(char c) {
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    private boolean isCaracter(String c) {
        return (c.compareTo("'") == 0);
    }

    private boolean isUnderline(char c) {
        return c == '_';
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