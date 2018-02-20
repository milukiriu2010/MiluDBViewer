package milu.gui.ctrl.schema;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import milu.db.MyDBAbstract;
import milu.db.type.TypeDBAbstract;
import milu.db.type.TypeDBFactory;
import milu.entity.schema.SchemaEntity;

import java.sql.SQLException;

/**
 * This class is invoked, when "type" item is clicked on SchemaTreeView.
 * Show "Type Source"
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
 *     - [PACKAGE_DEF]
 *   - [ROOT_PACAKGE_BODY]
 *     - [PACKAGE_BODY]
 *   - [ROOT_TYPE]
 *     - [TYPE] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachType extends SelectedItemHandlerAbstract
{
	public SelectedItemHandlerEachType
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.TYPE )
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
		String typeName   = itemSelected.getValue().getName();
		String id         = schemaName + "@type@" + typeName;
		System.out.println( "setType:" + typeName );
		
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
		DBSchemaProcViewTab newTab = new DBSchemaProcViewTab();
		newTab.setId( id );
		newTab.setText( typeName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		ImageView iv = new ImageView( new Image("file:resources/images/type.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );
		
		// get table definition
		//String strSrc = 
		//		myDBAbs.getTypeSourceBySchemaType( schema, typeName );
		TypeDBAbstract typeDBAbs = TypeDBFactory.getInstance(myDBAbs);
		if ( typeDBAbs == null )
		{
			return;
		}
		String strSrc = typeDBAbs.getSRC(schemaName, typeName);
		
		// set type source in SqlTextArea
		newTab.setSrcText( strSrc );
	}
	
}
