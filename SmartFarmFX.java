import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SmartFarmFX extends Application {
    private AppContext context;
    private SmartFarmDataService data;
    private final SmartFarmUi ui = new SmartFarmUi();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private TableView<Zone> zoneTable;
    private TableView<Crop> cropTable;
    private TableView<Animal> animalTable;
    private TableView<ProductionRecord> productionTable;
    private TableView<Sensor> sensorTable;
    private TableView<Reading> readingTable;
    private TableView<Alert> alertTable;
    private TableView<HealthEvent> healthEventTable;
    private LineChart<Number, Number> readingChart;
    private Label overviewLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        context = Main.createSampleData();
        data = new SmartFarmDataService(context);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app");
        root.setTop(header());

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
            tab("Vue d'ensemble", overviewPane()),
            tab("Zones", zonesPane()),
            tab("Cultures", cropsPane()),
            tab("Animaux", animalsPane()),
            tab("Production", productionPane()),
            tab("Capteurs", sensorsPane()),
            tab("Relevés", readingsPane()),
            tab("Alertes", alertsPane())
        );
        root.setCenter(tabs);

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(ui.stylesheet());
        stage.setTitle("Smart Farm Manager - JavaFX");
        stage.setScene(scene);
        stage.show();
        refreshAll();
    }

    private HBox header() {
        Label title = new Label("🌿 Smart Farm Manager");
        title.getStyleClass().add("title");
        Label subtitle = new Label("Gestion ferme, zones, cultures, animaux, capteurs et alertes");
        subtitle.getStyleClass().add("subtitle");
        VBox texts = new VBox(title, subtitle);
        texts.setSpacing(4);
        HBox header = new HBox(texts);
        header.getStyleClass().add("hero");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private Tab tab(String title, VBox content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private VBox overviewPane() {
        overviewLabel = new Label();
        overviewLabel.getStyleClass().add("metric");
        TextArea requirements = new TextArea(
            "Fonctionnalités couvertes:\n" +
            "• Ajouter, modifier, désactiver et supprimer les zones.\n" +
            "• Affecter cultures/animaux aux zones et afficher le statut global.\n" +
            "• Enregistrer la production par zone.\n" +
            "• Gérer cultures, stades de croissance et exigences pédologiques.\n" +
            "• Gérer animaux, santé, événements sanitaires et alimentation par zone.\n" +
            "• Gérer capteurs, seuils, statuts, relevés, graphiques et alertes.\n" +
            "• Acquitter, supprimer et filtrer visuellement l'historique des alertes."
        );
        requirements.setEditable(false);
        requirements.setWrapText(true);
        requirements.getStyleClass().add("panel");
        return ui.page(new VBox(16, overviewLabel, requirements));
    }

    private VBox zonesPane() {
        zoneTable = ui.table();
        ui.column(zoneTable, "Code", zone -> zone.getCode());
        ui.column(zoneTable, "Type", zone -> zone.getClass().getSimpleName());
        ui.column(zoneTable, "Nom", Zone::getName);
        ui.column(zoneTable, "Statut", zone -> zone.getStatus().name());
        ui.column(zoneTable, "Entités", zone -> String.valueOf(zone.getEntityCount()));
        ui.column(zoneTable, "Résumé", Zone::getSummary);

        Button add = ui.button("Ajouter zone", () -> editZone(null));
        Button edit = ui.button("Modifier", () -> editZone(ui.selected(zoneTable)));
        Button toggle = ui.button("Activer / Désactiver", this::toggleZone);
        Button delete = ui.dangerButton("Supprimer", this::deleteZone);
        return ui.page(ui.section("Zones de la ferme", zoneTable, ui.actions(add, edit, toggle, delete)));
    }

    private VBox cropsPane() {
        cropTable = ui.table();
        ui.column(cropTable, "ID", crop -> "#" + crop.getId());
        ui.column(cropTable, "Espèce", crop -> crop.getSpecies().toString());
        ui.column(cropTable, "Zone", crop -> data.zoneNameForCrop(crop));
        ui.column(cropTable, "Plantation", crop -> crop.getPlantingDate().toString());
        ui.column(cropTable, "Récolte prévue", crop -> crop.getExpectedHarvestDate().toString());
        ui.column(cropTable, "Stade", crop -> crop.getCurrentStage().name());
        ui.column(cropTable, "Sol", crop -> crop.getSoilRequirement().toString());

        Button add = ui.button("Ajouter culture", () -> editCrop(null));
        Button edit = ui.button("Modifier", () -> editCrop(ui.selected(cropTable)));
        Button advance = ui.button("Avancer stade", this::advanceCropStage);
        Button delete = ui.dangerButton("Supprimer", this::deleteCrop);
        return ui.page(ui.section("Cultures", cropTable, ui.actions(add, edit, advance, delete)));
    }

    private VBox animalsPane() {
        animalTable = ui.table();
        ui.column(animalTable, "ID", animal -> "#" + animal.getId());
        ui.column(animalTable, "Espèce", animal -> animal.getSpecies().toString());
        ui.column(animalTable, "Zone", animal -> data.zoneNameForAnimal(animal));
        ui.column(animalTable, "Âge", animal -> animal.getAge() + " mois");
        ui.column(animalTable, "Poids", animal -> animal.getWeight() + " kg");
        ui.column(animalTable, "Santé", animal -> animal.getHealthStatus().name());

        healthEventTable = ui.table();
        ui.column(healthEventTable, "Date", event -> event.getTimestamp().format(dateTimeFormat));
        ui.column(healthEventTable, "Type", event -> event.getType().name());
        ui.column(healthEventTable, "Description", HealthEvent::getDescription);
        ui.column(healthEventTable, "Poids", event -> event.getWeightAtEvent() > 0 ? event.getWeightAtEvent() + " kg" : "-");
        animalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldAnimal, newAnimal) -> refreshHealthEvents());

        Button add = ui.button("Ajouter animal", () -> editAnimal(null));
        Button edit = ui.button("Modifier animal", () -> editAnimal(ui.selected(animalTable)));
        Button delete = ui.dangerButton("Supprimer animal", this::deleteAnimal);
        Button eventAdd = ui.button("Ajouter événement", () -> editHealthEvent(null));
        Button eventEdit = ui.button("Modifier événement", () -> editHealthEvent(ui.selected(healthEventTable)));
        Button eventDelete = ui.dangerButton("Supprimer événement", this::deleteHealthEvent);
        return ui.page(new VBox(16,
            ui.section("Animaux", animalTable, ui.actions(add, edit, delete)),
            ui.section("Événements sanitaires de l'animal sélectionné", healthEventTable, ui.actions(eventAdd, eventEdit, eventDelete))
        ));
    }

    private VBox productionPane() {
        productionTable = ui.table();
        ui.column(productionTable, "Zone", ProductionRecord::getZoneCode);
        ui.column(productionTable, "Nom zone", record -> data.zoneName(record.getZoneCode()));
        ui.column(productionTable, "Date", record -> record.getDate().toString());
        ui.column(productionTable, "Type", record -> record.getType().name());
        ui.column(productionTable, "Quantité", record -> record.getQuantity() + " " + record.getUnit());

        Button add = ui.button("Ajouter production", () -> editProduction(null));
        Button edit = ui.button("Modifier", () -> editProduction(ui.selected(productionTable)));
        Button delete = ui.dangerButton("Supprimer", this::deleteProduction);
        return ui.page(ui.section("Production par zone", productionTable, ui.actions(add, edit, delete)));
    }

    private VBox sensorsPane() {
        sensorTable = ui.table();
        ui.column(sensorTable, "Code", Sensor::getCode);
        ui.column(sensorTable, "Type", sensor -> sensor.getClass().getSimpleName());
        ui.column(sensorTable, "Mesure", sensor -> sensor.getMeasurementType().toString());
        ui.column(sensorTable, "Zone", sensor -> data.zoneName(sensor.getZoneCode()));
        ui.column(sensorTable, "Statut", sensor -> sensor.getStatus().name());
        ui.column(sensorTable, "Seuil", sensor -> sensor.getThresholdRange().toString());
        ui.column(sensorTable, "Relevés", sensor -> String.valueOf(sensor.getReadings().size()));

        Button add = ui.button("Ajouter capteur", () -> editSensor(null));
        Button edit = ui.button("Modifier seuil/zone", () -> editSensor(ui.selected(sensorTable)));
        Button status = ui.button("Changer statut", this::changeSensorStatus);
        Button delete = ui.dangerButton("Supprimer", this::deleteSensor);
        return ui.page(ui.section("Capteurs", sensorTable, ui.actions(add, edit, status, delete)));
    }

    private VBox readingsPane() {
        ComboBox<Sensor> sensorFilter = new ComboBox<>();
        sensorFilter.setItems(FXCollections.observableArrayList(context.getSensors()));
        sensorFilter.setPromptText("Filtrer par capteur");
        sensorFilter.setOnAction(event -> refreshReadings(sensorFilter.getValue()));

        readingTable = ui.table();
        ui.column(readingTable, "Date", reading -> reading.getTimestamp().format(dateTimeFormat));
        ui.column(readingTable, "Capteur", Reading::getSensorCode);
        ui.column(readingTable, "Zone", reading -> {
            Sensor sensor = context.getSensorsByCode().get(reading.getSensorCode());
            return sensor == null ? "-" : data.zoneName(sensor.getZoneCode());
        });
        ui.column(readingTable, "Valeur", Reading::getValueAsString);
        ui.column(readingTable, "Niveau", reading -> reading.getLevel().getLabel());

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Relevé #");
        yAxis.setLabel("Valeur");
        readingChart = new LineChart<>(xAxis, yAxis);
        readingChart.setLegendVisible(false);
        readingChart.setAnimated(false);
        readingChart.getStyleClass().add("chart-card");

        Button add = ui.button("Ajouter relevé", () -> addReading(sensorFilter.getValue()));
        Button delete = ui.dangerButton("Supprimer relevé", this::deleteReading);
        Button all = ui.button("Tous", () -> {
            sensorFilter.setValue(null);
            refreshReadings(null);
        });
        return ui.page(new VBox(16,
            ui.actions(new Label("Capteur:"), sensorFilter, all, add, delete),
            readingTable,
            readingChart
        ));
    }

    private VBox alertsPane() {
        ComboBox<String> filter = new ComboBox<>(FXCollections.observableArrayList("Toutes", "Actives", "Critiques", "Warnings"));
        filter.setValue("Toutes");
        filter.setOnAction(event -> refreshAlerts(filter.getValue()));

        alertTable = ui.table();
        ui.column(alertTable, "ID", alert -> "#" + alert.getId());
        ui.column(alertTable, "Sévérité", alert -> alert.getSeverity().name());
        ui.column(alertTable, "Statut", alert -> alert.getStatus().name());
        ui.column(alertTable, "Zone", alert -> data.zoneName(alert.getZoneCode()));
        ui.column(alertTable, "Capteur", Alert::getSensorCode);
        ui.column(alertTable, "Date", alert -> alert.getCreatedAt().format(dateTimeFormat));
        ui.column(alertTable, "Message", Alert::getMessage);

        Button acknowledge = ui.button("Acquitter", this::acknowledgeAlert);
        Button dismiss = ui.button("Classer", this::dismissAlert);
        Button edit = ui.button("Modifier message", this::editAlertMessage);
        Button delete = ui.dangerButton("Supprimer", this::deleteAlert);
        return ui.page(new VBox(16, ui.actions(new Label("Filtre:"), filter, acknowledge, dismiss, edit, delete), alertTable));
    }

    private void editZone(Zone zone) {
        SmartFarmForm form = new SmartFarmForm("Zone", ui);
        ComboBox<String> type = form.combo("Type", "Culture", "Élevage", "Aquaculture");
        TextField name = form.text("Nom", zone == null ? "" : zone.getName());
        TextField north = form.text("Latitude nord", zone == null ? "35.8" : String.valueOf(zone.getBounds().getNorthLat()));
        TextField south = form.text("Latitude sud", zone == null ? "35.1" : String.valueOf(zone.getBounds().getSouthLat()));
        TextField east = form.text("Longitude est", zone == null ? "-6.1" : String.valueOf(zone.getBounds().getEastLng()));
        TextField west = form.text("Longitude ouest", zone == null ? "-6.8" : String.valueOf(zone.getBounds().getWestLng()));
        ComboBox<AnimalCategory> category = form.combo("Catégorie élevage", AnimalCategory.values());
        TextField food = form.text("Aliment", "Hay");
        TextField quantity = form.text("Quantité/repas", "2");
        TextField unit = form.text("Unité", "kg");
        TextField meals = form.text("Repas/jour", "2");

        if (zone instanceof CropZone) type.setValue("Culture");
        if (zone instanceof LivestockZone) {
            type.setValue("Élevage");
            LivestockZone livestockZone = (LivestockZone) zone;
            category.setValue(livestockZone.getAnimalCategory());
            fillFeeding(livestockZone.getFeedingProgram(), food, quantity, unit, meals);
        }
        if (zone instanceof AquacultureZone) {
            type.setValue("Aquaculture");
            fillFeeding(((AquacultureZone) zone).getFeedingProgram(), food, quantity, unit, meals);
        }

        form.show(() -> {
            GeographicBounds bounds = new GeographicBounds(number(north), number(south), number(east), number(west));
            if (zone == null) {
                Zone created;
                if ("Culture".equals(type.getValue())) {
                    created = new CropZone(name.getText(), bounds);
                } else if ("Élevage".equals(type.getValue())) {
                    created = new LivestockZone(name.getText(), bounds, value(category), feeding(food, quantity, unit, meals));
                } else {
                    created = new AquacultureZone(name.getText(), bounds, feeding(food, quantity, unit, meals));
                }
                context.getFarm().addZone(created);
            } else {
                zone.setName(name.getText());
                zone.setBounds(bounds);
                if (zone instanceof LivestockZone) ((LivestockZone) zone).setFeedingProgram(feeding(food, quantity, unit, meals));
                if (zone instanceof AquacultureZone) ((AquacultureZone) zone).setFeedingProgram(feeding(food, quantity, unit, meals));
            }
            refreshAll();
        });
    }

    private void editCrop(Crop crop) {
        SmartFarmForm form = new SmartFarmForm("Culture", ui);
        ComboBox<CropZone> zoneBox = form.combo("Zone", context.getFarm().getCropZones());
        ComboBox<CropSpecies> species = form.combo("Espèce", CropSpecies.values());
        DatePicker planting = form.date("Plantation", crop == null ? LocalDate.now() : crop.getPlantingDate());
        DatePicker harvest = form.date("Récolte", crop == null ? LocalDate.now().plusMonths(2) : crop.getExpectedHarvestDate());
        ComboBox<GrowthStage> stage = form.combo("Stade", GrowthStage.values());
        TextField minPh = form.text("pH min", crop == null ? "6" : String.valueOf(crop.getSoilRequirement().getMinPH()));
        TextField maxPh = form.text("pH max", crop == null ? "7.5" : String.valueOf(crop.getSoilRequirement().getMaxPH()));
        TextField minHumidity = form.text("Humidité min", crop == null ? "40" : String.valueOf(crop.getSoilRequirement().getMinHumidity()));
        TextField maxHumidity = form.text("Humidité max", crop == null ? "70" : String.valueOf(crop.getSoilRequirement().getMaxHumidity()));
        if (crop != null) {
            species.setValue(crop.getSpecies());
            stage.setValue(crop.getCurrentStage());
            zoneBox.setValue(data.findCropZone(crop));
        }
        form.show(() -> {
            SoilRequirement soil = new SoilRequirement(number(minPh), number(maxPh), number(minHumidity), number(maxHumidity));
            if (crop == null) {
                Crop created = new Crop(value(species), planting.getValue(), harvest.getValue(), soil);
                value(zoneBox).addCrop(created);
                context.getCropsById().put(created.getId(), created);
                context.getCropStageHistory().put(created.getId(), new ArrayList<>());
            } else {
                CropZone oldZone = data.findCropZone(crop);
                CropZone newZone = value(zoneBox);
                if (oldZone != newZone) {
                    if (oldZone != null) oldZone.removeCrop(crop);
                    newZone.addCrop(crop);
                }
                crop.setSpecies(value(species));
                crop.setPlantingDate(planting.getValue());
                crop.setExpectedHarvestDate(harvest.getValue());
                crop.setCurrentStage(value(stage));
                crop.setSoilRequirement(soil);
            }
            refreshAll();
        });
    }

    private void editAnimal(Animal animal) {
        SmartFarmForm form = new SmartFarmForm("Animal", ui);
        ComboBox<Zone> zoneBox = form.combo("Zone", data.animalZones());
        ComboBox<AnimalSpecies> species = form.combo("Espèce", AnimalSpecies.values());
        TextField age = form.text("Âge (mois)", animal == null ? "12" : String.valueOf(animal.getAge()));
        TextField weight = form.text("Poids (kg)", animal == null ? "50" : String.valueOf(animal.getWeight()));
        ComboBox<HealthStatus> status = form.combo("Santé", HealthStatus.values());
        if (animal != null) {
            zoneBox.setValue(data.findAnimalZone(animal));
            species.setValue(animal.getSpecies());
            status.setValue(animal.getHealthStatus());
        }
        form.show(() -> {
            if (animal == null) {
                Animal created = new Animal(value(species), integer(age), number(weight), value(status));
                data.addAnimalToZone(value(zoneBox), created);
                context.getAnimalsById().put(created.getId(), created);
            } else {
                Zone oldZone = data.findAnimalZone(animal);
                Zone newZone = value(zoneBox);
                if (oldZone != newZone) {
                    data.removeAnimalFromZone(oldZone, animal);
                    data.addAnimalToZone(newZone, animal);
                }
                animal.setSpecies(value(species));
                animal.setAge(integer(age));
                animal.setWeight(number(weight));
                animal.setHealthStatus(value(status));
            }
            refreshAll();
        });
    }

    private void editProduction(ProductionRecord record) {
        SmartFarmForm form = new SmartFarmForm("Production", ui);
        ComboBox<Zone> zoneBox = form.combo("Zone", context.getFarm().getAllZones());
        DatePicker date = form.date("Date", record == null ? LocalDate.now() : record.getDate());
        ComboBox<ProductionType> type = form.combo("Type", ProductionType.values());
        TextField quantity = form.text("Quantité", record == null ? "100" : String.valueOf(record.getQuantity()));
        TextField unit = form.text("Unité", record == null ? "kg" : record.getUnit());
        if (record != null) {
            zoneBox.setValue(context.getFarm().findZone(record.getZoneCode()));
            type.setValue(record.getType());
        }
        form.show(() -> {
            Zone selectedZone = value(zoneBox);
            if (record == null) {
                selectedZone.addProductionRecord(new ProductionRecord(date.getValue(), number(quantity), unit.getText(), value(type), selectedZone.getCode()));
            } else {
                Zone oldZone = context.getFarm().findZone(record.getZoneCode());
                if (oldZone != selectedZone) {
                    if (oldZone != null) oldZone.removeProductionRecord(record);
                    selectedZone.addProductionRecord(record);
                }
                record.setDate(date.getValue());
                record.setQuantity(number(quantity));
                record.setUnit(unit.getText());
                record.setType(value(type));
                record.setZoneCode(selectedZone.getCode());
            }
            refreshAll();
        });
    }

    private void editSensor(Sensor sensor) {
        SmartFarmForm form = new SmartFarmForm("Capteur", ui);
        ComboBox<String> kind = form.combo("Type", "Environnement", "Sol", "Eau", "Biométrique", "GPS");
        ComboBox<Zone> zone = form.combo("Zone", context.getFarm().getAllZones());
        ComboBox<MeasurementType> measure = form.combo("Mesure", MeasurementType.values());
        TextField min = form.text("Seuil min", sensor == null ? "0" : String.valueOf(sensor.getThresholdRange().getMin()));
        TextField max = form.text("Seuil max", sensor == null ? "100" : String.valueOf(sensor.getThresholdRange().getMax()));
        TextField unit = form.text("Unité", sensor == null ? "" : sensor.getThresholdRange().getUnit());
        TextField animalId = form.text("Animal ID", "1");
        if (sensor != null) {
            zone.setValue(context.getFarm().findZone(sensor.getZoneCode()));
            measure.setValue(sensor.getMeasurementType());
            if (sensor instanceof EnvironmentalSensor) kind.setValue("Environnement");
            if (sensor instanceof SoilSensor) kind.setValue("Sol");
            if (sensor instanceof WaterSensor) kind.setValue("Eau");
            if (sensor instanceof BiometricSensor) {
                kind.setValue("Biométrique");
                animalId.setText(String.valueOf(((BiometricSensor) sensor).getAnimalId()));
            }
            if (sensor instanceof GPSSensor) {
                kind.setValue("GPS");
                animalId.setText(String.valueOf(((GPSSensor) sensor).getAnimalId()));
            }
        }
        form.show(() -> {
            Zone selectedZone = value(zone);
            if (sensor == null) {
                Sensor created = createSensor(kind.getValue(), selectedZone, value(measure), number(min), number(max), unit.getText(), integer(animalId));
                context.getSensors().add(created);
                context.getSensorsByCode().put(created.getCode(), created);
            } else {
                sensor.setZoneCode(selectedZone.getCode());
                sensor.setThresholdRange(new ThresholdRange(number(min), number(max), unit.getText()));
            }
            refreshAll();
        });
    }

    private void addReading(Sensor filteredSensor) {
        Sensor target = filteredSensor == null ? ui.selected(sensorTable) : filteredSensor;
        if (target == null && !context.getSensors().isEmpty()) target = context.getSensors().get(0);
        if (target == null) return;

        SmartFarmForm form = new SmartFarmForm("Relevé pour " + target.getCode(), ui);
        TextField value = form.text(target.getMeasurementType() == MeasurementType.GPS_POSITION ? "Latitude" : "Valeur", "25");
        TextField longitude = form.text("Longitude (GPS)", "-6.3");
        Sensor finalTarget = target;
        form.show(() -> {
            Reading reading;
            if (finalTarget instanceof GPSSensor) {
                reading = new GPSReading(LocalDateTime.now(), finalTarget.getCode(), number(value), number(longitude));
            } else {
                reading = new NumericalReading(LocalDateTime.now(), finalTarget.getCode(), number(value), finalTarget.getThresholdRange().getUnit());
            }
            context.getAlertManager().registerAlert(finalTarget.addReading(reading));
            refreshAll();
        });
    }

    private void editHealthEvent(HealthEvent event) {
        Animal animal = ui.selected(animalTable);
        if (animal == null) return;
        SmartFarmForm form = new SmartFarmForm("Événement sanitaire", ui);
        ComboBox<HealthEventType> type = form.combo("Type", HealthEventType.values());
        TextField description = form.text("Description", event == null ? "" : event.getDescription());
        TextField weight = form.text("Poids à l'événement", event == null ? "0" : String.valueOf(event.getWeightAtEvent()));
        if (event != null) type.setValue(event.getType());
        form.show(() -> {
            if (event == null) {
                animal.addHealthEvent(new HealthEvent(LocalDateTime.now(), value(type), description.getText(), number(weight)));
            } else {
                animal.removeHealthEvent(event);
                event.setType(value(type));
                event.setDescription(description.getText());
                event.setWeightAtEvent(number(weight));
                animal.addHealthEvent(event);
            }
            refreshAll();
        });
    }

    private void editAlertMessage() {
        Alert alert = ui.selected(alertTable);
        if (alert == null) return;
        SmartFarmForm form = new SmartFarmForm("Message alerte", ui);
        TextField message = form.text("Message", alert.getMessage());
        form.show(() -> {
            alert.setMessage(message.getText());
            refreshAll();
        });
    }

    private Sensor createSensor(String kind, Zone zone, MeasurementType measurement, double min, double max, String unit, int animalId) {
        if ("Environnement".equals(kind)) return new EnvironmentalSensor(zone.getCode(), measurement, new ThresholdRange(min, max, unit));
        if ("Sol".equals(kind)) return new SoilSensor(zone.getCode(), measurement, new ThresholdRange(min, max, unit));
        if ("Eau".equals(kind)) return new WaterSensor(zone.getCode(), measurement, new ThresholdRange(min, max, unit));
        if ("Biométrique".equals(kind)) return new BiometricSensor(zone.getCode(), measurement, new ThresholdRange(min, max, unit), animalId);
        return new GPSSensor(zone.getCode(), animalId, zone.getBounds().getNorthLat(), zone.getBounds().getSouthLat(), zone.getBounds().getEastLng(), zone.getBounds().getWestLng());
    }

    private void refreshAll() {
        refreshOverview();
        if (zoneTable != null) zoneTable.setItems(FXCollections.observableArrayList(context.getFarm().getAllZones()));
        if (cropTable != null) cropTable.setItems(FXCollections.observableArrayList(data.allCrops()));
        if (animalTable != null) animalTable.setItems(FXCollections.observableArrayList(data.allAnimals()));
        if (productionTable != null) productionTable.setItems(FXCollections.observableArrayList(data.allProduction()));
        if (sensorTable != null) sensorTable.setItems(FXCollections.observableArrayList(context.getSensors()));
        refreshReadings(null);
        refreshAlerts("Toutes");
        refreshHealthEvents();
    }

    private void refreshOverview() {
        if (overviewLabel == null) return;
        overviewLabel.setText(
            context.getFarm().getName() + "\n" +
            "Zones: " + context.getFarm().getAllZones().size() +
            "  • Cultures: " + data.allCrops().size() +
            "  • Animaux: " + data.allAnimals().size() +
            "  • Capteurs: " + context.getSensors().size() +
            "  • Alertes actives: " + context.getAlertManager().getActiveAlerts().size()
        );
    }

    private void refreshReadings(Sensor onlySensor) {
        if (readingTable == null) return;
        List<Reading> readings = new ArrayList<>();
        for (Sensor sensor : context.getSensors()) {
            if (onlySensor == null || sensor == onlySensor) readings.addAll(sensor.getReadings());
        }
        readings.sort(Comparator.comparing(Reading::getTimestamp).reversed());
        readingTable.setItems(FXCollections.observableArrayList(readings));
        refreshChart(readings);
    }

    private void refreshChart(List<Reading> readings) {
        if (readingChart == null) return;
        readingChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int index = 1;
        for (int position = readings.size() - 1; position >= 0; position--) {
            Reading reading = readings.get(position);
            if (reading instanceof NumericalReading) {
                series.getData().add(new XYChart.Data<>(index++, ((NumericalReading) reading).getValue()));
            }
        }
        readingChart.getData().add(series);
    }

    private void refreshAlerts(String mode) {
        if (alertTable == null) return;
        List<Alert> alerts = new ArrayList<>(context.getAlertManager().getAllAlerts());
        alerts.sort(Alert::compareTo);
        alerts.removeIf(alert ->
            ("Actives".equals(mode) && alert.getStatus() != AlertStatus.ACTIVE) ||
            ("Critiques".equals(mode) && alert.getSeverity() != SeverityLevel.CRITICAL) ||
            ("Warnings".equals(mode) && alert.getSeverity() != SeverityLevel.WARNING)
        );
        alertTable.setItems(FXCollections.observableArrayList(alerts));
    }

    private void refreshHealthEvents() {
        if (healthEventTable == null) return;
        Animal animal = ui.selected(animalTable);
        healthEventTable.setItems(FXCollections.observableArrayList(animal == null ? Collections.emptyList() : animal.getHealthHistory()));
    }

    private void toggleZone() {
        Zone zone = ui.selected(zoneTable);
        if (zone == null) return;
        try {
            if (zone.isActive()) zone.suspend(); else zone.activate();
        } catch (AlreadySuspended exception) {
            ui.message("Zone déjà suspendue");
        }
        refreshAll();
    }

    private void changeSensorStatus() {
        Sensor sensor = ui.selected(sensorTable);
        if (sensor == null) return;
        if (sensor.getStatus() == SensorStatus.ACTIVE) sensor.suspend();
        else if (sensor.getStatus() == SensorStatus.SUSPENDED) sensor.markFaulty();
        else sensor.activate();
        refreshAll();
    }

    private void advanceCropStage() {
        Crop crop = ui.selected(cropTable);
        if (crop == null) return;
        crop.advanceStage();
        context.getCropStageHistory().computeIfAbsent(crop.getId(), ignored -> new ArrayList<>()).add(new StageSnapshot(LocalDate.now(), crop.getCurrentStage()));
        refreshAll();
    }

    private void acknowledgeAlert() {
        Alert alert = ui.selected(alertTable);
        if (alert != null) {
            alert.acknowledge();
            refreshAll();
        }
    }

    private void dismissAlert() {
        Alert alert = ui.selected(alertTable);
        if (alert != null) {
            alert.dismiss();
            refreshAll();
        }
    }

    private void deleteZone() {
        data.deleteZone(ui.selected(zoneTable));
        refreshAll();
    }

    private void deleteCrop() {
        data.deleteCrop(ui.selected(cropTable));
        refreshAll();
    }

    private void deleteAnimal() {
        data.deleteAnimal(ui.selected(animalTable));
        refreshAll();
    }

    private void deleteProduction() {
        data.deleteProduction(ui.selected(productionTable));
        refreshAll();
    }

    private void deleteSensor() {
        data.deleteSensor(ui.selected(sensorTable));
        refreshAll();
    }

    private void deleteReading() {
        data.deleteReading(ui.selected(readingTable));
        refreshAll();
    }

    private void deleteAlert() {
        data.deleteAlert(ui.selected(alertTable));
        refreshAll();
    }

    private void deleteHealthEvent() {
        data.deleteHealthEvent(ui.selected(animalTable), ui.selected(healthEventTable));
        refreshAll();
    }

    private void fillFeeding(FeedingProgram feeding, TextField food, TextField quantity, TextField unit, TextField meals) {
        food.setText(feeding.getFoodType());
        quantity.setText(String.valueOf(feeding.getQuantityPerMeal()));
        unit.setText(feeding.getUnit());
        meals.setText(String.valueOf(feeding.getMealsPerDay()));
    }

    private FeedingProgram feeding(TextField food, TextField quantity, TextField unit, TextField meals) {
        return new FeedingProgram(food.getText(), number(quantity), unit.getText(), integer(meals));
    }

    private <T> T value(ComboBox<T> comboBox) {
        T value = comboBox.getValue();
        if (value == null && !comboBox.getItems().isEmpty()) return comboBox.getItems().get(0);
        return value;
    }

    private double number(TextField field) {
        return Double.parseDouble(field.getText().trim().replace(",", "."));
    }

    private int integer(TextField field) {
        return Integer.parseInt(field.getText().trim());
    }
}

