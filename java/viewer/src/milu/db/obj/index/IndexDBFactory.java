package milu.db.obj.index;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;

public class IndexDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
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
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			IndexDBAbs = new IndexDBSQLServer();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			IndexDBAbs = new IndexDBSQLite();
		}
		else
		{
			return null;
		}
		
		IndexDBAbs.setMyDBAbstract(myDBAbs);
		return IndexDBAbs;
	}
}
