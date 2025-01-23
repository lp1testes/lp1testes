package Controller;

import DAL.LogsDAL;
import Model.Logs;

public class LogsController {

    public void criarLog(int dia, int hora, String logType, String logDescription) {
        Logs log = new Logs(dia, hora, logType, logDescription);
        LogsDAL logsDAL = LogsDAL.getInstance();
        logsDAL.adicionarLog(log);
        System.out.println("Log criado: " + log);
    }
}