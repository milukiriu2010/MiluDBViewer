package milu.db.driver;

public enum DriverNameConst
{
	DB_CASSANDRA1("Cassandra(zhicwu)"),
	DB_CASSANDRA2("Cassandra(adejanovski)"),
	DB_MONGODB1("MongoDB(Unity)"),
	DB_MYSQL("MySQL"),
	DB_ORACLE("Oracle"),
	DB_POSTGRESQL("PostgreSQL"),
	DB_SQLITE("SQLite"),
	DB_SQLSERVER("SQLServer")
	;
	
	private String val = null;
	
	private DriverNameConst( String val )
	{
		this.val = val;
	}
	
	public String val()
	{
		return this.val;
	}
}
