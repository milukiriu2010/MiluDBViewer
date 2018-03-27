package milu.db.aggregate;

import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.db.MyDBCassandra;

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
