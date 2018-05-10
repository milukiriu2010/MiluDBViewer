package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root view" item is clicked on SchemaTreeView.
 * Show "View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW] => selected
 *     - [VIEW]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		/*
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.VIEW );
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
				List<SchemaEntity> entityLst = objDBInf.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(entityLst);
				this.schemaTreeView.addEntityLst( itemSelected, entityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists.
		this.removeRelatedTab( SchemaTableViewTab.class );
		*/
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.VIEW, SchemaTableViewTab.class, CollectDataType.LIST );
	}

}
