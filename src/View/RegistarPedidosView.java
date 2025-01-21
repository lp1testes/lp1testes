package View;

import java.util.Scanner;

public class RegistarPedidosView {

    public void exibirMenuRegistoPedidos(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Registo de Pedidos --");
            System.out.println("1. Associar Pedido a uma Mesa");
            System.out.println("2. Calcular Total de um Pedido");
            System.out.println("3. Gerir Pedidos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 3);

            switch (opcao) {
                case 1:
                    associarPedido();
                    break;
                case 2:
                    calcularPedido();
                    break;
                case 3:
                    gerirPedidos();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void associarPedido() {

    }

    private void calcularPedido() {

    }

    private void gerirPedidos() {

    }
}
