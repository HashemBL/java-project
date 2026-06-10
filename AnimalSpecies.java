/**
 * Common animal species used on the farm.
 */
public enum AnimalSpecies {
    // Ruminants
    COW("Cow", AnimalCategory.RUMINANT),
    SHEEP("Sheep", AnimalCategory.RUMINANT),
    GOAT("Goat", AnimalCategory.RUMINANT),

    // Poultry
    CHICKEN("Chicken", AnimalCategory.POULTRY),
    TURKEY("Turkey", AnimalCategory.POULTRY),

    // Aquaculture
    FISH("Fish", AnimalCategory.AQUACULTURE),
    SHRIMP("Shrimp", AnimalCategory.AQUACULTURE);

    private final String commonName;
    private final AnimalCategory category;

    AnimalSpecies(String commonName, AnimalCategory category) {
        this.commonName = commonName;
        this.category = category;
    }

    public String getCommonName()    { return commonName; }
    public AnimalCategory getCategory() { return category; }

    @Override
    public String toString() { return commonName; }
}
