package milu.file.json;

public class MyJsonEachFactory 
{
	public enum factoryType
	{
		APP_CONF,
		DRIVER_SHIM,
		MY_DB_ABS
	}
	
	public static <T> MyJsonEachAbstract<T> getInstance( MyJsonEachFactory.factoryType factoryType )
	{
		MyJsonEachAbstract<T> myJsonAbs = null;
		if ( MyJsonEachFactory.factoryType.APP_CONF.equals(factoryType) )
		{
			myJsonAbs = new MyJsonEachAppConf<T>();
		}
		else if ( MyJsonEachFactory.factoryType.DRIVER_SHIM.equals(factoryType) )
		{
			myJsonAbs = new MyJsonEachDriverShim<T>();
		}
		else if ( MyJsonEachFactory.factoryType.MY_DB_ABS.equals(factoryType) )
		{
			myJsonAbs = new MyJsonEachMyDBAbs<T>();
		}
		else
		{
			return null;
		}
		
		return myJsonAbs;
	}
}
