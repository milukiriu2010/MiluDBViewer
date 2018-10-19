package milu.gui.ctrl.prepare;

import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLExplainFactory;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.ctrl.query.SQLExecInterface;
import milu.gui.ctrl.query.SQLExecWithoutParseInterface;
import milu.gui.ctrl.query.SQLExplainInterface;
import milu.gui.ctrl.query.SQLFetchInterface;
import milu.gui.ctrl.query.SQLFileInterface;
import milu.gui.ctrl.query.SQLFormatInterface;
import milu.gui.ctrl.query.SQLHistoryInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.ProcInterface;
import milu.tool.MyGUITool;
import milu.tool.TextUtils;

public class PrepareSQLToolBar extends ToolBar
	implements 
		ActionInterface,
		ProcInterface,
		SQLFetchInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	// Exec SQL
	private Button    btnExecSQL = new Button();
	// Explain SQL
	private Button    btnExplainSQL = new Button();
	// Exec Query SQL
	private Button    btnExecSQLQuery = new Button();
	// Exec Transaction SQL
	private Button    btnExecSQLTrans = new Button();
	// Exec Transaction SQL(every ";")
	private Button    btnExecSQLTransSemi = new Button();
	// Toggle Horizontal/Vertical mode
	private Button    btnToggleHV = new Button();
	// Button to copy table data(no column)
	private Button    btnCopyTblNoHead = new Button();
	// Button to copy table data(with column)
	private Button    btnCopyTblWithHead = new Button();
	// Format SQL
	private Button    btnFmtSQL = new Button();
	// One Line SQL
	private Button    btnOneLineSQL = new Button();
	// Previous SQL
	private Button    btnPrevSQL = new Button();
	// Next SQL
	private Button    btnNextSQL = new Button();
	// Open SQL
	private Button    btnOpenSQL = new Button();
	// Save SQL
	private Button    btnSaveSQL = new Button();
	// Absolute Position when selecting
	private TextField txtFetchPos = new TextField();
	// Max fetch rows when selecting
	private TextField txtFetchMax = new TextField();

	PrepareSQLToolBar( DBView dbView )
	{
		this.dbView = dbView;
		
		MainController mainCtrl = this.dbView.getMainController();
		this.btnExecSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/execsql.png") ) );
		this.btnExplainSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/explain.png") ) );
		this.btnExecSQLQuery.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/execQUERY.png") ) );
		this.btnExecSQLTrans.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/execTRANS.png") ) );
		this.btnExecSQLTransSemi.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/execTRANS_SEMI.png") ) );
		this.btnToggleHV.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/direction.png") ) );
		this.btnCopyTblNoHead.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/copy.png") ) );
		this.btnCopyTblWithHead.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/copy2.png") ) );
		this.btnFmtSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/sql_format.png") ) );
		this.btnOneLineSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/sql_oneline.png") ) );
		this.btnPrevSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/back.png") ) );
		this.btnNextSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/next.png") ) );
		this.btnOpenSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/folder.png") ) );
		this.btnSaveSQL.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/save.png") ) );
		
		AppConf appConf = mainCtrl.getAppConf();
		this.txtFetchPos.setText(String.valueOf(appConf.getFetchPos()));
		
		this.txtFetchMax.setText(String.valueOf(appConf.getFetchMax()));
		
		
		this.getItems().addAll(	this.btnExecSQL	);
		
		ExecSQLAbstract execSQLAbs = new ExecSQLExplainFactory().createFactory( null, this.dbView.getMyDBAbstract(), mainCtrl.getAppConf(), null, 0.0 );
		if ( execSQLAbs != null )
		{
			this.getItems().add( this.btnExplainSQL );
		}
		
		this.getItems().addAll
		(
			this.btnExecSQLQuery,
			this.btnExecSQLTrans,
			this.btnExecSQLTransSemi,
			new Separator(),
			this.btnToggleHV,
			this.btnCopyTblNoHead,
			this.btnCopyTblWithHead,
			new Separator(),
			this.btnPrevSQL,
			this.btnNextSQL,
			new Separator(),
			this.btnFmtSQL,
			this.btnOneLineSQL,
			new Separator(),
			this.btnOpenSQL,
			this.btnSaveSQL,
			new Separator(),
			this.txtFetchPos,
			this.txtFetchMax
		);
		
		this.setAction();
		
		this.changeLang();
	}
	
	
	private void setAction()
	{
		this.setTextWidth(this.txtFetchPos);
		this.txtFetchPos.textProperty().addListener((obs,oldVal,newVal)->{
			this.checkText( obs, oldVal, newVal );
			this.setTextWidth( this.txtFetchPos );
		});
		
		this.setTextWidth(this.txtFetchMax);
		this.txtFetchMax.textProperty().addListener((obs,oldVal,newVal)->{
			this.checkText( obs, oldVal, newVal );
			this.setTextWidth( this.txtFetchMax );
		});
	}
	
	private void checkText( ObservableValue<? extends String> obs, String oldVal, String newVal )
	{
		// "Numeric" or "No Input" are allowed.
		if ( newVal.length() == 0 )
		{
			
		}
		// if alphabets or marks are input, back to previous input.
		else if ( newVal.matches( "^[0-9]+$" ) == false )
		{
			((StringProperty)obs).setValue( oldVal );
		}
	}
	
	private void setTextWidth( TextField txtField )
	{
		txtField.setPrefWidth( TextUtils.computeTextWidth( txtField.getFont(), txtField.getText(), 0.0D) + 20 );
	}
	
	// ActionInterface
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
		
		if ( obj instanceof SQLExecWithoutParseInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnExecSQLQuery.setOnAction( ((SQLExecWithoutParseInterface)obj)::execSQLQuery );
			
			// mouse click, "space/return" key enter 
			this.btnExecSQLTrans.setOnAction( ((SQLExecWithoutParseInterface)obj)::execSQLTrans );
			
			// mouse click, "space/return" key enter 
			this.btnExecSQLTransSemi.setOnAction( ((SQLExecWithoutParseInterface)obj)::execSQLTransSemi );
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
			
			// mouse click, "space/return" key enter 
			this.btnOneLineSQL.setOnAction( ((SQLFormatInterface)obj)::oneLineSQL );
		}
		
		if ( obj instanceof SQLHistoryInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnPrevSQL.setOnAction( ((SQLHistoryInterface)obj)::prevSQL );
			
			// mouse click, "space/return" key enter 
			this.btnNextSQL.setOnAction( ((SQLHistoryInterface)obj)::nextSQL );
		}
		
		if ( obj instanceof SQLFileInterface )
		{
			// mouse click, "space/return" key enter 
			this.btnOpenSQL.setOnAction( ((SQLFileInterface)obj)::openSQL );
			
			// mouse click, "space/return" key enter 
			this.btnSaveSQL.setOnAction( ((SQLFileInterface)obj)::saveSQL );
		}
	}
	
	// SQLFetchInterface
	@Override
	public Integer getFetchPos()
	{
		try
		{
			return Integer.valueOf(this.txtFetchPos.getText());
		}
		catch ( NumberFormatException nfEx )
		{
			MainController mainCtrl = this.dbView.getMainController();
			AppConf appConf = mainCtrl.getAppConf();
			return appConf.getFetchPos();
		}
	}
	
	// SQLFetchInterface
	@Override
	public Integer getFetchMax()
	{
		try
		{
			return Integer.valueOf(this.txtFetchMax.getText());
		}
		catch ( NumberFormatException nfEx )
		{
			MainController mainCtrl = this.dbView.getMainController();
			AppConf appConf = mainCtrl.getAppConf();
			return appConf.getFetchMax();
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
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Query] 
		// ----------------------------------------------
		Tooltip tipExecSQL = new Tooltip( langRB.getString( "TOOLTIP_EXEC_SQL" ));
		tipExecSQL.getStyleClass().add("Common_MyToolTip");
		this.btnExecSQL.setTooltip(tipExecSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Explain] 
		// ----------------------------------------------
		Tooltip tipExplainSQL = new Tooltip( langRB.getString( "TOOLTIP_EXEC_EXPLAIN" ) );
		tipExplainSQL.getStyleClass().add("Common_MyToolTip");
		this.btnExplainSQL.setTooltip( tipExplainSQL );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Query-SQL without parse] 
		// ----------------------------------------------
		Tooltip tipExecSQLQuery = new Tooltip( langRB.getString( "TOOLTIP_EXEC_SQL_QUERY" ));
		tipExecSQLQuery.getStyleClass().add("Common_MyToolTip");
		this.btnExecSQLQuery.setTooltip(tipExecSQLQuery);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Transaction-SQL without parse] 
		// ----------------------------------------------
		Tooltip tipExecSQLTrans = new Tooltip( langRB.getString( "TOOLTIP_EXEC_SQL_TRANS" ));
		tipExecSQLTrans.getStyleClass().add("Common_MyToolTip");
		this.btnExecSQLTrans.setTooltip(tipExecSQLTrans);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Transaction-SQL(every ;) without parse] 
		// ----------------------------------------------
		Tooltip tipExecSQLTransSemi = new Tooltip( langRB.getString( "TOOLTIP_EXEC_SQL_TRANS_SEMI" ));
		tipExecSQLTransSemi.getStyleClass().add("Common_MyToolTip");
		this.btnExecSQLTransSemi.setTooltip(tipExecSQLTransSemi);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Toggle Horizontal/Vertical mode] 
		// ----------------------------------------------
		Tooltip tipToggleHV = new Tooltip( langRB.getString( "TOOLTIP_TOGGLE_HV" ));
		tipToggleHV.getStyleClass().add("Common_MyToolTip");
		this.btnToggleHV.setTooltip( tipToggleHV );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data without column] 
		// ----------------------------------------------
		Tooltip tipCopyTblNoHead = new Tooltip( langRB.getString( "TOOLTIP_COPY_TABLE_NO_HEAD" ));
		tipCopyTblNoHead.getStyleClass().add("Common_MyToolTip");
		this.btnCopyTblNoHead.setTooltip( tipCopyTblNoHead );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data with column] 
		// ----------------------------------------------
		Tooltip tipCopyTblWithHead = new Tooltip( langRB.getString( "TOOLTIP_COPY_TABLE_WITH_HEAD" ));
		tipCopyTblWithHead.getStyleClass().add("Common_MyToolTip");
		this.btnCopyTblWithHead.setTooltip( tipCopyTblWithHead );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Format SQL] 
		// ----------------------------------------------
		Tooltip tipFmtSQL = new Tooltip( langRB.getString( "TOOLTIP_FORMAT_SQL" ));
		tipFmtSQL.getStyleClass().add("Common_MyToolTip");
		this.btnFmtSQL.setTooltip(tipFmtSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[1Line SQL] 
		// ----------------------------------------------
		Tooltip tipOneLineSQL = new Tooltip( langRB.getString( "TOOLTIP_ONELINE_SQL" ));
		tipOneLineSQL.getStyleClass().add("Common_MyToolTip");
		this.btnOneLineSQL.setTooltip(tipOneLineSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Prev SQL] 
		// ----------------------------------------------
		Tooltip tipPrevSQL = new Tooltip( langRB.getString( "TOOLTIP_PREV_SQL" ));
		tipPrevSQL.getStyleClass().add("Common_MyToolTip");
		this.btnPrevSQL.setTooltip(tipPrevSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Next SQL] 
		// ----------------------------------------------
		Tooltip tipNextSQL = new Tooltip( langRB.getString( "TOOLTIP_NEXT_SQL" ));
		tipNextSQL.getStyleClass().add("Common_MyToolTip");
		this.btnNextSQL.setTooltip(tipNextSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Open SQL] 
		// ----------------------------------------------
		Tooltip tipOpenSQL = new Tooltip( langRB.getString( "TOOLTIP_OPEN_SQL" ));
		tipOpenSQL.getStyleClass().add("Common_MyToolTip");
		this.btnOpenSQL.setTooltip(tipOpenSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Save SQL] 
		// ----------------------------------------------
		Tooltip tipSaveSQL = new Tooltip( langRB.getString( "TOOLTIP_SAVE_SQL" ));
		tipSaveSQL.getStyleClass().add("Common_MyToolTip");
		this.btnSaveSQL.setTooltip(tipSaveSQL);
		
		// ----------------------------------------------
		// ToolTip
		//   TextField[Fetch Pos] 
		// ----------------------------------------------
		Tooltip tipFetchPos = new Tooltip( extLangRB.getString( "TOOLTIP_FETCH_POS" ));
		tipFetchPos.getStyleClass().add("Common_MyToolTip");
		this.txtFetchPos.setTooltip(tipFetchPos);
		
		// ----------------------------------------------
		// ToolTip
		//   TextField[Fetch Max] 
		// ----------------------------------------------
		Tooltip tipFetchMax = new Tooltip( extLangRB.getString( "TOOLTIP_FETCH_MAX" ));
		tipFetchMax.getStyleClass().add("Common_MyToolTip");
		this.txtFetchMax.setTooltip(tipFetchMax);
	}		
}
