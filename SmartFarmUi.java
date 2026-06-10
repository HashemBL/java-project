import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SmartFarmUi {
    public interface TextMapper<T> {
        String map(T value);
    }

    public <T> TableView<T> table() {
        TableView<T> table = new TableView<>();
        table.getStyleClass().add("data-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        return table;
    }

    public <T> void column(TableView<T> table, String title, TextMapper<T> mapper) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(mapper.map(data.getValue())));
        table.getColumns().add(column);
    }

    public VBox page(VBox content) {
        content.setPadding(new Insets(20));
        content.setSpacing(16);
        VBox.setVgrow(content, Priority.ALWAYS);
        return content;
    }

    public VBox section(String title, TableView<?> table, HBox actions) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(title);
        label.getStyleClass().add("section-title");
        VBox box = new VBox(12, label, actions, table);
        box.getStyleClass().add("card");
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public HBox actions(javafx.scene.Node... nodes) {
        HBox box = new HBox(10, nodes);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("actions");
        return box;
    }

    public Button button(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("primary");
        button.setOnAction(event -> safe(action));
        return button;
    }

    public Button dangerButton(String text, Runnable action) {
        Button button = button(text, action);
        button.getStyleClass().add("danger");
        return button;
    }

    public <T> T selected(TableView<T> table) {
        return table == null ? null : table.getSelectionModel().getSelectedItem();
    }

    public void safe(Runnable action) {
        try {
            action.run();
        } catch (Exception exception) {
            message(exception.getMessage());
        }
    }

    public void message(String text) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION,
            text == null ? "Action impossible" : text
        );
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public String stylesheet() {
        return new java.io.File("smartfarm.css").toURI().toString();
    }
}
