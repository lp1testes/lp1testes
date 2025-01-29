package DAL;

import Controller.ConfiguracaoController;
import Controller.SimulacaoDiaController;
import Model.Configuracao;
import Model.Logs;
import Model.SimulacaoDia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class LogsDAL {

    private static LogsDAL instance;
    private Configuracao configuracao;
    private SimulacaoDiaController simulacaoDiaController;

    private LogsDAL() {
        ConfiguracaoController configuracaoController = ConfiguracaoController.getInstancia();
        this.configuracao = configuracaoController.getConfiguracao();
    }

    public static synchronized LogsDAL getInstance() {
        if (instance == null) {
            instance = new LogsDAL();
        }
        return instance;
    }

    private SimulacaoDiaController getSimulacaoDiaController() {
        if (simulacaoDiaController == null) {
            simulacaoDiaController = SimulacaoDiaController.getInstance();
        }
        return simulacaoDiaController;
    }

    public void adicionarLog(Logs log) {
        String fileName = getLogFileName(); // Obtém o nome do arquivo de log
        File file = new File(configuracao.getCaminhoFicheiros() + "Logs/" + fileName);
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(log.getDay() + configuracao.getSeparadorFicheiros() + log.getHour() + configuracao.getSeparadorFicheiros() +
                    log.getSystemTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + configuracao.getSeparadorFicheiros() +
                    log.getLogType() + configuracao.getSeparadorFicheiros() + log.getLogDescription());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLogFileName() {
        SimulacaoDia simulacaoDia = getSimulacaoDiaController().getSimulacaoDia();
        String fileName;
        if (simulacaoDia.isAtivo()) {
            int diaAtual = simulacaoDia.getDia();
            fileName = String.format("Logs_Dia%d.txt", diaAtual);
        } else {
            fileName = "Geral_Log.txt";
        }
        return fileName;
    }
}