package db;

import java.sql.*;

// http://www.postgresqltutorial.com/postgresql-jdbc/connecting-to-postgresql-database/
// https://www.tutorialspoint.com/postgresql/postgresql_java.htm
public class PostgresCon {
	public static void main(String args[]) {
		try {
			//step1 load the driver class
			System.out.println( "step1" );
		    Class.forName("org.postgresql.Driver");
		    
			//step2 create  the connection object
			System.out.println( "step2" );
		    Connection con = 
		    	DriverManager.getConnection("jdbc:postgresql://localhost:5432/miludb", "milu", "milu");
		    
			//step3 create the statement object
			System.out.println( "step3" );
		    Statement stmt=con.createStatement();  
				  
			//step4 execute query
			System.out.println( "step4" );
			//ResultSet rs=stmt.executeQuery("select table_name from infromation_schema.tables order by table_name");  
			ResultSet rs=stmt.executeQuery("select * from m_npb_team_list order by id");  
			while(rs.next())  
			{
				//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
				System.out.println(rs.getString("ID")+ " " + rs.getString("HEAD_NAME") );
			}
				  
			//step5 close the connection object
			System.out.println( "step5" );
			con.close();  

		} catch (Exception e) {
		     e.printStackTrace();
		     System.err.println(e.getClass().getName()+": "+e.getMessage());
		     System.exit(0);
		}
		System.out.println("Opened database successfully");
	}
}
