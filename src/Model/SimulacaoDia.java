package Model;

public class SimulacaoDia {

    private Integer dia;
    private Integer unidadeTempoAtual;
    private boolean isAtivo;

    public SimulacaoDia() {
    }

    public SimulacaoDia(Integer dia, Integer unidadeTempoAtual, boolean isAtivo) {
        this.dia = dia;
        this.unidadeTempoAtual = unidadeTempoAtual;
        this.isAtivo = isAtivo;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getUnidadeTempoAtual() {
        return unidadeTempoAtual;
    }

    public void setUnidadeTempoAtual(Integer unidadeTempoAtual) {
        this.unidadeTempoAtual = unidadeTempoAtual;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }
}
