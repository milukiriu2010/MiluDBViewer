/*
 * This code was created by Unity Data Inc. (www.unityjdbc.com).
 * 
 * It may be freely used, modified, and distributed with no restrictions.
 */

package mongodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mongodb.jdbc.MongoStatement;
import unity.operators.Operator;
import unity.query.GlobalQuery;

/**
 * Example code to translate a SQL query to MongoDB syntax.  If query cannot be translated for complete execution on Mongo, outputs the Unity query plan.
 */
@SuppressWarnings("nls")
public class ExampleMongoTranslate
{
    // Use demo tpch Mongo instance. Queries that can be directly done using MongoDB can refer to collections not in this instance.
    // Queries that will involve UnityJDBC need a schema and a valid instance.
    private static String     url = "jdbc:mongo://ds029847.mongolab.com:29847/tpch?debug=false";

    // Mongo JDBC connection
    private static Connection con = null;

    /**
     * Main method
     * 
     * @param args
     *            no arguments required
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

            // A query that can be translated and executed directly on MongoDB
            String sql = "SELECT r_regionkey, r_name FROM region WHERE r_regionkey < 3;";
            translate(sql);

            // A query that can be translated and executed directly on MongoDB (even though the given collection does not exist in the sample Mongo instance)
            sql = "SELECT * FROM my_collection WHERE value < 3 AND value2 >= 'abc';";
            translate(sql);

            // A query that can be executed by MongoDB directly which is translated into a query plan involving queries to MongoDB and operators done by UnityJDBC
            sql = "SELECT r_regionkey, r_name, n_name, n_regionkey, n_nationkey FROM region R INNER JOIN nation N ON R.r_regionkey = N.n_regionkey WHERE r_regionkey < 3;";
            translate(sql);
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
        finally
        {
            if (con != null)
            {
                try
                {	// Close the connection
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
     * Translates a SQL query to MongoDB (if possible).
     * 
     * @param sql
     *            SQL query to translate
     * @throws SQLException
     *             if a database or translation error occurs
     */
    public static void translate(String sql)
            throws SQLException
    {
        MongoStatement stmt = (MongoStatement) con.createStatement();
        boolean schemaValidation = false;                // This will allow a query to be executed without a schema (or a Mongo connection)
        GlobalQuery gq = stmt.parseQuery(sql, schemaValidation);

        System.out.println("\n\nTranslating SQL query: \n" + sql + '\n');
        String mongoQuery = stmt.getQueryString();
        if (mongoQuery.equals(""))
        {    // Query could not be executed by Mongo, output Unity execution plan
            System.out.println("SQL query cannot be directly executed by UnityJDBC.  Here is UnityJDBC logical query tree: ");
            gq.printTree();
            System.out.println("\nExecution plan: ");
            Operator.printTree(gq.getExecutionTree(), 1);
        }
        else
        {
            System.out.println("To Mongo query: \n" + mongoQuery);
        }
    }
}
