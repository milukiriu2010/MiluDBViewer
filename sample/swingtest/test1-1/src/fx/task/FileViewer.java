package fx.task;

import java.io.IOException;
import javafx.application.Application;
//import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FileViewer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/swing/longtask/View.fxml"));
        loader.load();
        StackPane root = loader.getRoot();
        ViewController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("File Viewer");
        stage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
