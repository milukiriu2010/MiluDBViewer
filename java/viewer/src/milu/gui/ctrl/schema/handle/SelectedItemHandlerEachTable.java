package milu.gui.ctrl.schema.handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import milu.db.table.TableDBAbstract;
import milu.db.table.TableDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.ctrl.MainController;

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
 * @author miluk
 *
 */
public class SelectedItemHandlerEachTable extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.TABLE )
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
		String schemaName = itemParent.getParent().getValue().toString();
		SchemaEntity selectedEntity = itemSelected.getValue();
		String tableName  = itemSelected.getValue().getName();
		String id         = schemaName + "@table@" + tableName;
		System.out.println( "setTableDef:" + tableName );
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof SchemaTableViewTab ) &&
				id.equals(tab.getId())
			)
			{
				// NO Refresh
				// Activate SchemaTableViewTab, if already exists.
				if ( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
				{
					this.tabPane.getSelectionModel().select( tab );
					return;
				}
				// Refresh
				// Delete SchemaTableViewTab, if already exists. 
				else
				{
					this.tabPane.getTabs().remove( tab );
					break;
				}
			}
		}		
		
		// Create SchemaTableViewTab, if it doesn't exist.
		SchemaTableViewTab newTab = new SchemaTableViewTab();
		newTab.setId( id );
		newTab.setText( tableName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/table.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get table definition
		List<Map<String,String>>  dataLst = selectedEntity.getDefinitionLst();
		if ( dataLst.size() == 0 )
		{
			TableDBAbstract tableDBAbs = TableDBFactory.getInstance(this.myDBAbs);
			if ( tableDBAbs == null )
			{
				return;
			}
			dataLst = tableDBAbs.selectDefinition(schemaName, tableName);
		}
		
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
	}
	
}