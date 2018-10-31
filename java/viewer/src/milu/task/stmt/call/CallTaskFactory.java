package milu.task.stmt.call;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public class CallTaskFactory 
{
	public enum FACTORY_TYPE
	{
		SCRIPT,
		EXPLAIN
	}
	
	public static Task<Exception> createFactory
	(
		FACTORY_TYPE  factoryType,
		DBView        dbView,
		MyDBAbstract  myDBAbs,
		AppConf       appConf,
		TabPane       tabPane,
		List<SQLBag>  sqlBagLst,
		ProcInterface procInf,
		Orientation   orientation,
		ObservableList<CallObj> placeHolderParamLst,
		List<List<Object>>  placeHolderInLst
	)
	{
		Task<Exception> task = null;
		
		if ( FACTORY_TYPE.SCRIPT.equals(factoryType) )
		{
			task = new CallScriptAllTask();
		}
		else
		{
			return null;
		}
		
		((CallTaskInterface)task).setDBView(dbView);
		((CallTaskInterface)task).setMyDBAbstract(myDBAbs);
		((CallTaskInterface)task).setAppConf(appConf);
		((CallTaskInterface)task).setTabPane(tabPane);
		((CallTaskInterface)task).setSQLBagLst(sqlBagLst);
		((CallTaskInterface)task).setProcInf(procInf);
		((CallTaskInterface)task).setOrientation(orientation);
		((CallTaskInterface)task).setPlaceHolderParamLst(placeHolderParamLst);
		((CallTaskInterface)task).setPlaceHolderInLst(placeHolderInLst);
		
		return task;
	}
}
