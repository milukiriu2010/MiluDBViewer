package milu.db.table;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class TableDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		TableDBAbstract tableDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			tableDBAbs = new TableDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			tableDBAbs = new TableDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			tableDBAbs = new TableDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			tableDBAbs = new TableDBCassandra();
		}
		else
		{
			return null;
		}
		
		tableDBAbs.setMyDBAbstract( myDBAbs );
		return tableDBAbs;
	}
}
