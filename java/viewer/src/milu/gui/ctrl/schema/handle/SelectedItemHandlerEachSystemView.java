package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

/**
 * This class is invoked, when "system view" item is clicked on SchemaTreeView.
 * Show "System View Information"
 * ------------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_SYSTEM_VIEW]
 *     - [SYSTEM_VIEW]    => selected
 * ------------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachSystemView extends SelectedItemHandlerAbstract
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
		String schemaName     = itemParent.getParent().getValue().toString();
		String systemViewName = itemSelected.getValue().getName();
		//String id             = schemaName + "@system_view@" + systemViewName;
		String id         = schemaName + this.strPartUserData + systemViewName;
		System.out.println( "setSystemViewDef:" + systemViewName );
		
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
		newTab.setText( systemViewName );
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
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			dataLst = objDBInf.selectDefinition(schemaName, systemViewName);
			selectedEntity.setDefinitionlst(dataLst);
		}
		*/
		/*
		// table header
		List<String> headLst = new ArrayList<String>();
		headLst.add( "COLUMN" );
		headLst.add( "TYPE" );
		headLst.add( "NULL?" );
		//headLst.add( "DEFAULT" );
		
		// table data
		List<List<String>>  data2Lst = new ArrayList<List<String>>();
		for ( Map<String,String> dataRow1 : dataLst )
		{
			List<String> dataRow2 = new ArrayList<String>();
			dataRow2.add( dataRow1.get("column_name") );
			String dataType = dataRow1.get("data_type");
			String dataSize = dataRow1.get("data_size");
			String dataType2 = "";
			if ( dataSize == null )
			{
				dataType2 = dataType;
			}
			else
			{
				// Oracle => TIMESTAMP(*)
				if ( dataType.contains("(") )
				{
					dataType2 = dataType;
				}
				else
				{
					dataType2 = dataType + "(" + dataSize + ")";
				}
			}
			dataRow2.add( dataType2 );
			dataRow2.add( dataRow1.get("nullable") );
			//dataRow2.add( dataRow1.get("data_default") );
			
			data2Lst.add( dataRow2 );
		}
		
		// set header&data in SqlTableView
		newTab.setTableViewSQL( headLst, data2Lst );
		*/
		/*
		newTab.setTableViewSQL
		( 
			this.createHeadLst(DEFINITION_TYPE.NO_DEFAULT), 
			this.createDataLst(DEFINITION_TYPE.NO_DEFAULT, dataLst) 
		);
		*/
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}

}
