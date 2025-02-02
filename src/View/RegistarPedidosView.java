package View;

import Controller.*;

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
    private PedidoController pedidoController;

    public RegistarPedidosView(MesaController mesaController, ReservaController reservaController, SimulacaoDiaController simulacaoDiaController, ConfiguracaoController configuracaoController, PratoController pratoController, MenuController menuController) {
        this.mesaController = mesaController;
        this.reservaController = reservaController;
        this.simulacaoDiaController = simulacaoDiaController;
        this.configuracaoController = configuracaoController;
        this.pratoController = pratoController;
        this.menuController = menuController;
        this.logsController = LogsController.getInstance();
        this.pedidoController = new PedidoController();
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

        String efetuarPagamentoRetorno = pedidoController.efetuarPagamentoView(scanner);

        System.out.println(efetuarPagamentoRetorno);
    }

    private void associarPedido(Scanner scanner) {

        String retornoAssociacaoPedido = mesaController.associarPedido(scanner);

        System.out.println(retornoAssociacaoPedido);
    }

    private void listarPratos(Scanner scanner, int idMesa, int clienteIndex) {
        pratoController.listarPratosRegistarPedidosView(scanner,idMesa,clienteIndex);
    }

    private void listarMenus(Scanner scanner, int idMesa, int clienteIndex) {
        menuController.listarMenus(scanner,idMesa,clienteIndex);
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