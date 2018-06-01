package milu.db.access;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecSQLExplainPostgres extends ExecSQLAbstract 
{

	@Override
	public void exec(int checkCnt) throws SQLException, Exception 
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			Boolean explainAnalyze = this.appConf.getPostgresExplainAnalyze();
			Boolean explainVerbose = this.appConf.getPostgresExplainVerbose();
			Boolean explainCosts   = this.appConf.getPostgresExplainCosts();
			Boolean explainBuffers = this.appConf.getPostgresExplainBuffers();
			Boolean explainTiming  = this.appConf.getPostgresExplainTiming();
			String  explainFormat  = this.appConf.getPostgresExplainFormat();
			
			stmt = this.myDBAbs.createStatement();
			String sqlExplain = 
					"explain " +
					"(" +
					"analyze " + explainAnalyze + "," +
					"verbose " + explainVerbose + "," +
					"costs "   + explainCosts   + "," +
					"buffers " + explainBuffers + "," +
					"timing "  + explainTiming  + "," +
					"format "  + explainFormat  + 
					")" + 
					this.sqlBag.getSQL();
			System.out.println( " -- explain(postgres) -------------" );
			System.out.println( sqlExplain );
			System.out.println( " ----------------------------------" );
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
				List<Object> dataRow = new ArrayList<>();
				for ( int i = 1; i <= headCnt; i++ )
				{
					Object colName = this.colNameLst.get( i-1 );
					dataRow.add( rs.getString( (String)colName ) );
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
			
			this.execEndTime = System.nanoTime();
		}
	}

}
