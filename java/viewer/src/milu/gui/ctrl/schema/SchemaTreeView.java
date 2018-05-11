package milu.gui.ctrl.schema;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.skin.VirtualFlow;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;
import milu.entity.schema.SchemaEntity;

public class SchemaTreeView extends TreeView<SchemaEntity>
	implements
		ChangeLangInterface
{
	// Control View
	private DBView  dbView = null;
	
	// Parent pane of this class
	private AnchorPane    parentPane = null;
	
	// children count of selected item
	private Label lblChildrenCnt = new Label("*");
	
	private boolean isLoading = false;
	
	private MenuItem  menuItemRefresh = new MenuItem();
	
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
				dbView.Go();
				if ( newVal != null )
				{
					// scroll, when "<=(arrow)" key is clicked. 
					this.scrollToSelectedItem(newVal);
					// set "children count" of selected item
					this.setChildrenCnt();
					/*
					this.lblChildrenCnt.textProperty().unbind();
					this.lblChildrenCnt.textProperty().bind
					(
						Bindings.convert
						(
							new SimpleIntegerProperty( newVal.getChildren().size() )
						)
					);
					*/
				}
				// shift label position
				// -------------------------------------------------------
				// vertical scrollbar invisible => margin 0
				// vertical scrollbar visible   => margin scrollbar width
				this.shiftLabelChildrenCountPosition();
				
				// Disable/Enable MenuItem(Refresh)
				// -------------------------------------------------------
				SchemaEntity scEnt = newVal.getValue();
				SchemaEntity.SCHEMA_TYPE schemaType = scEnt.getType();
				switch ( schemaType )
				{
					// Disable "Refresh" menu when these items are selected.
					//case ROOT:
					case SCHEMA:
					//case ROOT_TABLE:
					//case INDEX:
					case INDEX_COLUMN:
					//case ROOT_SYSTEM_VIEW:
					//case SYSTEM_VIEW:
						this.menuItemRefresh.setDisable(true);
						break;
					// Enable "Refresh" menu when these items are selected.
					default:
						this.menuItemRefresh.setDisable(false);
						break;
				}
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
	
	public void setChildrenCnt()
	{
		TreeItem<SchemaEntity> selectedItem = this.getSelectionModel().getSelectedItem();
		String cnt = null;
		if ( selectedItem == null )
		{
			cnt = "*";
		}
		else
		{
			cnt = Integer.toString( selectedItem.getChildren().size() );
		}
		this.lblChildrenCnt.setText(cnt);
	}
	
	private void setContextMenu()
	{
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.schema.SchemaTreeView");
		ContextMenu  contextMenu = new ContextMenu();
		
		//MenuItem  menuItemRefresh = new MenuItem( langRB.getString("MENU_REFRESH") );
		this.menuItemRefresh.setText( langRB.getString("MENU_REFRESH") );
		this.menuItemRefresh.setOnAction
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
		
		contextMenu.getItems().addAll( this.menuItemRefresh );
		/*
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
					//case ROOT:
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
		*/
		this.setContextMenu(contextMenu);
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
						this.scrollToSelectedItem(nextItem);
						return;
					}
					nextItem = nextItem.nextSibling();
				}
				
				// search firstItem to selectedItem
				if ( childrenItem.size() == 0 )
				{
					return;
				}
				nextItem = childrenItem.get(0);
				while ( ( nextItem != null ) && ( nextItem != selectedItem ) )
				{
					String val = nextItem.getValue().getName();
					if ( val.toUpperCase().startsWith(strKey) )
					{
						this.getSelectionModel().select(nextItem);
						this.scrollToSelectedItem(nextItem);
						return;
					}
					nextItem = nextItem.nextSibling();
				}
			}
		);
		
		this.setCellFactory
		(
			(treeView)->
			{
				TextFieldTreeCell<SchemaEntity> treeCell = new TextFieldTreeCell<>();
				treeCell.setConverter
				(
					new StringConverter<SchemaEntity>()
					{
						@Override
						public String toString(SchemaEntity schemaEntity)
						{
							if ( isLoading == false )
							{
								return schemaEntity.toString();
							}
							else
							{
								TreeItem<SchemaEntity> selectedEntity = treeCell.getTreeItem();
								if ( treeCell.getTreeView().getSelectionModel().getSelectedItem() == selectedEntity )
								{
									return schemaEntity.toString() + "...Loading...";
								}
								else
								{
									return schemaEntity.toString();
								}
							}
						}
						
						@Override
						public SchemaEntity fromString( String str )
						{
							return null;
						}
					}
				);
				
				
				return treeCell;
			}
		);
	}
	
	public void setIsLoading( boolean isLoading )
	{
		this.isLoading = isLoading;
		this.refresh();
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
			// office win => OK
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
		MainController mainCtrl = this.dbView.getMainController();
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
		        	//TreeItem<?> itemTarget = MyTool.cast(obj, TreeItem.class );
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
	
	public void scrollToSelectedItem( TreeItem<SchemaEntity> itemTarget )
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
	/*
	public void setInitialData( SchemaEntity rootEntity )
	{
		// create Root Item
		TreeItem<SchemaEntity> itemRoot = this.addItem( null, rootEntity );
		itemRoot.setExpanded(true);
		this.setRoot(itemRoot);
		
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
				this.addItem( itemRoot, schemaEntity );
			
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
	*/
	/*
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
	*/
	public void addEntityLst( TreeItem<SchemaEntity> itemTarget, List<SchemaEntity> schemaEntityLst, boolean isExpanded )
	{
		// create Root
		if ( itemTarget == null && schemaEntityLst.size() == 1 )
		{
			SchemaEntity rootEntity = schemaEntityLst.get(0);
			TreeItem<SchemaEntity> itemRoot = this.addItem( null, rootEntity );
			itemRoot.setExpanded(isExpanded);
			this.setRoot(itemRoot);
			if ( rootEntity.getEntityLst().size() > 0 )
			{
				this.addEntityLst( itemRoot, rootEntity.getEntityLst(), true );
			}
			return;
		}
		
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
			TreeItem<SchemaEntity> itemNew = this.addItem( itemTarget, schemaEntity );
			if ( schemaEntity.getEntityLst().size() > 0 )
			{
				this.addEntityLst( itemNew, schemaEntity.getEntityLst(), false );
			}
		}
		
		itemTarget.setExpanded(isExpanded);
		this.scrollBack(itemTarget);
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.setContextMenu();
	}
	
}
