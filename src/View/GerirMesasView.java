package View;

import Controller.*;
import Model.Configuracao;
import Model.Reserva;
import Model.Mesa;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GerirMesasView {

    private MesaController mesaController;
    private ReservaController reservaController;
    private SimulacaoDiaController simulacaoDiaController;
    private ConfiguracaoController configuracaoController;

    public GerirMesasView(MesaController mesaController, ReservaController reservaController, SimulacaoDiaController simulacaoDiaController, ConfiguracaoController configuracaoController) {
        this.mesaController = mesaController;
        this.reservaController = reservaController;
        this.simulacaoDiaController = simulacaoDiaController;
        this.configuracaoController = configuracaoController;
    }

    public void exibirMenuGestaoMesas(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Gestão de Mesas --");
            System.out.println("1. Registar Mesas");
            System.out.println("2. Verificar Estado das Mesas");
            System.out.println("3. Atribuir Clientes a Mesas");
            System.out.println("4. Ver Cliente da Mesa");
            System.out.println("5. Editar Mesa");
            System.out.println("6. Remover Mesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 6);

            switch (opcao) {
                case 1:
                    registarMesas(scanner);
                    break;
                case 2:
                    verificarEstadoMesas();
                    break;
                case 3:
                    atribuirClientesMesas(scanner);
                    break;
                case 4:
                    verClienteDaMesa(scanner);
                    break;
                case 5:
                    editarMesa(scanner);
                    break;
                case 6:
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

    public void atribuirClientesMesas(Scanner scanner) {
        verificarEstadoMesas();

        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();
        int currentDay = simulacaoDiaController.getDiaAtual();
        int unitsForAssignment = configuracaoController.getConfiguracao().getUnidadesTempoIrParaMesa();

        Reserva[] reservasDisponiveis = reservaController.listarReservasDisponiveis(tempoAtual);

        if (reservasDisponiveis.length == 0) {
            System.out.println("Não há reservas disponíveis para associar a uma mesa no momento.");
            return;
        }

        System.out.println("\n-- Reservas Disponíveis --");
        for (Reserva reserva : reservasDisponiveis) {
            if (reserva != null) {
                System.out.println("ID: " + reserva.getId() + ", Nome: " + reserva.getNome() + ", Número de Pessoas: " + reserva.getNumeroPessoas() + ", Tempo de Chegada: " + reserva.getTempoChegada());
            }
        }

        int idMesa = -1;
        while (idMesa == -1) {
            System.out.print("\nID da mesa a atribuir clientes: ");
            try {
                idMesa = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }

        int idReserva = -1;
        while (idReserva == -1) {
            System.out.print("ID da reserva: ");
            try {
                idReserva = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine(); // Limpar o buffer

        Reserva reserva = reservaController.getReservaById(idReserva);
        if (reserva != null) {
            int tempoChegada = reserva.getTempoChegada();
            int tempoLimite = tempoChegada + unitsForAssignment;

            if (tempoAtual <= tempoLimite) {
                mesaController.atribuirClientesAMesa(idMesa, reserva, tempoAtual);
                LogsController logsController = new LogsController();
                String logType = "ACTION";
                String logDescription = String.format("Clientes da reserva %s (ID: %d) foram atribuídos à mesa %d. Número de Pessoas: %d, Tempo de Chegada: %d",
                        reserva.getNome(), reserva.getId(), idMesa, reserva.getNumeroPessoas(), reserva.getTempoChegada());
                logsController.criarLog(currentDay, tempoAtual, logType, logDescription);
            } else {
                System.out.println("Tempo limite para atribuição da reserva " + reserva.getNome() + " expirou.");
            }
        } else {
            System.out.println("Reserva não encontrada.");
        }
    }

    private void verClienteDaMesa(Scanner scanner) {
        int idMesa = -1;
        while (idMesa == -1) {
            System.out.print("\nID da mesa para ver o cliente: ");
            try {
                idMesa = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine(); // Limpar o buffer

        Reserva reserva = mesaController.getClienteDaMesa(idMesa);
        if (reserva != null) {
            System.out.println("ID: " + reserva.getId() + ", Nome: " + reserva.getNome() + ", Número de Pessoas: " + reserva.getNumeroPessoas() + ", Tempo de Chegada: " + reserva.getTempoChegada());
        } else {
            System.out.println("Nenhum cliente associado a esta mesa ou mesa não encontrada.");
        }
    }

    private void editarMesa(Scanner scanner) {
        verificarEstadoMesas();

        int id = -1;
        while (id == -1) {
            System.out.print("\nID da mesa a editar: ");
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        int novaCapacidade = -1;
        while (novaCapacidade == -1) {
            System.out.print("Nova capacidade da mesa: ");
            try {
                novaCapacidade = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        Boolean novaOcupacao = null;
        while (novaOcupacao == null) {
            System.out.print("A mesa está ocupada? (true/false): ");
            try {
                novaOcupacao = scanner.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira 'true' ou 'false'.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        mesaController.editarMesa(id, novaCapacidade, novaOcupacao);
        System.out.println("Mesa editada com sucesso!");
    }

    private void removerMesa(Scanner scanner) {
        verificarEstadoMesas();

        int id = -1;
        while (id == -1) {
            System.out.print("\nID da mesa a remover: ");
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
        scanner.nextLine();

        mesaController.removerMesa(id);
        System.out.println("Mesa removida com sucesso!");
    }
}