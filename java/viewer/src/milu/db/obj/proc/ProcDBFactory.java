package milu.db.obj.proc;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;

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
		else
		{
			return null;
		}
		
		
		procDBAbs.setMyDBAbstract(myDBAbs);
		return procDBAbs;
	}
}
