package milu.db.mateview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class MaterializedViewDBCassandra extends MaterializedViewDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(MaterializedView) -----" );
		System.out.println( sql );
		System.out.println( " ------------------------------------------" );
		
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				/*
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "viewName", rs.getString("view_name") );
				this.viewLst.add( mapView );
				*/
				SchemaEntity viewEntity = SchemaEntityFactory.createInstance( rs.getString("view_name"), SchemaEntity.SCHEMA_TYPE.VIEW );
				viewEntityLst.add( viewEntity );				
				
			}
		}
		
		return viewEntityLst;
	}

	@Override
	protected String listSQL(String schemaName)
	{
		String sql =
			" select \n"           +
		    "   view_name      \n" +
			" from  \n" +
			"   system_schema.views \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by view_name";
		return sql;
	}

	// select View Definition
	@Override
	public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schameName, viewName );
		
		System.out.println( " -- selectDefinition(MaterializedView) ------------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				Map<String,String> dataRow = new LinkedHashMap<String,String>();
				dataRow.put( "column_name"   , rs.getString("column_name") );
				dataRow.put( "data_type"     , rs.getString("data_type") );
				dataRow.put( "data_size"     , rs.getString("data_size") );
				String nullable = "NULL OK";
				// partition_key => PRIMARY KEY
				// clustering    => part of PRIMARY KEY
				// regular       => 
				String kind = rs.getString("kind");
				if ( "partition_key".equals(kind) )
				{
					nullable = "NULL NG";
				}
				else if ( "clustering".equals(kind) )
				{
					nullable = "NULL NG";
				}
				dataRow.put( "nullable"      , nullable );
				dataRow.put( "data_default"  , "" );
				dataLst.add( dataRow );
			}
		}
		return dataLst;
	}
	
	// SQL for View Definition
	@Override
	protected String definitionSQL( String schemaName, String viewName )
	{
		String sql =
			" select         \n" +
		    "   column_name, \n" +
		    "   type,        \n" +
		    "   kind         \n" +
			" from           \n" +
			"   system_schema.columns \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			"   and \n" +
			"   table_name = '" + viewName + "'";
		return sql;
	}

}
