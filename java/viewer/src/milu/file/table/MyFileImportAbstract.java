package milu.file.table;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

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
	
	abstract public void open( File file ) throws Exception; 
	abstract public void close() throws IOException;
	abstract public void load(int columnCnt);
}
