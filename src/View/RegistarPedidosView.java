package View;

import Controller.MesaController;
import Controller.ReservaController;
import Controller.SimulacaoDiaController;
import Controller.ConfiguracaoController;
import Controller.PratoController;
import Controller.MenuController;
import Model.Reserva;
import Model.Mesa;
import Model.Prato;
import Model.Menu;

import java.util.Scanner;

public class RegistarPedidosView {

    private MesaController mesaController;
    private ReservaController reservaController;
    private SimulacaoDiaController simulacaoDiaController;
    private ConfiguracaoController configuracaoController;
    private PratoController pratoController;
    private MenuController menuController;

    public RegistarPedidosView(MesaController mesaController, ReservaController reservaController, SimulacaoDiaController simulacaoDiaController, ConfiguracaoController configuracaoController, PratoController pratoController, MenuController menuController) {
        this.mesaController = mesaController;
        this.reservaController = reservaController;
        this.simulacaoDiaController = simulacaoDiaController;
        this.configuracaoController = configuracaoController;
        this.pratoController = pratoController;
        this.menuController = menuController;
    }

    public void exibirMenuRegistoPedidos(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Registo de Pedidos --");
            System.out.println("1. Associar Pedido a uma Mesa");
            System.out.println("2. Calcular Total de um Pedido");
            System.out.println("3. Gerir Pedidos");
            System.out.println("4. Listar Pedidos Atendidos de uma Mesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 4);

            switch (opcao) {
                case 1:
                    associarPedido(scanner);
                    break;
                case 2:
                    calcularPedido();
                    break;
                case 3:
                    gerirPedidos();
                    break;
                case 4:
                    listarPedidosAtendidos(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    private void associarPedido(Scanner scanner) {
        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();
        int unidadesTempoParaPedido = configuracaoController.getConfiguracao().getUnidadesTempoParaPedido();

        // Listar mesas ocupadas com reservas que ainda não foram atendidas
        Mesa[] mesasOcupadas = mesaController.listarMesasOcupadasComReservasNaoAtendidas(tempoAtual, unidadesTempoParaPedido);
        if (mesasOcupadas.length == 0) {
            System.out.println("Não há mesas ocupadas com reservas não atendidas no momento.");
            return;
        }

        System.out.println("\n-- Mesas Ocupadas com Reservas Não Atendidas --");
        for (Mesa mesa : mesasOcupadas) {
            if (mesa != null) {
                Reserva reserva = mesaController.getClienteDaMesa(mesa.getId());
                System.out.println("Mesa " + mesa.getId() + " - Reserva: " + reserva.getNome() + " (ID da Reserva: " + reserva.getId() + ")");
            }
        }

        System.out.print("\nID da mesa para registrar o pedido: ");
        int idMesa = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Reserva reserva = mesaController.getClienteDaMesa(idMesa);
        if (reserva == null) {
            System.out.println("Nenhuma reserva encontrada para a mesa " + idMesa);
            return;
        }

        int tempoAssociacao = reserva.getTempoChegada();
        int tempoLimite = tempoAssociacao + unidadesTempoParaPedido;

        if (tempoAtual > tempoLimite) {
            System.out.println("Tempo limite para registrar o pedido expirou. Clientes da reserva " + reserva.getNome() + " foram embora.");
            mesaController.removerReservaDaMesa(idMesa);
            return;
        }

        System.out.println("Clientes da reserva " + reserva.getNome() + " estão prontos para fazer o pedido.");
        mesaController.registrarPedido(idMesa, tempoAtual, scanner);

        // Marcar a reserva como atendida
        mesaController.marcarReservaComoAtendida(idMesa);
    }

    private void exibirSubmenuCliente(Scanner scanner, int idMesa, int clienteIndex) {
        int opcao;

        do {
            System.out.printf("Cliente %d: Escolha entre as seguintes opções:%n", clienteIndex + 1);
            System.out.println("1. Prato");
            System.out.println("2. Menu");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    listarPratos(scanner, idMesa, clienteIndex);
                    break;
                case 2:
                    listarMenus(scanner, idMesa, clienteIndex);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 1 && opcao != 2);
    }

    private void listarPratos(Scanner scanner, int idMesa, int clienteIndex) {
        Prato[] pratos = pratoController.getPratos();
        if (pratos.length == 0) {
            System.out.println("Não há pratos disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Pratos Disponíveis --");
        for (Prato prato : pratos) {
            if (prato != null && prato.isDisponivel()) {
                System.out.println("ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
            }
        }

        System.out.print("Escolha o ID do prato: ");
        int idPrato = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Prato pratoSelecionado = pratoController.getPratoById(idPrato);
        if (pratoSelecionado != null && pratoSelecionado.isDisponivel()) {
            mesaController.adicionarPratoAoPedido(idMesa, pratoSelecionado);
            System.out.printf("Prato %s adicionado ao pedido do Cliente %d.%n", pratoSelecionado.getNome(), clienteIndex + 1);
        } else {
            System.out.println("Prato inválido ou não disponível.");
        }
    }

    private void listarMenus(Scanner scanner, int idMesa, int clienteIndex) {
        Menu[] menus = menuController.obterTodosMenus();
        if (menus.length == 0) {
            System.out.println("Não há menus disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Menus Disponíveis --");
        for (Menu menu : menus) {
            if (menu != null) {
                System.out.println("ID: " + menu.getId() + " - Pratos:");
                for (Prato prato : menu.getPratos()) {
                    if (prato != null && prato.isDisponivel()) {
                        System.out.println("   ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
                    }
                }
            }
        }

        System.out.print("Escolha o ID do menu: ");
        int idMenu = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Menu menuSelecionado = menuController.getMenuById(idMenu);
        if (menuSelecionado != null) {
            mesaController.adicionarMenuAoPedido(idMesa, menuSelecionado);
            System.out.printf("Menu %d adicionado ao pedido do Cliente %d.%n", menuSelecionado.getId(), clienteIndex + 1);
        } else {
            System.out.println("Menu inválido.");
        }
    }

    private void listarPedidosAtendidos(Scanner scanner) {
        System.out.print("Digite o ID da mesa para listar os pedidos atendidos: ");
        int idMesa = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        mesaController.listarPedidosAtendidos(idMesa);
    }

    private void calcularPedido() {
        // Implementar a funcionalidade de calcular o total de um pedido
    }

    private void gerirPedidos() {
        // Implementar a funcionalidade de gerir pedidos
    }
}