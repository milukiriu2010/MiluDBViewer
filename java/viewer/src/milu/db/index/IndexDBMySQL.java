package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class IndexDBMySQL extends IndexDBAbstract 
{
	public IndexDBMySQL( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectColumnLst(String schemaName, String tableName, String indexName) 
		throws SQLException 
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			
			stmt = this.myDBAbs.createStatement();
			
			String sql = this.columnLstSQL( schemaName, tableName, indexName );
			System.out.println( " -- selectColumnLst ---------------" );
			System.out.println( sql );
			System.out.println( " ----------------------------------" );
			rs   = stmt.executeQuery( sql );
						
			while ( rs.next() )
			{
				Map<String, String> mapColumn = new HashMap<String,String>();
				mapColumn.put( "columnName", rs.getString(1) );
				this.dataLst.add( mapColumn );
			}
		}
		finally
		{
			try
			{
				if ( stmt != null )
				{
					stmt.close();
				}
			}
			catch ( SQLException sqlEx1 )
			{
				// suppress close error
			}
			
			try
			{
				if ( rs != null )
				{
					rs.close();
				}
			}
			catch ( SQLException sqlEx2 )
			{
				// suppress close error
			}
			
			try
			{
				this.myDBAbs.rollback();
			}
			catch ( SQLException sqlEx3 )
			{
				// suppress rollback error
			}
		}
	}

	@Override
	protected String columnLstSQL(String schemaName, String tableName, String indexName) 
	{
		String sql =
			"select \n" + 
			"   column_name \n" + 
			" from \n" + 
			"   information_schema.statistics \n" + 
			" where \n" + 
			"    table_schema = '" + schemaName + "' \n" + 
			"    and \n" + 
			"    table_name = '" + tableName + "' \n" +
			"    and \n" +
			"    index_name = '" + indexName + "' \n" +
			" order by table_name,index_name,seq_in_index\r\n" + 
			"";
		return sql;
	}

}
