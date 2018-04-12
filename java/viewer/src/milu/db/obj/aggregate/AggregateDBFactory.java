package milu.db.obj.aggregate;

import milu.db.MyDBAbstract;
import milu.db.MyDBCassandra;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;

public class AggregateDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		AggregateDBAbstract aggregateDBAbs = null;
		if ( myDBAbs instanceof MyDBCassandra )
		{
			aggregateDBAbs = new AggregateDBCassandra();
		}
		else
		{
			return null;
		}
		
		aggregateDBAbs.setMyDBAbstract(myDBAbs);
		return aggregateDBAbs;
	}
}
