package milu.gui.ctrl.schema.handle;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

import java.sql.SQLException;

/**
 * This class is invoked, when "function" item is clicked on SchemaTreeView.
 * Show "Function Source"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC]
 *     - [FUNC] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachFunc extends SelectedItemHandlerAbstract
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
		String schemaName = itemParent.getParent().getValue().toString();
		String funcName   = itemSelected.getValue().getName();
		String id         = schemaName + this.strPartUserData + funcName;
		System.out.println( "setFuncDef:" + funcName );
		
		// Activate Tab, if already exists.
		// Delete Tab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaProcViewTab.class , id )) )
		{
			return;
		}
		
		// Create DBSchemaProcViewTab, if it doesn't exist.
		SchemaProcViewTab newTab = new SchemaProcViewTab( this.dbView );
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setText( funcName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get function ddl
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.FUNC );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			strSrc = objDBInf.getSRC( schemaName, funcName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		// set function source in SqlTextArea
		newTab.setSrcText( strSrc );
		*/
		
		this.loadSource( AbsDBFactory.FACTORY_TYPE.FUNC, SchemaProcViewTab.class );
	}
}
