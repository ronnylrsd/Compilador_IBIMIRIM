import AnalisadorLexico.*;
import AnalisadorSemantico.ibiSemanticException;
import AnalisadorSintatico.*;

public class Main {
    public static void main(String[] args) {
        try {
            IbiScanner sc = new IbiScanner("C:\\Users\\djalm\\Documents\\GitHub\\Compilador_IBIMIRIM\\src\\input.ibi");
            IbiParser par = new IbiParser(sc);

            par.programa();
            System.out.println("Compilação bem sucedida!");
        } catch (ibiLexicalException ex) {
            System.err.println("Erro Léxico: " + ex.getMessage());
        } catch (ibiSyntaxException ex) {
            System.out.println("Erro Sintático: " + ex.getMessage());
        } catch (ibiSemanticException ex) {
            System.out.println("Erro Semântico: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Erro Genérico: " + ex.getMessage());
        }
    }
}