package Model;

public class Mesa {

    private Integer id;
    private int capacidade;
    private boolean ocupada;

    public Mesa() {
    }
    public Mesa(int id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
        this.ocupada = false; // valor padrão
    }
    public Mesa(Integer id, int capacidade, boolean ocupada) {
        this.id = id;
        this.capacidade = capacidade;
        this.ocupada = ocupada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
    @Override
    public String toString() {
        return "Mesa ID: " + id;
    }
}

