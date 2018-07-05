package milu.task;

import javafx.concurrent.Task;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import javafx.application.Platform;

public class ToggleHVTask extends Task<Double> 
	implements 
		ProgressInterface
{
	private DirectionSwitchInterface  toggleHVInteface = null;
	
	private final double MAX = 100.0;
	
	private double progress = 0.0;
	
	private int cnt = -1;
	
	public ToggleHVTask( DirectionSwitchInterface toggleHVInteface, int cnt )
	{
		super();
		this.toggleHVInteface = toggleHVInteface;
		this.cnt = cnt;
	}

	@Override
	protected Double call() 
	{
		final double MAX = 100.0;
		
		try
		{
			System.out.println( "ToggleHVTask:start." );
			this.setProgress(0.0);
			if ( this.cnt > 0 )
			{
				Thread.sleep( 100 );
			}
			
			Platform.runLater(()->{
				if ( this.toggleHVInteface instanceof ProgressReportInterface )
				{
					((ProgressReportInterface)this.toggleHVInteface).setProgressInterface(this);
					((ProgressReportInterface)this.toggleHVInteface).setAssignedSize(MAX);
				}
				this.toggleHVInteface.switchDirection(null);
				System.out.println( "ToggleHVTask:runlater." );
				this.setProgress(MAX);
			});
			
			System.out.println( "ToggleHVTask:done:main" );
			
			return MAX;
		}
		catch ( Exception ex )
		{
			System.out.println( "ToggleHVTask:Exception." );
			ex.printStackTrace();
			
			//this.updateProgress( -1.0, MAX );
			this.setProgress(MAX);
			
			return -1.0;
		}
		finally
		{
			System.out.println( "ToggleHVTask:finally." );
		}
		
	}
	
	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
		//System.out.println( "ToggleHVTask:addProgress:" + addpos + ":" + this.progress );
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
