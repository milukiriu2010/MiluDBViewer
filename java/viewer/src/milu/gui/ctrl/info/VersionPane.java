package milu.gui.ctrl.info;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.text.MessageFormat;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.event.Event;
import javafx.concurrent.Task;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.WatchInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.main.MainController;
import milu.task.version.ModuleTaskFactory;
import milu.tool.MyGUITool;
import milu.tool.MyServiceTool;

public class VersionPane extends Pane 
	implements
		CloseInterface,
		MapInterface,
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
	
	private Map<String,Object> dataMap = new HashMap<>();
	
	public VersionPane( MainController mainCtrl )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		
		this.setTopPane();
		
		this.barProgress.setPrefWidth(500);
		
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
		this.service.submit( task );
		
		this.barProgress.progressProperty().unbind();
		this.barProgress.progressProperty().bind(task.progressProperty());
		
		task.progressProperty().addListener
		((obs,oldVal,newVal)->{
			System.out.println( "ModuleCheckTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			// task start.
			if ( newVal.doubleValue() == 0.0 )
			{
				this.beginUpdateCheck();
			}
			// task done.
			else if ( newVal.doubleValue() == 1.0 )
			{
				this.endUpdateCheck();
			}
		});
		
		task.messageProperty().addListener
		((obs,oldVal,msg)->{
			this.lblProgress.setText(msg);
		});
		
		task.valueProperty().addListener((obs,oldVal,ex)->{
			if ( ex == null )
			{
				return;
			}
			
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.ctrl.info.VersionTab", "LABEL_PROXY_ERROR", ex );
		});
	}
	
	private void moduleDownload( Event event )
	{
		Task<Exception> task = 
			ModuleTaskFactory.createFactory( 
				ModuleTaskFactory.FACTORY_TYPE.DOWNLOAD, 
				this.mainCtrl, 
				(String)this.dataMap.get("newLink"),
				this
			);
			
		// execute task
		this.service.submit( task );
		
		this.barProgress.progressProperty().unbind();
		this.barProgress.progressProperty().bind(task.progressProperty());
			
		task.progressProperty().addListener
		((obs,oldVal,newVal)->{
			System.out.println( "ModuleDownloadTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			// task start.
			if ( newVal.doubleValue() == 0.0 )
			{
				this.beginDownload();
			}
			// task done.
			else if ( newVal.doubleValue() == 1.0 )
			{
				this.endDownload();
			}
		});
		
		task.messageProperty().addListener
		((obs,oldVal,msg)->{
			this.lblProgress.setText(msg);
		});
			
		task.valueProperty().addListener((obs,oldVal,ex)->{
			if ( ex == null )
			{
				return;
			}
			
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.ctrl.info.VersionTab", "LABEL_PROXY_ERROR", ex );
		});
	}
	
	private void moduleUpdate( Event event )
	{
		Task<Exception> task = 
			ModuleTaskFactory.createFactory( 
				ModuleTaskFactory.FACTORY_TYPE.UPDATE, 
				this.mainCtrl, 
				null,
				this
			);
			
		// execute task
		this.service.submit( task );
		
		this.barProgress.progressProperty().unbind();
		this.barProgress.progressProperty().bind(task.progressProperty());
			
		task.progressProperty().addListener
		((obs,oldVal,newVal)->{
			//System.out.println( "ModuleUpdateTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			// task start.
			if ( newVal.doubleValue() == 0.0 )
			{
				this.beginUpdate();
			}
			// task done.
			else if ( newVal.doubleValue() == 1.0 )
			{
				this.endUpdate();
			}
		});
		
		task.messageProperty().addListener
		((obs,oldVal,msg)->{
			this.lblProgress.setText(msg);
		});
		
		task.valueProperty().addListener((obs,oldVal,ex)->{
			if ( ex == null )
			{
				return;
			}
			
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.ctrl.info.VersionTab", "LABEL_PROXY_ERROR", ex );
		});
	}
	
	// CloseInterface
	@Override
	public void closeRequest( Event event )
	{
		MyServiceTool.shutdownService(this.service);
		/*
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
		*/
	}
	
	// CloseInterface
	@Override
	public void addWatchLst( WatchInterface watchInf )
	{
	}
	
	// MapInterface
	@Override
	public Map<String, Object> getValue()
	{
		return this.dataMap;
	}
	
	// MapInterface
	@Override
	public void setValue( Map<String, Object> dataMap )
	{
		this.dataMap = dataMap;
	}
	
	private void beginUpdateCheck()
	{
		this.btnCheck.setDisable(true);
		HBox hBox = new HBox(2);
		hBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBox.setSpacing(10);
		hBox.getChildren().addAll( this.barProgress, this.lblProgress );
		this.basePane.setLeft(null);
		this.basePane.setBottom(hBox);
	}
	
	private void endUpdateCheck()
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
		this.btnCheck.setDisable(false);
		this.basePane.setBottom(null);
		if ( (Boolean)this.dataMap.get("isExistNew") )
		{
			this.lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_FOUND_NEW_VERSION"), this.dataMap.get("newVersion") ) );
			HBox hBoxLeft = new HBox(2);
			hBoxLeft.getChildren().add( this.btnUpdate );
			hBoxLeft.setPadding( new Insets( 10, 10, 10, 10 ) );
			hBoxLeft.setSpacing(10);
			this.basePane.setLeft(hBoxLeft);
			this.btnUpdate.setOnAction( this::moduleDownload );
		}
		else
		{
			this.lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_THIS_VERSION"), this.dataMap.get("newVersion") ) );
		}
	}
	
	private void beginDownload()
	{
		this.btnCheck.setDisable(true);
		HBox hBox = new HBox(2);
		hBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBox.setSpacing(10);
		hBox.getChildren().addAll( this.barProgress, this.lblProgress );
		this.btnUpdate.setDisable(true);
		this.basePane.setBottom(hBox);
	}
	
	private void endDownload()
	{
		this.btnCheck.setDisable(false);
		this.btnUpdate.setDisable(false);
		this.basePane.setBottom(null);
		this.moduleUpdate(null);
	}
	
	private void beginUpdate()
	{
		this.btnCheck.setDisable(true);
		HBox hBox = new HBox(2);
		hBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBox.setSpacing(10);
		hBox.getChildren().addAll( this.barProgress, this.lblProgress );
		this.btnUpdate.setDisable(true);
		this.basePane.setBottom(hBox);
	}
	
	private void endUpdate()
	{
		this.btnCheck.setDisable(false);
		this.btnUpdate.setDisable(false);
		this.basePane.setBottom(null);
	}
	
	@Override
	public void changeLang() 
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.info.VersionTab");
		
		this.btnCheck.setText( langRB.getString("BTN_CHECK_UPDATE") );
		this.btnUpdate.setText( langRB.getString("BTN_UPDATE") );
		
	}

}
