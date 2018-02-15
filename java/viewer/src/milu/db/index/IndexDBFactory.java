package milu.db.index;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class IndexDBFactory
{
	public static IndexDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		if ( myDBAbs instanceof MyDBPostgres )
		{
			return new IndexDBPostgres( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			return new IndexDBMySQL( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			return new IndexDBOracle( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			return new IndexDBCassandra( myDBAbs );
		}
		else
		{
			return null;
		}
	}
}

