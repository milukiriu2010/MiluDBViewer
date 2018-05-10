package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

/**
 * This class is invoked, when "root proc" item is clicked on SchemaTreeView.
 * Show "Procedure List"
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
 *   - [ROOT_PROC] => selected
 *     - [PROC]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootProc extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		/*
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get procedure List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.PROC );
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
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaProcViewTab.class );
		*/
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.PROC, SchemaProcViewTab.class );
	}

}
