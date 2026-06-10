public enum HealthEventType {
    DISEASE("Disease diagnosis"),
    WEIGHT_CHANGE("Weight change recorded"),
    VACCINATION("Vaccination administered"),
    TREATMENT("Medical treatment applied"),
    CHECKUP("Routine health checkup");

    private final String description;

    HealthEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
