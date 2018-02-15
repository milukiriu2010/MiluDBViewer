package milu.db.view;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class ViewDBFactory
{
	public static ViewDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		if ( myDBAbs instanceof MyDBPostgres )
		{
			return new ViewDBPostgres( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			return new ViewDBMySQL( myDBAbs );
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			return new ViewDBOracle( myDBAbs );
		}
		else
		{
			return null;
		}
	}
}
