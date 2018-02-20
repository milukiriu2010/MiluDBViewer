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
	
	// Table List
	protected List<Map<String,String>>  tableLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	/*
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  tableEntityLst = new ArrayList<>();
		for ( Map<String,String> table : this.tableLst )
		{
			SchemaEntity eachTableEntity = SchemaEntityFactory.createInstance( table.get("tableName"), SchemaEntity.SCHEMA_TYPE.TABLE );
			tableEntityLst.add( eachTableEntity );
		}
		return tableEntityLst;
	}
	*/
	
	// select Table List
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		List<SchemaEntity>  tableEntityLst = new ArrayList<>();
		
		String sql = this.listSQL( schemaName );
		
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
				SchemaEntity tableEntity = SchemaEntityFactory.createInstance( rs.getString(1), SchemaEntity.SCHEMA_TYPE.TABLE );
				tableEntityLst.add( tableEntity );
			}
		}
		
		return tableEntityLst;
	}
	
	// SQL for Table List
	abstract protected String listSQL( String schemaName );
	
	// select Table Definition
	abstract public List<Map<String,String>> selectDefinition( String schameName, String tableName ) throws SQLException;
	
	// SQL for Table Definition
	abstract protected String definitionSQL( String schemaName, String tableName );
}
