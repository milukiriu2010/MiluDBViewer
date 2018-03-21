package milu.db.sequence;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;

public class SequenceDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
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
