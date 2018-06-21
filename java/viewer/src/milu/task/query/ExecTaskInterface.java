package milu.task.query;

import java.util.List;

import javafx.scene.control.TabPane;
import javafx.geometry.Orientation;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.gui.ctrl.common.inf.ProcInterface;

public interface ExecTaskInterface 
{
	public void setDBView( DBView dbView );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	public void setAppConf( AppConf appConf );
	public void setTabPane( TabPane tabPane );
	public void setSQLBagLst( List<SQLBag> sqlBagLst );
	public void setProcInf( ProcInterface procInf );
	public void setOrientation( Orientation orientation );
}
