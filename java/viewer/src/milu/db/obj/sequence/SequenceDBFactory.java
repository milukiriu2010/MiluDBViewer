package milu.db.obj.sequence;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
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
