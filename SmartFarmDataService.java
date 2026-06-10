import java.util.ArrayList;
import java.util.List;

public class SmartFarmDataService {
    private final AppContext context;

    public SmartFarmDataService(AppContext context) {
        this.context = context;
    }

    public AppContext getContext() {
        return context;
    }

    public List<Crop> allCrops() {
        List<Crop> crops = new ArrayList<>();
        for (CropZone zone : context.getFarm().getCropZones()) crops.addAll(zone.getCrops());
        return crops;
    }

    public List<Animal> allAnimals() {
        List<Animal> animals = new ArrayList<>();
        for (LivestockZone zone : context.getFarm().getLivestockZones()) animals.addAll(zone.getAnimals());
        for (AquacultureZone zone : context.getFarm().getAquacultureZones()) animals.addAll(zone.getSpecimens());
        return animals;
    }

    public List<ProductionRecord> allProduction() {
        List<ProductionRecord> records = new ArrayList<>();
        for (Zone zone : context.getFarm().getAllZones()) records.addAll(zone.getProductionHistory());
        return records;
    }

    public List<Zone> animalZones() {
        List<Zone> zones = new ArrayList<>();
        zones.addAll(context.getFarm().getLivestockZones());
        zones.addAll(context.getFarm().getAquacultureZones());
        return zones;
    }

    public CropZone findCropZone(Crop crop) {
        if (crop == null) return null;
        for (CropZone zone : context.getFarm().getCropZones()) {
            if (zone.getCrops().contains(crop)) return zone;
        }
        return null;
    }

    public Zone findAnimalZone(Animal animal) {
        if (animal == null) return null;
        for (LivestockZone zone : context.getFarm().getLivestockZones()) {
            if (zone.getAnimals().contains(animal)) return zone;
        }
        for (AquacultureZone zone : context.getFarm().getAquacultureZones()) {
            if (zone.getSpecimens().contains(animal)) return zone;
        }
        return null;
    }

    public void addAnimalToZone(Zone zone, Animal animal) {
        if (zone instanceof LivestockZone) ((LivestockZone) zone).addAnimal(animal);
        else if (zone instanceof AquacultureZone) ((AquacultureZone) zone).addSpecimen(animal);
    }

    public void removeAnimalFromZone(Zone zone, Animal animal) {
        if (zone instanceof LivestockZone) ((LivestockZone) zone).removeAnimal(animal);
        else if (zone instanceof AquacultureZone) ((AquacultureZone) zone).removeSpecimen(animal);
    }

    public String zoneNameForCrop(Crop crop) {
        CropZone zone = findCropZone(crop);
        return zone == null ? "-" : zone.getName();
    }

    public String zoneNameForAnimal(Animal animal) {
        Zone zone = findAnimalZone(animal);
        return zone == null ? "-" : zone.getName();
    }

    public String zoneName(String code) {
        Zone zone = context.getFarm().findZone(code);
        return zone == null ? code : zone.getName();
    }

    public void deleteZone(Zone zone) {
        if (zone == null) return;
        context.getFarm().removeZone(zone.getCode());
        context.getSensors().removeIf(sensor -> sensor.getZoneCode().equals(zone.getCode()));
        context.getSensorsByCode().entrySet().removeIf(entry -> entry.getValue().getZoneCode().equals(zone.getCode()));
    }

    public void deleteCrop(Crop crop) {
        CropZone zone = findCropZone(crop);
        if (crop != null && zone != null) {
            zone.removeCrop(crop);
            context.getCropsById().remove(crop.getId());
            context.getCropStageHistory().remove(crop.getId());
        }
    }

    public void deleteAnimal(Animal animal) {
        if (animal == null) return;
        removeAnimalFromZone(findAnimalZone(animal), animal);
        context.getAnimalsById().remove(animal.getId());
        context.getSensors().removeIf(sensor -> sensor instanceof BiometricSensor && ((BiometricSensor) sensor).getAnimalId() == animal.getId());
    }

    public void deleteProduction(ProductionRecord record) {
        if (record == null) return;
        Zone zone = context.getFarm().findZone(record.getZoneCode());
        if (zone != null) zone.removeProductionRecord(record);
    }

    public void deleteSensor(Sensor sensor) {
        if (sensor == null) return;
        context.getSensors().remove(sensor);
        context.getSensorsByCode().remove(sensor.getCode());
    }

    public void deleteReading(Reading reading) {
        if (reading == null) return;
        Sensor sensor = context.getSensorsByCode().get(reading.getSensorCode());
        if (sensor != null) sensor.removeReading(reading);
    }

    public void deleteAlert(Alert alert) {
        if (alert != null) context.getAlertManager().deleteAlert(alert.getId());
    }

    public void deleteHealthEvent(Animal animal, HealthEvent event) {
        if (animal != null && event != null) animal.removeHealthEvent(event);
    }
}
