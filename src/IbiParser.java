public class IbiParser {
    private IbiScanner scanner;
    private Token token;

    public IbiParser(IbiScanner scanner) {
        this.scanner = scanner;
    }

    public void I() {
        T();
        Parenthesis();
        Variable();
        OP_Relacional();
        Variable();
        Parenthesis();
    }

    public void OP_Relacional() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RELATIONAL) {
            throw new IbiSyntaxException("RELATIONAL EXPRESSION expected");
        }
    }

    public void Parenthesis() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_SPECIAL) {
            throw new IbiSyntaxException("PARENTHESIS expected");
        }
    }

    public void Variable() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_INT && token.getType() != Token.TK_FLOAT
                && token.getType() != Token.TK_CHARACTER) {
            throw new IbiSyntaxException("NUMBER expected");
        }
    }

    public void T() {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_RESERVED) {
            throw new IbiSyntaxException("RESERVED TOKEN expected");
        }
    }
}
