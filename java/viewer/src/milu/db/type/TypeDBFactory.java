package milu.db.type;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

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
		else
		{
			return null;
		}
		
		typeDBAbs.setMyDBAbstract(myDBAbs);
		return typeDBAbs;
	}
}
