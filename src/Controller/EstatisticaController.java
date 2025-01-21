package Controller;

import DAL.EstatisticaDAL;
import Model.Estatistica;

public class EstatisticaController {

    private Estatistica estatistica;
    private EstatisticaDAL estatisticaDAL;

    public EstatisticaController() {
        estatistica = new Estatistica();
        //estatistica = estatisticaDAL.carregarEstatisticas();
    }

    public Estatistica getEstatistica() {
        return estatistica;
    }

    public void atualizarEstatistica(Estatistica novaEstatistica) {
        estatistica = novaEstatistica;
        //estatisticaDAL.salvarEstatisticas(estatistica);
    }
}