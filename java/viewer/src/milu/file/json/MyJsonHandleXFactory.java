package milu.file.json;

import milu.main.AppConf;

// https://stackoverflow.com/questions/34291714/how-to-implement-factory-pattern-with-generics-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class MyJsonHandleXFactory<T> 
{
	public MyJsonHandleXAbstract<T> createInstance( Class<T> clazz )
	{
		MyJsonHandleXAbstract<T> myJsonAbs = null;
		if ( clazz.equals(AppConf.class) )
		{
			myJsonAbs = (MyJsonHandleXAbstract<T>)new MyJsonHandleXSingle<AppConf>();
		}
		else
		{
			return null;
		}
		
		return myJsonAbs;
	}
	
}
