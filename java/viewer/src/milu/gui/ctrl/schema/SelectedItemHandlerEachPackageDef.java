package milu.gui.ctrl.schema;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import milu.db.packagedef.PackageDefDBAbstract;
import milu.db.packagedef.PackageDefDBFactory;
import milu.entity.schema.SchemaEntity;

import java.sql.SQLException;

/**
 * This class is invoked, when "package definition" item is clicked on SchemaTreeView.
 * Show "Package Source"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC]
 *     - [FUNC]
 *   - [ROOT_PROC]
 *     - [PROC]
 *   - [ROOT_PACAKGE_DEF]
 *     - [PACKAGE_DEF] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachPackageDef extends SelectedItemHandlerAbstract
{
	/*
	public SelectedItemHandlerEachPackageDef
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	*/
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF )
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
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName     = itemParent.getParent().getValue().toString();
		String packageDefName = itemSelected.getValue().getName();
		String id             = schemaName + "@package_def@" + packageDefName;
		System.out.println( "setPackageDef:" + packageDefName );
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof DBSchemaProcViewTab ) &&
				id.equals(tab.getId())
			)
			{
				// Activate DBSchemaTableViewTab, if already exists.
				if ( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
				{
					this.tabPane.getSelectionModel().select( tab );
					return;
				}
				// Delete DBSchemaTableViewTab, if already exists. 
				else
				{
					this.tabPane.getTabs().remove( tab );
					break;
				}
			}
		}		
		
		
		// Create DBSchemaProcViewTab, if it doesn't exist.
		DBSchemaProcViewTab newTab = new DBSchemaProcViewTab();
		newTab.setId( id );
		newTab.setText( packageDefName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		ImageView iv = new ImageView( new Image("file:resources/images/package_def.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get table definition
		//String strSrc = 
		//		myDBAbs.getPackageDefSourceBySchemaPackageDef( schema, packageDefName );
		PackageDefDBAbstract packageDefDBAbs = PackageDefDBFactory.getInstance(myDBAbs);
		if ( packageDefDBAbs == null )
		{
			return;
		}
		String strSrc = packageDefDBAbs.getSRC(schemaName, packageDefName);
		
		// set package source in SqlTextArea
		newTab.setSrcText( strSrc );
	}
	
}
