package milu.db.access;

import milu.ctrl.sqlparse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.main.AppConf;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class ExecSQLExplainFactory extends ExecSQLFactoryAbstract 
{
	@Override
	public ExecSQLAbstract createFactory(SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf ) 
	{
		ExecSQLAbstract execSQLAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			execSQLAbs = new ExecSQLExplainPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			execSQLAbs = new ExecSQLExplainMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			execSQLAbs = new ExecSQLExplainOracle();
		}
		else
		{
			return null;
		}

		execSQLAbs.setSQLBag(sqlBag);
		execSQLAbs.setMyDBAbstract(myDBAbs);
		execSQLAbs.setAppConf(appConf);
		return execSQLAbs;
	}

}
