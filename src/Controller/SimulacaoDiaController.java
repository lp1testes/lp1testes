package Controller;

import Model.SimulacaoDia;

public class SimulacaoDiaController {
    private static SimulacaoDiaController instance;
    private final SimulacaoDia simulacaoDia;
    private final ConfiguracaoController configuracaoController;
    private final ReservaController reservaController;

    private SimulacaoDiaController() {
        this.configuracaoController = ConfiguracaoController.getInstancia();
        this.reservaController = new ReservaController(configuracaoController.getConfiguracao());
        this.simulacaoDia = new SimulacaoDia();
    }

    public static synchronized SimulacaoDiaController getInstance() {
        if (instance == null) {
            instance = new SimulacaoDiaController();
        }
        return instance;
    }

    public int getDiaAtualLogs() {
        return simulacaoDia.getDia() != null ? simulacaoDia.getDia() : 1; // Retornar 1 se for null
    }

    public int getUnidadeTempoAtualLogs() {
        return simulacaoDia.getUnidadeTempoAtual() != null ? simulacaoDia.getUnidadeTempoAtual() : 0; // Retornar 0 se for null
    }

    public String iniciarNovoDia() {
        if (simulacaoDia.isAtivo()) {
            return "\n O dia atual ainda está em andamento. Por favor, encerre o dia atual antes de iniciar um novo dia.";
        }
        int novoDia = (simulacaoDia.getDia() == null) ? 1 : simulacaoDia.getDia() + 1;
        simulacaoDia.setDia(novoDia);
        simulacaoDia.setUnidadeTempoAtual(1);
        simulacaoDia.setAtivo(true);

        return "\n Novo dia iniciado. Dia: " + simulacaoDia.getDia() + ", Unidade de Tempo Atual: " + simulacaoDia.getUnidadeTempoAtual();
    }

    public String avancarUnidadeTempo() {
        if (!simulacaoDia.isAtivo()) {
            return "\n Nenhum dia em andamento. Por favor, inicie um novo dia primeiro.";
        }
        if (simulacaoDia.getUnidadeTempoAtual() + 1 > configuracaoController.getConfiguracao().getUnidadesTempoDia()) {
            return "\n O dia só tem " + configuracaoController.getConfiguracao().getUnidadesTempoDia() + " tempos, não podes avançar mais!";
        } else {
            simulacaoDia.setUnidadeTempoAtual(simulacaoDia.getUnidadeTempoAtual() + 1);

            return "\n Unidade de Tempo Avançada. Unidade de Tempo Atual: " + simulacaoDia.getUnidadeTempoAtual();
        }
    }

    public String encerrarDia() {
        simulacaoDia.setUnidadeTempoAtual(0);
        simulacaoDia.setAtivo(false);

        return "\nDia encerrado.";
    }

    public SimulacaoDia getSimulacaoDia() {
        return simulacaoDia;
    }

    public String obterNotificacoes() {
        int tempoAtual = simulacaoDia.getUnidadeTempoAtual();
        StringBuilder notificacoes = new StringBuilder();
        notificacoes.append(reservaController.verificarChegadaReservas(tempoAtual));
        return notificacoes.toString();
    }
}