import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all sensors in the SmartFarming system.
 * Each sensor has a unique code, belongs to a zone, has a status,
 * a measurement type and a threshold range.
 */
public abstract class Sensor {
    private static int counter = 0;

    private final String code;
    private String zoneCode;
    private SensorStatus status;
    private MeasurementType measurementType;
    private ThresholdRange thresholdRange;
    private List<Reading> readings;

    public Sensor(String zoneCode, MeasurementType measurementType, ThresholdRange thresholdRange) {
        this.code = "SEN-" + String.format("%04d", ++counter);
        this.zoneCode = zoneCode;
        this.measurementType = measurementType;
        this.thresholdRange = thresholdRange;
        this.status = SensorStatus.ACTIVE;
        this.readings = new ArrayList<>();
    }

    /**
     * Records a new reading. If the sensor is suspended, reading is ignored.
     * @return the Alert generated if threshold is exceeded, or null.
     */
    public Alert addReading(Reading reading) {
        if (status == SensorStatus.SUSPENDED) {
            System.out.println("Sensor " + code + " is suspended. Reading ignored.");
            return null;
        }
        readings.add(reading);
        return checkThreshold(reading);
    }

    /** Subclasses define how to check the threshold for their reading type. */
    protected abstract Alert checkThreshold(Reading reading);

    public void suspend() {
        this.status = SensorStatus.SUSPENDED;
    }

    public void activate() {
        this.status = SensorStatus.ACTIVE;
    }

    public void markFaulty() {
        this.status = SensorStatus.FAULTY;
    }

    public List<Reading> getReadings() {
        return new ArrayList<>(readings);
    }

    public boolean removeReading(Reading reading) {
        return readings.remove(reading);
    }

    public Reading getLatestReading() {
        if (readings.isEmpty()) return null;
        return readings.get(readings.size() - 1);
    }

    public String getCode()                      { return code; }
    public String getZoneCode()                  { return zoneCode; }
    public SensorStatus getStatus()              { return status; }
    public MeasurementType getMeasurementType()  { return measurementType; }
    public ThresholdRange getThresholdRange()    { return thresholdRange; }

    public void setThresholdRange(ThresholdRange range) { this.thresholdRange = range; }
    public void setZoneCode(String zoneCode)             { this.zoneCode = zoneCode; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + code + "] zone=" + zoneCode + " status=" + status;
    }
}
