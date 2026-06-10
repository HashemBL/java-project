/**
 * Lifecycle status of an alert.
 */
public enum AlertStatus {
    ACTIVE("Alert is active and requires attention"),
    ACKNOWLEDGED("Alert has been acknowledged by the farm manager"),
    DISMISSED("Alert has been dismissed");

    private final String description;

    AlertStatus(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}
