package AnalisadorSemantico;

public class Semantica {
    private String nome;
    private String tipo;
    private int escopo;

    public void Semantica() {
        this.nome = "";
        this.tipo = "";
        this.escopo = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEscopo() {
        return escopo;
    }
    
    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }
}
