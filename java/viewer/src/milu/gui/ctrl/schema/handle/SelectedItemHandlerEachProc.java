package milu.gui.ctrl.schema.handle;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

import java.sql.SQLException;

/**
 * This class is invoked, when "procedure" item is clicked on SchemaTreeView.
 * Show "Procedure Definition"
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
 *     - [PROC] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachProc extends SelectedItemHandlerAbstract
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
		String procName   = itemSelected.getValue().getName();
		//String id         = schemaName + "@proc@" + procName;
		String id         = schemaName + this.strPartUserData + procName;
		System.out.println( "setProcDef:" + procName );
		
		// Activate DBSchemaProcViewTab, if already exists.
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
		newTab.setText( procName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );		
		
		// get procedure ddl
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
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
			strSrc = objDBInf.getSRC( schemaName, procName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		// set procedure source in SqlTextArea
		newTab.setSrcText( strSrc );
		*/
		
		this.loadSource( AbsDBFactory.FACTORY_TYPE.PROC, SchemaProcViewTab.class );
	}
	
}
