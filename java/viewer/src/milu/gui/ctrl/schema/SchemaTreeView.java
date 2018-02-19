package milu.gui.ctrl.schema;

import java.util.Map;
import java.util.List;
//import java.util.Set;
import java.util.ResourceBundle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
//import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.Group;
//import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.collections.ObservableList;
//import javafx.geometry.Orientation;
import javafx.beans.property.BooleanProperty;
import milu.gui.view.DBView;

import milu.ctrl.ChangeLangInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

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
	
	private TreeItem<SchemaEntity> addItem
		( TreeItem<SchemaEntity>   itemParent, 
		  String                   itemName,
		  NAME_TYPE                skipBundle,
		  SchemaEntity.SCHEMA_TYPE itemType,
		  String                   fileResourceName ) 
	{
		return this.addItem(itemParent, itemName, skipBundle, itemType, STATUS.VALID, fileResourceName );
	}
	
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

	/*
	public void setInitialData( String strRoot, List<Map<String,String>> schemaNameLst, List<SchemaEntity.SCHEMA_TYPE>  suppoertedTypeLst )
	{
		// create Root Item
		this.item0Root = 
			this.addItem( 
				null, 
				strRoot, 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.ROOT, 
				"file:resources/images/url.png" 
			);
		this.item0Root.setExpanded( true );
		this.setRoot( this.item0Root );
		
		// create Schema Item
		for ( Map<String,String> schemaNameItem : schemaNameLst )
		{
			// create Schema Root
			TreeItem<SchemaEntity> item1Schema = 
				this.addItem( 
					this.item0Root, 
					schemaNameItem.get("schemaName"), 
					NAME_TYPE.NAME_OBJECT, 
					SchemaEntity.SCHEMA_TYPE.SCHEMA,
					"file:resources/images/schema.png" 
				); 
				
			// create Table Root
			this.addItem( 
				item1Schema, 
				"ITEM_TABLE", 
				NAME_TYPE.NAME_BUNDLE, 
				SchemaEntity.SCHEMA_TYPE.ROOT_TABLE, 
				"file:resources/images/table_root.png" 
			);
				
			// create View Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_VIEW", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_VIEW, 
					"file:resources/images/view_root.png" 
				);
			}
			
			// create System View Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_SYSTEM_VIEW", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW, 
					"file:resources/images/systemview_root.png" 
				);
			}
			
			// create Materialized View Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_MATERIALIZED_VIEW", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW, 
					"file:resources/images/materialized_view_root.png" 
				);
			}
			
			// create Function Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_FUNC", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_FUNC, 
					"file:resources/images/func_root.png" 
				);
			}
			
			// create Aggregate Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_AGGREGATE", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE, 
					"file:resources/images/aggregate_root.png" 
				);
			}
			
			// create Procedure Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_PROC ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_PROC", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_PROC, 
					"file:resources/images/proc_root.png" 
				);
			}
			
			// create Package Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_PACKAGE_DEF", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF, 
					"file:resources/images/package_def_root.png" 
				);
			}
			
			// create Package Body Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_PACKAGE_BODY", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY, 
					"file:resources/images/package_body_root.png" 
				);
			}
			
			// create Trigger Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_TRIGGER", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER, 
					"file:resources/images/trigger_root.png" 
				);
			}
			
			// create Type Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_TYPE", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_TYPE, 
					"file:resources/images/type_root.png" 
				);
			}
			
			// create Sequence Root
			if ( suppoertedTypeLst.contains( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE ) )
			{
				this.addItem( 
					item1Schema, 
					"ITEM_SEQ", 
					NAME_TYPE.NAME_BUNDLE, 
					SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE,
					"file:resources/images/seq_root.png"
				);
			}
		}
		
		this.setAction();
	}
	*/

	/*
	public void setTableData( TreeItem<SchemaEntity> itemTarget, List<Map<String,String>> dataLst )
	{
		for ( Map<String,String> dataRow : dataLst )
		{
			STATUS status = STATUS.VALID; 
			String strStatus = dataRow.get("status");
			if ( "INVALID".equals(strStatus) )
			{
				status = STATUS.INVALID;
			}
			
	        // create Table Item
			TreeItem<SchemaEntity> item3TableName = 
				this.addItem( 
					itemTarget, 
					dataRow.get("tableName"), 
					NAME_TYPE.NAME_OBJECT, 
					SchemaEntity.SCHEMA_TYPE.TABLE,
					status,
					"file:resources/images/table.png" 
				);
			
			// create Index Root
			this.addItem( 
				item3TableName, 
				"ITEM_INDEX", 
				NAME_TYPE.NAME_BUNDLE, 
				SchemaEntity.SCHEMA_TYPE.ROOT_INDEX, 
				"file:resources/images/index_root.png" 
			);
			
		}
		
		this.setAction();
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	*/
	
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
			
			/*
			// create Index Root
			this.addItem( 
				item3TableName, 
				"ITEM_INDEX", 
				NAME_TYPE.NAME_BUNDLE, 
				SchemaEntity.SCHEMA_TYPE.ROOT_INDEX, 
				"file:resources/images/index_root.png" 
			);
			*/
		}
		
		this.setAction();
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
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
			
			/*
			// split column list into each column
			// ex1. {column1}
			//        => column1
			// ex2. {column1,column2}
			//        => column1 column2
			String index_keys = dataRow.get("index_keys");
			if ( index_keys != null )
			{
				index_keys = index_keys.replace( "{",  "" );
				index_keys = index_keys.replace( "}",  "" );
				String[] indexLst = index_keys.split(",");
				// create Column Item
				for ( String ind : indexLst )
				{
					this.addItem( 
						item5Index, 
						"file:resources/images/column.png", 
						ind, 
						NAME_TYPE.NAME_OBJECT, 
						SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN 
					);
				}
			}
			*/
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
	public void setIndexColumnData( TreeItem<SchemaEntity> itemTarget, List<Map<String,String>> dataLst )
	{
		for ( Map<String,String> dataRow : dataLst )
		{
			// create IndexColumn Item
			String columnName      = dataRow.get("columnName");
			String clusteringOrder = dataRow.get("clusteringOrder");
			String iconFileName  = null;
			if ( "asc".equals( clusteringOrder ) )
			{
				iconFileName = "file:resources/images/order_a.png";
			}
			else if ( "desc".equals( clusteringOrder ) )
			{
				iconFileName = "file:resources/images/order_d.png";
			}
			else
			{
				iconFileName = "file:resources/images/column.png";
			}
			
			this.addItem( 
				itemTarget, 
				columnName, 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN,
				iconFileName
			);
		}
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}

	/*
	public void setViewData( TreeItem<SchemaEntity> itemTarget, List<Map<String,String>> dataLst )
	{
		
		for ( Map<String,String> dataRow : dataLst )
		{
			STATUS status = STATUS.VALID; 
			String strStatus = dataRow.get("status");
			if ( "INVALID".equals(strStatus) )
			{
				status = STATUS.INVALID;
			}
	        // create View Item
			this.addItem( 
				itemTarget, 
				dataRow.get("name"), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.VIEW,
				status,
				"file:resources/images/view.png" 
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
	
	public void setSystemViewData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
	{
		
		for ( List<String> dataRow : dataLst )
		{
	        // create System View Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW, 
				"file:resources/images/systemview.png" 
			);
			
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
	public void setMaterializedViewData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
			// create Materialized View Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW,
				status,
				"file:resources/images/materialized_view.png" 
			);
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
	public void setFuncData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
			// create Function Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.FUNC,
				status,
				"file:resources/images/func.png" 
			);
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
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
	
	public void setProcData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
			// create Procedure Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.PROC,
				status,
				"file:resources/images/proc.png" 
			);
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	/*
	public void setPackageDefData( TreeItem<SchemaEntity> itemTarget, List<SchemaEntity> schemaEntityLst )
	{
		for ( SchemaEntity schemaEntity : schemaEntityLst )
		{
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_PACKAGE_DEF]
			//       -[PACKAGE_DEF]    => add
			// ---------------------------------------
			this.addItem( itemTarget, schemaEntity ); 
		}
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	*/
	
	public void setPackageBodyData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
	        // create Package Body Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY,
				status,
				"file:resources/images/package_body.png" 
			);
			
		}
		itemTarget.setExpanded(true);
		
		this.scrollBack(itemTarget);
	}
	
	public void setTypeData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
	        // create Type Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.TYPE,
				status,
				"file:resources/images/type.png" 
			);
			
		}
		itemTarget.setExpanded(true);
		
		this.scrollBack(itemTarget);
	}
	
	public void setTriggerData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
	        // create Trigger Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.TRIGGER,
				status,
				"file:resources/images/trigger.png" 
			);
			
		}
		itemTarget.setExpanded(true);
		
		this.scrollBack(itemTarget);
	}
	
	public void setSequenceData( TreeItem<SchemaEntity> itemTarget, List<List<String>> dataLst )
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
	        // create Sequence Item
			this.addItem( 
				itemTarget, 
				dataRow.get(1), 
				NAME_TYPE.NAME_OBJECT, 
				SchemaEntity.SCHEMA_TYPE.SEQUENCE,
				status,
				"file:resources/images/seq.png" 
			);
			
		}
		itemTarget.setExpanded(true);
		
		this.scrollBack(itemTarget);
	}
	
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
		//this.skimThrough( this.item0Root );
	}
	
	private void skimThrough( TreeItem<SchemaEntity> itemParent )
	{
		if ( itemParent == null )
		{
			return;
		}
		
		for ( TreeItem<SchemaEntity> itemChild : itemParent.getChildren() )
		{
			SchemaEntity scEnt = itemChild.getValue();
			//System.out.println( "skimThrough:"+ scEnt.getName() );
			SchemaEntity.SCHEMA_TYPE schemaType = scEnt.getType();
			if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TABLE )
			{
				//System.out.println( "skimThrough:ROOT_TABLE" );
				scEnt.setName( langRB.getString("ITEM_TABLE"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_VIEW )
			{
				//System.out.println( "skimThrough:ROOT_VIEW" );
				scEnt.setName( langRB.getString("ITEM_VIEW"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW )
			{
				//System.out.println( "skimThrough:ROOT_MATERIALIZED_VIEW" );
				scEnt.setName( langRB.getString("ITEM_MATERIALIZED_VIEW"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_INDEX )
			{
				//System.out.println( "skimThrough:ROOT_INDEX" );
				scEnt.setName( langRB.getString("ITEM_INDEX"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_FUNC )
			{
				scEnt.setName( langRB.getString("ITEM_FUNC"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE )
			{
				scEnt.setName( langRB.getString("ITEM_AGGREGATE"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PROC )
			{
				scEnt.setName( langRB.getString("ITEM_PROC"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF )
			{
				scEnt.setName( langRB.getString("ITEM_PACKAGE_DEF"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY )
			{
				scEnt.setName( langRB.getString("ITEM_PACKAGE_BODY"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TYPE )
			{
				scEnt.setName( langRB.getString("ITEM_TYPE"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER )
			{
				scEnt.setName( langRB.getString("ITEM_TRIGGER"));
			}
			else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE )
			{
				scEnt.setName( langRB.getString("ITEM_SEQ"));
			}
			itemChild.setValue( scEnt );

			
			this.skimThrough( itemChild );
		}
	}
}
