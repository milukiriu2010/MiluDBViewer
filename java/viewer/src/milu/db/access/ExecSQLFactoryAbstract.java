package milu.db.access;

import java.util.List;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;
import milu.task.ProgressInterface;

abstract public class ExecSQLFactoryAbstract 
{
	abstract public ExecSQLAbstract createFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ProgressInterface progressInf, double assignedSize );
	abstract public ExecSQLAbstract createPreparedFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, List<Object> preLst );
}
