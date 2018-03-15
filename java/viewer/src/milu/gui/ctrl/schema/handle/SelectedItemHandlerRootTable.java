package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.ctrl.visitor.SearchSchemaEntityInterface;
import milu.ctrl.visitor.SearchSchemaEntityVisitorFactory;

/**
 * This class is invoked, when "root table" item is clicked on SchemaTreeView.
 * Show "Table List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE] => selected
 *     - [TABLE]    => get
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootTable extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_TABLE )
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
		
		// get Table List & add list as children
		if ( itemChildren.size() == 0 )
		{
			String schemaName = itemParent.getValue().toString();
			
			// Search [SCHEMA]
			SearchSchemaEntityInterface sseVisitorTN = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA, schemaName);
			this.myDBAbs.getSchemaRoot().accept(sseVisitorTN);
			SchemaEntity schemaEntity = sseVisitorTN.getHitSchemaEntity();
			
			// Search [ROOT_TABLE]
			SearchSchemaEntityInterface sseVisitorT = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
			schemaEntity.accept( sseVisitorT );
			SchemaEntity rootTableEntity = sseVisitorT.getHitSchemaEntity();
			
			if ( rootTableEntity != null )
			{
				this.schemaTreeView.setTableData( itemSelected, rootTableEntity.getEntityLst() );
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
					( tab.getId().contains("@table@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
	}

}
