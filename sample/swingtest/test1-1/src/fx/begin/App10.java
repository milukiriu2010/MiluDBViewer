package fx.begin;
// https://www.tuyano.com/index3?id=12496003&page=3
// https://www.tuyano.com/index3?id=12496003&page=4

import java.io.IOException;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
 
public class App10 extends Application {
     
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
            BorderPane root;
            try {
                root = (BorderPane)FXMLLoader.load(getClass().getResource("/resources/begin/app10.fxml"));
                Scene scene = new Scene(root,200,100);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
