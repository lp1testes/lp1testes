package Controller;

import DAL.DesempenhoFinanceiroDAL;
import Model.DesempenhoFinanceiro;

public class DesempenhoFinanceiroController {

    private DesempenhoFinanceiro desempenhoFinanceiro;
    private DesempenhoFinanceiroDAL desempenhoFinanceiroDAL;

    public DesempenhoFinanceiroController() {
        desempenhoFinanceiro = new DesempenhoFinanceiro();
        //desempenhoFinanceiro = desempenhoFinanceiroDAL.carregarDesempenhosFinanceiros();
    }

    public DesempenhoFinanceiro getDesempenhoFinanceiro() {
        return desempenhoFinanceiro;
    }

    public void atualizarConfiguracao(DesempenhoFinanceiro novoDesempenhoFinanceiro) {
        desempenhoFinanceiro = novoDesempenhoFinanceiro;
        //desempenhoFinanceiroDAL.salvarDesempenhosFinanceiros(desempenhoFinanceiro);
    }
}