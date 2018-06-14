package milu.task.version;

import javafx.concurrent.Task;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class ModuleCheckTask extends Task<Exception> 
	implements 
		ModuleTaskInterface,
		ProgressInterface 
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
	private String         strUrl = null;
	
	private double        progress = 0.0;
	
	// ModuleTaskInterface
	@Override
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	// ModuleTaskInterface
	@Override
	public void setUrl( String strUrl )
	{
		this.strUrl = strUrl;
	}
	
	// Task
	@Override
	protected Exception call() 
	{
		Exception    taskEx = null;
		
		try
		{
			this.setProgress(0.0);
			
			Thread.sleep(100);
			
			return taskEx;
		}
		catch ( Exception ex )
		{
			taskEx = ex;
			return taskEx;
		}
		finally
		{
			this.setProgress(MAX);
			this.updateValue(taskEx);
			this.setMsg("");
		}
	}

	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
	}
	
	@Override
	synchronized public void setProgress( double pos )
	{
		this.progress = pos;
		this.updateProgress( this.progress, MAX );
	}
	
	@Override
	synchronized public void setMsg( String msg )
	{
		if ( "".equals(msg) == false )
		{
			String strMsg = String.format( "Loaded(%.3f%%) %s", this.progress, msg );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}
}
