package milu.db.obj.view;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;

public class ViewDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		ViewDBAbstract viewDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			viewDBAbs = new ViewDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			viewDBAbs = new ViewDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			viewDBAbs = new ViewDBOracle();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			viewDBAbs = new ViewDBSQLServer();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			viewDBAbs = new ViewDBSQLite();
		}
		else
		{
			return null;
		}
		
		viewDBAbs.setMyDBAbstract(myDBAbs);
		return viewDBAbs;
	}
}
