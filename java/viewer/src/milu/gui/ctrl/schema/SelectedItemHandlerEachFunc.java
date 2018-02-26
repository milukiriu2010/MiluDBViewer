package milu.gui.ctrl.schema;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import milu.db.func.FuncDBAbstract;
import milu.db.func.FuncDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.ctrl.MainController;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.FUNC )
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
		String funcName   = itemSelected.getValue().getName();
		String id         = schemaName + "@func@" + funcName;
		System.out.println( "setFuncDef:" + funcName );
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof DBSchemaProcViewTab ) &&
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
		
		
		// Create DBSchemaProcViewTab, if it doesn't exist.
		DBSchemaProcViewTab newTab = new DBSchemaProcViewTab( this.dbView );
		newTab.setId( id );
		newTab.setText( funcName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/func.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get table definition
		//String strSrc = 
		//		myDBAbs.getFunctionSourceBySchemaFunc( schema, funcName );
		FuncDBAbstract funcDBAbs = FuncDBFactory.getInstance(myDBAbs);
		if ( funcDBAbs == null )
		{
			return;
		}
		String strSrc = funcDBAbs.getSRC( schemaName, funcName );
		
		// set function source in SqlTextArea
		newTab.setSrcText( strSrc );
	}
	
}
