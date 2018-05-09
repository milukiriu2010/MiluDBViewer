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
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;

public class CollectTaskFactory
{
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
	
	public static Task<Exception> getInstance( AbsDBFactory.FACTORY_TYPE factoryType, MainController mainCtrl, MyDBAbstract myDBAbs, SchemaEntity selectedSchemaEntity )
	{
		Task<Exception> task = null;
		
		if ( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY.equals(factoryType) )
		{
			task = new CollectTaskForeignKey();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX.equals(factoryType) )
		{
			task = new CollectTaskObjLstByTable();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN.equals(factoryType) )
		{
			task = new CollectTaskObjLstByIndex();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.SEQUENCE.equals(factoryType) )
		{
			task = new CollectTaskObjLstBySchema();
		}
		else
		{
			return null;
		}
		
		if ( task instanceof TaskInterface )
		{
			((TaskInterface)task).setAbsDBFactory(factoryType);
			((TaskInterface)task).setMainController(mainCtrl);
			((TaskInterface)task).setMyDBAbstract(myDBAbs);
			((TaskInterface)task).setSelectedSchemaEntity(selectedSchemaEntity);
		}
		
		return task;
	}

}
