package db.oracle;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.*; 
import java.util.*;

import abc.DriverShim;

public class OracleConDynamic {
	public static void main(String args[]){  
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			System.setProperty( "oracle.net.tns_admin", "C:\\oracle\\instantclient_12_2\\network\\admin" );
			URL url = null;
			if ( args.length == 0 || args[0].equals("1") )
			{
				String driverPath = "C:\\myjava\\MiluDBViewer.git\\java\\viewer\\lib\\oracle\\ojdbc8.jar";
				url = Paths.get(driverPath).toUri().toURL();
			}
			else if ( args[0].equals("2") )
			{
				url = new URL("file:loader/oracle/ojdbc6.jar");
			}
			System.out.println( url );
			URL[] urls = { url };
			URLClassLoader loader =	new URLClassLoader( urls );
			Driver d = 
					(Driver)Class.forName
					(
						"oracle.jdbc.driver.OracleDriver", 
						true, 
						loader
					).getDeclaredConstructor().newInstance();
			DriverManager.registerDriver( new DriverShim(d) );
			
			
	        Enumeration<Driver> drivers = DriverManager.getDrivers();
	        while(drivers.hasMoreElements()){
	            Driver driver = drivers.nextElement();
	            System.out.println("driver:"+driver.toString()+":major["+driver.getMajorVersion()+"]minnor["+driver.getMinorVersion()+"]");
	        }			
			  
			//step2 create  the connection object
			System.out.println( "step2" );
			Connection con = null;
			String urljdbc = 
					"jdbc:oracle:thin:@ORCL";
			System.out.println( urljdbc );
			con = DriverManager.getConnection( urljdbc, "milu", "milu" );
			
			//step3 create the statement object
			System.out.println( "step3" );
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
			System.out.println( "step4" );
			ResultSet rs=stmt.executeQuery("select * from user_tables where rownum <= 1");
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
