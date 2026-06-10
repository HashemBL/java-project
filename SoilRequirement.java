/**
 * Represents the pedological (soil) requirements of a crop:
 * optimal pH range and optimal humidity range.
 */
public class SoilRequirement {
    private double minPH;
    private double maxPH;
    private double minHumidity;
    private double maxHumidity;

    public SoilRequirement(double minPH, double maxPH, double minHumidity, double maxHumidity) {
        this.minPH = minPH;
        this.maxPH = maxPH;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
    }

    public boolean isPHOptimal(double ph) {
        return ph >= minPH && ph <= maxPH;
    }

    public boolean isHumidityOptimal(double humidity) {
        return humidity >= minHumidity && humidity <= maxHumidity;
    }

    public double getMinPH()          { return minPH; }
    public double getMaxPH()          { return maxPH; }
    public double getMinHumidity()    { return minHumidity; }
    public double getMaxHumidity()    { return maxHumidity; }

    public void setMinPH(double minPH)             { this.minPH = minPH; }
    public void setMaxPH(double maxPH)             { this.maxPH = maxPH; }
    public void setMinHumidity(double minHumidity) { this.minHumidity = minHumidity; }
    public void setMaxHumidity(double maxHumidity) { this.maxHumidity = maxHumidity; }

    @Override
    public String toString() {
        return "SoilRequirement{pH=[" + minPH + "," + maxPH + "], humidity=[" + minHumidity + "," + maxHumidity + "]}";
    }
}
