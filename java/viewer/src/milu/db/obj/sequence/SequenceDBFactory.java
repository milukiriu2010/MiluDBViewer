package milu.db.obj.sequence;

import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;

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
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			sequenceDBAbs = new SequenceDBSQLServer();
		}
		else
		{
			return null;
		}
		
		sequenceDBAbs.setMyDBAbstract(myDBAbs);
		return sequenceDBAbs;
	}
}
