package db;

import java.sql.*; 

public class OracleCon {
	public static void main(String args[]){  
		if ( args.length < 4 )
		{
			System.out.println( "Usage: db.OracleCon [user] [pass] [sid] [host]" );
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
		ResultSet rs=stmt.executeQuery("select * from user_tables order by table_name");  
		while(rs.next())  
		{
			//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			System.out.println(rs.getString("TABLE_NAME"));
		}
		  
		//step5 close the connection object
		System.out.println( "step5" );
		con.close();  
		  
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}  

	}  
}
