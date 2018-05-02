package milu.file.ext;

public class MyFileExtFactory 
{
	public enum TYPE
	{
		SERIALIZE,
		TNSNAMES_ORACLE
	}
	
	public static <T> MyFileExtAbstract<T> getInstance( MyFileExtFactory.TYPE type )
	{
		MyFileExtAbstract<T> myFileExtAbs = null;
		if ( MyFileExtFactory.TYPE.SERIALIZE.equals(type) )
		{
			myFileExtAbs = new MyFileExtSerialize<T>();
		}
		else if ( MyFileExtFactory.TYPE.TNSNAMES_ORACLE.equals(type) )
		{
			myFileExtAbs = new MyFileExtTnsNamesOra<T>();
		}
		
		return myFileExtAbs;
	}
}
