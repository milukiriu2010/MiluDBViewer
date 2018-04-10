package db.sqlite;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.*; 
import java.util.*;

public class SQLiteConDynamic {
	public static void main(String args[]){  
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			String driverPath = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\sqlite\\sqlite-jdbc-3.21.0.jar";
			URL url = Paths.get(driverPath).toUri().toURL();
			System.out.println( url );
			URL[] urls = { url };
			URLClassLoader loader =	new URLClassLoader( urls );
			Driver d = 
					(Driver)Class.forName
					(
						"org.sqlite.JDBC", 
						true, 
						loader
					).getDeclaredConstructor().newInstance();
			DriverManager.registerDriver( new DriverShim(d) );
			
			  
			//step2 create  the connection object
			System.out.println( "step2" );
			Connection con = null;
			String urljdbc = null;
			if ( args.length == 0 || args[0].equals("1") )
			{
				urljdbc =  "jdbc:sqlite:C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\sql\\sqlite\\ex1.db";
				System.out.println( urljdbc );
				con = DriverManager.getConnection( urljdbc );
			}
			else if ( args[0].equals("2") )
			{
				urljdbc =   
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=miludb;integratedSecurity=true;";  
				System.out.println( urljdbc );
				con = DriverManager.getConnection( urljdbc );
			}
			else if ( args[0].equals("3") )
			{
				urljdbc =   
						"jdbc:sqlserver://localhost:1433;" +
						"databaseName=miludb;";  
				System.out.println( urljdbc );
				Properties prop = new Properties();
				prop.setProperty("user", "milu");
				prop.setProperty("password", "milu");
				con = DriverManager.getConnection( urljdbc,prop );
			}
			
			//step3 create the statement object
			System.out.println( "step3" );
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
			System.out.println( "step4" );
			ResultSet rs=stmt.executeQuery("select * from sqlite_master");
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
