package milu.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class TableDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Table Name List
	protected List<Map<String,String>>  tableNameLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public List<Map<String,String>> getTableNameLst()
	{
		return this.tableNameLst;
	}	
	
	public List<SchemaEntity> getTableEntityLst()
	{
		List<SchemaEntity>  tableEntityLst = new ArrayList<>();
		for ( Map<String,String> tableName : this.tableNameLst )
		{
			SchemaEntity eachTableEntity = SchemaEntityFactory.createInstance( tableName.get("tableName"), SchemaEntity.SCHEMA_TYPE.TABLE );
			tableEntityLst.add( eachTableEntity );
		}
		return tableEntityLst;
	}
	
	protected void clear()
	{
		this.tableNameLst.clear();
	}	
	
	public void selectTableLst( String schemaName ) throws SQLException
	{
		String sql = this.tableLstSQL( schemaName );
		
		System.out.println( " -- selectTableLst ------------------" );
		System.out.println( sql );
		System.out.println( " -------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql )
		)
		{
			while ( rs.next() )
			{
				Map<String,String> dataRow = new HashMap<>();
				dataRow.put( "tableName", rs.getString(1) );
				this.tableNameLst.add( dataRow );
			}
		}
	}
	
	abstract protected String tableLstSQL( String schemaName );	
	
}
