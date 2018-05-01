package milu.db.obj.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class ViewDBSQLite extends ViewDBAbstract 
{
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName ) throws SQLException 
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(View) ---------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------" );
		
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				SchemaEntity viewEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.VIEW );
				viewEntityLst.add( viewEntity );
			}
		}
		
		return viewEntityLst;
	}

	@Override
	protected String listSQL(String schemaName ) 
	{
		String sql = 
			" select \n" +
			"   name    \n" +
			" from   \n" +
	  		"   sqlite_master \n"  +
			" where \n" +
			"   type='view' \n" + 
			" order by name";
		return sql;
	}

	// select Table Definition
	@Override
	public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schameName, viewName );
		
		System.out.println( " -- selectDefinition(View) -------------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				Map<String,String> dataRow = new LinkedHashMap<String,String>();
				dataRow.put( "column_name"   , rs.getString("name") );
				dataRow.put( "data_type"     , rs.getString("type") );
				dataRow.put( "data_size"     , null );
				/*
				String nullable = "NULL OK";
				String pk       = rs.getString("pk");
				String notnull  = rs.getString("notnull");
				if ( "1".equals(pk) )
				{
					nullable = "NULL NG";
				}
				else if ( "1".equals(notnull) )
				{
					nullable = "NULL NG";
				}
				
				dataRow.put( "nullable"      , nullable );
				*/
				dataLst.add( dataRow );
			}
		}
		
		return dataLst;
	}
	
	// SQL for Table Definition
	@Override
	protected String definitionSQL( String schemaName, String viewName )
	{
		String sql = 
			" pragma table_info(" + viewName + ")";
		return sql;
	}
	
}
