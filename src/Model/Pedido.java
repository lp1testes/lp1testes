package Model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private Integer id;
    private Mesa mesa;
    private List<Prato> pratos;
    private List<Menu> menus;
    private double totalCusto;
    private double precoTotal;
    private double lucro;

    public Pedido() {
        this.pratos = new ArrayList<>();
        this.menus = new ArrayList<>();
    }

    public Pedido(Integer id, Mesa mesa, List<Prato> pratos, List<Menu> menus, double totalCusto, double lucro, double precoTotal) {
        this.id = id;
        this.mesa = mesa;
        this.pratos = pratos;
        this.menus = menus;
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
        calcularTotais();
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
        calcularTotais();
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

    public void adicionarPrato(Prato prato) {
        this.pratos.add(prato);
        calcularTotais();
    }

    public void adicionarMenu(Menu menu) {
        this.menus.add(menu);
        calcularTotais();
    }

    private void calcularTotais() {
        this.totalCusto = 0;
        this.precoTotal = 0;
        for (Prato prato : pratos) {
            if (prato != null) {
                this.totalCusto += prato.getPrecoCusto();
                this.precoTotal += prato.getPrecoVenda();
            }
        }
        for (Menu menu : menus) {
            for (Prato prato : menu.getPratos()) {
                if (prato != null) {
                    this.totalCusto += prato.getPrecoCusto();
                    this.precoTotal += prato.getPrecoVenda();
                }
            }
        }
        this.lucro = this.precoTotal - this.totalCusto;
    }
}