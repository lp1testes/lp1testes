package Controller;

import DAL.*;
import Model.Configuracao;
import Model.SimulacaoDia;

public class ConfiguracaoController {

    private static ConfiguracaoController configuracaoController;
    private Configuracao configuracao;
    private ConfiguracaoDAL configuracaoDAL;
    private SimulacaoDia simulacaoDia;
    private MesaDAL mesaDAL;
    private PratoDAL pratoDAL;
    private MenuDAL menuDAL;
    private PedidoDAL pedidoDAL;
    private ReservaDAL reservaDAL;
    private DesempenhoFinanceiroDAL desempenhoFinanceiroDAL;
    private EstatisticaDAL estatisticaDAL;

    public static ConfiguracaoController getInstancia() {
        if (configuracaoController == null) configuracaoController = new ConfiguracaoController();
        return configuracaoController;
    }

    private ConfiguracaoController() {
        configuracaoDAL = new ConfiguracaoDAL();
        configuracao = new Configuracao();
        carregarConfiguracoes();
        simulacaoDia = new SimulacaoDia();
        mesaDAL = new MesaDAL(configuracao);
        pratoDAL = new PratoDAL();
        menuDAL = new MenuDAL();
        pedidoDAL = new PedidoDAL();
        reservaDAL = new ReservaDAL(configuracao);
        desempenhoFinanceiroDAL = new DesempenhoFinanceiroDAL();
        estatisticaDAL = new EstatisticaDAL();
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public int getUnidadesTempoDia() {
        return configuracao.getUnidadesTempoDia();
    }

    public void atualizarConfiguracao(Configuracao novaConfiguracao) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        configuracao = novaConfiguracao;
        salvarConfiguracoes();
    }

    private void carregarConfiguracoes() {
        configuracaoDAL.carregarConfiguracoes(configuracao);
    }

    public void alterarCaminhoFicheiros(String novoCaminho) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        configuracao.setCaminhoFicheiros(novoCaminho);
        salvarConfiguracoes();
    }

    public void alterarSeparadorFicheiros(String novoSeparador) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        configuracao.setSeparadorFicheiros(novoSeparador);
        salvarConfiguracoes();
    }

    public void alterarUnidadeTempoDia(String novaUnidadeTempoStr) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        try {
            int novaUnidadeTempo = Integer.parseInt(novaUnidadeTempoStr.trim());
            configuracao.setUnidadesTempoDia(novaUnidadeTempo);
            salvarConfiguracoes();
        } catch (NumberFormatException e) {
            System.err.println("O valor fornecido não é um número inteiro válido: " + novaUnidadeTempoStr);
        }
    }

    public void alterarUnidadeTempoIrParaMesa(String novaUnidadeTempoIrParaMesaStr) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        try {
            int novaUnidadeTempoIrParaMesa = Integer.parseInt(novaUnidadeTempoIrParaMesaStr.trim());
            configuracao.setUnidadesTempoIrParaMesa(novaUnidadeTempoIrParaMesa);
            salvarConfiguracoes();
        } catch (NumberFormatException e) {
            System.err.println("O valor fornecido não é um número inteiro válido: " + novaUnidadeTempoIrParaMesaStr);
        }
    }

    public void alterarUnidadeTempoParaPedido(String novaUnidadeTempoParaPedidoStr) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        try {
            int novaUnidadeTempoIrParaPedido = Integer.parseInt(novaUnidadeTempoParaPedidoStr.trim());
            configuracao.setUnidadesTempoParaPedido(novaUnidadeTempoIrParaPedido);
            salvarConfiguracoes();
        } catch (NumberFormatException e) {
            System.err.println("O valor fornecido não é um número inteiro válido: " + novaUnidadeTempoParaPedidoStr);
        }
    }

    public void alterarUnidadeTempoParaPagamento(String novaUnidadeTempoParaPagamentoStr) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        try {
            int novaUnidadeTempoParaPagamento = Integer.parseInt(novaUnidadeTempoParaPagamentoStr.trim());
            configuracao.setUnidadesTempoParaPagamento(novaUnidadeTempoParaPagamento);
            salvarConfiguracoes();
        } catch (NumberFormatException e) {
            System.err.println("O valor fornecido não é um número inteiro válido: " + novaUnidadeTempoParaPagamentoStr);
        }
    }

    public void alterarCustoClienteNaoAtendido(String novoCustoStr) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        try {
            double novoCusto = Double.parseDouble(novoCustoStr.trim());
            configuracao.setCustoClienteNaoAtendido(novoCusto);
            salvarConfiguracoes();
        } catch (NumberFormatException e) {
            System.err.println("O valor fornecido não é um número válido: " + novoCustoStr);
        }
    }

    public void alterarPassword(String novaPassword) {
        if (isDiaEmAndamento()) {
            System.err.println("Não é possível alterar a configuração enquanto um dia está em andamento.");
            return;
        }
        configuracao.setPassword(novaPassword);
        System.out.println(novaPassword);
        salvarConfiguracoes();
    }

    private void salvarConfiguracoes() {
        configuracaoDAL.salvarConfiguracoes(configuracao);
        carregarConfiguracoes();
        mesaDAL = new MesaDAL(configuracao);
    }

    private boolean isDiaEmAndamento() {
        return simulacaoDia.isAtivo();
    }
}