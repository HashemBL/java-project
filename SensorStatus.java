/**
 * Represents the operational status of a sensor.
 */
public enum SensorStatus {
    ACTIVE("Sensor is active and sending readings"),
    FAULTY("Sensor is malfunctioning"),
    SUSPENDED("Sensor is suspended - zone under maintenance");

    private final String description;

    SensorStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
