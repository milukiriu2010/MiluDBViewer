package fx.begin;
// https://www.tuyano.com/index3?id=5316003&page=3

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
 
public class App7 extends Application {
    Label label;
    ToggleGroup group;
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) throws Exception {
        label = new Label("This is JavaFX!");
        group = new ToggleGroup();
        RadioButton btn1 = new RadioButton("Male");
        btn1.setToggleGroup(group);
        btn1.setSelected(true);
        RadioButton btn2 = new RadioButton("Female");
        btn2.setToggleGroup(group);
        btn1.setOnAction((ActionEvent)->{
            label.setText("you are Male?");
        });
        btn2.setOnAction((ActionEvent)->{
            label.setText("you are Female?");
        });
         
        BorderPane pane = new BorderPane();
        pane.setTop(label);
        FlowPane flow = new FlowPane();
        flow.getChildren().add(btn1);
        flow.getChildren().add(btn2);
        pane.setCenter(flow);
        Scene scene = new Scene(pane, 320, 120);
        stage.setScene(scene);
        stage.show();
    }
 
}