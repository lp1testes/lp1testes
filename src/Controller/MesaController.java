package Controller;

import DAL.MesaDAL;
import Model.Configuracao;
import Model.Mesa;

public class MesaController {

    private Mesa[] mesas;
    private MesaDAL mesaDAL;

    public MesaController(Configuracao configuracao) {
        mesaDAL = new MesaDAL(configuracao);
        mesas = mesaDAL.carregarMesas();
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
}