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

// http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
public class DriverShim implements Driver 
{
	final public static Map<DriverClassConst, DriverNameConst> driverDBMap = new HashMap<>();
	
	@Expose(serialize = false, deserialize = false)
	private Driver       driver = null;
	
	@Expose(serialize = true, deserialize = true)
	private String       driverClassName = null;
	
	@Expose(serialize = true, deserialize = true)
	private List<String> driverPathLst   = new ArrayList<>();
	
	@Expose(serialize = false, deserialize = false)
	private String       dbName = null;
	
	@Expose(serialize = true, deserialize = true)
	private String       tmplateUrl = null;
	
	@Expose(serialize = true, deserialize = true)
	private String       referenceUrl = null;
	
	static
	{
		driverDBMap.put( DriverClassConst.CLASS_NAME_ORACLE    , DriverNameConst.DB_ORACLE );
		driverDBMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, DriverNameConst.DB_POSTGRESQL );
		driverDBMap.put( DriverClassConst.CLASS_NAME_MYSQL     , DriverNameConst.DB_MYSQL );
		driverDBMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, DriverNameConst.DB_CASSANDRA1 );
		driverDBMap.put( DriverClassConst.CLASS_NAME_CASSANDRA2, DriverNameConst.DB_CASSANDRA2 );
		driverDBMap.put( DriverClassConst.CLASS_NAME_SQLSERVER , DriverNameConst.DB_SQLSERVER );
		driverDBMap.put( DriverClassConst.CLASS_NAME_SQLITE    , DriverNameConst.DB_SQLITE );
		driverDBMap.put( DriverClassConst.CLASS_NAME_MONGODB1  , DriverNameConst.DB_MONGODB1 );
		driverDBMap.put( DriverClassConst.CLASS_NAME_H2        , DriverNameConst.DB_H2 );
		driverDBMap.put( DriverClassConst.CLASS_NAME_MARIADB   , DriverNameConst.DB_MARIADB );
	}
	
	public void setDriver(Driver driver)
	{
		this.driver = driver;
	}
	// get ClassName by TextField
	public String getDriverClassName()
	{
		return this.driverClassName;
	}
	
	public void setDriverClassName( String driverClassName )
	{
		this.driverClassName = driverClassName;
	}

	// get ClassName by Class
	public String getDriverClazzName()
	{
		if ( this.driver == null )
		{
			return "";
		}
		String driverClazzName = this.driver.toString();
		int pos = driverClazzName.lastIndexOf("@");
		if ( pos < 0 )
		{
			return driverClazzName;
		}
		else
		{
			return driverClazzName.substring(0,pos);
		}
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
			Map.Entry<DriverClassConst, DriverNameConst> selectedDriverMapEntry =
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
		}
	}
	
	public void setDBName( String dbName )
	{
		this.dbName = dbName;
	}
	
	public String getTemplateUrl()
	{
		return this.tmplateUrl;
	}
	
	public void setTemplateUrl( String templateUrl )
	{
		this.tmplateUrl = templateUrl;
	}
	
	public String getReferenceUrl()
	{
		return this.referenceUrl;
	}
	
	public void setReferenceUrl( String referenceUrl )
	{
		this.referenceUrl = referenceUrl;
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
