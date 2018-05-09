package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

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
			// execute task
			this.service.submit( collectTask );
			
			collectTask.progressProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
					if ( newVal.doubleValue() == 0.0 )
					{
						this.schemaTreeView.setIsLoading(true);
						//this.dbView.taskProcessing();
					}
					// Task Done.
					else if ( newVal.doubleValue() == 1.0 )
					{
						System.out.println( "CollectTask:Done[" + newVal + "]" );
						this.addChildren(rootEntity);
						this.tabPane.getTabs().removeAll(this.tabPane.getTabs());
						this.schemaTreeView.setIsLoading(false);
						//this.dbView.taskDone();
						this.dbView.setBottomMsg(null);
						this.serviceShutdown();
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
	
}
