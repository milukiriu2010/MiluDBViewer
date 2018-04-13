package milu.file.json;

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
		else
		{
			return null;
		}
		
		return myJsonAbs;
	}
	
}
