package milu.db;

public enum DBConst 
{
	DB_ORACLE("Oracle"),
	DB_POSTGRESQL("PostgreSQL"),
	DB_MYSQL("MySQL"),
	DB_CASSANDRA1("Cassandra(zhicwu)"),
	DB_CASSANDRA2("Cassandra(adejanovski)")
	;
	
	private String val = null;
	
	private DBConst( String val )
	{
		this.val = val;
	}
	
	public String val()
	{
		return this.val;
	}
}
