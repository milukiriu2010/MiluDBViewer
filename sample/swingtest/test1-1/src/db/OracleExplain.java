package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// https://stackoverflow.com/questions/4376329/jdbc-oracle-fetch-explain-plan-for-query
public class OracleExplain
{
	public static void main(String args[]){  
		if ( args.length < 4 )
		{
			System.out.println( "Usage: db.OracleExplain [user] [pass] [sid] [host]" );
			return;
		}
				
		
		try{  
		//step1 load the driver class
		System.out.println( "step1" );
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		  
		//step2 create  the connection object
		System.out.println( "step2" );
		//Connection con=DriverManager.getConnection(  
		//		"jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
		String url = "jdbc:oracle:thin:@" + args[3] + ":1521:" + args[2];
		Connection con=DriverManager.getConnection(url, args[0], args[1]);

		  
		//step3 create the statement object
		System.out.println( "step3" );
		Statement stmt=con.createStatement();  
		  
		//step4 execute query
		System.out.println( "step4" );
		//stmt.execute( "explain plan for select 1 from dual" );
		stmt.execute( "explain plan for select * from m_npb_team_list where id = 'HC'" );
		//stmt.execute( "explain plan for select * from user_tables order by table_name" );

		/**
Plan hash value: 2713427666
###
 
###
--------------------------------------------------------------------------------------------------
###
| Id  | Operation                   | Name               | Rows  | Bytes | Cost (%CPU)| Time     |
###
--------------------------------------------------------------------------------------------------
###
|   0 | SELECT STATEMENT            |                    |     1 |    59 |     1   (0)| 00:00:01 |
###
|   1 |  TABLE ACCESS BY INDEX ROWID| M_NPB_TEAM_LIST    |     1 |    59 |     1   (0)| 00:00:01 |
###
|*  2 |   INDEX UNIQUE SCAN         | PK_M_NPB_TEAM_LIST |     1 |       |     0   (0)| 00:00:01 |
###
--------------------------------------------------------------------------------------------------
###
 
###
Predicate Information (identified by operation id):
###
---------------------------------------------------
###
 
###		 
		 */
		System.out.println( "step5" );
		ResultSet rs=stmt.executeQuery("select plan_table_output from table(dbms_xplan.display())");  
		while(rs.next())  
		{
			System.out.println(rs.getString(1));
			System.out.println( "###" );
		}
		  
		//step6 close the connection object
		System.out.println( "step6" );
		con.close();  
		  
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}  

	}  	
}
