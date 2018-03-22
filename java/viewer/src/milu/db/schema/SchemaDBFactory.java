package milu.db.schema;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

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
		else
		{
			return null;
		}
		
		schemaDBAbs.setMyDBAbstract(myDBAbs);
		return schemaDBAbs;
	}
}
