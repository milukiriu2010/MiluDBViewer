package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;


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
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		/*
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName     = itemParent.getParent().getValue().toString();
		String packageDefName = itemSelected.getValue().getName();
		//String id             = schemaName + "@package_def@" + packageDefName;
		String id         = schemaName + this.strPartUserData + packageDefName;
		System.out.println( "setPackageDef:" + packageDefName );
		
		// Activate DBSchemaTableViewTab, if already exists.
		// Delete DBSchemaTableViewTab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaProcViewTab.class , id )) )
		{
			return;
		}
		
		// Create DBSchemaProcViewTab, if it doesn't exist.
		SchemaProcViewTab newTab = new SchemaProcViewTab( this.dbView );
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setText( packageDefName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get package def definition
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			strSrc = objDBInf.getSRC( schemaName, packageDefName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		// set package source in SqlTextArea
		newTab.setSrcText( strSrc );
		*/
		
		this.loadSource( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF, SchemaProcViewTab.class );
	}
	
}
