package milu.gui.stmt.prepare;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBPostgres;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;

public class PrepareExampleFactory 
{
	public static PrepareExampleAbs createInstance( MyDBAbstract myDBAbs )
	{
		PrepareExampleAbs peAbs = null;
		if ( myDBAbs instanceof MyDBOracle )
		{
			peAbs = new PrepareExampleOracle();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			peAbs = new PrepareExampleMySQL();
		}
		else if ( myDBAbs instanceof MyDBPostgres )
		{
			peAbs = new PrepareExamplePostgres();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			peAbs = new PrepareExampleSQLite();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			peAbs = new PrepareExampleSQLServer();
		}
		else
		{
			peAbs = new PrepareExampleOracle();
		}
		
		return peAbs;
	}
}
