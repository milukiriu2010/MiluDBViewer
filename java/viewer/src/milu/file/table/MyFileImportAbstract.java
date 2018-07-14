package milu.file.table;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public abstract class MyFileImportAbstract
	implements
		ProgressReportInterface
{
	protected List<Object>       headLst = new ArrayList<>();
	protected List<List<Object>> dataLst = new ArrayList<>();
	
	protected ProgressInterface  progressInf = null;
	protected double  assignedSize = 0.0;
	
	public List<Object> getHeadLst()
	{
		return this.headLst;
	}
	
	public List<List<Object>> getDataLst()
	{
		return this.dataLst;
	}

	// ProgressReportInterface
	@Override
	public void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	// ProgressReportInterface
	@Override
	public void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	abstract public void open( File file )  throws FileNotFoundException, IOException, InvalidFormatException; 
	abstract public void close() throws IOException;
	abstract public void load(int columnCnt) throws IOException;
}
