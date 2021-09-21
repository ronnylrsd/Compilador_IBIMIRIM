public class Main {
    public static void main(String[] args) {
        try {
            IbiScanner sc = new IbiScanner("C:\\Users\\Davi\\Documents\\GitHub\\Compilador_IBIMIRIM\\src\\input.ibi");
            Token token = null;
            do {
                token = sc.nextToken();
                if (token != null) {
                    System.out.println(token);
                }
            } while (token != null);
        } catch (ibiLexicalException ex) {
            System.out.println("Lexical ERROR " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Gereric Error!");
        }
    }
}
