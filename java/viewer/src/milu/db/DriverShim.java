package milu.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

// http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
public class DriverShim implements Driver 
{
	private Driver driver = null;
	
	DriverShim(Driver d)
	{
		this.driver = d;
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
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
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

}
