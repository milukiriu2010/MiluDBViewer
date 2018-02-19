package milu.db.table;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.schema.SchemaDBCassandra;
import milu.db.schema.SchemaDBMySQL;
import milu.db.schema.SchemaDBOracle;
import milu.db.schema.SchemaDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class TableDBFactory
{
	public static TableDBAbstract getInstance( MyDBAbstract myDBAbs )
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
