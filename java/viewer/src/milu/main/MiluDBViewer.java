package milu.main;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

import javafx.application.Application;
import javafx.geometry.Pos;
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
import milu.task.main.InitialLoadTask;

public class MiluDBViewer extends Application
{
	private static final int SPLASH_WIDTH  = 320;
	private static final int SPLASH_HEIGHT = 200;
	
	private VBox         splashLayout = new VBox();
	private ProgressBar  loadProgress = new ProgressBar();
	private Label        progressText = new Label("Loading...");
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
    public static void main(String[] args)
    {
    	
    	
    	launch(args);
    	// https://stackoverflow.com/questions/46053974/using-platform-exit-and-system-exitint-together?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    	System.exit(0);
    }
    
    @Override
    public void start(Stage initStage) throws Exception
    {
    	Map<String,String> propMap = new HashMap<>();
    	Parameters params = this.getParameters();
    	if ( params != null )
    	{
    		// -----------------------------------
    		// command args
    		// -----------------------------------
    		//   --instDir=/home/kiriu
    		// -----------------------------------
    		params.getNamed().forEach( propMap::put );
    	}
    	
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
    	    //final Rectangle2D bounds = Screen.getPrimary().getBounds();
    	    //final Rectangle2D bounds = MyTool.getActiveScreen().getBounds();
    	    initStage.setScene(splashScene);
    	    //initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
    	    //initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
    	    MyTool.setWindowLocation(initStage, SPLASH_WIDTH, SPLASH_HEIGHT );
    	    initStage.show();
    	    
    	    final InitialLoadTask  ilTask = new InitialLoadTask();
    	    ilTask.setMainController(mainCtrl);
    	    ilTask.setPropMap(propMap);
			// execute task
			this.service.submit( ilTask );
    	    
			ilTask.progressProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					this.loadProgress.setProgress(newVal.doubleValue());
					if ( newVal.doubleValue() == 1.0 )
					{
			    		this.serviceShutdown();
						initStage.hide();
			    		mainCtrl.init(this);
					}
				}
			);
			
			ilTask.messageProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					this.progressText.setText(newVal);
				}
			);
			
			ilTask.valueProperty().addListener((obs,oldVal,ex)->{
				/*
				MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
	    		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
				alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
	    		alertDlg.setTxtExp( ex );
	    		alertDlg.showAndWait();
	    		alertDlg = null;
	    		*/
	    		MyTool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
			});
    	}
    	catch ( Exception ex )
    	{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
    		alertDlg.setHeaderText( "Something Wrong!!" );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
    }
    
    @Override
    public void stop()
    {
    	this.serviceShutdown();
    }
    
    private void serviceShutdown()
    {
		try
		{
			System.out.println( "shutdown executor start(MiluDBViewer)." );
			this.service.shutdown();
			this.service.awaitTermination( 3, TimeUnit.SECONDS );
		}
		catch ( InterruptedException intEx )
		{
			System.out.println( "tasks interrupted(MiluDBViewer)" );
		}
		finally
		{
			if ( !this.service.isTerminated() )
			{
				System.out.println( "executor still working...(MiluDBViewer)" );
			}
			this.service.shutdownNow();
			System.out.println( "executor finished(MiluDBViewer)." );
		}
    }
}
