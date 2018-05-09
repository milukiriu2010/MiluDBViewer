package milu.gui.ctrl.schema.handle;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.collect.CollectDataType;
import milu.task.collect.CollectTaskFactory;

import java.sql.SQLException;

/**
 * This class is invoked, when "index" item is clicked on SchemaTreeView.
 * Show "Index Column List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]          => selected
 *           - [INDEX_COLUMN] => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachIndex extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		//TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		//String schemaName = itemParent.getParent().getParent().getParent().getValue().toString();
		//String tableName  = this.itemSelected.getParent().getParent().getValue().getName();
		//String indexName  = this.itemSelected.getValue().getName();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		/*
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN );
				if ( objDBFactory == null )
				{
					return;
				}
				ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
				if ( objDBInf == null )
				{
					return;
				}
				List<SchemaEntity> entityLst = ((IndexColumnDBAbstract)objDBInf).selectEntityLst(schemaName,tableName,indexName);
				selectedEntity.addEntityAll(entityLst);
				this.schemaTreeView.addEntityLst( itemSelected, entityLst, true );
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
		
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN, CollectDataType.LIST, mainCtrl, this.myDBAbs, selectedEntity );
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
