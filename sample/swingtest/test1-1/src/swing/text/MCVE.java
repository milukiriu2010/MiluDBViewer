package swing.text;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class MCVE extends Application {
    public void start(Stage stage) {
        HBox root = new HBox();

        ComboBox<String> cb = new ComboBox<String>();
        cb.setEditable(true);

        // Create a list with some dummy values.
        ObservableList<String> items = FXCollections.observableArrayList("One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten");

        // Create a FilteredList wrapping the ObservableList.
        FilteredList<String> filteredItems = new FilteredList<String>(items, p -> true);

        // Add a listener to the textProperty of the combobox editor. The
        // listener will simply filter the list every time the input is changed
        // as long as the user hasn't selected an item in the list.
        cb.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = cb.getEditor();
            final String selected = cb.getSelectionModel().getSelectedItem();

            // This needs run on the GUI thread to avoid the error described
            // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
            Platform.runLater(() -> {
                // If the no item in the list is selected or the selected item
                // isn't equal to the current input, we refilter the list.
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        // We return true for any items that starts with the
                        // same letters as the input. We use toUpperCase to
                        // avoid case sensitivity.
                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        cb.setItems(filteredItems);

        root.getChildren().add(cb);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
