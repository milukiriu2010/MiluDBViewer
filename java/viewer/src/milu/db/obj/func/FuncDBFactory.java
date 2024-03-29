package milu.db.obj.func;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;

public class FuncDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		FuncDBAbstract funcDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			funcDBAbs = new FuncDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			funcDBAbs = new FuncDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			funcDBAbs = new FuncDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			funcDBAbs = new FuncDBCassandra();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			funcDBAbs = new FuncDBSQLServer();
		}
		else
		{
			return null;
		}		
		
		funcDBAbs.setMyDBAbstract(myDBAbs);
		return funcDBAbs;
	}
}
