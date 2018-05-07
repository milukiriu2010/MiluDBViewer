package milu.main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import milu.gui.dlg.MyAlertDialog;
import milu.tool.MyTool;

public class MiluDBViewer extends Application
{
	private static final int SPLASH_WIDTH  = 320;
	private static final int SPLASH_HEIGHT = 200;
	
	private VBox         splashLayout = new VBox();
	private ProgressBar  loadProgress = new ProgressBar();
	private Label        progressText = new Label("Loading...");
	
    public static void main(String[] args)
    {
    	launch(args);
    	// https://stackoverflow.com/questions/46053974/using-platform-exit-and-system-exitint-together?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    	System.exit(0);
    }
    
    @Override
    public void start(Stage initStage) throws Exception
    {
		MainController mainCtrl = new MainController();
    	try
    	{
    		ImageView splash = MyTool.createImageView( SPLASH_WIDTH, SPLASH_HEIGHT, new Image("file:resources/images/banner.gif") );
    		this.loadProgress.setPrefWidth( SPLASH_WIDTH-20 );
    		this.progressText.setAlignment(Pos.CENTER);
    		this.splashLayout.getChildren().addAll( splash, this.loadProgress, this.progressText );
    		this.splashLayout.setStyle("-fx-padding: 5; -fx-background-color: cornsilk; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, chocolate, derive(chocolate, 50%));");
    		this.splashLayout.setEffect(new DropShadow());
    		Scene splashScene = new Scene(this.splashLayout);
    	    initStage.initStyle(StageStyle.UNDECORATED);
    	    final Rectangle2D bounds = Screen.getPrimary().getBounds();
    	    initStage.setScene(splashScene);
    	    initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
    	    initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
    	    initStage.show();
    		mainCtrl.init(this);
    		initStage.hide();
    	}
    	catch ( Exception ex )
    	{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
    		alertDlg.setHeaderText( "Something Wrong!!" );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
    }
}
