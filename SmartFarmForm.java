import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SmartFarmForm {
    private final SmartFarmUi ui;
    private final Stage stage = new Stage();
    private final GridPane grid = new GridPane();
    private int row = 0;

    public SmartFarmForm(String title, SmartFarmUi ui) {
        this.ui = ui;
        stage.setTitle(title);
        grid.setPadding(new Insets(18));
        grid.setHgap(12);
        grid.setVgap(12);
        grid.getStyleClass().add("card");
    }

    public TextField text(String label, String value) {
        TextField field = new TextField(value);
        add(label, field);
        return field;
    }

    public DatePicker date(String label, LocalDate value) {
        DatePicker picker = new DatePicker(value);
        add(label, picker);
        return picker;
    }

    @SafeVarargs
    public final <T> ComboBox<T> combo(String label, T... values) {
        return combo(label, Arrays.asList(values));
    }

    public <T> ComboBox<T> combo(String label, List<T> values) {
        ComboBox<T> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
        if (!values.isEmpty()) comboBox.setValue(values.get(0));
        add(label, comboBox);
        return comboBox;
    }

    public <T> T value(ComboBox<T> comboBox) {
        T value = comboBox.getValue();
        if (value == null && !comboBox.getItems().isEmpty()) return comboBox.getItems().get(0);
        return value;
    }

    public double number(TextField field) {
        return Double.parseDouble(field.getText().trim().replace(",", "."));
    }

    public int integer(TextField field) {
        return Integer.parseInt(field.getText().trim());
    }

    public FeedingProgram feeding(TextField food, TextField quantity, TextField unit, TextField meals) {
        return new FeedingProgram(food.getText(), number(quantity), unit.getText(), integer(meals));
    }

    public void fillFeeding(FeedingProgram feeding, TextField food, TextField quantity, TextField unit, TextField meals) {
        food.setText(feeding.getFoodType());
        quantity.setText(String.valueOf(feeding.getQuantityPerMeal()));
        unit.setText(feeding.getUnit());
        meals.setText(String.valueOf(feeding.getMealsPerDay()));
    }

    private void add(String label, javafx.scene.Node node) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("section-title");
        grid.add(labelNode, 0, row);
        grid.add(node, 1, row++);
    }

    public void show(Runnable saveAction) {
        Button save = ui.button("Enregistrer", () -> {
            saveAction.run();
            stage.close();
        });
        Button cancel = ui.dangerButton("Annuler", stage::close);
        grid.add(ui.actions(save, cancel), 1, row);
        Scene scene = new Scene(grid, 520, Math.min(760, 110 + row * 52));
        scene.getStylesheets().add(ui.stylesheet());
        stage.setScene(scene);
        stage.showAndWait();
    }
}
