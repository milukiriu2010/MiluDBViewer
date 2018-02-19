package milu.db.func;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class FuncDBFactory 
{
	public static FuncDBAbstract getInstance( MyDBAbstract myDBAbs )
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
		else
		{
			return null;
		}		
		
		funcDBAbs.setMyDBAbstract(myDBAbs);
		return funcDBAbs;
	}
}
