package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;

import milu.db.view.ViewDBAbstract;
import milu.db.view.ViewDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.ctrl.MainController;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.VIEW )
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
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		String schemaName = itemParent.getParent().getValue().toString();
		String viewName   = itemSelected.getValue().getName();
		String id         = schemaName + "@view@" + viewName;
		System.out.println( "setViewDef:" + viewName );
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof SchemaTableViewTab ) &&
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
		SchemaTableViewTab newTab = new SchemaTableViewTab();
		newTab.setId( id );
		newTab.setText( viewName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/view.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get view definition
		List<Map<String,String>> dataLst = null;
		if ( selectedEntity.getDefinitionLst().size() == 0 )
		{
			ViewDBAbstract viewDBAbs = ViewDBFactory.getInstance(myDBAbs);
			if ( viewDBAbs == null )
			{
				return;
			}
			dataLst = viewDBAbs.selectDefinition(schemaName, viewName );
			selectedEntity.setDefinitionlst(dataLst);
		}
		else
		{
			dataLst = selectedEntity.getDefinitionLst();
		}
		
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
				dataType2 = dataType + "(" + dataSize + ")";
			}
			dataRow2.add( dataType2 );
			dataRow2.add( dataRow1.get("nullable") );
			//dataRow2.add( dataRow1.get("data_default") );
			
			data2Lst.add( dataRow2 );
		}
		
		// set header&data in SqlTableView
		newTab.setTableViewSQL( headLst, data2Lst );
	}

}
