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
            int proximoId = 1;
            while ((line = br.readLine()) != null && index < MAX_PRATOS) {
                String[] parts = line.split(";");
                if (parts.length == 7 || parts.length == 6) {
                    try {
                        int id;
                        String nome;
                        String categoria;
                        double precoCusto;
                        double precoVenda;
                        int tempoPreparacao;
                        boolean disponivel;

                        if (parts.length == 7) {
                            id = Integer.parseInt(parts[0].trim());
                            nome = parts[1].trim();
                            categoria = parts[2].trim();
                            precoCusto = Double.parseDouble(parts[3].trim());
                            precoVenda = Double.parseDouble(parts[4].trim());
                            tempoPreparacao = Integer.parseInt(parts[5].trim());
                            disponivel = Boolean.parseBoolean(parts[6].trim());
                        } else {
                            id = proximoId++;
                            nome = parts[0].trim();
                            categoria = parts[1].trim();
                            precoCusto = Double.parseDouble(parts[2].trim());
                            precoVenda = Double.parseDouble(parts[3].trim());
                            tempoPreparacao = Integer.parseInt(parts[4].trim());
                            disponivel = Boolean.parseBoolean(parts[5].trim());
                        }

                        if (id >= proximoId) {
                            proximoId = id + 1;
                        }

                        pratos[index++] = new Prato(id, nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);
                    } catch (NumberFormatException e) {
                        System.err.println("Erro na formatação dos números na linha: " + line);
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