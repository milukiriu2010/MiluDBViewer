package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;

public class IndexDBCassandra extends IndexDBAbstract
{
	public IndexDBCassandra( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectColumnLst(String schemaName, String tableName, String indexName) throws SQLException 
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
				String kind     = rs.getString( "kind" );
				if ( indexName.equals( kind ) == false )
				{
					continue;
				}
				
				String position = rs.getString( "position" );
				int pos = Integer.parseInt( position );
				
				Map<String, String> mapColumn = new HashMap<String,String>();
				mapColumn.put( "columnName"     , rs.getString( "column_name" ) );
				mapColumn.put( "clusteringOrder", rs.getString( "clustering_order" ) );
				if ( pos > this.dataLst.size() )
				{
					pos = this.dataLst.size();
				}
				this.dataLst.add( pos, mapColumn );
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
			" select         \n" +
		    "   column_name, \n" +
		    "   kind,        \n" +
		    "   position,    \n" +
		    "   clustering_order \n" +
			" from           \n" +
			"   system_schema.columns \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			"   and \n" +
			"   table_name = '" + tableName + "'";
		return sql;
	}

}
