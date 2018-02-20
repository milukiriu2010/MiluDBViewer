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
		IndexDBAbstract IndexDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			IndexDBAbs = new IndexDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			IndexDBAbs = new IndexDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			IndexDBAbs = new IndexDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			IndexDBAbs = new IndexDBCassandra();
		}
		else
		{
			return null;
		}
		
		IndexDBAbs.setMyDBAbstract(myDBAbs);
		return IndexDBAbs;
	}
}
