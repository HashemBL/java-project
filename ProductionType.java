public enum ProductionType {
    DAIRY("Dairy production"),
    EGGS("Egg production"),
    HARVEST_WEIGHT("Harvest weight production"),
    CROP_YIELD("Crop yield production"),
    MILK_YIELD(" Milk yield production");

    private final String description;

    ProductionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
