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

public class MaterializedViewDBOracle extends MaterializedViewDBAbstract
{
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
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
				mapView.put( "viewName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.viewLst.add( mapView );
				*/
				SchemaEntity viewEntity = SchemaEntityFactory.createInstance( rs.getString("object_name"), SchemaEntity.SCHEMA_TYPE.VIEW );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "INVALID".equals(strStatus) )
				{
					viewEntity.setState( SchemaEntity.STATE.INVALID );
				}
				viewEntityLst.add( viewEntity );				
			}
		}
		
		return viewEntityLst;
	}
	
	protected String listSQL( String schemaName )
	{
		String sql = 
			" select object_name, status from all_objects \n" +
			" where \n" +
			" object_type='MATERIALIZED VIEW' \n" + 
			" and \n" + 
			" owner = '" + schemaName + "' \n" +
			" order by owner, object_name";
		return sql;
	}
	

	// select View Definition
	@Override
	public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schameName, viewName );
		
		System.out.println( " -- selectDefinition(MaterializedView) -------------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------------------" );
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
			" select \n"         + 
			"   column_name, \n" +
			"   data_type,   \n" +
	   		"   case         \n" + 
	   		"     when       \n" +
	        "       data_precision is null and regexp_like( data_type, '*CHAR*' ) \n" + 
	        "                       then cast(data_length as varchar2(5)) \n" + 
	        "     else \n" +
	     	"       case \n" +
	        "         when data_scale is null then cast(data_precision as varchar2(5)) \n" + 
	        "         when data_scale =  0    then cast(data_precision as varchar2(5)) \n" +
	        "         else                         data_precision||','||data_scale \n"     +
	        "       end \n"                    +
			"   end data_size, \n"             +
			"   case nullable  \n"             +
			"     when 'Y' then 'NULL OK'  \n" +
			"     else          'NULL NG'  \n" +
			"   end nullable,  \n"             +
			"   data_default   \n"             +
			" from             \n"             + 
			"   all_tab_columns  \n"           +
			" where            \n"             +
			"   owner = '" + schemaName + "' \n"     +
			"   and              \n"             +
			"   table_name = '" + viewName + "' \n" + 
			" order by column_id";
		return sql;
	}
	
}
