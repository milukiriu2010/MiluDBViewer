package db;

import java.sql.*; 

// https://www.javatpoint.com/example-to-connect-to-the-mysql-database
public class MySQLCon {
	public static void main(String args[]){  
		try{  
		//step1 load the driver class
		System.out.println( "step1" );
		Class.forName("com.mysql.jdbc.Driver");
		
		  
		//step2 create  the connection object
		System.out.println( "step2" );
		Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/sakila","milu","milu");  
		  
		//step3 create the statement object
		System.out.println( "step3" );
		Statement stmt=con.createStatement();  
		  
		//step4 execute query
		System.out.println( "step4" );
		ResultSet rs=stmt.executeQuery("select * from actor");  
		while(rs.next())  
		{
			//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			System.out.println(
					rs.getString("actor_id") + ":" +
					rs.getString("first_name") + ":" +
					rs.getString("last_name") + ":" +
					rs.getString("last_update")
					);
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
