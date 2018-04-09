package milu.db;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import java.util.List;
import java.util.ArrayList;

import java.nio.file.Paths;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.lang.reflect.InvocationTargetException;

public class MyDBGeneral extends MyDBAbstract 
{
	@Override
	void init()
	{
	}
	
	@Override
	protected void loadDriver() 
			throws ClassNotFoundException, 
					SQLException,
					InstantiationException,
					IllegalAccessException,
					InvocationTargetException,
					NoSuchMethodException
	{
		List<URL> urlLst = new ArrayList<>();
		this.driverPathLst.forEach
		(
			(driverPath)->
			{
				try
				{
					URL url = Paths.get(driverPath).toUri().toURL();
					urlLst.add( url );
				}
				catch ( MalformedURLException malEx )
				{
					throw new RuntimeException( malEx );
				}
			}
		);

		URL[] urls = urlLst.toArray(new URL[urlLst.size()]);
		URLClassLoader loader = new URLClassLoader( urls );
		Driver d = 
			(Driver)Class.forName
			(
				this.driverClassName, 
				true, 
				loader
			).getDeclaredConstructor().newInstance();
		DriverManager.registerDriver( new DriverShim(d) );
	}

	@Override
	protected void loadSpecial() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void processAfterException() throws SQLException 
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap) 
	{
		return null;
	}

	@Override
	public int getDefaultPort() 
	{
		return 0;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "[<<Any>>]";
	}

}
