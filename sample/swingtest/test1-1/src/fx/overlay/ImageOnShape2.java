package fx.overlay;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;

// https://stackoverflow.com/questions/22848829/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
// http://krr.blog.shinobi.jp/javafx/javafx%20%E3%83%96%E3%83%AC%E3%83%B3%E3%83%89%E3%83%BB%E3%83%A2%E3%83%BC%E3%83%89
public class ImageOnShape2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Pane root = new Pane();

            Group imageContainer = new Group();
            Image image = new Image( "file:resources/images/duke.png" );
            ImageView imageview = new ImageView( image );
            System.out.println( "width:" + image.getWidth() );
            System.out.println( "height:" + image.getHeight() );
            Line shape = new Line( 0, 0, image.getWidth(), image.getHeight() );
            shape.setStyle("-fx-stroke: red; -fx-stroke-width: 3; ");
            imageContainer.getChildren().addAll( imageview, shape );
            imageContainer.setEffect( new Blend(BlendMode.OVERLAY) );
            enableDragging(imageContainer);

            root.getChildren().add(imageContainer);

            Scene scene = new Scene(root,800,600);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void enableDragging(Node node) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        node.setOnMousePressed( event -> mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY())));
        node.setOnMouseDragged( event -> {
            double deltaX = event.getSceneX() - mouseAnchor.get().getX();
            double deltaY = event.getSceneY() - mouseAnchor.get().getY();
            node.relocate(node.getLayoutX()+deltaX, node.getLayoutY()+deltaY);
            mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY()));;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
