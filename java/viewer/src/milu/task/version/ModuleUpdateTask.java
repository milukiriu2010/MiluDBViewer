package milu.task.version;

import javafx.concurrent.Task;
import milu.gui.ctrl.info.MapInterface;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class ModuleUpdateTask extends Task<Exception> 
	implements 
		ModuleTaskInterface, 
		ProgressInterface 
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
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
		try
		{
			this.setProgress(0.0);
			
			Thread.sleep(100);
			
			System.out.println( "ModuleUpdate start." );
			
			ModuleUpdate mdUp = new ModuleUpdate();
			mdUp.setAppConf(this.mainCtrl.getAppConf());
			mdUp.setAssignedSize(MAX);
			mdUp.setProgressInterface(this);
			mdUp.setValue(this.mapInf.getValue());
			mdUp.exec();
			
			System.out.println( "ModuleUpdate end." );
			
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
			String strMsg = String.format( "%s", msg );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}
	
	// ProgressInterface
	@Override
	public void cancelProc()
	{
	}
}
