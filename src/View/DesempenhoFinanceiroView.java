package View;

import java.util.Scanner;
import Controller.DesempenhoFinanceiroController;


public class DesempenhoFinanceiroView {

    public void exibirMenuDesempenhoFinanceiro(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Gestão Financeira --");
            System.out.println("1. Exibir Total Faturado");
            System.out.println("2. Exibir Total de Gastos");
            System.out.println("3. Exibir Total de Lucro");
            System.out.println("4. Exibir Número de Pedidos Atendidos");
            System.out.println("5. Exibir Número de Pedidos Não Atendidos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 5);

            switch (opcao) {
                case 1:
                    exibirTotalFaturado();
                    break;
                case 2:
                    exibirTotalGastos();
                    break;
                case 3:
                    exibirTotalLucro();
                    break;
                case 4:
                    exibirNumeroPedidosAtendidos();
                    break;
                case 5:
                    exibirNumeroPedidosNaoAtendidos();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void exibirTotalFaturado() {

    }

    private void exibirTotalGastos() {

    }

    private void exibirTotalLucro() {

    }

    private void exibirNumeroPedidosAtendidos() {

    }

    private void exibirNumeroPedidosNaoAtendidos() {

    }
}
