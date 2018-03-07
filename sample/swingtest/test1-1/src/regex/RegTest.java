package regex;


import javafx.application.Application;
//import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegTest extends Application
{
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)
    {
    	VBox vbox = new VBox();
    	TextField  src = new TextField( "frozen<tuple<int,bigint>>");
    	TextField  mat = new TextField( "frozen<(.+)>" );
    	TextField  rep = new TextField( "$1" );
    	TextField  res = new TextField();
    	Button     btn = new Button("replace");
    	btn.setOnAction
    	(
    		(event)->
    		{
    			String srcStr = src.getText();
    			String matStr = mat.getText();
    			String repStr = rep.getText();
    			
    			String resStr = srcStr.replaceAll( matStr, repStr );
    			res.setText( resStr );
    		}
    	);
    	vbox.getChildren().addAll( src, mat, rep, res, btn );
    	
    	BorderPane brdPane = new BorderPane();
    	brdPane.setCenter( vbox );
    	
    	Scene scene = new Scene( brdPane, 640, 480 );
    	primaryStage.setScene( scene );
    	primaryStage.show();
    	
    }
}
