package milu.gui.ctrl.schema.handle;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaProcViewTab;
import milu.main.MainController;
import milu.tool.MyTool;


/**
 * This class is invoked, when "package definition" item is clicked on SchemaTreeView.
 * Show "Package Source"
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
 *     - [PROC]
 *   - [ROOT_PACAKGE_DEF]
 *     - [PACKAGE_DEF] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachPackageDef extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF )
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
		String schemaName     = itemParent.getParent().getValue().toString();
		String packageDefName = itemSelected.getValue().getName();
		String id             = schemaName + "@package_def@" + packageDefName;
		System.out.println( "setPackageDef:" + packageDefName );
		
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
		newTab.setText( packageDefName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		/*
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/package_def.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		*/
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get package def definition
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			strSrc = objDBInf.getSRC( schemaName, packageDefName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		// set package source in SqlTextArea
		newTab.setSrcText( strSrc );
	}
	
}
