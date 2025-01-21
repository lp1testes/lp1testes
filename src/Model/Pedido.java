package Model;

import java.util.List;

public class Pedido {

    private Integer id;
    private Mesa mesa;
    private List<Prato> pratos;
    private double totalCusto;
    private double precoTotal;
    private double lucro;

    public Pedido() {
    }

    public Pedido(Integer id, Mesa mesa, List<Prato> pratos, double totalCusto, double lucro, double precoTotal) {
        this.id = id;
        this.mesa = mesa;
        this.pratos = pratos;
        this.totalCusto = totalCusto;
        this.lucro = lucro;
        this.precoTotal = precoTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<Prato> getPratos() {
        return pratos;
    }

    public void setPratos(List<Prato> pratos) {
        this.pratos = pratos;
    }

    public double getTotalCusto() {
        return totalCusto;
    }

    public void setTotalCusto(double totalCusto) {
        this.totalCusto = totalCusto;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public double getLucro() {
        return lucro;
    }

    public void setLucro(double lucro) {
        this.lucro = lucro;
    }
}
