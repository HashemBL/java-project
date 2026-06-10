import java.util.List;
import java.util.Map;

public class AppContext {
    private final Farm farm;
    private final AlertManager alertManager;
    private final List<Sensor> sensors;
    private final Map<String, Sensor> sensorsByCode;
    private final Map<Integer, List<StageSnapshot>> cropStageHistory;
    private final Map<Integer, Crop> cropsById;
    private final Map<Integer, Animal> animalsById;

    public AppContext(
        Farm farm,
        AlertManager alertManager,
        List<Sensor> sensors,
        Map<String, Sensor> sensorsByCode,
        Map<Integer, List<StageSnapshot>> cropStageHistory,
        Map<Integer, Crop> cropsById,
        Map<Integer, Animal> animalsById
    ) {
        this.farm = farm;
        this.alertManager = alertManager;
        this.sensors = sensors;
        this.sensorsByCode = sensorsByCode;
        this.cropStageHistory = cropStageHistory;
        this.cropsById = cropsById;
        this.animalsById = animalsById;
    }

    public Farm getFarm() { return farm; }
    public AlertManager getAlertManager() { return alertManager; }
    public List<Sensor> getSensors() { return sensors; }
    public Map<String, Sensor> getSensorsByCode() { return sensorsByCode; }
    public Map<Integer, List<StageSnapshot>> getCropStageHistory() { return cropStageHistory; }
    public Map<Integer, Crop> getCropsById() { return cropsById; }
    public Map<Integer, Animal> getAnimalsById() { return animalsById; }
}
