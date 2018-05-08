package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;

import java.util.ArrayList;

import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.collect.CollectTaskFactory;

/**
 * This class is invoked, when there is no root item on SchemaTreeView
 * @author milu
 *
 */
public class SelectedItemHandlerRoot extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
		if ( rootEntity.getEntityLst().size() > 0 )
		{
			this.addChildren(rootEntity);
		}
		else
		{
			MainController mainCtrl = this.dbView.getMainController();
			final Task<Exception> collectTask = CollectTaskFactory.getInstance( mainCtrl, this.myDBAbs );
			if ( collectTask == null )
			{
				return;
			}
			// Thread Pool
			ExecutorService service = Executors.newSingleThreadExecutor();
			// execute task
			service.submit( collectTask );
			
			this.schemaTreeView.setIsLoading(true);
			
			collectTask.progressProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
					// Task Done.
					if ( newVal.doubleValue() == 1.0 )
					{
						System.out.println( "CollectTask:Done[" + newVal + "]" );
						this.addChildren(rootEntity);
						this.schemaTreeView.setIsLoading(false);
						this.dbView.setBottomMsg(null);
						this.serviceShutdown(service);
					}
				}
			);
			
			collectTask.messageProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					System.out.println( "CollectTask:Message[" + newVal + "]" );
					this.dbView.setBottomMsg(newVal);
				}
			);
		}
		
	}
	
	private void addChildren( SchemaEntity rootEntity )
	{
		if ( this.itemRoot == null )
		{
			List<SchemaEntity> schemaEntityLst = new ArrayList<>();
			schemaEntityLst.add(rootEntity);
			schemaTreeView.addEntityLst( null, schemaEntityLst, true );
		}
		else if ( this.itemSelected.getChildren().size() == 0 )
		{
			schemaTreeView.addEntityLst( this.itemSelected, rootEntity.getEntityLst(), true );
		}
	}
	
    private void serviceShutdown( ExecutorService service )
    {
		try
		{
			System.out.println( "shutdown executor start(" + this.getClass().toString() + ")." );
			service.shutdown();
			service.awaitTermination( 3, TimeUnit.SECONDS );
		}
		catch ( InterruptedException intEx )
		{
			System.out.println( "tasks interrupted(" + this.getClass().toString() + ")." );
		}
		finally
		{
			if ( !service.isTerminated() )
			{
				System.out.println( "executor still working...(" + this.getClass().toString() + ")." );
			}
			service.shutdownNow();
			System.out.println( "executor finished(" + this.getClass().toString() + ")." );
		}
    }
	
}
