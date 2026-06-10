import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Always launch the JavaFX UI
        SmartFarmFX.main(args);
    }

    public static AppContext createSampleData() {
        Farm farm = new Farm("Green Valley Farm");

        CropZone cropZone = new CropZone(
            "Field Alpha",
            new GeographicBounds(35.8, 35.1, -6.1, -6.8)
        );
        LivestockZone livestockZone = new LivestockZone(
            "Barn Ruminants",
            new GeographicBounds(35.7, 35.2, -6.0, -6.6),
            AnimalCategory.RUMINANT,
            new FeedingProgram("Hay", 5.0, "kg", 2)
        );
        LivestockZone poultryZone = new LivestockZone(
            "Poultry House",
            new GeographicBounds(35.69, 35.18, -6.02, -6.62),
            AnimalCategory.POULTRY,
            new FeedingProgram("Grain Mix", 1.5, "kg", 3)
        );
        AquacultureZone aquacultureZone = new AquacultureZone(
            "Pond Blue",
            new GeographicBounds(35.6, 35.3, -6.2, -6.7),
            new FeedingProgram("Pellets", 2.0, "kg", 3)
        );

        farm.addZone(cropZone);
        farm.addZone(livestockZone);
        farm.addZone(poultryZone);
        farm.addZone(aquacultureZone);

        SoilRequirement soilRequirement = new SoilRequirement(6.0, 7.5, 40, 70);
        Crop wheat = new Crop(
            CropSpecies.WHEAT,
            LocalDate.now().minusMonths(2),
            LocalDate.now().plusMonths(1),
            soilRequirement
        );
        Crop tomato = new Crop(
            CropSpecies.TOMATO,
            LocalDate.now().minusWeeks(6),
            LocalDate.now().plusWeeks(8),
            soilRequirement
        );
        Crop apple = new Crop(
            CropSpecies.APPLE,
            LocalDate.now().minusMonths(5),
            LocalDate.now().plusMonths(3),
            soilRequirement
        );
        cropZone.addCrop(wheat);
        cropZone.addCrop(tomato);
        cropZone.addCrop(apple);
        // Additional sample crops (cover all families)
        Crop barley = new Crop(
            CropSpecies.BARLEY,
            LocalDate.now().minusMonths(3),
            LocalDate.now().plusMonths(1),
            soilRequirement
        );
        Crop corn = new Crop(
            CropSpecies.CORN,
            LocalDate.now().minusMonths(4),
            LocalDate.now().plusMonths(2),
            soilRequirement
        );
        Crop potato = new Crop(
            CropSpecies.POTATO,
            LocalDate.now().minusWeeks(10),
            LocalDate.now().plusWeeks(12),
            soilRequirement
        );
        Crop carrot = new Crop(
            CropSpecies.CARROT,
            LocalDate.now().minusWeeks(8),
            LocalDate.now().plusWeeks(6),
            soilRequirement
        );
        Crop grape = new Crop(
            CropSpecies.GRAPE,
            LocalDate.now().minusMonths(6),
            LocalDate.now().plusMonths(4),
            soilRequirement
        );
        Crop olive = new Crop(
            CropSpecies.OLIVE,
            LocalDate.now().minusMonths(12),
            LocalDate.now().plusMonths(6),
            soilRequirement
        );
        cropZone.addCrop(barley);
        cropZone.addCrop(corn);
        cropZone.addCrop(potato);
        cropZone.addCrop(carrot);
        cropZone.addCrop(grape);
        cropZone.addCrop(olive);

        Map<Integer, Crop> cropsById = new HashMap<>();
        for (Crop c : new Crop[]{wheat, barley, corn, tomato, potato, carrot, apple, grape, olive}) {
            cropsById.put(c.getId(), c);
        }

        Map<Integer, List<StageSnapshot>> cropStageHistory = new HashMap<>();
        List<StageSnapshot> wheatTimeline = new ArrayList<>();
        recordStage(wheatTimeline, LocalDate.now().minusMonths(2), wheat.getCurrentStage());
        wheat.advanceStage();
        recordStage(wheatTimeline, LocalDate.now().minusWeeks(7), wheat.getCurrentStage());
        wheat.advanceStage();
        recordStage(wheatTimeline, LocalDate.now().minusWeeks(3), wheat.getCurrentStage());
        wheat.advanceStage();
        recordStage(wheatTimeline, LocalDate.now().minusDays(10), wheat.getCurrentStage());
        cropStageHistory.put(wheat.getId(), wheatTimeline);

        List<StageSnapshot> tomatoTimeline = new ArrayList<>();
        recordStage(tomatoTimeline, LocalDate.now().minusWeeks(6), tomato.getCurrentStage());
        tomato.advanceStage();
        recordStage(tomatoTimeline, LocalDate.now().minusWeeks(4), tomato.getCurrentStage());
        tomato.advanceStage();
        recordStage(tomatoTimeline, LocalDate.now().minusWeeks(1), tomato.getCurrentStage());
        cropStageHistory.put(tomato.getId(), tomatoTimeline);

        List<StageSnapshot> appleTimeline = new ArrayList<>();
        recordStage(appleTimeline, LocalDate.now().minusMonths(5), apple.getCurrentStage());
        apple.advanceStage();
        recordStage(appleTimeline, LocalDate.now().minusMonths(3), apple.getCurrentStage());
        apple.advanceStage();
        recordStage(appleTimeline, LocalDate.now().minusWeeks(2), apple.getCurrentStage());
        cropStageHistory.put(apple.getId(), appleTimeline);

        // timelines for additional crops
        List<StageSnapshot> barleyTimeline = new ArrayList<>();
        recordStage(barleyTimeline, LocalDate.now().minusMonths(3), barley.getCurrentStage());
        barley.advanceStage();
        recordStage(barleyTimeline, LocalDate.now().minusWeeks(6), barley.getCurrentStage());
        cropStageHistory.put(barley.getId(), barleyTimeline);

        List<StageSnapshot> cornTimeline = new ArrayList<>();
        recordStage(cornTimeline, LocalDate.now().minusMonths(4), corn.getCurrentStage());
        corn.advanceStage();
        recordStage(cornTimeline, LocalDate.now().minusWeeks(12), corn.getCurrentStage());
        cropStageHistory.put(corn.getId(), cornTimeline);

        List<StageSnapshot> potatoTimeline = new ArrayList<>();
        recordStage(potatoTimeline, LocalDate.now().minusWeeks(10), potato.getCurrentStage());
        potato.advanceStage();
        recordStage(potatoTimeline, LocalDate.now().minusWeeks(5), potato.getCurrentStage());
        cropStageHistory.put(potato.getId(), potatoTimeline);

        List<StageSnapshot> carrotTimeline = new ArrayList<>();
        recordStage(carrotTimeline, LocalDate.now().minusWeeks(8), carrot.getCurrentStage());
        carrot.advanceStage();
        recordStage(carrotTimeline, LocalDate.now().minusWeeks(2), carrot.getCurrentStage());
        cropStageHistory.put(carrot.getId(), carrotTimeline);

        List<StageSnapshot> grapeTimeline = new ArrayList<>();
        recordStage(grapeTimeline, LocalDate.now().minusMonths(6), grape.getCurrentStage());
        grape.advanceStage();
        recordStage(grapeTimeline, LocalDate.now().minusMonths(2), grape.getCurrentStage());
        cropStageHistory.put(grape.getId(), grapeTimeline);

        List<StageSnapshot> oliveTimeline = new ArrayList<>();
        recordStage(oliveTimeline, LocalDate.now().minusMonths(12), olive.getCurrentStage());
        olive.advanceStage();
        recordStage(oliveTimeline, LocalDate.now().minusMonths(6), olive.getCurrentStage());
        cropStageHistory.put(olive.getId(), oliveTimeline);

        Animal cow = new Animal(AnimalSpecies.COW, 24, 450, HealthStatus.HEALTHY);
        Animal goat = new Animal(AnimalSpecies.GOAT, 18, 60, HealthStatus.SICK);
        Animal sheep = new Animal(AnimalSpecies.SHEEP, 30, 80, HealthStatus.QUARANTINED);
        Animal chicken = new Animal(AnimalSpecies.CHICKEN, 12, 2.0, HealthStatus.HEALTHY);
        Animal turkey = new Animal(AnimalSpecies.TURKEY, 14, 5.0, HealthStatus.SICK);
        livestockZone.addAnimal(cow);
        livestockZone.addAnimal(goat);
        livestockZone.addAnimal(sheep);
        poultryZone.addAnimal(chicken);
        poultryZone.addAnimal(turkey);

        cow.addHealthEvent(new HealthEvent(
            LocalDateTime.now().minusDays(30),
            HealthEventType.CHECKUP,
            "Routine check"
        ));
        cow.addHealthEvent(new HealthEvent(
            LocalDateTime.now().minusDays(15),
            HealthEventType.VACCINATION,
            "FMD vaccine"
        ));
        goat.addHealthEvent(new HealthEvent(
            LocalDateTime.now().minusDays(7),
            HealthEventType.DISEASE,
            "Respiratory infection"
        ));
        goat.addHealthEvent(new HealthEvent(
            LocalDateTime.now().minusDays(2),
            HealthEventType.TREATMENT,
            "Antibiotic course"
        ));

        Animal fish = new Animal(AnimalSpecies.FISH, 6, 2.5, HealthStatus.HEALTHY);
        aquacultureZone.addSpecimen(fish);

        Map<Integer, Animal> animalsById = new HashMap<>();
        for (Animal a : new Animal[]{cow, goat, sheep, chicken, turkey, fish}) {
            animalsById.put(a.getId(), a);
        }

        cropZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(20),
            1200,
            "kg",
            ProductionType.CROP_YIELD,
            cropZone.getCode()
        ));
        cropZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(5),
            900,
            "kg",
            ProductionType.CROP_YIELD,
            cropZone.getCode()
        ));
        livestockZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(12),
            300,
            "L",
            ProductionType.MILK_YIELD,
            livestockZone.getCode()
        ));
        livestockZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(3),
            280,
            "L",
            ProductionType.MILK_YIELD,
            livestockZone.getCode()
        ));
        aquacultureZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(8),
            200,
            "kg",
            ProductionType.HARVEST_WEIGHT,
            aquacultureZone.getCode()
        ));
        aquacultureZone.addProductionRecord(new ProductionRecord(
            LocalDate.now().minusDays(1),
            240,
            "kg",
            ProductionType.HARVEST_WEIGHT,
            aquacultureZone.getCode()
        ));

        AlertManager alertManager = new AlertManager();
        List<Sensor> sensors = new ArrayList<>();
        Map<String, Sensor> sensorsByCode = new HashMap<>();

        EnvironmentalSensor tempSensor = new EnvironmentalSensor(
            cropZone.getCode(),
            MeasurementType.TEMPERATURE,
            new ThresholdRange(15, 35, MeasurementType.TEMPERATURE.getUnit())
        );
        EnvironmentalSensor humSensor = new EnvironmentalSensor(
            cropZone.getCode(),
            MeasurementType.HUMIDITY,
            new ThresholdRange(40, 80, MeasurementType.HUMIDITY.getUnit())
        );
        SoilSensor phSensor = new SoilSensor(
            cropZone.getCode(),
            MeasurementType.PH,
            new ThresholdRange(6, 7.5, MeasurementType.PH.getUnit())
        );
        SoilSensor nitrogenSensor = new SoilSensor(
            cropZone.getCode(),
            MeasurementType.NITROGEN,
            new ThresholdRange(0, 50, MeasurementType.NITROGEN.getUnit())
        );
        WaterSensor oxygenSensor = new WaterSensor(
            aquacultureZone.getCode(),
            MeasurementType.DISSOLVED_OXYGEN,
            new ThresholdRange(5, 12, MeasurementType.DISSOLVED_OXYGEN.getUnit())
        );
        BiometricSensor bodyTempSensor = new BiometricSensor(
            livestockZone.getCode(),
            MeasurementType.BODY_TEMPERATURE,
            new ThresholdRange(37.5, 39.5, MeasurementType.BODY_TEMPERATURE.getUnit()),
            cow.getId()
        );
        BiometricSensor chickenBio = new BiometricSensor(
            livestockZone.getCode(),
            MeasurementType.BODY_TEMPERATURE,
            new ThresholdRange(40.0, 44.0, MeasurementType.BODY_TEMPERATURE.getUnit()),
            chicken.getId()
        );
        GPSSensor gpsSensor = new GPSSensor(
            livestockZone.getCode(),
            cow.getId(),
            livestockZone.getBounds().getNorthLat(),
            livestockZone.getBounds().getSouthLat(),
            livestockZone.getBounds().getEastLng(),
            livestockZone.getBounds().getWestLng()
        );

        addSensor(sensors, sensorsByCode, tempSensor);
        addSensor(sensors, sensorsByCode, humSensor);
        addSensor(sensors, sensorsByCode, phSensor);
        addSensor(sensors, sensorsByCode, nitrogenSensor);
        addSensor(sensors, sensorsByCode, oxygenSensor);
        addSensor(sensors, sensorsByCode, bodyTempSensor);
        addSensor(sensors, sensorsByCode, chickenBio);
        addSensor(sensors, sensorsByCode, gpsSensor);

        LocalDateTime now = LocalDateTime.now();
        // Temperature readings: normal, warning (near min), critical (out of range)
        registerAlert(alertManager, tempSensor.addReading(
            new NumericalReading(now.minusDays(6), tempSensor.getCode(), 25, MeasurementType.TEMPERATURE.getUnit())
        ));
        registerAlert(alertManager, tempSensor.addReading(
            new NumericalReading(now.minusDays(3), tempSensor.getCode(), 16, MeasurementType.TEMPERATURE.getUnit())
        ));
        registerAlert(alertManager, tempSensor.addReading(
            new NumericalReading(now.minusDays(1), tempSensor.getCode(), 52, MeasurementType.TEMPERATURE.getUnit())
        ));

        // Humidity: warning and critical
        registerAlert(alertManager, humSensor.addReading(
            new NumericalReading(now.minusDays(5), humSensor.getCode(), 42, MeasurementType.HUMIDITY.getUnit())
        ));
        registerAlert(alertManager, humSensor.addReading(
            new NumericalReading(now.minusDays(1), humSensor.getCode(), 85, MeasurementType.HUMIDITY.getUnit())
        ));

        // Soil pH: near lower bound (warning) and high (critical)
        registerAlert(alertManager, phSensor.addReading(
            new NumericalReading(now.minusDays(4), phSensor.getCode(), 6.12, MeasurementType.PH.getUnit())
        ));
        registerAlert(alertManager, phSensor.addReading(
            new NumericalReading(now.minusDays(1), phSensor.getCode(), 8.0, MeasurementType.PH.getUnit())
        ));

        // Soil nitrogen: normal and critical
        registerAlert(alertManager, nitrogenSensor.addReading(
            new NumericalReading(now.minusDays(7), nitrogenSensor.getCode(), 12, MeasurementType.NITROGEN.getUnit())
        ));
        registerAlert(alertManager, nitrogenSensor.addReading(
            new NumericalReading(now.minusDays(2), nitrogenSensor.getCode(), 60, MeasurementType.NITROGEN.getUnit())
        ));

        // Water dissolved oxygen readings
        registerAlert(alertManager, oxygenSensor.addReading(
            new NumericalReading(now.minusDays(2), oxygenSensor.getCode(), 6.5, MeasurementType.DISSOLVED_OXYGEN.getUnit())
        ));
        registerAlert(alertManager, oxygenSensor.addReading(
            new NumericalReading(now.minusHours(6), oxygenSensor.getCode(), 3.2, MeasurementType.DISSOLVED_OXYGEN.getUnit())
        ));

        // Biometric readings for cow and chicken
        registerAlert(alertManager, bodyTempSensor.addReading(
            new NumericalReading(now.minusDays(2), bodyTempSensor.getCode(), 38.0, MeasurementType.BODY_TEMPERATURE.getUnit())
        ));
        registerAlert(alertManager, bodyTempSensor.addReading(
            new NumericalReading(now.minusHours(12), bodyTempSensor.getCode(), 40.0, MeasurementType.BODY_TEMPERATURE.getUnit())
        ));
        registerAlert(alertManager, chickenBio.addReading(
            new NumericalReading(now.minusHours(5), chickenBio.getCode(), 43.0, MeasurementType.BODY_TEMPERATURE.getUnit())
        ));

        // GPS readings: one inside bounds, one outside
        registerAlert(alertManager, gpsSensor.addReading(
            new GPSReading(now.minusHours(10), gpsSensor.getCode(), 35.5, -6.3)
        ));
        registerAlert(alertManager, gpsSensor.addReading(
            new GPSReading(now.minusHours(2), gpsSensor.getCode(), 36.2, -7.4)
        ));

        return new AppContext(
            farm,
            alertManager,
            sensors,
            sensorsByCode,
            cropStageHistory,
            cropsById,
            animalsById
        );
    }

    private static void addSensor(List<Sensor> sensors, Map<String, Sensor> sensorsByCode, Sensor sensor) {
        sensors.add(sensor);
        sensorsByCode.put(sensor.getCode(), sensor);
    }

    private static void registerAlert(AlertManager manager, Alert alert) {
        if (alert != null) {
            manager.registerAlert(alert);
        }
    }

    private static void recordStage(List<StageSnapshot> timeline, LocalDate date, GrowthStage stage) {
        timeline.add(new StageSnapshot(date, stage));
    }
}
