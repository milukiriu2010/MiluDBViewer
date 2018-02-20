package milu.db.indexcolumn;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class IndexColumnDBFactory
{
	public static IndexColumnDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		if ( myDBAbs instanceof MyDBPostgres )
		{
			return new IndexColumnDBPostgres( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			return new IndexColumnDBMySQL( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			return new IndexColumnDBOracle( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			return new IndexColumnDBCassandra( myDBAbs );
		}
		else
		{
			return null;
		}
	}
}

