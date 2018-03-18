package fx.line;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;

// https://stackoverflow.com/questions/41353685/how-to-draw-arrow-javafx-pane
public class ArrowSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
	    Pane root = new Pane();
	    Arrow arrow = new Arrow();
	    root.getChildren().add(arrow);

	    root.setOnMouseClicked(evt -> {
	        switch (evt.getButton()) {
	            case PRIMARY:
	                // set pos of end with arrow head
	                arrow.setEndX(evt.getX());
	                arrow.setEndY(evt.getY());
	                break;
	            case SECONDARY:
	                // set pos of end without arrow head
	                arrow.setStartX(evt.getX());
	                arrow.setStartY(evt.getY());
	                break;
	            default:
	            	break;
	        }
	    });

	    Scene scene = new Scene(root, 400, 400);

	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
