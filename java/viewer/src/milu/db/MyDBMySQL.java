package milu.db;

import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * [SUPPORT OBJECT]
 * ROOT_TABLE
 * ROOT_VIEW
 * ROOT_SYSTEM_VIEW
 * ROOT_FUNC
 * ROOT_PROC
 * ROOT_TRIGGER
 * ROOT_ER
 * 
 * @author milu
 *
 */
public class MyDBMySQL extends MyDBAbstract 
{
	@Override
	void init()
	{
		//this.driverClassName = "com.mysql.jdbc.Driver"; 
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
		// URL Example
		// ---------------------------------------------------
		// jdbc:mysql://localhost:3306/sakila
		// ---------------------------------------------------
		// https://www.javatpoint.com/example-to-connect-to-the-mysql-database
		urlTmp = 
			"jdbc:mysql://"+
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
		return 3306;
	}
	
	@Override
	public void processAfterException() throws SQLException
	{
	}
}
