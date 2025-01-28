package View;

import Controller.LogsController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LogsView {

    private LogsController logsController;

    public LogsView() {
        logsController = new LogsController();
    }

    public void exibirMenuLogsEventos(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Logs e Eventos --");
            System.out.println("1. Consultar Logs de um Dia Específico");
            System.out.println("2. Consultar Logs de Todos os Dias");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 2);

            switch (opcao) {
                case 1:
                    consultarLogsDiaEspecifico(scanner);
                    break;
                case 2:
                    consultarLogsTodosDias(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void consultarLogsDiaEspecifico(Scanner scanner) {
        File logDirectory = new File("Resources/Logs/");
        System.out.println("\nDiretório de logs: " + logDirectory.getAbsolutePath());

        File[] logFiles = logDirectory.listFiles((dir, name) -> name.startsWith("Logs_Dia") || name.equals("Geral_Log.txt"));

        if (logFiles != null && logFiles.length > 0) {
            System.out.println("Logs disponíveis:");
            for (int i = 0; i < logFiles.length; i++) {
                System.out.println((i + 1) + ". " + logFiles[i].getName());
            }

            System.out.print("Selecione o log pelo número: ");
            int logIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (logIndex >= 0 && logIndex < logFiles.length) {
                File selectedLogFile = logFiles[logIndex];
                System.out.println("Conteúdo do log " + selectedLogFile.getName() + ":");
                try (BufferedReader br = new BufferedReader(new FileReader(selectedLogFile))) {
                    String linha;
                    while ((linha = br.readLine()) != null) {
                        System.out.println(linha);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Seleção inválida.");
            }
        } else {
            System.out.println("Nenhum log encontrado.");
        }
    }

    private void consultarLogsTodosDias(Scanner scanner) {
        File logDirectory = new File("Resources/Logs/");
        System.out.println("\nDiretório de logs: " + logDirectory.getAbsolutePath());

        File[] logFiles = logDirectory.listFiles();

        if (logFiles != null && logFiles.length > 0) {
            System.out.println("Todos os logs disponíveis:");
            for (File logFile : logFiles) {
                System.out.println(logFile.getName());
            }
        } else {
            System.out.println("Nenhum log encontrado.");
        }
    }
}