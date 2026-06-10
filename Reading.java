import java.time.LocalDateTime;

/**
 * Abstract base class for all sensor readings.
 */
public abstract class Reading {
    private final LocalDateTime timestamp;
    private final String sensorCode;
    private ReadingLevel level;

    public Reading(LocalDateTime timestamp, String sensorCode) {
        this.timestamp = timestamp;
        this.sensorCode = sensorCode;
        this.level = ReadingLevel.NORMAL;
    }

    public abstract String getValueAsString();

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSensorCode()       { return sensorCode; }
    public ReadingLevel getLevel()      { return level; }
    public void setLevel(ReadingLevel level) { this.level = level; }

    @Override
    public String toString() {
        return "[" + timestamp + "] Sensor " + sensorCode + " -> " + getValueAsString() + " [" + level + "]";
    }
}
