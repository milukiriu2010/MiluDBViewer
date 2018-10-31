package milu.db.access;

import java.util.List;

import javafx.collections.ObservableList;
import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;
import milu.task.ProgressInterface;

abstract public class ExecSQLFactoryAbstract 
{
	abstract public ExecSQLAbstract createFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ProgressInterface progressInf, double assignedSize );
	abstract public ExecSQLAbstract createPreparedFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, List<Object> preLst );
	abstract public ExecSQLAbstract createCallableFactory( SQLBag sqlBag, MyDBAbstract myDBAbs, AppConf appConf, ObservableList<CallObj> paramLst, List<Object> preLst );
}
