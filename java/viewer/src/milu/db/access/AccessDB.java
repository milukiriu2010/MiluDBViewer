package milu.db.access;

import java.util.List;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

import milu.db.MyDBAbstract;

public class AccessDB
{
	// DB Access Object
	private MyDBAbstract  myDBAbs = null;
	
	// Column Name List
	List<String>  colNameLst      = new ArrayList<String>();
	// Column Type Name List
	List<String>  colTypeNameLst  = new ArrayList<String>();
	// Column Class Name List
	List<String>  colClassNameLst = new ArrayList<String>();
	
	// Data List
	List<List<String>> dataLst    = new ArrayList<List<String>>();
	
	public AccessDB( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public List<String> getColNameLst()
	{
		return this.colNameLst;
	}
	
	public List<String> getColTypeNameLst()
	{
		return this.colTypeNameLst;
	}
	
	public List<String> getColClassNameLst()
	{
		return this.colClassNameLst;
	}
	
	public List<List<String>> getDataLst()
	{
		return this.dataLst;
	}
	
	private void clear()
	{
		this.colNameLst.clear();
		this.colTypeNameLst.clear();
		this.colClassNameLst.clear();
		this.dataLst.clear();
	}
	
	public void select( String sql, final int fetchMax )
		throws 
			SQLException,
			MyDBOverFetchSizeException,
			Exception
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		Exception lastEx = null;
		
		try
		{
			this.clear();
			
			stmt = this.myDBAbs.createStatement();
			rs   = stmt.executeQuery( sql );
			
			// Get Column Attribute
			// https://examples.javacodegeeks.com/core-java/sql/resultsetmetadata/java-sql-resultsetmetadata-example/
			ResultSetMetaData rsmd = rs.getMetaData();
			// Result ColumnName
			int headCnt = rsmd.getColumnCount();
			System.out.println( "---DB COLUMN----------------" );
			for ( int i = 1; i <= headCnt; i++ )
			{
				this.colNameLst.add( rsmd.getColumnName( i ) );
				this.colTypeNameLst.add( rsmd.getColumnTypeName( i ) );
				this.colClassNameLst.add( rsmd.getColumnClassName( i ) );
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
			System.out.println( "----------------------------" );
			
			// Fetch Count
			int fetchCnt = 0;
			while ( rs.next() )
			{
				if ( fetchCnt >= fetchMax )
				{
					throw new MyDBOverFetchSizeException();
				}
				List<String> dataRow = new ArrayList<String>();
				for ( int i = 1; i <= headCnt; i++ )
				{
					String colName = this.colNameLst.get( i-1 );
					try
					{
						dataRow.add( rs.getString( colName ) );
					}
					catch ( Exception ex )
					{
						lastEx = ex;
						StringWriter sw = new StringWriter();
						PrintWriter  pw = new PrintWriter(sw);
						ex.printStackTrace(pw);
						String strEx = sw.toString();
						
						// put Exception String on Cell 
						String[] strExSplit = strEx.split( "\n" );
						dataRow.add( strExSplit[0] );
					}
				}
				dataLst.add( dataRow );
				fetchCnt++;
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
			
			if ( lastEx != null )
			{
				throw lastEx;
			}
			
		}
	}
	
	public int transaction( String sql, final int rowNum )
		throws SQLException
	{
		try
		(
			Statement stmt   = this.myDBAbs.createStatement();
		)
		{
			return stmt.executeUpdate(sql);
		}
	}
}
