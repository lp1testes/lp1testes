package Controller;

import DAL.ReservaDAL;
import Model.Configuracao;
import Model.Reserva;

import java.util.Arrays;
import java.util.Comparator;

public class ReservaController {

    private Reserva[] reservas;
    private ReservaDAL reservaDAL;
    private Configuracao configuracao;
    private MesaController mesaController;
    private ConfiguracaoController configuracaoController;

    public ReservaController(Configuracao configuracao) {
        this.configuracao = configuracao;
        reservaDAL = new ReservaDAL(configuracao);
        reservas = reservaDAL.carregarReservas();
        mesaController = new MesaController(configuracao, this);
        configuracaoController = ConfiguracaoController.getInstancia();
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
}