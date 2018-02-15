package swing.begin;
// https://www.tuyano.com/index3?id=5316003&page=4

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
 
public class App8 extends Application {
    Label label;
    ComboBox<String> combo;
     
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) throws Exception {
        label = new Label("This is JavaFX!");
        ObservableList<String> data = FXCollections.observableArrayList(
                "One","Two","Three"
        );
        combo = new ComboBox<String>(data);
        combo.setOnAction((ActionEvent)->{
            label.setText(combo.getValue());
        });
        BorderPane pane = new BorderPane();
        pane.setTop(label);
        pane.setCenter(combo);
        Scene scene = new Scene(pane, 320, 120);
        stage.setScene(scene);
        stage.show();
    }
 
}
