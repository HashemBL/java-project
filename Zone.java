import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all farm zones.
 * Each zone has a unique code, a name, geographic bounds, a status,
 * and a history of production records.
 */
public abstract class Zone {
    private static int counter = 0;

    private final String code;
    private String name;
    private GeographicBounds bounds;
    private ZoneStatus status;
    private List<ProductionRecord> productionHistory;

    public Zone(String name, GeographicBounds bounds) {
        this.code = "ZONE-" + (++counter);
        this.name = name;
        this.bounds = bounds;
        this.status = ZoneStatus.ACTIVE;
        this.productionHistory = new ArrayList<>();
    }

    /**
     * Suspends the zone and all its sensors (subclass may override to cascade).
     */
    public void suspend() throws AlreadySuspended {
        if ( this.status == ZoneStatus.SUSPENDED) throw new AlreadySuspended("msg");
        this.status = ZoneStatus.SUSPENDED;
        onSuspend();
    }

    /**
     * Reactivates the zone and all its sensors.
     */
    public void activate() {
        this.status = ZoneStatus.ACTIVE;
        onActivate();
    }

    /** Hook for subclasses to handle suspension side-effects (e.g., suspend sensors). */
    protected abstract void onSuspend();

    /** Hook for subclasses to handle reactivation side-effects. */
    protected abstract void onActivate();

    /** Returns the number of entities (crops or animals) hosted in this zone. */
    public abstract int getEntityCount();

    /** Returns a short summary string describing the zone contents. */
    public abstract String getSummary();

    public void addProductionRecord(ProductionRecord record) {
        productionHistory.add(record);
    }

    public boolean removeProductionRecord(ProductionRecord record) {
        return productionHistory.remove(record);
    }

    public List<ProductionRecord> getProductionHistory() {
        return new ArrayList<>(productionHistory);
    }

    // --- Getters & Setters ---

    public String getCode()              { return code; }
    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }
    public GeographicBounds getBounds()  { return bounds; }
    public void setBounds(GeographicBounds bounds) { this.bounds = bounds; }
    public ZoneStatus getStatus()        { return status; }
    public boolean isActive()            { return status == ZoneStatus.ACTIVE; }

    @Override
    public String toString() {
        return "Zone[" + code + "] " + name + " (" + status + ") - entities: " + getEntityCount();
    }
}
