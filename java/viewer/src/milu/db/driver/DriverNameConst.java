package milu.db.driver;

public enum DriverNameConst 
{
	DB_ORACLE("Oracle"),
	DB_POSTGRESQL("PostgreSQL"),
	DB_MYSQL("MySQL"),
	DB_CASSANDRA1("Cassandra(zhicwu)"),
	DB_CASSANDRA2("Cassandra(adejanovski)"),
	DB_SQLSERVER("SQLServer"),
	DB_SQLITE("SQLite")
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
