import java.util.ArrayList;
import java.util.List;

/**
 * A zone dedicated to crop cultivation.
 * Equipped with environmental sensors and soil sensors (managed by Student 2).
 */
public class CropZone extends Zone {
    private List<Crop> crops;

    public CropZone(String name, GeographicBounds bounds) {
        super(name, bounds);
        this.crops = new ArrayList<>();
    }

    public void addCrop(Crop crop) {
        crops.add(crop);
    }

    public boolean removeCrop(Crop crop) {
        return crops.remove(crop);
    }

    public List<Crop> getCrops() {
        return new ArrayList<>(crops);
    }

    public List<Crop> getCropsByFamily(CropFamily family) {
        List<Crop> result = new ArrayList<>();
        for (Crop c : crops) {
            if (c.getSpecies().getFamily() == family) {
                result.add(c);
            }
        }
        return result;
    }

    public String getCropStatusReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Crop report for zone ").append(getName()).append(" ===\n");
        for (Crop c : crops) {
            sb.append("  ").append(c.getStatusReport()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int getEntityCount() {
        return crops.size();
    }

    @Override
    public String getSummary() {
        return "CropZone [" + getCode() + "] " + getName() + " - " + crops.size() + " crop(s)";
    }

    @Override
    protected void onSuspend() {
        // Sensor cascade is handled by Student 2's sensor layer
        System.out.println("CropZone " + getName() + " suspended. Sensors should be suspended by sensor layer.");
    }

    @Override
    protected void onActivate() {
        System.out.println("CropZone " + getName() + " reactivated. Sensors should be reactivated by sensor layer.");
    }
}
