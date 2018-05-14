package milu.gui.ctrl.schema;

import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import milu.ctrl.sqlgenerate.GenerateSQLAbstract;
import milu.ctrl.sqlgenerate.GenerateSQLFactory;
import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.gui.ctrl.common.inf.ChangeLangInterface;

public class SchemaTreeContextMenu extends ContextMenu 
	implements
		ChangeLangInterface
{
	// Control View
	private DBView  dbView = null;
	
	private SchemaTreeView schemaTreeView = null;
	
	// [Refresh]
	private MenuItem  menuItemRefresh = new MenuItem();
	
	// [Copy Object Name]
	private MenuItem  menuItemCopyObjectName = new MenuItem();
	
	// [Generate SELECT]
	private MenuItem  menuItemGenerateSelect = new MenuItem();
	
	// [Generate INSERT]
	private Menu  subMenuGenerateInsert = new Menu();
	
	// [Generate INSERT]-[PlaceHolder by Name]
	private MenuItem  menuItemInsertPlaceHolderName = new MenuItem();
	
	// [Generate INSERT]-[PlaceHolder by ?]
	private MenuItem  menuItemInsertPlaceHolderSimple = new MenuItem();
	
	// [Generate UPDATE]
	private Menu  subMenuGenerateUpdate = new Menu();
	
	// [Generate UPDATE]-[PlaceHolder by Name]
	private MenuItem  menuItemUpdatePlaceHolderName = new MenuItem();
	
	// [Generate UPDATE]-[PlaceHolder by ?]
	private MenuItem  menuItemUpdatePlaceHolderSimple = new MenuItem();
	
	// [Generate DELETE]
	private MenuItem  menuItemGenerateDelete = new MenuItem();

	SchemaTreeContextMenu( DBView dbView, SchemaTreeView schemaTreeView )
	{
		super();
		
		this.dbView = dbView;
		this.schemaTreeView = schemaTreeView;
		
		this.changeLang();
		
		this.setAction();
	}
	
	private void setAction()
	{
		this.menuItemRefresh.setOnAction
		( 
			(event)->
			{ 
				TreeItem<SchemaEntity>  itemSelected = this.schemaTreeView.getSelectionModel().getSelectedItem();
				SchemaEntity selectedEntity = itemSelected.getValue();
				SchemaEntity.SCHEMA_TYPE schemaType = selectedEntity.getType();
				switch ( schemaType )
				{
					case TABLE:
						// remove grand children & refresh
						ObservableList<TreeItem<SchemaEntity>>  itemChildren1 = itemSelected.getChildren();
						for ( TreeItem<SchemaEntity> itemChild : itemChildren1 )
						{
							ObservableList<TreeItem<SchemaEntity>> itemGrandChildren = itemChild.getChildren();
							itemChild.getChildren().removeAll( itemGrandChildren );
						}
						this.dbView.Refresh();
						break;
					default:
						// remove children on TreeView
						ObservableList<TreeItem<SchemaEntity>>  itemChildren2 = itemSelected.getChildren();
						itemSelected.getChildren().removeAll( itemChildren2 );
						// remove children of selected SchemaEntity
						selectedEntity.delEntityAll();
						// refresh
						this.dbView.Refresh();
						break;
				}
			} 
		);
		
		this.menuItemCopyObjectName.setOnAction
		( 
			(event)->
			{ 
				TreeItem<SchemaEntity>  itemSelected = this.schemaTreeView.getSelectionModel().getSelectedItem();
				SchemaEntity selectedEntity = itemSelected.getValue();
				// Copy to clipboard
				final ClipboardContent content = new ClipboardContent();
				content.putString( selectedEntity.getName() );
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				clipboard.setContent( content );				
			}
		);
		
		this.menuItemGenerateSelect.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.SELECT );	} );
		this.menuItemInsertPlaceHolderName.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.INSERT_BY_NAME );	} );
		this.menuItemInsertPlaceHolderSimple.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.INSERT_BY_SIMPLE );	} );
		this.menuItemUpdatePlaceHolderName.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.UPDATE_BY_NAME );	} );
		this.menuItemUpdatePlaceHolderSimple.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.UPDATE_BY_SIMPLE );	} );
		this.menuItemGenerateDelete.setOnAction( (event)->{	this.GenerateSQL( GenerateSQLFactory.TYPE.DELETE );	} );
		
		this.getItems().addAll
		( 
			this.menuItemRefresh,
			new SeparatorMenuItem(),
			this.menuItemCopyObjectName,
			new SeparatorMenuItem(),
			this.menuItemGenerateSelect,
			this.subMenuGenerateInsert,
			this.subMenuGenerateUpdate,
			this.menuItemGenerateDelete
		);
		
		this.subMenuGenerateInsert.getItems().addAll
		(
			this.menuItemInsertPlaceHolderName,
			this.menuItemInsertPlaceHolderSimple
		);
		
		this.subMenuGenerateUpdate.getItems().addAll
		(
			this.menuItemUpdatePlaceHolderName,
			this.menuItemUpdatePlaceHolderSimple
		);
	}
	
	private void GenerateSQL( GenerateSQLFactory.TYPE type )
	{
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(type);
		TreeItem<SchemaEntity> selectedItem = this.schemaTreeView.getSelectionModel().getSelectedItem(); 
		String strSQL = gsAbs.generate(selectedItem.getValue(),myDBAbs);
		// SQL to clipboard
		final ClipboardContent content = new ClipboardContent();
		content.putString( strSQL );
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent( content );
	}
	
	void setMenuStatus( TreeItem<SchemaEntity> selectedItem )
	{
		SchemaEntity schemaEntity = selectedItem.getValue();
		SchemaEntity.SCHEMA_TYPE schemaType = schemaEntity.getType();
		switch ( schemaType )
		{
			case ROOT:
			case ROOT_TABLE:
			case ROOT_INDEX:
			case INDEX:
			case ROOT_VIEW:
			case ROOT_SYSTEM_VIEW:
			case ROOT_MATERIALIZED_VIEW:
			case ROOT_FUNC:
			case FUNC:
			case ROOT_AGGREGATE:
			case AGGREGATE:
			case ROOT_PROC:
			case PROC:
			case ROOT_PACKAGE_DEF:
			case PACKAGE_DEF:
			case ROOT_PACKAGE_BODY:
			case PACKAGE_BODY:
			case ROOT_TRIGGER:
			case TRIGGER:
			case ROOT_TYPE:
			case TYPE:
			case ROOT_SEQUENCE:
			case ROOT_ER:
				this.menuItemRefresh.setDisable(false);
				this.menuItemGenerateSelect.setDisable(true);
				this.subMenuGenerateInsert.setDisable(true);
				this.subMenuGenerateUpdate.setDisable(true);
				this.menuItemGenerateDelete.setDisable(true);
				break;
			case SCHEMA:
			case INDEX_COLUMN:
			case SEQUENCE:
			case FOREIGN_KEY:
				this.menuItemRefresh.setDisable(true);
				this.menuItemGenerateSelect.setDisable(true);
				this.subMenuGenerateInsert.setDisable(true);
				this.subMenuGenerateUpdate.setDisable(true);
				this.menuItemGenerateDelete.setDisable(true);
				break;
			case TABLE:
				this.menuItemRefresh.setDisable(false);
				this.menuItemGenerateSelect.setDisable(false);
				this.subMenuGenerateInsert.setDisable(false);
				this.subMenuGenerateUpdate.setDisable(false);
				this.menuItemGenerateDelete.setDisable(false);
				break;
			case VIEW:
			case SYSTEM_VIEW:
			case MATERIALIZED_VIEW:
				this.menuItemRefresh.setDisable(false);
				this.menuItemGenerateSelect.setDisable(false);
				this.subMenuGenerateInsert.setDisable(true);
				this.subMenuGenerateUpdate.setDisable(true);
				this.menuItemGenerateDelete.setDisable(true);
				break;
			default:
				break;
		}		
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.schema.SchemaTreeView");
		
		this.menuItemRefresh.setText( langRB.getString("MENU_REFRESH") );
		
		this.menuItemCopyObjectName.setText( langRB.getString("MENU_COPY_OBJECT_NAME") );
		
		this.menuItemGenerateSelect.setText( langRB.getString("MENU_GENERATE_SELECT") );
		
		this.subMenuGenerateInsert.setText( langRB.getString("MENU_GENERATE_INSERT") );
		
		this.menuItemInsertPlaceHolderName.setText( langRB.getString("MENU_INSERT_PLACEHOLDER_NAME") );
		
		this.menuItemInsertPlaceHolderSimple.setText( langRB.getString("MENU_INSERT_PLACEHOLDER_SIMPLE") );
		
		this.subMenuGenerateUpdate.setText( langRB.getString("MENU_GENERATE_UPDATE") );
		
		this.menuItemUpdatePlaceHolderName.setText( langRB.getString("MENU_UPDATE_PLACEHOLDER_NAME") );
		
		this.menuItemUpdatePlaceHolderSimple.setText( langRB.getString("MENU_UPDATE_PLACEHOLDER_SIMPLE") );
		
		this.menuItemGenerateDelete.setText( langRB.getString("MENU_GENERATE_DELETE") );
		
	}
}
