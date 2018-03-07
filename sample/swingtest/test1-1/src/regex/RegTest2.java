package regex;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegTest2 extends Application
{
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)
    {
    	VBox vbox = new VBox();
    	TextArea  src = new TextArea( "frozen<tuple<int,bigint>>");
    	TextArea  regEx = new TextArea( "frozen<(.+)>" );
    	TextArea  rep = new TextArea( "$1" );
    	TextArea  res = new TextArea();
    	TextArea  matLst = new TextArea();
    	Button    btn = new Button("replace");
    	Button    btnMat = new Button("match");
    	btn.setOnAction
    	(
    		(event)->
    		{
    			String srcStr = src.getText();
    			String regExStr = regEx.getText();
    			String repStr = rep.getText();
    			
    			String resStr = srcStr.replaceAll( regExStr, repStr );
    			res.setText( resStr );
    		}
    	);
    	// https://stackoverflow.com/questions/6020384/create-array-of-regex-matches
    	btnMat.setOnAction
    	(
    		(event)->
    		{
    			StringBuffer sb = new StringBuffer();
    			Matcher m = Pattern.compile( regEx.getText() )
    					.matcher( src.getText() );
    			while ( m.find() )
    			{
    				sb.append( m.group() );
    				sb.append("\n");
    			}
    			
    			matLst.setText( sb.toString() );
    		}
    	);
    	
    	GridPane gridPane = new GridPane();
    	gridPane.setHgap(5);
    	gridPane.setVgap(2);
    	gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
    	gridPane.add( new Label("Original"), 0, 0 );
    	gridPane.add( src, 1, 0 );
    	gridPane.add( new Label("RegEx"), 0, 1 );
    	gridPane.add( regEx, 1, 1 );
    	gridPane.add( new Label("Replacement"), 0, 2 );
    	gridPane.add( rep, 1, 2 );
    	gridPane.add( new Label("After Replace"), 0, 3 );
    	gridPane.add( res, 1, 3 );
    	gridPane.add( new Label("Match List"), 0, 4 );
    	gridPane.add( matLst, 1, 4 );
    	
    	HBox hBox = new HBox(2);
    	hBox.getChildren().addAll( btn, btnMat );
    	
    	vbox.getChildren().addAll( gridPane, hBox );
    	
    	
    	BorderPane brdPane = new BorderPane();
    	brdPane.setCenter( vbox );
    	
    	Scene scene = new Scene( brdPane, 640, 480 );
    	primaryStage.setScene( scene );
    	primaryStage.show();
    	
    }
}
