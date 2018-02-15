package milu.db.explain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.db.MyDBAbstract;

public class ExplainDBMySQL extends ExplainDBAbstract 
{
	public ExplainDBMySQL( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void explain(String sql) throws SQLException
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			
			stmt = this.myDBAbs.createStatement();
			String sqlExplain = "explain " + sql;
			rs   = stmt.executeQuery( sqlExplain );
			
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
		}
	}

}
