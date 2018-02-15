package db;

import java.sql.*; 

public class SQLServerCon {
	public static void main(String args[]){  
		try{  
		//step1 load the driver class
		System.out.println( "step1" );
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
		  
		//step2 create  the connection object
		System.out.println( "step2" );
		Connection con=DriverManager.getConnection(  
				"jdbc:sqlserver://localhost:1433;" +
				"databaseName=miludb;user=milu;password=milu");  
		  
		//step3 create the statement object
		System.out.println( "step3" );
		Statement stmt=con.createStatement();  
		  
		//step4 execute query
		System.out.println( "step4" );
		ResultSet rs=stmt.executeQuery("select * from m_npb_team_list");  
		while(rs.next())  
		{
			//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			System.out.println(rs.getString("id"));
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
