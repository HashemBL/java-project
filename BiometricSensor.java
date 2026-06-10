/**
 * Measures biometric parameters of livestock animals:
 * body temperature and activity level (steps per minute).
 */
public class BiometricSensor extends Sensor {
    private int animalId;

    public BiometricSensor(String zoneCode, MeasurementType type, ThresholdRange thresholdRange, int animalId) {
        super(zoneCode, type, thresholdRange);
        if (type != MeasurementType.BODY_TEMPERATURE && type != MeasurementType.ACTIVITY) {
            throw new IllegalArgumentException("BiometricSensor only supports BODY_TEMPERATURE or ACTIVITY.");
        }
        this.animalId = animalId;
    }

    @Override
    protected Alert checkThreshold(Reading reading) {
        if (!(reading instanceof NumericalReading)) return null;
        NumericalReading nr = (NumericalReading) reading;
        ReadingLevel level = getThresholdRange().evaluate(nr.getValue());
        reading.setLevel(level);

        if (level == ReadingLevel.CRITICAL) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.CRITICAL,
                "Animal #" + animalId + " - " + getMeasurementType().getLabel() + " critical: " + nr.getValueAsString());
        } else if (level == ReadingLevel.WARNING) {
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.WARNING,
                "Animal #" + animalId + " - " + getMeasurementType().getLabel() + " warning: " + nr.getValueAsString());
        }
        return null;
    }

    public int getAnimalId() { return animalId; }
}
