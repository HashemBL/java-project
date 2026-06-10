public enum GrowthStage {
    SOWING("Sowing stage"),
    GERMINATION("Germination stage"),
    GROWTH("Growth stage"),
    MATURITY("Maturity stage"),
    HARVEST("Harvest stage");

    private final String description;

    GrowthStage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
