package Model;

public class Configuracao {

    private String caminhoFicheiros;
    private String separadorFicheiros;
    private int unidadesTempoDia;
    private int unidadesTempoIrParaMesa;
    private int unidadesTempoParaPedido;
    private int unidadesTempoParaPagamento;
    private double custoClienteNaoAtendido;
    private String password;

    public Configuracao() {
    }

    public Configuracao(String caminhoFicheiros, String separadorFicheiros, int unidadesTempoDia, int unidadesTempoIrParaMesa, int unidadesTempoParaPedido, int unidadesTempoParaPagamento, double custoClienteNaoAtendido, String password) {
        this.caminhoFicheiros = caminhoFicheiros;
        this.separadorFicheiros = separadorFicheiros;
        this.unidadesTempoDia = unidadesTempoDia;
        this.unidadesTempoIrParaMesa = unidadesTempoIrParaMesa;
        this.unidadesTempoParaPedido = unidadesTempoParaPedido;
        this.unidadesTempoParaPagamento = unidadesTempoParaPagamento;
        this.custoClienteNaoAtendido = custoClienteNaoAtendido;
        this.password = password;
    }

    public String getCaminhoFicheiros() {
        return caminhoFicheiros;
    }

    public void setCaminhoFicheiros(String caminhoFicheiros) {
        this.caminhoFicheiros = caminhoFicheiros;
    }

    public String getSeparadorFicheiros() {
        return separadorFicheiros;
    }

    public void setSeparadorFicheiros(String separadorFicheiros) {
        this.separadorFicheiros = separadorFicheiros;
    }

    public int getUnidadesTempoDia() {
        return unidadesTempoDia;
    }

    public void setUnidadesTempoDia(int unidadesTempoDia) {
        this.unidadesTempoDia = unidadesTempoDia;
    }

    public int getUnidadesTempoIrParaMesa() {
        return unidadesTempoIrParaMesa;
    }

    public void setUnidadesTempoIrParaMesa(int unidadesTempoIrParaMesa) {
        this.unidadesTempoIrParaMesa = unidadesTempoIrParaMesa;
    }

    public int getUnidadesTempoParaPedido() {
        return unidadesTempoParaPedido;
    }

    public void setUnidadesTempoParaPedido(int unidadesTempoParaPedido) {
        this.unidadesTempoParaPedido = unidadesTempoParaPedido;
    }

    public int getUnidadesTempoParaPagamento() {
        return unidadesTempoParaPagamento;
    }

    public void setUnidadesTempoParaPagamento(int unidadesTempoParaPagamento) {
        this.unidadesTempoParaPagamento = unidadesTempoParaPagamento;
    }

    public double getCustoClienteNaoAtendido() {
        return custoClienteNaoAtendido;
    }

    public void setCustoClienteNaoAtendido(double custoClienteNaoAtendido) {
        this.custoClienteNaoAtendido = custoClienteNaoAtendido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}