/**
 * Types of measurements a sensor can take.
 */
public enum MeasurementType {
    TEMPERATURE("Temperature", "°C"),
    HUMIDITY("Humidity", "%"),
    RAINFALL("Rainfall", "mm"),
    PH("pH level", "pH"),
    SOIL_HUMIDITY("Soil humidity", "%"),
    NITROGEN("Nitrogen content", "mg/kg"),
    DISSOLVED_OXYGEN("Dissolved oxygen", "mg/L"),
    BODY_TEMPERATURE("Body temperature", "°C"),
    ACTIVITY("Activity level", "steps/min"),
    GPS_POSITION("GPS position", "coordinates");

    private final String label;
    private final String unit;

    MeasurementType(String label, String unit) {
        this.label = label;
        this.unit = unit;
    }

    public String getLabel() { return label; }
    public String getUnit()  { return unit; }

    @Override
    public String toString() { return label + " (" + unit + ")"; }
}
