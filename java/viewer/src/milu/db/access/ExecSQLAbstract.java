package milu.db.access;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;
import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

abstract public class ExecSQLAbstract
	implements ProgressReportInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Application Configuration
	protected AppConf       appConf = null;
	
	protected SQLBag        sqlBag  = null;
	
	// exec start time
	protected long          execStartTime = -1;
	
	// exec end time
	protected long          execEndTime   = -1;
	
	// Column Name List
	protected List<Object>  colNameLst      = new ArrayList<>();
	// Data List
	protected List<List<Object>> dataLst    = new ArrayList<>();
	// Prepared List
	protected List<Object>   preLst = null;
	// Callable List
	protected ObservableList<CallObj> callLst = null;
	
	protected ProgressInterface  progressInf  = null;
	protected double             assignedSize = 0.0;
	protected boolean            isCancel = false;
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public void setSQLBag( SQLBag sqlBag )
	{
		this.sqlBag = sqlBag;
	}
	
	public long getExecTime()
	{
		return ( this.execEndTime - this.execStartTime );
	}
	
	public List<Object> getColNameLst()
	{
		return this.colNameLst;
	}
	
	public List<List<Object>> getDataLst()
	{
		return this.dataLst;
	}
	
	public void setPreLst( List<Object> preLst )
	{
		this.preLst = preLst;
	}
	
	// Callable List
	public void setCallLst( ObservableList<CallObj> callLst )
	{
		this.callLst = callLst;
	}
	
	// ProgressReportInterface
	@Override
	public void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	// ProgressReportInterface
	@Override
	public void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	// ProgressReportInterface
	@Override
	public void cancel()
	{
		this.isCancel= true;
	}
	
	protected void clear()
	{
		this.execStartTime = -1;
		this.execEndTime   = -1;
		this.colNameLst.clear();
		this.dataLst.clear();
	}
	
	abstract public void exec( final int checkCnt, final int fetchPos )
			throws 
			SQLException,
			MyDBOverFetchSizeException,
			Exception;
}
