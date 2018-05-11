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
			task = new CollectTaskRootBasic();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			task = new CollectTaskRootBasic();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			task = new CollectTaskRootBasic();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			task = new CollectTaskRootBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			task = new CollectTaskRootBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			task = new CollectTaskRootNoSchema();
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
	
	public static Task<Exception> getInstance( AbsDBFactory.FACTORY_TYPE factoryType, CollectDataType dataType, MainController mainCtrl, MyDBAbstract myDBAbs, SchemaEntity selectedSchemaEntity )
	{
		Task<Exception> task = null;
		
		if ( AbsDBFactory.FACTORY_TYPE.TABLE.equals(factoryType) )
		{
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			else
			{
				task = new CollectTaskObj1Level();
			}
		}
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX.equals(factoryType) )
		{
			task = new CollectTaskObj2Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN.equals(factoryType) )
		{
			task = new CollectTaskObj3Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.VIEW.equals(factoryType) )
		{
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			else
			{
				task = new CollectTaskObj1Level();
			}
		}
		else if ( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW.equals(factoryType) )
		{
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			else
			{
				task = new CollectTaskObj1Level();
			}
		}
		else if ( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW.equals(factoryType) )
		{
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			else
			{
				task = new CollectTaskObj1Level();
			}
		}
		else if ( AbsDBFactory.FACTORY_TYPE.FUNC.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.AGGREGATE.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.PROC.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.PACKAGE_BODY.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.TYPE.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.TRIGGER.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.SEQUENCE.equals(factoryType) )
		{
			task = new CollectTaskObj1Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY.equals(factoryType) )
		{
			task = new CollectTaskObjForeignKey();
		}
		else
		{
			return null;
		}
		
		if ( task instanceof TaskInterface )
		{
			((TaskInterface)task).setAbsDBFactory(factoryType);
			((TaskInterface)task).setCollectDataType(dataType);
			((TaskInterface)task).setMainController(mainCtrl);
			((TaskInterface)task).setMyDBAbstract(myDBAbs);
			((TaskInterface)task).setSelectedSchemaEntity(selectedSchemaEntity);
		}
		
		return task;
	}

}
