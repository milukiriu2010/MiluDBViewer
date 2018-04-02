package milu.db.access;

import milu.ctrl.sqlparse.SQLBag;
import milu.db.MyDBAbstract;
import milu.conf.AppConf;

public class ExecSQLFactory extends ExecSQLFactoryAbstract {

	@Override
	public ExecSQLAbstract createFactory(SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf ) 
	{
		ExecSQLAbstract execSQLAbs = null;
		if ( SQLBag.TYPE.SELECT.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLSelect();
		}
		else if ( SQLBag.TYPE.INSERT.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.UPDATE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.DELETE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
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
