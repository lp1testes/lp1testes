package Model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private Mesa mesa;
    private List<Prato> pratos;
    private List<Menu> menus;
    private double totalCusto;
    private double totalVenda;
    private double lucro;

    public Pedido() {
        this.pratos = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.totalCusto = 0.0;
        this.totalVenda = 0.0;
        this.lucro = 0.0;
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

    public List<Menu> getMenus() {
        return menus;
    }

    public double getTotalCusto() {
        return totalCusto;
    }

    public double getTotalVenda() {
        return totalVenda;
    }

    public double getLucro() {
        return lucro;
    }

    public void adicionarPrato(Prato prato) {
        this.pratos.add(prato);
        this.totalCusto += prato.getPrecoCusto();
        this.totalVenda += prato.getPrecoVenda();
        this.lucro = this.totalVenda - this.totalCusto;
    }

    public void adicionarMenu(Menu menu) {
        this.menus.add(menu);
        for (Prato prato : menu.getPratos()) {
            if (prato != null) {
                this.totalCusto += prato.getPrecoCusto();
                this.totalVenda += prato.getPrecoVenda();
            }
        }
        this.lucro = this.totalVenda - this.totalCusto;
    }
}