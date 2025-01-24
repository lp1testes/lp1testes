package Model;

public class MesaReserva {
    private int idMesa;
    private int idReserva;
    private int tempoAssociacao;

    public MesaReserva() {
    }

    public MesaReserva(int idMesa, int idReserva, int tempoAssociacao) {
        this.idMesa = idMesa;
        this.idReserva = idReserva;
        this.tempoAssociacao = tempoAssociacao;
    }

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

    @Override
    public String toString() {
        return "MesaReserva{" +
                "idMesa=" + idMesa +
                ", idReserva=" + idReserva +
                ", tempoAssociacao=" + tempoAssociacao +
                '}';
    }
}