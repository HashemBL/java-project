/**
 * Indicates whether a sensor reading is within acceptable bounds.
 */
public enum ReadingLevel {
    NORMAL("Normal", "#28a745"),
    WARNING("Warning", "#ffc107"),
    CRITICAL("Critical", "#dc3545");

    private final String label;
    private final String colorHex; // for UI display

    ReadingLevel(String label, String colorHex) {
        this.label = label;
        this.colorHex = colorHex;
    }

    public String getLabel()    { return label; }
    public String getColorHex() { return colorHex; }
}
