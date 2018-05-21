package mongodb;

/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mongodb.jdbc.MongoStatement;
import unity.util.StringFunc;

/**
 * Demonstrates how to retrieve and build metadata for a MongoDB database and advanced features
 * of the MongoDB JDBC driver.
 */
public class ExampleMongoJDBCMetadata
{
    /**
     * Main method.
     * 
     * @param args
     *            no arguments required
     */
    public static void main(String[] args)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet rst = null;

        try
        {
            // Create new instance of Mongo JDBC Driver and make connection.
            System.out.println("Registering Driver.");
            Class.forName("mongodb.jdbc.MongoDriver");

            // Connect to the URL. The last part is the database name (tpch in this case).
            String url = "jdbc:mongo://ds029847.mongolab.com:29847/tpch";
            System.out.println("Making a connection to: " + url);
            con = DriverManager.getConnection(url, "dbuser", "dbuser");
            System.out.println("Connection successful.\n");

            // Retrieve database metadata
            DatabaseMetaData dmd = con.getMetaData();

            // Request information about the Mongo server and JDBC version
            System.out.println("Mongo Server version: " + dmd.getDatabaseProductVersion());
            System.out.println("JDBC driver version:  " + dmd.getDriverVersion());

            // Request information on the collections (tables)
            rst = dmd.getTables(null, null, "%", new String[]{"TABLE"});        // % matches any table name
            String result = StringFunc.resultSetToString(rst);
            System.out.println("Tables (Collections) Available:");
            System.out.println(result);
            rst.close();

            // Request information on the fields for collection customer
            rst = dmd.getColumns(null, null, "customer", "%");
            result = StringFunc.resultSetToString(rst);
            System.out.println("\nFields in customer collection:");
            System.out.println(result);
            rst.close();

            // Request primary keys for collection customer
            rst = dmd.getPrimaryKeys(null, null, "customer");
            result = StringFunc.resultSetToString(rst);
            System.out.println("\nPrimary keys of customer collection:");
            System.out.println(result);
            rst.close();

            // Request index information for collection customer
            rst = dmd.getIndexInfo(null, null, "customer", false, false);
            result = StringFunc.resultSetToString(rst);
            System.out.println("\nIndexes for customer collection:");
            System.out.println(result);
            rst.close();

            // Execute a query on Mongo and print Results
            String sql = "SELECT c_custkey, c_name, c_acctbal FROM customer WHERE c_acctbal > 9900 ORDER BY c_acctbal DESC";
            System.out.println("\nExecuting a query to retrieve customers who have a large balance.  SQL:");
            System.out.println(sql);
            stmt = con.createStatement();
            rst = stmt.executeQuery(sql);
            System.out.println("\nExecuted Mongo query: " + ((MongoStatement) stmt).getQueryString());
            result = StringFunc.resultSetToString(rst);
            System.out.println(result);

            // Execute a query joining two Mongo collections
            sql = "SELECT c_custkey, c_name, c_acctbal, n_name FROM customer C INNER JOIN nation N ON C.c_nationkey = N.n_nationkey WHERE c_custkey < 10";
            System.out.println("\nExecuting a query to retrieve the customers and the country they are in.  SQL:");
            System.out.println(sql);
            stmt = con.createStatement();
            rst = stmt.executeQuery(sql);
            result = StringFunc.resultSetToString(rst);
            System.out.println(result);

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
}