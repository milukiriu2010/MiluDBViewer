package milu.db.explain;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class ExplainDBFactory
{
	public static ExplainDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		if ( myDBAbs instanceof MyDBPostgres )
		{
			return new ExplainDBPostgres( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			return new ExplainDBMySQL( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			return new ExplainDBOracle( myDBAbs );
		}
		else
		{
			return null;
		}
	}
}

