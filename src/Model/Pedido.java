package Model;

public class Pedido {

    private Mesa mesa;
    private Prato[] pratos;
    private Menu[] menus;
    private double totalCusto;
    private double totalVenda;
    private double lucro;
    private boolean pago;
    private int tempoPedido;
    private int pratoCount;
    private int menuCount;
    private static final int LIMITE = 100;

    public Pedido() {
        this.pratos = new Prato[LIMITE]; // Tamanho máximo definido em 100
        this.menus = new Menu[LIMITE]; // Tamanho máximo definido em 100
        this.totalCusto = 0.0;
        this.totalVenda = 0.0;
        this.lucro = 0.0;
        this.pago = false; // Inicialmente o pedido não está pago
        this.tempoPedido = -1; // Inicializa com um valor inválido
        this.pratoCount = 0;
        this.menuCount = 0;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Prato[] getPratos() {
        return pratos;
    }

    public int getTempoPedido() {
        return tempoPedido;
    }

    public void setTempoPedido(int tempoPedido) {
        this.tempoPedido = tempoPedido;
    }

    public Menu[] getMenus() {
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
        if (pratoCount < LIMITE) {
            pratos[pratoCount++] = prato;
            this.totalCusto += prato.getPrecoCusto();
            this.totalVenda += prato.getPrecoVenda();
            this.lucro = this.totalVenda - this.totalCusto;
        } else {
            System.out.println("Limite de pratos atingido!");
        }
    }

    public void adicionarMenu(Menu menu) {
        if (menuCount < LIMITE) {
            menus[menuCount++] = menu;
            for (Prato prato : menu.getPratos()) {
                if (prato != null) {
                    this.totalCusto += prato.getPrecoCusto();
                    this.totalVenda += prato.getPrecoVenda();
                }
            }
            this.lucro = this.totalVenda - this.totalCusto;
        } else {
            System.out.println("Limite de menus atingido!");
        }
    }

    // Método para verificar se a coleção de pratos está vazia
    public boolean isPratosVazio() {
        return pratoCount == 0;
    }

    // Método para verificar se a coleção de menus está vazia
    public boolean isMenusVazio() {
        return menuCount == 0;
    }
}