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
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

import milu.ctrl.MainController;
import milu.ctrl.sqlparse.SQLBag;
import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.ctrl.common.CopyInterface;
import milu.gui.ctrl.common.CounterInterface;
import milu.gui.ctrl.common.ExecExplainDBInterface;
import milu.gui.ctrl.common.ExecQueryDBInterface;
import milu.gui.ctrl.common.FocusInterface;
import milu.gui.ctrl.common.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.query.SqlTableView;
import milu.gui.view.DBView;
import milu.tool.MyTool;
import milu.db.MyDBAbstract;
import milu.db.access.MyDBOverFetchSizeException;
import milu.task.ToggleHVTask;
import milu.task.ExecQueryTask;
import milu.task.ExplainTask;
import milu.task.ExecScriptTask;

import milu.conf.AppConf;

public class DBSqlScriptTab extends Tab
	implements 
		ExecQueryDBInterface,
		ExecExplainDBInterface,
		ToggleHorizontalVerticalInterface,
		CounterInterface,
		CopyInterface,
		FocusInterface,
		ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
		"conf.lang.ctrl.query.DBSqlTab";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	private DBView          dbView = null;
	
	// Counter for how many times this class is opened.
	private static int counterOpend = 0;
	
	// TextArea on this pane
	private AnchorPane   upperPane = new AnchorPane();
	
	// TextArea for input SQL 
	private SqlTextArea  textAreaSQL = null;
	
	// "SqlTableView & Warning Message" on this Pane.
	private VBox         lowerPane = new VBox(2);
	
	// TableView for SQL result
	//private SqlTableView tableViewSQL = null;
	// TabPane for SQL result
	private TabPane         tabPane = new TabPane();
	
	// Label for SQL result count
	private Label labelCntSQL = new Label();
	
	// Label for SQL execution time
	private Label labelExecTimeSQL = new Label();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	public DBSqlScriptTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// Increment Counter
		// Counter for how many times this class is opened.
		DBSqlScriptTab.counterOpend++;
		
        // http://tutorials.jenkov.com/javafx/textarea.html
		this.textAreaSQL  = new SqlTextArea( dbView );
        // AnchorPane
        this.upperPane.getChildren().add( this.textAreaSQL );
        this.textAreaSQL.init();
        AnchorPane.setTopAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setBottomAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setLeftAnchor( this.textAreaSQL, 0.0 );
        AnchorPane.setRightAnchor( this.textAreaSQL, 0.0 );
        
        // https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
        //this.tableViewSQL = new SqlTableView();
        //this.lowerPane.getChildren().add( this.tableViewSQL );
        this.lowerPane.getChildren().add( this.tabPane );
        
		this.labelCntSQL.getStyleClass().add("label-statusbar");
		this.labelExecTimeSQL.getStyleClass().add("label-statusbar");
        
        HBox hBox = new HBox( 10 );
        hBox.setPadding( new Insets( 2, 2, 2, 2 ) );
        hBox.getChildren().addAll( this.labelCntSQL, this.labelExecTimeSQL );
        
        // SplitPane
        // http://fxexperience.com/2011/06/splitpane-in-javafx-2-0/
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.getItems().addAll( this.upperPane, this.lowerPane );
		splitPane.setDividerPositions( 0.3f, 0.7f );
		
		//this.tableViewSQL.prefHeightProperty().bind( this.lowerPane.heightProperty() );
		
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( splitPane );
		brdPane.setBottom( hBox );
		
		this.setContent( brdPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// set icon on Tab
		ImageView iv = new ImageView( mainCtrl.getImage("file:resources/images/sql.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		this.setGraphic( iv );
		
		// Tab Title
		this.setText( "X SQL" + Integer.valueOf( counterOpend ) );
		
		this.setAction();
	}
	
	private void setAction()
	{
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
	
	/**************************************************
	 * Override from ToggleHorizontalVerticalInterface
	 ************************************************** 
	 */
	@Override
	public void switchDirection()
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof ToggleHorizontalVerticalInterface )
		{
			((ToggleHorizontalVerticalInterface)selectedTab).switchDirection();
		}
		
		/*
		long startTime = System.nanoTime();
		int cnt = this.tableViewSQL.getRowSize();
		if ( cnt > 0 )
		{
			final ToggleHVTask toggleHVTask = new ToggleHVTask( this.tableViewSQL, cnt );
			// execute task
			this.service.submit( toggleHVTask );
			
			toggleHVTask.progressProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					System.out.println( "ToggleHVTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
					// task start.
					if ( newVal.doubleValue() == 0.0 )
					{
						this.dbView.taskProcessing();
						Label label = new Label( langRB.getString("LABEL_PROCESSING") );
						this.lowerPane.getChildren().clear();
						this.lowerPane.getChildren().add( label );
						System.out.println( "ToggleHVTask:clear" );
					}
					// task done.
					else if ( newVal.doubleValue() == 1.0 )
					{
						this.lowerPane.getChildren().clear();
						this.lowerPane.getChildren().add( this.tableViewSQL );
						this.dbView.taskDone();
						long endTime = System.nanoTime();
						this.setExecTime( endTime - startTime );
						System.out.println( "ToggleHVTask:set" );
					}
				}
			);
		}
		*/
	}

	/**************************************************
	 * Override from ExecQueryDBInterface
	 ************************************************** 
	 */
	@Override
	public void Go( MyDBAbstract myDBAbs )
	{
		long startTime = System.nanoTime();
		MainController mainController = this.dbView.getMainController();
		AppConf appConf = mainController.getAppConf();
		
		this.tabPane.getTabs().removeAll(this.tabPane.getTabs());
		
		List<SQLBag> sqlBagLst = this.textAreaSQL.getSQLBagLst();
		if ( sqlBagLst.size() == 0 )
		{
			String strSQL = this.textAreaSQL.getSQL();
			SQLBag sqlBag = new SQLBag();
			sqlBag.setSQL(strSQL);
			sqlBag.setCommand(SQLBag.COMMAND.QUERY);
			sqlBag.setType(SQLBag.TYPE.SELECT);
			sqlBagLst.add(sqlBag);
		}
		
		ExecScriptTask execScriptTask = new ExecScriptTask();
		execScriptTask.setDBView(this.dbView);
		execScriptTask.setMyDBAbstract(myDBAbs);
		execScriptTask.setAppConf(appConf);
		execScriptTask.setSQLBagLst(sqlBagLst);
		execScriptTask.setTabPane(this.tabPane);
		
		// execute task
		final Future<?> futureExecScriptTask = this.service.submit( execScriptTask );
		
		execScriptTask.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ExecScriptTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				// task start.
				if ( newVal.doubleValue() == 0.0 )
				{
					this.dbView.taskProcessing();
					VBox vBox = new VBox(2);
					Label  labelProcess = new Label( langRB.getString("LABEL_PROCESSING") );
					Button btnCancel    = new Button( langRB.getString("BTN_CANCEL") );
					vBox.getChildren().addAll( labelProcess, btnCancel );
					
					btnCancel.setOnAction
					(
						(event)->
						{
							futureExecScriptTask.cancel(true);
						}
					);
					
					this.lowerPane.getChildren().clear();
					this.lowerPane.getChildren().add( vBox );
					System.out.println( "ExecScriptTask:clear" );
				}
				// task done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					this.lowerPane.getChildren().clear();
					this.lowerPane.getChildren().add( this.tabPane );
					this.dbView.taskDone();
					long endTime = System.nanoTime();
					this.setExecTime( endTime - startTime );
					System.out.println( "ExecScriptTask:set" );
				}
			}
		);
		
		/*
		execScriptTask.valueProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ExecScriptTask:Value[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				this.lowerPane.getChildren().clear();
				// task exception.
				if ( newVal instanceof MyDBOverFetchSizeException )
				{
					Label     labelTitle = new Label( langRB.getString("TITLE_OVER_FETCH_SIZE") );
					String    msg        = langRB.getString("WARN_OVER_FETCH_SIZE");
					TextArea  txtMsg     = new TextArea( msg );
					int lfCnt = MyTool.getCharCount( msg, "\n" ); 
					txtMsg.setPrefRowCount( lfCnt+1 );
					
					this.tableViewSQL.prefHeightProperty().unbind();
					this.lowerPane.getChildren().addAll( labelTitle, txtMsg, this.tableViewSQL );
				}
				else if ( newVal instanceof SQLException )
				{
		    		this.showSQLException( (SQLException)newVal, myDBAbs );
				}
				else if ( newVal instanceof Exception )
				{
					Label     labelTitle = new Label( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
					
					String    strMsg     = newVal.getMessage();
					TextArea  txtMsg     = new TextArea( strMsg );
					txtMsg.setPrefColumnCount( MyTool.getCharCount( strMsg, "\n" )+1 );
					txtMsg.setEditable( false );
					
					String    strExp     = MyTool.getExceptionString( newVal );
					TextArea  txtExp     = new TextArea( strExp );
					txtExp.setPrefRowCount( MyTool.getCharCount( strExp, "\n" )+1 );
					txtExp.setEditable( false );
					
					VBox vBoxExp = new VBox(2);
					vBoxExp.getChildren().addAll( labelTitle, txtMsg, txtExp );
					
					SplitPane splitPane = new SplitPane();
					splitPane.setOrientation(Orientation.VERTICAL);
					splitPane.getItems().addAll( vBoxExp , this.tableViewSQL );
					splitPane.setDividerPositions( 0.3f, 0.7f );
					
					this.tableViewSQL.prefHeightProperty().unbind();
					this.lowerPane.getChildren().add( splitPane );
				}

				// Record Count
				this.setCount( this.tableViewSQL.getRowSize() );
				this.dbView.taskDone();
				long endTime = System.nanoTime();
				this.setExecTime( endTime - startTime );
				System.out.println( "ExecScriptTask:set" );
			}
		);
		*/
	}
	
	/**
	 * Override from ExecExplainDBInterface
	 */
	@Override
	public void Explain( MyDBAbstract myDBAbs )
	{
		/*
		long startTime = System.nanoTime();
		
		final ExplainTask explainTask = 
				new ExplainTask( myDBAbs, this.dbView.getMainController(), this.textAreaSQL.getSQL(), this.tableViewSQL );
		// execute task
		final Future<?> futureExplainTask = this.service.submit( explainTask );		
		
		explainTask.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ExplainTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				// task start.
				if ( newVal.doubleValue() == 0.0 )
				{
					this.dbView.taskProcessing();
					VBox vBox = new VBox(2);
					Label  labelProcess = new Label( langRB.getString("LABEL_PROCESSING") );
					Button btnCancel    = new Button( langRB.getString("BTN_CANCEL") );
					vBox.getChildren().addAll( labelProcess, btnCancel );
					
					btnCancel.setOnAction
					(
						(event)->
						{
							futureExplainTask.cancel(true);
						}
					);
					
					this.lowerPane.getChildren().clear();
					this.lowerPane.getChildren().add( vBox );
					System.out.println( "ExplainTask:clear" );
				}
				// task done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					this.lowerPane.getChildren().clear();
					this.lowerPane.getChildren().add( this.tableViewSQL );
					this.tableViewSQL.prefHeightProperty().bind( this.lowerPane.heightProperty() );
					// Record Count
					this.setCount( this.tableViewSQL.getRowSize() );
					this.dbView.taskDone();
					long endTime = System.nanoTime();
					this.setExecTime( endTime - startTime );
					System.out.println( "ExplainTask:set" );
				}
			}
		);
		
		explainTask.valueProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "ExplainTask:Value[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				this.lowerPane.getChildren().clear();
				// task exception.
				if ( newVal instanceof SQLException )
				{
		    		this.showSQLException( (SQLException)newVal, myDBAbs );
				}
				else if ( newVal instanceof Exception )
				{
					Label     labelTitle = new Label( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
					
					String    strMsg     = newVal.getMessage();
					TextArea  txtMsg     = new TextArea( strMsg );
					txtMsg.setPrefColumnCount( MyTool.getCharCount( strMsg, "\n" )+1 );
					txtMsg.setEditable( false );
					
					String    strExp     = MyTool.getExceptionString( newVal );
					TextArea  txtExp     = new TextArea( strExp );
					txtExp.setPrefRowCount( MyTool.getCharCount( strExp, "\n" )+1 );
					txtExp.setEditable( false );
					
					VBox vBoxExp = new VBox(2);
					vBoxExp.getChildren().addAll( labelTitle, txtMsg, txtExp );
					
					SplitPane splitPane = new SplitPane();
					splitPane.setOrientation(Orientation.VERTICAL);
					splitPane.getItems().addAll( vBoxExp , this.tableViewSQL );
					splitPane.setDividerPositions( 0.3f, 0.7f );
					
					this.tableViewSQL.prefHeightProperty().unbind();
					this.lowerPane.getChildren().add( splitPane );
				}

				// Record Count
				this.setCount( this.tableViewSQL.getRowSize() );
				this.dbView.taskDone();
				long endTime = System.nanoTime();
				this.setExecTime( endTime - startTime );
				System.out.println( "ExecQueryTask:set" );
			}
		);
		
		*/
	}
	
	private void showSQLException( SQLException sqlEx, MyDBAbstract myDBAbs )
	{
		System.out.println( "SQLState:" + sqlEx.getSQLState() );
		System.out.println( "ErrorCode:" + sqlEx.getErrorCode() );
		
		Label     labelTitle = new Label( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
		
		String    strMsg     = sqlEx.getMessage();
		TextArea  txtMsg     = new TextArea( strMsg );
		txtMsg.setPrefColumnCount( MyTool.getCharCount( strMsg, "\n" )+1 );
		txtMsg.setEditable( false );
		
		String    strExp     = MyTool.getExceptionString( sqlEx );
		TextArea  txtExp     = new TextArea( strExp );
		txtExp.setPrefRowCount( MyTool.getCharCount( strExp, "\n" )+1 );
		txtExp.setEditable( false );
		
		Button    btnReConnect = new Button (langRB.getString("BTN_RECONNECT"));
		btnReConnect.setOnAction
		(
			event->
			{
				try
				{
					myDBAbs.reconnect();
					dbView.Go();
				}
				catch ( SQLException sqlEx2 )
				{
					String strMsg2 = sqlEx2.getMessage();
					txtMsg.setText(strMsg2);
					txtMsg.setPrefColumnCount( MyTool.getCharCount( strMsg, "\n" )+1 );
					
					String strExp2 = MyTool.getExceptionString( sqlEx2 );
					txtExp.setText(strExp2);
					txtExp.setPrefRowCount( MyTool.getCharCount( strExp2, "\n" )+1 );
				}
			} 
		);
		
		HBox hBoxExp = new HBox(2);
		hBoxExp.getChildren().addAll( labelTitle, btnReConnect );
		
		VBox vBoxExp = new VBox(2);
		vBoxExp.getChildren().addAll( hBoxExp, txtMsg, txtExp );
		
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		//splitPane.getItems().addAll( vBoxExp , this.tableViewSQL );
		splitPane.setDividerPositions( 0.3f, 0.7f );
		
		//this.tableViewSQL.prefHeightProperty().unbind();
		this.lowerPane.getChildren().add( splitPane );
	}
	
	/**************************************************
	 * Override from CopyInterface
	 ************************************************** 
	 */
	@Override
	public void copyTableNoHead()
	{
		//this.tableViewSQL.copyTableNoHead();
	}
	
	/**
	 * Override from CopyInterface
	 */
	@Override
	public void copyTableWithHead()
	{
		//this.tableViewSQL.copyTableWithHead();
	}
	
	/**
	 * Load Language Resource
	 */
	private void loadLangResource()
	{
		this.langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.loadLangResource();
		//this.tableViewSQL.changeLang();
	}
	
}
