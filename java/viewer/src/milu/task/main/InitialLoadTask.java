package milu.task.main;

import java.util.List;
import java.util.ArrayList;

import javafx.concurrent.Task;

import milu.main.MainController;

import milu.task.ProgressInterface;

public class InitialLoadTask extends Task<Exception>
	implements 
		ProgressInterface
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
	private double         progress = 0.0;
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}	

	@Override
	protected Exception call()
	{
		Exception    taskEx = null;
		List<InitialLoadFactory.FACTORY_TYPE> ilLst = new ArrayList<>();
		ilLst.add(InitialLoadFactory.FACTORY_TYPE.IMAGE);
		ilLst.add(InitialLoadFactory.FACTORY_TYPE.LANG);
		ilLst.add(InitialLoadFactory.FACTORY_TYPE.APPCONF);
		ilLst.add(InitialLoadFactory.FACTORY_TYPE.DRIVER);
		ilLst.add(InitialLoadFactory.FACTORY_TYPE.SECRETKEY);
		
		try
		{
			this.setProgress(0.0);
			
			Thread.sleep(1000);
			
			double assigendSize = MAX/ilLst.size();
			ilLst.forEach
			(
				(factoryType)->
				{
					InitialLoadAbstract ilAbs = InitialLoadFactory.getInstance( factoryType, this.mainCtrl, this, assigendSize );
					ilAbs.load();
				}
			);
			
			Thread.sleep(1000);
			
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
