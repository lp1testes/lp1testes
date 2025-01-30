package Model;

public class Reserva {
    private int id;
    private String nome;
    private int numeroPessoas;
    private int tempoChegada;
    private boolean associada;

    public Reserva(int id, String nome, int numeroPessoas, int tempoChegada) {
        this.id = id;
        this.nome = nome;
        this.numeroPessoas = numeroPessoas;
        this.tempoChegada = tempoChegada;
        this.associada = false;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(int numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
    }

    public int getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(int tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    public boolean isAssociada() {
        return associada;
    }

    public void setAssociada(boolean associada) {
        this.associada = associada;
    }
}