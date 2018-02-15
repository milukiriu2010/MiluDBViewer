package db;

import java.sql.*; 
import oracle.xdb.XMLType;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import oracle.sql.OPAQUE;

import org.w3c.dom.Document;

// https://docs.oracle.com/cd/B14117_01/appdev.101/b10790/xdb11jav.htm
public class OracleConXmlType {
	public static void main(String args[]){
		if ( args.length < 5 )
		{
			System.out.println( "Usage: db.OracleConXmlType [user] [pass] [sid] [host] [pattern]" );
			return;
		}
		try{  
			if ( args[4].equals("1") )
			{
				OracleConXmlType.goPattern1( args );
			}
			else if ( args[4].equals("2") )
			{
				OracleConXmlType.goPattern2( args );
			}
			else if ( args[4].equals("3") )
			{
				OracleConXmlType.goPattern3( args );
			}
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}  

	}  
	
	public static void goPattern1( String[] args ) 
			throws
				ClassNotFoundException,
				SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		  
		String url = "jdbc:oracle:thin:@" + args[3] + ":1521:" + args[2];
		System.out.println( "url[" + url + "]" );
		Connection con=DriverManager.getConnection(  
				url, args[0], args[1]);  
		Statement stmt=con.createStatement();  
		  
		System.out.println( "step4" );
		ResultSet rs=stmt.executeQuery("select * from employees order by id");  
		while(rs.next())  
		{
			System.out.println( "ID=" + rs.getString("id"));
			System.out.println( "DATA=" + rs.getString("data"));
		}
		con.close();  
	}
	
	public static void goPattern2( String[] args ) 
			throws
				ClassNotFoundException,
				SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		  
		String url = "jdbc:oracle:thin:@" + args[3] + ":1521:" + args[2];
		System.out.println( "url[" + url + "]" );
		Connection con=DriverManager.getConnection(  
				url, args[0], args[1]);  
		OraclePreparedStatement stmt= 
				(OraclePreparedStatement)con.
					prepareStatement( "select e.id id, e.data data from employees e order by e.id" );  
		
		ResultSet rset = stmt.executeQuery();
		OracleResultSet orset = (OracleResultSet) rset;   
		while(orset.next())  
		{
			System.out.println( "ID=" + orset.getString("id"));
			
			OPAQUE opaque = orset.getOPAQUE("data");
			if ( opaque == null )
			{
				System.out.println( "OPAQUE: null" );
				continue;
			}
			// get the XMLType
			XMLType poxml = XMLType.createXML( opaque );
			// get the XMLDocument as a string
			@SuppressWarnings("deprecation")
			Document podoc = (Document)poxml.getDOM();
			System.out.println( "DATA=" + podoc.toString() );
		}
		con.close();  
	}
	
	public static void goPattern3( String[] args ) 
			throws
				ClassNotFoundException,
				SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		  
		String url = "jdbc:oracle:thin:@" + args[3] + ":1521:" + args[2];
		System.out.println( "url[" + url + "]" );
		Connection con=DriverManager.getConnection(  
				url, args[0], args[1]);  
		OraclePreparedStatement stmt= 
				(OraclePreparedStatement)con.
					prepareStatement( "select e.id id, e.data.getStringVal() data, e.data.getClobVal() dataclob from employees e order by e.id" );  
		
		ResultSet rset = stmt.executeQuery();
		OracleResultSet orset = (OracleResultSet) rset;   
		while(orset.next())  
		{
			System.out.println( "ID=" + orset.getString("id"));
			System.out.println( "DATA=" + orset.getString("data"));
			oracle.sql.CLOB clb = orset.getCLOB("dataclob");
			System.out.println( "DATACLOB=" + clb.toString() );
		}
		con.close();  
	}
	
}
