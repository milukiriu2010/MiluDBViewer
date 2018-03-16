package fx.overlay;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import netscape.javascript.JSObject;

// https://stackoverflow.com/questions/10894903/how-to-make-an-overlay-on-top-of-javafx-2-webview
public class OverlayDemo extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
        StackPane root = new StackPane();
        WebView webView = new WebView();

        final WebEngine webEngine = webView.getEngine();
        Canvas overlay = new Canvas(600,600);
        overlay.setOpacity(0.5);
        final GraphicsContext gc = overlay.getGraphicsContext2D();
        gc.setFill(Color.RED);

        root.getChildren().addAll(webView, overlay);
        stage.setScene(new Scene(root, 600, 600));

        webEngine.getLoadWorker().workDoneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 100) {
                // find coordinates by javascript call
                JSObject bounds = (JSObject)webEngine.executeScript("document.getElementsByClassName('question-hyperlink')[0].getBoundingClientRect()");

                Number right = (Number)bounds.getMember("right");
                Number top = (Number)bounds.getMember("top");
                Number bottom = (Number)bounds.getMember("bottom");
                Number left = (Number)bounds.getMember("left");

                // paint on overlaing canvas
                gc.rect(left.doubleValue(), top.doubleValue(), right.doubleValue(), bottom.doubleValue());
                gc.fill();
            }
        });
        webEngine.load("http://stackoverflow.com/questions/10894903/how-to-make-an-overlay-on-top-of-javafx-2-webview");

        stage.show();
	}
	
	public static void main(String[] args)
	{
		if ( args.length >= 2 )
		{
			System.setProperty( "http.proxyHost", args[0] );
			System.setProperty( "http.proxyPort", args[1] );
		}
		launch(); 
	}

}
