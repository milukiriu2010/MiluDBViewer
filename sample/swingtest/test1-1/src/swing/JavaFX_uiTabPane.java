package swing;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
*
* @web http://java-buddy.blogspot.com/
*/
// http://java-buddy.blogspot.jp/2012/02/javafx-20-tabpane.html
public class JavaFX_uiTabPane extends Application {

 /**
  * @param args the command line arguments
  */
  public static void main(String[] args) {
	  launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
	  primaryStage.setTitle("http://java-buddy.blogspot.com/");
	  Group root = new Group();
	  Scene scene = new Scene(root, 400, 300, Color.WHITE);
	  
	  TabPane tabPane = new TabPane();
	  BorderPane mainPane = new BorderPane();
	  
	  //Create Tabs
	  Tab tabA = new Tab();
	  tabA.setText("Tab A");
	  tabPane.getTabs().add(tabA);
	  
	  Tab tabB = new Tab();
	  tabB.setText("Tab B");
	  tabPane.getTabs().add(tabB);
	  
	  Tab tabC = new Tab();
	  tabC.setText("Tab C");
	  tabPane.getTabs().add(tabC);
	  
	  mainPane.setCenter(tabPane);
	  
	  mainPane.prefHeightProperty().bind(scene.heightProperty());
	  mainPane.prefWidthProperty().bind(scene.widthProperty());
	  
	  root.getChildren().add(mainPane);
	  primaryStage.setScene(scene);
	  primaryStage.show();
  }
}
