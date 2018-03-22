package milu.db.explain;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

import milu.ctrl.MainController;

public class ExplainDBFactory
{
	public static ExplainDBAbstract getInstance( MyDBAbstract myDBAbs, MainController mainCtrl )
	{
		ExplainDBAbstract explainDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			explainDBAbs = new ExplainDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			explainDBAbs = new ExplainDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			explainDBAbs = new ExplainDBOracle();
		}
		else
		{
			return null;
		}
		
		explainDBAbs.setMyDBAbstract(myDBAbs);
		explainDBAbs.setMainController(mainCtrl);
		return explainDBAbs;
	}
}

