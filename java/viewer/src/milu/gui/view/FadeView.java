package milu.gui.view;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import milu.tool.MyGUITool;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class FadeView extends Stage
{
	static int WIDTH  = 160;
	static int HEIGHT = 100;
	
	public FadeView( String msg )
	{
		// Remove window decoration
		this.initStyle(StageStyle.TRANSPARENT);
		
		Label lblMsg = new Label(msg);
		lblMsg.setAlignment(Pos.CENTER);
		lblMsg.setStyle( "-fx-font-size: 28; -fx-font-weight: bold;" );
		
		StackPane stackPane = new StackPane();
		stackPane.setStyle( "-fx-background-color: #ffffcc; -fx-border-color: #000000; -fx-border-width: 2px;" );
		stackPane.getChildren().add(lblMsg);
		
		Scene scene = new Scene( stackPane, WIDTH, HEIGHT );
		//Makes scene background transparent
		scene.setFill(Color.TRANSPARENT);
		this.setScene(scene);
		MyGUITool.setWindowLocation( this, WIDTH, HEIGHT );
		this.show();
		
        Timeline timeline = new Timeline();
        KeyFrame key = 
    		new KeyFrame(
    			Duration.millis(2000),
                new KeyValue(this.getScene().getRoot().opacityProperty(), 0)
            ); 
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished( (event)->this.hide() );
		timeline.play();
	}
	
	
}
