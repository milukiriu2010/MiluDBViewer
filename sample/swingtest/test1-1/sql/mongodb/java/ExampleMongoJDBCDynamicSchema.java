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
import java.sql.Statement;

import mongodb.jdbc.MongoConnection;
import mongodb.jdbc.MongoStatement;
import unity.annotation.AnnotatedSourceDatabase;
import unity.annotation.AnnotatedSourceField;
import unity.annotation.AnnotatedSourceTable;
import unity.annotation.GlobalSchema;

/**
 * Demonstrates how to dynamically change the schema of the Mongo JDBC driver.
 */
public class ExampleMongoJDBCDynamicSchema
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
        MongoConnection con = null;
        String sql;

        try
        {
            // Connect to the URL. The last part is the database name (tpch in this case).
            String url = "jdbc:mongo://ds029847.mongolab.com:29847/tpch?rebuildschema=false";

            System.out.println("Making a connection to: " + url);
            con = (MongoConnection) DriverManager.getConnection(url, "dbuser", "dbuser");
            System.out.println("Connection successful.\n");

            // Retrieve the current schema for the connection
            GlobalSchema schema = con.getGlobalSchema();

            // Print out current schema
            System.out.println(schema);

            // Example: Remove a table (collection) from the schema
            String tableName = "\"region\"";        // Double-quotes as delimited identifier (case-sensitive for MongoDB)
            String schemaName = "tpch";             // Same as MongoDB database name
            schema.removeTable("tpch", tableName);

            // Try to execute query to demonstrate that driver does not "see" region collection any more
            // Note: This query involves an expression that forces it to be executed within the MongoDB driver.
            // Queries that can be executed directly on MongoDB do not use the schema.
            sql = "SELECT \"r_regionkey\"+1, \"r_name\" FROM \"region\"";

            try
            {
                doQuery(con, sql);
            }
            catch (Exception e)
            {
                System.out.println("Query on region failed as it does not exist.  Exception thrown: " + e);
            }

            // Example: Add a table (collection) to the schema
            // Retrieve the database information
            AnnotatedSourceDatabase db = schema.getDB("tpch");

            // Create the table first
            AnnotatedSourceTable table = new AnnotatedSourceTable();
            table.setSchemaName(schemaName);
            table.setTableName(tableName);
            table.setParentDatabase(db);
            table.setNumTuples(5);
            db.addTable(table);

            // Create some fields: Parameters: String catalogName, String schemaName, String tableName, String fieldName, int sqltype, String typeName, int size, int digits,
            // int precision, int nullable, String remarks, String defaultValue, int octetLength, int position, String isNull
            AnnotatedSourceField sf = new AnnotatedSourceField(null, schemaName, tableName, "\"r_regionkey\"", java.sql.Types.INTEGER, "integer", 10, 0, 10, 0, null, null, 10, 1, null);
            table.addField(sf);
            sf.setParentTable(table);
            sf = new AnnotatedSourceField(null, schemaName, tableName, "\"r_name\"", java.sql.Types.VARCHAR, "varchar", 50, 0, 50, 0, null, null, 50, 2, null);
            table.addField(sf);
            sf.setParentTable(table);

            schema.addTableIdentifiers(table);

            // Demonstrate that we can now query on this table that we added
            doQuery(con, sql);
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
    public static void doQuery(Connection con, String sql)
            throws SQLException
    {
        Statement stmt = con.createStatement();

        System.out.println("\n\nExecuting query:      " + sql);
        ResultSet rst = stmt.executeQuery(sql);

        System.out.println("Executed Mongo query: " + ((MongoStatement) stmt).getQueryString());
        System.out.println("Query execution complete.\n");

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
    public static int printResults(ResultSet rst)
            throws SQLException
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
