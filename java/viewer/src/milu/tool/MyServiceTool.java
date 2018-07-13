package milu.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class MyServiceTool 
{
	public static void shutdownService( ExecutorService service )
	{
		try
		{
			System.out.println( "shutdown executor start." );
			service.shutdown();
			service.awaitTermination( 3, TimeUnit.SECONDS );
		}
		catch ( InterruptedException intEx )
		{
			System.out.println( "tasks interrupted" );
		}
		finally
		{
			if ( !service.isTerminated() )
			{
				System.out.println( "executor still working..." );
			}
			service.shutdownNow();
			System.out.println( "executor finished." );
		}
	}
}
