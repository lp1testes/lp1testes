package Controller;

import DAL.ReservaDAL;
import Model.Configuracao;
import Model.Mesa;
import Model.Reserva;

import java.util.Arrays;
import java.util.Comparator;

public class ReservaController {

    private Reserva[] reservas;
    private ReservaDAL reservaDAL;

    public ReservaController(Configuracao configuracao) {
        reservaDAL = new ReservaDAL(configuracao);
        reservas = reservaDAL.carregarReservas();
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
}