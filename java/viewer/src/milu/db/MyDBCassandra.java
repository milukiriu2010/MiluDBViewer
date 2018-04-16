package milu.db;

import java.sql.SQLException;
import java.util.Map;

import milu.db.driver.DriverConst;

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
		//this.driverClassName = "com.github.cassandra.jdbc.CassandraDriver"; 
	}

	@Override
	protected void loadSpecial()
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap)
	{
		if ( DriverConst.CLASS_NAME_CASSANDRA1.val().equals(this.driverShim.getDriverClazzName()) )
		{
			this.url =
					"jdbc:c*:datastax://"+
					dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
					dbOptMap.get( "DBName" );
		}
		else if ( DriverConst.CLASS_NAME_CASSANDRA2.val().equals(this.driverShim.getDriverClazzName()) )
		{
			this.url =
					"jdbc:cassandra://"+
					dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
					dbOptMap.get( "DBName" );
		}
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
