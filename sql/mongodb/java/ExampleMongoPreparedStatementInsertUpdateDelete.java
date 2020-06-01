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
import java.sql.Statement;
import java.text.SimpleDateFormat;

import mongodb.jdbc.MongoPreparedStatement;
import unity.util.StringFunc;

/**
 * Demonstrates how to use PreparedStatements using the Mongo JDBC driver with INSERT, UPDATE, and DELETE statements. 
 * 
 * PreparedStatements improve performance by avoiding parsing a SQL query or update multiple times.
 */
@SuppressWarnings("nls")
public class ExampleMongoPreparedStatementInsertUpdateDelete
{
    // Note: This URL allows writing to the Mongo database
    private static String     url = "jdbc:mongo://ds035438.mongolab.com:35438/tpch_writeable";

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

            // PreparedStatement with INSERT, UPDATE, and DELETE
            preparedInsertUpdateDelete();

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
     * Drops a table (collection) by name.
     * 
     * @param tableName
     *            table or collection to drop
     * @throws SQLException
     *             if error occurs during drop
     */
    public static void dropCollection(String tableName) throws SQLException
    {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("DROP TABLE " + tableName);
    }

    /**
     * Example of PreparedStatement with INSERT statement.
     */
    public static void preparedInsertUpdateDelete()
    {
        try
        {
            // DROP test collection if it exists
            dropCollection("test_updates");

            // SQL statement
            String sql = "INSERT INTO test_updates(key, name, entryDate) VALUES (?, ?, ?);";
            MongoPreparedStatement stmt = (MongoPreparedStatement) con.prepareStatement(sql);

            // Used to parse string representations of dates
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Add a few rows
            // Row 1 (1, 'one', '2014-05-17 15:35:42')
            stmt.setInt(1, 1);
            stmt.setString(2, "one");
            stmt.setDate(3, new java.sql.Date(df.parse("2014-05-17 15:35:42").getTime()));               // Can use either Date or Timestamp both stored as Mongo date type
            stmt.executeUpdate();
            System.out.println("INSERTED ROW: (1, 'one', '2014-05-17 15:35:42')");

            // Row 2 (2, null, '2014-05-18 11:12:13')
            stmt.setInt(1, 2);
            stmt.setNull(2, java.sql.Types.VARCHAR);
            stmt.setTimestamp(3, new java.sql.Timestamp(df.parse("2014-05-18 11:12:13").getTime()));     // Can use either Date or Timestamp both stored as Mongo date type
            stmt.executeUpdate();
            System.out.println("INSERTED ROW: (2, null, '2014-05-18 11:12:13')");

            // Retrieve the rows to demonstrate INSERT was successful
            doQuery(con, "SELECT * FROM test_updates");

            // Use UPDATE statement to change name
            sql = "UPDATE test_updates SET name = ? WHERE key = ?";
            stmt = (MongoPreparedStatement) con.prepareStatement(sql);
            stmt.setString(1, "two");
            stmt.setInt(2, 2);
            stmt.executeUpdate();
            System.out.println("\n\nUPDATED ROW with key 2 to have name 'two'.");

            // Retrieve the rows to demonstrate UPDATE was successful
            doQuery(con, "SELECT * FROM test_updates");

            // Use DELETE to delete the first row
            sql = "DELETE FROM test_updates WHERE key = ?";
            stmt = (MongoPreparedStatement) con.prepareStatement(sql);
            stmt.setInt(1, 1);
            stmt.executeUpdate();
            System.out.println("\n\nDELETED ROW with key 1.");

            // Retrieve the rows to demonstrate UPDATE was successful
            doQuery(con, "SELECT _id, key, name, entryDate FROM test_updates");
        }
        catch (Exception e)
        {
            System.out.println(e);
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
        Statement stmt = con.createStatement();

        System.out.println("\nExecuting query:      " + sql);
        ResultSet rst = stmt.executeQuery(sql);

        System.out.println("Results:");

        System.out.println(StringFunc.resultSetToString(rst));

        // Close statement
        rst.close();
        stmt.close();
    }
} // end ExampleMongoPreparedStatement
