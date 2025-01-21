package Model;

public class DesempenhoFinanceiro {

    private Integer id;
    private double totalFaturado;
    private double totalGastos;
    private double totalLucro;
    private int numeroPedidosAtendidos;
    private int numeroPedidosNaoAtendidos;

    public DesempenhoFinanceiro() {
    }

    public DesempenhoFinanceiro(Integer id, int numeroPedidosNaoAtendidos, int numeroPedidosAtendidos, double totalLucro, double totalFaturado, double totalGastos) {
        this.id = id;
        this.numeroPedidosNaoAtendidos = numeroPedidosNaoAtendidos;
        this.numeroPedidosAtendidos = numeroPedidosAtendidos;
        this.totalLucro = totalLucro;
        this.totalFaturado = totalFaturado;
        this.totalGastos = totalGastos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getTotalFaturado() {
        return totalFaturado;
    }

    public void setTotalFaturado(double totalFaturado) {
        this.totalFaturado = totalFaturado;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getTotalLucro() {
        return totalLucro;
    }

    public void setTotalLucro(double totalLucro) {
        this.totalLucro = totalLucro;
    }

    public int getNumeroPedidosAtendidos() {
        return numeroPedidosAtendidos;
    }

    public void setNumeroPedidosAtendidos(int numeroPedidosAtendidos) {
        this.numeroPedidosAtendidos = numeroPedidosAtendidos;
    }

    public int getNumeroPedidosNaoAtendidos() {
        return numeroPedidosNaoAtendidos;
    }

    public void setNumeroPedidosNaoAtendidos(int numeroPedidosNaoAtendidos) {
        this.numeroPedidosNaoAtendidos = numeroPedidosNaoAtendidos;
    }
}
