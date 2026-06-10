import java.util.TreeSet;

/**
 * Represents an animal on the farm.
 * Each animal has a unique ID, species, age, weight and health status.
 * Health events are stored in chronological order.
 */
public class Animal {
    private static int counter = 0;

    private final int id;
    private AnimalSpecies species;
    private int age;           // in months
    private double weight;     // in kg
    private HealthStatus healthStatus;
    private TreeSet<HealthEvent> healthHistory;

    public Animal(AnimalSpecies species, int age, double weight, HealthStatus healthStatus) {
        this.id = ++counter;
        this.species = species;
        this.age = age;
        this.weight = weight;
        this.healthStatus = healthStatus;
        this.healthHistory = new TreeSet<>();
    }

    public void addHealthEvent(HealthEvent event) {
        healthHistory.add(event);
        // Update weight if the event carries a weight reading
        if (event.getWeightAtEvent() > 0) {
            this.weight = event.getWeightAtEvent();
        }
    }

    public boolean removeHealthEvent(HealthEvent event) {
        return healthHistory.remove(event);
    }

    public TreeSet<HealthEvent> getHealthHistory() {
        return new TreeSet<>(healthHistory);
    }

    public String getHealthReport() {
        return "Animal #" + id + " [" + species.getCommonName() + "]"
                + " | Status: " + healthStatus.getDescription()
                + " | Age: " + age + " months"
                + " | Weight: " + weight + " kg";

    }

    public int getId()                    { return id; }
    public AnimalSpecies getSpecies()     { return species; }
    public int getAge()                   { return age; }
    public double getWeight()             { return weight; }
    public HealthStatus getHealthStatus() { return healthStatus; }

    public void setSpecies(AnimalSpecies species)          { this.species = species; }
    public void setAge(int age)                       { this.age = age; }
    public void setWeight(double weight)              { this.weight = weight; }
    public void setHealthStatus(HealthStatus status)  { this.healthStatus = status; }

    @Override
    public String toString() {
        return getHealthReport();
    }
}
