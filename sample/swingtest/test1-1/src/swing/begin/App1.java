package swing.begin;
// https://www.tuyano.com/index3?id=12466003&page=3

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
 
public class App1 extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("This is JavaFX!");
        BorderPane pane = new BorderPane();
        pane.setCenter(label);
        Scene scene = new Scene(pane, 320, 240);
        stage.setScene(scene);
        stage.show();
    }
 
}