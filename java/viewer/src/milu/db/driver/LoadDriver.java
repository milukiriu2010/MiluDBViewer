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
	public static DriverShim loadDriver( String driverClassName, List<String> driverPathLst ) 
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
					URL url = null;
					if ( driverPath.startsWith("file:") )
					{
						url = new URL(driverPath);
					}
					else
					{
						url = Paths.get(driverPath).toUri().toURL();
					}
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
		DriverShim driverShim = new DriverShim();
		driverShim.setDriver(driver);
		driverShim.setDriverClassName(driverClassName);
		driverShim.setDriverPathLst(driverPathLst);
		DriverManager.registerDriver( driverShim );
		
		return driverShim;
	}

}
