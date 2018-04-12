package milu.db.obj.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableDBOracle extends TableDBAbstract 
{

	@Override
	protected String listSQL( String schemaName )
	{
		String sql = 
			" select object_name, status from all_objects \n" +
			" where \n" +
			" object_type='TABLE'      \n" +
			" and \n" +
			" owner = '" + schemaName + "' \n" +
			" order by owner, object_name";
		return sql;
	}

	// select Table Definition
	@Override
	public List<Map<String,String>> selectDefinition( String schameName, String tableName ) throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schameName, tableName );
		
		System.out.println( " -- selectDefinition(Table) ------------" );
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
	protected String definitionSQL( String schemaName, String tableName )
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
			"   table_name = '" + tableName + "' \n" + 
			" order by column_id";
		return sql;
	}
}
