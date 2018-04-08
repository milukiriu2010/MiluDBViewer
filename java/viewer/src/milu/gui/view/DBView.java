package milu.gui.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.ctrl.common.CopyInterface;
import milu.gui.ctrl.common.ExecExplainDBInterface;
import milu.gui.ctrl.common.ExecQueryDBInterface;
import milu.gui.ctrl.common.FocusInterface;
import milu.gui.ctrl.common.RefreshInterface;
import milu.gui.ctrl.common.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.menu.MainMenuBar;
import milu.gui.ctrl.menu.MainToolBar;
import milu.gui.ctrl.query.DBSqlScriptTab;
import milu.gui.ctrl.schema.DBSchemaTab;
import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;
import milu.db.MyDBAbstract;
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
	
	// BorderPane 
	private BorderPane brdPane = new BorderPane();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	// Message from Task
	private Label  lblMsg = new Label();
	
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
	
	public MainController getMainController()
	{
		return this.mainCtrl;
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
        
		// View for SQL
		Tab dbSqlTab = new DBSqlScriptTab( this );
		
		// VBox for MenuBar/ToolBar
		VBox vboxMenu = new VBox( 2 );
		vboxMenu.getChildren().addAll( this.mainMenuBar, this.mainToolBar );
		
		// TabPane for multi view
		this.tabPane = new TabPane();
		this.tabPane.getTabs().add( dbSqlTab );
		// Enable tag dragging
		this.mainCtrl.addDraggingTabPaneMap( this.myDBAbs, this.tabPane );
		
		// put items on Pane
        this.brdPane.setTop( vboxMenu );
        this.brdPane.setCenter( this.tabPane );
		
        // set action
        this.setAction();
        
        // Stage for New Window
        // http://o7planning.org/en/11533/opening-a-new-window-in-javafx
        Scene scene = new Scene(this.brdPane, 800, 600 );
        // load css on DBView elements
        String[] cssLst =
        	{
        		"/conf/css/ctrl/common/LabelTable.css",
        		"/conf/css/ctrl/menu/MainToolBar.css",
        		"/conf/css/ctrl/query/DBSqlTab.css",
        		"/conf/css/ctrl/query/SqlTableView.css",
        		"/conf/css/ctrl/query/SqlTextArea.css",
        		"/conf/css/ctrl/schema/SchemaTreeView.css",
        		"/conf/css/ctrl/schema/SchemaERView.css"
        	};
        for ( String css : cssLst )
        {
    		scene.getStylesheets().add(	getClass().getResource(css).toExternalForm() );
        }
        this.setScene(scene);
		// Window Title
        this.setTitle( "MiluDBViewer[URL=" + this.myDBAbs.getUrl() + "][USER=" + this.myDBAbs.getUsername() + "]" );
        // Window Icon
        this.getIcons().add( this.mainCtrl.getImage("file:resources/images/winicon.gif") );
		
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
		((FocusInterface)dbSqlTab).setFocus();
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
				final CollectTask collectTask = new CollectTask();
				collectTask.setMainController(this.mainCtrl);
				collectTask.setMyDBAbstract(this.myDBAbs);
				// execute task
				this.service.submit( collectTask );
				
				collectTask.progressProperty().addListener
				(
					(obs,oldVal,newVal)->
					{
						System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
						// Task Done.
						if ( newVal.doubleValue() == 1.0 )
						{
							//this.mainToolBar.taskDone();
							this.taskDone();
							this.brdPane.setBottom(null);
						}
					}
				);
				
				collectTask.messageProperty().addListener
				(
					(obs,oldVal,newVal)->
					{
						System.out.println( "CollectTask:Message[" + newVal + "]" );
						this.lblMsg.setText(newVal);
						this.brdPane.setBottom(lblMsg);
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
		
		/*
		Child Node:class javafx.scene.control.skin.TabPaneSkin$TabHeaderArea
		  Child Node:class javafx.scene.layout.StackPane
		  Child Node:class javafx.scene.control.skin.TabPaneSkin$TabHeaderArea$1
		    Child Node:class javafx.scene.control.skin.TabPaneSkin$TabHeaderSkin
		      Child Node:class javafx.scene.control.skin.TabPaneSkin$TabHeaderSkin$4
		        Child Node:class javafx.scene.control.Label
		          Child Node:class javafx.scene.control.Label
		            Child Node:class javafx.scene.image.ImageView
		            Child Node:class com.sun.javafx.scene.control.LabeledText
		        Child Node:class javafx.scene.control.skin.TabPaneSkin$TabHeaderSkin$2
		        Child Node:class javafx.scene.layout.Region
		  Child Node:class javafx.scene.control.skin.TabPaneSkin$TabControlButtons
		    Child Node:class javafx.scene.control.skin.TabPaneSkin$TabControlButtons$1
		      Child Node:class javafx.scene.layout.Pane
		        Child Node:class javafx.scene.layout.StackPane
		*/
		/*
		this.tabPane.setOnMouseEntered
		(
			(event)->
			{
				System.out.println( "=== DBView: MouseEntered ===" );
				//MyTool.skimThroughChildren( this.tabPane, 0 );
				//String strLabel = MyTool.findTabText( this.tabPane, null );
				//System.out.println( "findTabText:hit:" + strLabel );
			}
		);
		*/
	}
	
	public void taskProcessing()
	{
		this.mainToolBar.taskProcessing();
	}
	
	public void taskDone()
	{
		this.mainToolBar.taskDone();
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
		Tab newTab = null;
		newTab = new DBSqlScriptTab( this );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		// set Focus on TextArea of DBSqlTab.
		((FocusInterface)newTab).setFocus();
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
	
	public void commit()
	{
		try
		{
			this.myDBAbs.commit();
		}
		catch ( SQLException sqlEx )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
	}
	
	public void rollback()
	{
		try
		{
			this.myDBAbs.rollback();
		}
		catch ( SQLException sqlEx )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
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
}
