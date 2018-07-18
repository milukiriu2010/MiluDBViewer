package milu.db.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecSQLExplainOracle extends ExecSQLAbstract 
{

	@Override
	public void exec( final int checkCnt, final int fetchPos ) 
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
			String sqlExplain = "explain plan for " + this.sqlBag.getSQL();
			stmt.execute( sqlExplain );
			
			rs = stmt.executeQuery( "select plan_table_output from table(dbms_xplan.display())" );
			
			
			System.out.println( "--- PLAN_TABLE_OUTPUT --------------------" );
			// Predicate Information
			// id <=> filter, access ... etc
			// --------------------------------------------------------------------------------------------------------
			// | Id  | Operation                       | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
			// --------------------------------------------------------------------------------------------------------
			// |   0 | SELECT STATEMENT                |                      |     1 |   124 |   242   (1)| 00:00:03 |
			// |   1 |  SORT GROUP BY                  |                      |     1 |   124 |   242   (1)| 00:00:03 |
			// |*  2 |   FILTER                        |                      |       |       |            |          |
			// |   3 |    NESTED LOOPS OUTER           |                      |     1 |   124 |   241   (0)| 00:00:03 |
			// |   4 |     NESTED LOOPS                |                      |     1 |    98 |   236   (0)| 00:00:03 |
			// |*  5 |      FILTER                     |                      |       |       |            |          |
			// |*  6 |       HASH JOIN RIGHT OUTER     |                      |    20 |  1180 |   136   (0)| 00:00:02 |
			// |*  7 |        TABLE ACCESS FULL        | IPOE_ORDER_HISTORY   |  1978 | 55384 |    68   (0)| 00:00:01 |
			// |*  8 |        TABLE ACCESS FULL        | IPOE_ORDER_HISTORY   |  1978 | 61318 |    68   (0)| 00:00:01 |
			// |*  9 |      TABLE ACCESS BY INDEX ROWID| IPOE_DATA_HISTORY    |     1 |    39 |     5   (0)| 00:00:01 |
			// |* 10 |       INDEX RANGE SCAN          | I1_IPOE_DATA_HISTORY |     2 |       |     2   (0)| 00:00:01 |
			// |* 11 |     TABLE ACCESS BY INDEX ROWID | IPOE_DATA_HISTORY    | 60833 |  1544K|     5   (0)| 00:00:01 |
			// |* 12 |      INDEX RANGE SCAN           | I1_IPOE_DATA_HISTORY |     2 |       |     2   (0)| 00:00:01 |
			// --------------------------------------------------------------------------------------------------------
			// 
			// Predicate Information (identified by operation id):
			// 	---------------------------------------------------
			// 	 
			// 	   2 - filter("IDH2"."DATA_NO" IS NULL)
			// 	   5 - filter("IOH2"."DATA_NO" IS NULL)
			// 	   6 - access("IOH1"."ORDER_NO"="IOH2"."ORDER_NO"(+))
			// 	       filter("IOH1"."BACKUP_DATE"<"IOH2"."BACKUP_DATE"(+))
			// 	   7 - filter("IOH2"."BACKUP_DATE"(+)<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss'))
			// 	   8 - filter("IOH1"."BACKUP_DATE"<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss'))
			// 	   9 - filter("IDH1"."BACKUP_DATE"<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss') 
			// 	              AND ("IDH1"."ORDER_NO_CLOSE"="IOH1"."ORDER_NO" OR "IDH1"."ORDER_NO_OPEN"="IOH1"."ORDER_NO" AND 
			// 	              "IDH1"."ORDER_NO_CLOSE"='999'))
			// 	  10 - access("IDH1"."DATA_NO"="IOH1"."DATA_NO")
			// 	  11 - filter("IDH2"."BACKUP_DATE"(+)<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss') 
			// 	              AND "IDH1"."BACKUP_DATE"<"IDH2"."BACKUP_DATE"(+))
			// 	  12 - access("IDH1"."DATA_NO"="IDH2"."DATA_NO"(+))
			Map<String,String> preInfoMap = new LinkedHashMap<>(); 
			boolean header = true;
			boolean afterPredicate = false;
			String lastStrId = "";
			while ( rs.next() )
			{
				String planTableOutput = rs.getString(1);
				System.out.println( planTableOutput );
				if ( planTableOutput == null )
				{
					continue;
				}
				if ( planTableOutput.startsWith("|") )
				{
					String split[] = planTableOutput.split("\\|");
					if ( header == true )
					{
						this.colNameLst = new ArrayList<>(Arrays.asList(split).subList( 1, split.length ));
						header = false;
					}
					else
					{
						List<Object> dataRow = new ArrayList<>();
						for ( int i = 1; i < split.length; i++ )
						{
							dataRow.add( split[i] );
							// put "Id" into map
							//if ( i == 1 && split[i].startsWith("*") )
							if ( i == 1 )
							{
								String strId = split[i];
								strId = strId.replaceAll( "\\*?\\s*(\\d+)\\s*", "$1" );
								preInfoMap.put( strId, "" );
								//System.out.println( "strId["+strId+"]");
							}
						}
						this.dataLst.add( dataRow );
					}
				}
				// check "Predicate Information"
				else if ( planTableOutput.startsWith("Predicate Information") )
				{
					afterPredicate = true;
				}
				// check After "Predicate Information"
				else if ( afterPredicate == true )
				{
					String strSrc = planTableOutput;
					String strKey = strSrc.replaceAll( "^\\s*(\\d+)\\s+-\\s*.*", "$1" );
					String strVal = strSrc.replaceAll( "^\\s*\\d+\\s+-\\s*(.*)", "$1" );
					//System.out.println( "afterPredicate:strKey["+strKey+"]strVal["+strVal+"]");
					// ----------------------------------------------------------------
					//      6 - access("IOH1"."ORDER_NO"="IOH2"."ORDER_NO"(+))
					// 	        filter("IOH1"."BACKUP_DATE"<"IOH2"."BACKUP_DATE"(+))
					// ----------------------------------------------------------------
					// match
					//      6 - access("IOH1"."ORDER_NO"="IOH2"."ORDER_NO"(+))
					// ----------------------------------------------------------------
					if ( ( strKey.equals(planTableOutput) == false ) && strKey.matches("^\\d+$") )
					{
						preInfoMap.put( strKey, strVal );
						lastStrId = strKey;
					}
					// ----------------------------------------------------------------
					// match
					// 	        filter("IOH1"."BACKUP_DATE"<"IOH2"."BACKUP_DATE"(+))
					// ----------------------------------------------------------------
					else if ( ( strKey.equals(planTableOutput) == true ) && ( "".equals(lastStrId) == false ) )
					{
						String strTmp = preInfoMap.get( lastStrId );
						preInfoMap.put( lastStrId, strTmp + "\n" + strVal );
					}
				}
			}
			//System.out.println( "------------------------------------------" );
			//preInfoMap.forEach( (k,v)->System.out.println( "preInfoMap:k["+k+"]v["+v+"]") );
			//System.out.println( "------------------------------------------" );
			
			this.colNameLst.add("Predicate Information");
			this.dataLst.forEach
			( 
				(dataRow)->
				{
					Object strId = dataRow.get(0);
					strId = strId.toString().replaceAll( "\\*?\\s*(\\d+)\\s*", "$1" );
					dataRow.add( preInfoMap.get(strId) );
				}
			);
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
