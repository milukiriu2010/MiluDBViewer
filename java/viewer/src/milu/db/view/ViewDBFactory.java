package milu.db.view;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.abs.ObjDBFactory;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class ViewDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		ViewDBAbstract viewDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			viewDBAbs = new ViewDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			viewDBAbs = new ViewDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			viewDBAbs = new ViewDBOracle();
		}
		else
		{
			return null;
		}
		
		viewDBAbs.setMyDBAbstract(myDBAbs);
		return viewDBAbs;
	}
}
