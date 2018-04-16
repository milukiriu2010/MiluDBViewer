package milu.db;

import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * [SUPPORT OBJECT]
 * ROOT_TABLE
 * ROOT_VIEW
 * ROOT_MATERIALIZED_VIEW
 * ROOT_FUNC
 * ROOT_TYPE
 * ROOT_TRIGGER
 * ROOT_SEQUENCE
 * ROOT_ER
 * 
 * @author milu
 *
 */
public class MyDBPostgres extends MyDBAbstract 
{
	@Override
	void init()
	{
		//this.driverClassName = "org.postgresql.Driver"; 
	}

	@Override
	protected void loadSpecial()
	{
	}
	
	/**
	 * Get Driver URL
	 ***********************************************
	 */
	public String getDriverUrl( Map<String, String> dbOptMap )
	{
		this.url = 
				"jdbc:postgresql://"+
				dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
				dbOptMap.get( "DBName" );
		return this.url;
	}
	
	/**
	 * Get Default Port Number
	 **********************************************
	 * @return 
	 */
	@Override
	public int getDefaultPort()
	{
		return 5432;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "PostgreSQL";
	}
	
	@Override
	// https://stackoverflow.com/questions/10399727/psqlexception-current-transaction-is-aborted-commands-ignored-until-end-of-tra
	public void processAfterException() throws SQLException
	{
		this.rollback();
	}	
}
