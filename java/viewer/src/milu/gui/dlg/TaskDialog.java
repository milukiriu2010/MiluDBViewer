package milu.gui.dlg;

import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import milu.main.MainController;
import milu.tool.MyGUITool;
import milu.tool.MyServiceTool;

public class TaskDialog extends Dialog<Boolean> 
{
	private Task<Exception> task = null;
	
	private MainController mainCtrl = null;
	
	private ProgressBar progressBar = new ProgressBar();
	private Label       lblMsg = new Label();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	public TaskDialog( Task<Exception> task, MainController mainCtrl )
	{
		super();
		
		this.task = task;
		this.mainCtrl = mainCtrl;
		
		VBox vBox = new VBox(2);
		vBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		vBox.setSpacing(10);
		vBox.getChildren().addAll( this.progressBar, this.lblMsg );
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );

		// set location
		Platform.runLater( ()->MyGUITool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );

		this.getDialogPane().setContent(vBox);
		
		this.setAction();
		
		this.start();
	}
	
	private void setAction()
	{
		this.setResultConverter((dialogButton)->Boolean.TRUE);
		
		this.setOnCloseRequest((event)->{
			MyServiceTool.shutdownService(this.service);
		});
	}
	
	private void start()
	{
		// execute task
		this.service.submit( this.task );
		
		this.progressBar.progressProperty().unbind();
		this.progressBar.progressProperty().bind(this.task.progressProperty());
		
		this.lblMsg.textProperty().unbind();
		this.lblMsg.textProperty().bind(this.task.messageProperty());
		
		this.task.progressProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal.doubleValue() == 1.0 )
			{
				this.close();
			}
		});
		
		this.task.valueProperty().addListener((obs,oldVal,ex)->{
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		});
	}
}
