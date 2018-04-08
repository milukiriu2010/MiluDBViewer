package milu.db;

import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * [SUPPORT OBJECT]
 * ROOT_TABLE
 * ROOT_MATERIALIZED_VIEW
 * ROOT_FUNC
 * ROOT_AGGREGATE
 * ROOT_TYPE
 * 
 * @author milu
 *
 */
public class MyDBCassandra extends MyDBAbstract
{
	@Override
	void init()
	{
		this.driverClassName = "com.github.cassandra.jdbc.CassandraDriver"; 
	}
	
	@Override
	protected void loadDriver() throws ClassNotFoundException
	{
		Class.forName( this.driverClassName );
	}

	@Override
	protected void loadSpecial()
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap)
	{
		this.url =
				"jdbc:c*:datastax://"+
				dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
				dbOptMap.get( "DBName" );
		return this.url;
	}

	@Override
	public int getDefaultPort()
	{
		return 9042;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "Cassandra";
	}

	@Override
	public void processAfterException() throws SQLException
	{
	}
}
