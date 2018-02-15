package milu.db.schema;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class SchemaDBFactory 
{
	public static SchemaDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		if ( myDBAbs instanceof MyDBPostgres )
		{
			return new SchemaDBPostgres( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			return new SchemaDBMySQL( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			return new SchemaDBOracle( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			return new SchemaDBCassandra( myDBAbs );
		}
		else
		{
			return null;
		}
	}
}
