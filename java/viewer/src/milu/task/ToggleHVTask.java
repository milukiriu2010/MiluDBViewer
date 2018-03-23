package milu.task;

import javafx.concurrent.Task;
import milu.gui.ctrl.common.ToggleHorizontalVerticalInterface;
import javafx.application.Platform;

public class ToggleHVTask extends Task<Double> 
{
	private ToggleHorizontalVerticalInterface  toggleHVInteface = null;
	
	private int cnt = -1;
	
	public ToggleHVTask( ToggleHorizontalVerticalInterface toggleHVInteface, int cnt )
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
			this.updateProgress( 0.0, MAX );
			if ( this.cnt > 0 )
			{
				Thread.sleep( 100 );
			}
			/**/
			Platform.runLater
			(
				()->
				{
					this.toggleHVInteface.switchDirection();
					System.out.println( "ToggleHVTask:runlater." );
					this.updateProgress( MAX, MAX );
				}
			);
			/**/
			
			
			System.out.println( "ToggleHVTask:done:main" );
			
			return MAX;
		}
		catch ( Exception ex )
		{
			System.out.println( "ToggleHVTask:Exception." );
			ex.printStackTrace();
			
			this.updateProgress( -1.0, MAX );
			
			return -1.0;
		}
		finally
		{
			System.out.println( "ToggleHVTask:finally." );
		}
		
	}

}
