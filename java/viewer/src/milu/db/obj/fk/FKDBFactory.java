package milu.db.obj.fk;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;

public class FKDBFactory implements ObjDBFactory 
{
	@Override
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs ) 
	{
		FKDBAbstract fkDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			fkDBAbs = new FKDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			fkDBAbs = new FKDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			fkDBAbs = new FKDBOracle();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			fkDBAbs = new FKDBSQLServer();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			fkDBAbs = new FKDBSQLite();
		}
		else
		{
			return null;
			//fkDBAbs = new FKDBGeneral();
		}
		
		fkDBAbs.setMyDBAbstract(myDBAbs);
		return fkDBAbs;
	}

}
