package milu.db.obj.schema;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
//import milu.db.MyDBGeneral;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;

public class SchemaDBFactory implements ObjDBFactory 
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		SchemaDBAbstract schemaDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			schemaDBAbs = new SchemaDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			schemaDBAbs = new SchemaDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			schemaDBAbs = new SchemaDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			schemaDBAbs = new SchemaDBCassandra();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			schemaDBAbs = new SchemaDBSQLServer();
		}
		else
		{
			schemaDBAbs = new SchemaDBGeneral();
			//return null;
		}
		
		schemaDBAbs.setMyDBAbstract(myDBAbs);
		return schemaDBAbs;
	}
}
