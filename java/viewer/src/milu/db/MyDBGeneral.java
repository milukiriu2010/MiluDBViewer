package milu.db;
import java.sql.SQLException;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import milu.db.driver.LoadDriver;

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
		/*
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
		*/
		LoadDriver.loadDriver( this.driverClassName, this.driverPathLst );
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
