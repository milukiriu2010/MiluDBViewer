package milu.task.prepare;

import java.util.List;

import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public class PrepareTaskFactory 
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
		List<List<Object>>  placeHolderLst
	)
	{
		Task<Exception> task = null;
		
		if ( FACTORY_TYPE.SCRIPT.equals(factoryType) )
		{
			task = new PrepareScriptAllTask();
		}
		else
		{
			return null;
		}
		
		((PrepareTaskInterface)task).setDBView(dbView);
		((PrepareTaskInterface)task).setMyDBAbstract(myDBAbs);
		((PrepareTaskInterface)task).setAppConf(appConf);
		((PrepareTaskInterface)task).setTabPane(tabPane);
		((PrepareTaskInterface)task).setSQLBagLst(sqlBagLst);
		((PrepareTaskInterface)task).setProcInf(procInf);
		((PrepareTaskInterface)task).setOrientation(orientation);
		((PrepareTaskInterface)task).setPlaceHolderLst(placeHolderLst);
		
		return task;
	}
}
