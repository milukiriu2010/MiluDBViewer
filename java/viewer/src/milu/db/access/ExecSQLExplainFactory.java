package milu.db.access;

import milu.ctrl.sqlparse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;
import milu.main.AppConf;

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
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			execSQLAbs = new ExecSQLExplainSQLServer();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			execSQLAbs = new ExecSQLExplainSQLite();
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
