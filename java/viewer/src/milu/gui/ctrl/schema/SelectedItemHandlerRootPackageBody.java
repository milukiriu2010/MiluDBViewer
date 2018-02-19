package milu.gui.ctrl.schema;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.db.packagebody.PackageBodyDBAbstract;
import milu.db.packagebody.PackageBodyDBFactory;
import milu.entity.schema.SchemaEntity;

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
	public SelectedItemHandlerRootPackageBody
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
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY )
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
			List<List<String>> dataLst = myDBAbs.getSchemaPackageBody( schema );
			this.schemaTreeView.setPackageBodyData( itemSelected, dataLst );
			*/
			PackageBodyDBAbstract packageBodyDBAbs = PackageBodyDBFactory.getInstance(myDBAbs);
			if ( packageBodyDBAbs != null )
			{
				String schemaName = itemParent.getValue().toString();
				packageBodyDBAbs.selectEntityLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, packageBodyDBAbs.getEntityLst() );
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
					( tab.getId().contains("@package_body@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		
	}

}
