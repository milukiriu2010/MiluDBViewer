package milu.file.json;

import milu.db.driver.DriverShim;
import milu.main.AppConf;

public class MyJsonHandleFactory
{
	public MyJsonHandleAbstract createInstance( Class<?> clazz )
	{
		MyJsonHandleAbstract myJsonAbs = null;
		if ( clazz.equals(AppConf.class) )
		{
			myJsonAbs = new MyJsonHandleAppConf();
		}
		else if ( clazz.equals(DriverShim.class))
		{
			myJsonAbs = new MyJsonHandleDriverShim();
		}
		else
		{
			return null;
		}
		
		return myJsonAbs;
	}
	
}
