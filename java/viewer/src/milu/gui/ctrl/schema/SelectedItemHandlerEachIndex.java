package milu.gui.ctrl.schema;

import javafx.scene.control.TreeItem;

import milu.db.indexcolumn.IndexColumnDBAbstract;
import milu.db.indexcolumn.IndexColumnDBFactory;
import milu.entity.schema.SchemaEntity;

import java.sql.SQLException;

/**
 * This class is invoked, when "index" item is clicked on SchemaTreeView.
 * Show "Index Column List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]          => selected
 *           - [INDEX_COLUMN] => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachIndex extends SelectedItemHandlerAbstract
{
	/*
	public SelectedItemHandlerEachIndex
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
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.INDEX )
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
		if ( this.itemSelected.getChildren().size() > 0 )
		{
			return;
		}
		
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName = itemParent.getParent().getParent().getParent().getValue().toString();
		String tableName  = this.itemSelected.getParent().getParent().getValue().getName();
		String indexName  = this.itemSelected.getValue().getName();
		/*
		System.out.println( this.getClass() + "=>true" );
		System.out.println( "schemaName:" + schemaName );
		System.out.println( "tableName: " + tableName );
		System.out.println( "indexName: " + indexName );
		*/
		
		IndexColumnDBAbstract indexColumnDBAbs = IndexColumnDBFactory.getInstance( this.myDBAbs );
		if ( indexColumnDBAbs != null )
		{
			indexColumnDBAbs.selectEntityLst( schemaName, tableName, indexName );
			this.schemaTreeView.addEntityLst( itemSelected, indexColumnDBAbs.getEntityLst() );
			//this.schemaTreeView.setIndexColumnData( this.itemSelected, indexColumnDBAbs.getDataLst() );
		}
		
		/*
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schema    = itemParent.getParent().getValue().toString();
		String tableName = itemSelected.getValue().getName();
		String id        = schema + "@table@" + tableName;
		System.out.println( "setTableDef:" + tableName );
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof DBSchemaTableViewTab ) &&
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
		
		// Create DBSchemaTableViewTab, if it doesn't exist.
		DBSchemaTableViewTab newTab = new DBSchemaTableViewTab();
		newTab.setId( id );
		newTab.setText( tableName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		ImageView iv = new ImageView( new Image("file:resources/images/table.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get table definition
		List<Map<String,String>> dataLst = 
				myDBAbs.getTableDefBySchemaTable( schema, tableName );
		
		// table header
		List<String> headLst = new ArrayList<String>();
		headLst.add( "COLUMN" );
		headLst.add( "TYPE" );
		headLst.add( "NULL?" );
		headLst.add( "DEFAULT" );
		
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
				dataType2 = dataType + "(" + dataSize + ")";
			}
			dataRow2.add( dataType2 );
			dataRow2.add( dataRow1.get("nullable") );
			dataRow2.add( dataRow1.get("data_default") );
			
			data2Lst.add( dataRow2 );
		}
		
		// set header&data in SqlTableView
		newTab.setTableViewSQL( headLst, data2Lst );
		*/
	}
	
}
