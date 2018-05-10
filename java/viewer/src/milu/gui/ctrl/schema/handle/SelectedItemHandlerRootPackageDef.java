package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root package definition" item is clicked on SchemaTreeView.
 * Show "Package List"
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
 *   - [ROOT_PROC]
 *     - [PROC]
 *   - [ROOT_PACAKGE_DEF] => selected
 *     - [PACKAGE_DEF]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootPackageDef extends SelectedItemHandlerAbstract
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
		
		// get package definition List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory packageDefDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF );
				if ( packageDefDBFactory == null )
				{
					return;
				}
				ObjDBInterface packageDefDBAbs = packageDefDBFactory.getInstance(myDBAbs);
				if ( packageDefDBAbs == null )
				{
					return;
				}
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> packageDefEntityLst = packageDefDBAbs.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(packageDefEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, packageDefEntityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaProcViewTab.class );
		*/
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF, SchemaProcViewTab.class, CollectDataType.LIST );
	}

}
