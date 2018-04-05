package milu.gui.ctrl.schema;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.collections.ObservableList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.scene.Node;
import javafx.scene.control.skin.VirtualFlow;

import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.tool.MyTool;
import milu.ctrl.MainController;
import milu.entity.schema.SchemaEntity;

public class SchemaTreeView extends TreeView<SchemaEntity>
	implements
		ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.gui.ctrl.schema.SchemaTreeView";
	
	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// Control View
	private DBView  dbView = null;
	
	// Root Item of this Tree
	private TreeItem<SchemaEntity> item0Root = null;
	
	// Parent pane of this class
	private AnchorPane    parentPane = null;
	
	// children count of selected item
	private Label lblChildrenCnt = new Label("*");
	
	public SchemaTreeView( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.lblChildrenCnt.getStyleClass().add("SchemaTreeView_LabelChildrenCount");
		
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
				if ( newVal != null )
				{
					// scroll, when "<=(arrow)" key is clicked. 
					this.scrollToSelectedItem(newVal);
					// set "children count" of selected item
					this.lblChildrenCnt.textProperty().bind
					(
						Bindings.convert
						(
							new SimpleIntegerProperty( newVal.getChildren().size() )
						)
					);
				}
				// shift label position
				// -------------------------------------------------------
				// vertical scrollbar invisible => margin 0
				// vertical scrollbar visible   => margin scrollbar width
				this.shiftLabelChildrenCountPosition();
				
			}
		);
		
		this.setAction();
		
		this.setContextMenu();
	}
	
	/**
	 *  Call after this class added on AnchorPane
	 */
	public void init()
	{
		this.parentPane = MyTool.findAnchorPane( this );
		if ( this.parentPane != null )
		{
			this.parentPane.getChildren().add( this.lblChildrenCnt );
			AnchorPane.setTopAnchor( this.lblChildrenCnt, 0.0 );
			//AnchorPane.setBottomAnchor( this.lblChildrenCnt, 0.0 );
			//AnchorPane.setLeftAnchor( this.lblChildrenCnt, 0.0 );
			AnchorPane.setRightAnchor( this.lblChildrenCnt, 0.0 );
			
		}
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
						// remove children on TreeView
						ObservableList<TreeItem<SchemaEntity>>  itemChildren2 = itemSelected.getChildren();
						itemSelected.getChildren().removeAll( itemChildren2 );
						// remove children of selected SchemaEntity
						scEnt.delEntityAll();
						// refresh
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
					case ROOT_TABLE:
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
		//this.setContextMenu(contextMenu);
	}
	
	private void setAction()
	{
		// https://stackoverflow.com/questions/38101041/javafx-treeview-restore-scroll-state
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
		
		
		// search item by "pressed key"
		// -------------------------------------------------------
		// search sibling , when selected item is not expanded.
		// search children, when selected item is expanded.
		this.addEventHandler
		( 
			KeyEvent.KEY_PRESSED, 
			(event)->
			{
				TreeItem<SchemaEntity> selectedItem = this.getSelectionModel().getSelectedItem();
				if ( selectedItem == null )
				{
					return;
				}
				//System.out.println( "SelectedItem[" + this.getRow(selectedItem) + "]" );
				TreeItem<SchemaEntity> parentItem = null;
				if ( selectedItem.isExpanded() )
				{
					parentItem = selectedItem;
				}
				else
				{
					parentItem = selectedItem.getParent();
				}
				if ( parentItem == null )
				{
					return;
				}
				ObservableList<TreeItem<SchemaEntity>> childrenItem = parentItem.getChildren();
				String strKey = event.getCode().getChar();
				
				// search selectedItem to lastItem
				TreeItem<SchemaEntity> nextItem = selectedItem.nextSibling();
				while ( nextItem != null )
				{
					String val = nextItem.getValue().getName();
					if ( val.toUpperCase().startsWith(strKey) )
					{
						this.getSelectionModel().select(nextItem);
						/*
						int nextItemId = this.getRow(nextItem);
						if ( ( nextItemId < visibleIdFirst) || ( visibleIdLast < nextItemId ) )
						{
							this.scrollTo(nextItemId);
						}
						*/
						this.scrollToSelectedItem(nextItem);
						return;
					}
					nextItem = nextItem.nextSibling();
				}
				
				// search firstItem to selectedItem
				nextItem = childrenItem.get(0);
				while ( ( nextItem != null ) && ( nextItem != selectedItem ) )
				{
					String val = nextItem.getValue().getName();
					if ( val.toUpperCase().startsWith(strKey) )
					{
						this.getSelectionModel().select(nextItem);
						/*
						int nextItemId = this.getRow(nextItem);
						if ( ( nextItemId < visibleIdFirst) || ( visibleIdLast < nextItemId ) )
						{
							this.scrollTo(nextItemId);
						}
						*/
						this.scrollToSelectedItem(nextItem);
						return;
					}
					nextItem = nextItem.nextSibling();
				}
			}
		);
	}
	
	// shift label position
	// -------------------------------------------------------
	// vertical scrollbar invisible => margin 0
	// vertical scrollbar visible   => margin scrollbar width
	private void shiftLabelChildrenCountPosition()
	{
		ScrollBar scrollBarVertical = MyTool.getScrollBarVertical(this);
		if ( scrollBarVertical != null )
		{
			System.out.println( "SchemaTreeView:ScrollBar Found." );
			AnchorPane.setRightAnchor( this.lblChildrenCnt, scrollBarVertical.getWidth() );
			// "visibleProperty" doesn't work on some PC.
			scrollBarVertical.visibleProperty().addListener
			(
				(obs2,oldVal2,newVal2)->
				{
					if ( newVal2 == true )
					{
						System.out.println( "SchemaTreeView:ScrollBar Found - visible." );
						AnchorPane.setRightAnchor( this.lblChildrenCnt, scrollBarVertical.getWidth() );
					}
					else
					{
						System.out.println( "SchemaTreeView:ScrollBar Found - invisible." );
						AnchorPane.setRightAnchor( this.lblChildrenCnt, 0.0 );
					}
				}
			);
		}
		else
		{
			System.out.println( "SchemaTreeView:ScrollBar not Found." );
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private TreeItem<SchemaEntity> addItem
		( TreeItem<SchemaEntity>   itemParent, 
		  SchemaEntity             schemaEntity )
	{
		/*
		MainController mainCtrl = dbView.getMainController();
		
		String imageResourceName = schemaEntity.getImageResourceName();
		//System.out.println( "image:" + imageResourceName );
		ImageView iv = new ImageView( mainCtrl.getImage(imageResourceName) );
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
		*/
		MainController mainCtrl = dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, schemaEntity );
		
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
		        	// itemTarget is set to the top of TreeView
		        	scrollBack( itemTarget );
		        }
				// shift label position
				// -------------------------------------------------------
				// vertical scrollbar invisible => margin 0
				// vertical scrollbar visible   => margin scrollbar width
				this.shiftLabelChildrenCountPosition();
		    }
		);
		
		return itemNew;
	}
	
	// When expanding and unexpanding with too many treeitems, weird scroll occurs.
	private void scrollBack( TreeItem<SchemaEntity> itemTarget )
	{
		if ( itemTarget.getChildren().size() >= 10 )
		{
			int rowId = this.getRow( itemTarget );
			this.scrollTo( rowId );
		}
	}
	
	private void scrollToSelectedItem( TreeItem<SchemaEntity> itemTarget )
	{
		// https://stackoverflow.com/questions/10113045/javafx-2-0-get-treeitems-or-nodes-currently-visible-on-screen
		// VirtualFlow => Java 9
		Node node = MyTool.searchChildNode( this, VirtualFlow.class );
		VirtualFlow<?> nodeVF = null;
		int visibleIdFirst = -1;
		int visibleIdLast  = -1;
		if ( node instanceof VirtualFlow )
		{
			nodeVF = (VirtualFlow<?>)node;
			if ( nodeVF.getFirstVisibleCell() != null )
			{
				visibleIdFirst = nodeVF.getFirstVisibleCell().getIndex();
			}
			if ( nodeVF.getLastVisibleCell() != null )
			{
				visibleIdLast  = nodeVF.getLastVisibleCell().getIndex();
			}
		}
		int itemTargetId = this.getRow(itemTarget);
		System.out.println( "VisibleItem[" + visibleIdFirst + "," + visibleIdLast + "," + itemTargetId + "]" );
		// scroll to the item, if invisible
		if ( ( itemTargetId <= visibleIdFirst) || ( visibleIdLast <= itemTargetId ) )
		{
			this.scrollTo(itemTargetId);
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
		
		itemTarget.setExpanded(true);
		this.scrollBack(itemTarget);
	}
	
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
