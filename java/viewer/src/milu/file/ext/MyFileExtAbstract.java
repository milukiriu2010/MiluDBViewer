package milu.file.ext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class MyFileExtAbstract<T>
{
	abstract public T load( File file, Class<T> clazz ) 
		throws 
			IOException, 
			ClassCastException, 
			ClassNotFoundException,
			IllegalAccessException,
			InstantiationException,
			InvocationTargetException,
			NoSuchMethodException;
	abstract public void save( File file, T obj ) throws IOException;
}
