/**
 * Represents the geographic bounds of a zone using four corner positions.
 */
public class GeographicBounds {
    private double northLat;
    private double southLat;
    private double eastLng;
    private double westLng;

    public GeographicBounds(double northLat, double southLat, double eastLng, double westLng) {
        this.northLat = northLat;
        this.southLat = southLat;
        this.eastLng = eastLng;
        this.westLng = westLng;
    }

    public boolean contains(double lat, double lng) {
        return lat <= northLat && lat >= southLat && lng <= eastLng && lng >= westLng;
    }

    public double getNorthLat() { return northLat; }
    public double getSouthLat() { return southLat; }
    public double getEastLng()  { return eastLng; }
    public double getWestLng()  { return westLng; }

    public void setNorthLat(double northLat) { this.northLat = northLat; }
    public void setSouthLat(double southLat) { this.southLat = southLat; }
    public void setEastLng(double eastLng)   { this.eastLng = eastLng; }
    public void setWestLng(double westLng)   { this.westLng = westLng; }

    @Override
    public String toString() {
        return "GeographicBounds{N=" + northLat + ", S=" + southLat +
               ", E=" + eastLng + ", W=" + westLng + "}";
    }
}
