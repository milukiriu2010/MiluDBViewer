package milu.db.access;

import java.util.List;

import javafx.collections.ObservableList;
import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;
import milu.main.AppConf;
import milu.task.ProgressInterface;

public class ExecSQLExplainFactory extends ExecSQLFactoryAbstract 
{
	@Override
	public ExecSQLAbstract createFactory(SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ProgressInterface progressInf, double assignedSize ) 
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

	@Override
	public ExecSQLAbstract createPreparedFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, List<Object> preLst ) 
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ExecSQLAbstract createCallableFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ObservableList<CallObj> paramLst, List<Object> preLst ) 
	{
		throw new UnsupportedOperationException();
	}
}
