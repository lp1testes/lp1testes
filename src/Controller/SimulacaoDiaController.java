package Controller;

import Model.SimulacaoDia;

public class SimulacaoDiaController {
    private static SimulacaoDiaController instance;
    private final SimulacaoDia simulacaoDia;
    private final ConfiguracaoController configuracaoController;
    private ReservaController reservaController;

    private SimulacaoDiaController() {
        this.configuracaoController = ConfiguracaoController.getInstancia();
        this.simulacaoDia = new SimulacaoDia();
    }

    public static synchronized SimulacaoDiaController getInstance() {
        if (instance == null) {
            instance = new SimulacaoDiaController();
        }
        return instance;
    }

    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;
    }

    public int getDiaAtual() {
        return simulacaoDia.getDia() != null ? simulacaoDia.getDia() : 1;
    }

    public int getUnidadeTempoAtual() {
        return simulacaoDia.getUnidadeTempoAtual() != null ? simulacaoDia.getUnidadeTempoAtual() : 0;
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
        if (reservaController != null) {
            notificacoes.append(reservaController.verificarChegadaReservas(tempoAtual));
        }
        return notificacoes.toString();
    }
}