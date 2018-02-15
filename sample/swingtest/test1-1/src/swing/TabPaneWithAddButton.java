package swing;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// https://community.oracle.com/thread/2535484
public class TabPaneWithAddButton extends Application {

  @Override
  public void start(Stage primaryStage) {
    final AnchorPane root = new AnchorPane();
    final TabPane tabs = new TabPane();
    final Button addButton = new Button("+");

    AnchorPane.setTopAnchor(tabs, 5.0);
    AnchorPane.setLeftAnchor(tabs, 5.0);
    AnchorPane.setRightAnchor(tabs, 5.0);
    AnchorPane.setTopAnchor(addButton, 10.0);
    AnchorPane.setLeftAnchor(addButton, 10.0);

    addButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        final Tab tab = new Tab("Tab " + (tabs.getTabs().size() + 1));
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
      }
    });

    root.getChildren().addAll(tabs, addButton);

    final Scene scene = new Scene(root, 600, 400);
    //scene.getStylesheets().add("tabs.css");
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }
}
