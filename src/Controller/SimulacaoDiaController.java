package Controller;
import Model.*;
import DAL.LogsDAL;
import Model.Configuracao;
import Model.SimulacaoDia;
import Model.Logs;

import java.util.HashSet;
import java.util.Set;

public class SimulacaoDiaController {
    private static SimulacaoDiaController instance;
    private final SimulacaoDia simulacaoDia;
    private final ConfiguracaoController configuracaoController;
    private ReservaController reservaController;
    private LogsDAL logsDAL;
    private MesaController mesaController;
    private double prejuizoTotal;
    private double totalGanho;
    private Set<Integer> reservasNotificadas;


    private SimulacaoDiaController() {
        this.configuracaoController = ConfiguracaoController.getInstancia();
        this.simulacaoDia = new SimulacaoDia();
        this.prejuizoTotal = 0.0;
        this.totalGanho = 0.0;
        this.reservasNotificadas = new HashSet<>();

        Configuracao configuracao = configuracaoController.getConfiguracao();
        this.mesaController = MesaController.getInstance(configuracao, null);
    }

    public static synchronized SimulacaoDiaController getInstance() {
        if (instance == null) {
            instance = new SimulacaoDiaController();
        }
        return instance;
    }

    private LogsDAL getLogsDAL() {
        if (logsDAL == null) {
            logsDAL = LogsDAL.getInstance();
        }
        return logsDAL;
    }

    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;
        this.mesaController.setReservaController(reservaController);
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

        // Adicionar log para o novo dia
        Logs log = new Logs(novoDia, 1, "INFO", "Novo dia iniciado");
        getLogsDAL().adicionarLog(log);

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
            String verificacoes = verificarPagamentosEReservasExpiradas();
            return "\n Unidade de Tempo Avançada. Unidade de Tempo Atual: " + simulacaoDia.getUnidadeTempoAtual() + verificacoes + "\nPerdas Totais: " + prejuizoTotal;
        }
    }

    private void calcularTotalGanho() {
        totalGanho = 0; // Reinicializa o total ganho
        for (Mesa mesa : mesaController.getMesas()) {
            if (mesa == null) continue;

            Pedido pedido = mesaController.getPedidoByMesa(mesa.getId());
            if (pedido != null && pedido.isPago()) {
                totalGanho += pedido.getTotalVenda(); // Incrementa o total ganho
            }
        }
    }

    private String verificarPagamentosEReservasExpiradas() {
        StringBuilder verificacoes = new StringBuilder();
        int tempoAtual = simulacaoDia.getUnidadeTempoAtual();
        Configuracao configuracao = configuracaoController.getConfiguracao();

        for (Mesa mesa : mesaController.getMesas()) {
            if (mesa == null || !mesa.isOcupada()) continue;

            Pedido pedido = mesaController.getPedidoByMesa(mesa.getId());
            if (pedido != null && !pedido.isPago()) {
                if (mesaController.tempoPagamentoUltrapassado(mesa.getId(), tempoAtual)) {
                    prejuizoTotal += pedido.getTotalCusto();
                    mesaController.removerReservaDaMesa(mesa.getId());
                    mesaController.marcarMesaComoDisponivel(mesa.getId());
                    verificacoes.append("\nTempo limite para pagamento expirou. Clientes da mesa ").append(mesa.getId()).append(" foram embora. Perdas Totais: ").append(pedido.getTotalCusto());
                }
            }

            Reserva reserva = mesaController.getClienteDaMesa(mesa.getId());
            if (reserva != null) {
                if (mesaController.tempoPedidoUltrapassado(mesa.getId(), tempoAtual) && (pedido == null || (pedido.getPratos().isEmpty() && pedido.getMenus().isEmpty()))) {
                    prejuizoTotal += reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido();
                    mesaController.removerReservaDaMesa(mesa.getId());
                    verificacoes.append("\nTempo limite para registrar o pedido expirou. Perdas Totais: ").append(reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido());
                }
            }
        }

        // Verificar reservas que não foram associadas a nenhuma mesa
        if (reservaController != null) {
            Reserva[] reservasNaoAssociadas = reservaController.listarReservasNaoAssociadas(tempoAtual);
            for (Reserva reserva : reservasNaoAssociadas) {
                if (!reservasNotificadas.contains(reserva.getId())) { // Verifica se a reserva já foi notificada
                    prejuizoTotal += reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido();
                    verificacoes.append("\nReserva ").append(reserva.getId()).append(" não foi associada a nenhuma mesa. Clientes foram embora. Perdas Totais: ").append(reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido());
                    reservasNotificadas.add(reserva.getId()); // Adiciona a reserva à lista de notificadas
                }
            }
        }

        // Calcular o total ganho a partir dos pedidos pagos
        calcularTotalGanho();

        // Calcular o lucro
        double lucro = totalGanho - prejuizoTotal;
        verificacoes.append("\nTotal Ganho: ").append(totalGanho);
        verificacoes.append("\nPerdas Totais: ").append(prejuizoTotal);
        verificacoes.append("\nLucro: ").append(lucro);

        return verificacoes.toString();
    }

    public String encerrarDia() {
        simulacaoDia.setUnidadeTempoAtual(0);
        simulacaoDia.setAtivo(false);

        // Calcular o total ganho antes de encerrar o dia
        calcularTotalGanho();

        // Calcular o lucro antes de encerrar o dia
        double lucro = totalGanho - prejuizoTotal;
        return "\nDia encerrado. Perdas Totais: " + prejuizoTotal + "\nTotal Ganho: " + totalGanho + "\nLucro: " + lucro;
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

    public boolean diaJaComecou() {
        return simulacaoDia.getDia() > 0;
    }

    public double getPerdasTotais() {
        return prejuizoTotal;
    }

    public double getTotalGanho() {
        return totalGanho;
    }

    public double getLucro() {
        return totalGanho - prejuizoTotal;
    }
}