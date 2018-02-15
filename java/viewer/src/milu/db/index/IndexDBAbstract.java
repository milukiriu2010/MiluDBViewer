package milu.db.index;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.SQLException;

import milu.db.MyDBAbstract;

public abstract class IndexDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Column Name List
	protected List<String>  colNameLst      = new ArrayList<String>();
	
	// Data List
	protected List<Map<String,String>> dataLst    = new ArrayList<>();
	
	public IndexDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public List<String> getColNameLst()
	{
		return this.colNameLst;
	}
	
	public List<Map<String,String>> getDataLst()
	{
		return this.dataLst;
	}
	
	protected void clear()
	{
		this.colNameLst.clear();
		this.dataLst.clear();
	}
	
	abstract public void selectColumnLst( String schemaName, String tableName, String indexName ) throws SQLException;
	
	abstract protected String columnLstSQL( String schemaName, String tableName, String indexName );
}
