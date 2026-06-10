import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainStudent1 {
    public static void main(String[] args) {
        AppContext context = createSampleData();
        ConsoleMenu menu = new ConsoleMenu(context);
        menu.run();
    }

    private static AppContext createSampleData() {
        Farm farm = new Farm("Green Valley Farm");

        CropZone cropZone = new CropZone(
                "North Fields",
                new GeographicBounds(35.8, 35.1, -6.1, -6.8)
        );
        LivestockZone livestockZone = new LivestockZone(
                "EAST PASTURE",
                new GeographicBounds(35.7, 35.2, -6.0, -6.6),
                AnimalCategory.RUMINANT,
                new FeedingProgram("Hay", 15.0, "kg", 2)
        );
        AquacultureZone aquacultureZone = new AquacultureZone(
                "South Pond",
                new GeographicBounds(35.6, 35.3, -6.2, -6.7),
                new FeedingProgram("Pellets", 2.0, "kg", 3)
        );

        farm.addZone(cropZone);
        farm.addZone(livestockZone);
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


        cropZone.addCrop(wheat);
        cropZone.addCrop(tomato);
        // Additional sample crops (cover all families)
        Crop corn = new Crop(
                CropSpecies.CORN,
                LocalDate.now().minusMonths(4),
                LocalDate.now().plusMonths(2),
                soilRequirement
        );



        cropZone.addCrop(corn);
        Crop Apple = new Crop(
                CropSpecies.APPLE,
                LocalDate.now().minusMonths(4),
                LocalDate.now().plusMonths(2),
                soilRequirement
        );



        cropZone.addCrop(Apple);


        Map<Integer, Crop> cropsById = new HashMap<>();
        for (Crop c : new Crop[]{wheat, corn, tomato, Apple}) {
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



        List<StageSnapshot> cornTimeline = new ArrayList<>();
        recordStage(cornTimeline, LocalDate.now().minusMonths(4), corn.getCurrentStage());
        corn.advanceStage();
        recordStage(cornTimeline, LocalDate.now().minusWeeks(12), corn.getCurrentStage());
        cropStageHistory.put(corn.getId(), cornTimeline);

        List<StageSnapshot> appleTimeline = new ArrayList<>();
        recordStage(appleTimeline, LocalDate.now().minusMonths(4), Apple.getCurrentStage());
        cropStageHistory.put(Apple.getId(), cornTimeline);


        Animal cow1 = new Animal(AnimalSpecies.COW, 4, 520, HealthStatus.HEALTHY);
        Animal cow2 = new Animal(AnimalSpecies.COW, 3, 480, HealthStatus.HEALTHY);
        Animal sheep = new Animal(AnimalSpecies.SHEEP, 2, 65, HealthStatus.SICK);
        livestockZone.addAnimal(cow1);
        livestockZone.addAnimal(cow2);
        livestockZone.addAnimal(sheep);

        cow1.addHealthEvent(new HealthEvent(
                LocalDateTime.now().minusDays(30),
                HealthEventType.CHECKUP,
                "Routine check"
        ));
        cow1.addHealthEvent(new HealthEvent(
                LocalDateTime.now().minusDays(15),
                HealthEventType.VACCINATION,
                "FMD vaccine"
        ));
        cow2.addHealthEvent(new HealthEvent(
                LocalDateTime.now().minusDays(7),
                HealthEventType.DISEASE,
                "Respiratory infection"
        ));
        cow2.addHealthEvent(new HealthEvent(
                LocalDateTime.now().minusDays(2),
                HealthEventType.TREATMENT,
                "Antibiotic course"
        ));


        Map<Integer, Animal> animalsById = new HashMap<>();
        for (Animal a : new Animal[]{cow1,cow2, sheep}) {
            animalsById.put(a.getId(), a);
        }


        livestockZone.addProductionRecord(new ProductionRecord(
                LocalDate.now().minusDays(47),
                110,
                "L",
                ProductionType.MILK_YIELD,
                livestockZone.getCode()
        ));
        livestockZone.addProductionRecord(new ProductionRecord(
                LocalDate.now().minusDays(16),
                125,
                "L",
                ProductionType.MILK_YIELD,
                livestockZone.getCode()
        ));
        livestockZone.addProductionRecord(new ProductionRecord(
                LocalDate.now().minusDays(2),
                118,
                "L",
                ProductionType.MILK_YIELD,
                livestockZone.getCode()
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
                cow1.getId()
        );
        GPSSensor gpsSensor = new GPSSensor(
                livestockZone.getCode(),
                cow1.getId(),
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

