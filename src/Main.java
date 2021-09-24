public class Main {
    public static void main(String[] args) {
        try {
            IbiScanner sc = new IbiScanner("C:\\Users\\djalm\\Documents\\GitHub\\Compilador_IBIMIRIM\\src\\input.ibi");
            Token token = null;
            do {
                token = sc.nextToken();
                if (token != null) {
                    System.out.println(token);
                }
            } while (token != null);
        } catch (ibiLexicalException ex) {
            System.out.println("Erro léxico: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
