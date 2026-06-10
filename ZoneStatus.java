public enum ZoneStatus {
    ACTIVE("Zone is active and operational"),
    SUSPENDED("Zone is suspended for maintenance");

    private final String description;

    ZoneStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
