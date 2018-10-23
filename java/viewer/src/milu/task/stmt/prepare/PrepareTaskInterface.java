package milu.task.stmt.prepare;

import java.util.List;

import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public interface PrepareTaskInterface 
{
	public void setDBView( DBView dbView );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	public void setAppConf( AppConf appConf );
	public void setTabPane( TabPane tabPane );
	public void setSQLBagLst( List<SQLBag> sqlBagLst );
	public void setProcInf( ProcInterface procInf );
	public void setOrientation( Orientation orientation );
	public void setPlaceHolderLst( List<List<Object>> placeHolderLst );
}
