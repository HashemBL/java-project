import java.time.LocalDateTime;

/**
 * An alert generated when a sensor reading exceeds its configured threshold.
 * Each alert is linked to the reading that triggered it.
 */
public class Alert implements Comparable<Alert> {
    private static int counter = 0;

    private final int id;
    private final String sensorCode;
    private final String zoneCode;
    private final Reading triggeringReading;
    private final SeverityLevel severity;
    private final LocalDateTime createdAt;
    private AlertStatus status;
    private String message;

    public Alert(String sensorCode, String zoneCode, Reading triggeringReading, SeverityLevel severity, String message) {
        this.id = ++counter;
        this.sensorCode = sensorCode;
        this.zoneCode = zoneCode;
        this.triggeringReading = triggeringReading;
        this.severity = severity;
        this.createdAt = LocalDateTime.now();
        this.status = AlertStatus.ACTIVE;
        this.message = message;
    }

    public void acknowledge() {
        this.status = AlertStatus.ACKNOWLEDGED;
    }

    public void dismiss() {
        this.status = AlertStatus.DISMISSED;
    }

    /** Sort by severity descending (CRITICAL first), then by creation time. */
    @Override
    public int compareTo(Alert other) {
        int severityCompare = other.severity.ordinal() - this.severity.ordinal();
        if (severityCompare != 0) return severityCompare;
        return this.createdAt.compareTo(other.createdAt);
    }

    public int getId()                     { return id; }
    public String getSensorCode()          { return sensorCode; }
    public String getZoneCode()            { return zoneCode; }
    public Reading getTriggeringReading()  { return triggeringReading; }
    public SeverityLevel getSeverity()     { return severity; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public AlertStatus getStatus()         { return status; }
    public String getMessage()             { return message; }

    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "Alert#" + id + " [" + severity + "] Zone=" + zoneCode
                + " Sensor=" + sensorCode + " | " + message + " | " + status;
    }
}
