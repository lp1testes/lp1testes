package View;

import Controller.DesempenhoFinanceiroController;
import Controller.LogsController;
import Model.Logs;

import java.util.Scanner;

public class DesempenhoFinanceiroView {
    private DesempenhoFinanceiroController desempenhoFinanceiroController;
    private LogsController logsController;

    public DesempenhoFinanceiroView(DesempenhoFinanceiroController desempenhoFinanceiroController) {
        this.desempenhoFinanceiroController = desempenhoFinanceiroController;
        this.logsController = LogsController.getInstance(); // Usando a instância Singleton
    }

    public void exibirMenuDesempenhoFinanceiro(Scanner scanner) {
        int opcao;

        do {
            listarTodosLogs(); // Lista todos os logs antes de exibir o menu
            System.out.println("\n-- Gestão Financeira --");
            System.out.println("1. Exibir Total Faturado");
            System.out.println("2. Exibir Total de Gastos");
            System.out.println("3. Exibir Total de Lucro");
            System.out.println("4. Exibir Número de Pedidos Atendidos");
            System.out.println("5. Exibir Número de Pedidos Não Atendidos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

            switch (opcao) {
                case 1:
                    exibirTotalFaturado(scanner);
                    break;
                case 2:
                    exibirTotalGastos(scanner);
                    break;
                case 3:
                    exibirTotalLucro(scanner);
                    break;
                case 4:
                    exibirNumeroPedidosAtendidos(scanner);
                    break;
                case 5:
                    exibirNumeroPedidosNaoAtendidos(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarTodosLogs() {
        Logs[] logs = logsController.obterTodosLogs();
        System.out.println("\n-- Logs em Memória --");
        for (Logs log : logs) {
            System.out.println(log.getDay() + "/" + log.getHour() + " - " + log.getLogType() + ": " + log.getLogDescription());
        }
    }

    private void exibirTotalFaturado(Scanner scanner) {
        String periodo = obterPeriodo(scanner);
        double totalFaturado = desempenhoFinanceiroController.calcularTotalFaturado(periodo);
        System.out.printf("Total Faturado no período %s: %.2f\n", periodo, totalFaturado);
    }

    private void exibirTotalGastos(Scanner scanner) {
        String periodo = obterPeriodo(scanner);
        double totalGastos = desempenhoFinanceiroController.calcularTotalGastos(periodo);
        System.out.printf("Total de Gastos no período %s: %.2f\n", periodo, totalGastos);
    }

    private void exibirTotalLucro(Scanner scanner) {
        String periodo = obterPeriodo(scanner);
        double totalLucro = desempenhoFinanceiroController.calcularTotalLucro(periodo);
        System.out.printf("Total de Lucro no período %s: %.2f\n", periodo, totalLucro);
    }

    private void exibirNumeroPedidosAtendidos(Scanner scanner) {
        String periodo = obterPeriodo(scanner);
        int numeroPedidosAtendidos = desempenhoFinanceiroController.calcularNumeroPedidosAtendidos(periodo);
        System.out.printf("Número de Pedidos Atendidos no período %s: %d\n", periodo, numeroPedidosAtendidos);
    }

    private void exibirNumeroPedidosNaoAtendidos(Scanner scanner) {
        String periodo = obterPeriodo(scanner);
        int numeroPedidosNaoAtendidos = desempenhoFinanceiroController.calcularNumeroPedidosNaoAtendidos(periodo);
        System.out.printf("Número de Pedidos Não Atendidos no período %s: %d\n", periodo, numeroPedidosNaoAtendidos);
    }

    private String obterPeriodo(Scanner scanner) {
        System.out.print("Você quer procurar apenas pelo dia ou pelo período? (dia/periodo): ");
        String escolha = scanner.nextLine().trim().toLowerCase();

        if (escolha.equals("dia")) {
            System.out.print("Digite o dia: ");
            String dia = scanner.nextLine().trim();
            return dia;
        } else if (escolha.equals("periodo")) {
            System.out.print("Digite a hora inicial: ");
            String horaInicio = scanner.nextLine().trim();

            System.out.print("Digite o dia inicial: ");
            String diaInicio = scanner.nextLine().trim();

            System.out.print("Digite a hora final: ");
            String horaFim = scanner.nextLine().trim();

            System.out.print("Digite o dia final: ");
            String diaFim = scanner.nextLine().trim();

            return String.format("%s %s to %s %s", diaInicio, horaInicio, diaFim, horaFim);
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            return obterPeriodo(scanner); // Chama a função novamente em caso de entrada inválida
        } }
    }