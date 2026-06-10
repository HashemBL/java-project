import java.util.ArrayList;
import java.util.List;

/**
 * A zone dedicated to aquaculture (fish, shrimp, etc.) with a water basin
 * monitored by water sensors (temperature, dissolved oxygen, pH).
 */
public class AquacultureZone extends Zone {
    private List<Animal> aquaticSpecies;
    private int animalCount;
    private FeedingProgram feedingProgram;

    public AquacultureZone(String name, GeographicBounds bounds, FeedingProgram feedingProgram) {
        super(name, bounds);
        this.aquaticSpecies = new ArrayList<>();
        this.feedingProgram = feedingProgram;
        this.animalCount = 0;
    }

    public void addSpecimen(Animal animal) {
        if (animal.getSpecies().getCategory() != AnimalCategory.AQUACULTURE) {
            throw new IllegalArgumentException("Only aquaculture species are allowed in AquacultureZone.");
        }
        aquaticSpecies.add(animal);
        animalCount++;
    }

    public List<Animal> getSpecimens() {
        return new ArrayList<>(aquaticSpecies);
    }

    public boolean removeSpecimen(Animal animal) {
        boolean removed = aquaticSpecies.remove(animal);
        if (removed) animalCount--;
        return removed;
    }

    @Override
    public int getEntityCount() {
        return aquaticSpecies.size();
    }

    @Override
    public String getSummary() {
        return "AquacultureZone [" + getCode() + "] " + getName() + " - " + aquaticSpecies.size() + " specimen(s)";
    }

    @Override
    protected void onSuspend() {
        System.out.println("AquacultureZone " + getName() + " suspended. Water sensors should be suspended by sensor layer.");
    }

    @Override
    protected void onActivate() {
        System.out.println("AquacultureZone " + getName() + " reactivated. Water sensors should be reactivated by sensor layer.");
    }

    public FeedingProgram getFeedingProgram()           { return feedingProgram; }
    public void setFeedingProgram(FeedingProgram fp)    { this.feedingProgram = fp; }
}
