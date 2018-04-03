package db.sqlserver;

import java.sql.*; 
import java.util.*;

public class SQLServerCon {
	public static void main(String args[]){  
		try{  
		//step1 load the driver class
		System.out.println( "step1" );
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
		  
		//step2 create  the connection object
		System.out.println( "step2" );
		Connection con = null;
		String url = null;
		if ( args.length == 0 || args[0].equals("1") )
		{
			url =   
				"jdbc:sqlserver://localhost:1433;" +
				"databaseName=miludb;user=milu;password=milu";  
			System.out.println( url );
			con = DriverManager.getConnection( url );
		}
		else if ( args[0].equals("2") )
		{
			url =   
				"jdbc:sqlserver://localhost:1433;" +
				"databaseName=miludb;integratedSecurity=true;";  
			System.out.println( url );
			con = DriverManager.getConnection( url );
		}
		else if ( args[0].equals("3") )
		{
			url =   
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=miludb;";  
			System.out.println( url );
			Properties prop = new Properties();
			prop.setProperty("user", "milu");
			prop.setProperty("password", "milu");
			con = DriverManager.getConnection( url,prop );
		}
		
		//step3 create the statement object
		System.out.println( "step3" );
		Statement stmt=con.createStatement();  
		  
		//step4 execute query
		System.out.println( "step4" );
		ResultSet rs=stmt.executeQuery("select * from m_npb_team_list");
		//ResultSet rs=stmt.executeQuery("select * from information_schema.schemata");
		while(rs.next())  
		{
			System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			//System.out.println(rs.getString("id"));
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
