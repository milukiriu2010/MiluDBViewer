package db.cassandra;

import java.sql.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;

// https://github.com/adejanovski/cassandra-jdbc-wrapper
// http://www.ne.jp/asahi/hishidama/home/tech/java/DriverManager.html#h_connect
// http://www.ne.jp/asahi/hishidama/home/tech/java/DriverManager.html
public class CassandraConDynamic {
	public static void main( String[] args )
	{
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			//Class.forName("com.github.cassandra.jdbc.CassandraDriver");
			//Class.forName("com.github.adejanovski.cassandra.jdbc.CassandraDriver");
			/**/
			String driverPath = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\cassandra\\cassandra-jdbc-wrapper-3.1.0.jar";
			URL url = Paths.get(driverPath).toUri().toURL();
			System.out.println( url );
			URL[] urls = { url };
			URLClassLoader loader =	new URLClassLoader( urls );
			Driver d = 
					(Driver)Class.forName
					(
						"com.github.adejanovski.cassandra.jdbc.CassandraDriver", 
						true, 
						loader
					).getDeclaredConstructor().newInstance();
			DriverManager.registerDriver(new DriverShim(d) );
			
	        Enumeration<Driver> drivers = DriverManager.getDrivers();
	        while(drivers.hasMoreElements()){
	            Driver driver = drivers.nextElement();
	            System.out.println("driver:"+driver);
	            DriverPropertyInfo[] driverPropInfoLst = driver.getPropertyInfo( "",null);
	            for ( DriverPropertyInfo  driverPropInfo : driverPropInfoLst )
	            {
	            	System.out.println("  DriverPropertyInfo:name["+ driverPropInfo.name +"]value["+ driverPropInfo.value +"]");
	            }
	        }			
			  
			//step2 create  the connection object
			System.out.println( "step2" );
			Connection con=DriverManager.getConnection(  
					"jdbc:cassandra://127.0.0.1:9042/videodb","cassandra","cassandra");
			/*
			Driver driver = cd.newInstance();
			DriverManager.registerDriver(driver);
			
			Properties prop = new Properties();
			prop.setProperty("user", "cassandra");
			prop.setProperty("password", "cassandra");
			Connection con = driver.connect("jdbc:c*:datastax://127.0.0.1:9042/videodb", prop );
			*/
			  
			//step3 create the statement object
			System.out.println( "step3" );
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
			System.out.println( "step4" );
			ResultSet rs=stmt.executeQuery("select * from users");  
			while(rs.next())  
			{
				System.out.println(
						rs.getString(1)+"  "+
						rs.getString(2)+"  "+
						rs.getString(3)+"  "+
						rs.getString(4) );
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
