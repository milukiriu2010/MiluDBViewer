package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import milu.db.view.ViewDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.db.view.ViewDBAbstract;

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
	/*
	public SelectedItemHandlerRootView
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	*/
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_VIEW )
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
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				String schema = itemParent.getValue().toString();
				ViewDBAbstract viewDBAbs = ViewDBFactory.getInstance(myDBAbs);
				if ( viewDBAbs != null )
				{
					List<SchemaEntity> viewEntityLst = viewDBAbs.selectEntityLst( schema );
					selectedEntity.addEntityAll(viewEntityLst);
					this.schemaTreeView.addEntityLst( itemSelected, viewEntityLst );
				}
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst() );
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
					( tab.getId().contains("@view@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
	}

}
