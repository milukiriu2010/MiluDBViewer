package milu.db.type;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class TypeDBFactory 
{
	public static TypeDBAbstract getInstance( MyDBAbstract myDBAbs )
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
		
		typeDBAbs.setMyDBAbstract(myDBAbs);
		return typeDBAbs;
	}
}
