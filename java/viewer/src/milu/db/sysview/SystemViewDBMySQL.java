package milu.db.sysview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class SystemViewDBMySQL extends SystemViewDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(SystemView) ---" );
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
				/*
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "viewName", rs.getString("table_name") );
				this.viewLst.add( mapView );
				*/
				SchemaEntity viewEntity = SchemaEntityFactory.createInstance( rs.getString("table_name"), SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW );
				viewEntityLst.add( viewEntity );
			}
		}
		
		return viewEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select table_name from information_schema.tables \n" +
			" where \n" +
			" table_type='SYSTEM VIEW' \n" + 
			" and \n" + 
			" table_schema = '" + schemaName + "' \n" +
			" order by table_name";
		return sql;
	}

	// select View Definition
	@Override
	public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schameName, viewName );
		
		System.out.println( " -- selectDefinition(SystemView) -------" );
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
				dataRow.put( "column_name"   , rs.getString("column_name") );
				dataRow.put( "data_type"     , rs.getString("data_type") );
				dataRow.put( "data_size"     , rs.getString("data_size") );
				dataRow.put( "nullable"      , rs.getString("nullable") );
				dataRow.put( "data_default"  , rs.getString("data_default") );
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
			" select \n"                             + 
			"   column_name, \n"                     +
			"   column_type  data_type,     \n"      +
			"   null         data_size,     \n"      +
			"   case is_nullable  \n"                +
			"     when 'YES' then 'NULL OK' \n"      +
			"     else            'NULL NG' \n"      +
			"   end nullable,  \n"                   +
			"   column_default data_default \n"      +
			" from             \n"                   + 
			"   information_schema.columns  \n"      +
			" where            \n"                   +
			"   table_schema = '" + schemaName + "' \n"  +
			"   and              \n"                 +
			"   table_name = '" + viewName + "' \n"     + 
			" order by ordinal_position";
		return sql;
	}
}
