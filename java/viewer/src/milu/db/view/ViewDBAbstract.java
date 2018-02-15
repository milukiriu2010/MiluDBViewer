package milu.db.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.SQLException;

import milu.db.MyDBAbstract;

public abstract class ViewDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// View Name List
	protected List<String>  viewNameLst      = new ArrayList<String>();
	
	// Data List
	protected List<Map<String,String>> dataLst    = new ArrayList<>();
	
	public ViewDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public List<String> getViewNameLst()
	{
		return this.viewNameLst;
	}
	
	public List<Map<String,String>> getDataLst()
	{
		return this.dataLst;
	}
	
	protected void clear()
	{
		this.viewNameLst.clear();
		this.dataLst.clear();
	}
	
	abstract public void selectViewLst( String schemaName ) throws SQLException;
	
	abstract protected String viewLstSQL( String schemaName );
}
