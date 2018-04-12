package milu.db.driver;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadDriver 
{
	public static Driver loadDriver( String driverClassName, List<String> driverPathLst ) 
			throws ClassNotFoundException, 
			SQLException,
			InstantiationException,
			IllegalAccessException,
			InvocationTargetException,
			NoSuchMethodException
	{
		List<URL> urlLst = new ArrayList<>();
		driverPathLst.forEach
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
		Driver driver = 
			(Driver)Class.forName
			(
				driverClassName, 
				true, 
				loader
			).getDeclaredConstructor().newInstance();
		DriverManager.registerDriver( new DriverShim(driver) );
		
		return driver;
	}

}
