package milu.file.json;

public class MyJsonListFactory 
{
	public enum factoryType
	{
		DRIVER_INFO
	}
	
	public static <T> MyJsonListAbstract<T> getInstance( MyJsonListFactory.factoryType factoryType )
	{
		MyJsonListAbstract<T> myJsonAbs = null;
		if ( MyJsonListFactory.factoryType.DRIVER_INFO.equals(factoryType) )
		{
			myJsonAbs = new MyJsonListDriverInfo<T>();
		}
		else
		{
			return null;
		}
		
		return myJsonAbs;
	}
}
