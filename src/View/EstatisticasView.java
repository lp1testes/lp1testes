package View;

import java.util.Scanner;
import Controller.EstatisticaController;
import Controller.SimulacaoDiaController;
import Controller.ConfiguracaoController;
import Model.Configuracao;
import Model.Prato;

public class EstatisticasView {
    private EstatisticaController estatisticaController;
    private SimulacaoDiaController simulacaoDiaController;
    private ConfiguracaoController configuracaoController;

    public EstatisticasView() {
        this.configuracaoController = ConfiguracaoController.getInstancia();
        Configuracao configuracao = configuracaoController.getConfiguracao();
        this.estatisticaController = new EstatisticaController(configuracao);
        this.simulacaoDiaController = SimulacaoDiaController.getInstance();
    }

    public void exibirMenuEstatisticasGerais(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Estatísticas Gerais --");
            System.out.println("1. Mostrar Prato Mais Pedido");
            System.out.println("2. Quantidade Total de Clientes Atendidos");
            System.out.println("3. Tempo Médio de Espera por Mesa");
            System.out.println("4. Tempo Médio para Servir uma Mesa");
            System.out.println("5. Quantidade de Clientes Atendidos por Dia");
            System.out.println("6. Quantidade de Clientes Atendidos por Período");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 6);

            switch (opcao) {
                case 1:
                    mostrarPratoMaisPedido();
                    break;
                case 2:
                    mostrarClientesAtendidos();
                    break;
                case 3:
                    mostrarTempoMedioEsperaMesa();
                    break;
                case 4:
                    mostrarTempoMedioServirMesa();
                    break;
                case 5:
                    mostrarClientesAtendidosPorDia(scanner);
                    break;
                case 6:
                    mostrarClientesAtendidosPorPeriodo(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void mostrarPratoMaisPedido() {
        Prato prato = estatisticaController.mostrarPratoMaisPedido();
        if (prato != null) {
            System.out.println("O prato mais pedido é: " + prato.getNome());
        } else {
            System.out.println("Nenhum prato foi pedido.");
        }
    }

    private void mostrarClientesAtendidos() {
        int totalClientesAtendidos = simulacaoDiaController.calcularClientesAtendidos();
        System.out.println("Total de clientes atendidos: " + totalClientesAtendidos);
    }

    private void mostrarTempoMedioEsperaMesa() {
        double tempoMedioEspera = estatisticaController.calcularTempoMedioEsperaMesa();
        System.out.println("Tempo médio de espera por mesa: " + tempoMedioEspera + " unidades de tempo.");
    }

    private void mostrarTempoMedioServirMesa() {
        double tempoMedioServir = estatisticaController.calcularTempoMedioServirMesa();
        System.out.println("Tempo médio para servir uma mesa: " + tempoMedioServir + " unidades de tempo.");
    }

    private void mostrarClientesAtendidosPorDia(Scanner scanner) {
        System.out.print("Digite o dia: ");
        int dia = scanner.nextInt();
        int totalClientesAtendidos = simulacaoDiaController.calcularClientesAtendidosPorDia(dia);
        System.out.println("Total de clientes atendidos no dia " + dia + ": " + totalClientesAtendidos);
    }

    private void mostrarClientesAtendidosPorPeriodo(Scanner scanner) {
        System.out.print("Digite o dia de início: ");
        int diaInicio = scanner.nextInt();
        System.out.print("Digite a hora de início: ");
        int horaInicio = scanner.nextInt();
        System.out.print("Digite o dia de fim: ");
        int diaFim = scanner.nextInt();
        System.out.print("Digite a hora de fim: ");
        int horaFim = scanner.nextInt();
        int totalClientesAtendidos = simulacaoDiaController.calcularClientesAtendidosPorPeriodo(diaInicio, horaInicio, diaFim, horaFim);
        System.out.println("Total de clientes atendidos do dia " + diaInicio + " hora " + horaInicio + " ao dia " + diaFim + " hora " + horaFim + ": " + totalClientesAtendidos);
    }
}