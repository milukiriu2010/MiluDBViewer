package db;

import java.sql.*; 
//import oracle.xdb.XMLType;
//import oracle.jdbc.OraclePreparedStatement;
//import oracle.jdbc.OracleResultSet;

//import oracle.sql.OPAQUE;

//import org.w3c.dom.Document;

// https://docs.oracle.com/cd/B14117_01/appdev.101/b10790/xdb11jav.htm
public class OracleConOCI {
	public static void main(String args[]){
		if ( args.length < 4 )
		{
			//                                          0      1      2         3      4
			//System.out.println( "Usage: db.OracleConOCI [user] [pass] [tnsname] [host] [pattern]" );
			System.out.println( "Usage: db.OracleConOCI [user] [pass] [tnsname] [pattern]" );
			return;
		}
		try{  
			if ( args[3].equals("1") )
			{
				OracleConOCI.goPattern1( args );
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
		//Class.forName("oracle.jdbc.OracleDriver");
		
		
		  
		//String url = "jdbc:oracle:oci:@//" + args[3] + ":1521:" + args[2];
		String url = "jdbc:oracle:oci:@/" + args[2];
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

	
}
