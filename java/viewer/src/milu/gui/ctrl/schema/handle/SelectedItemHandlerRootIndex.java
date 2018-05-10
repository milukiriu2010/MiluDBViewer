package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root index" item is clicked on SchemaTreeView.
 * Show "Index Information"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX] => selected
 *         - [INDEX]    => get
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author miluk
 *
 */
public class SelectedItemHandlerRootIndex extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		/*
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		//TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get function List & add list as children
		if ( itemChildren.size() > 0 )
		{
			return;
		}
		
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( AbsDBFactory.FACTORY_TYPE.INDEX,  CollectDataType.LIST,mainCtrl, this.myDBAbs, selectedEntity );
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
				}
				// Task Done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					System.out.println( "CollectTask:Done[" + newVal + "]" );
					this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
					this.schemaTreeView.setChildrenCnt();
					this.schemaTreeView.setIsLoading(false);
					this.dbView.setBottomMsg(null);
					this.serviceShutdown();
				}
			}
		);
		*/
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.INDEX, null, CollectDataType.LIST );
	}

}
