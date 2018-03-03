package milu.db;

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
	/*
	public MyDBCassandra()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_AGGREGATE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TYPE );
	}
	*/
	@Override
	protected void loadDriver() throws ClassNotFoundException
	{
		Class.forName( "com.github.cassandra.jdbc.CassandraDriver" );
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

	/*
	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		this.url =
			"jdbc:c*:datastax://"+
			dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
			dbOptMap.get( "DBName" );
	}
	*/
}
