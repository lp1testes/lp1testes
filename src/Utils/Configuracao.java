package Utils;

public class Configuracao {

    private static Configuracao instancia;
    private String caminhoFicheiros;
    private String separadorFicheiros;
    private int unidadesTempoDia;
    private int unidadesTempoIrParaMesa;
    private int unidadesTempoParaPedido;
    private int unidadesTempoParaPagamento;
    private double custoClienteNaoAtendido;
    private String password;

    private Configuracao() {
    }

    public static Configuracao getInstancia(){
        if(instancia == null){
            instancia = new Configuracao();
        }
        return instancia;
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