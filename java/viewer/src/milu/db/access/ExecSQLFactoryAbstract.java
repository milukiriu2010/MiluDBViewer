package milu.db.access;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;

abstract public class ExecSQLFactoryAbstract 
{
	abstract public ExecSQLAbstract createFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf );
}
