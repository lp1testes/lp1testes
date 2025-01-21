package View;

import java.util.Scanner;
import Controller.EstatisticaController;

public class EstatisticasView {

    public void exibirMenuEstatisticasGerais(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Estatísticas Gerais --");
            System.out.println("1. Mostrar Prato Mais Pedido");
            System.out.println("2. Quantidade Total de Clientes Atendidos");
            System.out.println("3. Tempo Médio de Espera por Mesa");
            System.out.println("4. Tempo Médio para Servir uma Mesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 4);

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
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void mostrarPratoMaisPedido() {

    }

    private void mostrarClientesAtendidos() {

    }

    private void mostrarTempoMedioEsperaMesa() {

    }

    private void mostrarTempoMedioServirMesa() {

    }
}
