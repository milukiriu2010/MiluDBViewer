package milu.db.aggregate;

import milu.db.MyDBAbstract;
import milu.db.MyDBCassandra;

public class AggregateDBFactory
{
	public static AggregateDBAbstract getInstance( MyDBAbstract myDBAbs )
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
