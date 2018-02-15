package db;

import java.sql.*; 

public class CassandraCon {
	public static void main( String[] args )
	{
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			Class.forName("com.github.cassandra.jdbc.CassandraDriver");
			
			  
			//step2 create  the connection object
			System.out.println( "step2" );
			//Connection con=DriverManager.getConnection(  
			//		"jdbc:c*:datastax://localhost/system_auth?consistencyLevel=ONE","cassandra","cassandra");
			//con.setSchema("videodb");
			//Connection con=DriverManager.getConnection(  
			//		"jdbc:c*:datastax://localhost/videodb","cassandra","cassandra");
			//Connection con=DriverManager.getConnection(  
			//		"jdbc:c*:datastax://localhost:9160/videodb","cassandra","cassandra");
			//Connection con=DriverManager.getConnection(  
			//		"jdbc:c*:datastax://localhost:9160/videodb?consistencyLevel=ONE","cassandra","cassandra");
			Connection con=DriverManager.getConnection(  
					"jdbc:c*:datastax://127.0.0.1:9042/videodb","cassandra","cassandra");
			  
			//step3 create the statement object
			System.out.println( "step3" );
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
			System.out.println( "step4" );
			ResultSet rs=stmt.executeQuery("select * from users");  
			while(rs.next())  
			{
				System.out.println(rs.getString(1)+"  "+rs.getString(3)+"  "+rs.getString(4));
				//System.out.println(rs.getString(1));
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
