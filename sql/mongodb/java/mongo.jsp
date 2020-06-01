<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,mongodb.jdbc.MongoStatement" %>
<!DOCTYPE html>
<html>
<head>
<title>MongoDB Query with JSP Example</title>
</head>
<body>

<% 
Class.forName("mongodb.jdbc.MongoDriver");
		
// Connect to the URL.  The last part is the database name (tpch in this case).
String url="jdbc:mongo://ds029847.mongolab.com:29847/tpch?rebuildschema=false";
Connection con = null;
String sql;

try
{	
	con = DriverManager.getConnection(url, "dbuser", "dbuser");        

	MongoStatement stmt = (MongoStatement) con.createStatement();								
	
	sql = "SELECT _id, n_nationkey, n_name, n_regionkey FROM nation WHERE n_name >= 'C';";			
	ResultSet rst = stmt.executeQuery(sql);								

	out.println("<h4>SQL query</h4><pre>"+sql+"</pre>");
	// Note: MongoStatement.getQueryString() only returns a Mongo query string if the entire query is executed on MongoDB.
	out.println("<h4>Executed Mongo query</h4><pre>"+((MongoStatement) stmt).getQueryString()+"</pre>");
	
	// Print out your results			
	ResultSetMetaData meta = rst.getMetaData();
	int numColumns = meta.getColumnCount();
	out.print("<table border=\"1\"><tr>");
	out.print("<th>"+meta.getColumnName(1)+"</th>");
	for (int j = 2; j <= meta.getColumnCount(); j++)
	    out.print("<th>"+ meta.getColumnName(j)+"</th>");
	out.println("</tr>");

	int count = 0;
	while (rst.next()) 
	{								
		out.print("<tr><td>"+rst.getObject(1)+"</td>");
		for (int j = 2; j <= numColumns; j++)
			out.print("<td>" + rst.getObject(j)+"</td>");
		out.println("</tr>");				
		count++;
	}
	out.println("<h2>Total results: "+count+"</h2>");
}
catch (Exception ex)
{	
	out.println("Exception: " + ex);
	ex.printStackTrace();
}
finally
{			
	if (con != null)
		{	
			try
		{	
			// Close the connection
			con.close();				
		}
		catch (SQLException ex)
		{	
			out.println("SQLException: " + ex);
		}
	}
} 
%>
</body>
</html>