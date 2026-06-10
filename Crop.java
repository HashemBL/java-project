import java.time.LocalDate;

/**
 * Represents a crop planted in a CropZone.
 */
public class Crop {
    private static int counter = 0;

    private final int id;
    private CropSpecies species;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
    private GrowthStage currentStage;
    private SoilRequirement soilRequirement;

    public Crop(CropSpecies species, LocalDate plantingDate, LocalDate expectedHarvestDate, SoilRequirement soilRequirement) {
        this.id = ++counter;
        this.species = species;
        this.plantingDate = plantingDate;
        this.expectedHarvestDate = expectedHarvestDate;
        this.soilRequirement = soilRequirement;
        this.currentStage = GrowthStage.SOWING;
    }

    public void advanceStage() {
        GrowthStage[] stages = GrowthStage.values();
        int next = currentStage.ordinal() + 1;
        if (next < stages.length) {
            currentStage = stages[next];
        }
    }

    public String getStatusReport() {
        return "Crop #" + id + " [" + species.getCommonName() + "] - Stage: " + currentStage
                + " | ph: ["+ soilRequirement.getMinPH() +"-"+ soilRequirement.getMaxPH() +"] |" + "" +
                " humidity : [" + soilRequirement.getMinHumidity()+ "-"+ soilRequirement.getMaxHumidity()+" %]  | "
                +" planting date : " + plantingDate + " | Expected harvest: " + expectedHarvestDate;
    }

    public int getId()                             { return id; }
    public CropSpecies getSpecies()                { return species; }
    public LocalDate getPlantingDate()             { return plantingDate; }
    public LocalDate getExpectedHarvestDate()      { return expectedHarvestDate; }
    public GrowthStage getCurrentStage()           { return currentStage; }
    public SoilRequirement getSoilRequirement()    { return soilRequirement; }

    public void setSpecies(CropSpecies species)                { this.species = species; }
    public void setPlantingDate(LocalDate date)                { this.plantingDate = date; }
    public void setCurrentStage(GrowthStage stage)             { this.currentStage = stage; }
    public void setExpectedHarvestDate(LocalDate date)         { this.expectedHarvestDate = date; }
    public void setSoilRequirement(SoilRequirement req)        { this.soilRequirement = req; }

    @Override
    public String toString() {
        return getStatusReport();
    }
}
