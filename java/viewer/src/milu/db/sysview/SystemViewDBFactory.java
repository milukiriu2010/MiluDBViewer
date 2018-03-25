package milu.db.sysview;

import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;

public class SystemViewDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		SystemViewDBAbstract systemViewDBAbs = null;
		if ( myDBAbs instanceof MyDBMySQL )
		{
			systemViewDBAbs = new SystemViewDBMySQL();
		}
		else
		{
			return null;
		}
		
		systemViewDBAbs.setMyDBAbstract(myDBAbs);
		return systemViewDBAbs;
	}
}
