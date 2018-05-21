package mongodb;

/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import mongodb.jdbc.MongoStatement;

/**
 * Demonstrates how to write queries using the Mongo JDBC driver using a database with nested collections and arrays. 
 * Need to have write privileges on database in order to update _schema as this will be built by Unity. 
 * 
 * Force rebuild of schema on connection
 * jdbc:mongo://url:port/dbName?rebuildschema=true
 * 
 * Force rebuild of schema and store locally
 * jdbc:mongo://url:port/dbName?rebuildschema=true&schema=tmp.txt
 */
public class ExampleMongoJDBCNestedArray
{
    /**
     * Main method.
     * 
     * @param args
     *            no arguments required
     */
    @SuppressWarnings({"nls"})
    public static void main(String[] args)
    {
        Connection con = null;
        String sql;

        try
        {
            // Create new instance of Mongo JDBC Driver and make connection.
            System.out.println("Registering Driver.");
            Class.forName("mongodb.jdbc.MongoDriver");

            // Connect to the URL. The last part is the database name (nest in this case).
            String url = "jdbc:mongo://ds041168.mongolab.com:41168/nest?rebuildschema=false";

            System.out.println("Making a connection to: " + url);
            con = DriverManager.getConnection(url, "dbuser", "dbuser");
            System.out.println("Connection successful.\n");

            // Create a statement and submit a query.
            /*
             * Sample queries
             */
            // Select Nested Object (returns Object)
            sql = "SELECT value FROM nested2;";
            doQuery(con, sql);

            // Selected Nested base values
            sql = "SELECT num, value.num, value.name FROM nested2;";
            doQuery(con, sql);

            // Select nested values with nested value in WHERE filter
            sql = "SELECT num, value.num, value.name FROM nested2 WHERE value.name = 'L1_B1';";
            doQuery(con, sql);

            // Select deeply nested value
            sql = "SELECT num, name, value, value.num, value.value, value.value.num, value.value.value, value.value.value.num FROM nested4;";
            doQuery(con, sql);

            // Select from Array
            sql = "SELECT * FROM arraystring;";
            doQuery(con, sql);

            // Select from Array with array value in WHERE filter
            sql = "SELECT * FROM arraystring WHERE value = 'G_6';";
            doQuery(con, sql);

            // Select from Array with array value in WHERE filter
            sql = "SELECT * FROM arraystring WHERE value = 'G_6' and value = 'I_8';";
            doQuery(con, sql);

            // Select from Array with array value in WHERE filter using IN
            sql = "SELECT * FROM arraystring WHERE 'G_6' IN value;";
            doQuery(con, sql);

            // Select from Array with mixed types
            sql = "SELECT * FROM arraymixedobj;";
            doQuery(con, sql);

            // Select from Array with mixed types
            sql = "SELECT * FROM arraymixedobj WHERE value.num = 2;";
            doQuery(con, sql);

            // Select from Array with mixed types (including objects).  Filter on object value.
            // Good practice to double-quote identifiers as they are case-sensitive in MongoDB.
            sql = "SELECT _id, \"value\", \"value.num\" from arraymixedobj WHERE \"value.num\" = 5 limit 5";
            doQuery(con, sql);
            
            // Select from Array with mixed types (including objects).  Filter on object value at a particular subscript.
            // When accessing array subscripts, double-quote identifiers are required.
            sql = "SELECT _id, value, value.num from arraymixedobj WHERE \"value.2.num\" = 5 limit 5";
            doQuery(con, sql);

            // Retrieve individual subscripts of an array.
            sql = "SELECT _id, \"value\", \"value.0\", \"value.1\", \"value.5\" from arraymixedobj WHERE \"value.2.num\" = 5 limit 5";
            doQuery(con, sql);
            
            // Arrays with objects that contain arrays (nested arrays and objects)
            sql = "SELECT num, \"value.0\", \"value.1.num2\", \"value.value2\", \"value.1.value2.value3\", \"value.1.value2.1.value3\" from nestedarray where \"value.1.value2.value3\" < 2 limit 3";
            doQuery(con, sql);
            
            // An array example where array is accessible as ResultSet
            sql = "SELECT value FROM arrayobj;";

            ResultSet rst;
            Statement stmt;

            stmt = con.createStatement();

            System.out.println("\n\nExecuting query: " + sql);

            rst = stmt.executeQuery(sql);

            System.out.println("Executed Mongo query: " + ((MongoStatement) stmt).getQueryString());
            System.out.println("Query execution complete.\n");

            System.out.println("Results:\n");

            int i = 1, j = 1;

            // the ResultSet only array (first element)
            while (rst.next())
            {
                System.out.println("Tuple " + (i++) + " array:");
                Array a = rst.getArray(1);
                // get array as ResultSet
                ResultSet rs = a.getResultSet();
                while (rs.next())
                {
                    Object arrayObject = rs.getObject(1);
                    System.out.println("Element " + (j++) + " : " + arrayObject.toString());
                }
                rs.close();
                j = 1;
            }

            // Close statement
            rst.close();
            stmt.close();
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
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
                    System.out.println("SQLException: " + ex);
                }
            }
        }
    }

    /**
     * Helper method to run SQL query on MongoDB.
     * 
     * @param con Connection object to MongoDB
     * @param sql SQL statement to run
     * 
     * @throws SQLException
     *             if a database error occurs
     */
    public static void doQuery(Connection con, String sql) throws SQLException
    {
        ResultSet rst;
        Statement stmt;

        stmt = con.createStatement();

        System.out.println("\n\nExecuting query:      " + sql);
        rst = stmt.executeQuery(sql);

        System.out.println("Executed Mongo query: " + ((MongoStatement) stmt).getQueryString());
        System.out.println("Query execution complete.");

        System.out.println("Results:\n");

        int count = printResults(rst);

        // Close statement
        rst.close();
        stmt.close();

        System.out.println("\nSUCCESS.  Total results: " + count);
    }

    /**
     * Helper method to print out the resultSet.
     * 
     * @param rst the resultSet returned from the query execution.
     * @return the number of tuples returned
     * @throws SQLException
     *             if a database error occurs
     */
    public static int printResults(ResultSet rst) throws SQLException
    {
        // Print out your results
        ResultSetMetaData meta = rst.getMetaData();
        int numColumns = meta.getColumnCount();
        System.out.print(meta.getColumnName(1));
        for (int j = 2; j <= meta.getColumnCount(); j++)
            System.out.print(", " + meta.getColumnName(j));
        System.out.println();

        int count = 0;
        while (rst.next())
        {
            System.out.print(rst.getObject(1));
            for (int j = 2; j <= numColumns; j++)
                System.out.print(", " + rst.getObject(j));
            System.out.println();
            count++;
        }
        return count;
    }
}