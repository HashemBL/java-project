import java.time.LocalDate;

public class StageSnapshot {
    private final LocalDate date;
    private final GrowthStage stage;

    public StageSnapshot(LocalDate date, GrowthStage stage) {
        this.date = date;
        this.stage = stage;
    }

    public LocalDate getDate() { return date; }
    public GrowthStage getStage() { return stage; }

    @Override
    public String toString() {
        return date + " -> " + stage;
    }
}
