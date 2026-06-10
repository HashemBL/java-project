/**
 * Defines the acceptable value range for a sensor.
 * Values outside this range trigger alerts.
 */
public class ThresholdRange {
    private double min;
    private double max;
    private String unit;

    public ThresholdRange(double min, double max, String unit) {
        if (max <= min) {
            throw new IllegalArgumentException("Max must be greater than min.");
        }
        this.min = min;
        this.max = max;
        this.unit = unit;
    }

    public boolean inRange(double value) {
        return value >= min && value <= max;
    }

    /**
     * Returns the reading level based on how far the value is from the range.
     * Outside range -> CRITICAL. Within 10% of boundary -> WARNING. Otherwise -> NORMAL.
     */
    public ReadingLevel evaluate(double value) {
        if (!inRange(value)) return ReadingLevel.CRITICAL;
        double range = max - min;
        double buffer = range * 0.10;
        if (value <= min + buffer || value >= max - buffer) return ReadingLevel.WARNING;
        return ReadingLevel.NORMAL;
    }

    public double getMin()  { return min; }
    public double getMax()  { return max; }
    public String getUnit() { return unit; }

    public void setMin(double min) { this.min = min; }
    public void setMax(double max) { this.max = max; }
    public void setUnit(String unit) { this.unit = unit; }

    @Override
    public String toString() {
        return "[" + min + " - " + max + " " + unit + "]";
    }
}
