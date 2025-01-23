package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs {
    private int day;                   // Dia definido pelo programa
    private int hour;                  // Hora definida pelo programa
    private LocalDateTime systemTime;  // Hora do computador
    private String logType;            // Tipo do log (ex: INFO, ERROR, ACTION, FINANCE)
    private String logDescription;     // Descrição do log

    public Logs(int day, int hour, String logType, String logDescription) {
        this.day = day;
        this.hour = hour;
        this.systemTime = LocalDateTime.now();
        this.logType = logType;
        this.logDescription = logDescription;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public LocalDateTime getSystemTime() {
        return systemTime;
    }

    public String getLogType() {
        return logType;
    }

    public String getLogDescription() {
        return logDescription;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Logs{" +
                "day=" + day +
                ", hour=" + hour +
                ", systemTime=" + systemTime.format(formatter) +
                ", logType='" + logType + '\'' +
                ", logDescription='" + logDescription + '\'' +
                '}';
    }}

    // Exemplo de uso da classe Logs:
    /**
    public static void main(String[] args) {
        // Criando logs de exemplo com dia e hora definidos pelo programa
        Logs log1 = new Logs(1, 1, "INFO", "Cliente foi atendido na mesa 5");
        Logs log2 = new Logs(1, 2, "ERROR", "Falha ao processar pagamento na mesa 3");
        Logs log3 = new Logs(1, 3, "ACTION", "Novo prato adicionado ao menu: Lasanha");

        // Log com informações financeiras detalhadas
        double custoTotal = 50.0;
        double precoTotal = 100.0;
        double lucro = precoTotal - custoTotal;
        String descricaoFinanceira = String.format("Pedido finalizado. Custo total: %.2f, Preço total: %.2f, Lucro: %.2f", custoTotal, precoTotal, lucro);
        Logs logFinanceiro = new Logs(1, 4, "FINANCE", descricaoFinanceira);

        // Exibindo os logs
        System.out.println(log1);
        System.out.println(log2);
        System.out.println(log3);
        System.out.println(logFinanceiro);
    }
*/