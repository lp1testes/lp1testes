package View;

import Controller.LogsController;
import Controller.MenuController;
import Controller.PratoController;
import Controller.SimulacaoDiaController;
import Model.Prato;
import java.util.Scanner;

public class GerirMenusView {
    private PratoController pratoController;
    private MenuController menuController;
    private LogsController logsController;
    private SimulacaoDiaController simulacaoDiaController;

    public GerirMenusView() {
        pratoController = new PratoController();
        menuController = new MenuController();
        logsController = new LogsController();
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
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 9);

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

        String categoria = pratoController.validarCategoria(scanner);
        double precoCusto = pratoController.validarPreco(scanner, "Preço de custo");
        double precoVenda = pratoController.validarPreco(scanner, "Preço de venda");

        int tempoPreparacao = pratoController.validarTempoPreparacao(scanner);

        boolean disponivel = pratoController.validarDisponibilidade(scanner);

        Prato novoPrato = new Prato(null, nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);
        pratoController.adicionarPrato(novoPrato);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato criado: %s, Categoria: %s, Custo: %.2f, Venda: %.2f, Tempo: %d, Disponível: %b",
                nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Prato criado com sucesso!");
    }

    private void editarPrato(Scanner scanner) {
        Prato[] pratos = pratoController.getPratos();
        System.out.println("\n-- Lista de Pratos --");
        for (Prato prato : pratos) {
            if (prato != null) {
                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        Prato pratoParaEditar = pratoController.validarPratoParaEditar(scanner);
        if (pratoParaEditar != null) {
            pratoController.editarPrato(scanner, pratoParaEditar);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato editado: %s, Categoria: %s, Custo: %.2f, Venda: %.2f, Tempo: %d, Disponível: %b",
                    pratoParaEditar.getNome(), pratoParaEditar.getCategoria(), pratoParaEditar.getPrecoCusto(), pratoParaEditar.getPrecoVenda(), pratoParaEditar.getTempoPreparacao(), pratoParaEditar.isDisponivel());

            logsController.criarLog(currentDay, currentHour, logType, logDescription);

            System.out.println("Prato editado com sucesso!");
        }
    }

    private void removerPrato(Scanner scanner) {
        Prato[] pratos = pratoController.getPratos();
        System.out.println("\n-- Lista de Pratos --");
        for (Prato prato : pratos) {
            if (prato != null) {
                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        System.out.print("ID do prato a remover: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        pratoController.removerPrato(id);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato removido: ID %d", id);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Prato removido com sucesso!");
    }

    private void listarPratos() {
        Prato[] pratos = pratoController.getPratos();
        System.out.println("\n-- Lista de Pratos --");
        for (Prato prato : pratos) {
            if (prato != null) {
                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "INFO";
        String logDescription = "Listagem de pratos exibida";

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
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

        System.out.print("\nID do menu: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("ID do prato a editar: ");
        int pratoId = scanner.nextInt();
        scanner.nextLine();

        Prato prato = pratoController.getPratoById(pratoId);
        if (prato != null) {
            System.out.println("\nListando pratos da categoria " + prato.getCategoria() + ":");
            pratoController.listarPratosPorCategoria(prato.getCategoria());
            System.out.print("Selecione o id do novo prato: ");
            String novoPratoId = scanner.nextLine();

            menuController.editarPratoNoMenu(menuId, pratoId, novoPratoId);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato editado no menu ID: %d, prato antigo ID: %d, novo prato ID: %s",
                    menuId, pratoId, novoPratoId);

            logsController.criarLog(currentDay, currentHour, logType, logDescription);

            System.out.println("Prato editado com sucesso no menu!");
        } else {
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
}