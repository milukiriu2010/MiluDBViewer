package milu.gui.ctrl.schema;

import javafx.scene.control.TreeItem;

import milu.db.indexcolumn.IndexColumnDBAbstract;
import milu.db.indexcolumn.IndexColumnDBFactory;
import milu.entity.schema.SchemaEntity;

import java.sql.SQLException;

/**
 * This class is invoked, when "index" item is clicked on SchemaTreeView.
 * Show "Index Column List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]          => selected
 *           - [INDEX_COLUMN] => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachIndex extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.INDEX )
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
		if ( this.itemSelected.getChildren().size() > 0 )
		{
			return;
		}
		
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName = itemParent.getParent().getParent().getParent().getValue().toString();
		String tableName  = this.itemSelected.getParent().getParent().getValue().getName();
		String indexName  = this.itemSelected.getValue().getName();
		
		IndexColumnDBAbstract indexColumnDBAbs = IndexColumnDBFactory.getInstance( this.myDBAbs );
		if ( indexColumnDBAbs != null )
		{
			indexColumnDBAbs.selectEntityLst( schemaName, tableName, indexName );
			this.schemaTreeView.addEntityLst( itemSelected, indexColumnDBAbs.getEntityLst() );
		}
	}
	
}
