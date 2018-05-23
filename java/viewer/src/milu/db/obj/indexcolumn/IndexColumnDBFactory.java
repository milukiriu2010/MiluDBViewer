package milu.db.obj.indexcolumn;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
//import milu.db.MyDBGeneral;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;
import milu.db.MyDBMongo;

public class IndexColumnDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		IndexColumnDBAbstract indexColumnDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			indexColumnDBAbs = new IndexColumnDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			indexColumnDBAbs = new IndexColumnDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			indexColumnDBAbs = new IndexColumnDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			indexColumnDBAbs = new IndexColumnDBCassandra();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			indexColumnDBAbs = new IndexColumnDBSQLServer();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			indexColumnDBAbs = new IndexColumnDBSQLite();
		}
		else if ( myDBAbs instanceof MyDBMongo )
		{
			indexColumnDBAbs = new IndexColumnDBMongo();
		}
		else
		{
			indexColumnDBAbs = new IndexColumnDBGeneral();
			//return null;
		}
		
		indexColumnDBAbs.setMyDBAbstract(myDBAbs);
		return indexColumnDBAbs;
	}
}

