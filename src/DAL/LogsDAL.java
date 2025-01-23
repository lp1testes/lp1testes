package DAL;

import Controller.ConfiguracaoController;
import Controller.ReservaController;
import Controller.SimulacaoDiaController;
import Model.Configuracao;
import Model.Logs;
import Model.SimulacaoDia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogsDAL {

    private static LogsDAL instance;
    private Configuracao configuracao;
    private SimulacaoDiaController simulacaoDiaController;

    private LogsDAL() {
        ConfiguracaoController configuracaoController = ConfiguracaoController.getInstancia();
        this.configuracao = configuracaoController.getConfiguracao();
        this.simulacaoDiaController = SimulacaoDiaController.getInstance();
    }

    public static synchronized LogsDAL getInstance() {
        if (instance == null) {
            instance = new LogsDAL();
        }
        return instance;
    }

    public void adicionarLog(Logs log) {
        String fileName = getLogFileName(); // Obtém o nome do arquivo de log
        File file = new File(configuracao.getCaminhoFicheiros() + fileName);
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
        // Inicializar a simulação dia para obter o dia atual
        SimulacaoDia simulacaoDia = simulacaoDiaController.getSimulacaoDia();
        int diaAtual = simulacaoDia.getDia();
        String systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));


        String fileName = String.format("Logs_Dia%d_Sistema%s.txt", diaAtual, systemTime);
        return fileName;
    }
}