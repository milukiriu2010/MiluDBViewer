package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class IndexDBOracle extends IndexDBAbstract 
{
	public IndexDBOracle( MyDBAbstract myDBAbs )
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
			" select \n" + 
			"   aic.column_name  column_name \n" + 
			" from \r\n" + 
			"   all_indexes      ai,  \n" + 
			"   all_ind_columns  aic  \n" + 
			" where \r\n" + 
			"   ai.owner = '" + schemaName + "' \n" + 
			"   and \n" + 
			"   ai.table_name = '" + tableName + "' \n" + 
			"   and \n" + 
			"   ai.index_name = '" + indexName + "' \n" + 
			"   and \n" + 
			"   ai.owner = aic.index_owner \n" + 
			"   and \n" + 
			"   ai.table_name = aic.table_name \n" + 
			"   and \n" + 
			"   ai.index_name = aic.index_name \n" + 
			"order by aic.column_name"; 
		return sql;
	}

}
