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
import milu.db.MyDBMongo;
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
		else if ( myDBAbs instanceof MyDBMongo )
		{
			task = new CollectTaskRootBasic();
			//task = new CollectTaskRootNoSchema();
		}
		else
		{
			task = new CollectTaskRootBasic();
			//task = new CollectTaskRootNoSchema();
			//return null;
		}
		
		if ( task instanceof CollectTaskInterface )
		{
			((CollectTaskInterface)task).setMainController(mainCtrl);
			((CollectTaskInterface)task).setMyDBAbstract(myDBAbs);
		}
		
		return task;
	}
	
	public static Task<Exception> getInstanceForTableLst( MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		Task<Exception> task = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			task = new CollectTaskTableBasic();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			task = new CollectTaskTableBasic();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			task = new CollectTaskTableBasic();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			task = new CollectTaskTableBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			task = new CollectTaskTableBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			task = new CollectTaskTableNoSchema();
		}
		else if ( myDBAbs instanceof MyDBMongo )
		{
			task = new CollectTaskTableBasic();
		}
		else
		{
			task = new CollectTaskTableBasic();
		}
		
		if ( task instanceof CollectTaskInterface )
		{
			((CollectTaskInterface)task).setMainController(mainCtrl);
			((CollectTaskInterface)task).setMyDBAbstract(myDBAbs);
		}
		
		return task;
	}
	
	public static Task<Exception> getInstance( AbsDBFactory.FACTORY_TYPE factoryType, CollectDataType dataType, MainController mainCtrl, MyDBAbstract myDBAbs, SchemaEntity selectedSchemaEntity )
	{
		Task<Exception> task = null;
		
		if ( AbsDBFactory.FACTORY_TYPE.TABLE.equals(factoryType) )
		{
			// get list & definition "TABLE"
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			// get list "TABLE"
			else
			{
				task = new CollectTaskObj1Level();
			}
		}
		// get list "INDEX"
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX.equals(factoryType) )
		{
			task = new CollectTaskObj2Level();
		}
		// get list "COLUMN" for "INDEX"
		else if ( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN.equals(factoryType) )
		{
			task = new CollectTaskObj3Level();
		}
		else if ( AbsDBFactory.FACTORY_TYPE.VIEW.equals(factoryType) )
		{
			// get list & definition "VIEW"
			if ( CollectDataType.LIST_AND_DEFINITION.equals(dataType) )
			{
				task = new CollectTaskRootObject();
			}
			// get list "VIEW"
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
		
		if ( task instanceof CollectTaskInterface )
		{
			((CollectTaskInterface)task).setAbsDBFactory(factoryType);
			((CollectTaskInterface)task).setCollectDataType(dataType);
			((CollectTaskInterface)task).setMainController(mainCtrl);
			((CollectTaskInterface)task).setMyDBAbstract(myDBAbs);
			((CollectTaskInterface)task).setSelectedSchemaEntity(selectedSchemaEntity);
		}
		
		return task;
	}

}
