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
import milu.gui.ctrl.schema.DBSchemaTab;
import milu.gui.stmt.call.CallSQLTab;
import milu.gui.stmt.prepare.PrepareSQLTab;
import milu.gui.view.DBView;
import milu.gui.view.FadeView;
import milu.main.MainController;
import milu.task.ProcInterface;
import milu.tool.MyGUITool;

public class MainToolBar extends ToolBar
	implements
		ProcInterface,
		ChangeLangInterface
{
	// DBView
	private DBView  dbView = null;
	
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
	// Button for "PreparedStatement"
	private Button btnPrepare  = new Button();
	// Button for "CallableStatement"
	private Button btnCall     = new Button();
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
		
		// Button to commit
		this.btnCommit.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/commit.png") ) );
		
		// Button to rollback
		this.btnRollback.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/rollback.png") ) );
		
		// Button to add a new tab
		this.btnNewTab.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/newtab.png") ) );
		
		// Button to add a new window
		this.btnNewWin.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/newwin.png") ) );
		
		// Button for new DB connection
		this.btnNewCon.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/connect.png") ) );
		
		// Button for "PreparedStatement"
		this.btnPrepare.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/prepare.png") ) );
		
		// Button for "CallableStatement"
		this.btnCall.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/call.png") ) );
		
		// Button to Open Schema View
		this.btnSchema.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/schema.png") ) );
		
		this.getItems().addAll
		(
			this.btnCommit,
			this.btnRollback,
			new Separator(),
			this.btnNewTab,
			this.btnNewWin,
			this.btnNewCon,
			this.btnPrepare,
			this.btnCall,
			new Separator(),
			this.btnSchema,
			new Separator()
		);
	}
	
	private void setAction()
	{
		// Commit button clicked
		this.btnCommit.setOnAction((event)->{
			this.dbView.commit();
			new FadeView( "Commit" );
		});
		
		// Rollback button clicked
		this.btnRollback.setOnAction((event)->{
			this.dbView.rollback();
			new FadeView( "Rollback" );
		});
		
		// "New Tab" button clicked
		this.btnNewTab.setOnAction(	this.dbView::createNewTab );
		
		// "New Window" button clicked
		this.btnNewWin.setOnAction(	this.dbView::createNewWindow );
		
		// "New DB Connection" button clicked
		this.btnNewCon.setOnAction( this.dbView::createNewDBConnection );
		
		// Button for "PreparedStatement"
		this.btnPrepare.setOnAction( (event)->{ this.dbView.openView(PrepareSQLTab.class,false); } );
		
		// Button for "CallableStatement"
		this.btnCall.setOnAction( (event)->{ this.dbView.openView(CallSQLTab.class,false); } );
		
		// "Open Schema View" button clicked
		this.btnSchema.setOnAction( (event)->{ this.dbView.openView(DBSchemaTab.class,true); } );
		// disable until finishing to get schema list.
		this.btnSchema.setDisable(true);
	}
	
	public void setMnemonic()
	{
		// ---------------------------------
		// Mnemonic for "Open new tab"
		// Ctrl+T
		// ---------------------------------
		this.btnNewTab.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.T, KeyCombination.CONTROL_DOWN ),	
			()->{ dbView.createNewTab(null); }
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
			()->{ dbView.createNewWindow(null); }
		);
	}
	
	// ProcBeginInterface
	@Override
	public void beginProc()
	{
		this.btnSchema.setDisable(true);
	}
	
	// ProcBeginInterface
	@Override
	public void endProc()
	{
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
		ResourceBundle cmnLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Commit] 
		// ----------------------------------------------
		Tooltip tipCommit = new Tooltip( cmnLangRB.getString( "TOOLTIP_COMMIT" ) );
		tipCommit.getStyleClass().add("Common_MyToolTip");
		this.btnCommit.setTooltip( tipCommit );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Rollback] 
		// ----------------------------------------------
		Tooltip tipRollback = new Tooltip( cmnLangRB.getString( "TOOLTIP_ROLLBACK" ) );
		tipRollback.getStyleClass().add("Common_MyToolTip");
		this.btnRollback.setTooltip( tipRollback );
		// ----------------------------------------------
		// ToolTip
		//   Button[New Tab] 
		// ----------------------------------------------
		Tooltip tipNewTab = new Tooltip( langRB.getString( "TOOLTIP_NEW_TAB" ));
		tipNewTab.getStyleClass().add("Common_MyToolTip");
		this.btnNewTab.setTooltip( tipNewTab );

		// ----------------------------------------------
		// ToolTip
		//   Button[New Window] 
		// ----------------------------------------------
		Tooltip tipNewWin = new Tooltip( langRB.getString( "TOOLTIP_NEW_WIN" ));
		tipNewWin.getStyleClass().add("Common_MyToolTip");
		this.btnNewWin.setTooltip( tipNewWin );

		// ----------------------------------------------
		// ToolTip
		//   Button[New DB Connection] 
		// ----------------------------------------------
		Tooltip tipNewCon = new Tooltip( langRB.getString( "TOOLTIP_NEW_CON" ));
		tipNewCon.getStyleClass().add("Common_MyToolTip");
		this.btnNewCon.setTooltip( tipNewCon );
		
		// ----------------------------------------------
		// ToolTip
		// Button for "PreparedStatement"
		// ----------------------------------------------
		Tooltip tipPrepare = new Tooltip( langRB.getString( "TOOLTIP_PREPARE" ));
		tipPrepare.getStyleClass().add("Common_MyToolTip");
		this.btnPrepare.setTooltip( tipPrepare );
		
		// ----------------------------------------------
		// ToolTip
		// Button for "CallableStatement"
		// ----------------------------------------------
		Tooltip tipCall = new Tooltip( langRB.getString( "TOOLTIP_CALL" ));
		tipCall.getStyleClass().add("Common_MyToolTip");
		this.btnCall.setTooltip( tipCall );

		// ----------------------------------------------
		// ToolTip
		//   Button[Open Schema View] 
		// ----------------------------------------------
		Tooltip tipSchema = new Tooltip( langRB.getString( "TOOLTIP_SCHEMA" ));
		tipSchema.getStyleClass().add("Common_MyToolTip");
		this.btnSchema.setTooltip( tipSchema );
	}
	
}
