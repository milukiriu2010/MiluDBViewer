package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

/**
 * This class is invoked, when "view" item is clicked on SchemaTreeView.
 * Show "View Information"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]    => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachView extends SelectedItemHandlerAbstract
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
		String schemaName = itemParent.getParent().getValue().toString();
		String viewName   = itemSelected.getValue().getName();
		//String id         = schemaName + "@view@" + viewName;
		String id         = schemaName + this.strPartUserData + viewName;
		System.out.println( "setViewDef:" + viewName );
		
		// Activate DBSchemaTableViewTab, if already exists.
		// Delete DBSchemaTableViewTab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaTableViewTab.class , id )) )
		{
			return;
		}
		
		// Create DBSchemaTableViewTab, if it doesn't exist.
		SchemaTableViewTab newTab = new SchemaTableViewTab(this.dbView);
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setText( viewName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get view definition
		List<Map<String,String>>  dataLst = selectedEntity.getDefinitionLst();
		if ( dataLst.size() == 0 )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.VIEW );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			dataLst = objDBInf.selectDefinition(schemaName, viewName);
			selectedEntity.setDefinitionlst(dataLst);
		}
		
		newTab.setTableViewSQL
		( 
			this.createHeadLst(DEFINITION_TYPE.NO_DEFAULT), 
			this.createDataLst(DEFINITION_TYPE.NO_DEFAULT, dataLst) 
		);
		*/
		
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.VIEW, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}

}
