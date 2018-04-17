package milu.db.driver;

public enum DriverClassConst 
{
	CLASS_NAME_ORACLE("oracle.jdbc.driver.OracleDriver"),
	CLASS_NAME_POSTGRESQL("org.postgresql.Driver"),
	CLASS_NAME_MYSQL("com.mysql.jdbc.Driver"),
	CLASS_NAME_CASSANDRA1("com.github.cassandra.jdbc.CassandraDriver"),
	CLASS_NAME_CASSANDRA2("com.github.adejanovski.cassandra.jdbc.CassandraDriver")
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
