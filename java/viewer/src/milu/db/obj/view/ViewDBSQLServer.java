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

public class ViewDBSQLServer extends ViewDBAbstract 
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
			"   sv.name  name    \n" +
			" from   \n" +
	  		"   sys.views sv, \n"  +
			"   sys.schemas ss \n" +
			" where \n" +
			"   sv.type_desc='VIEW' \n" + 
			"   and \n" + 
			"   sv.schema_id=ss.schema_id \n" +
			"   and \n" + 
			"   ss.name = '" + schemaName + "' \n" +
			" order by sv.name";
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
	
	// SQL for Table Definition
	@Override
	protected String definitionSQL( String schemaName, String viewName )
	{
		String sql = 
			" select \n"                             + 
			"   column_name, \n"                     +
			"   data_type,   \n"                     +
			"  case \n" + 
			"    when numeric_precision is null and data_type like '%char%' \n" + 
			"                  then  replace( cast( character_maximum_length as char ), ' ', '' ) \n" +
			"    when    numeric_scale is null \n"   + 
			"                  then  replace( cast( numeric_precision as char ), ' ', '' ) \n" + 
			"    when    numeric_scale = 0 \n"       + 
			"                  then  replace( cast( numeric_precision as char ), ' ', '' ) \n" + 
			"    else                replace( cast( concat( numeric_precision, ',', numeric_scale ) as char ), ' ', '' ) \n" + 
			"    end         data_size,     \n"      +
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
