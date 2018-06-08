package milu.task.query;

import java.util.List;
import javafx.concurrent.Task;

import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.gui.ctrl.common.inf.ProcInterface;

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
		ProcInterface procInf
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
		
		return task;
	}
}
