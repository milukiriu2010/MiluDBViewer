package milu.task.version;

import javafx.concurrent.Task;

import milu.main.MainController;
import milu.gui.ctrl.info.MapInterface;
import milu.task.ProgressInterface;

public class ModuleCheckTask extends Task<Exception> 
	implements 
		ModuleTaskInterface,
		ProgressInterface 
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
	private String         strUrl   = null;
	
	private MapInterface   mapInf   = null;
	
	private double        progress  = 0.0;
	
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
	
	// MapInterface
	@Override
	public void setMapInterface( MapInterface mapInf )
	{
		this.mapInf = mapInf;
	}
	
	// Task
	@Override
	protected Exception call() 
	{
		Exception    taskEx = null;
		
		ModuleCheck mdChk = new ModuleCheck();
		try
		{
			this.setProgress(0.0);
			
			Thread.sleep(100);
			
			System.out.println( "ModuleCheck start." );
			
			mdChk.setAppConf(this.mainCtrl.getAppConf());
			mdChk.setProgressInterface(this);
			mdChk.check(this.strUrl);
			
			System.out.println( "ModuleCheck end." );
			
			return taskEx;
		}
		catch ( Exception ex )
		{
			taskEx = ex;
			return taskEx;
		}
		finally
		{
			this.mapInf.setValue(mdChk.getValue());
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
			String strMsg = String.format( "%s", msg );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}
}
