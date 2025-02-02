package DAL;

import Utils.Configuracao;
import Model.Reserva;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ReservaDAL {

    private static final int MAX_RESERVAS = 100;
    private static final Configuracao configuracao = Configuracao.getInstancia();

    public ReservaDAL() {
    }

    public Reserva[] carregarReservas() {
        Reserva[] reservas = new Reserva[MAX_RESERVAS];
        File file = new File(configuracao.getCaminhoFicheiros() + "Reservas.txt");

        if (!file.exists()) {
            return reservas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null && index < MAX_RESERVAS) {
                String[] parts = line.split(configuracao.getSeparadorFicheiros());
                if (parts.length == 3) {
                    try {
                        Integer id = obterProximoId(reservas);
                        String nome = parts[0].trim();
                        int numeroPessoas = Integer.parseInt(parts[1].trim());
                        int tempoChegada = Integer.parseInt(parts[2].trim());
                        reservas[index++] = new Reserva(id, nome, numeroPessoas, tempoChegada);
                    } catch (NumberFormatException e) {
                        System.err.println("Reserva mal configurada ignorada: " + line);
                    }
                } else {
                    System.err.println("Reserva mal configurada ignorada: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    public void salvarReservas(Reserva[] reservas) {
        File file = new File(configuracao.getCaminhoFicheiros() + "Reservas.txt");
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Reserva reserva : reservas) {
                if (reserva != null) {
                    bw.write(reserva.getNome() + configuracao.getSeparadorFicheiros() + reserva.getNumeroPessoas() + configuracao.getSeparadorFicheiros() + reserva.getTempoChegada());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int obterProximoId(Reserva[] reservas) {
        Set<Integer> idsExistentes = new HashSet<>();
        for (Reserva reserva : reservas) {
            if (reserva != null) {
                idsExistentes.add(reserva.getId());
            }
        }

        int proximoId = 1;
        while (idsExistentes.contains(proximoId)) {
            proximoId++;
        }
        return proximoId;
    }
}