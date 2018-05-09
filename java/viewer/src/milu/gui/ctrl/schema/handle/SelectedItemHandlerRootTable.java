package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

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
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get Table List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.TABLE );
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
					this.schemaTreeView.addEntityLst( itemSelected, rootTableEntity.getEntityLst(), true );
				}
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaTableViewTab.class );
		/*
		if ( this.refreshType == SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH )
		{
			final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
			ObservableList<Tab> relatedTabLst = FXCollections.observableArrayList();
			for ( Tab tab : tabLst )
			{
				if (
					( tab instanceof SchemaTableViewTab ) &&
					//( tab.getId().contains("@table@") == true )
					( ((String)tab.getUserData()).contains("@table@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		*/
	}

}
