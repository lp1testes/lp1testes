package View;

import Controller.LogsController;
import Controller.MenuController;
import Controller.PratoController;
import Controller.SimulacaoDiaController;

import java.util.Scanner;

public class GerirMenusView {
    private PratoController pratoController;
    private MenuController menuController;
    private LogsController logsController;
    private SimulacaoDiaController simulacaoDiaController;

    public GerirMenusView() {
        pratoController = new PratoController();
        menuController = new MenuController();
        logsController = LogsController.getInstance();
        simulacaoDiaController = SimulacaoDiaController.getInstance();
    }

    public void exibirMenuGestaoMenus(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Gestão de Menus --");
            System.out.println("1. Criar Prato");
            System.out.println("2. Editar Prato");
            System.out.println("3. Remover Prato");
            System.out.println("4. Listar Pratos");
            System.out.println("5. Criar Menu");
            System.out.println("6. Listar Menus");
            System.out.println("7. Editar Prato no Menu");
            System.out.println("8. Remover Prato do Menu");
            System.out.println("9. Adicionar Prato ao Menu");
            System.out.println("10. Salvar Pratos e Menus");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 10);

            switch (opcao) {
                case 1:
                    criarPrato(scanner);
                    break;
                case 2:
                    editarPrato(scanner);
                    break;
                case 3:
                    removerPrato(scanner);
                    break;
                case 4:
                    listarPratos();
                    break;
                case 5:
                    criarMenu(scanner);
                    break;
                case 6:
                    listarMenus();
                    break;
                case 7:
                    editarPratoNoMenu(scanner);
                    break;
                case 8:
                    removerPratoDoMenu(scanner);
                    break;
                case 9:
                    adicionarPratoAoMenu(scanner);
                    break;
                case 10:
                    salvarPratosEMenus(); // Chamada para o método de salvar pratos e menus
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void criarPrato(Scanner scanner) {

        System.out.print("Nome do prato: ");
        String nome = scanner.nextLine();

        boolean pratoCriado = pratoController.criarPratoGerirMenusView(nome, scanner);

        if(pratoCriado){
            System.out.println("Prato criado com sucesso!");
        }
        else{
            System.out.println("Erro ao criar prato!");
        }
    }

    private void editarPrato(Scanner scanner) {

        boolean pratoEditado = pratoController.editarPratoGerirMenusView(scanner);

        if(pratoEditado){
            System.out.println("Prato editado com sucesso!");
        }
        else{
            System.out.println("Falha ao editar prato!");
        }
    }

    private void removerPrato(Scanner scanner) {

        boolean pratoRemovido = pratoController.removerPratoGerirMenusView(scanner);

        if(pratoRemovido){
            System.out.println("Prato removido com sucesso!");
        }
        else{
            System.out.println("Erro ao remover prato!");
        }
    }

    private void listarPratos() {
        pratoController.listarPratosGerirMenusView();
    }

    private void criarMenu(Scanner scanner) {
        System.out.println("\nListando entradas:");
        pratoController.listarPratosPorCategoria("entrada");
        System.out.print("Selecione o id do prato (ou deixe em branco): ");
        String entradaId = scanner.nextLine();

        System.out.println("\nListando pratos principais:");
        pratoController.listarPratosPorCategoria("prato principal");
        System.out.print("Selecione o id do prato (ou deixe em branco): ");
        String pratoPrincipalId = scanner.nextLine();

        System.out.println("\nListando sobremesas:");
        pratoController.listarPratosPorCategoria("sobremesa");
        System.out.print("Selecione o id do prato (ou deixe em branco): ");
        String sobremesaId = scanner.nextLine();

        menuController.criarMenu(entradaId, pratoPrincipalId, sobremesaId);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Menu criado com entrada ID: %s, prato principal ID: %s, sobremesa ID: %s",
                entradaId, pratoPrincipalId, sobremesaId);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Menu criado com sucesso!");
    }

    private void listarMenus() {
        String menus = menuController.listarMenus();
        System.out.println(menus);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "INFO";
        String logDescription = "Listagem de menus exibida";

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }

    private void editarPratoNoMenu(Scanner scanner) {

        listarMenus();

        boolean pratoEditadoNoMenu = pratoController.editarPratoNoMenuGerirMenusView(scanner);

        if(pratoEditadoNoMenu) {
            System.out.println("Prato editado com sucesso no menu!");
        }
        else {
            System.out.println("Prato não encontrado.");
        }
    }

    private void removerPratoDoMenu(Scanner scanner) {
        listarMenus();

        System.out.print("\nID do menu: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("ID do prato a remover: ");
        int pratoId = scanner.nextInt();
        scanner.nextLine();

        menuController.removerPratoDoMenu(menuId, pratoId);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato removido do menu ID: %d, prato ID: %d", menuId, pratoId);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Prato removido com sucesso do menu!");
    }

    private void adicionarPratoAoMenu(Scanner scanner) {
        listarMenus();

        System.out.print("\nID do menu: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        listarPratos();
        System.out.print("Selecione o id do prato a adicionar: ");
        String pratoId = scanner.nextLine();

        menuController.adicionarPratoAoMenu(menuId, pratoId);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato adicionado ao menu ID: %d, prato ID: %s", menuId, pratoId);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Prato adicionado com sucesso ao menu!");
    }
    private void salvarPratosEMenus() {
        pratoController.salvarPratos();
        menuController.salvarMenus();
        System.out.println("Pratos e menus salvos com sucesso!");
    }
}