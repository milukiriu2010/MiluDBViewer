package milu.db.obj.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableDBMySQL extends TableDBAbstract 
{

	@Override
	protected String listSQL( String schemaName ) 
	{
		String sql = 
			" select \n" +
			"	table_name   \n" +
			" from \n" +
			"   information_schema.tables \n" +
			" where \n" +
			"   table_type='BASE TABLE' \n" +
			"   and \n" +
			"   table_schema = '" + schemaName + "' \n" +
			" order by table_name";
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
			"   table_name = '" + tableName + "' \n"     + 
			" order by ordinal_position";
		return sql;
	}

}
