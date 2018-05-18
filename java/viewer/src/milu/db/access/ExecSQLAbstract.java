package milu.db.access;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.main.AppConf;

abstract public class ExecSQLAbstract 
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
	protected List<String>  colNameLst      = new ArrayList<String>();	
	// Data List
	protected List<List<String>> dataLst    = new ArrayList<List<String>>();	
	
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
	
	public List<String> getColNameLst()
	{
		return this.colNameLst;
	}
	
	public List<List<String>> getDataLst()
	{
		return this.dataLst;
	}
	
	protected void clear()
	{
		this.execStartTime = -1;
		this.execEndTime   = -1;
		this.colNameLst.clear();
		this.dataLst.clear();
	}
	
	abstract public void exec( final int checkCnt )
			throws 
			SQLException,
			MyDBOverFetchSizeException,
			Exception;
}
