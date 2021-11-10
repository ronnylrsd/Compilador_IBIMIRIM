import AnalisadorLexico.*;
import AnalisadorSintatico.*;

public class Main {
    public static void main(String[] args) {
        try {
            IbiScanner sc = new IbiScanner("E:\\Ramon\\Documentos\\GitHub\\Compilador_IBIMIRIM\\src\\input.ibi");
            IbiParser par = new IbiParser(sc);

            par.P();
            System.out.println("Compilação bem sucedida!");
        } 
        catch (ibiLexicalException ex) {
            System.err.println("Erro Léxico: " + ex.getMessage());
        }
        catch (ibiSyntaxException ex) {
            System.out.println("Erro Sintático: " + ex.getMessage());
        } 
        catch (Exception ex) {
            System.err.println("Erro Genérico: " + ex.getMessage());
        }
    }
}