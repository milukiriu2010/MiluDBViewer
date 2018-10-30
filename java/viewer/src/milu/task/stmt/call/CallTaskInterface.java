package milu.task.stmt.call;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.stmt.call.CallObj;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public interface CallTaskInterface 
{
	public void setDBView( DBView dbView );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	public void setAppConf( AppConf appConf );
	public void setTabPane( TabPane tabPane );
	public void setSQLBagLst( List<SQLBag> sqlBagLst );
	public void setProcInf( ProcInterface procInf );
	public void setOrientation( Orientation orientation );
	public void setPlaceHolderParamLst( ObservableList<CallObj> placeHolderParamLst );
	public void setPlaceHolderInLst( List<List<Object>> placeHolderInLst );
}
