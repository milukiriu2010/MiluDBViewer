package milu.gui.dlg;

import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import milu.task.ProcInterface;
import milu.task.ProgressInterface;
import milu.main.MainController;
import milu.tool.MyGUITool;
import milu.tool.MyServiceTool;

public class TaskDialog extends Dialog<Boolean> 
{
	private Task<Exception> task     = null;
	private MainController  mainCtrl = null;
	private ProcInterface   procInf  = null;  
	
	private ProgressBar progressBar = new ProgressBar();
	private Label       lblMsg = new Label();
	private Button      btnCancel = new Button("Cancel");
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	public TaskDialog( Task<Exception> task, MainController mainCtrl, ProcInterface procInf )
	{
		super();
		
		this.task     = task;
		this.mainCtrl = mainCtrl;
		this.procInf  = procInf;
		
		this.progressBar.prefWidthProperty().bind(this.getDialogPane().widthProperty().multiply(0.7));
		
		HBox hBox = new HBox(2);
		hBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBox.setSpacing(10);
		hBox.getChildren().addAll( this.progressBar, this.btnCancel );
		
		VBox vBox = new VBox(2);
		vBox.setPadding( new Insets( 0, 0, 0, 10 ) );
		//vBox.setSpacing(10);
		vBox.getChildren().addAll( hBox, this.lblMsg );
		//vBox.getChildren().addAll( this.progressBar, this.lblMsg );
		//vBox.getChildren().addAll( this.progressBar, this.lblMsg, this.btnCancel );
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );

		// set location
		Platform.runLater( ()->MyGUITool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );

		this.getDialogPane().setContent(vBox);
		
		this.getDialogPane().setPrefSize(500, 80);
		
		this.setAction();
		
		this.start();
	}
	
	private void setAction()
	{
		this.setResultConverter((dialogButton)->Boolean.TRUE);
		
		/*
		this.setOnCloseRequest((event)->{
			MyServiceTool.shutdownService(this.service);
		});
		*/
		
		/*
		Window window = this.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest((event)->{
			System.out.println( "TaskDialog hide" );
			window.hide();
		});
		*/
	}
	
	private void start()
	{
		// execute task
		final Future<?> future = this.service.submit( this.task );
		
		this.progressBar.progressProperty().unbind();
		this.progressBar.progressProperty().bind(this.task.progressProperty());
		
		this.lblMsg.textProperty().unbind();
		this.lblMsg.textProperty().bind(this.task.messageProperty());
		
		this.btnCancel.setOnAction((event)->{
			if ( this.task instanceof ProgressInterface )
			{
				((ProgressInterface)this.task).cancelProc();
			}
			future.cancel(true);
			this.getDialogPane().getScene().getWindow().hide();
			this.close();
		});
		
		this.task.progressProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal.doubleValue() == 1.0 )
			{
				System.out.println( "TaskDialog close" );
				//Platform.runLater(()->this.close());
				if ( this.procInf != null )
				{
					this.procInf.endProc();
				}
				this.getDialogPane().getScene().getWindow().hide();
				this.close();
				MyServiceTool.shutdownService(this.service);
			}
		});
		
		this.task.valueProperty().addListener((obs,oldVal,ex)->{
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		});
	}
}
