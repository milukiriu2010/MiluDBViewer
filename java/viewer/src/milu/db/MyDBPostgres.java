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
	public String getDriverUrl( Map<String, String> dbOptMap, MyDBAbstract.UPDATE update )
	{
		String urlTmp = "";
		urlTmp = 
				"jdbc:postgresql://"+
				dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
				dbOptMap.get( "DBName" );
		if ( update.equals(MyDBAbstract.UPDATE.WITH) )
		{
			this.dbOptsAux.clear();
			dbOptMap.forEach( (k,v)->this.dbOptsAux.put(k,v) );
			this.url = urlTmp;
		}
		return urlTmp;
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
	
	@Override
	// https://stackoverflow.com/questions/10399727/psqlexception-current-transaction-is-aborted-commands-ignored-until-end-of-tra
	public void processAfterException() throws SQLException
	{
		this.rollback();
	}	
}
