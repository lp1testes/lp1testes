package Model;

public class Estatistica {

    private Integer id;
    private Prato pratoMaisPedido;
    private int totalClientesAtendidos;
    private double tempoMedioEsperaMesa;
    private double tempoMedioServicoMesa;

    public Estatistica() {
    }

    public Estatistica(Integer id, int totalClientesAtendidos, Prato pratoMaisPedido, double tempoMedioEsperaMesa, double tempoMedioServicoMesa) {
        this.id = id;
        this.totalClientesAtendidos = totalClientesAtendidos;
        this.pratoMaisPedido = pratoMaisPedido;
        this.tempoMedioEsperaMesa = tempoMedioEsperaMesa;
        this.tempoMedioServicoMesa = tempoMedioServicoMesa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Prato getPratoMaisPedido() {
        return pratoMaisPedido;
    }

    public void setPratoMaisPedido(Prato pratoMaisPedido) {
        this.pratoMaisPedido = pratoMaisPedido;
    }

    public int getTotalClientesAtendidos() {
        return totalClientesAtendidos;
    }

    public void setTotalClientesAtendidos(int totalClientesAtendidos) {
        this.totalClientesAtendidos = totalClientesAtendidos;
    }

    public double getTempoMedioEsperaMesa() {
        return tempoMedioEsperaMesa;
    }

    public void setTempoMedioEsperaMesa(double tempoMedioEsperaMesa) {
        this.tempoMedioEsperaMesa = tempoMedioEsperaMesa;
    }

    public double getTempoMedioServicoMesa() {
        return tempoMedioServicoMesa;
    }

    public void setTempoMedioServicoMesa(double tempoMedioServicoMesa) {
        this.tempoMedioServicoMesa = tempoMedioServicoMesa;
    }
}
