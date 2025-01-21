package DAL;

import Model.Configuracao;
import java.io.*;

public class ConfiguracaoDAL {

    private static final String FILE_PATH = "resources/Configuracoes.txt";

    public void carregarConfiguracoes(Configuracao configuracao) {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 2) {
                    configuracao.setSeparadorFicheiros(line.trim());
                } else {
                    String[] parts = line.split(";");
                    if (parts.length == 7) {
                        try {
                            configuracao.setCaminhoFicheiros(parts[0].trim());
                            configuracao.setUnidadesTempoDia(Integer.parseInt(parts[1].trim()));
                            configuracao.setUnidadesTempoIrParaMesa(Integer.parseInt(parts[2].trim()));
                            configuracao.setUnidadesTempoParaPedido(Integer.parseInt(parts[3].trim()));
                            configuracao.setUnidadesTempoParaPagamento(Integer.parseInt(parts[4].trim()));
                            configuracao.setCustoClienteNaoAtendido(Double.parseDouble(parts[5].trim()));
                            configuracao.setPassword(parts[6].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Configuração mal configurada ignorada: " + line);
                        }
                    } else {
                        System.err.println("Configuração mal configurada ignorada: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvarConfiguracoes(Configuracao configuracao) {
        File file = new File(FILE_PATH);
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(configuracao.getCaminhoFicheiros() + ";" + configuracao.getUnidadesTempoDia() + ";" + configuracao.getUnidadesTempoIrParaMesa() + ";" + configuracao.getUnidadesTempoParaPedido() + ";" + configuracao.getUnidadesTempoParaPagamento() + ";" + configuracao.getCustoClienteNaoAtendido() + ";" + configuracao.getPassword());
            bw.newLine();
            bw.write(configuracao.getSeparadorFicheiros());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}