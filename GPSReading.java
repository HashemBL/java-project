import java.time.LocalDateTime;

/**
 * A reading from a GPS sensor carrying latitude and longitude coordinates.
 */
public class GPSReading extends Reading {
    private double latitude;
    private double longitude;

    public GPSReading(LocalDateTime timestamp, String sensorCode, double latitude, double longitude) {
        super(timestamp, sensorCode);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getValueAsString() {
        return "(" + latitude + ", " + longitude + ")";
    }

    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }
}
