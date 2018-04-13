package milu.file.json;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class MyJsonHandleXAbstract<T> 
{
	abstract public void open( String filePath );
	abstract public T load( Class<T> clazz ) throws FileNotFoundException, IOException;
	abstract public void save( T t ) throws IOException;
}
