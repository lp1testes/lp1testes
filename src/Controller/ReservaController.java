package Controller;

import DAL.ReservaDAL;
import Utils.Configuracao;
import Model.Reserva;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReservaController {

    private Reserva[] reservas;

    private static ReservaController instance;
    private static ReservaDAL reservaDAL ;
    private Configuracao configuracao = Configuracao.getInstancia();
    private static final MesaController mesaController = MesaController.getInstance();
    private static final ConfiguracaoController configuracaoController = ConfiguracaoController.getInstancia();
    private static final SimulacaoDiaController simulacaoDiaController = SimulacaoDiaController.getInstance();
    private static final LogsController logsController = LogsController.getInstance();

    ReservaController() {
        this.reservaDAL             = new ReservaDAL();
        this.reservas               = reservaDAL.carregarReservas();
        //this.configuracaoController = ConfiguracaoController.getInstancia();
        //this.simulacaoDiaController = SimulacaoDiaController.getInstance();
        //this.mesaController         = MesaController.getInstance();
        //this.logsController         = LogsController.getInstance();
    }
    // Método para obter a instância Singleton
    public static synchronized ReservaController getInstance() {
        if (instance == null) {
            instance = new ReservaController();
        }
        return instance;
    }

    public Reserva[] getReservas() {
        return reservas;
    }

    public String listarReservas() {
        Arrays.sort(getReservas(), new Comparator<Reserva>() {
            @Override
            public int compare(Reserva r1, Reserva r2) {
                if (r1 == null && r2 == null) {
                    return 0;
                }
                if (r1 == null) {
                    return 1;
                }
                if (r2 == null) {
                    return -1;
                }
                return Integer.compare(r1.getId(), r2.getId());
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append("\n-- Lista de Reservas --\n");
        for (Reserva reserva : reservas) {
            if (reserva != null) {
                sb.append("ID: ").append(reserva.getId())
                        .append(", Nome: ").append(reserva.getNome())
                        .append(", Número de Pessoas: ").append(reserva.getNumeroPessoas())
                        .append(", Tempo de Chegada: ").append(reserva.getTempoChegada())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    public void atualizarReservas(Reserva[] novasReservas) {
        reservas = novasReservas;
        reservaDAL.salvarReservas(reservas);
    }

    public String verificarChegadaReservas(int tempoAtual) {
        StringBuilder notificacoes = new StringBuilder();
        for (Reserva reserva : reservas) {
            if (reserva != null && reserva.getTempoChegada() == tempoAtual) {
                notificacoes.append("\nChegou Reserva: ").append(reserva.getNome())
                        .append(", Número de Pessoas: ").append(reserva.getNumeroPessoas()).append("\n");
            }
        }
        return notificacoes.toString();
    }

    public void verificarReservas(int tempoAtual) {
        for (Reserva reserva : reservas) {
            if (reserva != null && tempoAtual >= reserva.getTempoChegada() && tempoAtual <= reserva.getTempoChegada() + configuracao.getUnidadesTempoIrParaMesa()) {
                System.out.println("Reserva " + reserva.getNome() + " está dentro do tempo permitido para ser associada a uma mesa.");
                mesaController.atribuirClientesAMesa(reserva.getId(), reserva, tempoAtual); // Atualizando para passar o tempoAtual
            } else if (reserva != null && tempoAtual > reserva.getTempoChegada() + configuracao.getUnidadesTempoIrParaMesa()) {
                System.out.println("Reserva " + reserva.getNome() + " passou do tempo permitido e foi embora.");
            }
        }
    }

    public Reserva getReservaById(int id) {
        for (Reserva reserva : reservas) {
            if (reserva != null && reserva.getId() == id) {
                return reserva;
            }
        }
        return null;
    }

    public Reserva[] listarReservasDisponiveis(int tempoAtual) {
        Reserva[] reservasDisponiveis = new Reserva[reservas.length];
        int index = 0;
        for (Reserva reserva : reservas) {
            if (reserva != null && !reserva.isAssociada() && reserva.getTempoChegada() <= tempoAtual && tempoAtual <= reserva.getTempoChegada() + configuracaoController.getConfiguracao().getUnidadesTempoIrParaMesa()) {
                reservasDisponiveis[index++] = reserva;
            }
        }
        return Arrays.copyOf(reservasDisponiveis, index);
    }
    public void criarReserva(String nome, int numeroPessoas, int tempoChegada) {
        int proximoId = reservaDAL.obterProximoId(reservas);
        Reserva novaReserva = new Reserva(proximoId, nome, numeroPessoas, tempoChegada);

        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i] == null) {
                reservas[i] = novaReserva;
                reservaDAL.salvarReservas(reservas);
                System.out.println("Reserva adicionada com sucesso!");
                return;
            }
        }
        System.out.println("Não é possível adicionar mais reservas. Capacidade máxima atingida.");
    }

    public Reserva[] listarReservasNaoAssociadas(int tempoAtual) {
        Configuracao configuracao = configuracaoController.getConfiguracao();
        int tempoLimite = configuracao.getUnidadesTempoIrParaMesa();
        int count = 0;

        // Primeiro, contar o número de reservas não associadas
        for (Reserva reserva : reservas) {
            if (reserva != null && !reserva.isAssociada() && (tempoAtual > reserva.getTempoChegada() + tempoLimite)) {
                count++;
            }
        }

        // Criar um array do tamanho apropriado
        Reserva[] reservasNaoAssociadas = new Reserva[count];
        int index = 0;

        // Preencher o array com as reservas não associadas
        for (Reserva reserva : reservas) {
            if (reserva != null && !reserva.isAssociada() && (tempoAtual > reserva.getTempoChegada() + tempoLimite)) {
                reservasNaoAssociadas[index++] = reserva;
            }
        }

        return reservasNaoAssociadas;
    }

    public void editarReserva(int id, String novoNome, int novoNumeroPessoas) {
        Reserva reserva = getReservaById(id);
        if (reserva != null) {
            reserva.setNome(novoNome);
            reserva.setNumeroPessoas(novoNumeroPessoas);
            reservaDAL.salvarReservas(reservas);
            System.out.println("Reserva editada com sucesso!");
        } else {
            System.out.println("Reserva não encontrada.");
        }
    }

    public void removerReserva(int id) {
        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i] != null && reservas[i].getId() == id) {
                reservas[i] = null;
                reservaDAL.salvarReservas(reservas);
                System.out.println("Reserva removida com sucesso!");
                return;
            }
        }
        System.out.println("Reserva não encontrada.");
    }

    public String atribuirClientesMesas(Scanner scanner){

        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();
        int currentDay = simulacaoDiaController.getDiaAtual();
        int unitsForAssignment = configuracaoController.getConfiguracao().getUnidadesTempoIrParaMesa();

        Reserva[] reservasDisponiveis = listarReservasDisponiveis(tempoAtual);

        if (reservasDisponiveis.length == 0) {
            return "Não há reservas disponíveis para associar a uma mesa no momento.";
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

        Reserva reserva = getReservaById(idReserva);
        if (reserva != null) {
            int tempoChegada = reserva.getTempoChegada();
            int tempoLimite = tempoChegada + unitsForAssignment;

            if (tempoAtual <= tempoLimite) {
                mesaController.atribuirClientesAMesa(idMesa, reserva, tempoAtual);

                // Criação do log
                String logType = "ACTION";
                String logDescription = String.format("Clientes da reserva %s (ID: %d) foram atribuídos à mesa %d. Número de Pessoas: %d, Tempo de Chegada: %d",
                        reserva.getNome(), reserva.getId(), idMesa, reserva.getNumeroPessoas(), reserva.getTempoChegada());

                logsController.criarLog(currentDay, tempoAtual, logType, logDescription);

                return "Clientes atribuídos à mesa com sucesso!";
            } else {
                return "Tempo limite para atribuição da reserva " + reserva.getNome() + " expirou.";
            }
        } else {
            return "Reserva não encontrada.";
        }
    }

    public void verClienteDaMesa(Scanner scanner){

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

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "INFO";
            String logDescription = String.format("Cliente da mesa %d visualizado: ID %d, Nome %s", idMesa, reserva.getId(), reserva.getNome());

            logsController.criarLog(currentDay, currentHour, logType, logDescription);
        } else {
            System.out.println("Nenhum cliente associado a esta mesa ou mesa não encontrada.");
        }
    }
}