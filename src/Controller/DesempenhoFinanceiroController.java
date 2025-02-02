package Controller;

import Utils.Configuracao;
import Utils.Logs;

public class DesempenhoFinanceiroController {
    private LogsController logsController;
    private ConfiguracaoController configuracaoController;

    public DesempenhoFinanceiroController(LogsController logsController) {
        this.logsController = logsController;
        this.configuracaoController = ConfiguracaoController.getInstancia(); // Obtém a instância de ConfiguracaoController
    }

    public double calcularTotalFaturado(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs;
        if (range.length == 2) {
            // Consulta por dia
            logs = logsController.buscarLogsPorDia(range[0]);
            return processarUltimoLogPorTipo(logs, "Total Ganho");
        } else {
            // Consulta por período
            logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
            return processarSomaLogsPorTipo(logs, "Total Ganho");
        }
    }

    public double calcularTotalGastos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs;
        if (range.length == 2) {
            // Consulta por dia
            logs = logsController.buscarLogsPorDia(range[0]);
            return processarUltimoLogPorTipo(logs, "Perdas Totais");
        } else {
            // Consulta por período
            logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
            return processarSomaLogsPorTipo(logs, "Perdas Totais");
        }
    }

    public double calcularTotalLucro(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs;
        if (range.length == 2) {
            // Consulta por dia
            logs = logsController.buscarLogsPorDia(range[0]);
            return processarUltimoLogPorTipo(logs, "Lucro");
        } else {
            // Consulta por período
            logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
            return processarSomaLogsPorTipo(logs, "Lucro");
        }
    }

    public int calcularNumeroPedidosAtendidos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0;

        Logs[] logs;
        if (range.length == 2) {
            // Consulta por dia
            logs = logsController.buscarLogsPorDia(range[0]);
        } else {
            // Consulta por período
            logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        }

        int numeroPedidosAtendidos = 0;
        for (Logs log : logs) {
            if (log.getLogType().equals("ACTION") && log.getLogDescription().startsWith("Pagamento efetuado para a mesa")) {
                numeroPedidosAtendidos++;
            }
        }
        return numeroPedidosAtendidos;
    }

    public int calcularNumeroPedidosNaoAtendidos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0;

        Logs[] logs;
        if (range.length == 2) {
            // Consulta por dia
            logs = logsController.buscarLogsPorDia(range[0]);
        } else {
            // Consulta por período
            logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        }

        int numeroPedidosNaoAtendidos = 0;
        for (Logs log : logs) {
            if (log.getLogType().equals("ACTION") && log.getLogDescription().startsWith("Pedido não atendido para a mesa")) {
                numeroPedidosNaoAtendidos++;
            }
        }
        return numeroPedidosNaoAtendidos;
    }

    private double processarUltimoLogPorTipo(Logs[] logs, String chave) {
        for (int i = logs.length - 1; i >= 0; i--) {
            if (logs[i].getLogType().equals("FINANCE")) {
                return extrairValorDoLog(logs[i], chave);
            }
        }
        return 0.0;
    }

    private double processarSomaLogsPorTipo(Logs[] logs, String chave) {
        double soma = 0.0;
        for (Logs log : logs) {
            if (log.getLogType().equals("FINANCE")) {
                soma += extrairValorDoLog(log, chave);
            }
        }
        return soma;
    }

    private double extrairValorDoLog(Logs log, String chave) {
        String descricao = log.getLogDescription();
        String[] partes = descricao.split(", ");
        for (String parte : partes) {
            if (parte.contains(chave)) {
                String valorStr = parte.split(": ")[1];
                // Substitui a vírgula pelo ponto
                valorStr = valorStr.replace(",", ".");
                return Double.parseDouble(valorStr);
            }
        }
        return 0.0;
    }

    private int[] parsePeriodo(String periodo) {
        try {
            // Obter a configuração para a unidade de tempo do dia
            Configuracao configuracao = configuracaoController.getConfiguracao();
            int unidadesTempoDia = configuracao.getUnidadesTempoDia();

            String[] datas = periodo.split(" to ");
            if (datas.length == 1) {
                // Apenas o dia foi fornecido
                int dia = Integer.parseInt(datas[0]);
                return new int[]{dia, unidadesTempoDia}; // Considera todas as horas do dia
            } else {
                // Um período completo foi fornecido
                String[] inicio = datas[0].split(" ");
                String[] fim = datas[1].split(" ");

                int diaInicio = Integer.parseInt(inicio[0]);
                int horaInicio = inicio.length > 1 ? Integer.parseInt(inicio[1]) : 0; // Hora inicial padrão: 0
                int diaFim = Integer.parseInt(fim[0]);
                int horaFim = fim.length > 1 ? Integer.parseInt(fim[1]) : unidadesTempoDia; // Hora final padrão

                return new int[]{diaInicio, horaInicio, diaFim, horaFim};
            }
        } catch (Exception e) {
            System.out.println("Formato de período inválido! Use o formato 'dia' ou 'dia to dia' ou 'dia hora to dia hora'.");
            return null;
        }
    }
}