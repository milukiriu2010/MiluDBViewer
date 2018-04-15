package milu.db.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import com.google.gson.annotations.Expose;

import milu.db.DBConst;

// http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
public class DriverShim implements Driver 
{
	final public static Map<DriverConst, DBConst> driverDBMap = new HashMap<>();
	
	@Expose(serialize = false, deserialize = false)
	private Driver       driver = null;
	
	private String       driverClassName = null;
	
	private List<String> driverPathLst   = new ArrayList<>();
	
	private String       dbName = null;
	
	static
	{
		driverDBMap.put( DriverConst.CLASS_NAME_ORACLE    , DBConst.DB_ORACLE );
		driverDBMap.put( DriverConst.CLASS_NAME_POSTGRESQL, DBConst.DB_POSTGRESQL );
		driverDBMap.put( DriverConst.CLASS_NAME_MYSQL     , DBConst.DB_MYSQL );
		driverDBMap.put( DriverConst.CLASS_NAME_CASSANDRA1, DBConst.DB_CASSANDRA1 );
	}
	
	public void setDriver(Driver driver)
	{
		this.driver = driver;
	}
	
	public String getDriverClassName()
	{
		return this.driverClassName;
	}
	
	public void setDriverClassName( String driverClassName )
	{
		this.driverClassName = driverClassName;
	}
	
	public String getDriverClazzName()
	{
		String driverClazzName = this.driver.toString();
		return driverClazzName.substring(0,driverClazzName.lastIndexOf("@"));
	}
	
	public List<String> getDriverPathLst()
	{
		return this.driverPathLst;
	}
	
	public void setDriverPathLst( List<String> driverPathLst )
	{
		this.driverPathLst.addAll( driverPathLst );
	}
	
	public String getDBName()
	{
		if ( this.dbName != null )
		{
			return this.dbName;
		}
		else
		{
			Map.Entry<DriverConst, DBConst> selectedDriverMapEntry =
					driverDBMap.entrySet().stream()
						.filter( x -> x.getKey().val().equals(this.driverClassName) )
						.findAny()
						.orElse(null);
			if ( selectedDriverMapEntry != null )
			{
				return selectedDriverMapEntry.getValue().val();
			}
			else
			{
				return this.driverClassName;
			}
			/*
			if ( driverDBMap.containsKey(this.driverClassName) )
			{
				return driverDBMap.get(this.driverClassName);
			}
			else
			{
				return this.driverClassName;
			}
			*/
		}
	}
	
	public void setDBName( String dbName )
	{
		this.dbName = dbName;
	}
	
	@Override
	public boolean acceptsURL(String u) throws SQLException 
	{
		return this.driver.acceptsURL(u);
	}

	@Override
	public Connection connect(String u, Properties p) throws SQLException 
	{
		return this.driver.connect(u, p);
	}
	
	@Override
	public int getMajorVersion() 
	{
		return this.driver.getMajorVersion();
	}
	
	@Override
	public int getMinorVersion() 
	{
		return this.driver.getMinorVersion();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException 
	{
		return null;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException 
	{
		return this.driver.getPropertyInfo(u, p);
	}

	@Override
	public boolean jdbcCompliant() 
	{
		return this.driver.jdbcCompliant();
	}

	/*
	@Override
	public String toString()
	{
		return this.driver.toString();
	}
	*/
}
