package milu.task.query;

import java.util.List;

import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.gui.ctrl.common.inf.ProcInterface;

interface ExecTaskInterface 
{
	void setDBView( DBView dbView );
	void setMyDBAbstract( MyDBAbstract myDBAbs );
	void setAppConf( AppConf appConf );
	void setTabPane( TabPane tabPane );
	void setSQLBagLst( List<SQLBag> sqlBagLst );
	void setProcInf( ProcInterface procInf );
}
