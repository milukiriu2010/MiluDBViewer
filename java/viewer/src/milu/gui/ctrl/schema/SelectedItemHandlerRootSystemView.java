package milu.gui.ctrl.schema;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

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
	public SelectedItemHandlerRootSystemView
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			String schema = itemParent.getValue().toString();
			List<List<String>> dataLst = myDBAbs.getSchemaSystemView( schema );
			this.schemaTreeView.setSystemViewData( itemSelected, dataLst );
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		if ( this.refreshType == SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH )
		{
			final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
			ObservableList<Tab> relatedTabLst = FXCollections.observableArrayList();
			for ( Tab tab : tabLst )
			{
				if (
					( tab instanceof DBSchemaTableViewTab ) &&
					( tab.getId().contains("@system_view@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		
	}

}
