package Controller;

import Model.Logs;
import Model.Prato;
import Model.Reserva;
import Model.Configuracao;
import java.util.Arrays;

public class EstatisticaController {
    private LogsController logsController;
    private PratoController pratoController;
    private ReservaController reservaController;

    public EstatisticaController(Configuracao configuracao) {
        this.logsController = LogsController.getInstance();
        this.pratoController = new PratoController();
        this.reservaController = ReservaController.getInstance(configuracao);
    }

    public Prato mostrarPratoMaisPedido() {
        Logs[] logs = logsController.obterTodosLogs();
        Prato[] pratos = pratoController.getPratos();

        // Imprimir todos os logs
        System.out.println("----- Todos os Logs -----");
        for (Logs log : logs) {
            if (log != null) {
                System.out.println(log);
            }
        }
        System.out.println("----- Fim dos Logs -----");

        if (pratos == null || pratos.length == 0) {
            System.out.println("Não há pratos disponíveis.");
            return null;
        }

        int[] pratoContagem = new int[pratos.length]; // Array para contar os pedidos de cada prato

        // Contar a frequência de cada prato nos logs
        for (Logs log : logs) {
            if (log != null && log.getLogType().equals("ACTION") && log.getLogDescription().contains("Pratos consumidos:")) {
                String descricao = log.getLogDescription();
                String[] nomesPratos = obterNomesPratosDaDescricao(descricao);

                for (String nomePrato : nomesPratos) {
                    for (int i = 0; i < pratos.length; i++) {
                        if (pratos[i] != null && pratos[i].getNome().equals(nomePrato.trim())) {
                            pratoContagem[i]++;
                        }
                    }
                }
            }
        }

        // Encontrar o prato mais pedido
        Prato pratoMaisPedido = null;
        int maxPedidos = 0;
        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] != null && pratoContagem[i] > maxPedidos) {
                pratoMaisPedido = pratos[i];
                maxPedidos = pratoContagem[i];
            }
        }

        if (pratoMaisPedido != null) {
            System.out.println("Total de pedidos do prato mais pedido: " + maxPedidos);
        }

        return pratoMaisPedido;
    }
    public double calcularTempoMedioEsperaMesa() {
        Reserva[] reservas = reservaController.getReservas();
        if (reservas == null || reservas.length == 0) {
            System.out.println("Não há reservas disponíveis.");
            return 0;
        }

        // Imprimir as reservas disponíveis
        System.out.println("Reservas disponíveis:");
        for (Reserva reserva : reservas) {
            if (reserva != null) {
                System.out.println("ID: " + reserva.getId() +
                        ", Nome: " + reserva.getNome() +
                        ", Tempo de Chegada: " + reserva.getTempoChegada());
            } else {
                System.out.println("Reserva nula encontrada.");
            }
        }

        int[] temposEspera = new int[reservas.length];
        int count = 0;
        Logs[] logs = logsController.obterTodosLogs();

        for (Reserva reserva : reservas) {
            if (reserva == null) continue;

            Integer tempoChegada = reserva.getTempoChegada();
            Integer tempoMesa = null;

            for (Logs log : logs) {
                if (log.getLogType().equals("ACTION") && log.getLogDescription().contains("Clientes da reserva") && log.getLogDescription().contains("foram atribuídos à mesa")) {
                    String descricao = log.getLogDescription();
                    if (descricao.contains("ID: " + reserva.getId())) {
                        tempoMesa = log.getHour();
                        break;
                    }
                }
            }

            if (tempoChegada != null && tempoMesa != null) {
                temposEspera[count] = tempoMesa - tempoChegada;
                count++;
            }
        }

        return calcularMedia(Arrays.copyOf(temposEspera, count));
    }

    public double calcularTempoMedioServirMesa() {
        Logs[] logs = logsController.obterTodosLogs();
        int[] temposServir = new int[logs.length];
        int count = 0;

        // Percorrer os logs para encontrar os tempos de associação e pagamento
        for (Logs log : logs) {
            if (log == null) continue;

            if (log.getLogType().equals("ACTION") && log.getLogDescription().contains("Clientes da reserva") && log.getLogDescription().contains("foram atribuídos à mesa")) {
                String descricao = log.getLogDescription();
                String[] partes = descricao.split("ID: ");
                int idMesa = Integer.parseInt(partes[1].split(" ")[0]);
                int tempoAssociacao = log.getHour();

                // Procurar o log de pagamento correspondente
                for (Logs logPagamento : logs) {
                    if (logPagamento.getLogType().equals("ACTION") && logPagamento.getLogDescription().contains("Pagamento efetuado para a mesa ID: " + idMesa)) {
                        int tempoPagamento = logPagamento.getHour();
                        temposServir[count] = tempoPagamento - tempoAssociacao;
                        count++;
                        break;
                    }
                }
            }
        }

        return calcularMedia(Arrays.copyOf(temposServir, count));
    }

    private double calcularMedia(int[] tempos) {
        if (tempos.length == 0) return 0;

        int soma = 0;
        for (int tempo : tempos) {
            soma += tempo;
        }

        return (double) soma / tempos.length;
    }

    private String[] obterNomesPratosDaDescricao(String descricao) {
        // Implementar lógica para extrair os nomes dos pratos da descrição do log
        // Exemplo: "Pagamento efetuado para a mesa ID: 3. Pratos consumidos: Atum com Natas, aa, Atum com Natas, Atum com Natas, Atum com Natas"
        if (descricao.contains("Pratos consumidos:")) {
            String[] partes = descricao.split("Pratos consumidos:");
            if (partes.length > 1) {
                String pratosStr = partes[1].trim();
                return pratosStr.split(",");
            }
        }
        return new String[0];
    }
}