package milu.db.obj.sysview;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;

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
