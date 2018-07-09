package milu.db.access;

import java.util.List;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;
import milu.task.ProgressInterface;

public class ExecSQLFactory extends ExecSQLFactoryAbstract {

	@Override
	public ExecSQLAbstract createFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ProgressInterface progressInf, double assignedSize ) 
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
		else if ( SQLBag.TYPE.CREATE_TABLE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.CREATE_INDEX.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.CREATE_VIEW.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.ALTER_VIEW.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.ALTER.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.DROP.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.TRUNCATE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.EXECUTE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.MERGE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.UPSERT.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransaction();
		}
		else if ( SQLBag.TYPE.REPLACE.equals(sqlBag.getType()) )
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
		execSQLAbs.setProgressInterface(progressInf);
		execSQLAbs.setAssignedSize(assignedSize);
		return execSQLAbs;
	}

	@Override
	public ExecSQLAbstract createPreparedFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, List<Object> preLst ) 
	{
		ExecSQLAbstract execSQLAbs = null;
		if ( SQLBag.TYPE.INSERT.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransactionPrepared();
		}
		else if ( SQLBag.TYPE.UPDATE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransactionPrepared();
		}
		else if ( SQLBag.TYPE.DELETE.equals(sqlBag.getType()) )
		{
			execSQLAbs = new ExecSQLTransactionPrepared();
		}
		else
		{
			return null;
		}
		execSQLAbs.setSQLBag(sqlBag);
		execSQLAbs.setMyDBAbstract(myDBAbs);
		execSQLAbs.setAppConf(appConf);
		execSQLAbs.setPreLst(preLst);
		return execSQLAbs;
	}
}
