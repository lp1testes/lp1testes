package Controller;

import Utils.Logs;

public class DesempenhoFinanceiroController {
    private LogsController logsController;

    public DesempenhoFinanceiroController(LogsController logsController) {
        this.logsController = logsController;
    }

    public double calcularTotalFaturado(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        double totalFaturado = 0.0;
        if (logs.length > 0) {
            for (int i = logs.length - 1; i >= 0; i--) {
                if (logs[i].getLogType().equals("FINANCE")) {
                    totalFaturado = extrairValorDoLog(logs[i], "Total Ganho");
                    break;
                }
            }
        }
        return totalFaturado;
    }

    public double calcularTotalGastos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        double totalGastos = 0.0;
        if (logs.length > 0) {
            for (int i = logs.length - 1; i >= 0; i--) {
                if (logs[i].getLogType().equals("FINANCE")) {
                    totalGastos = extrairValorDoLog(logs[i], "Perdas Totais");
                    break;
                }
            }
        }
        return totalGastos;
    }

    public double calcularTotalLucro(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0.0;

        Logs[] logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        double totalLucro = 0.0;
        if (logs.length > 0) {
            for (int i = logs.length - 1; i >= 0; i--) {
                if (logs[i].getLogType().equals("FINANCE")) {
                    totalLucro = extrairValorDoLog(logs[i], "Lucro");
                    break;
                }
            }
        }
        return totalLucro;
    }

    public int calcularNumeroPedidosAtendidos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0;

        Logs[] logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        int numeroPedidosAtendidos = 0;
        for (Logs log : logs) {
            if (log.getLogType().equals("FINANCE")) {
                numeroPedidosAtendidos += extrairValorDoLog(log, "Pedidos Atendidos").intValue();
            }
        }
        return numeroPedidosAtendidos;
    }

    public int calcularNumeroPedidosNaoAtendidos(String periodo) {
        int[] range = parsePeriodo(periodo);
        if (range == null) return 0;

        Logs[] logs = logsController.buscarLogsPorPeriodo(range[0], range[1], range[2], range[3]);
        int numeroPedidosNaoAtendidos = 0;
        for (Logs log : logs) {
            if (log.getLogType().equals("FINANCE")) {
                numeroPedidosNaoAtendidos += extrairValorDoLog(log, "Pedidos Não Atendidos").intValue();
            }
        }
        return numeroPedidosNaoAtendidos;
    }

    private Double extrairValorDoLog(Logs log, String chave) {
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
            String[] datas = periodo.split(" to ");
            if (datas.length == 1) {
                // Apenas o dia foi fornecido
                int dia = Integer.parseInt(datas[0]);
                return new int[]{dia, 0, dia, 23}; // Considera todas as horas do dia
            } else {
                // Um período completo foi fornecido
                String[] inicio = datas[0].split(" ");
                String[] fim = datas[1].split(" ");

                int diaInicio = Integer.parseInt(inicio[0]);
                int horaInicio = inicio.length > 1 ? Integer.parseInt(inicio[1]) : 0; // Hora inicial padrão: 0
                int diaFim = Integer.parseInt(fim[0]);
                int horaFim = fim.length > 1 ? Integer.parseInt(fim[1]) : 23; // Hora final padrão: 23

                return new int[]{diaInicio, horaInicio, diaFim, horaFim};
            }
        } catch (Exception e) {
            System.out.println("Formato de período inválido! Use o formato 'dia' ou 'dia to dia' ou 'dia hora to dia hora'.");
            return null;
        }
    }
}