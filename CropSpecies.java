/**
 * Common crop species grouped by family.
 */
public enum CropSpecies {
    // Cereals
    WHEAT("Wheat", CropFamily.CEREAL),
    CORN("Corn", CropFamily.CEREAL),
    BARLEY("Barley", CropFamily.CEREAL),

    // Vegetables
    TOMATO("Tomato", CropFamily.VEGETABLE),
    POTATO("Potato", CropFamily.VEGETABLE),
    CARROT("Carrot", CropFamily.VEGETABLE),

    // Fruits
    APPLE("Apple", CropFamily.FRUIT),
    GRAPE("Grape", CropFamily.FRUIT),
    OLIVE("Olive", CropFamily.FRUIT);

    private final String commonName;
    private final CropFamily family;

    CropSpecies(String commonName, CropFamily family) {
        this.commonName = commonName;
        this.family = family;
    }

    public String getCommonName() { return commonName; }
    public CropFamily getFamily() { return family; }

    @Override
    public String toString() {
        return commonName + " (" + family + ")";
    }
}
