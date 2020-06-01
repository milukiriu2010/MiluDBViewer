package mongodb;

/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import mongodb.jdbc.MongoStatement;
import unity.jdbc.UnityDriver;

/**
 * Demonstrates how to write queries using the Mongo JDBC driver using a database with read/write privileges. 
 *
 * For more examples, go to: http://www.unityjdbc.com/mongojdbc/mongosqltranslate.php
 * 
 * Force rebuild of schema on connection
 * jdbc:mongo://url:port/dbName?rebuildschema=true
 * 
 * Force rebuild of schema and store locally
 * jdbc:mongo://url:port/dbName?rebuildschema=true&schema=tmp.txt
 */
public class ExampleMongoJDBCUpdate
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

            // Connect to the URL. The last part is the database name (tpch_writeable in this case).
            String url = "jdbc:mongo://ds035438.mongolab.com:35438/tpch_writeable?rebuildschema=false";

            System.out.println("Making a connection to: " + url);
            con = DriverManager.getConnection(url, "dbuser", "dbuser");
            System.out.println("Connection successful.\n");

            // Create a statement and submit a query.
            /*
             * Sample queries
             */
            // DELETE FROM with a WHERE filter (inequality)
            sql = "DELETE FROM region WHERE r_regionkey > 3";
            doUpdate(con, sql);

            // DELETE FROM with a WHERE filter (string)
            sql = "DELETE FROM region WHERE r_name = 'AMERICA'";
            doUpdate(con, sql);

            // UPDATE a collection attribute with a WHERE filter
            sql = "DELETE FROM nation WHERE n_regionkey = 1";
            doUpdate(con, sql);

            // UPDATE a collection attribute with a WHERE filter
            sql = "UPDATE region SET r_regionkey = 6 WHERE r_regionkey = 3";
            doUpdate(con, sql);

            sql = "UPDATE region SET r_name = 'ANTARTICA' WHERE r_regionkey = 2";
            doUpdate(con, sql);

            sql = "INSERT INTO region2 (r_regionkey, r_name, r_comment) VALUES (5, 'WESTEROS', 'ASDFQWER1234')";
            doUpdate(con, sql);
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
     * Helper method to run SQL update commands on MongoDB.
     * 
     * @param con Connection object to MongoDB
     * @param sql SQL statement to run
     * 
     * @throws SQLException
     *             if a database error occurs
     */
    @SuppressWarnings("nls")
    public static void doUpdate(Connection con, String sql) throws SQLException
    {
        int rst;
        MongoStatement stmt;

        stmt = (MongoStatement) con.createStatement();

        System.out.println("\nExecuting query:      " + sql);

        // Execute query
        rst = stmt.executeUpdate(sql);

        System.out.println("Executed Mongo query: " + ((MongoStatement) stmt).getQueryString());
        System.out.println("Query execution complete.");

        stmt.close();

        System.out.println("SUCCESS.  Total rows affected: " + rst);
    }

    /**
     * Helper method to print out the resultSet.
     * 
     * @param rst the resultSet returned from the query execution.
     * @return the number of tuples returned
     * @throws SQLException
     *             if a database error occurs
     */
    @SuppressWarnings("nls")
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