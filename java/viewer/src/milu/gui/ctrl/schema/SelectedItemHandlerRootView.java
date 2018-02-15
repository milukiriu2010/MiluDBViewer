package milu.gui.ctrl.schema;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import milu.db.MyDBAbstract;
import milu.db.view.ViewDBFactory;
import milu.entity.schema.SchemaEntity;
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
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			String schema = itemParent.getValue().toString();
			/*
			List<List<String>> dataLst = myDBAbs.getSchemaView( schema );
			this.schemaTreeView.setViewData( itemSelected, dataLst );
			*/
			ViewDBAbstract viewDBAbs = ViewDBFactory.getInstance(myDBAbs);
			if ( viewDBAbs != null )
			{
				viewDBAbs.selectViewLst( schema );
				List<Map<String,String>> dataLst = viewDBAbs.getDataLst();
				this.schemaTreeView.setViewData( itemSelected, dataLst );
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
					( tab instanceof DBSchemaTableViewTab ) &&
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
