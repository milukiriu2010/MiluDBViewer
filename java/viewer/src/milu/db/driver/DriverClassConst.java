package milu.db.driver;

public enum DriverClassConst 
{
	CLASS_NAME_ORACLE("oracle.jdbc.driver.OracleDriver"),
	CLASS_NAME_POSTGRESQL("org.postgresql.Driver"),
	//CLASS_NAME_MYSQL("com.mysql.jdbc.Driver"),
	CLASS_NAME_MYSQL("com.mysql.cj.jdbc.Driver"),
	CLASS_NAME_CASSANDRA1("com.github.cassandra.jdbc.CassandraDriver"),
	CLASS_NAME_CASSANDRA2("com.github.adejanovski.cassandra.jdbc.CassandraDriver"),
	CLASS_NAME_SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	CLASS_NAME_SQLITE("org.sqlite.JDBC"),
	CLASS_NAME_MONGODB1("mongodb.jdbc.MongoDriver"),
	CLASS_NAME_H2("org.h2.Driver"),
	CLASS_NAME_MARIADB("org.mariadb.jdbc.Driver"),
	CLASS_NAME_DB2("com.ibm.db2.jcc.DB2Driver")
	;
	
	private String val = null;
	
	private DriverClassConst( String val )
	{
		this.val = val;
	}
	
	public String val()
	{
		return this.val;
	}
}
