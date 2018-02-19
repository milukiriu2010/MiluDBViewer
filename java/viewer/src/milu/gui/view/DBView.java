package milu.gui.view;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.util.List;
//import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;

import javafx.concurrent.Task;

import milu.ctrl.ExecQueryDBInterface;
import milu.ctrl.ExecExplainDBInterface;
import milu.ctrl.RefreshInterface;
import milu.ctrl.ToggleHorizontalVerticalInterface;
import milu.ctrl.CopyInterface;
import milu.ctrl.ChangeLangInterface;
import milu.ctrl.MainController;
import milu.gui.ctrl.common.DraggingTabPaneSupport;
import milu.gui.ctrl.menu.MainMenuBar;
import milu.gui.ctrl.menu.MainToolBar;
import milu.gui.ctrl.query.DBSqlTab;
import milu.gui.ctrl.schema.DBSchemaTab;
import milu.db.MyDBAbstract;
import milu.db.schema.SchemaDBAbstract;
import milu.db.schema.SchemaDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.task.CollectTask;

public class DBView extends Stage
	implements 
		ChangeLangInterface
{
	// Main Controller
	private MainController mainCtrl = null;
	
	// DB Connection Object
	private MyDBAbstract myDBAbs = null;
	
	// MenuBar
	private MainMenuBar mainMenuBar = null;
	
	// ToolBar(Main)
	private MainToolBar mainToolBar = null;
	
	// TabPane for multi view
	private TabPane tabPane = null;
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	private Future<?> future  = null;
	
	// ---------------------------
	// Constractor
	// ---------------------------
	public DBView( MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		super();
		
		// MainController
		this.mainCtrl = mainCtrl;
		
		// DB Connection Object
		this.myDBAbs = myDBAbs;
	}
	
	public MyDBAbstract getMyDBAbstract()
	{
		return this.myDBAbs;
	}

	// ---------------------------
	// create new window.
	// ---------------------------
	public void start()
	{
		// Create MenuBar
		this.createMenuBar();
		
		// Create ToolBar
		this.createToolBar();
		
        // set action
        this.setAction();
        
		// View for SQL
		DBSqlTab dbSqlTab = new DBSqlTab( this );
		
		// VBox for MenuBar/ToolBar
		VBox vboxMenu = new VBox( 2 );
		vboxMenu.getChildren().addAll( this.mainMenuBar, this.mainToolBar );
		
		// TabPane for multi view
		this.tabPane = new TabPane();
		this.tabPane.getTabs().add( dbSqlTab );
		// Enable tag dragging
		DraggingTabPaneSupport  dragSupport = new DraggingTabPaneSupport();
		dragSupport.addSupport( this.tabPane );
		
		// put items on Pane
        BorderPane pane = new BorderPane();
        pane.setTop( vboxMenu );
        pane.setCenter( this.tabPane );
        
        // Stage for New Window
        // http://o7planning.org/en/11533/opening-a-new-window-in-javafx
        Scene scene = new Scene(pane, 800, 600 );
        // load css on DBView elements
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/ctrl/menu/MainToolBar.css").toExternalForm()
		);
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/ctrl/query/SqlTableView.css").toExternalForm()
		);
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/ctrl/query/DBSqlTab.css").toExternalForm()
		);
        this.setScene(scene);
		// Window Title
        this.setTitle( "MiluDBViewer[URL=" + this.myDBAbs.getUrl() + "][USER=" + this.myDBAbs.getUsername() + "]" );
        // Window Icon
		try
		{
			InputStream inputStreamWinIcon = new FileInputStream( "resources" + File.separator + "images" + File.separator + "winicon.gif" );
			Image imgWinIcon = new Image( inputStreamWinIcon );
			this.getIcons().add( imgWinIcon );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
		}
		
		// Action when pushing 'x' on this view
		this.setOnCloseRequest( event->this.mainCtrl.close(this) );
		
		this.show();
        
        // change Language
        this.changeLang();
		
        // --------------------------------------------------
		// Mnemonic for Button
        // Mnemonic should be set after stage.show();
        // --------------------------------------------------
		// About Mnemonic
		// http://www.loop81.com/2011/12/javafx-2-mnemonic-what-eh.html
		// http://zetcode.com/gui/javafx/firstprograms/
		// https://stackoverflow.com/questions/12710468/using-javafx-2-2-mnemonic-and-accelerators
        // --------------------------------------------------
		this.setMnemonic();
		
		// set Focus on TextArea of DBSqlTab.
		dbSqlTab.setFocus();
    }
	
	// Create MenuBar
	private void createMenuBar()
	{
		this.mainMenuBar = new MainMenuBar( this );
	}
	
	private void createToolBar()
	{
		this.mainToolBar = new MainToolBar( this );
	}
	
	// Mnemonic for Button
	private void setMnemonic()
	{
		this.mainToolBar.setMnemonic();
	}
	
	private void setAction()
	{
		// thread start after opening this window.
		this.setOnShown
		(
			(event)->
			{
				System.out.println( "dbView Shown." );
				final CollectTask collectTask = new CollectTask( this.myDBAbs );
				// execute task
				this.future = this.service.submit( collectTask );
				
				collectTask.progressProperty().addListener
				(
					(obs,oldVal,newVal)->
					{
						System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
						if ( newVal.doubleValue() == 1.0 )
						{
							this.mainToolBar.taskDone();
						}
					}
				);
			}
		);
		
		// shutdown the thread pool on closing this window. 
		// http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
		this.setOnCloseRequest
		(	
			(event)->
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
		);
	}
	
	/**
	 * Quit Application
	 */
	public void quit()
	{
		this.mainCtrl.quit();
	}
	
	/**
	 * Reuqest for changing language
	 * @param langCode
	 */
	public void requestChangeLang( String langCode )
	{
		this.mainCtrl.changeLang( langCode );
	}
	
	/********************************
	 * Exec SQL Query 
	 ********************************
	 */
	public void Go()
	{
		// Call SQL on Selected Tab.
		try
		{
			ExecQueryDBInterface eqInterface = 
					(ExecQueryDBInterface)this.tabPane.getSelectionModel().getSelectedItem();
			eqInterface.Go( this.myDBAbs );
		}
		catch ( ClassCastException ccEx )
		{
			// suppress error
			//ccEx.printStackTrace();
		}
	}
	
	/********************************
	 * Refresh Activated Tab 
	 ********************************
	 */
	public void Refresh()
	{
		// Call SQL on Selected Tab.
		try
		{
			RefreshInterface refreshInterface = 
				(RefreshInterface)this.tabPane.getSelectionModel().getSelectedItem();
			refreshInterface.Refresh( this.myDBAbs );
		}
		catch ( ClassCastException ccEx )
		{
			// suppress error
			//ccEx.printStackTrace();
		}
	}
	
	/**
	 * Exec Explain for SQL
	 */
	public void execExplain()
	{
		// Call Explain on Selected Tab.
		Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( tab instanceof ExecExplainDBInterface )
		{
			((ExecExplainDBInterface)tab).Explain( this.myDBAbs );
		}
	}
	
	/********************************
	 * Switch Direction 
	 ********************************
	 */
	public void switchDirection()
	{
		// Call switch direction on Selected Tab.
		try
		{
			ToggleHorizontalVerticalInterface thvInterface = (ToggleHorizontalVerticalInterface)this.tabPane.getSelectionModel().getSelectedItem();
			thvInterface.switchDirection();
		}
		catch ( ClassCastException ccEx )
		{
			// suppress error
			//ccEx.printStackTrace();
		}
	}

	/********************************
	 * Create New Tab 
	 ********************************
	 */
	public void createNewTab()
	{
		DBSqlTab newTab = new DBSqlTab( this );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		// set Focus on TextArea of DBSqlTab.
		newTab.setFocus();
	}

	/********************************
	 * Create New Window 
	 ********************************
	 */
	public void createNewWindow()
	{
		this.mainCtrl.createNewWindow( this.myDBAbs, this );
	}
	
	/********************************
	 * Create New DB Connection 
	 ********************************
	 */
	public void createNewDBConnection()
	{
		this.mainCtrl.createNewDBConnectionAndOpenNewWindow();
	}

	/************************************************
	 * Copy datas on TableView without column heads 
	 ************************************************
	 */
	public void copyTableNoHead()
	{
		// Call copy on Selected Tab.
		try
		{
			CopyInterface copyInterface = (CopyInterface)this.tabPane.getSelectionModel().getSelectedItem();
			copyInterface.copyTableNoHead();
		}
		catch ( ClassCastException ccEx )
		{
			// suppress error
			//ccEx.printStackTrace();
		}
	}
	
	/************************************************
	 * Copy datas on TableView with column heads 
	 ************************************************
	 */
	public void copyTableWithHead()
	{
		// Call copy on Selected Tab.
		try
		{
			CopyInterface copyInterface = (CopyInterface)this.tabPane.getSelectionModel().getSelectedItem();
			copyInterface.copyTableWithHead();
		}
		catch ( ClassCastException ccEx )
		{
			// suppress error
			//ccEx.printStackTrace();
		}
	}
	
	/********************************
	 * Open Schema View 
	 ********************************
	 */
	public void openSchemaView()
	{
		// Activate DBSchemaTab, if already exists.
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if ( tab instanceof DBSchemaTab )
			{
				this.tabPane.getSelectionModel().select( tab );
				return;
			}
		}
		
		// Create DBSchemaTab, if it doesn't exist.
		final Tab newTab = new DBSchemaTab( this );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		this.Go();
	}
	
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		// change Language on MainMenuBar
		this.mainMenuBar.changeLang();
		
		// change Language on MainMenuBar
		this.mainToolBar.changeLang();
		
		// ----------------------------------------------
		// All Tabs
		// ----------------------------------------------
		ObservableList<Tab> tabLst = this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			try
			{
				if ( tab instanceof ChangeLangInterface )
				{
					ChangeLangInterface chInterface = (ChangeLangInterface)tab;
					chInterface.changeLang();
				}
			}
			catch ( ClassCastException ccEx )
			{
				// suppress error
				//ccEx.printStackTrace();
			}
		}
	}
	
	/*
	class CollectTask extends Task<Integer>
	{
		@Override
		protected Integer call() 
		{
			int progress = 0;
			try
			{
				System.out.println( "Task start." );
				SchemaEntity schemaRoot = myDBAbs.getSchemaRoot();
				if ( schemaRoot.getEntityLst().size() == 0 )
				{
					System.out.println( "Schema retriving..." );
					SchemaDBAbstract schemaDBAbs = SchemaDBFactory.getInstance( myDBAbs );
					if ( schemaDBAbs != null )
					{
						schemaDBAbs.selectSchemaLst();
						List<SchemaEntity> schemaEntityLst = schemaDBAbs.getSchemaEntityLst();
						schemaRoot.addEntityAll(schemaEntityLst);
						//System.out.println( "schemaEntityLst:size:" + schemaEntityLst.size() );
					}
				}
				else
				{
					System.out.println( "Schema already retrieved." );
				}
				
				// delete later
				{
					SchemaEntity schemaRoot2 = myDBAbs.getSchemaRoot();
					System.out.println( "schemaEntityLst:size:" + schemaRoot2.getEntityLst().size() );
				}
				
				System.out.println( "Task finish." );
				
				progress = 100;
				return progress;
			}
			catch ( SQLException sqlEx )
			{
				System.out.println( "CollectTask:SQLException." );
				progress = -2;
				
				return progress;
			}
		}
		
		@Override
		protected void succeeded()
		{
			System.out.println( "Task Success." );
		}
	}
	*/
}
