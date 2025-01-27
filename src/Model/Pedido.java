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
    private boolean pago;
    private int tempoPedido;

    public Pedido() {
        this.pratos = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.totalCusto = 0.0;
        this.totalVenda = 0.0;
        this.lucro = 0.0;
        this.pago = false; // Inicialmente o pedido não está pago
        this.tempoPedido = -1; // Inicializa com um valor inválido
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
    public int getTempoPedido() {
        return tempoPedido;
    }

    public void setTempoPedido(int tempoPedido) {
        this.tempoPedido = tempoPedido;
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

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
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