package View;

import Controller.SimulacaoDiaController;

import java.util.Scanner;

public class SimulacaoDiaView {

    private final SimulacaoDiaController simulacaoDiaController;

    public SimulacaoDiaView(SimulacaoDiaController simulacaoDiaController) {
        this.simulacaoDiaController = simulacaoDiaController;
    }

    public void exibirMenuSimulacaoDia(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Simulação do Dia-a-Dia --");
            System.out.println("1. Iniciar Novo Dia");
            System.out.println("2. Avançar Unidade de Tempo");
            System.out.println("3. Consultar Notificações do Dia");
            System.out.println("4. Encerrar Dia");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 4);

            switch (opcao) {
                case 1:
                    iniciarNovoDia();
                    break;
                case 2:
                    avancarUnidadeTempo();
                    break;
                case 3:
                    consultarNotificacoes();
                    break;
                case 4:
                    encerrarDia();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void iniciarNovoDia() {
        String mensagem = simulacaoDiaController.iniciarNovoDia();
        System.out.println(mensagem);
    }

    private void avancarUnidadeTempo() {
        String mensagem = simulacaoDiaController.avancarUnidadeTempo();
        System.out.println(mensagem);
    }

    private void consultarNotificacoes() {
        String notificacoes = simulacaoDiaController.obterNotificacoes();
        System.out.println(notificacoes);
    }

    private void encerrarDia() {
        String mensagem = simulacaoDiaController.encerrarDia();
        System.out.println(mensagem);
    }
}