package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.type.TypeDBAbstract;
import milu.db.type.TypeDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaProcViewTab;

/**
 * This class is invoked, when "root type" item is clicked on SchemaTreeView.
 * Show "Type List"
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
 *   - [ROOT_TYPE] => selected
 *     - [TYPE]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootType extends SelectedItemHandlerAbstract
{
	/*
	public SelectedItemHandlerRootType
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
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_TYPE )
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
			/*
			String schema = itemParent.getValue().toString();
			List<List<String>> dataLst = myDBAbs.getSchemaType( schema );
			this.schemaTreeView.setTypeData( itemSelected, dataLst );
			*/
			TypeDBAbstract typeDBAbs = TypeDBFactory.getInstance(myDBAbs);
			if ( typeDBAbs != null )
			{
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> typeEntityLst = typeDBAbs.selectEntityLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, typeEntityLst );
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
					( tab instanceof SchemaProcViewTab ) &&
					( tab.getId().contains("@type@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		
	}

}