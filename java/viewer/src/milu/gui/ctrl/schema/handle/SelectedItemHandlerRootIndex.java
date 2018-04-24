package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.obj.index.IndexDBAbstract;
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
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get function List & add list as children
		/*
		if ( itemChildren.size() == 0 )
		{
			String schemaName = itemParent.getParent().getParent().getValue().toString();
			String tableName  = itemParent.getValue().toString();
			IndexDBAbstract indexDBAbs = IndexDBFactory.getInstance( this.myDBAbs );
			if ( indexDBAbs != null )
			{
				indexDBAbs.selectEntityLst(schemaName, tableName);
				this.schemaTreeView.addEntityLst( this.itemSelected, indexDBAbs.getEntityLst() );
			}
		}
		*/
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.INDEX );
				if ( objDBFactory == null )
				{
					return;
				}
				ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
				if ( objDBInf == null )
				{
					return;
				}
				String schemaName = itemParent.getParent().getParent().getValue().toString();
				String tableName  = itemParent.getValue().toString();
				List<SchemaEntity> entityLst = ((IndexDBAbstract)objDBInf).selectEntityLst(schemaName,tableName);
				selectedEntity.addEntityAll(entityLst);
				this.schemaTreeView.addEntityLst( itemSelected, entityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}		
	}

}
