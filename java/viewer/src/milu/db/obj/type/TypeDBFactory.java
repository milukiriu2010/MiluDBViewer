package milu.db.obj.type;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;

public class TypeDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		TypeDBAbstract typeDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			typeDBAbs = new TypeDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			typeDBAbs = new TypeDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			typeDBAbs = new TypeDBCassandra();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			typeDBAbs = new TypeDBSQLServer();
		}
		else
		{
			return null;
		}
		
		typeDBAbs.setMyDBAbstract(myDBAbs);
		return typeDBAbs;
	}
}
