package milu.gui.stmt.query;

import java.io.File;
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
import javafx.scene.control.ProgressBar;
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

import net.sf.jsqlparser.JSQLParserException;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.ParseAction;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CounterInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.ProcInterface;
import milu.task.stmt.query.ExecTaskFactory;
import milu.tool.MyGUITool;
import milu.tool.MyStringTool;
import milu.tool.LimitedQueue;
import milu.tool.MyFileTool;

public class DBSqlScriptTab extends Tab
	implements 
		CounterInterface,
		FocusInterface,
		ChangeLangInterface,
		ActionInterface,
		SQLExecInterface,
		SQLExplainInterface,
		SQLExecWithoutParseInterface,
		CopyTableInterface,
		DirectionSwitchInterface,
		ProcInterface,
		SQLFormatInterface,
		SQLHistoryInterface,
		SQLFileInterface
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

	// Upper => [AnchorPane(upperPane)]-[SqlTextArea(textAreaSQL)]
	// Lower => [TabPane(tabPane)]
	private SplitPane splitPane = new SplitPane();
	
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
	
	public DBSqlScriptTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// Increment Counter
		// Counter for how many times this class is opened.
		DBSqlScriptTab.counterOpend++;
		
		this.toolBar = new DBSqlScriptToolBar(this.dbView);
		
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
		
		this.barProgress.setPrefWidth(500);
		
		MainController mainCtrl = this.dbView.getMainController();
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/sql.png") ) );
		
		// Tab Title
		this.setText( "SQL" + Integer.valueOf( counterOpend ) );
		
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
	
	private void execTask( ExecTaskFactory.FACTORY_TYPE factoryType, ParseAction.SQLPARSE sqlParse, ParseAction.SQLTYPE sqlType )
	{
		long startTime = System.nanoTime();
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		
		// Get fetchPos, fetchMax from ToolBar
		// &
		// Set those values to AppConf
		AppConf appConfClone = null;
		try
		{
			appConfClone = (AppConf)appConf.clone();
		}
		catch ( CloneNotSupportedException cnsEx )
		{
		}
		appConfClone.setFetchPos(((SQLFetchInterface)this.toolBar).getFetchPos());
		appConfClone.setFetchMax(((SQLFetchInterface)this.toolBar).getFetchMax());
		
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
		
		// put "new SQL" to "SQL History List"
		String strSQL = this.textAreaSQL.getText();
		if ( this.histSQLLst.contains(strSQL) == false )
		{
			this.histSQLPos = 0;
			this.histSQLLst.add(0,strSQL);
		}
		
		// remove Tab "SQL Result"
		this.tabPane.getTabs().removeAll(this.tabPane.getTabs());
		
		// SQL List for Exec
		List<SQLBag> sqlBagLst = null;
		if ( sqlParse == ParseAction.SQLPARSE.WITH_PARSE )
		{
			try
			{
				sqlBagLst = this.textAreaSQL.getSQLBagLst();
			}
			catch ( JSQLParserException parseEx )
			{
				this.tabPane.getTabs().add(new Tab("..."));
				this.showException( parseEx, startTime );
				return;
			}
		}
		else if ( sqlParse == ParseAction.SQLPARSE.WITHOUT_PARSE )
		{
			if ( sqlType == ParseAction.SQLTYPE.QUERY )
			{
				sqlBagLst = this.textAreaSQL.getSQLBagLst( SQLBag.COMMAND.QUERY );
			}
			else if ( sqlType == ParseAction.SQLTYPE.TRANS )
			{
				sqlBagLst = this.textAreaSQL.getSQLBagLst( SQLBag.COMMAND.TRANSACTION );
			}
		}
		else if ( sqlParse == ParseAction.SQLPARSE.WITHOUT_PARSE_SPLIT_SEMI )
		{
			if ( sqlType == ParseAction.SQLTYPE.QUERY )
			{
				sqlBagLst = this.textAreaSQL.getSQLBagLst( SQLBag.COMMAND.QUERY, ";" );
			}
			else if ( sqlType == ParseAction.SQLTYPE.TRANS )
			{
				sqlBagLst = this.textAreaSQL.getSQLBagLst( SQLBag.COMMAND.TRANSACTION, ":" );
			}
		}
		this.setCount( sqlBagLst.size() );
		
		Task<Exception> task =
			ExecTaskFactory.createFactory
			( 
				factoryType, 
				this.dbView, 
				myDBAbs, 
				appConfClone, 
				this.tabPane, 
				sqlBagLst, 
				this,
				orientation
			); 
		
		// execute task
		final Future<?> future = this.service.submit( task );
		
		this.barProgress.progressProperty().unbind();
		this.barProgress.progressProperty().bind(task.progressProperty());
		
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		ResourceBundle cmnLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		Label  labelProcess = new Label();
		labelProcess.textProperty().unbind();
		labelProcess.setText( langRB.getString("LABEL_PROCESSING") );
		labelProcess.textProperty().bind(task.messageProperty());
		
		task.progressProperty().addListener((obs,oldVal,newVal)->{
			//System.out.println( "ExecExplainAllTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			// task start.
			if ( newVal.doubleValue() == 0.0 )
			{
				System.out.println( "ExecTask:start" );
				this.beginProc();
				VBox vBox = new VBox(2);
				Button btnCancel    = new Button( cmnLangRB.getString("BTN_CANCEL") );
				vBox.getChildren().addAll( labelProcess, this.barProgress, btnCancel );
				
				// Oracle =>
				//   java.sql.SQLRecoverableException
				btnCancel.setOnAction((event)->{
					future.cancel(true);
				});
				
				Tab tab = new Tab("...");
				tab.setContent( vBox );
				this.tabPane.getTabs().add(tab);
			}
			// task done.
			else if ( newVal.doubleValue() == 1.0 )
			{
				// remove tab for cancel
				this.tabPane.getTabs().remove(0);
				this.endProc();
				long endTime = System.nanoTime();
				this.setExecTime( endTime - startTime );
				System.out.println( "ExecTask:done" );
			}
		});
		
		// Exception Returned by Task
		task.valueProperty().addListener((obs,oldVal,ex)->{
			this.showException(ex,startTime);
		});
	}
	
	private void showException( Exception ex, long startTime )
	{
		// get Tab "..." 
		Tab tab = this.tabPane.getTabs().get(0);
		tab.setContent(null);
		
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		
		Label     labelTitle = new Label();
		if ( ex instanceof JSQLParserException )
		{
			labelTitle.setText(langRB.getString("TITLE_EXEC_PARSE_ERROR"));
		}
		else
		{
			labelTitle.setText(langRB.getString("TITLE_EXEC_QUERY_ERROR"));
		}
		
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
		this.execTask( ExecTaskFactory.FACTORY_TYPE.SCRIPT, ParseAction.SQLPARSE.WITH_PARSE, ParseAction.SQLTYPE.ANY );
	}
	
	// SQLExplainInterface
	@Override
	public void explainSQL( Event event )
	{
		this.execTask( ExecTaskFactory.FACTORY_TYPE.EXPLAIN, ParseAction.SQLPARSE.WITH_PARSE, ParseAction.SQLTYPE.ANY );
	}
	
	// SQLExecWithoutParseInterface
	@Override
	public void execSQLQuery( Event event )
	{
		this.execTask( ExecTaskFactory.FACTORY_TYPE.SCRIPT, ParseAction.SQLPARSE.WITHOUT_PARSE, ParseAction.SQLTYPE.QUERY );
	}
	
	// SQLExecWithoutParseInterface
	@Override
	public void execSQLTrans( Event event )
	{
		this.execTask( ExecTaskFactory.FACTORY_TYPE.SCRIPT, ParseAction.SQLPARSE.WITHOUT_PARSE, ParseAction.SQLTYPE.TRANS );
	}
	
	// SQLExecWithoutParseInterface
	@Override
	public void execSQLTransSemi( Event event )
	{
		this.execTask( ExecTaskFactory.FACTORY_TYPE.SCRIPT, ParseAction.SQLPARSE.WITHOUT_PARSE_SPLIT_SEMI, ParseAction.SQLTYPE.TRANS );
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

	// SQLFormatInterface
	@Override
	public void oneLineSQL( Event event )
	{
		this.textAreaSQL.oneLineSQL( event );
	}

	// SQLHistoryInterface
	@Override
	public void prevSQL( Event event )
	{
		this.histSQLPos++;
		int size = this.histSQLLst.size();
		if ( size == 0 )
		{
			this.histSQLPos = 0;
		}
		else if ( this.histSQLPos > (size-1) )
		{
			this.histSQLPos = size-1;
		}
		this.setSQLTextFromHistory();
	}

	// SQLHistoryInterface
	@Override
	public void nextSQL( Event event )
	{
		this.histSQLPos--;
		if ( this.histSQLPos < -1 )
		{
			this.histSQLPos = -1;
		}
		this.setSQLTextFromHistory();
	}
	
	private void setSQLTextFromHistory()
	{
		if ( this.histSQLPos >= this.histSQLLst.size() )
		{
			return;
		}
		else if ( this.histSQLPos == -1 )
		{
			this.textAreaSQL.setText(null);
			return;
		}
		
		String strSQL = this.histSQLLst.get(this.histSQLPos);
		if ( strSQL != null )
		{
			this.textAreaSQL.setText(strSQL);
		}
	}

	// SQLFileInterface
	@Override
	public void openSQL( Event event )
	{
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		File file = MyGUITool.fileOpenDialog( appConf.getInitDirSQLFile(), null, null, this.getTabPane() );
		if ( file == null )
		{
			return;
		}
		appConf.setInitDirSQLFile(file.getParent());
		MyFileTool.save(mainCtrl, appConf);
		
		String strSQL = MyFileTool.loadFile(file, mainCtrl, appConf);
		this.textAreaSQL.setText(strSQL);
	}

	// SQLFileInterface
	@Override
	public void saveSQL( Event event )
	{
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		File file = MyGUITool.fileSaveDialog( appConf.getInitDirSQLFile(), null, this.getTabPane() );
		if ( file == null )
		{
			return;
		}
		appConf.setInitDirSQLFile(file.getParent());
		MyFileTool.save(mainCtrl, appConf);
		
		MyFileTool.saveFile(file, this.textAreaSQL.getText(), mainCtrl, appConf);
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
		((ChangeLangInterface)this.toolBar).changeLang();
		this.textAreaSQL.changeLang();
	}
	
}
