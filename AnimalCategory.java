/**
 * High-level category grouping for animal species.
 */
public enum AnimalCategory {
    RUMINANT("Ruminant"),
    POULTRY("Poultry"),
    AQUACULTURE("Aquaculture");

    private final String label;

    AnimalCategory(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }
}
