package View;

import Controller.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GerirMesasView {

    private MesaController mesaController;
    private ReservaController reservaController;
    private SimulacaoDiaController simulacaoDiaController;
    private ConfiguracaoController configuracaoController;
    private LogsController logsController;

    public GerirMesasView(MesaController mesaController, ReservaController reservaController, SimulacaoDiaController simulacaoDiaController, ConfiguracaoController configuracaoController) {
        this.mesaController = mesaController;
        this.reservaController = reservaController;
        this.simulacaoDiaController = simulacaoDiaController;
        this.configuracaoController = configuracaoController;
        this.logsController = LogsController.getInstance(); // Usando a instância Singleton
    }

    public void exibirMenuGestaoMesas(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Gestão de Mesas --");
            System.out.println("1. Registar Mesas");
            System.out.println("2. Verificar Estado das Mesas");
            System.out.println("3. Atribuir Clientes a Mesas");
            System.out.println("4. Ver Cliente da Mesa");
            System.out.println("5. Editar Mesa");
            System.out.println("6. Remover Mesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 6);

            switch (opcao) {
                case 1:
                    registarMesas(scanner);
                    break;
                case 2:
                    verificarEstadoMesas();
                    break;
                case 3:
                    atribuirClientesMesas(scanner);
                    break;
                case 4:
                    verClienteDaMesa(scanner);
                    break;
                case 5:
                    editarMesa(scanner);
                    break;
                case 6:
                    removerMesa(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void registarMesas(Scanner scanner) {

        boolean retornoRegistoComSucesso = mesaController.registarMesas(scanner);

        if(retornoRegistoComSucesso){
            System.out.println("Mesa registrada com sucesso!");
        }
        else{
            System.out.println("Adição de mesa cancelada.");
        }
    }

    private void verificarEstadoMesas() {
        mesaController.verificarEstadoMesas();
    }

    public void atribuirClientesMesas(Scanner scanner) {

        verificarEstadoMesas();

        String retornoReserva = reservaController.atribuirClientesMesas(scanner);

        System.out.println(retornoReserva);
    }

    private void verClienteDaMesa(Scanner scanner) {
        reservaController.verClienteDaMesa(scanner);
    }

    private void editarMesa(Scanner scanner) {
        verificarEstadoMesas();

        int id = -1;
        while (id == -1) {
            System.out.print("\nID da mesa a editar: ");
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        int novaCapacidade = -1;
        while (novaCapacidade == -1) {
            System.out.print("Nova capacidade da mesa: ");
            try {
                novaCapacidade = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        Boolean novaOcupacao = null;
        while (novaOcupacao == null) {
            System.out.print("A mesa está ocupada? (true/false): ");
            try {
                novaOcupacao = scanner.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira 'true' ou 'false'.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        mesaController.editarMesa(id, novaCapacidade, novaOcupacao);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Mesa editada: ID %d, Nova Capacidade %d, Nova Ocupação %b", id, novaCapacidade, novaOcupacao);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Mesa editada com sucesso!");
    }

    private void removerMesa(Scanner scanner) {
        verificarEstadoMesas();

        int id = -1;
        while (id == -1) {
            System.out.print("\nID da mesa a remover: ");
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        mesaController.removerMesa(id);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Mesa removida: ID %d", id);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        System.out.println("Mesa removida com sucesso!");
    }
}