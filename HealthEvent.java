import java.time.LocalDateTime;

/**
 * Records a health-related event for an animal (disease, weight change, etc.).
 */
public class HealthEvent implements Comparable<HealthEvent> {
    private LocalDateTime timestamp;
    private HealthEventType type;
    private String description;
    private double weightAtEvent; // 0 if not a weight-related event

    public HealthEvent(LocalDateTime timestamp, HealthEventType type, String description, double weightAtEvent) {
        this.timestamp = timestamp;
        this.type = type;
        this.description = description;
        this.weightAtEvent = weightAtEvent;
    }

    public HealthEvent(LocalDateTime timestamp, HealthEventType type, String description) {
        this(timestamp, type, description, 0);
    }

    @Override
    public int compareTo(HealthEvent other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    public LocalDateTime getTimestamp()  { return timestamp; }
    public HealthEventType getType()     { return type; }
    public String getDescription()       { return description; }
    public double getWeightAtEvent()     { return weightAtEvent; }

    public void setTimestamp(LocalDateTime timestamp)       { this.timestamp = timestamp; }
    public void setType(HealthEventType type)               { this.type = type; }
    public void setDescription(String description)           { this.description = description; }
    public void setWeightAtEvent(double weightAtEvent)       { this.weightAtEvent = weightAtEvent; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + ": " + description
                + (weightAtEvent > 0 ? " (weight: " + weightAtEvent + " kg)" : "");
    }
}
