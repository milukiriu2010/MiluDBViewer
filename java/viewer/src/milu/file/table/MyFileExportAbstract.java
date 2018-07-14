package milu.file.table;

import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public abstract class MyFileExportAbstract
	implements
		ProgressReportInterface
{
	protected ProgressInterface  progressInf = null;
	protected double  assignedSize = 0.0;
	
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
	
	abstract public void open( File file ) throws FileNotFoundException, IOException;
	abstract public void close();
	abstract public void export( List<Object> headLst, List<List<Object>> dataLst ) throws IOException;
}
