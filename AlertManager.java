import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central manager for all alerts in the system.
 * Stores the full alert history and provides filtering/display capabilities.
 */
public class AlertManager {
    private List<Alert> allAlerts;

    public AlertManager() {
        this.allAlerts = new ArrayList<>();
    }

    public void registerAlert(Alert alert) {
        if (alert != null) {
            allAlerts.add(alert);
        }
    }

    /** Returns active alerts sorted by severity (CRITICAL first). */
    public List<Alert> getActiveAlerts() {
        List<Alert> active = new ArrayList<>();
        for (Alert a : allAlerts) {
            if (a.getStatus() == AlertStatus.ACTIVE) active.add(a);
        }
        Collections.sort(active);
        return active;
    }

    public List<Alert> filterByZone(String zoneCode) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : allAlerts) {
            if (a.getZoneCode().equals(zoneCode)) result.add(a);
        }
        return result;
    }

    public List<Alert> filterBySensor(String sensorCode) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : allAlerts) {
            if (a.getSensorCode().equals(sensorCode)) result.add(a);
        }
        return result;
    }

    public List<Alert> filterBySeverity(SeverityLevel severity) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : allAlerts) {
            if (a.getSeverity() == severity) result.add(a);
        }
        return result;
    }

    public List<Alert> filterByPeriod(LocalDateTime from, LocalDateTime to) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : allAlerts) {
            if (!a.getCreatedAt().isBefore(from) && !a.getCreatedAt().isAfter(to)) {
                result.add(a);
            }
        }
        return result;
    }

    public void acknowledgeAlert(int alertId) {
        for (Alert a : allAlerts) {
            if (a.getId() == alertId) {
                a.acknowledge();
                return;
            }
        }
        System.out.println("Alert #" + alertId + " not found.");
    }

    public void dismissAlert(int alertId) {
        for (Alert a : allAlerts) {
            if (a.getId() == alertId) {
                a.dismiss();
                return;
            }
        }
        System.out.println("Alert #" + alertId + " not found.");
    }

    public boolean deleteAlert(int alertId) {
        return allAlerts.removeIf(a -> a.getId() == alertId);
    }

    public void printActiveAlerts() {
        List<Alert> active = getActiveAlerts();
        System.out.println("=== ACTIVE ALERTS (" + active.size() + ") ===");
        for (Alert a : active) {
            System.out.println("  " + a);
        }
    }

    public List<Alert> getAllAlerts() {
        return new ArrayList<>(allAlerts);
    }
}
