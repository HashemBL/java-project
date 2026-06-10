public enum CropFamily {
    CEREAL("Cereals (wheat, corn, barley, etc.)"),
    VEGETABLE("Vegetables (tomato, potato, carrot, etc.)"),
    FRUIT("Fruits (apple, grape, olive, etc.)");

    private final String description;

    CropFamily(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
