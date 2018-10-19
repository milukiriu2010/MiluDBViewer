package milu.gui.ctrl.prepare;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.ParseAction;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CounterInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.ctrl.query.SQLTextArea;
import milu.gui.ctrl.query.SQLExecInterface;
import milu.gui.ctrl.query.SQLFetchInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.ProcInterface;
import milu.task.prepare.PrepareTaskFactory;
import milu.tool.LimitedQueue;
import milu.tool.MyGUITool;
import milu.tool.MyStringTool;
import net.sf.jsqlparser.JSQLParserException;

public class PrepareSQLTab extends Tab 
	implements
		CounterInterface,
		FocusInterface,
		ChangeLangInterface,
		ActionInterface,
		SQLExecInterface,
		ProcInterface
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
		
		this.toolBar = new PrepareSQLToolBar(this.dbView);
		
        // http://tutorials.jenkov.com/javafx/textarea.html
		this.textAreaSQL  = new SQLTextArea( dbView );
		this.textAreaSQL.setText
		(
		"select * from all_objects \n" + 
		"where \n" + 
		" status = ? \n" + 
		" and\r\n" + 
		" namespace = ? \n" + 
		""
		);
		
		
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

		// -----------------------------------------------
		// Initialize data for TableView
		// -----------------------------------------------
		this.objTableView = new ObjTableView(this.dbView);
		List<Object> headLst = new ArrayList<>();
		headLst.add(1);
		headLst.add(2);
		List<Object> dataRow1 = new ArrayList<>();
		dataRow1.add("INVALID");
		dataRow1.add(1);
		List<Object> dataRow2 = new ArrayList<>();
		dataRow2.add("INVALID");
		dataRow2.add(2);
		List<List<Object>> dataRowLst = new ArrayList<>();
		dataRowLst.add(dataRow1);
		dataRowLst.add(dataRow2);
		this.objTableView.setTableViewData(headLst, dataRowLst);
		
		// ---------------------------------------------------------------------
		// Upper-Left  => [AnchorPane(upperPane)]-[SqlTextArea(textAreaSQL)]
		// Upper-Right => [TableView]
		// ---------------------------------------------------------------------
		// Parameter for PreparedStatement
		this.splitPane2.setOrientation(Orientation.HORIZONTAL);
		this.splitPane2.getItems().addAll( this.upperPane, this.objTableView );
		this.splitPane2.setDividerPositions( 0.5f, 0.5f );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setTop(this.toolBar);
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
	
	
	private void execTask( PrepareTaskFactory.FACTORY_TYPE factoryType, ParseAction.SQLPARSE sqlParse, ParseAction.SQLTYPE sqlType )
	{
		System.out.println( "execTask:sqlParse[" + sqlParse + "]" );
		
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

		// PreparedStatementのプレースホルダに渡すパラメータ
		List<List<Object>> placeHolderLst = this.objTableView.getDataList();
		
		Task<Exception> task =
			PrepareTaskFactory.createFactory
			( 
				factoryType, 
				this.dbView, 
				myDBAbs, 
				appConfClone, 
				this.tabPane, 
				sqlBagLst, 
				this,
				orientation,
				placeHolderLst
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
		this.execTask( PrepareTaskFactory.FACTORY_TYPE.SCRIPT, ParseAction.SQLPARSE.WITH_PARSE, ParseAction.SQLTYPE.ANY );
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
