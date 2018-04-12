package milu.db.obj.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableDBCassandra extends TableDBAbstract 
{

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n"           +
			"   table_name     \n" +
			" from \n"             +
			"   system_schema.tables \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
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
				dataRow.put( "data_type"     , rs.getString("type") );
				dataRow.put( "data_size"     , null );
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
	
	// SQL for Table Definition
	@Override
	protected String definitionSQL( String schemaName, String tableName )
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
			"   table_name = '" + tableName + "'";
		return sql;
	}
	
}
