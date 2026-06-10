/**
 * GPS collar sensor attached to a livestock animal.
 * Sends the animal's geographic position periodically.
 * An alert is triggered if the animal leaves the zone boundaries.
 */
public class GPSSensor extends Sensor {
    private int animalId;

    // Zone boundary (kept simple as doubles for compatibility with Student 1's GeographicBounds)
    private double northLat;
    private double southLat;
    private double eastLng;
    private double westLng;

    public GPSSensor(String zoneCode, int animalId,
                     double northLat, double southLat, double eastLng, double westLng) {
        super(zoneCode, MeasurementType.GPS_POSITION,
              new ThresholdRange(0, 1, "boundary")); // dummy range - boundary checked manually
        this.animalId = animalId;
        this.northLat = northLat;
        this.southLat = southLat;
        this.eastLng = eastLng;
        this.westLng = westLng;
    }

    private boolean isInsideBounds(double lat, double lng) {
        return lat <= northLat && lat >= southLat && lng <= eastLng && lng >= westLng;
    }

    @Override
    protected Alert checkThreshold(Reading reading) {
        if (!(reading instanceof GPSReading)) return null;
        GPSReading gps = (GPSReading) reading;

        if (!isInsideBounds(gps.getLatitude(), gps.getLongitude())) {
            reading.setLevel(ReadingLevel.CRITICAL);
            return new Alert(getCode(), getZoneCode(), reading, SeverityLevel.CRITICAL,
                "Animal #" + animalId + " has left the zone boundaries! Position: "
                + gps.getValueAsString());
        }
        reading.setLevel(ReadingLevel.NORMAL);
        return null;
    }

    public int getAnimalId() { return animalId; }
}
