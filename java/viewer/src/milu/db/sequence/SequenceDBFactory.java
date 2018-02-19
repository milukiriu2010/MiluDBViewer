package milu.db.sequence;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;

public class SequenceDBFactory 
{
	public static SequenceDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		SequenceDBAbstract sequenceDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			sequenceDBAbs = new SequenceDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			sequenceDBAbs = new SequenceDBOracle();
		}
		else
		{
			return null;
		}
		
		sequenceDBAbs.setMyDBAbstract(myDBAbs);
		return sequenceDBAbs;
	}
}
