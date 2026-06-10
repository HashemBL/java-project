import java.util.ArrayList;
import java.util.List;

/**
 * The main Farm class. Holds all zones and provides farm-wide operations.
 */
public class Farm {
    private String name;
    private List<Zone> zones;

    public Farm(String name) {
        this.name = name;
        this.zones = new ArrayList<>();
    }

    // --- Zone management ---

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public boolean removeZone(String code) {
        return zones.removeIf(z -> z.getCode().equals(code));
    }

    public Zone findZone(String code) {
        for (Zone z : zones) {
            if (z.getCode().equals(code)) return z;
        }
        return null;
    }

    public void suspendZone(String code) throws AlreadySuspended {
        Zone z = findZone(code);
        if (z != null) z.suspend();
    }

    public void activateZone(String code) {
        Zone z = findZone(code);
        if (z != null) z.activate();
    }

    public List<Zone> getAllZones() {
        return new ArrayList<>(zones);
    }

    public List<CropZone> getCropZones() {
        List<CropZone> result = new ArrayList<>();
        for (Zone z : zones) {
            if (z instanceof CropZone) result.add((CropZone) z);
        }
        return result;
    }

    public List<LivestockZone> getLivestockZones() {
        List<LivestockZone> result = new ArrayList<>();
        for (Zone z : zones) {
            if (z instanceof LivestockZone) result.add((LivestockZone) z);
        }
        return result;
    }

    public List<AquacultureZone> getAquacultureZones() {
        List<AquacultureZone> result = new ArrayList<>();
        for (Zone z : zones) {
            if (z instanceof AquacultureZone) result.add((AquacultureZone) z);
        }
        return result;
    }

    // --- Overview ---

    public void printOverview() {
        System.out.println("==============================");
        System.out.println("  FARM: " + name);
        System.out.println("  Total zones: " + zones.size());
        System.out.println("==============================");
        for (Zone z : zones) {
            System.out.println("  " + z.getSummary() + " [" + z.getStatus() + "]");
        }
        System.out.println("==============================");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Farm{name='" + name + "', zones=" + zones.size() + "}";
    }
}
