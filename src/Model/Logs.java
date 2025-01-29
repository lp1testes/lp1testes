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
    }
}