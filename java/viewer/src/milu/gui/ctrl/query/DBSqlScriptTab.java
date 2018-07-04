package milu.gui.ctrl.query;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.concurrent.Task;
import javafx.event.Event;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CounterInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.ProcInterface;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.query.ExecTaskFactory;
import milu.tool.MyGUITool;
import milu.tool.MyStringTool;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;

public class DBSqlScriptTab extends Tab
	implements 
		CounterInterface,
		FocusInterface,
		ChangeLangInterface,
		ActionInterface,
		SQLExecInterface,
		SQLExplainInterface,
		CopyTableInterface,
		DirectionSwitchInterface,
		ProcInterface,
		SQLFormatInterface
{
	private DBView          dbView = null;
	
	// Counter for how many times this class is opened.
	private static int counterOpend = 0;
	
	// ToolBar
	private ToolBar  toolBar = null;
	
	// TextArea on this pane
	private AnchorPane   upperPane = new AnchorPane();
	
	// TextArea for input SQL 
	private SqlTextArea  textAreaSQL = null;

	// Upper => [AnchorPane(upperPane)]-[SqlTextArea(textAreaSQL)]
	// Lower => [TabPane(tabPane)]
	private SplitPane splitPane = new SplitPane();
	
	// TabPane for SQL result
	private TabPane         tabPane = new TabPane();
	
	// Label for SQL result count
	private Label labelCntSQL = new Label("*");
	
	// Label for SQL execution time
	private Label labelExecTimeSQL = new Label("*");
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	public DBSqlScriptTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// Increment Counter
		// Counter for how many times this class is opened.
		DBSqlScriptTab.counterOpend++;
		
		this.toolBar = new DBSqlScriptToolBar(this.dbView);
		
        // http://tutorials.jenkov.com/javafx/textarea.html
		this.textAreaSQL  = new SqlTextArea( dbView );
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
        
        // SplitPane
        // http://fxexperience.com/2011/06/splitpane-in-javafx-2-0/
		this.splitPane.setOrientation(Orientation.VERTICAL);
		this.splitPane.getItems().addAll( this.upperPane, this.tabPane );
		this.splitPane.setDividerPositions( 0.3f, 0.7f );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setTop(this.toolBar);
		brdPane.setCenter( splitPane );
		brdPane.setBottom( hBox );
		
		this.setContent( brdPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/sql.png") ) );
		
		// Tab Title
		this.setText( "SQL" + Integer.valueOf( counterOpend ) );
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
	
	private void execTask( ExecTaskFactory.FACTORY_TYPE factoryType )
	{
		long startTime = System.nanoTime();
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		
		// Get "orientation" of the active tab
		Tab activeTab = 
				this.tabPane.getTabs().stream()
					.filter( tab->tab.isSelected() )
					.findFirst()
					.orElse(null);
		Orientation orientation = Orientation.HORIZONTAL;
		if ( activeTab != null && activeTab instanceof DirectionSwitchInterface )
		{
			orientation = ((DirectionSwitchInterface)activeTab).getOrientation();
		}
		
		// remove Tab "SQL Result"
		this.tabPane.getTabs().removeAll(this.tabPane.getTabs());
		
		List<SQLBag> sqlBagLst = this.textAreaSQL.getSQLBagLst();
		this.setCount( sqlBagLst.size() );
		
		Task<Exception> task =
			ExecTaskFactory.createFactory
			( 
				factoryType, 
				this.dbView, 
				myDBAbs, 
				appConf, 
				this.tabPane, 
				sqlBagLst, 
				this,
				orientation
			); 
		
		// execute task
		final Future<?> future = this.service.submit( task );
		
		task.progressProperty().addListener((obs,oldVal,newVal)->{
			System.out.println( "ExecExplainAllTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
			ResourceBundle cmnLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
			// task start.
			if ( newVal.doubleValue() == 0.0 )
			{
				this.beginProc();
				VBox vBox = new VBox(2);
				Label  labelProcess = new Label( langRB.getString("LABEL_PROCESSING") );
				Button btnCancel    = new Button( cmnLangRB.getString("BTN_CANCEL") );
				vBox.getChildren().addAll( labelProcess, btnCancel );
				
				// Oracle =>
				//   java.sql.SQLRecoverableException
				btnCancel.setOnAction((event)->{
					future.cancel(true);
				});
				
				Tab tab = new Tab("...");
				tab.setContent( vBox );
				this.tabPane.getTabs().add(tab);
				System.out.println( "ExecExplainAllTask:clear" );
			}
			// task done.
			else if ( newVal.doubleValue() == 1.0 )
			{
				//this.lowerPane.getChildren().clear();
				//this.lowerPane.getChildren().add( this.tabPane );
				// remove tab for cancel
				this.tabPane.getTabs().remove(0);
				this.endProc();
				long endTime = System.nanoTime();
				this.setExecTime( endTime - startTime );
				System.out.println( "ExecExplainAllTask:clear" );
			}
		});
		
		// Exception Returned by Task
		task.valueProperty().addListener( (obs,oldVal,newVal)->this.showException(newVal,startTime) );
	}
	
	private void showException( Exception ex, long startTime )
	{
		// get Tab "..." 
		Tab tab = this.tabPane.getTabs().get(0);
		tab.setContent(null);
		
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		
		Label     labelTitle = new Label( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
		
		String    strMsg     = ex.getMessage();
		TextArea  txtMsg     = new TextArea( strMsg );
		txtMsg.setPrefColumnCount( MyStringTool.getCharCount( strMsg, "\n" )+1 );
		txtMsg.setEditable( false );
		
		String    strExp     = MyStringTool.getExceptionString( ex );
		TextArea  txtExp     = new TextArea( strExp );
		txtExp.setPrefRowCount( MyStringTool.getCharCount( strExp, "\n" )+1 );
		txtExp.setEditable( false );
		
		VBox vBoxExp = new VBox(2);
		vBoxExp.getChildren().addAll( labelTitle, txtMsg, txtExp );
		
		tab.setContent(vBoxExp);
		
		long endTime = System.nanoTime();
		this.setExecTime( endTime - startTime );
	}
	
	// SQLExecInterface
	@Override
	public void execSQL( Event event )
	{
		/*
		if (event instanceof KeyEvent)
		{
			KeyEvent keyEvent = (KeyEvent)event;
			System.out.println( "execSQL KeyEvent:" + keyEvent.getCode() );
			if ( keyEvent.isControlDown() )
			{
				System.out.println( "execSQL KeyEvent => Ctrl is down." );
			}
			return;
		}
		*/		
		System.out.println( "execSQL" );
		this.execTask(ExecTaskFactory.FACTORY_TYPE.SCRIPT);
	}
	
	// SQLExplainInterface
	@Override
	public void explainSQL( Event event )
	{
		this.execTask(ExecTaskFactory.FACTORY_TYPE.EXPLAIN);
	}
	
	// CopyTableInterface
	@Override
	public void copyTableNoHead( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof CopyTableInterface )
		{
			((CopyTableInterface)selectedTab).copyTableNoHead( event );
		}
	}
	
	// CopyTableInterface
	@Override
	public void copyTableWithHead( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof CopyTableInterface )
		{
			((CopyTableInterface)selectedTab).copyTableWithHead( event );
		}
	}
	
	// DirectionSwitchInterface
	@Override
	public Orientation getOrientation()
	{
		throw new UnsupportedOperationException();
	}
	
	// DirectionSwitchInterface
	@Override
	public void setOrientation( Orientation orientation )
	{
		throw new UnsupportedOperationException();
	}
	
	// DirectionSwitchInterface
	@Override
	public void switchDirection( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof DirectionSwitchInterface )
		{
			((DirectionSwitchInterface)selectedTab).switchDirection( event );
		}
	}

	// SQLFormatInterface
	@Override
	public void formatSQL( Event event )
	{
		this.textAreaSQL.formatSQL( event );
	}
	
	// ProcInterface
	@Override
	public void beginProc()
	{
		((ProcInterface)this.toolBar).beginProc();
	}
	
	// ProcInterface
	@Override
	public void endProc()
	{
		((ProcInterface)this.toolBar).endProc();
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.textAreaSQL.changeLang();
	}
	
}
