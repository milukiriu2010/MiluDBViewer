package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.collect.CollectDataType;
import milu.task.collect.CollectTaskFactory;


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
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		//TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		/*
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SEQUENCE );
				if ( objDBFactory == null )
				{
					return;
				}
				ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
				if ( objDBInf == null )
				{
					return;
				}
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> sequenceEntityLst = objDBInf.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(sequenceEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, sequenceEntityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		*/
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
		
		
	}

}
