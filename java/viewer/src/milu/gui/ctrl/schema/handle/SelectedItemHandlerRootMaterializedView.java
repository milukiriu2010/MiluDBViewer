package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
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
		if ( this.refreshType == SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH )
		{
			final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
			ObservableList<Tab> relatedTabLst = FXCollections.observableArrayList();
			for ( Tab tab : tabLst )
			{
				if (
					( tab instanceof SchemaTableViewTab ) &&
					( tab.getId().contains("@materialized_view@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		
	}

}
