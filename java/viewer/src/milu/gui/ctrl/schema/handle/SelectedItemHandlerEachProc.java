package milu.gui.ctrl.schema.handle;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaProcViewTab;
import milu.main.MainController;
import milu.tool.MyTool;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.PROC )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName = itemParent.getParent().getValue().toString();
		String procName   = itemSelected.getValue().getName();
		String id         = schemaName + "@proc@" + procName;
		System.out.println( "setProcDef:" + procName );
		
		// Activate DBSchemaProcViewTab, if already exists.
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof SchemaProcViewTab ) &&
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
		SchemaProcViewTab newTab = new SchemaProcViewTab( this.dbView );
		newTab.setId( id );
		newTab.setText( procName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		/*
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/proc.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		*/
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );		
		
		// get procedure ddl
		/*
		ProcDBAbstract procDBAbs = ProcDBFactory.getInstance(myDBAbs);
		if ( procDBAbs == null )
		{
			return;
		}
		String strSrc = procDBAbs.getSRC(schemaName, procName);
		*/
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
	}
	
}
