package db.oracle;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.*; 
import java.util.*;

import abc.DriverShim;

public class OracleTableLst {
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
			else if ( args[0].equals("3") )
			{
				url = new URL("file:loader/oracle/ojdbc7.jar");
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
			
			try
			{
				DriverPropertyInfo[] driverPropInfoLst = d.getPropertyInfo( "", null );
				for ( DriverPropertyInfo  driverPropInfo : driverPropInfoLst )
				{
					System.out.println( "driverPropInfo:key[" + driverPropInfo.name + "]val[" + driverPropInfo.value + "]" );
					break;
				}
			}
			catch ( SQLException sqlEx )
			{
				sqlEx.printStackTrace();
			}
			
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
			//Statement stmt=con.createStatement();
			DatabaseMetaData md = con.getMetaData();
			  
			//step4 execute query
			System.out.println( "step4" );
			/*
			ResultSet rs=stmt.executeQuery("select * from user_tables where rownum <= 1");
			//ResultSet rs=stmt.executeQuery("select * from information_schema.schemata");
			while(rs.next())  
			{
				System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
				//System.out.println(rs.getString("id"));
			}
			*/
			ResultSet rs = md.getTables(null, "MILU", "%", null);
			ResultSetMetaData rsmd = rs.getMetaData();
			while ( rs.next() )
			{
				System.out.println( "==================================" );
				for ( int i = 1; i < rsmd.getColumnCount(); i++ )
				{
					System.out.println( i + ":" + rsmd.getColumnName(i) + ":" + rs.getString(rsmd.getColumnName(i)));
				}
				ResultSet rs2 = md.getColumns(null, "MILU", rs.getString("TABLE_NAME"), "%" );
				ResultSetMetaData rsmd2 = rs2.getMetaData();
				while ( rs2.next() )
				{
					System.out.println( "+++++++++++++++++++++++++" );
					for ( int j = 1; j < rsmd2.getColumnCount(); j++ )
					{
						System.out.println( "    " + j + ":" + rsmd2.getColumnName(j) + ":" + rs2.getString(j));
						//System.out.println( "    " + j + ":" + rsmd2.getColumnName(j) + ":" );
					}
				}
				rs2.close();
				/*
				DatabaseMetaData md3 = con.getMetaData();
				ResultSet rs3 = md3.getPrimaryKeys(null, "test", rs.getString(3) );
				ResultSetMetaData rsmd3 = rs3.getMetaData();
				for ( int j = 1; j < rsmd3.getColumnCount(); j++ )
				{
					System.out.println( "    " + j + ":" + rsmd3.getColumnName(j) + ":" );
					//System.out.println( "    " + j + ":" + rsmd3.getColumnName(j) + ":" + rs3.getString(j) );
				}
				rs3.close();
				*/
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
