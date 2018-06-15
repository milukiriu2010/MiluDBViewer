package milu.gui.ctrl.info;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.text.MessageFormat;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import javafx.concurrent.Task;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ProcInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.main.AppConst;
import milu.main.MainController;
import milu.task.version.ModuleTaskFactory;
import milu.tool.MyTool;

public class VersionPane extends Pane 
	implements
		CloseInterface,
		MapInterface,
		ProcInterface,
		ChangeLangInterface 
{
	private MainController    mainCtrl = null;
	
	private BorderPane basePane = new BorderPane();
	
	// ----------------------------------------
	// [Top on Pane]
	// ----------------------------------------	
	private Button    btnCheck  = new Button();
	
	private Label     lblCheck  = new Label();
	
	private Button    btnUpdate = new Button();
	
	// ----------------------------------------
	// [Bottom on Pane]
	// ----------------------------------------	
	private Label       lblProgress = new Label();
	
	private ProgressBar barProgress = new ProgressBar();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	private boolean isExistNew = false;
	
	// x.x.x
	private String  newVersion = null;
	
	private String  newLink = null;
	
	private Integer fileSize = null;
	
	public VersionPane( MainController mainCtrl )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		
		this.setTopPane();
		
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
		Task<Exception> task = 
			ModuleTaskFactory.createFactory( 
				ModuleTaskFactory.FACTORY_TYPE.CHECK, 
				this.mainCtrl, 
				"https://sourceforge.net/projects/miludbviewer/rss?path=/",
				this
			);
		
		// execute task
		final Future<?> future = this.service.submit( task );
		
		this.barProgress.progressProperty().unbind();
		this.barProgress.progressProperty().bind(task.progressProperty());
		
		task.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ModuleCheckTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				// task start.
				if ( newVal.doubleValue() == 0.0 )
				{
					this.beginProc();
				}
				// task done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					this.endProc();
				}
			}
		);
		
		task.valueProperty().addListener((obs,oldVal,ex)->{
			if ( ex == null )
			{
				return;
			}
			
			MyTool.showException( this.mainCtrl, "conf.lang.gui.ctrl.info.VersionTab", "LABEL_PROXY_ERROR", ex );
		});
	}
	
	private void moduleUpdate( Event event )
	{
		
	}
	
	private void compareVersion()
	{
		this.isExistNew = false;
		if ( this.newVersion == null )
		{
			return;
		}
		
		String[] nowVer = AppConst.VER.val().split("\\.");
		String[] newVer = this.newVersion.split("\\.");
		
		if ( nowVer.length != 3 || newVer.length != 3 )
		{
			return;
		}

		for ( int i = 0; i <= 2; i++ )
		{
			// "nowVer" is older than "newVer"
			if ( 
					( nowVer[i].compareTo(newVer[i]) < 0 ) 
					&&
					( nowVer[i].length() <= newVer[i].length() )
			)
			{
				System.out.println( "compareVersion break i:" + i );
				this.isExistNew = true;
				return;
			}
			// "nowVer" is newer than "newVer"
			else if ( nowVer[i].compareTo(newVer[i]) > 0 )
			{
				return;
			}
		}
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
	
	// MapInterface
	@Override
	public void setValue( Map<String, Object> dataMap )
	{
		this.newVersion = (String)dataMap.get("newVersion");
		this.newLink    = (String)dataMap.get("newLink");
		this.fileSize   = (Integer)dataMap.get("fileSize");
		System.out.println( "MapInterface:done." );
	}
	
	// ProcInterface
	@Override
	public void beginProc()
	{
		this.btnCheck.setDisable(true);
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.barProgress, this.lblProgress );
		this.basePane.setLeft(null);
		this.basePane.setBottom(vBox);
	}
	
	// ProcInterface
	@Override
	public void endProc()
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
		this.btnCheck.setDisable(false);
		this.basePane.setBottom(null);
		this.compareVersion();
		if ( this.isExistNew )
		{
			this.lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_FOUND_NEW_VERSION"), this.newVersion ) );
			HBox hBoxLeft = new HBox(2);
			hBoxLeft.getChildren().add( this.btnUpdate );
			hBoxLeft.setPadding( new Insets( 10, 10, 10, 10 ) );
			hBoxLeft.setSpacing(10);
			this.basePane.setLeft(hBoxLeft);
			this.btnUpdate.setOnAction( this::moduleUpdate );
		}
		else
		{
			this.lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_THIS_VERSION"), this.newVersion ) );
		}
	}
	
	@Override
	public void changeLang() 
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
		
		this.btnCheck.setText( langRB.getString("BTN_CHECK_UPDATE") );
		this.btnUpdate.setText( langRB.getString("BTN_UPDATE") );
		
	}

}
