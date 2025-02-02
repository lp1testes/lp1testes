package Controller;

import DAL.LogsDAL;
import Utils.Logs;

public class LogsController {
    private static final int MAX_LOGS = 100; // Valor padrão para o tamanho máximo de logs
    private Logs[] logs;

    private static LogsController instance;
    private int logCount;

    // Construtor sem parâmetros, usando o valor padrão para o tamanho máximo
    LogsController() {
        this.logs = new Logs[MAX_LOGS];
        this.logCount = 0;
    }

    public static LogsController getInstance() {
        if (instance == null) {
            instance = new LogsController();
        }
        return instance;
    }

    // Construtor com parâmetro para definir o tamanho máximo
    public LogsController(int maxLogs) {
        this.logs = new Logs[maxLogs];
        this.logCount = 0;
    }

    public void criarLog(int dia, int hora, String logType, String logDescription) {
        Logs log = new Logs(dia, hora, logType, logDescription);
        LogsDAL logsDAL = LogsDAL.getInstance();
        logsDAL.adicionarLog(log);

        if (logCount < logs.length) {
            logs[logCount++] = log;
            System.out.println("Log adicionado ao array: " + log);
        } else {
            System.out.println("Log storage is full. Cannot add more logs.");
        }

        System.out.println("Log criado: " + log);
    }

    public Logs[] buscarLogsPorPeriodo(int diaInicio, int horaInicio, int diaFim, int horaFim) {
        int count = 0;

        // Primeiro, contamos quantos logs atendem aos critérios
        for (int i = 0; i < logCount; i++) {
            if (logEstaNoPeriodo(logs[i], diaInicio, horaInicio, diaFim, horaFim)) {
                count++;
            }
        }

        // Criamos um array com o tamanho exato
        Logs[] logsFiltrados = new Logs[count];
        int index = 0;

        // Adicionamos os logs que atendem aos critérios no array
        for (int i = 0; i < logCount; i++) {
            if (logEstaNoPeriodo(logs[i], diaInicio, horaInicio, diaFim, horaFim)) {
                logsFiltrados[index++] = logs[i];
            }
        }

        return logsFiltrados;
    }

    private boolean logEstaNoPeriodo(Logs log, int diaInicio, int horaInicio, int diaFim, int horaFim) {
        int logDia = log.getDay();
        int logHora = log.getHour();
        if (logDia < diaInicio || logDia > diaFim) {
            return false;
        }
        if (logDia == diaInicio && logHora < horaInicio) {
            return false;
        }
        if (logDia == diaFim && logHora > horaFim) {
            return false;
        }
        return true;
    }

    public Logs[] obterTodosLogs() {
        Logs[] logsAtuais = new Logs[logCount];
        System.arraycopy(logs, 0, logsAtuais, 0, logCount);
        return logsAtuais;
    }
}