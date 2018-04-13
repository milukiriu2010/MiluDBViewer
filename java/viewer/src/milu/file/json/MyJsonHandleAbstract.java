package milu.file.json;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class MyJsonHandleAbstract 
{
	abstract public void open( String filePath );
	abstract public Object load() throws FileNotFoundException, IOException;
	abstract public void save( Object obj ) throws IOException;
}
