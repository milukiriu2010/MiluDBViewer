package milu.db.trigger;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class TriggerDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		TriggerDBAbstract triggerDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			triggerDBAbs = new TriggerDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			triggerDBAbs = new TriggerDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			triggerDBAbs = new TriggerDBOracle();
		}
		else
		{
			return null;
		}
		
		triggerDBAbs.setMyDBAbstract(myDBAbs);
		return triggerDBAbs;
	}
}
