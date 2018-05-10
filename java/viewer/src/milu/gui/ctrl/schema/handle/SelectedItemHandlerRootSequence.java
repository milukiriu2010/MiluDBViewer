package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.task.collect.CollectDataType;


/**
 * This class is invoked, when "root sequence" item is clicked on SchemaTreeView.
 * Show "Sequence List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC]
 *     - [FUNC]
 *   - [ROOT_TYPE]
 *     - [TYPE]
 *   - [ROOT_TRIGGER]
 *     - [TRIGGER]
 *   - [ROOT_SEQUENCE] => selected
 *     - [SEQUENCE]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootSequence extends SelectedItemHandlerAbstract
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
		
		if ( itemChildren.size() > 0 )
		{
			return;
		}
		
		// get list & add list as children
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( AbsDBFactory.FACTORY_TYPE.SEQUENCE,  CollectDataType.LIST,mainCtrl, this.myDBAbs, selectedEntity );
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
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.SEQUENCE, null, CollectDataType.LIST );
	}

}
