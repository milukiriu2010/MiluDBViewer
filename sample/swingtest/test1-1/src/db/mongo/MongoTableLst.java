package db.mongo;

import java.sql.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

import abc.DriverShim;

public class MongoTableLst 
{
	public static void main( String[] args )
	{
		try{  
			//step1 load the driver class
			System.out.println( "step1" );
			/**/
			String driverPath1 = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\mongo\\mongo-java-driver-3.0.3.jar";
			URL url1 = Paths.get(driverPath1).toUri().toURL();
			System.out.println( url1 );
			String driverPath2 = "C:\\myjava\\MiluDBViewer.git\\sample\\swingtest\\test1-1\\loader\\mongo\\unityjdbc.jar";
			URL url2 = Paths.get(driverPath2).toUri().toURL();
			URL[] urls = { url1, url2 };
			URLClassLoader loader =	new URLClassLoader( urls );
			Driver d = 
					(Driver)Class.forName
					(
						"mongodb.jdbc.MongoDriver", 
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
					//"jdbc:mongo://127.0.0.1:27017/test","","");
					"jdbc:mongo://10.100.93.50:27017/test","","");
			  
			//step3 create the statement object
			System.out.println( "step3" );
			//Statement stmt=con.createStatement();
			DatabaseMetaData md = con.getMetaData();
			  
			//step4 execute query
			System.out.println( "step4" );
			/*
			ResultSet rs=stmt.executeQuery("select * from restaurants");  
			while(rs.next())  
			{
				System.out.println(
						rs.getString(1)+"  "+
						rs.getString(2)+"  "+
						rs.getString(3)+"  "+
						rs.getString(4) );
				//System.out.println(rs.getString(1));
			}
			*/
			
			System.out.println( "=== Schema ================" );
			ResultSet rs0 = md.getSchemas();
			while ( rs0.next() )
			{
				System.out.println( rs0.getString("TABLE_SCHEM") );
			}
			
			
			List<String> tableLst = new ArrayList<>();
			ResultSet rs = md.getTables(null, "test", "%", null);
			ResultSetMetaData rsmd = rs.getMetaData();
			while ( rs.next() )
			{
				System.out.println( "===================" );
				for ( int i = 1; i < rsmd.getColumnCount(); i++ )
				{
					System.out.println( i + ":" + rsmd.getColumnName(i) + ":" + rs.getString(i));
				}
				tableLst.add( rs.getString("TABLE_NAME") );
			}
			
			System.out.println( "=== columnName ================" );
			for ( String tableName : tableLst )
			{
				System.out.println( "    === " + tableName + " ================" );
				//ResultSet rs2 = md.getColumns(null, "test", tableName, "%" );
				ResultSet rs2 = md.getColumns(null, null, tableName, "%" );
				ResultSetMetaData rsmd2 = rs2.getMetaData();
				while ( rs2.next() )
				{
					System.out.println( "+++++++++++++++++++++++++" );
					for ( int j = 1; j < rsmd2.getColumnCount(); j++ )
					{
						System.out.println( "    " + j + ":" + rsmd2.getColumnName(j) + ":" + rs2.getObject(rsmd2.getColumnName(j)) );
						//System.out.println( "    " + j + ":" + rsmd2.getColumnName(j) + ":" );
					}
				}
				rs2.close();
			}
			  
			System.out.println( "=== primaryKeys ================" );
			for ( String tableName : tableLst )
			{
				ResultSet rs3 = md.getPrimaryKeys(null, "test", tableName );
				ResultSetMetaData rsmd3 = rs3.getMetaData();
				while ( rs3.next() )
				{
					System.out.println( "+++++++++++++++++++++++++" );
					for ( int j = 1; j < rsmd3.getColumnCount(); j++ )
					{
						System.out.println( "        " + j + ":" + rsmd3.getColumnName(j) + ":" + rs3.getString(rsmd3.getColumnName(j)) );
					}
				}
				rs3.close();
			}
			
			System.out.println( "=== indexInfo ================" );
			for ( String tableName : tableLst )
			{
				ResultSet rs4 = md.getIndexInfo(null, "test", tableName, false, false );
				ResultSetMetaData rsmd4 = rs4.getMetaData();
				while ( rs4.next() )
				{
					System.out.println( "+++++++++++++++++++++++++" );
					for ( int j = 1; j < rsmd4.getColumnCount(); j++ )
					{
						System.out.println( "        " + j + ":" + rsmd4.getColumnName(j) + ":" + rs4.getString(rsmd4.getColumnName(j)) );
					}
				}
				rs4.close();
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