package milu.gui.ctrl.schema;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.db.packagedef.PackageDefDBAbstract;
import milu.db.packagedef.PackageDefDBFactory;
import milu.entity.schema.SchemaEntity;

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
	public SelectedItemHandlerRootPackageDef
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
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF )
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
			List<List<String>> dataLst = myDBAbs.getSchemaPackageDef( schema );
			this.schemaTreeView.setPackageDefData( itemSelected, dataLst );
			*/
			PackageDefDBAbstract packageDefDBAbs = PackageDefDBFactory.getInstance(myDBAbs);
			if ( packageDefDBAbs != null )
			{
				String schemaName = itemParent.getValue().toString();
				packageDefDBAbs.selectPackageDefLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, packageDefDBAbs.getEntityLst() );
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
					( tab instanceof DBSchemaProcViewTab ) &&
					( tab.getId().contains("@package_def@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
	}

}
