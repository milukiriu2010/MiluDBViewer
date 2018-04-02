package milu.db.access;

import milu.ctrl.sqlparse.SQLBag;
import milu.db.MyDBAbstract;
import milu.conf.AppConf;

abstract public class ExecSQLFactoryAbstract 
{
	abstract public ExecSQLAbstract createFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf );
}
