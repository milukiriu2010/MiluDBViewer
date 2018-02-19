package milu.db.sysview;

import milu.db.MyDBAbstract;
import milu.db.MyDBMySQL;

public class SystemViewDBFactory 
{
	public static SystemViewDBAbstract getInstance( MyDBAbstract myDBAbs )
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
