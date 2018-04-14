package milu.gui.ctrl.menu;

import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLExplainFactory;

public class MainToolBar extends ToolBar
	implements ChangeLangInterface
{
	// DBView
	private DBView  dbView = null;
	
	// Button to get data
	private Button btnGo       = new Button();
	// Button to get explain
	private Button btnExplain  = new Button();
	// Button to toggle Horizontal/Vertical mode
	private Button btnToggleHV = new Button();
	// Button to commit
	private Button btnCommit   = new Button();
	// Button to rollback
	private Button btnRollback = new Button();
	// Button to add new tab
	private Button btnNewTab   = new Button();
	// Button to add new window
	private Button btnNewWin   = new Button();
	// Button to add new DB connection
	private Button btnNewCon   = new Button();
	// Button to copy table data(no column)
	private Button btnCopyTblNoHead   = new Button();
	// Button to copy table data(with column)
	private Button btnCopyTblWithHead = new Button();
	// Button  to Open Schema View
	private Button btnSchema   = new Button();
	
	public MainToolBar( DBView dbView )
	{
		// DBView
		this.dbView = dbView;
		
		// set Menu for this ToolBar
		this.setMenu();
		
		// set Action for this ToolBar
		this.setAction();
		
		// set text on Menu
		this.changeLang();
	}
	
	private void setMenu()
	{
		MainController mainCtrl = this.dbView.getMainController();
		
		// Button to get data
		this.btnGo.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/execsql.png") ) );
		
		// Button to get explain
		this.btnExplain.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/explain.png") ) );
		
		// Button to toggle Horizontal/Vertical mode
		this.btnToggleHV.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/direction.png") ) );
		
		// Button to commit
		this.btnCommit.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/commit.png") ) );
		
		// Button to rollback
		this.btnRollback.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/rollback.png") ) );
		
		// Button to add a new tab
		this.btnNewTab.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/newtab.png") ) );
		
		// Button to add a new window
		this.btnNewWin.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/newwin.png") ) );
		
		// Button for new DB connection
		this.btnNewCon.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/connect.png") ) );
		
		// Button to copy table data(no column)
		this.btnCopyTblNoHead.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/copy.png") ) );
		
		// Button to copy table data(with column)
		this.btnCopyTblWithHead.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/copy2.png") ) );
		
		// Button to Open Schema View
		this.btnSchema.setGraphic( MyTool.createImageView( 24, 24, mainCtrl.getImage("file:resources/images/schema.png") ) );
		
		// put buttons on this ToolBar
		this.getItems().addAll
		(
			new Separator(),
			this.btnGo
		);
		
		ExecSQLAbstract execSQLAbs = new ExecSQLExplainFactory().createFactory( null, this.dbView.getMyDBAbstract(), mainCtrl.getAppConf() );
		if ( execSQLAbs != null )
		{
			this.getItems().add( this.btnExplain );
		}
		
		this.getItems().addAll
		(
			this.btnToggleHV,
			this.btnCopyTblNoHead,
			this.btnCopyTblWithHead,
			this.btnCommit,
			this.btnRollback,
			new Separator(),
			this.btnNewTab,
			this.btnNewWin,
			this.btnNewCon,
			new Separator(),
			this.btnSchema,
			new Separator()
		);
	}
	
	private void setAction()
	{
		// "Go!!" button clicked
		// http://code.makery.ch/blog/javafx-8-event-handling-examples/		
		this.btnGo.setOnAction(	(event)->{ this.dbView.Go(); } );
		
		// Explain button clicked
		this.btnExplain.setOnAction( event->this.dbView.execExplain() );
		
		// Commit button clicked
		this.btnCommit.setOnAction(	(event)->this.dbView.commit() );
		
		// Rollback button clicked
		this.btnRollback.setOnAction(	(event)->this.dbView.rollback()	);
		
		// "Toggle H/V" button clicked
		this.btnToggleHV.setOnAction( (event)->{ this.dbView.switchDirection();	} );
		
		// "New Tab" button clicked
		this.btnNewTab.setOnAction(	(event)->{ this.dbView.createNewTab(); } );
		
		// "New Window" button clicked
		this.btnNewWin.setOnAction(	(event)->{ this.dbView.createNewWindow(); } );
		
		// "New DB Connection" button clicked
		this.btnNewCon.setOnAction( (event)->{ this.dbView.createNewDBConnection(); } );
		
		// "Copy data on TableView without column heads" button clicked
		this.btnCopyTblNoHead.setOnAction( (event)->{ this.dbView.copyTableNoHead(); } );
		
		// "Copy data on TableView with column heads" button clicked
		this.btnCopyTblWithHead.setOnAction( (event)->{ this.dbView.copyTableWithHead(); } );
		
		// "Open Schema View" button clicked
		this.btnSchema.setOnAction( (event)->{ this.dbView.openSchemaView(); } );
		// disable until finishing to get schema list.
		this.btnSchema.setDisable(true);
	}
	
	public void setMnemonic()
	{
		// ---------------------------------
		// Mnemonic for "Exec SQL"
		// Ctrl+G
		// ---------------------------------
		this.btnGo.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.G, KeyCombination.CONTROL_DOWN ),
			// Runnable.run()
			dbView::Go
		);
		
		// ---------------------------------
		// Mnemonic for "Toggle horizontal/vertical mode for Table"
		// Ctrl+D
		// ---------------------------------
		this.btnToggleHV.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.D, KeyCombination.CONTROL_DOWN ),	
			dbView::switchDirection
		);
		
		// ---------------------------------
		// Mnemonic for "Open new tab"
		// Ctrl+T
		// ---------------------------------
		this.btnNewTab.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.T, KeyCombination.CONTROL_DOWN ),	
			dbView::createNewTab
		);
		/*
		this.btnNewTab.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN ),	
			dbView::createNewTab
		);
		*/
		
		// ---------------------------------
		// Mnemonic for "Open new window"
		// Ctrl+N
		// ---------------------------------
		this.btnNewWin.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.N, KeyCombination.CONTROL_DOWN ),	
			dbView::createNewWindow
		);

		// ------------------------------------------------------------
		// Mnemonic for "Copy datas on TableView without column heads"
		// Alt+C
		// ------------------------------------------------------------
		this.btnCopyTblNoHead.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.C, KeyCombination.ALT_DOWN ),	
			dbView::copyTableNoHead
		);

		// ------------------------------------------------------------
		// Mnemonic for "Copy datas on TableView with column heads"
		// Alt+Shift+C
		// ------------------------------------------------------------
		this.btnCopyTblWithHead.getScene().getAccelerators().put
		(
			//new KeyCodeCombination( KeyCode.C, KeyCombination.ALT_DOWN | KeyCombination.SHIFT_DOWN ),	
			KeyCombination.keyCombination( "ALT+SHIFT+C" ),
			dbView::copyTableWithHead
		);
	}
	
	public void taskProcessing()
	{
		this.btnGo.setDisable(true);
		this.btnExplain.setDisable(true);
		this.btnToggleHV.setDisable(true);
	}
	
	public void taskDone()
	{
		this.btnGo.setDisable(false);
		this.btnExplain.setDisable(false);
		this.btnToggleHV.setDisable(false);
		// disable until finishing to get schema list.
		this.btnSchema.setDisable(false);
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.menu.MainToolBar");
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Query] 
		// ----------------------------------------------
		Tooltip tipGo = new Tooltip( langRB.getString( "TIP_EXECSQL" ) );
		tipGo.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnGo.setTooltip( tipGo );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Explain] 
		// ----------------------------------------------
		Tooltip tipExplain = new Tooltip( langRB.getString( "TIP_EXEC_EXPLAIN" ) );
		tipExplain.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnExplain.setTooltip( tipExplain );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Commit] 
		// ----------------------------------------------
		Tooltip tipCommit = new Tooltip( langRB.getString( "TIP_COMMIT" ) );
		tipCommit.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnCommit.setTooltip( tipCommit );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Rollback] 
		// ----------------------------------------------
		Tooltip tipRollback = new Tooltip( langRB.getString( "TIP_ROLLBACK" ) );
		tipRollback.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnRollback.setTooltip( tipRollback );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Toggle Horizontal/Vertical mode] 
		// ----------------------------------------------
		Tooltip tipToggleHV = new Tooltip( langRB.getString( "TIP_TOGGLE_HV" ));
		tipToggleHV.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnToggleHV.setTooltip( tipToggleHV );

		// ----------------------------------------------
		// ToolTip
		//   Button[New Tab] 
		// ----------------------------------------------
		Tooltip tipNewTab = new Tooltip( langRB.getString( "TIP_NEW_TAB" ));
		tipNewTab.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnNewTab.setTooltip( tipNewTab );

		// ----------------------------------------------
		// ToolTip
		//   Button[New Window] 
		// ----------------------------------------------
		Tooltip tipNewWin = new Tooltip( langRB.getString( "TIP_NEW_WIN" ));
		tipNewWin.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnNewWin.setTooltip( tipNewWin );

		// ----------------------------------------------
		// ToolTip
		//   Button[New DB Connection] 
		// ----------------------------------------------
		Tooltip tipNewCon = new Tooltip( langRB.getString( "TIP_NEW_CON" ));
		tipNewCon.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnNewCon.setTooltip( tipNewCon );

		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data without column] 
		// ----------------------------------------------
		Tooltip tipCopyTblNoHead = new Tooltip( langRB.getString( "TIP_COPY_TABLE_NO_HEAD" ));
		tipCopyTblNoHead.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnCopyTblNoHead.setTooltip( tipCopyTblNoHead );

		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data with column] 
		// ----------------------------------------------
		Tooltip tipCopyTblWithHead = new Tooltip( langRB.getString( "TIP_COPY_TABLE_WITH_HEAD" ));
		tipCopyTblWithHead.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnCopyTblWithHead.setTooltip( tipCopyTblWithHead );

		// ----------------------------------------------
		// ToolTip
		//   Button[Open Schema View] 
		// ----------------------------------------------
		Tooltip tipSchema = new Tooltip( langRB.getString( "TIP_SCHEMA" ));
		tipSchema.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnSchema.setTooltip( tipSchema );
	}
	
}
