package View;

import java.util.Scanner;

public class LogsView {

    public void exibirMenuLogsEventos(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Logs e Eventos --");
            System.out.println("1. Consultar Logs de um Dia Específico");
            System.out.println("2. Consultar Logs de Todos os Dias");
            System.out.println("3. Gerar Novo Log");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 3);

            switch (opcao) {
                case 1:
                    consultarLogsDiaEspecifico();
                    break;
                case 2:
                    consultarLogsTodosDias();
                    break;
                case 3:
                    gerarNovoLog();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void consultarLogsDiaEspecifico() {

    }

    private void consultarLogsTodosDias() {

    }

    private void gerarNovoLog() {

    }
}
