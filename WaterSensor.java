/**
 * Monitors water quality in aquaculture zones:
 * temperature, dissolved oxygen and pH.
 */
public class WaterSensor extends Sensor {

    public WaterSensor(String zoneCode, MeasurementType type, ThresholdRange thresholdRange) {
        super(zoneCode, type, thresholdRange);
        if (type != MeasurementType.TEMPERATURE && type != MeasurementType.DISSOLVED_OXYGEN && type != MeasurementType.PH) {
            throw new IllegalArgumentException("WaterSensor only supports TEMPERATURE, DISSOLVED_OXYGEN or PH.");
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
                "Water " + getMeasurementType().getLabel() + " critically out of range: " + nr.getValueAsString()
                + " (acceptable: " + getThresholdRange() + ")");
        } else if (level == ReadingLevel.WARNING) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.WARNING,
                "Water " + getMeasurementType().getLabel() + " approaching limit: " + nr.getValueAsString());
        }
        return null;
    }
}
