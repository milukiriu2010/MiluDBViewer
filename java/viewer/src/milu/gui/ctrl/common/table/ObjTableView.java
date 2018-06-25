package milu.gui.ctrl.common.table;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.geometry.Orientation;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.util.Callback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import milu.tool.MyTool;
import milu.file.table.MyFileExportAbstract;
import milu.file.table.MyFileExportFactory;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.SetTableViewDataInterface;
import milu.gui.view.DBView;

public class ObjTableView extends TableView<List<Object>>
	implements 
		SetTableViewDataInterface,
		DirectionSwitchInterface,
		CopyTableInterface,
		ChangeLangInterface
{
	private DBView  dbView = null;
	
	private List<Object>        headObjLst = new ArrayList<>();
	private List<List<Object>>  dataObjLst = new ArrayList<>();
	
	private int skipRowCnt = 0;
	
	// Listener for "this.tableViewDirection = 2:vertical"
	// This listener enables to select the whole column, when cliking a cell.
	@SuppressWarnings("rawtypes")
	ChangeListener<TablePosition> tableViewChangeListner = null;
    
    Set<TableColumn<List<Object>,Object>>  tableColSet = new HashSet<>();
    
    // Callback for CellEdit
    private Callback<TableColumn<List<Object>,Object>, TableCell<List<Object>,Object>> cellFactory = null;
	
	// tableView Direction
	private Orientation tableViewDirection = Orientation.HORIZONTAL;
	
	enum COPY_TYPE
	{
		WITH_HEAD,
		NO_HEAD
	}

	// @SuppressWarnings({"rawtypes","unchecked"})
	@SuppressWarnings({"unchecked"})
	public ObjTableView( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
        // enable to select multi rows
        this.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        //this.getSelectionModel().setCellSelectionEnabled( true );
        
        // -----------------------------------------------------------------
        // Listener for "this.tableViewSQLDirection = Orientation.VERTICAL"
        // -----------------------------------------------------------------
		// https://stackoverflow.com/questions/37739593/javafx-property-remove-listener-not-work
		// https://stackoverflow.com/questions/26424769/javafx8-how-to-create-listener-for-selection-of-row-in-tableview
        // ----------------------------------------------------------
        // new ChangeListener<TablePosition>()
		//{
		//	@Override
		//    public void changed
		//    (
		//    	  ObservableValue<? extends TablePosition> obs,
		//        TablePosition oldVal, 
		//        TablePosition newVal
		//    )
        // ----------------------------------------------------------
		this.tableViewChangeListner = (obs,oldVal,newVal)->
			{
				/*
				if ( oldVal.getTableColumn() != null )
				{
					this.tableColSet.add(oldVal.getTableColumn());
				}
				*/
        		if ( newVal.getTableColumn() != null )
        		{
        			/*
        			if ( this.tableColSet.size() == 0 )
        			{
	        			for ( int i = 0; i < getItems().size(); i++ )
	        			{
	        				getSelectionModel().clearSelection(i,oldVal.getTableColumn());
	        			}
        			}
        			*/
        			/*
        			this.tableColSet.forEach
        			(
        				(tableCol)->
        				{
                			getSelectionModel().selectRange
            				( 
            					0, 
            					tableCol, 
            					getItems().size(), 
            					tableCol 
            				);
        				}
        			);
        			*/
        			
        			// set the range of selection on this TableView
        			// select all rows on the same column
        			getSelectionModel().selectRange
    				( 
    					0, 
    					newVal.getTableColumn(), 
    					getItems().size(), 
    					newVal.getTableColumn() 
    				);
        			System.out.println( "---------------" );
        			System.out.println( "V Selected new TableColumn : " + newVal.getTableColumn().getText() );
        			System.out.println( "V Selected new column index: " + newVal.getColumn() );
        			System.out.println( "V Selected old column index: " + oldVal.getColumn() );
        			System.out.println( "tableColSet size           : " + this.tableColSet.size() );
        		}
			};
        
		this.setEditable(true);
		// Callback for CellEdit
		final ObjTableView objTableView = this;
		this.cellFactory =
			new Callback<TableColumn<List<Object>,Object>, TableCell<List<Object>,Object>>()
			{
				public TableCell<List<Object>,Object> call(TableColumn<List<Object>,Object> p)
				{
					return new EditingCell(objTableView);
				}
			};
		
		/*
		this.setRowFactory((tableView)->{
			TableRow<List<Object>> row = new TableRow<>();
			//row.setStyle("-fx-text-background-color:red;");
			row.setStyle("-fx-background-color:#808080;");
			return row;
		});
		*/
        
		this.setContextMenu();
	}
	
	public int getSkipRowCnt()
	{
		return this.skipRowCnt;
	}
	
	public void setSkipRowCnt( int skipRowCnt )
	{
		this.skipRowCnt = skipRowCnt;
		this.setRowFactory((tableView)->{
			final TableRow<List<Object>> row = new TableRow<>() {
				@Override
				protected void updateItem( List<Object> dataLst, boolean empty )
				{
					super.updateItem(dataLst, empty );
					
					if (!empty)
					{
						Object obj = dataLst.get(0);
						if ( obj instanceof Number )
						{
							Number num = (Number)obj;
							if ( num.intValue() <= ObjTableView.this.skipRowCnt )
							{
								setStyle("-fx-background-color:#808080;");
							}
							else
							{
								getStyleClass().remove("-fx-background-color:#808080;");
							}
						}
					}
				}
			};
			
			return row;
		});
		this.refresh();
	}
	
	void selectArea()
	{
		getSelectionModel().clearSelection();
		
		this.tableColSet.forEach((tableCol)->{
			getSelectionModel().selectRange
			( 
				0, 
				tableCol, 
				getItems().size(), 
				tableCol 
			);
		});
	}
	
	Callback<TableColumn<List<Object>,Object>, TableCell<List<Object>,Object>> getCellFactory()
	{
		return this.cellFactory;
	}
	
	private void setContextMenu()
	{
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.ctrl.common.table.ObjTableView");
		ContextMenu contextMenu = new ContextMenu();
		
		MenuItem menuItemCopyTblNoHead = new MenuItem( langRB.getString("MENU_COPY_TABLE_NO_HEAD") );
		menuItemCopyTblNoHead.setOnAction( this::copyTableNoHead );
		
		MenuItem menuItemCopyTblWithHead = new MenuItem( langRB.getString("MENU_COPY_TABLE_WITH_HEAD") );
		menuItemCopyTblWithHead.setOnAction( this::copyTableWithHead );

		MenuItem menuItemSave2Excel = new MenuItem( langRB.getString("MENU_SAVE_2_EXCEL") );
		menuItemSave2Excel.setOnAction( event->this.save2Excel() );

		//MenuItem menuItemToggleHV = new MenuItem( langRB.getString("MENU_TOGGLE_HV") );
		//menuItemToggleHV.setOnAction( event->this.switchDirection() );
		
		contextMenu.getItems().addAll
		( 
			menuItemCopyTblNoHead, 
			menuItemCopyTblWithHead,
			new SeparatorMenuItem(),
			menuItemSave2Excel //,
			//new SeparatorMenuItem(),
			//menuItemToggleHV
		);
		
		//this.setOnContextMenuRequested( (event)->{ contextMenu.show( this, event.getScreenX(), event.getScreenY() ); } );
		this.setContextMenu(contextMenu);
	}
	
	public synchronized int getRowSize()
	{
		/*
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		return tpAbs.getRowSize();
		*/
		return this.dataObjLst.size();
	}
	
	// DirectionSwitchInterface
	@Override
	public Orientation getOrientation()
	{
		return this.tableViewDirection;
	}
	
	// DirectionSwitchInterface
	@Override
	public void setOrientation( Orientation orientation )
	{
		this.tableViewDirection = orientation;
	}
	
	// DirectionSwitchInterface
	@Override
	public synchronized void switchDirection( Event event )
	{
		this.tableColSet.clear();
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		tpAbs.switchDirection();
	}
	
	
	// Set ColumnName & Data to TableView Horizontally
	@Override
	public synchronized void setTableViewData( List<Object> headLst, List<List<Object>> dataLst )
	{
		// Clear TableView
		this.getItems().clear();
		this.getColumns().clear();
		this.headObjLst.clear();
		this.dataObjLst.clear();
		//this.headObjLst = headLst.stream().collect(Collectors.toList());
		//this.dataObjLst = dataLst.stream().collect(Collectors.toList());
		//this.headObjLst = new ArrayList<>( headLst );
		//this.dataObjLst = new ArrayList<>( dataLst );
		this.headObjLst = headLst;
		this.dataObjLst = dataLst;
		
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		tpAbs.setData( this.headObjLst, this.dataObjLst );
	}

	synchronized void setTableViewData()
	{
		// Clear TableView
		this.getItems().clear();
		this.getColumns().clear();
		
		//List<Object>       headLst = this.headObjLst.stream().collect(Collectors.toList());
		//List<List<Object>> dataLst = this.dataObjLst.stream().collect(Collectors.toList());
		//List<Object>        headLst = new ArrayList<>( this.headObjLst );
		//List<List<Object>>  dataLst = new ArrayList<>( this.dataObjLst );
		
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		//tpAbs.setData( headLst, dataLst );
		tpAbs.setData( this.headObjLst, this.dataObjLst );
	}
	
	// CopyTableInterface
	@Override
	public synchronized void copyTableNoHead( Event event )
	{
		this.copyTable( COPY_TYPE.NO_HEAD );
	}
	// CopyTableInterface
	@Override
	public synchronized void copyTableWithHead( Event event )
	{
		this.copyTable( COPY_TYPE.WITH_HEAD );
	}
	
	// @withHead
	//   1:Copy table data with heads
	//   0:Copy table data without heads
	private void copyTable( COPY_TYPE copyType )
	{
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		tpAbs.copyTable(copyType);
	}
	
	private void save2Excel()
	{
		FileChooser  fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll
		(
			new ExtensionFilter( "Excel Files", "*.xlsx" )
		);
		File file = fileChooser.showSaveDialog( this.getScene().getWindow() );
		if ( file != null )
		{
			MyFileExportAbstract myFileAbs = MyFileExportFactory.getInstance( file );
			try
			{
				myFileAbs.open( file );
				List<Object> headLst = this.getHeadList();
				List<List<Object>> dataLst = this.getDataList();
				List<String> strHeadLst = headLst.stream().map( x->x.toString() ).collect(Collectors.toList());
				List<List<String>> strDataLst = 
					dataLst.stream()
						.map( x->x.stream().map( y->y.toString() ).collect(Collectors.toList()) )
						.collect(Collectors.toList());
				myFileAbs.save( strHeadLst, strDataLst );
			}
			catch( IOException ioEx )
			{
				MyTool.showException( this.dbView.getMainController(), "conf.lang.gui.ctrl.query.ObjTableView", "TITLE_SAVE_ERROR", ioEx );
	    	}
			finally
			{
				myFileAbs.close();
				myFileAbs = null;
			}
		}
	}
	
	// Get ColumnName from TableView 
	public List<Object> getHeadList()
	{
		/*
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		return tpAbs.getHeadList();
		*/
		return this.headObjLst;
	}
	
	// Get Data from TableView 
	public List<List<Object>> getDataList()
	{
		/*
		TableProcessAbstract tpAbs = TableProcessFactory.getInstance( this.tableViewDirection, this );
		return tpAbs.getDataList();
		*/
		return this.dataObjLst;
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.setContextMenu();
		
		System.out.println( "TableViewSQL refreshing..." );
		// it doesn't seem to work well.
		// I hope "表に列はありません" => "No columns in table"
		this.refresh();
	}
	
	class EditingCell extends TableCell<List<Object>, Object>
	{
		private TextInputControl textInputCtrl = null;
		
		public EditingCell( ObjTableView objTableView )
		{
			this.addEventFilter
			(
				MouseEvent.MOUSE_CLICKED, 
				(event)->
				{
					System.out.println( "Mouse Clicked." );
					if ( event.isControlDown() || event.isShiftDown() )
					{
						objTableView.tableColSet.add(this.getTableColumn());
						//objTableView.selectArea();
					}
					else
					{
						//objTableView.selectArea();
						objTableView.tableColSet.clear();
					}
				}
			);
		}
		
		/*
		 * (non-Javadoc)
		 * @see javafx.scene.control.TableCell#startEdit()
		 */
		@Override
		public void startEdit()
		{
			if ( !this.isEmpty() )
			{
				super.startEdit();
				this.createTextField();
				this.setText( null );
				this.setGraphic( this.textInputCtrl );
				this.textInputCtrl.selectAll();
				this.textInputCtrl.requestFocus();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see javafx.scene.control.TableCell#cancelEdit()
		 */
		@Override
		public void cancelEdit()
		{
			super.cancelEdit();
			
			setText((String)getItem());
			setGraphic(null);
		}
		
		/**
		 * Callback for Cell
		 */
		@Override
		protected void updateItem( Object item, boolean empty )
		{
			super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textInputCtrl != null) {
                    	textInputCtrl.setText(getString());
                    }
                    setText(null);
                    setGraphic(textInputCtrl);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
		}
		
        private void createTextField()
        {
        	String str = getString();
        	int rtnStr = MyTool.getCharCount( str, "\n" );
        	if ( rtnStr == 0 )
        	{
        		textInputCtrl = new TextField( str );
        	}
        	else
        	{
        		textInputCtrl = new TextArea( str );
        		((TextArea)textInputCtrl).setPrefRowCount( rtnStr );
        	}
        	textInputCtrl.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2 );
        	textInputCtrl.focusedProperty().addListener
        	(
        		new ChangeListener<Boolean>()
        		{
	                @Override
	                public void changed
	                (
	                	ObservableValue<? extends Boolean> arg0, 
	                    Boolean arg1, 
	                    Boolean arg2
	                )
	                {
	                    if (!arg2)
	                    {
	        				// To copy data, open an edit cell.
	        				// Do not change the cell data, no matter what an user inputs
	                        //commitEdit(textField.getText());
	                    }
	                }
        		}
        	);
        }
        
        private String getString()
        {
            return getItem() == null ? "" : getItem().toString();
        }
        
    }
}
