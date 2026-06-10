public enum HealthStatus {
    HEALTHY("Animal is healthy"),
    SICK("Animal is sick"),
    QUARANTINED("Animal is under quarantine");

    private final String description;

    HealthStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
