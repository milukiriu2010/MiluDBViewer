package milu.task.exp;

import java.io.File;
import java.io.IOException;

import java.util.List;

import javafx.concurrent.Task;
import milu.file.table.MyFileExportAbstract;
import milu.file.table.MyFileExportFactory;
import milu.task.ProgressInterface;

public class ExportTask extends Task<Exception> 
	implements 
		ProgressInterface 
{
	private File               file    = null;
	private List<Object>       headLst = null;
	private List<List<Object>> dataLst = null;
	private int  dataSize = 0;
	
	private final double MAX = 100.0;
	private double progress = 0.0;
	
	public void setFile( File file )
	{
		this.file = file;
	}
	
	public void setHeadLst( List<Object> headLst )
	{
		this.headLst = headLst;
	}

	public void setDataLst( List<List<Object>> dataLst )
	{
		this.dataLst  = dataLst;
		this.dataSize = dataLst.size();
	}

	// Task
	@Override
	protected Exception call() throws Exception 
	{
		Exception taskEx = null;
		
		Thread.sleep(100);
		this.setProgress(0.0);
		
		MyFileExportAbstract myFileAbs = MyFileExportFactory.getInstance( file );
		try
		{
			myFileAbs.setProgressInterface(this);
			myFileAbs.setAssignedSize(MAX);
			myFileAbs.open( this.file );
			myFileAbs.export( this.headLst, this.dataLst );
			
			return taskEx;
		}
		catch( IOException ioEx )
		{
			taskEx = ioEx;
			return taskEx;
		}
		finally
		{
			myFileAbs.close();
			myFileAbs = null;
			
			this.setProgress(MAX);
			this.setMsg("");
		}
	}

	// ProgressInterface
	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
	}
	
	// ProgressInterface
	@Override
	synchronized public void setProgress( double pos )
	{
		this.progress = pos;
		this.updateProgress( this.progress, MAX );
	}
	
	// ProgressInterface
	@Override
	synchronized public void setMsg( String msg )
	{
		if ( "".equals(msg) == false )
		{
			String strMsg = String.format( "Export(%.3f%%) - %s/%d", this.progress, msg, this.dataSize );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}

}
