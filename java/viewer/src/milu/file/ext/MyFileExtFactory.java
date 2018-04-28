package milu.file.ext;

public class MyFileExtFactory 
{
	public enum TYPE
	{
		SERIALIZE
	}
	
	public static <T> MyFileExtAbstract<T> getInstance( MyFileExtFactory.TYPE type )
	{
		MyFileExtAbstract<T> myFileExtAbs = null;
		if ( MyFileExtFactory.TYPE.SERIALIZE.equals(type) )
		{
			myFileExtAbs = new MyFileExtSerialize<T>();
		}
		
		return myFileExtAbs;
	}
}
