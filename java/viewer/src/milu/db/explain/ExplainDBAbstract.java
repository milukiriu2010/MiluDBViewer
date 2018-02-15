package milu.db.explain;

import java.util.List;
import java.util.ArrayList;

import java.sql.SQLException;

import milu.db.MyDBAbstract;

abstract public class ExplainDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Column Name List
	protected List<String>  colNameLst      = new ArrayList<String>();
	
	// Data List
	protected List<List<String>> dataLst    = new ArrayList<List<String>>();
	
	public ExplainDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
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
		this.colNameLst.clear();
		this.dataLst.clear();
	}
	
	abstract public void explain( String sql ) throws SQLException;
}
