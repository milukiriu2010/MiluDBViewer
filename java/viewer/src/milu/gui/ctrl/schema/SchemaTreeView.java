package milu.gui.ctrl.schema;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.collections.ObservableList;
import javafx.beans.property.BooleanProperty;
import milu.gui.view.DBView;

import milu.ctrl.ChangeLangInterface;
import milu.entity.schema.SchemaEntity;

public class SchemaTreeView extends TreeView<SchemaEntity>
	implements
		ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.ctrl.schema.SchemaTreeView";
	
	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// Control View
	private DBView  dbView = null;
	
	// Root Item of this Tree
	private TreeItem<SchemaEntity> item0Root = null;
	/*
	private enum NAME_TYPE
	{
		NAME_OBJECT,
		NAME_BUNDLE
	}
	
	private enum STATUS
	{
		VALID,
		INVALID
	}
	*/
	
	public SchemaTreeView( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// When the selected item is changed & the item doesn't have children,
		// call "Exec Query" to get children items.
		this.getSelectionModel().selectedItemProperty().addListener
		(
			// ChangedListner
			// @Override
			// public void changed( ObservableValue obs, Object oldVal, Object newVal )
			( obs, oldVal, newVal )->
			{
				//TreeItem<SchemaEntity> itemChanged = (TreeItem<SchemaEntity>)newVal;
				//System.out.println( "SchemaTreeView itemChanged:" + itemChanged.getValue() );
				dbView.Go();
			}
		);
		
		this.setContextMenu();
	}
	
	private void setContextMenu()
	{
		ContextMenu  contextMenu = new ContextMenu();
		
		MenuItem  menuItemRefresh = new MenuItem( langRB.getString("MENU_REFRESH") );
		menuItemRefresh.setOnAction
		( 
			(event)->
			{ 
				TreeItem<SchemaEntity>  itemSelected = this.getSelectionModel().getSelectedItem();
				SchemaEntity scEnt = itemSelected.getValue();
				SchemaEntity.SCHEMA_TYPE schemaType = scEnt.getType();
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
						// remove children & refresh
						ObservableList<TreeItem<SchemaEntity>>  itemChildren2 = itemSelected.getChildren();
						itemSelected.getChildren().removeAll( itemChildren2 );
						this.dbView.Refresh();
						break;
				}
			} 
		);
		
		contextMenu.getItems().addAll( menuItemRefresh );
		this.setOnContextMenuRequested
		( 
			(event)->
			{ 
				TreeItem<SchemaEntity>  itemSelected = this.getSelectionModel().getSelectedItem();
				SchemaEntity scEnt = itemSelected.getValue();
				SchemaEntity.SCHEMA_TYPE schemaType = scEnt.getType();
				switch ( schemaType )
				{
					// do not show ContextMenu when these items are selected.
					case ROOT:
					case SCHEMA:
					//case INDEX:
					case INDEX_COLUMN:
					case ROOT_SYSTEM_VIEW:
					case SYSTEM_VIEW:
						break;
					// show ContextMenu when other items are selected.
					default:
						contextMenu.show( this, event.getScreenX(), event.getScreenY() ); 
						break;
				}
			} 
		);
	}
	
	// https://stackoverflow.com/questions/38101041/javafx-treeview-restore-scroll-state
	private void setAction()
	{
		/*
		Set<Node> nodes = this.lookupAll(".scroll-bar");
		for (Node node : nodes) {
			if ( node instanceof ScrollBar )
			{
				System.out.println( "TreeView:ScrollBar" );
			    ScrollBar scrollBar = (ScrollBar)node;
			    if (scrollBar.getOrientation() == Orientation.VERTICAL) {
			        scrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
			        	System.out.println( "TreeView:ScrollBar:obs[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
			        	
			            //double value = newValue.doubleValue();
			            //if (value > 0) {
			            //    scrollState.setMin(scrollBar.getMin());
			            //    scrollState.setMax(scrollBar.getMax());
			            //    scrollState.setUnitIncrement(scrollBar.getUnitIncrement());
			            //    scrollState.setBlockIncrement(scrollBar.getBlockIncrement());
			            //    scrollState.setVerticalValue(value);
			            //}
			            
			        });
			    }
			}
		}
		*/
	}
	/*
	private TreeItem<SchemaEntity> addItem
		( TreeItem<SchemaEntity>   itemParent, 
		  String                   itemName,
		  NAME_TYPE                skipBundle,
		  SchemaEntity.SCHEMA_TYPE itemType,
		  STATUS                   status, 
		  String                   fileResourceName )
	{
		SchemaEntity seEnt = null;
		if ( skipBundle == NAME_TYPE.NAME_OBJECT )
		{
			seEnt = SchemaEntityFactory.createInstance( itemName, itemType );
		}
		else if ( skipBundle == NAME_TYPE.NAME_BUNDLE )
		{
			seEnt = SchemaEntityFactory.createInstance( langRB.getString(itemName), itemType );
		}

		String imageResourceName = seEnt.getImageResourceName();
		if ( imageResourceName == null )
		{
			imageResourceName = fileResourceName;
		}
		ImageView iv = new ImageView( new Image( imageResourceName ) );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		
		Group imageGroup = new Group();
		if ( status == STATUS.VALID )
		{
			imageGroup.getChildren().add( iv );
		}
		else if ( status == STATUS.INVALID )
		{
			Line lineLTRB = new Line( 0, 0, iv.getFitWidth(), iv.getFitHeight() );
			lineLTRB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			Line lineRTLB = new Line( iv.getFitWidth(), 0, 0, iv.getFitHeight() );
			lineRTLB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			imageGroup.getChildren().addAll( iv, lineLTRB, lineRTLB );
			imageGroup.setEffect( new Blend(BlendMode.OVERLAY) );
		}
		
		TreeItem<SchemaEntity> itemNew = new TreeItem<SchemaEntity>( seEnt, imageGroup );
		if ( itemParent != null )
		{
			itemParent.getChildren().add( itemNew );
		}
		
		// https://stackoverflow.com/questions/14236666/how-to-get-current-treeitem-reference-which-is-expanding-by-user-click-in-javafx
		itemNew.expandedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
		        System.out.println("newVal = " + newVal);
		        BooleanProperty bb = (BooleanProperty) obs;
		        System.out.println("bb.getBean() = " + bb.getBean());
		        TreeItem<SchemaEntity> itemTarget = (TreeItem<SchemaEntity>) bb.getBean();
		        scrollBack( itemTarget );
		    }
		);
		
		return itemNew;
	}
	*/
	
	@SuppressWarnings("unchecked")
	private TreeItem<SchemaEntity> addItem
		( TreeItem<SchemaEntity>   itemParent, 
		  SchemaEntity             schemaEntity )
	{
		String imageResourceName = schemaEntity.getImageResourceName();
		ImageView iv = new ImageView( new Image( imageResourceName ) );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		
		SchemaEntity.STATE state = schemaEntity.getState();
		
		Group imageGroup = new Group();
		if ( state == SchemaEntity.STATE.VALID )
		{
			imageGroup.getChildren().add( iv );
		}
		else if ( state == SchemaEntity.STATE.INVALID )
		{
			Line lineLTRB = new Line( 0, 0, iv.getFitWidth(), iv.getFitHeight() );
			lineLTRB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			Line lineRTLB = new Line( iv.getFitWidth(), 0, 0, iv.getFitHeight() );
			lineRTLB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			imageGroup.getChildren().addAll( iv, lineLTRB, lineRTLB );
			imageGroup.setEffect( new Blend(BlendMode.OVERLAY) );
		}
		
		TreeItem<SchemaEntity> itemNew = new TreeItem<SchemaEntity>( schemaEntity, imageGroup );
		if ( itemParent != null )
		{
			itemParent.getChildren().add( itemNew );
		}
		
		// https://stackoverflow.com/questions/14236666/how-to-get-current-treeitem-reference-which-is-expanding-by-user-click-in-javafx
		itemNew.expandedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
		        System.out.println("newVal = " + newVal);
		        BooleanProperty bb = (BooleanProperty) obs;
		        Object obj = bb.getBean();
		        System.out.println("bb.getBean() = " + obj);
		        if ( obj instanceof TreeItem )
		        {
		        	TreeItem<SchemaEntity> itemTarget = (TreeItem<SchemaEntity>)obj;
		        	scrollBack( itemTarget );
		        }
		    }
		);
		
		return itemNew;
	}
	
	// When expanding adn unexpanding with too many treeitems, weird scroll occurs.
	private void scrollBack( TreeItem<SchemaEntity> itemTarget )
	{
		if ( itemTarget.getChildren().size() >= 10 )
		{
			int rowId = this.getRow( itemTarget );
			this.scrollTo( rowId );
		}
	}
	
	public void setInitialData( SchemaEntity rootEntity )
	{
		// create Root Item
		this.item0Root = this.addItem( null, rootEntity );
		this.item0Root.setExpanded( true );
		this.setRoot( this.item0Root );
		
		if ( rootEntity == null )
		{
			return;
		}
		
		// ----------------------------------------
		// -[ROOT]
		//   -[SCHEMA] => set
		// ----------------------------------------
		for ( SchemaEntity schemaEntity: rootEntity.getEntityLst() )
		{
			TreeItem<SchemaEntity> item1Schema =
				this.addItem( this.item0Root, schemaEntity );
			
			//System.out.println( "schemaEntity.getName():" + schemaEntity.getName() );
			//System.out.println( "schemaEntity.getEntityLst().size():" + schemaEntity.getEntityLst().size() );
			
			// ----------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE] => set
			//     -[ROOT_VIEW]  => set
			// ----------------------------------------
			for ( SchemaEntity rootObjEntity: schemaEntity.getEntityLst() )
			{
				this.addItem( item1Schema, rootObjEntity );
			}
		}
	}
	
	public void setTableData( TreeItem<SchemaEntity> itemTarget, List<SchemaEntity> tableEntityLst )
	{
		for ( SchemaEntity tableEntity : tableEntityLst )
		{
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]
			//       -[TABLE]    => add
			// ---------------------------------------
			TreeItem<SchemaEntity> item3TableName = 
				this.addItem( 
					itemTarget,
					tableEntity
				);
			
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]
			//       -[TABLE]
			//         -[INDEX_ROOT] => add
			// ---------------------------------------
			for ( SchemaEntity rootObjEntity : tableEntity.getEntityLst() )
			{
				this.addItem( item3TableName, rootObjEntity );
			}
		}
		
		this.setAction();
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	/*
	public void setIndexData( TreeItem<SchemaEntity> itemTarget, List<Map<String,String>> dataLst )
	{
		for ( Map<String,String> dataRow : dataLst )
		{
			// create Index Item
			String is_primary    = dataRow.get("is_primary");
			String is_unique     = dataRow.get("is_unique");
			String is_functional = dataRow.get("is_functional");
			String iconFileName  = null;
			if ( "t".equals(is_primary) )
			{
				iconFileName = "file:resources/images/index_p.png";
			}
			else if ( "t".equals(is_unique) )
			{
				iconFileName = "file:resources/images/index_u.png";
			}
			else if ( "t".equals(is_functional) )
			{
				iconFileName = "file:resources/images/index_f.png";
			}
			else
			{
				iconFileName = "file:resources/images/index_i.png";
			}
			
			STATUS status = STATUS.VALID; 
			String strStatus = dataRow.get("status");
			if ( "INVALID".equals(strStatus) )
			{
				status = STATUS.INVALID;
			}

			TreeItem<SchemaEntity> item5Index = 
					this.addItem( 
						itemTarget, 
						dataRow.get("index_name"), 
						NAME_TYPE.NAME_OBJECT, 
						SchemaEntity.SCHEMA_TYPE.INDEX,
						status,
						iconFileName 
					);
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	*/
	public void addEntityLst( TreeItem<SchemaEntity> itemTarget, List<SchemaEntity> schemaEntityLst )
	{
		for ( SchemaEntity schemaEntity : schemaEntityLst )
		{
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_VIEW]
			//       -[VIEW]    => add
			//     -[ROOT_PACKAGE_DEF]
			//       -[PACKAGE_DEF]    => add
			// ---------------------------------------
			this.addItem( itemTarget, schemaEntity ); 
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	/*
	public void setAggregateData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
	{
		
		for ( List<String> dataRow : dataLst )
		{
			int cnt = dataRow.size();
			STATUS status = STATUS.VALID; 
			if ( cnt >= 3 )
			{
				String strStatus = dataRow.get(2);
				if ( "INVALID".equals(strStatus) )
				{
					status = STATUS.INVALID;
				}
			}
			// create Aggregate Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.AGGREGATE,
				status,
				"file:resources/images/aggregate.png" 
			);
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	*/
	/**
	 * Load Language Resource
	 */
	private void loadLangResource()
	{
		this.langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.loadLangResource();
		
		this.setContextMenu();
	}
	
}
