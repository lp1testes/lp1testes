package Model;

public class Reserva {

    private Integer id;
    private String nome;
    private int numeroPessoas;
    private int tempoChegada;

    public Reserva() {
    }

    public Reserva(Integer id, String nome, int numeroPessoas,  int tempoChegada) {
        this.id = id;
        this.nome = nome;
        this.numeroPessoas = numeroPessoas;
        this.tempoChegada = tempoChegada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
