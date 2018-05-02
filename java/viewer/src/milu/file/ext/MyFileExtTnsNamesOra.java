package milu.file.ext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

public class MyFileExtTnsNamesOra<T> extends MyFileExtAbstract<T>
{
	public T load( File file, Class<T> clazz ) 
		throws 
			IOException, 
			ClassCastException, 
			ClassNotFoundException,
			IllegalAccessException,
			InstantiationException,
			InvocationTargetException,
			NoSuchMethodException
	{
		if ( file.exists() == false )
		{
			return null;
		}
		
		// https://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		try
		(
			FileInputStream  fis = new FileInputStream(file);
		)
		{
			byte[] data = new byte[(int)file.length()];
			fis.read(data);
			
			//String str = new String( data );
			//T obj = clazz.cast(str);
			
			// java.lang.IllegalArgumentException: wrong number of arguments
			//String str = new String( data );
			//T obj = clazz.getDeclaredConstructor().newInstance(str);
			Constructor<?>[] constructors = clazz.getDeclaredConstructors();
			System.out.println( "constructors.size:" + constructors.length );
			T obj = null;
			for ( int i = 0; i < constructors.length; i++ )
			{
				try
				{
					// exit loop 
					// when match "new String( byte[] )"
					obj = clazz.cast(constructors[i].newInstance(data));
					break;
				}
				catch ( Exception ex )
				{
				}
			}
			return obj;
		}
		
	}
	
	public void save( File file, T obj ) throws IOException
	{
		throw new UnsupportedOperationException();
	}
}
