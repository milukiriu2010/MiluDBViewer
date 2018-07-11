package milu.file.table;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public abstract class MyFileExportAbstract
{
	abstract public void open( File file ) throws FileNotFoundException, IOException;
	abstract public void close();
	abstract public void export( List<Object> headLst, List<List<Object>> dataLst ) throws IOException;
}
