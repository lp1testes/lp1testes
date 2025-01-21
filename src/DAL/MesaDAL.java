package DAL;

import Model.Mesa;
import Model.Configuracao;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class MesaDAL {

    private Configuracao configuracao;
    private static final int MAX_MESAS = 100;

    public MesaDAL(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    public Mesa[] carregarMesas() {
        Mesa[] mesas = new Mesa[MAX_MESAS];
        File file = new File(configuracao.getCaminhoFicheiros() + "Mesas.txt");

        if (!file.exists()) {
            return mesas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null && index < MAX_MESAS) {
                String[] parts = line.split(configuracao.getSeparadorFicheiros());
                if (parts.length == 3) {
                    try {
                        Integer id = Integer.parseInt(parts[0].trim());
                        int capacidade = Integer.parseInt(parts[1].trim());
                        boolean ocupada = Boolean.parseBoolean(parts[2].trim());
                        mesas[index++] = new Mesa(id, capacidade, ocupada);
                    } catch (NumberFormatException e) {
                        System.err.println("Mesa mal configurada ignorada: " + line);
                    }
                } else {
                    System.err.println("Mesa mal configurada ignorada: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mesas;
    }

    public void salvarMesas(Mesa[] mesas) {
        File file = new File(configuracao.getCaminhoFicheiros() + "Mesas.txt");
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Mesa mesa : mesas) {
                if (mesa != null) {
                    bw.write(mesa.getId() + configuracao.getSeparadorFicheiros() + mesa.getCapacidade() + configuracao.getSeparadorFicheiros() + mesa.isOcupada());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int obterProximoId(Mesa[] mesas) {
        Set<Integer> idsExistentes = new HashSet<>();
        for (Mesa mesa : mesas) {
            if (mesa != null) {
                idsExistentes.add(mesa.getId());
            }
        }

        int proximoId = 1;
        while (idsExistentes.contains(proximoId)) {
            proximoId++;
        }
        return proximoId;
    }
}