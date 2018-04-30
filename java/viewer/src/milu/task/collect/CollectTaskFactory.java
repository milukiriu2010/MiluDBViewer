package milu.task.collect;

import javafx.concurrent.Task;

import milu.main.MainController;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;

public class CollectTaskFactory
{
	public enum TYPE
	{
		BASIC,
		SQLITE
	}
	
	public static Task<Exception> getInstance( MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		Task<Exception> task = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			task = new CollectTaskBasic();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			task = new CollectTaskBasic();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			task = new CollectTaskBasic();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			task = new CollectTaskBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			task = new CollectTaskBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			task = new CollectTaskNoSchema();
		}
		else
		{
			return null;
		}
		
		if ( task instanceof TaskInterface )
		{
			((TaskInterface)task).setMainController(mainCtrl);
			((TaskInterface)task).setMyDBAbstract(myDBAbs);
		}
		
		return task;
	}
}
