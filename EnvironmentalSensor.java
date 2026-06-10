/**
 * Measures environmental parameters: temperature, humidity, rainfall.
 * Used in crop zones.
 */
public class EnvironmentalSensor extends Sensor {

    public EnvironmentalSensor(String zoneCode, MeasurementType type, ThresholdRange thresholdRange) {
        super(zoneCode, type, thresholdRange);
        if (type != MeasurementType.TEMPERATURE && type != MeasurementType.HUMIDITY && type != MeasurementType.RAINFALL) {
            throw new IllegalArgumentException("EnvironmentalSensor only supports TEMPERATURE, HUMIDITY or RAINFALL.");
        }
    }

    @Override
    protected Alert checkThreshold(Reading reading) {
        if (!(reading instanceof NumericalReading)) return null;
        NumericalReading nr = (NumericalReading) reading;
        ReadingLevel level = getThresholdRange().evaluate(nr.getValue());
        reading.setLevel(level);

        if (level == ReadingLevel.CRITICAL) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.CRITICAL,
                getMeasurementType().getLabel() + " critically out of range: " + nr.getValueAsString()
                + " (range: " + getThresholdRange() + ")");
        } else if (level == ReadingLevel.WARNING) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.WARNING,
                getMeasurementType().getLabel() + " approaching threshold: " + nr.getValueAsString());
        }
        return null;
    }
}
