package milu.gui.ctrl.schema.handle;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

import java.sql.SQLException;

/**
 * This class is invoked, when "trigger" item is clicked on SchemaTreeView.
 * Show "Type Source"
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
 *     - [PACKAGE_DEF]
 *   - [ROOT_PACAKGE_BODY]
 *     - [PACKAGE_BODY]
 *   - [ROOT_TYPE]
 *     - [TYPE]
 *   - [ROOT_TRIGGER]
 *     - [TRIGGER] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachTrigger extends SelectedItemHandlerAbstract
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
		String schemaName   = itemParent.getParent().getValue().toString();
		String triggerName  = itemSelected.getValue().getName();
		//String id           = schemaName + "@trigger@" + triggerName;
		String id         = schemaName + this.strPartUserData + triggerName;
		System.out.println( "setTrigger:" + triggerName );
		
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
		newTab.setText( triggerName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );		
		
		// get trigger definition
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.TRIGGER );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			strSrc = objDBInf.getSRC( schemaName, triggerName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		
		// set type source in SqlTextArea
		newTab.setSrcText( strSrc );
		*/
		
		this.loadSource( AbsDBFactory.FACTORY_TYPE.TRIGGER, SchemaProcViewTab.class );
	}
	
}
