package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;


/**
 * This class is invoked, when "root sequence" item is clicked on SchemaTreeView.
 * Show "Sequence List"
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
 *   - [ROOT_TYPE]
 *     - [TYPE]
 *   - [ROOT_TRIGGER]
 *     - [TRIGGER]
 *   - [ROOT_SEQUENCE] => selected
 *     - [SEQUENCE]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootSequence extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE )
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
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SEQUENCE );
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
				List<SchemaEntity> sequenceEntityLst = objDBInf.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(sequenceEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, sequenceEntityLst );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst() );
			}
		}
	}

}
