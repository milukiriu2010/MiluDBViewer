package milu.gui.ctrl.query;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CopyInterface;
import milu.gui.ctrl.common.inf.CounterInterface;
import milu.gui.ctrl.common.inf.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;
import milu.db.MyDBAbstract;
import milu.db.access.MyDBOverFetchSizeException;
import milu.task.ToggleHVTask;

public class DBResultTab extends Tab
	implements 
		ToggleHorizontalVerticalInterface,
		CopyInterface,
		CounterInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	// TextField for SQL
	private TextField  textFieldSQL = new TextField(); 
	
	// "ObjTableView & Warning Message" on this Pane.
	private VBox         lowerPane = new VBox(2);
	
	// TableView for SQL result
	private ObjTableView tableViewSQL = null;
	
	// Label for SQL result count
	private Label labelCntSQL = new Label();
	
	// Label for SQL execution time
	private Label labelExecTimeSQL = new Label();
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	public DBResultTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.textFieldSQL.setEditable(false);
		
        // https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
        this.tableViewSQL = new ObjTableView(this.dbView);
        this.lowerPane.getChildren().add( this.tableViewSQL );
        
		this.labelCntSQL.getStyleClass().add("DBSqlTab_Label_On_StatusBar");
		this.labelExecTimeSQL.getStyleClass().add("DBSqlTab_Label_On_StatusBar");
        
        HBox hBox = new HBox( 10 );
        hBox.setPadding( new Insets( 2, 2, 2, 2 ) );
        hBox.getChildren().addAll( this.labelCntSQL, this.labelExecTimeSQL );
        
		
		//this.tableViewSQL.prefHeightProperty().bind( this.lowerPane.heightProperty() );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setTop( this.textFieldSQL );
		brdPane.setCenter( this.lowerPane );
		brdPane.setBottom( hBox );
		
		this.setContent( brdPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// set icon on Tab
		ImageView iv = new ImageView( mainCtrl.getImage("file:resources/images/result.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		this.setGraphic( iv );
				
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
	
	public void setSQL( String sql )
	{
		if ( sql != null )
		{
			this.textFieldSQL.setText(sql);
		}
		else
		{
			((BorderPane)this.getContent()).setTop(null);
		}
	}
	
	public void setDataOnTableViewSQL( List<Object> headLst, List<List<Object>> dataLst )
	{
		this.tableViewSQL.setTableViewData(headLst, dataLst);
		this.setCount( this.tableViewSQL.getRowSize() );
	}
	
	public void setException( Exception ex )
	{
		this.lowerPane.getChildren().removeAll(this.lowerPane.getChildren());
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		if ( ex instanceof MyDBOverFetchSizeException )
		{
			Label     labelTitle = new Label( langRB.getString("TITLE_OVER_FETCH_SIZE") );
			String    msg        = langRB.getString("WARN_OVER_FETCH_SIZE");
			TextArea  txtMsg     = new TextArea( msg );
			int lfCnt = MyTool.getCharCount( msg, "\n" ); 
			txtMsg.setPrefRowCount( lfCnt+1 );
			
			this.tableViewSQL.prefHeightProperty().unbind();
			this.lowerPane.getChildren().addAll( labelTitle, txtMsg, this.tableViewSQL );
		}
		else if ( ex instanceof SQLException )
		{
			this.showSQLException( (SQLException)ex, this.dbView.getMyDBAbstract() );
		}
		else if ( ex instanceof Exception )
		{
			Label     labelTitle = new Label( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
			
			String    strMsg     = ex.getMessage();
			TextArea  txtMsg     = new TextArea( strMsg );
			txtMsg.setPrefColumnCount( MyTool.getCharCount( strMsg, "\n" )+1 );
			txtMsg.setEditable( false );
			
			String    strExp     = MyTool.getExceptionString( ex );
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
		long startTime = System.nanoTime();
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
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
	}
	
	private void showSQLException( SQLException sqlEx, MyDBAbstract myDBAbs )
	{
		System.out.println( "SQLState:" + sqlEx.getSQLState() );
		System.out.println( "ErrorCode:" + sqlEx.getErrorCode() );
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		
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
		splitPane.getItems().addAll( vBoxExp , this.tableViewSQL );
		splitPane.setDividerPositions( 0.3f, 0.7f );
		
		this.tableViewSQL.prefHeightProperty().unbind();
		this.lowerPane.getChildren().add( splitPane );
	}
	
	/**************************************************
	 * Override from CopyInterface
	 ************************************************** 
	 */
	@Override
	public void copyTableNoHead()
	{
		this.tableViewSQL.copyTableNoHead();
	}
	
	/**
	 * Override from CopyInterface
	 */
	@Override
	public void copyTableWithHead()
	{
		this.tableViewSQL.copyTableWithHead();
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.tableViewSQL.changeLang();
	}
	
}
