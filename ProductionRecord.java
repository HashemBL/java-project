import java.time.LocalDate;

/**
 * Records production data for a zone at a specific date.
 */
public class ProductionRecord {
    private LocalDate date;
    private double quantity;
    private String unit;
    private ProductionType type;
    private String zoneCode;

    public ProductionRecord(LocalDate date, double quantity, String unit, ProductionType type, String zoneCode) {
        this.date = date;
        this.quantity = quantity;
        this.unit = unit;
        this.type = type;
        this.zoneCode = zoneCode;
    }

    public LocalDate getDate()       { return date; }
    public double getQuantity()      { return quantity; }
    public String getUnit()          { return unit; }
    public ProductionType getType()  { return type; }
    public String getZoneCode()      { return zoneCode; }

    public void setDate(LocalDate date)       { this.date = date; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setUnit(String unit)         { this.unit = unit; }
    public void setType(ProductionType type)  { this.type = type; }
    public void setZoneCode(String zoneCode)  { this.zoneCode = zoneCode; }

    @Override
    public String toString() {
        return "[" + date + "] Zone " + zoneCode + " - " + type + ": " + quantity + " " + unit;
    }
}
