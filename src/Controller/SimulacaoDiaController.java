package Controller;

import Model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import DAL.LogsDAL;

public class SimulacaoDiaController {
    private static SimulacaoDiaController instance;
    private final SimulacaoDia simulacaoDia;
    private final ConfiguracaoController configuracaoController;
    private final ReservaController reservaController;
    private final MesaController mesaController;
    private LogsController logsController;
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
        this.reservaController = ReservaController.getInstance(configuracao);
        this.mesaController = MesaController.getInstance(configuracao, reservaController);
        this.logsController = LogsController.getInstance(); // Usando a instância Singleton
    }

    public static synchronized SimulacaoDiaController getInstance() {
        if (instance == null) {
            instance = new SimulacaoDiaController();
        }
        return instance;
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
        logsController.criarLog(log.getDay(), log.getHour(), log.getLogType(), log.getLogDescription());

        return "\n Novo dia iniciado. Dia: " + simulacaoDia.getDia() + ", Unidade de Tempo Atual: " + simulacaoDia.getUnidadeTempoAtual();
    }

    public String avancarUnidadeTempo() {
        if (!simulacaoDia.isAtivo()) {
            return "\n Nenhum dia em andamento. Por favor, inicie um novo dia primeiro.";
        }
        if (simulacaoDia.getUnidadeTempoAtual() + 1 > configuracaoController.getConfiguracao().getUnidadesTempoDia()) {
            return "\n O dia só tem " + configuracaoController.getConfiguracao().getUnidadesTempoDia() + " tempos, não podes avançar mais!";
        } else {
            // Calcular o total faturado, total de gastos e lucro
            calcularTotalGanho();
            double lucro = totalGanho - prejuizoTotal;

            // Obter a data e hora do sistema
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);

            // Criar o log
            int currentDay = simulacaoDia.getDia();
            int currentHour = simulacaoDia.getUnidadeTempoAtual();
            String logType = "FINANCE";
            String logDescription = String.format("Avanço de unidade de tempo. Data: %s, Dia: %d, Unidade de Tempo Atual: %d, Perdas Totais: %.2f, Total Ganho: %.2f, Lucro: %.2f",
                    formattedNow, currentDay, currentHour, prejuizoTotal, totalGanho, lucro);

            Logs log = new Logs(currentDay, currentHour, logType, logDescription);
            logsController.criarLog(log.getDay(), log.getHour(), log.getLogType(), log.getLogDescription());

            // Avançar a unidade de tempo
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
        int unitsForAssignment = configuracao.getUnidadesTempoIrParaMesa();

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
                    mesaController.marcarMesaComoDisponivel(mesa.getId());
                    verificacoes.append("\nTempo limite para registrar o pedido expirou. Perdas Totais: ").append(reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido());
                }
            }
        }

        // Verificar reservas que não foram associadas a nenhuma mesa
        if (reservaController != null) {
            Reserva[] reservasNaoAssociadas = reservaController.listarReservasNaoAssociadas(tempoAtual);
            for (Reserva reserva : reservasNaoAssociadas) {
                if (!reservasNotificadas.contains(reserva.getId())) { // Verifica se a reserva já foi notificada
                    int tempoChegada = reserva.getTempoChegada();
                    int tempoLimite = tempoChegada + unitsForAssignment;

                    if (tempoAtual <= tempoLimite) {
                        // Reserva ainda dentro do tempo limite para ser associada a uma mesa
                        verificacoes.append("\nReserva ").append(reserva.getId()).append(" ainda está dentro do tempo limite para ser associada a uma mesa.");
                    } else {
                        // Tempo limite para associação expirou
                        prejuizoTotal += reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido();
                        verificacoes.append("\nTempo limite para associação expirou. Reserva ").append(reserva.getId()).append(" não foi associada a nenhuma mesa. Clientes foram embora. Perdas Totais: ").append(reserva.getNumeroPessoas() * configuracao.getCustoClienteNaoAtendido());
                        reservasNotificadas.add(reserva.getId()); // Adiciona a reserva à lista de notificadas
                    }
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
        // Calcular o lucro antes de encerrar o dia
        double lucro = totalGanho - prejuizoTotal;

        // Obter a data e hora do sistema
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        // Obter o número de pedidos atendidos e não atendidos
        int pedidosAtendidos = mesaController.contarPedidosAtendidos();
        int pedidosNaoAtendidos = mesaController.contarPedidosNaoAtendidos();

        // Criar o log
        int currentDay = simulacaoDia.getDia();
        int currentHour = simulacaoDia.getUnidadeTempoAtual();
        String logType = "FINANCE";
        String logDescription = String.format("Dia encerrado. Data: %s, Perdas Totais: %.2f, Total Ganho: %.2f, Lucro: %.2f, Pedidos Atendidos: %d, Pedidos Não Atendidos: %d",
                formattedNow, prejuizoTotal, totalGanho, lucro, pedidosAtendidos, pedidosNaoAtendidos);

        Logs log = new Logs(currentDay, currentHour, logType, logDescription);
        logsController.criarLog(log.getDay(), log.getHour(), log.getLogType(), log.getLogDescription());

        simulacaoDia.setUnidadeTempoAtual(0);
        simulacaoDia.setAtivo(false);

        // Calcular o total ganho antes de encerrar o dia
        calcularTotalGanho();

        return String.format("\nDia encerrado em %s. Perdas Totais: %.2f\nTotal Ganho: %.2f\nLucro: %.2f\nPedidos Atendidos: %d\nPedidos Não Atendidos: %d",
                formattedNow, prejuizoTotal, totalGanho, lucro, pedidosAtendidos, pedidosNaoAtendidos);
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