package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

/**
 * This class is invoked, when "root package body" item is clicked on SchemaTreeView.
 * Show "Package Body List"
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
 *   - [ROOT_PACAKGE_DEF]
 *     - [PACKAGE_DEF]
 *   - [ROOT_PACAKGE_BODY] => selected
 *     - [PACKAGE_BODY]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootPackageBody extends SelectedItemHandlerAbstract
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
		
		// get package body List & add list as children
		if ( itemChildren.size() == 0 )
		{
			if ( selectedEntity.getEntityLst().size() == 0 )
			{
				ObjDBFactory packageBodyDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.PACKAGE_BODY );
				if ( packageBodyDBFactory == null )
				{
					return;
				}
				ObjDBInterface packageBodyDBAbs = packageBodyDBFactory.getInstance(myDBAbs);
				if ( packageBodyDBAbs == null )
				{
					return;
				}
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> packageBodyEntityLst = packageBodyDBAbs.selectEntityLst(schemaName);
				selectedEntity.addEntityAll(packageBodyEntityLst);
				this.schemaTreeView.addEntityLst( itemSelected, packageBodyEntityLst, true );
			}
			else
			{
				this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		this.removeRelatedTab( SchemaProcViewTab.class );
		*/
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.PACKAGE_BODY, SchemaProcViewTab.class );
	}

}
