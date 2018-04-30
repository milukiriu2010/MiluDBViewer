package milu.db.obj.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableDBSQLite extends TableDBAbstract 
{

	@Override
	protected String listSQL( String schemaName ) 
	{
		String sql = 
			" select \n" +
			"	name   \n" +
			" from \n" +
			"   sqlite_master \n" +
			" where \n" +
			"   type='table' \n" +
			" order by name";
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
				dataRow.put( "column_name"   , rs.getString("name") );
				dataRow.put( "data_type"     , rs.getString("type") );
				dataRow.put( "data_size"     , null );
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
				dataRow.put( "data_default"  , rs.getString("dflt_value") );
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
			" pragma table_info(" + tableName + ")";
		return sql;
	}

}
