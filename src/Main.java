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
            System.out.println("Compilação bem sucedida!");
        } catch (ibiLexicalException ex) {
            System.err.println("Erro Léxico: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Erro Genérico: " + ex.getMessage());
        }
    }
}