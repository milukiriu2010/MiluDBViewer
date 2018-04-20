package milu.db;

import java.sql.SQLException;
import java.util.Map;

import milu.db.driver.DriverClassConst;

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
	public String getDriverUrl(Map<String, String> dbOptMap, MyDBAbstract.UPDATE update )
	{
		String urlTmp = "";
		if ( DriverClassConst.CLASS_NAME_CASSANDRA1.val().equals(this.driverShim.getDriverClazzName()) )
		{
			urlTmp =
					"jdbc:c*:datastax://"+
					dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
					dbOptMap.get( "DBName" );
		}
		else if ( DriverClassConst.CLASS_NAME_CASSANDRA2.val().equals(this.driverShim.getDriverClazzName()) )
		{
			urlTmp =
					"jdbc:cassandra://"+
					dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
					dbOptMap.get( "DBName" );
		}
		if ( update.equals(MyDBAbstract.UPDATE.WITH) )
		{
			this.dbOptsAux.clear();
			dbOptMap.forEach( (k,v)->this.dbOptsAux.put(k,v) );
			this.url = urlTmp;
		}
		return urlTmp;
	}

	@Override
	public int getDefaultPort()
	{
		return 9042;
	}

	@Override
	public void processAfterException() throws SQLException
	{
	}
}
