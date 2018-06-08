package db.elasticsearch;

import java.sql.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Paths;

import abc.DriverShim;

// https://github.com/adejanovski/cassandra-jdbc-wrapper
// http://www.ne.jp/asahi/hishidama/home/tech/java/DriverManager.html#h_connect
// http://www.ne.jp/asahi/hishidama/home/tech/java/DriverManager.html
public class ElasticConDynamic {
	public static void main( String[] args )
	{
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			//Class.forName("com.github.cassandra.jdbc.CassandraDriver");
			//Class.forName("com.github.adejanovski.cassandra.jdbc.CassandraDriver");
			/**/
			String driverPath1 = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\elasticsearch\\sql4es-0.9.2.4.jar";
			URL url1 = Paths.get(driverPath1).toUri().toURL();
			System.out.println( url1 );
			String driverPath2 = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\elasticsearch\\elasticsearch-6.2.4.jar";
			URL url2 = Paths.get(driverPath2).toUri().toURL();
			URL[] urls = { url1, url2 };
			URLClassLoader loader =	new URLClassLoader( urls );
			Driver d = 
					(Driver)Class.forName
					(
						"nl.anchormen.sql4es.jdbc.ESDriver", 
						true, 
						loader
					).getDeclaredConstructor().newInstance();
			DriverManager.registerDriver(new DriverShim(d) );
			
			/*
	        Enumeration<Driver> drivers = DriverManager.getDrivers();
	        while(drivers.hasMoreElements()){
	            Driver driver = drivers.nextElement();
	            System.out.println("driver:"+driver);
	            DriverPropertyInfo[] driverPropInfoLst = driver.getPropertyInfo( "jdbc:sql4es://",null);
	            for ( DriverPropertyInfo  driverPropInfo : driverPropInfoLst )
	            {
	            	System.out.println("  DriverPropertyInfo:name["+ driverPropInfo.name +"]value["+ driverPropInfo.value +"]");
	            }
	        }
	        */			
			  
			//step2 create  the connection object
			System.out.println( "step2" );
			Connection con=DriverManager.getConnection(  
					"jdbc:sql4es://10.100.93.50:9300/bank","","");
			/*
			Connection con=DriverManager.getConnection(  
					"jdbc:sql4es://10.100.93.50:9300/bank?cluster.name=milu","","");
					*/
			/*
			Connection con=DriverManager.getConnection(  
					"jdbc:sql4es://10.100.93.50:9200/bank?cluster.name=milu","","");
					*/
			/*
			Connection con=DriverManager.getConnection(  
					"jdbc:sql4es://10.100.93.50:9200/customer","","");
					*/
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
			ResultSet rs=stmt.executeQuery("select * from account");  
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
			//System.out.println(e);
			e.printStackTrace();
		}  
	}

}
