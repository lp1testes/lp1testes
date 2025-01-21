package View;

import Controller.MesaController;
import Model.Configuracao;
import Model.Mesa;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class GerirMesasView {

    private MesaController mesaController;

    public GerirMesasView(MesaController mesaController) {
        this.mesaController = mesaController;
    }

    public void exibirMenuGestaoMesas(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Gestão de Mesas --");
            System.out.println("1. Registar Mesas");
            System.out.println("2. Verificar Estado das Mesas");
            System.out.println("3. Atribuir Clientes a Mesas");
            System.out.println("4. Editar Mesa");
            System.out.println("5. Remover Mesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 5);

            switch (opcao) {
                case 1:
                    registarMesas(scanner);
                    break;
                case 2:
                    verificarEstadoMesas();
                    break;
                case 3:
                    atribuirClientesMesas();
                    break;
                case 4:
                    editarMesa(scanner);
                    break;
                case 5:
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
        int capacidade = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print("Insira a capacidade da mesa (ou 000 para cancelar): ");
            String input = scanner.nextLine().trim();

            if (input.equals("000")) {
                System.out.println("Adição de mesa cancelada.");
                return;
            } else {
                try {
                    capacidade = Integer.parseInt(input);
                    if (capacidade > 0) {
                        valido = true;
                    } else {
                        System.out.println("Capacidade inválida! A capacidade não pode ser menor ou igual a zero.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                }
            }
        }

        Mesa novaMesa = new Mesa(null, capacidade, false);
        mesaController.adicionarMesa(novaMesa);

        System.out.println("Mesa registrada com sucesso!");
    }

    private void verificarEstadoMesas() {
        Mesa[] mesas = mesaController.getMesas();
        Arrays.sort(mesas, new Comparator<Mesa>() {
            @Override
            public int compare(Mesa m1, Mesa m2) {
                if (m1 == null && m2 == null) {
                    return 0;
                }
                if (m1 == null) {
                    return 1;
                }
                if (m2 == null) {
                    return -1;
                }
                return Integer.compare(m1.getId(), m2.getId());
            }
        });

        System.out.println("\n-- Estado das Mesas --");
        for (Mesa mesa : mesas) {
            if (mesa != null) {
                String estado = mesa.isOcupada() ? "Ocupada" : "Livre";
                System.out.println("Mesa " + mesa.getId() + ": Capacidade " + mesa.getCapacidade() + " - " + estado);
            }
        }
    }

    private void atribuirClientesMesas() {
        // Implementar lógica para atribuir clientes às mesas
    }

    private void editarMesa(Scanner scanner) {
        verificarEstadoMesas();

        System.out.print("\nID da mesa a editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nova capacidade da mesa: ");
        int novaCapacidade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("A mesa está ocupada? (true/false): ");
        boolean novaOcupacao = scanner.nextBoolean();
        scanner.nextLine();

        mesaController.editarMesa(id, novaCapacidade, novaOcupacao);
        System.out.println("Mesa editada com sucesso!");
    }

    private void removerMesa(Scanner scanner) {
        verificarEstadoMesas();

        System.out.print("\nID da mesa a remover: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        mesaController.removerMesa(id);
        System.out.println("Mesa removida com sucesso!");
    }
}