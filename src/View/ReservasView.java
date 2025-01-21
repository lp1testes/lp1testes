package View;

import Controller.ReservaController;

import java.util.Scanner;

public class ReservasView {

    private ReservaController reservaController;

    public ReservasView(ReservaController reservaController) {
        this.reservaController = reservaController;
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
                    adicionarReserva();
                    break;
                case 3:
                    removerReserva();
                    break;
                case 4:
                    editarReserva();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void mostrarReservas() {
        String reservas = reservaController.listarReservas();
        System.out.println(reservas);
    }

    private void adicionarReserva() {

    }

    private void removerReserva() {

    }

    private void editarReserva() {

    }
}
