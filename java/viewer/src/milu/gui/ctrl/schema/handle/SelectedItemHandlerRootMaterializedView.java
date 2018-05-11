package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root materialized view" item is clicked on SchemaTreeView.
 * Show "Materialized View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_MATERIALIZED_VIEW] => selected
 *     - [MATERIALIZED_VIEW]    => get
 *   - [ROOT_FUNC]
 *     - [FUNC]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootMaterializedView extends SelectedItemHandlerAbstract
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
				ObjDBFactory materializedViewDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW );
				if ( materializedViewDBFactory == null )
				{
					return;
				}
				ObjDBInterface materializedViewDBAbs = materializedViewDBFactory.getInstance(myDBAbs);
				if ( materializedViewDBAbs == null )
				{
					return;
				}
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> materializedViewEntityLst = materializedViewDBAbs.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(materializedViewEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, materializedViewEntityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaTableViewTab.class );
		*/
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaTableViewTab.class, CollectDataType.LIST );
		
		//this.loadChildLst( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaTableViewTab.class, CollectDataType.LIST_AND_DEFINITION );
	}

}
