package milu.db.access;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecSQLSelect extends ExecSQLAbstract 
{
	// Column Type Name List
	List<String>  colTypeNameLst  = new ArrayList<String>();
	// Column Class Name List
	List<String>  colClassNameLst = new ArrayList<String>();
	
	@Override
	protected void clear()
	{
		super.clear();
		
		this.colTypeNameLst.clear();
		this.colClassNameLst.clear();
	}	
	
	@Override
	public void exec(int checkCnt)		
		throws 
			SQLException,
			MyDBOverFetchSizeException,
			Exception 
	{
		System.out.println( "select:" + this.sqlBag.getSQL() );
		Statement stmt   = null;
		ResultSet rs     = null;
		Exception lastEx = null;
		
		try
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			stmt = this.myDBAbs.createStatement();
			rs   = stmt.executeQuery( this.sqlBag.getSQL() );
			
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
				if ( fetchCnt >= checkCnt )
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
		catch ( SQLException sqlEx )
		{
			try
			{
				this.myDBAbs.processAfterException();
			}
			catch ( SQLException sqlEx0 )
			{
				// suppress close error
			}
			
			throw sqlEx;
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
			
			this.execEndTime = System.nanoTime();
			
			if ( lastEx != null )
			{
				throw lastEx;
			}
		}
	}
}
