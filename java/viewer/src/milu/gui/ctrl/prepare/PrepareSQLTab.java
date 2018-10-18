package milu.gui.ctrl.prepare;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CounterInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.ctrl.query.DBSqlScriptTab;
import milu.gui.ctrl.query.SQLTextArea;
import milu.gui.ctrl.query.SQLExecInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.task.query.ExecTaskFactory;
import milu.tool.LimitedQueue;
import milu.tool.MyGUITool;

public class PrepareSQLTab extends Tab 
	implements
		CounterInterface,
		FocusInterface,
		ChangeLangInterface,
		ActionInterface,
		SQLExecInterface
{
	private DBView          dbView = null;
	
	// Counter for how many times this class is opened.
	private static int counterOpend = 0;
	
	// ToolBar
	private ToolBar  toolBar = null;
	
	// TextArea on this pane
	private AnchorPane   upperPane = new AnchorPane();
	
	// TextArea for input SQL 
	private SQLTextArea  textAreaSQL = null;

	// ---------------------------------------------------------------------
	// Upper => see. splitPane2
	// Lower => [TabPane(tabPane)]
	// ---------------------------------------------------------------------
	private SplitPane splitPane1 = new SplitPane();
	
	// ---------------------------------------------------------------------
	// Upper-Left  => [AnchorPane(upperPane)]-[SqlTextArea(textAreaSQL)]
	// Upper-Right => [ObjTableView]
	// ---------------------------------------------------------------------
	private SplitPane splitPane2 = new SplitPane();
	
	// Parameter for PreparedStatement
	private ObjTableView objTableView = null;
	
	// TabPane for SQL result
	private TabPane         tabPane = new TabPane();
	
	// Label for SQL result count
	private Label labelCntSQL = new Label("*");
	
	// Label for SQL execution time
	private Label labelExecTimeSQL = new Label("*");
	
	private ProgressBar barProgress = new ProgressBar();
	
	// SQL History List
	private List<String>  histSQLLst = null;
	
	// SQL History Position
	private int           histSQLPos = -1;
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	public PrepareSQLTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// Increment Counter
		// Counter for how many times this class is opened.
		PrepareSQLTab.counterOpend++;
		
		//this.toolBar = new DBSqlScriptToolBar(this.dbView);
		
        // http://tutorials.jenkov.com/javafx/textarea.html
		this.textAreaSQL  = new SQLTextArea( dbView );
        // AnchorPane
        this.upperPane.getChildren().add( this.textAreaSQL );
        this.textAreaSQL.init();
        AnchorPane.setTopAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setBottomAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setLeftAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setRightAnchor( this.textAreaSQL, 0.0 );
        
        // TabPane of SQL results
        this.tabPane.setTabClosingPolicy( TabClosingPolicy.UNAVAILABLE );
        
		this.labelCntSQL.getStyleClass().add("DBSqlTab_Label_On_StatusBar");
		this.labelExecTimeSQL.getStyleClass().add("DBSqlTab_Label_On_StatusBar");
        
        HBox hBox = new HBox( 10 );
        hBox.setPadding( new Insets( 2, 2, 2, 2 ) );
        hBox.getChildren().addAll( this.labelCntSQL, this.labelExecTimeSQL );

    	// ---------------------------------------------------------------------
    	// Upper => see. splitPane2
    	// Lower => [TabPane(tabPane)]
    	// ---------------------------------------------------------------------
        // SplitPane1
        // http://fxexperience.com/2011/06/splitpane-in-javafx-2-0/
		this.splitPane1.setOrientation(Orientation.VERTICAL);
		this.splitPane1.getItems().addAll( this.splitPane2, this.tabPane );
		this.splitPane1.setDividerPositions( 0.3f, 0.7f );

		// ---------------------------------------------------------------------
		// Upper-Left  => [AnchorPane(upperPane)]-[SqlTextArea(textAreaSQL)]
		// Upper-Right => [TableView]
		// ---------------------------------------------------------------------
		// Parameter for PreparedStatement
		this.objTableView = new ObjTableView(this.dbView);
		this.splitPane2.setOrientation(Orientation.HORIZONTAL);
		this.splitPane2.getItems().addAll( this.upperPane, this.objTableView );
		this.splitPane2.setDividerPositions( 0.5f, 0.5f );
		
		BorderPane brdPane = new BorderPane();
		//brdPane.setTop(this.toolBar);
		brdPane.setCenter( splitPane1 );
		brdPane.setBottom( hBox );
		
		this.setContent( brdPane );
		
		this.barProgress.setPrefWidth(500);
		
		MainController mainCtrl = this.dbView.getMainController();
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/prepare.png") ) );
		
		// Tab Title
		this.setText( "Prepared" + Integer.valueOf( counterOpend ) );
		
		// SQL History List
		this.histSQLLst = new LimitedQueue<String>(10);
	}
	
	
	// ActionInterface
	@Override
	public void setAction( Object obj )
	{
		// set Action on ToolBar
		((ActionInterface)this.toolBar).setAction(this);
		
		// shutdown the thread pool on closing this window. 
		// http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
		this.setOnCloseRequest
		((event)->{
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
		});
	}
	
	/**
	 * set Focus on TextArea
	 */
	@Override
	public void setFocus()
	{
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		// https://stackoverflow.com/questions/20049452/javafx-focusing-textfield-programmatically
		// call after "new Scene"
		Platform.runLater( ()->{ this.textAreaSQL.requestFocus(); System.out.println( "textAreaSQL focused."); } );
	}
	
	/**************************************************
	 * Override from CounterInterface
	 ************************************************** 
	 */
	@Override	
	public void setCount( int cnt )
	{
		this.labelCntSQL.setText( String.valueOf(cnt) );
	}
	
	public void setExecTime( long nanoSec )
	{
		this.labelExecTimeSQL.setText( String.format( "%,d", nanoSec ) + "nsec" );
	}
	
	
	// SQLExecInterface
	@Override
	public void execSQL( Event event )
	{
		//this.execTask( ExecTaskFactory.FACTORY_TYPE.SCRIPT, DBSqlScriptTab.SQLPARSE.WITH_PARSE, DBSqlScriptTab.SQLTYPE.ANY );
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		((ChangeLangInterface)this.toolBar).changeLang();
		this.textAreaSQL.changeLang();
	}

}
