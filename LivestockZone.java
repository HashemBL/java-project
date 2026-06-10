import java.util.ArrayList;
import java.util.List;

/**
 * A zone for raising livestock (ruminants or poultry).
 * Each zone hosts one species category and defines a feeding program.
 */
public class LivestockZone extends Zone {
    private AnimalCategory animalCategory;
    private List<Animal> animals;
    private FeedingProgram feedingProgram;

    public LivestockZone(String name, GeographicBounds bounds, AnimalCategory animalCategory, FeedingProgram feedingProgram) {
        super(name, bounds);
        this.animalCategory = animalCategory;
        this.animals = new ArrayList<>();
        this.feedingProgram = feedingProgram;
    }

    public void addAnimal(Animal animal) {
        if (animal.getSpecies().getCategory() != animalCategory) {
            throw new IllegalArgumentException(
                "Animal category mismatch: zone accepts " + animalCategory + " but got " + animal.getSpecies().getCategory()
            );
        }
        animals.add(animal);
    }

    public boolean removeAnimal(Animal animal) {
        return animals.remove(animal);
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public Animal findById(int id) {
        for (Animal a : animals) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    @Override
    public int getEntityCount() {
        return animals.size();
    }

    @Override
    public String getSummary() {
        return "LivestockZone [" + getCode() + "] " + getName()
                + " (" + animalCategory.getLabel() + ") - " + animals.size() + " animal(s)";
    }

    @Override
    protected void onSuspend() {
        System.out.println("LivestockZone " + getName() + " suspended. Sensors should be suspended by sensor layer.");
    }

    @Override
    protected void onActivate() {
        System.out.println("LivestockZone " + getName() + " reactivated. Sensors should be reactivated by sensor layer.");
    }

    public AnimalCategory getAnimalCategory()          { return animalCategory; }
    public FeedingProgram getFeedingProgram()           { return feedingProgram; }
    public void setFeedingProgram(FeedingProgram fp)    { this.feedingProgram = fp; }
}
