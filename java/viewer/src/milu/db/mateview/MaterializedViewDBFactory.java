package milu.db.mateview;

import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class MaterializedViewDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		MaterializedViewDBAbstract materializedViewDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			materializedViewDBAbs = new MaterializedViewDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			materializedViewDBAbs = new MaterializedViewDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			materializedViewDBAbs = new MaterializedViewDBCassandra();
		}
		else
		{
			return null;
		}
		
		materializedViewDBAbs.setMyDBAbstract( myDBAbs );
		return materializedViewDBAbs;
	}
}
