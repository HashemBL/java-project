/**
 * Defines the feeding program for a livestock or aquaculture zone.
 */
public class FeedingProgram {
    private String foodType;
    private double quantityPerMeal;
    private String unit;
    private int mealsPerDay;

    public FeedingProgram(String foodType, double quantityPerMeal, String unit, int mealsPerDay) {
        this.foodType = foodType;
        this.quantityPerMeal = quantityPerMeal;
        this.unit = unit;
        this.mealsPerDay = mealsPerDay;
    }

    public double getDailyQuantity() {
        return quantityPerMeal * mealsPerDay;
    }

    public String getSummary() {
        return foodType + ": " + quantityPerMeal + " " + unit + " x " + mealsPerDay + " meals/day"
                + " (total: " + getDailyQuantity() + " " + unit + "/day)";
    }

    public String getFoodType()          { return foodType; }
    public double getQuantityPerMeal()   { return quantityPerMeal; }
    public String getUnit()              { return unit; }
    public int getMealsPerDay()          { return mealsPerDay; }

    public void setFoodType(String foodType)               { this.foodType = foodType; }
    public void setQuantityPerMeal(double quantityPerMeal) { this.quantityPerMeal = quantityPerMeal; }
    public void setUnit(String unit)                       { this.unit = unit; }
    public void setMealsPerDay(int mealsPerDay)            { this.mealsPerDay = mealsPerDay; }

    @Override
    public String toString() {
        return getSummary();
    }
}
