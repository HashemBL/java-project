import java.time.LocalDateTime;

/**
 * A reading that carries a single numeric value with a unit.
 */
public class NumericalReading extends Reading {
    private double value;
    private String unit;

    public NumericalReading(LocalDateTime timestamp, String sensorCode, double value, String unit) {
        super(timestamp, sensorCode);
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String getValueAsString() {
        return value + " " + unit;
    }

    public double getValue() { return value; }
    public String getUnit()  { return unit; }
}
