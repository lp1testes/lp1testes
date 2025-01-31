package Model;

public class Menu {

    private Integer id;
    private Prato[] pratos;
    private static final int MAX_PRATOS = 100;
    private int pratoCount;

    public Menu() {
        this.pratos = new Prato[MAX_PRATOS];
        this.pratoCount = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Prato[] getPratos() {
        return pratos;
    }

    public void setPratos(Prato[] pratos) {
        this.pratos = pratos;
    }

    public void adicionarPrato(Prato prato) {
        if (pratoCount < MAX_PRATOS) {
            this.pratos[pratoCount++] = prato;
        }
    }

    public int getPratoCount() {
        return pratoCount;
    }

    // Método para verificar se o menu está vazio
    public boolean isVazio() {
        return pratoCount == 0;
    }
}