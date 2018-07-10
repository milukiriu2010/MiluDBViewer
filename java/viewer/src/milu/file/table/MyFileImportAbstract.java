package milu.file.table;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public abstract class MyFileImportAbstract 
{
	protected List<Object>       headLst = new ArrayList<>();
	protected List<List<Object>> dataLst = new ArrayList<>();
	
	public List<Object> getHeadLst()
	{
		return this.headLst;
	}
	
	public List<List<Object>> getDataLst()
	{
		return this.dataLst;
	}
	
	abstract public void open( File file )  throws FileNotFoundException, IOException, InvalidFormatException; 
	abstract public void close() throws IOException;
	abstract public void load(int columnCnt) throws IOException;
}
