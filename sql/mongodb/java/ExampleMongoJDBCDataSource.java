package mongodb;

/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import mongodb.jdbc.MongoDataSource;
import mongodb.jdbc.MongoXADataSource;
import unity.util.StringFunc;

/**
 * The JDBC Driver for MongoDB supports connecting using DataSource and XADataSource for use with connection pools and web application servers.
 * 
 * Class name for common data source (CommonDataSource) and pooled data source (ConnectionPoolDataSource): mongo.jdbc.MongoDataSource
 * 
 * Class name for XA data source (XADataSource): mongo.jdbc.MongoXADataSource
 * 
 * Note that although the driver will respond like it supports transactions and two phase commit (2PC), there is no transactional support as MongoDB does not support transactions.
 */
public class ExampleMongoJDBCDataSource
{
    private static String url = "jdbc:mongo://ds029847.mongolab.com:29847/tpch";

    /**
     * Main method.
     * 
     * @param args
     *            no arguments required
     */
    public static void main(String[] args)
    {
        // Tests connection with a standard data source
        testDataSourceConnection();

        // Tests connection with a pooled data source
        testDataSourcePooledConnection();

        // Tests connection with a XA data source
        testDataSourceXAConnection();
    }

    /**
     * Tests connection with a standard data source
     */
    public static void testDataSourceConnection()
    {
        Connection con = null;
        try
        {
            MongoDataSource ds = new MongoDataSource();
            Properties prop = new Properties();
            prop.setProperty("url", url);
            prop.setProperty("user", "dbuser");
            prop.setProperty("password", "dbuser");

            con = ds.getConnection(prop);

            // Execute a test query
            System.out.println("Testing common data source connection.");
            executeTestQuery(con);
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
     * Tests connection with a standard data source
     */
    public static void testDataSourcePooledConnection()
    {
        Connection con = null;
        try
        {
            MongoDataSource ds = new MongoDataSource();
            Properties prop = new Properties();
            prop.setProperty("url", url);
            prop.setProperty("user", "dbuser");
            prop.setProperty("password", "dbuser");

            con = ds.getPooledConnection(prop).getConnection();

            // Execute a test query
            System.out.println("\nTesting pool data source connection.");
            executeTestQuery(con);
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
     * Tests connection with a standard data source
     */
    public static void testDataSourceXAConnection()
    {
        Connection con = null;
        try
        {
            MongoXADataSource ds = new MongoXADataSource();
            ds.setProperty("url", url);
            ds.setProperty("user", "dbuser");
            ds.setProperty("password", "dbuser");

            con = ds.getXAConnection().getConnection();

            // Execute a test query
            System.out.println("\nTesting XA data source connection.");
            executeTestQuery(con);
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
     * Executes a test query using a connection.
     * 
     * @param con
     *            connection to execute query on
     * @throws SQLException
     *             if an error occurs during query execution
     */
    public static void executeTestQuery(Connection con) throws SQLException
    {
        System.out.println("\nExecuting test query.");
        Statement stmt = con.createStatement();
        ResultSet rst = stmt.executeQuery("SELECT _id, r_regionkey, r_name from region");
        System.out.println(StringFunc.resultSetToString(rst));
    }
}