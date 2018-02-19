package milu.db.proc;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class ProcDBFactory 
{
	public static ProcDBAbstract getInstance( MyDBAbstract myDBAbs )
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
