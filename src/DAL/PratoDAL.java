package DAL;

import Model.Prato;
import java.io.*;

public class PratoDAL {

    private static final String FILE_PATH = "resources/Pratos.txt";
    private static final int MAX_PRATOS = 100;

    public Prato[] carregarPratos() {
        Prato[] pratos = new Prato[MAX_PRATOS];
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return pratos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null && index < MAX_PRATOS) {
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    try {
                        Integer id = Integer.parseInt(parts[0].trim());
                        String nome = parts[1].trim();
                        String categoria = parts[2].trim();
                        double precoCusto = Double.parseDouble(parts[3].trim());
                        double precoVenda = Double.parseDouble(parts[4].trim());
                        int tempoPreparacao = Integer.parseInt(parts[5].trim());
                        boolean disponivel = Boolean.parseBoolean(parts[6].trim());
                        pratos[index++] = new Prato(id, nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);
                    } catch (NumberFormatException e) {
                        System.err.println("Linha mal configurada ignorada: " + line);
                    }
                } else {
                    System.err.println("Linha mal configurada ignorada: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pratos;
    }

    public void salvarPratos(Prato[] pratos) {
        File file = new File(FILE_PATH);
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Prato prato : pratos) {
                if (prato != null) {
                    bw.write(prato.getId() + ";" + prato.getNome() + ";" + prato.getCategoria() + ";" + prato.getPrecoCusto() + ";" + prato.getPrecoVenda() + ";" + prato.getTempoPreparacao() + ";" + prato.isDisponivel());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int obterProximoId(Prato[] pratos) {
        int maxId = 0;
        for (Prato prato : pratos) {
            if (prato != null && prato.getId() > maxId) {
                maxId = prato.getId();
            }
        }
        return maxId + 1;
    }
}