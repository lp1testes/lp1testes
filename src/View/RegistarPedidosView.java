package View;

import Controller.*;
import Model.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RegistarPedidosView {

    private MesaController mesaController;
    private ReservaController reservaController;
    private SimulacaoDiaController simulacaoDiaController;
    private ConfiguracaoController configuracaoController;
    private PratoController pratoController;
    private MenuController menuController;
    private LogsController logsController;

    public RegistarPedidosView(MesaController mesaController, ReservaController reservaController, SimulacaoDiaController simulacaoDiaController, ConfiguracaoController configuracaoController, PratoController pratoController, MenuController menuController) {
        this.mesaController = mesaController;
        this.reservaController = reservaController;
        this.simulacaoDiaController = simulacaoDiaController;
        this.configuracaoController = configuracaoController;
        this.pratoController = pratoController;
        this.menuController = menuController;
        this.logsController = LogsController.getInstance();
    }

    public void exibirMenuRegistoPedidos(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Registo de Pedidos --");
            System.out.println("1. Associar Pedido a uma Mesa");
            System.out.println("2. Gerir Pedidos");
            System.out.println("3. Listar Pedidos Atendidos de uma Mesa");
            System.out.println("4. Efetuar Pagamento");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 4);

            switch (opcao) {
                case 1:
                    associarPedido(scanner);
                    break;
                case 2:
                    gerirPedidos();
                    break;
                case 3:
                    listarPedidosAtendidos(scanner);
                    break;
                case 4:
                    efetuarPagamentoView(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void efetuarPagamentoView(Scanner scanner) {
        System.out.print("Digite o ID da mesa para efetuar o pagamento: ");
        int idMesa = -1;
        try {
            idMesa = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
            scanner.nextLine(); // Limpar o buffer
            return;
        }

        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();

        if (!mesaController.podeEfetuarPagamento(idMesa, tempoAtual)) {
            System.out.println("O pagamento só pode ser efetuado quando o prato com o maior tempo de preparo tiver sido consumido.");
            return;
        }

        mesaController.efetuarPagamento(idMesa, tempoAtual);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Obter o pedido associado à mesa
        Pedido pedido = mesaController.getPedidoByMesa(idMesa);
        if (pedido == null) {
            System.out.println("Pedido não encontrado para a mesa " + idMesa);
            return;
        }

        // Obter a reserva associada ao pedido
        Reserva reserva = mesaController.getClienteDaMesa(idMesa);
        if (reserva == null) {
            System.out.println("Reserva não encontrada para a mesa " + idMesa);
            return;
        }

        // Criar a lista de pratos consumidos
        StringBuilder pratosConsumidos = new StringBuilder();
        for (Prato prato : pedido.getPratos()) {
            if (prato != null) {
                pratosConsumidos.append(prato.getNome()).append(", ");
            }
        }

        // Adicionar pratos dos menus ao log
        for (Menu menu : pedido.getMenus()) {
            if (menu != null) { // Verificação nula adicionada
                for (Prato prato : menu.getPratos()) {
                    if (prato != null) {
                        pratosConsumidos.append(prato.getNome()).append(", ");
                    }
                }
            }
        }

        // Remover a última vírgula e espaço, se houver
        if (pratosConsumidos.length() > 0) {
            pratosConsumidos.setLength(pratosConsumidos.length() - 2);
        }

        // Criar o log com o número de pessoas
        String logType = "ACTION";
        String logDescription = String.format("Pagamento efetuado para a mesa ID: %d. Número de Pessoas: %d, Pratos consumidos: %s",
                idMesa, reserva.getNumeroPessoas(), pratosConsumidos.toString());

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }
    private void associarPedido(Scanner scanner) {
        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();
        int unidadesTempoParaPedido = configuracaoController.getConfiguracao().getUnidadesTempoParaPedido();

        Mesa[] mesasOcupadas = mesaController.listarMesasOcupadasComReservasNaoAtendidas(tempoAtual, unidadesTempoParaPedido);
        if (mesasOcupadas.length == 0) {
            System.out.println("Não há mesas ocupadas com reservas não atendidas no momento.");
            return;
        }

        System.out.println("\n-- Mesas Ocupadas com Reservas Não Atendidas --");
        for (Mesa mesa : mesasOcupadas) {
            if (mesa != null) {
                Reserva reserva = mesaController.getClienteDaMesa(mesa.getId());
                System.out.println("Mesa " + mesa.getId() + " - Reserva: " + reserva.getNome() + " (ID da Reserva: " + reserva.getId() + ")");
            }
        }

        int idMesa = -1;
        while (idMesa == -1) {
            System.out.print("\nID da mesa para registrar o pedido: ");
            try {
                idMesa = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine();
            }
        }
        scanner.nextLine();

        Reserva reserva = mesaController.getClienteDaMesa(idMesa);
        if (reserva == null) {
            System.out.println("Nenhuma reserva encontrada para a mesa " + idMesa);
            return;
        }

        int tempoAssociacao = reserva.getTempoChegada();
        int tempoLimite = tempoAssociacao + unidadesTempoParaPedido;

        if (tempoAtual < tempoAssociacao + 1) {
            System.out.println("O cliente terá que esperar uma unidade de tempo para fazer o pedido.");
            return;
        }

        if (tempoAtual > tempoLimite) {
            System.out.println("Tempo limite para registrar o pedido expirou. Clientes da reserva " + reserva.getNome() + " foram embora.");
            mesaController.removerReservaDaMesa(idMesa);
            return;
        }

        System.out.println("Clientes da reserva " + reserva.getNome() + " estão prontos para fazer o pedido.");
        mesaController.registrarPedido(idMesa, tempoAtual, scanner);

        mesaController.marcarReservaComoAtendida(idMesa);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Pedido associado à mesa ID: %d, Reserva: %s (ID da Reserva: %d)", idMesa, reserva.getNome(), reserva.getId());

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }

    private void listarPratos(Scanner scanner, int idMesa, int clienteIndex) {
        Prato[] pratos = pratoController.getPratos();
        if (pratos.length == 0) {
            System.out.println("Não há pratos disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Pratos Disponíveis --");
        for (Prato prato : pratos) {
            if (prato != null && prato.isDisponivel()) {
                System.out.println("ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
            }
        }

        int idPrato = -1;
        while (idPrato == -1) {
            System.out.print("Escolha o ID do prato: ");
            try {
                idPrato = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }

        Prato pratoSelecionado = pratoController.getPratoById(idPrato);
        if (pratoSelecionado != null && pratoSelecionado.isDisponivel()) {
            mesaController.adicionarPratoAoPedido(idMesa, pratoSelecionado);
            System.out.printf("Prato %s adicionado ao pedido do Cliente %d.%n", pratoSelecionado.getNome(), clienteIndex + 1);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato %s (ID: %d) adicionado ao pedido da mesa ID: %d", pratoSelecionado.getNome(), pratoSelecionado.getId(), idMesa);

            logsController.criarLog(currentDay, currentHour, logType, logDescription);
        } else {
            System.out.println("Prato inválido ou não disponível.");
        }
    }

    private void listarMenus(Scanner scanner, int idMesa, int clienteIndex) {
        Menu[] menus = menuController.obterTodosMenus();
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

        Menu menuSelecionado = menuController.getMenuById(idMenu);
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

    private void listarPedidosAtendidos(Scanner scanner) {
        int idMesa = -1;
        while (idMesa == -1) {
            System.out.print("Digite o ID da mesa para listar os pedidos atendidos: ");
            try {
                idMesa = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine();
            }
        }

        mesaController.listarPedidosAtendidos(idMesa);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "INFO";
        String logDescription = String.format("Pedidos atendidos listados para a mesa ID: %d", idMesa);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }

    private void gerirPedidos() {
        // Implementar a funcionalidade de gerir pedidos
    }
}