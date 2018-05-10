package milu.gui.ctrl.schema.handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.main.MainController;
import milu.tool.MyTool;

import java.sql.SQLException;

/**
 * This class is invoked, when "table" item is clicked on SchemaTreeView.
 * Show "Table Definition"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE] => selected
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachTable extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		/*
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName = itemParent.getParent().getValue().toString();
		SchemaEntity selectedEntity = itemSelected.getValue();
		String tableName  = itemSelected.getValue().getName();
		//String id         = schemaName + "@table@" + tableName;
		String id         = schemaName + this.strPartUserData + tableName;
		System.out.println( "setTableDef:" + tableName );
		
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaTableViewTab.class , id )) )
		{
			return;
		}
		
		// Create SchemaTableViewTab, if it doesn't exist.
		SchemaTableViewTab newTab = new SchemaTableViewTab(this.dbView);
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setText( tableName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );		
		
		// get table definition
		List<Map<String,String>>  dataLst = selectedEntity.getDefinitionLst();
		if ( dataLst.size() == 0 )
		{
			ObjDBFactory tableDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.TABLE );
			if ( tableDBFactory == null )
			{
				return;
			}
			ObjDBInterface tableDBAbs = tableDBFactory.getInstance(myDBAbs);
			if ( tableDBAbs == null )
			{
				return;
			}
			dataLst = tableDBAbs.selectDefinition(schemaName, tableName);
			selectedEntity.setDefinitionlst(dataLst);
		}
		
		newTab.setTableViewSQL
		( 
			this.createHeadLst(DEFINITION_TYPE.WITH_DEFAULT), 
			this.createDataLst(DEFINITION_TYPE.WITH_DEFAULT, dataLst) 
		);
		*/
		
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.TABLE, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}
	
}
