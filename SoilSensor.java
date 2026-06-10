/**
 * Measures soil parameters: pH, humidity and nitrogen content.
 * Used in crop zones.
 */
public class SoilSensor extends Sensor {

    public SoilSensor(String zoneCode, MeasurementType type, ThresholdRange thresholdRange) {
        super(zoneCode, type, thresholdRange);
        if (type != MeasurementType.PH && type != MeasurementType.SOIL_HUMIDITY && type != MeasurementType.NITROGEN) {
            throw new IllegalArgumentException("SoilSensor only supports PH, SOIL_HUMIDITY or NITROGEN.");
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
                "Soil " + getMeasurementType().getLabel() + " critically out of range: " + nr.getValueAsString()
                + " (range: " + getThresholdRange() + ")");
        } else if (level == ReadingLevel.WARNING) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.WARNING,
                "Soil " + getMeasurementType().getLabel() + " approaching threshold: " + nr.getValueAsString());
        }
        return null;
    }
}
