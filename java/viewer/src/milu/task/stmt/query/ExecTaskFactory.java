package milu.task.stmt.query;

import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TabPane;
import javafx.geometry.Orientation;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public class ExecTaskFactory 
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
		Orientation   orientation
	)
	{
		Task<Exception> task = null;
		
		if ( FACTORY_TYPE.SCRIPT.equals(factoryType) )
		{
			task = new ExecScriptAllTask();
		}
		else if ( FACTORY_TYPE.EXPLAIN.equals(factoryType) )
		{
			task = new ExecExplainAllTask();
		}
		else
		{
			return null;
		}
		
		((ExecTaskInterface)task).setDBView(dbView);
		((ExecTaskInterface)task).setMyDBAbstract(myDBAbs);
		((ExecTaskInterface)task).setAppConf(appConf);
		((ExecTaskInterface)task).setTabPane(tabPane);
		((ExecTaskInterface)task).setSQLBagLst(sqlBagLst);
		((ExecTaskInterface)task).setProcInf(procInf);
		((ExecTaskInterface)task).setOrientation(orientation);
		
		return task;
	}
}
