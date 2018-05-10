package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

/**
 * This class is invoked, when "root system view" item is clicked on SchemaTreeView.
 * Show "System View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_SYSTEM_VIEW]
 *     - [SYSTEM_VIEW]
 *   - [ROOT_MATERIALIZED_VIEW] => selected
 *     - [MATERIALIZED_VIEW]    => get
 *   - [ROOT_FUNC]
 *     - [FUNC]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootSystemView extends SelectedItemHandlerAbstract
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
				ObjDBFactory systemViewDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW );
				if ( systemViewDBFactory == null )
				{
					return;
				}
				ObjDBInterface systemViewDBAbs = systemViewDBFactory.getInstance(myDBAbs);
				if ( systemViewDBAbs == null )
				{
					return;
				}
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> systemViewEntityLst = systemViewDBAbs.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(systemViewEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, systemViewEntityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaTableViewTab.class );
		*/
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW, SchemaTableViewTab.class );
	}

}
