package milu.gui.ctrl.schema;

import java.sql.SQLException;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.db.index.IndexDBAbstract;
import milu.db.index.IndexDBFactory;
import milu.entity.schema.SchemaEntity;

/**
 * This class is invoked, when "root index" item is clicked on SchemaTreeView.
 * Show "Index Information"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX] => selected
 *         - [INDEX]    => get
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author miluk
 *
 */
public class SelectedItemHandlerRootIndex extends SelectedItemHandlerAbstract
{
	public SelectedItemHandlerRootIndex
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
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_INDEX )
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
		
		if ( itemChildren.size() == 0 )
		{
			/*
			String schema = itemParent.getParent().getParent().getValue().toString();
			String table  = itemParent.getValue().toString();
			List<Map<String,String>> dataLst = 
					myDBAbs.getIndexBySchemaTable( schema, table );
			this.schemaTreeView.setIndexData( itemSelected, dataLst );
			*/
			String schemaName = itemParent.getParent().getParent().getValue().toString();
			String tableName  = itemParent.getValue().toString();
			IndexDBAbstract indexDBAbs = IndexDBFactory.getInstance( this.myDBAbs );
			if ( indexDBAbs != null )
			{
				indexDBAbs.selectEntityLst(schemaName, tableName);
				this.schemaTreeView.addEntityLst( this.itemSelected, indexDBAbs.getEntityLst() );
			}
		}
	}

}
