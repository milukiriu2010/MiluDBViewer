package milu.db.index;

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Map;
import java.util.HashMap;

import milu.db.MyDBAbstract;

public class IndexDBPostgres 
	extends IndexDBAbstract
{
	public IndexDBPostgres( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}
	
	@Override
	public void selectColumnLst( String schemaName, String tableName, String indexName ) throws SQLException
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
			
			
			/*
			// Get Column Attribute
			ResultSetMetaData rsmd = rs.getMetaData();
			// Result ColumnName
			int headCnt = rsmd.getColumnCount();
			System.out.println( "---DB COLUMN----------------" );
			for ( int i = 1; i <= headCnt; i++ )
			{
				this.colNameLst.add( rsmd.getColumnName( i ) );
				// ----------------------------------------------------------
				// [Oracle XMLType Column] 
				//   ColumTypeName   => SYS.XMLTYPE
				//   ColumnClassName => oracle.jdbc.OracleOpaque
				//                      java.sql.SQLXML( by xmlparser2.jar )
				// ----------------------------------------------------------
				System.out.println
				( 
					rsmd.getColumnName( i )     + ":" + 
					rsmd.getColumnTypeName( i ) + ":" +
					rsmd.getColumnClassName( i )
				);
			}
			*/
			
			while ( rs.next() )
			{
				// --------------------------------------------
				// split column list into each column
				// --------------------------------------------
				// ex1. {column1}
				//        => column1
				// ex2. {column1,column2}
				//        => column1 column2
				// ex3. {countrycode,language}
				//        => countrycode, language
				// --------------------------------------------
				String strColumns = rs.getString(1);
				strColumns = strColumns.replace( "{",  "" );
				strColumns = strColumns.replace( "}",  "" );
				String[] arrayColumns = strColumns.split(",");
				for ( int i = 0; i < arrayColumns.length; i++ )
				{
					Map<String, String> mapColumn = new HashMap<String,String>();
					mapColumn.put( "columnName", arrayColumns[i] );
					this.dataLst.add( mapColumn );
				}
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
	
	/**
	 * 
select
  array
  (
  select
    pg_get_indexdef(i.indexrelid, k+1, TRUE )
  from
    generate_subscripts(i.indkey,1) as k
  order by k
  )
from
  pg_index  i  join
  pg_class  c on c.oid = i.indexrelid  join
  pg_namespace n on n.oid = c.relnamespace
where
  n.nspname = 'public'
  and
  i.indrelid = 'countrylanguage'::REGCLASS
  and
  c.relname = 'countrylanguage_pkey'
	 */
	@Override
	protected String columnLstSQL( String schemaName, String tableName, String indexName )
	{
		String sql =
			"select \n"  +
			"  array \n" +
			"  ( \n"     +
			"    select \n" +
			"      pg_get_indexdef(i.indexrelid, k+1, TRUE ) \n" +
			"    from \n" +
			"      generate_subscripts(i.indkey,1) as k \n" +
			"    order by k \n" +
			"  ) column_lists\n" +
			"from \n" +
			"  pg_index  i  join \n" +
			"  pg_class  c on c.oid = i.indexrelid  join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n"  +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  i.indrelid = '" + tableName + "'::REGCLASS \n" +
			"  and \n" +
			"  c.relname = '" + indexName + "'";
		return sql;
	}

}
