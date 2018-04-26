package milu.db.obj.proc;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;

public class ProcDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		ProcDBAbstract procDBAbs = null;
		
		if ( myDBAbs instanceof MyDBMySQL )
		{
			procDBAbs = new ProcDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			procDBAbs = new ProcDBOracle();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			procDBAbs = new ProcDBSQLServer();
		}
		else
		{
			return null;
		}
		
		
		procDBAbs.setMyDBAbstract(myDBAbs);
		return procDBAbs;
	}
}
