package milu.db.explain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplainDBOracle extends ExplainDBAbstract 
{
	@Override
	public void explain(String sql) throws SQLException 
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			
			stmt = this.myDBAbs.createStatement();
			String sqlExplain = "explain plan for " + sql;
			stmt.execute( sqlExplain );
			
			rs = stmt.executeQuery( "select plan_table_output from table(dbms_xplan.display())" );
			
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
			
			System.out.println( "--- PLAN_TABLE_OUTPUT --------------------" );
			boolean header = true;
			while ( rs.next() )
			{
				String planTableOutput = rs.getString(1);
				System.out.println( planTableOutput );
				if ( planTableOutput != null && planTableOutput.startsWith("|") )
				{
					String split[] = planTableOutput.split("\\|");
					if ( header == true )
					{
						this.colNameLst = Arrays.asList(split).subList( 1, split.length );
						header = false;
					}
					else
					{
						//this.dataLst.add( Arrays.asList(split).subList( 1, split.length ) );
						List<String> dataRow = new ArrayList<>();
						for ( int i = 1; i < split.length; i++ )
						{
							dataRow.add( split[i] );
						}
						this.dataLst.add( dataRow );
					}
				}
			}
			System.out.println( "------------------------------------------" );
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
