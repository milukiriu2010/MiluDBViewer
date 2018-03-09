package fx.text;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

// https://community.oracle.com/thread/2534556
public class CaretCoordinatesTest extends Application {

	  @Override
	  public void start(Stage primaryStage) {
	    final TextArea textArea = new TextArea();
	    final BorderPane root = new BorderPane();
	    root.setCenter(textArea);

	    final Popup popup = new Popup();
	    popup.setAutoHide(true);
	    VBox suggestionBox = new VBox();
	    suggestionBox.setStyle("-fx-border-color: -fx-accent;");
	    popup.getContent().add(suggestionBox);
	    suggestionBox.getChildren().add(new Label("Here are some suggestions:"));
	    for (int i = 1; i <= 10; i++) {
	      suggestionBox.getChildren().add(new Label("Suggestion " + i));
	    }

	    textArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
	      @Override
	      public void handle(KeyEvent event) {
	        if (event.getCode() == KeyCode.SPACE && event.isControlDown()) {
	          Path caret = findCaret(textArea);
	          Point2D screenLoc = findScreenLocation(caret);
	          popup.show(textArea, screenLoc.getX(), screenLoc.getY() + 20);
	        }
	      }
	    });

	    textArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
	      @Override
	      public void handle(KeyEvent event) {
	        if (popup.isShowing()) {
	          popup.hide();
	        }
	      }
	    });

	    primaryStage.setScene(new Scene(root, 600, 400));
	    primaryStage.show();
	  }

	  private Path findCaret(Parent parent) {
	    // Warning: this is an ENORMOUS HACK
	    for (Node n : parent.getChildrenUnmodifiable()) {
	      if (n instanceof Path) {
	        return (Path) n;
	      } else if (n instanceof Parent) {
	        Path p = findCaret((Parent) n);
	        if (p != null) {
	          return p;
	        }
	      }
	    }
	    return null;
	  }

	  private Point2D findScreenLocation(Node node) {
	    double x = 0;
	    double y = 0;
	    for (Node n = node; n != null; n=n.getParent()) {
	      Bounds parentBounds = n.getBoundsInParent();
	      x += parentBounds.getMinX();
	      y += parentBounds.getMinY();
	    }
	    Scene scene = node.getScene();
	    x += scene.getX();
	    y += scene.getY();
	    Window window = scene.getWindow();
	    x += window.getX();
	    y += window.getY();
	    Point2D screenLoc = new Point2D(x, y);
	    return screenLoc;
	  }

	  public static void main(String[] args) {
	    launch(args);
	  }
}

