package Model;

public class MesaReserva {
    private int idMesa;
    private int idReserva;
    private int tempoAssociacao;
    private int tempoAtendimento;
    private boolean atendida;

    public MesaReserva(int idMesa, int idReserva, int tempoAssociacao) {
        this.idMesa = idMesa;
        this.idReserva = idReserva;
        this.tempoAssociacao = tempoAssociacao;
        this.tempoAtendimento = tempoAssociacao;
        this.atendida = false; // Inicialmente, a reserva não está atendida
    }

    // Getters e Setters

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getTempoAssociacao() {
        return tempoAssociacao;
    }

    public void setTempoAssociacao(int tempoAssociacao) {
        this.tempoAssociacao = tempoAssociacao;
    }

    public int getTempoAtendimento() {
        return tempoAtendimento;
    }

    public void setTempoAtendimento(int tempoAtendimento) {
        this.tempoAtendimento = tempoAtendimento;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    @Override
    public String toString() {
        return "MesaReserva{" +
                "idMesa=" + idMesa +
                ", idReserva=" + idReserva +
                ", tempoAssociacao=" + tempoAssociacao +
                ", tempoAtendimento=" + tempoAtendimento +
                ", atendida=" + atendida +
                '}';
    }
}