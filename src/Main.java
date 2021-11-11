public class Main {
    public static void main(String[] args) {
        try {
            IbiScanner sc = new IbiScanner("C:\\Users\\V\\Desktop\\Compilador_IBIMIRIM-main\\src\\input.ibi");
            IbiParser pa = new IbiParser(sc);

            pa.I();

            System.out.println("Compilação bem sucedida!");
        } catch (ibiLexicalException ex) {
            System.err.println("Erro Léxico: " + ex.getMessage());
        } catch (IbiSyntaxException ex) {
            System.err.println("Erro Sintático: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Erro Genérico: " + ex.getMessage());
        }
    }
}