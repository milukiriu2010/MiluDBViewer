package milu.db.access;

import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecSQLExplainSQLServer extends ExecSQLAbstract 
{
	// https://qiita.com/ota-meshi/items/6a0dc20f2aca6746047a
	@Override
	public void exec(int checkCnt) 
		throws 
			SQLException, 
			Exception 
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			stmt = this.myDBAbs.createStatement();
			// SQL only
			//stmt.executeUpdate( "SET SHOWPLAN_TEXT ON" );
			// 1 line
			//stmt.executeUpdate( "SET SHOWPLAN_XML ON" );
			// best
			stmt.executeUpdate( "SET SHOWPLAN_ALL ON" );
			
			rs = stmt.executeQuery( this.sqlBag.getSQL() );
			
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
			
			System.out.println( "--- SHOW PLAN --------------------" );
			while ( rs.next() )
			{
				List<String> dataRow = new ArrayList<String>();
				for ( int i = 1; i <= headCnt; i++ )
				{
					String colName = this.colNameLst.get( i-1 );
					dataRow.add( rs.getString( colName ) );
				}
				dataLst.add( dataRow );
			}
		}
		finally
		{
			try
			{
				if ( stmt != null )
				{
					//stmt.executeUpdate( "SET SHOWPLAN_TEXT OFF" );
					//stmt.executeUpdate( "SET SHOWPLAN_XML OFF" );
					stmt.executeUpdate( "SET SHOWPLAN_ALL OFF" );
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
		}
	}

}
