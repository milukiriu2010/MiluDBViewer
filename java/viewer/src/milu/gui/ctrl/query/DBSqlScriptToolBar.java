package milu.gui.ctrl.query;

import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

import java.util.ResourceBundle;

import javafx.scene.control.Button;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

class DBSqlScriptToolBar extends ToolBar
	implements ChangeLangInterface
{
	private DBView          dbView = null;
	
	private Button          btnFmtSQL = new Button();
	
	DBSqlScriptToolBar( DBView dbView, SQLFormatInterface sqlFmtInterface )
	{
		this.dbView = dbView;
		
		MainController mainCtrl = this.dbView.getMainController();
		this.btnFmtSQL.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/sql_format.png") ) );
		
		this.getItems().add( btnFmtSQL );
		
		this.btnFmtSQL.setOnAction(	(event)->sqlFmtInterface.formatSQL() );
		
		this.changeLang();
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
		
		Tooltip tipFmtSQL = new Tooltip( langRB.getString( "LABEL_FORMAT_SQL" ));
		tipFmtSQL.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnFmtSQL.setTooltip(tipFmtSQL);
	}	
}
