import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final AppContext context;
    private final Scanner scanner;

    public ConsoleMenu(AppContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    runFarmMenu();
                    break;
                case 2:
                    runSensorMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1) Farm and Entities");
        System.out.println("2) Sensors and Alerts");
        System.out.println("0) Exit");
    }

    private void runFarmMenu() {
        boolean running = true;
        while (running) {
            printFarmMenu();
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    handleZoneProductionHistory();
                    break;
                case 2:
                    handleAnimalHealthHistory();
                    break;
                case 3:
                    handleCropStageTimeline();
                    break;
                case 4:
                    handleFilterCropsByFamily();
                    break;
                case 5:
                    handleFilterAnimalsByStatus();
                    break;
                case 6:
                    handleFilterProductionByTypeAndPeriod();
                    break;
                case 7:
                    handleFarmStatusReport();
                    break;
                case 8:
                    handleTotalProductionPerZone();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void printFarmMenu() {
        System.out.println("\n=== Farm and Entities ===");
        System.out.println("1) Zone production history by date range");
        System.out.println("2) Animal health event history");
        System.out.println("3) Crop growth stage timeline");
        System.out.println("4) Filter crops by family");
        System.out.println("5) Filter animals by health status");
        System.out.println("6) Filter productions by type and period");
        System.out.println("7) Farm status report");
        System.out.println("8) Total production per zone in period");
        System.out.println("0) Back");
    }

    private void runSensorMenu() {
        boolean running = true;
        while (running) {
            printSensorMenu();
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    handleSensorReadingHistory();
                    break;
                case 2:
                    handleAlertHistory();
                    break;
                case 3:
                    handleSensorStatistics();
                    break;
                case 4:
                    handleFilterAlerts();
                    break;
                case 5:
                    handleFilterReadingsByLevel();
                    break;
                case 6:
                    handleZoneSensorDashboard();
                    break;
                case 7:
                    handleActiveAlerts();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void printSensorMenu() {
        System.out.println("\n=== Sensors and Alerts ===");
        System.out.println("1) Sensor reading history by date range");
        System.out.println("2) Alert history");
        System.out.println("3) Sensor statistics (min, max, avg)");
        System.out.println("4) Filter alerts by criteria");
        System.out.println("5) Filter readings by level");
        System.out.println("6) Zone sensor dashboard");
        System.out.println("7) Active alerts sorted by severity");
        System.out.println("0) Back");
    }

    private void handleZoneProductionHistory() {
        listZones();
        String zoneCode = readLine("Zone code: ").trim();
        Zone zone = context.getFarm().findZone(zoneCode);
        if (zone == null) {
            System.out.println("Zone not found.");
            return;
        }

        LocalDate from = readDate("From date (YYYY-MM-DD): ");
        LocalDate to = readDate("To date (YYYY-MM-DD): ");
        LocalDateRange range = normalizeRange(from, to);
        System.out.println("Production history for zone " + zone.getCode() + " from "
            + range.from + " to " + range.to + ":");
        for (ProductionRecord record : zone.getProductionHistory()) {
            if (!record.getDate().isBefore(range.from) && !record.getDate().isAfter(range.to)) {
                System.out.println("  " + record);
            }

        }
    }

    private void handleAnimalHealthHistory() {
        listAnimals();
        int animalId = readInt("Animal ID: ");
        Animal animal = context.getAnimalsById().get(animalId);
        if (animal == null) {
            System.out.println("Animal not found.");
            return;
        }
        System.out.println("Health history for animal #" + animal.getId() + ":");
        for (HealthEvent event : animal.getHealthHistory()) {
            System.out.println("  " + event);
        }
    }

    private void handleCropStageTimeline() {
        listCrops();
        int cropId = readInt("Crop ID: ");
        Crop crop = context.getCropsById().get(cropId);
        if (crop == null) {
            System.out.println("Crop not found.");
            return;
        }
        List<StageSnapshot> timeline = context.getCropStageHistory().get(cropId);
        if (timeline == null || timeline.isEmpty()) {
            System.out.println("No stage history for crop #" + cropId + ".");
            return;
        }
        System.out.println("Growth stage timeline for crop #" + crop.getId() + " (" + crop.getSpecies().getCommonName() + "):");
        for (StageSnapshot snapshot : timeline) {
            System.out.println("  " + snapshot);
        }
    }

    private void handleFilterCropsByFamily() {
        CropFamily family = chooseCropFamily();
        List<CropZone> zones = context.getFarm().getCropZones();
        if (zones.isEmpty()) {
            System.out.println("No crop zones available.");
            return;
        }
        for (CropZone zone : zones) {
            System.out.println("Crops in " + zone.getName() + " filtered by family " + family + ":");
            List<Crop> crops = zone.getCropsByFamily(family);
            if (crops.isEmpty()) {
                System.out.println("  (none)");
            } else {
                for (Crop crop : crops) {
                    System.out.println("  " + crop);
                }
            }
        }
    }

    private void handleFilterAnimalsByStatus() {
        HealthStatus status = chooseHealthStatus();
        System.out.println("Animals filtered by health status " + status + ":");
        boolean found = false;
        for (Animal animal : context.getAnimalsById().values()) {
            if (animal.getHealthStatus() == status) {
                System.out.println("  " + animal);
                found = true;
            }
        }
        if (!found) {
            System.out.println("  (none)");
        }
    }

    private void handleFilterProductionByTypeAndPeriod() {
        listZones();
        String zoneCode = readLine("Zone code: ").trim();
        Zone zone = context.getFarm().findZone(zoneCode);
        if (zone == null) {
            System.out.println("Zone not found.");
            return;
        }
        ProductionType type = chooseProductionType();
        LocalDate from = readDate("From date (YYYY-MM-DD): ");
        LocalDate to = readDate("To date (YYYY-MM-DD): ");
        LocalDateRange range = normalizeRange(from, to);
        System.out.println("Productions for zone " + zone.getCode() + " filtered by type " + type
            + " and period " + range.from + " to " + range.to + ":");
        boolean found = false;
        for (ProductionRecord record : zone.getProductionHistory()) {
            if (record.getType() == type
                && !record.getDate().isBefore(range.from)
                && !record.getDate().isAfter(range.to)) {
                System.out.println("  " + record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("  (none)");
        }
    }

    private void handleFarmStatusReport() {
        Farm farm = context.getFarm();
        System.out.println("Farm status report for " + farm.getName() + ":");
        for (Zone zone : farm.getAllZones()) {
            System.out.println("- " + zone.getSummary() + " [" + zone.getStatus() + "]");
            if (zone instanceof CropZone) {
                System.out.println(((CropZone) zone).getCropStatusReport());
            } else if (zone instanceof LivestockZone) {
                LivestockZone livestockZone = (LivestockZone) zone;
                for (Animal animal : livestockZone.getAnimals()) {
                    System.out.println("  " + animal.getHealthReport());
                }
            } else if (zone instanceof AquacultureZone) {
                AquacultureZone aquacultureZone = (AquacultureZone) zone;
                for (Animal animal : aquacultureZone.getSpecimens()) {
                    System.out.println("  " + animal.getHealthReport());
                }
            }
        }
    }

    private void handleTotalProductionPerZone() {
        LocalDate from = readDate("From date (YYYY-MM-DD): ");
        LocalDate to = readDate("To date (YYYY-MM-DD): ");
        LocalDateRange range = normalizeRange(from, to);
        System.out.println("Total production per zone from " + range.from + " to " + range.to + ":");
        for (Zone zone : context.getFarm().getAllZones()) {
            Map<String, Double> totalsByUnit = new java.util.HashMap<>();
            for (ProductionRecord record : zone.getProductionHistory()) {
                if (!record.getDate().isBefore(range.from) && !record.getDate().isAfter(range.to)) {
                    totalsByUnit.put(
                        record.getUnit(),
                        totalsByUnit.getOrDefault(record.getUnit(), 0.0) + record.getQuantity()
                    );
                }
            }
            if (totalsByUnit.isEmpty()) {
                System.out.println("  " + zone.getCode() + ": no production in period");
            } else {
                for (Map.Entry<String, Double> entry : totalsByUnit.entrySet()) {
                    System.out.println("  " + zone.getCode() + ": " + entry.getValue() + " " + entry.getKey());
                }
            }
        }
    }

    private void handleSensorReadingHistory() {
        listSensors();
        String sensorCode = readLine("Sensor code: ").trim();
        Sensor sensor = context.getSensorsByCode().get(sensorCode);
        if (sensor == null) {
            System.out.println("Sensor not found.");
            return;
        }
        LocalDateTime from = readDateTime("From date-time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime to = readDateTime("To date-time (YYYY-MM-DDTHH:MM): ");
        DateTimeRange range = normalizeRange(from, to);
        System.out.println("Reading history for sensor " + sensor.getCode() + " from "
            + range.from + " to " + range.to + ":");
        boolean found = false;
        for (Reading reading : sensor.getReadings()) {
            if (!reading.getTimestamp().isBefore(range.from) && !reading.getTimestamp().isAfter(range.to)) {
                System.out.println("  " + reading);
                found = true;
            }
        }
        if (!found) {
            System.out.println("  (none)");
        }
    }

    private void handleAlertHistory() {
        printAlerts("All alerts", context.getAlertManager().getAllAlerts());
    }

    private void handleSensorStatistics() {
        listSensors();
        String sensorCode = readLine("Sensor code: ").trim();
        Sensor sensor = context.getSensorsByCode().get(sensorCode);
        if (sensor == null) {
            System.out.println("Sensor not found.");
            return;
        }
        printSensorStats(sensor);
    }

    private void handleFilterAlerts() {
        listZones();
        String zoneInput = readLine("Zone code (leave blank for any): ").trim();
        String zoneCode = zoneInput.isEmpty() ? null : zoneInput;
        Class<? extends Sensor> sensorType = chooseSensorTypeOptional();
        SeverityLevel severity = chooseSeverityOptional();
        LocalDateTime from = readOptionalDateTime("From date-time (YYYY-MM-DDTHH:MM, blank to skip): ");
        LocalDateTime to = readOptionalDateTime("To date-time (YYYY-MM-DDTHH:MM, blank to skip): ");
        DateTimeRange range = normalizeOptionalRange(from, to);

        List<Alert> filtered = filterAlerts(
            context.getAlertManager().getAllAlerts(),
            zoneCode,
            sensorType,
            severity,
            range.from,
            range.to,
            context.getSensorsByCode()
        );
        printAlerts("Filtered alerts", filtered);
    }

    private void handleFilterReadingsByLevel() {
        listSensors();
        String sensorCode = readLine("Sensor code: ").trim();
        Sensor sensor = context.getSensorsByCode().get(sensorCode);
        if (sensor == null) {
            System.out.println("Sensor not found.");
            return;
        }
        ReadingLevel level = chooseReadingLevel();
        System.out.println("Readings for sensor " + sensor.getCode() + " filtered by level " + level + ":");
        boolean found = false;
        for (Reading reading : sensor.getReadings()) {
            if (reading.getLevel() == level) {
                System.out.println("  " + reading);
                found = true;
            }
        }
        if (!found) {
            System.out.println("  (none)");
        }
    }

    private void handleZoneSensorDashboard() {
        listZones();
        String zoneCode = readLine("Zone code: ").trim();
        printZoneSensorDashboard(zoneCode, context.getSensors(), context.getAlertManager());
    }

    private void handleActiveAlerts() {
        printAlerts("Active alerts sorted by severity", context.getAlertManager().getActiveAlerts());
    }

    private void listZones() {
        System.out.println("Available zones:");
        for (Zone zone : context.getFarm().getAllZones()) {
            System.out.println("  " + zone.getCode() + " - " + zone.getName());
        }
    }

    private void listSensors() {
        System.out.println("Available sensors:");
        for (Sensor sensor : context.getSensors()) {
            System.out.println("  " + sensor.getCode() + " - " + sensor.getClass().getSimpleName()
                + " (zone " + sensor.getZoneCode() + ")");
        }
    }

    private void listAnimals() {
        System.out.println("Available animals:");
        for (Animal animal : context.getAnimalsById().values()) {
            System.out.println("  " + animal.getId() + " - " + animal.getSpecies().getCommonName()
                + " (" + animal.getHealthStatus() + ")");
        }
    }

    private void listCrops() {
        System.out.println("Available crops:");
        for (Crop crop : context.getCropsById().values()) {
            System.out.println("  " + crop.getId() + " - " + crop.getSpecies().getCommonName()
                + " (" + crop.getCurrentStage() + ")");
        }
    }

    private CropFamily chooseCropFamily() {
        CropFamily[] values = CropFamily.values();
        System.out.println("Crop families:");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ") " + values[i]);
        }
        while (true) {
            int choice = readInt("Select family: ");
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid choice.");
        }
    }

    private HealthStatus chooseHealthStatus() {
        HealthStatus[] values = HealthStatus.values();
        System.out.println("Health status options:");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ") " + values[i]);
        }
        while (true) {
            int choice = readInt("Select status: ");
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid choice.");
        }
    }

    private ProductionType chooseProductionType() {
        ProductionType[] values = ProductionType.values();
        System.out.println("Production types:");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ") " + values[i]);
        }
        while (true) {
            int choice = readInt("Select production type: ");
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid choice.");
        }
    }

    private ReadingLevel chooseReadingLevel() {
        ReadingLevel[] values = ReadingLevel.values();
        System.out.println("Reading levels:");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ") " + values[i]);
        }
        while (true) {
            int choice = readInt("Select reading level: ");
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid choice.");
        }
    }

    private SeverityLevel chooseSeverityOptional() {
        SeverityLevel[] values = SeverityLevel.values();
        System.out.println("Severity filter:");
        System.out.println("  0) Any");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ") " + values[i]);
        }
        while (true) {
            int choice = readInt("Select severity: ");
            if (choice == 0) {
                return null;
            }
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid choice.");
        }
    }

    private Class<? extends Sensor> chooseSensorTypeOptional() {
        System.out.println("Sensor type filter:");
        System.out.println("  0) Any");
        System.out.println("  1) EnvironmentalSensor");
        System.out.println("  2) SoilSensor");
        System.out.println("  3) WaterSensor");
        System.out.println("  4) BiometricSensor");
        System.out.println("  5) GPSSensor");
        while (true) {
            int choice = readInt("Select sensor type: ");
            switch (choice) {
                case 0:
                    return null;
                case 1:
                    return EnvironmentalSensor.class;
                case 2:
                    return SoilSensor.class;
                case 3:
                    return WaterSensor.class;
                case 4:
                    return BiometricSensor.class;
                case 5:
                    return GPSSensor.class;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            try {
                return LocalDateTime.parse(input);
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date-time format. Use YYYY-MM-DDTHH:MM.");
            }
        }
    }

    private LocalDateTime readOptionalDateTime(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalDateTime.parse(input);
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date-time format. Use YYYY-MM-DDTHH:MM or leave blank.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number.");
            }
        }
    }

    private LocalDateRange normalizeRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            return new LocalDateRange(to, from);
        }
        return new LocalDateRange(from, to);
    }

    private DateTimeRange normalizeRange(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            return new DateTimeRange(to, from);
        }
        return new DateTimeRange(from, to);
    }

    private DateTimeRange normalizeOptionalRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            return new DateTimeRange(from, to);
        }
        if (from.isAfter(to)) {
            return new DateTimeRange(to, from);
        }
        return new DateTimeRange(from, to);
    }

    private void printSensorStats(Sensor sensor) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double sum = 0;
        int count = 0;

        for (Reading reading : sensor.getReadings()) {
            if (reading instanceof NumericalReading) {
                double value = ((NumericalReading) reading).getValue();
                min = Math.min(min, value);
                max = Math.max(max, value);
                sum += value;
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No numerical readings for sensor " + sensor.getCode());
            return;
        }

        double avg = sum / count;
        System.out.println("Stats for " + sensor.getCode() + " (" + sensor.getMeasurementType().getLabel() + "):");
        System.out.println("  Min: " + min + " " + sensor.getMeasurementType().getUnit());
        System.out.println("  Max: " + max + " " + sensor.getMeasurementType().getUnit());
        System.out.println("  Avg: " + String.format("%.2f", avg) + " " + sensor.getMeasurementType().getUnit());
    }

    private List<Alert> filterAlerts(
        List<Alert> alerts,
        String zoneCode,
        Class<? extends Sensor> sensorType,
        SeverityLevel severity,
        LocalDateTime from,
        LocalDateTime to,
        Map<String, Sensor> sensorsByCode
    ) {
        List<Alert> result = new ArrayList<>();
        for (Alert alert : alerts) {
            if (zoneCode != null && !alert.getZoneCode().equals(zoneCode)) {
                continue;
            }
            if (severity != null && alert.getSeverity() != severity) {
                continue;
            }
            if (from != null && alert.getCreatedAt().isBefore(from)) {
                continue;
            }
            if (to != null && alert.getCreatedAt().isAfter(to)) {
                continue;
            }
            if (sensorType != null) {
                Sensor sensor = sensorsByCode.get(alert.getSensorCode());
                if (sensor == null || !sensorType.isInstance(sensor)) {
                    continue;
                }
            }
            result.add(alert);
        }
        return result;
    }

    private void printAlerts(String title, List<Alert> alerts) {
        System.out.println(title + " (" + alerts.size() + "):");
        for (Alert alert : alerts) {
            System.out.println("  " + alert);
        }
    }

    private void printZoneSensorDashboard(
        String zoneCode,
        List<Sensor> sensors,
        AlertManager alertManager
    ) {
        System.out.println("Zone sensor dashboard for " + zoneCode + ":");
        for (Sensor sensor : sensors) {
            if (!sensor.getZoneCode().equals(zoneCode)) {
                continue;
            }
            Reading latest = sensor.getLatestReading();
            String latestSummary = (latest == null)
                ? "no readings"
                : latest.getValueAsString() + " (" + latest.getLevel() + ")";
            System.out.println("  " + sensor.getClass().getSimpleName()
                + " " + sensor.getCode()
                + " status=" + sensor.getStatus()
                + " latest=" + latestSummary);
        }
        System.out.println("  Active alerts: " + alertManager.getActiveAlerts().size());
    }

    private static class LocalDateRange {
        private final LocalDate from;
        private final LocalDate to;

        private LocalDateRange(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }
    }

    private static class DateTimeRange {
        private final LocalDateTime from;
        private final LocalDateTime to;

        private DateTimeRange(LocalDateTime from, LocalDateTime to) {
            this.from = from;
            this.to = to;
        }
    }
}
