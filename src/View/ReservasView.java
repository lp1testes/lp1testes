package View;

import Controller.ConfiguracaoController;
import Controller.ReservaController;
import Controller.SimulacaoDiaController;
import Controller.LogsController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReservasView {

    private ReservaController reservaController;
    private GerirMesasView gerirMesasView;
    private SimulacaoDiaController simulacaoDiaController;
    private LogsController logsController;

    public ReservasView(ReservaController reservaController, GerirMesasView gerirMesasView, SimulacaoDiaController simulacaoDiaController, LogsController logsController) {
        this.reservaController = reservaController;
        this.gerirMesasView = gerirMesasView;
        this.simulacaoDiaController = simulacaoDiaController;
        this.logsController = LogsController.getInstance();
    }

    public void exibirMenuReservas(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Reservas --");
            System.out.println("1. Mostrar Reservas");
            System.out.println("2. Adicionar Reserva");
            System.out.println("3. Remover Reserva");
            System.out.println("4. Editar Reserva");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 4);

            switch (opcao) {
                case 1:
                    mostrarReservas();
                    break;
                case 2:
                    adicionarReserva(scanner);
                    break;
                case 3:
                    removerReserva(scanner);
                    break;
                case 4:
                    editarReserva(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void adicionarReserva(Scanner scanner) {
        // Verificar se o dia já começou
        if (!simulacaoDiaController.diaJaComecou()) {
            System.out.println("O dia ainda não começou. Por favor, inicie o dia antes de adicionar uma reserva.");
            return;
        }

        System.out.print("Digite o nome da reserva: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o número de pessoas: ");
        int numeroPessoas = -1;
        try {
            numeroPessoas = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
            scanner.nextLine(); // Limpar o buffer
            return;
        }

        // Obtendo o tempo atual do SimulacaoDiaController
        int tempoChegada = simulacaoDiaController.getUnidadeTempoAtual();

        int unidadesTempoDia = ConfiguracaoController.getInstancia().getUnidadesTempoDia();
        if (tempoChegada > unidadesTempoDia) {
            System.out.println("O tempo de chegada não pode ser maior que " + unidadesTempoDia);
            return;
        }

        reservaController.criarReserva(nome, numeroPessoas, tempoChegada);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Reserva criada: Nome: %s, Número de Pessoas: %d, Tempo de Chegada: %d", nome, numeroPessoas, tempoChegada);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.print("Deseja associar esta reserva a uma mesa agora? (sim/não): ");
        String associar = scanner.nextLine().trim().toLowerCase();
        if (associar.equals("sim")) {
            gerirMesasView.atribuirClientesMesas(scanner);
        }
    }

    private void mostrarReservas() {
        String reservas = reservaController.listarReservas();
        System.out.println(reservas);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "INFO";
        String logDescription = "Listagem de reservas exibida";

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }

    private void removerReserva(Scanner scanner) {
        System.out.print("Digite o ID da reserva a ser removida: ");
        int idReserva = -1;
        try {
            idReserva = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
            scanner.nextLine(); // Limpar o buffer
            return;
        }

        reservaController.removerReserva(idReserva);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Reserva removida: ID %d", idReserva);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Reserva removida com sucesso!");
    }

    private void editarReserva(Scanner scanner) {
        System.out.print("Digite o ID da reserva a ser editada: ");
        int idReserva = -1;
        try {
            idReserva = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
            scanner.nextLine(); // Limpar o buffer
            return;
        }

        System.out.print("Digite o novo nome da reserva: ");
        String novoNome = scanner.nextLine();

        System.out.print("Digite o novo número de pessoas: ");
        int novoNumeroPessoas = -1;
        try {
            novoNumeroPessoas = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
            scanner.nextLine(); // Limpar o buffer
            return;
        }

        reservaController.editarReserva(idReserva, novoNome, novoNumeroPessoas);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Reserva editada: ID %d, Novo Nome: %s, Novo Número de Pessoas: %d", idReserva, novoNome, novoNumeroPessoas);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Reserva editada com sucesso!");
    }
}