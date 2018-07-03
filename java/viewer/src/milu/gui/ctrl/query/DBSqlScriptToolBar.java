package milu.gui.ctrl.query;

import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLExplainFactory;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.ProcInterface;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyGUITool;

class DBSqlScriptToolBar extends ToolBar
	implements 
		ActionInterface,
		ProcInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	// Exec SQL
	private Button    btnExecSQL = new Button();
	// Explain SQL
	private Button    btnExplainSQL = new Button();
	// Toggle Horizontal/Vertical mode
	private Button    btnToggleHV = new Button();
	// Button to copy table data(no column)
	private Button    btnCopyTblNoHead = new Button();
	// Button to copy table data(with column)
	private Button    btnCopyTblWithHead = new Button();
	// Format SQL
	private Button    btnFmtSQL = new Button();
	
	DBSqlScriptToolBar( DBView dbView )
	{
		this.dbView = dbView;
		
		MainController mainCtrl = this.dbView.getMainController();
		this.btnExecSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/execsql.png") ) );
		this.btnExplainSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/explain.png") ) );
		this.btnToggleHV.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/direction.png") ) );
		this.btnCopyTblNoHead.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/copy.png") ) );
		this.btnCopyTblWithHead.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/copy2.png") ) );
		this.btnFmtSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/sql_format.png") ) );
		
		this.getItems().addAll(	this.btnExecSQL	);
		
		ExecSQLAbstract execSQLAbs = new ExecSQLExplainFactory().createFactory( null, this.dbView.getMyDBAbstract(), mainCtrl.getAppConf() );
		if ( execSQLAbs != null )
		{
			this.getItems().add( this.btnExplainSQL );
		}
		
		this.getItems().addAll
		(
			new Separator(),
			this.btnToggleHV,
			this.btnCopyTblNoHead,
			this.btnCopyTblWithHead,
			new Separator(),
			this.btnFmtSQL 
		);
		
		this.changeLang();
	}
	
	@Override
	public void setAction( Object obj )
	{
		if ( obj instanceof SQLExecInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnExecSQL.setOnAction( ((SQLExecInterface)obj)::execSQL );
		}
		
		if ( obj instanceof SQLExplainInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnExplainSQL.setOnAction( ((SQLExplainInterface)obj)::explainSQL );
		}
		
		if ( obj instanceof DirectionSwitchInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnToggleHV.setOnAction( ((DirectionSwitchInterface)obj)::switchDirection );
		}

		if ( obj instanceof CopyTableInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnCopyTblNoHead.setOnAction( ((CopyTableInterface)obj)::copyTableNoHead );
			
			// mouse click, "space/return" key enter 
			this.btnCopyTblWithHead.setOnAction( ((CopyTableInterface)obj)::copyTableWithHead );
		}
		
		if ( obj instanceof SQLFormatInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnFmtSQL.setOnAction(	((SQLFormatInterface)obj)::formatSQL );
			// any key
			this.btnFmtSQL.setOnKeyPressed( ((SQLFormatInterface)obj)::formatSQL );
		}
	}
	
	// ProcBeginInterface
	@Override
	public void beginProc()
	{
		this.btnExecSQL.setDisable(true);
		this.btnExplainSQL.setDisable(true);
		this.btnToggleHV.setDisable(true);
	}
	
	// ProcEndInterface
	@Override
	public void endProc()
	{
		this.btnExecSQL.setDisable(false);
		this.btnExplainSQL.setDisable(false);
		this.btnToggleHV.setDisable(false);
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.query.DBSqlTab");
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Query] 
		// ----------------------------------------------
		Tooltip tipExecSQL = new Tooltip( langRB.getString( "TIP_EXEC_SQL" ));
		tipExecSQL.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnExecSQL.setTooltip(tipExecSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Explain] 
		// ----------------------------------------------
		Tooltip tipExplainSQL = new Tooltip( langRB.getString( "TIP_EXEC_EXPLAIN" ) );
		tipExplainSQL.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnExplainSQL.setTooltip( tipExplainSQL );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Toggle Horizontal/Vertical mode] 
		// ----------------------------------------------
		Tooltip tipToggleHV = new Tooltip( langRB.getString( "TIP_TOGGLE_HV" ));
		tipToggleHV.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnToggleHV.setTooltip( tipToggleHV );
		
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
		
		Tooltip tipFmtSQL = new Tooltip( langRB.getString( "TIP_FORMAT_SQL" ));
		tipFmtSQL.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnFmtSQL.setTooltip(tipFmtSQL);
	}	
}
