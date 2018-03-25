package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;
import milu.db.abs.AbsDBFactory;
import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.ctrl.MainController;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW )
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
		String schameName     = itemParent.getParent().getValue().toString();
		String systemViewName = itemSelected.getValue().getName();
		String id             = schameName + "@system_view@" + systemViewName;
		System.out.println( "setSystemViewDef:" + systemViewName );
		
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
		newTab.setText( systemViewName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/systemview.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get view definition
		List<Map<String,String>>  dataLst = selectedEntity.getDefinitionLst();
		if ( dataLst.size() == 0 )
		{
			ObjDBFactory systemViewDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW );
			if ( systemViewDBFactory == null )
			{
				return;
			}
			ObjDBInterface systemViewDBAbs = systemViewDBFactory.getInstance(myDBAbs);
			if ( systemViewDBAbs == null )
			{
				return;
			}
			dataLst = systemViewDBAbs.selectDefinition(schameName, systemViewName);
			selectedEntity.setDefinitionlst(dataLst);
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
