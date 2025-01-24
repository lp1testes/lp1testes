package Controller;

import DAL.MesaDAL;
import Model.Configuracao;
import Model.Mesa;
import Model.Reserva;
import Model.MesaReserva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MesaController {

    private Mesa[] mesas;
    private MesaDAL mesaDAL;
    private ReservaController reservaController;
    private List<MesaReserva> mesaReservas;

    public MesaController(Configuracao configuracao, ReservaController reservaController) {
        mesaDAL = new MesaDAL(configuracao);
        mesas = mesaDAL.carregarMesas();
        this.reservaController = reservaController;
        this.mesaReservas = new ArrayList<>();
    }

    public Mesa[] getMesas() {
        return mesas;
    }

    public void adicionarMesa(Mesa novaMesa) {
        int proximoId = mesaDAL.obterProximoId(mesas);
        novaMesa.setId(proximoId);

        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] == null) {
                mesas[i] = novaMesa;
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Não é possível adicionar mais mesas. Capacidade máxima atingida.");
    }

    public void editarMesa(int id, int novaCapacidade, boolean novaOcupacao) {
        for (Mesa mesa : mesas) {
            if (mesa != null && mesa.getId() == id) {
                mesa.setCapacidade(novaCapacidade);
                mesa.setOcupada(novaOcupacao);
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public void removerMesa(int id) {
        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] != null && mesas[i].getId() == id) {
                mesas[i] = null;
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public void atribuirClientesAMesa(int idMesa, Reserva reserva, int tempoAtual) {
        if (reserva.isAssociada()) {
            System.out.println("Reserva " + reserva.getNome() + " já está associada a uma mesa.");
            return;
        }

        for (Mesa mesa : mesas) {
            if (mesa != null && mesa.getId() == idMesa) {
                if (!mesa.isOcupada() && reserva.getNumeroPessoas() <= mesa.getCapacidade()) {
                    mesa.setOcupada(true);
                    mesaDAL.salvarMesas(mesas);
                    MesaReserva mesaReserva = new MesaReserva(idMesa, reserva.getId(), tempoAtual);
                    mesaReservas.add(mesaReserva);
                    reserva.setAssociada(true); // Marcar a reserva como associada
                    System.out.println("Clientes da reserva " + reserva.getNome() + " atribuídos à mesa " + idMesa);
                    return;
                } else {
                    System.out.println("Mesa ocupada ou capacidade insuficiente.");
                    return;
                }
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public Reserva getClienteDaMesa(int idMesa) {
        for (MesaReserva mesaReserva : mesaReservas) {
            if (mesaReserva.getIdMesa() == idMesa) {
                return reservaController.getReservaById(mesaReserva.getIdReserva());
            }
        }
        return null;
    }

    public Mesa[] listarMesasDisponiveis() {
        Mesa[] mesasDisponiveis = new Mesa[mesas.length];
        int index = 0;
        for (Mesa mesa : mesas) {
            if (mesa != null && !mesa.isOcupada()) {
                mesasDisponiveis[index++] = mesa;
            }
        }
        return Arrays.copyOf(mesasDisponiveis, index);
    }
}