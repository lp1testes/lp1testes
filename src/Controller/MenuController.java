package Controller;

import DAL.MenuDAL;
import Model.Menu;
import Model.Prato;

public class MenuController {

    private Menu menu;
    private MenuDAL menuDAL;
    private PratoController pratoController;

    public MenuController() {
        menu = new Menu();
        pratoController = new PratoController();
        menuDAL = new MenuDAL();
        //menu = menuDAL.carregarMenus();
    }

    public Menu getMenu() {
        return menu;
    }

    public void atualizarMenu(Menu novoMenu) {
        menu = novoMenu;
        //menuDAL.salvarMenus(menu);
    }

    public void criarMenu(String entradaId, String pratoPrincipalId, String sobremesaId) {
        menu = new Menu(); // Resetar o menu para evitar duplicação

        if (!entradaId.isEmpty()) {
            Prato entrada = pratoController.getPratoById(Integer.parseInt(entradaId));
            if (entrada != null) {
                menu.adicionarPrato(entrada);
            }
        }

        if (!pratoPrincipalId.isEmpty()) {
            Prato pratoPrincipal = pratoController.getPratoById(Integer.parseInt(pratoPrincipalId));
            if (pratoPrincipal != null) {
                menu.adicionarPrato(pratoPrincipal);
            }
        }

        if (!sobremesaId.isEmpty()) {
            Prato sobremesa = pratoController.getPratoById(Integer.parseInt(sobremesaId));
            if (sobremesa != null) {
                menu.adicionarPrato(sobremesa);
            }
        }

        Menu[] menus = menuDAL.carregarTodosMenus();
        int proximoId = obterProximoId(menus);
        menu.setId(proximoId);

        for (int i = 0; i < menus.length; i++) {
            if (menus[i] == null) {
                menus[i] = menu;
                break;
            }
        }
        menuDAL.salvarMenus(menus);
    }

    private int obterProximoId(Menu[] menus) {
        int maxId = 0;
        for (Menu menu : menus) {
            if (menu != null && menu.getId() > maxId) {
                maxId = menu.getId();
            }
        }
        return maxId + 1;
    }

    public Menu[] obterTodosMenus() {
        return menuDAL.carregarTodosMenus();
    }

    public String listarMenus() {
        Menu[] menus = obterTodosMenus();
        StringBuilder sb = new StringBuilder();
        sb.append("\n-- Lista de Menus --\n");
        for (Menu menu : menus) {
            if (menu != null) {
                sb.append("Menu ID: ").append(menu.getId()).append("\n");
                for (Prato prato : menu.getPratos()) {
                    if (prato != null) {
                        sb.append("ID: ").append(prato.getId())
                                .append(", Nome: ").append(prato.getNome())
                                .append(", Categoria: ").append(prato.getCategoria())
                                .append(", Preço de Custo: ").append(prato.getPrecoCusto())
                                .append(", Preço de Venda: ").append(prato.getPrecoVenda())
                                .append(", Tempo de Preparação: ").append(prato.getTempoPreparacao())
                                .append(", Disponível: ").append(prato.isDisponivel());
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void editarPratoNoMenu(int menuId, int pratoId, String novoPratoId) {
        Menu[] menus = obterTodosMenus();
        for (Menu menu : menus) {
            if (menu != null && menu.getId() == menuId) {
                Prato[] pratos = menu.getPratos();
                for (int i = 0; i < pratos.length; i++) {
                    if (pratos[i] != null && pratos[i].getId() == pratoId) {
                        Prato novoPrato = pratoController.getPratoById(Integer.parseInt(novoPratoId));
                        if (novoPrato != null && novoPrato.getCategoria().equals(pratos[i].getCategoria())) {
                            pratos[i] = novoPrato;
                            menuDAL.salvarMenus(menus);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void removerPratoDoMenu(int menuId, int pratoId) {
        Menu[] menus = obterTodosMenus();
        for (Menu menu : menus) {
            if (menu != null && menu.getId() == menuId) {
                Prato[] pratos = menu.getPratos();
                for (int i = 0; i < pratos.length; i++) {
                    if (pratos[i] != null && pratos[i].getId() == pratoId) {
                        pratos[i] = null;
                        menuDAL.salvarMenus(menus);
                        return;
                    }
                }
            }
        }
    }

    public void adicionarPratoAoMenu(int menuId, String pratoId) {
        Menu[] menus = obterTodosMenus();
        for (Menu menu : menus) {
            if (menu != null && menu.getId() == menuId) {
                Prato novoPrato = pratoController.getPratoById(Integer.parseInt(pratoId));
                if (novoPrato != null) {
                    menu.adicionarPrato(novoPrato);
                    menuDAL.salvarMenus(menus);
                    return;
                }
            }
        }
    }
}