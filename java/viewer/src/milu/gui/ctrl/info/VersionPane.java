package milu.gui.ctrl.info;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import javafx.concurrent.Task;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ProcInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.main.MainController;
import milu.task.version.ModuleTaskFactory;

public class VersionPane extends Pane 
	implements
		CloseInterface,
		ProcInterface,
		ChangeLangInterface 
{
	private MainController    mainCtrl = null;
	
	private BorderPane basePane = new BorderPane();
	
	// ----------------------------------------
	// [Top on Pane]
	// ----------------------------------------	
	private Button    btnCheck = new Button();
	
	private Label     lblCheck = new Label();
	
	// ----------------------------------------
	// [Bottom on Pane]
	// ----------------------------------------	
	private TextField txtCheck = new TextField();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	// data
	private Map<String,Object>  dataMap = new HashMap<>();
	
	public VersionPane( MainController mainCtrl )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		
		this.txtCheck.setEditable(false);
		this.txtCheck.visibleProperty().setValue(false);
		
		this.setTopPane();
		this.basePane.setBottom(this.txtCheck);
		
		this.getChildren().add( this.basePane );
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setTopPane()
	{
		HBox hBoxCheck = new HBox(2);
		hBoxCheck.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxCheck.setSpacing(10);
		hBoxCheck.getChildren().addAll( this.btnCheck, this.lblCheck );		
		
		this.basePane.setTop(hBoxCheck);
	}
	
	private void setAction()
	{
		this.btnCheck.setOnAction( this::moduleCheck );
		
	}
	
	private void moduleCheck( Event event )
	{
		//this.txtCheck.setText("aaa");
		//this.txtCheck.visibleProperty().setValue(true);
		long startTime = System.nanoTime();
		Task<Exception> task = 
			ModuleTaskFactory.createFactory( 
				ModuleTaskFactory.FACTORY_TYPE.CHECK, 
				this.mainCtrl, 
				"https://sourceforge.net/projects/miludbviewer/rss?path=/" 
			);
		
		// execute task
		final Future<?> future = this.service.submit( task );
		
		task.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ModuleCheckTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
				// task start.
				if ( newVal.doubleValue() == 0.0 )
				{
					this.beginProc();
					VBox vBox = new VBox(2);
					//Label  labelProcess = new Label( langRB.getString("LABEL_PROCESSING") );
					//Button btnCancel    = new Button( langRB.getString("BTN_CANCEL") );
					//vBox.getChildren().addAll( labelProcess, btnCancel );
					
					// Oracle =>
					//   java.sql.SQLRecoverableException
					//btnCancel.setOnAction( (event2)->future.cancel(true) );
					
					System.out.println( "ModuleCheckTask:clear" );
				}
				// task done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					//this.lowerPane.getChildren().clear();
					//this.lowerPane.getChildren().add( this.tabPane );
					this.endProc();
					System.out.println( "ModuleCheckTask:clear" );
				}
			}
		);
		
	}
	
	// CloseInterface
	@Override
	public void closeRequest( Event event )
	{
		try
		{
			System.out.println( "shutdown executor start." );
			service.shutdown();
			service.awaitTermination( 3, TimeUnit.SECONDS );
		}
		catch ( InterruptedException intEx )
		{
			System.out.println( "tasks interrupted" );
		}
		finally
		{
			if ( !service.isTerminated() )
			{
				System.out.println( "executor still working..." );
			}
			service.shutdownNow();
			System.out.println( "executor finished." );
		}
	}
	
	// ProcInterface
	@Override
	public void beginProc()
	{
		this.btnCheck.setDisable(true);
	}
	
	// ProcInterface
	@Override
	public void endProc()
	{
		this.btnCheck.setDisable(false);
	}
	
	@Override
	public void changeLang() 
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
		
		this.btnCheck.setText( langRB.getString("BTN_CHECK_UPDATE") );
		
	}

}
