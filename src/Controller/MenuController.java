package Controller;

import DAL.MenuDAL;
import Model.Menu;
import Model.Prato;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuController {
    private static MenuController instance;
    private Menu menu;
    private MenuDAL menuDAL;
    private static final PratoController pratoController = PratoController.getInstance();
    private static final MesaController mesaController = MesaController.getInstance();
    private static final SimulacaoDiaController simulacaoDiaController = SimulacaoDiaController.getInstance();
    private static final LogsController logsController = LogsController.getInstance();

    public MenuController() {
        menu                    = new Menu();
        //pratoController         = new PratoController();
        menuDAL                 = new MenuDAL();
        //mesaController          = new MesaController();
        //simulacaoDiaController  = new SimulacaoDiaController();
        //logsController          = new LogsController();
        //menu = menuDAL.carregarMenus();
    }

    public static synchronized MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
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

    public Menu getMenuById(int id) {
        Menu[] menus = obterTodosMenus();
        for (Menu menu : menus) {
            if (menu != null && menu.getId() == id) {
                return menu;
            }
        }
        return null;
    }

    public void listarMenus(Scanner scanner, int idMesa, int clienteIndex){

        Menu[] menus = obterTodosMenus();

        if (menus.length == 0) {
            System.out.println("Não há menus disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Menus Disponíveis --");
        for (Menu menu : menus) {
            if (menu != null) {
                System.out.println("ID: " + menu.getId() + " - Pratos:");
                for (Prato prato : menu.getPratos()) {
                    if (prato != null && prato.isDisponivel()) {
                        System.out.println("   ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
                    }
                }
            }
        }

        int idMenu = -1;
        while (idMenu == -1) {
            System.out.print("Escolha o ID do menu: ");
            try {
                idMenu = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }

        Menu menuSelecionado = getMenuById(idMenu);
        if (menuSelecionado != null) {
            mesaController.adicionarMenuAoPedido(idMesa, menuSelecionado);
            System.out.printf("Menu %d adicionado ao pedido do Cliente %d.%n", menuSelecionado.getId(), clienteIndex + 1);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Menu %d adicionado ao pedido da mesa ID: %d", menuSelecionado.getId(), idMesa);

            logsController.criarLog(currentDay, currentHour, logType, logDescription);
        } else {
            System.out.println("Menu inválido.");
        }
    }
}