package mongodb;

/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import mongodb.jdbc.MongoPreparedStatement;
import unity.util.StringFunc;

/**
 * Demonstrates how to use PreparedStatements using the Mongo JDBC driver. 
 * 
 * PreparedStatements improve performance by avoiding parsing a SQL query or update multiple times.
 */
@SuppressWarnings("nls")
public class ExampleMongoPreparedStatement
{
    private static String     url = "jdbc:mongo://ds029847.mongolab.com:29847/tpch";

    private static Connection con = null;

    /**
     * Main method
     * 
     * @param args
     *            no arg required
     */
    public static void main(String[] args)
    {
        try
        {
            // Create new instance of Mongo JDBC Driver and make connection
            System.out.println("\nRegistering driver.");
            Class.forName("mongodb.jdbc.MongoDriver");

            System.out.println("\nGetting connection:  " + url);
            con = DriverManager.getConnection(url, "dbuser", "dbuser");
            System.out.println("\nConnection successful for " + url);

            // PreparedStatement using an integer parameter in a SQL query
            preparedQuery();

            // PreparedStatement using a String parameter in a SQL query
            preparedQueryString();

            // PreparedStatement using a integer parameter in a SQL query with a join
            preparedQueryJoin();

            // PreparedStatement using a SQL LIMIT clause that has the number of rows returns as the parameters
            preparedQueryLimit();

            System.out.println("\nALL TESTS COMPLETED SUCCESSFULLY!");
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
        } // end try-catch-finally block
    } // end main()

    /**
     * Example of PreparedStatement with an integer parameter in a SQL SELECT.
     */
    public static void preparedQuery()
    {
        try
        {
            String sql = "SELECT * FROM nation WHERE n_nationkey = ?;";
            MongoPreparedStatement stmt = (MongoPreparedStatement) con.prepareStatement(sql);

            for (int i = 0; i < 3; i++)
            {
                stmt.setInt(1, 1 + i % 5);
                ResultSet rst = stmt.executeQuery();
                System.out.println("\nResult of query: " + stmt.getSQLStatement());
                System.out.println(StringFunc.resultSetToString(rst));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Example of PreparedStatement with a string parameter in a SQL SELECT.
     */
    public static void preparedQueryString()
    {
        try
        {
            String sql = "SELECT * FROM nation WHERE n_name = ?;";
            MongoPreparedStatement stmt = (MongoPreparedStatement) con.prepareStatement(sql);

            stmt.setString(1, "UNITED STATES");
            ResultSet rst = stmt.executeQuery();
            System.out.println("\nResult of query: " + stmt.getSQLStatement());
            System.out.println(StringFunc.resultSetToString(rst));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Example of PreparedStatement with a parameter in a SQL SELECT that contains a join.
     */
    public static void preparedQueryJoin()
    {
        try
        {
            String sql = "SELECT n_nationkey, n_name, r_name FROM nation n, region r WHERE n_name = ? and n.n_regionkey = r.r_regionkey and n_nationkey > ?;";
            MongoPreparedStatement stmt = (MongoPreparedStatement) con.prepareStatement(sql);

            stmt.setString(1, "UNITED STATES");
            stmt.setInt(2, 9);
            ResultSet rst = stmt.executeQuery();
            System.out.println("\nResult of query: " + stmt.getSQLStatement());
            System.out.println(StringFunc.resultSetToString(rst));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Example of PreparedStatement with parameters used in SQL LIMIT clause.
     */
    public static void preparedQueryLimit()
    {
        try
        {
            String sql = "SELECT c_custkey FROM customer LIMIT ? OFFSET ?;";
            MongoPreparedStatement stmt = (MongoPreparedStatement) con.prepareStatement(sql);

            for (int i = 0; i < 2; i++)
            {
                stmt.setInt(1, i * 3 + 5);
                stmt.setInt(2, i);
                ResultSet rst = stmt.executeQuery();
                System.out.println("\nResult of query: " + stmt.getSQLStatement());
                System.out.println(StringFunc.resultSetToString(rst));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

} // end ExampleMongoPreparedStatement
