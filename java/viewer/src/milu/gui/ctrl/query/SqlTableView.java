package milu.gui.ctrl.query;

import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.util.Callback;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import milu.ctrl.ToggleHorizontalVerticalInterface;
import milu.ctrl.CopyInterface;
import milu.ctrl.ChangeLangInterface;
import milu.tool.MyTool;
import milu.file.MyFileAbstract;
import milu.file.MyFileFactory;
import milu.gui.dlg.MyAlertDialog;

public class SqlTableView extends TableView<List<String>>
	implements 
		ToggleHorizontalVerticalInterface,
		CopyInterface,
		ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
		"conf.lang.ctrl.query.SqlTableView";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// Listener for "this.tableViewSQLDirection = 2:vertical"
	// This listner enables to select the whole column, when cliking a cell.
    @SuppressWarnings("rawtypes")
	private ChangeListener<TablePosition> tableViewSQLChangeListner = null;
    
    // Callback for CellEdit
    private Callback<TableColumn<List<String>,String>, TableCell<List<String>,String>> cellFactory = null;
	
	// tableViewSQL Direction
	private Orientation tableViewSQLDirection = Orientation.HORIZONTAL;

	@SuppressWarnings({"rawtypes","unchecked"})
	public SqlTableView()
	{
		super();
		
        // enable to select multi rows
        this.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        
        // -----------------------------------------------------------------
        // Listener for "this.tableViewSQLDirection = Orientation.VERTICAL"
        // -----------------------------------------------------------------
		// https://stackoverflow.com/questions/37739593/javafx-property-remove-listener-not-work
		// https://stackoverflow.com/questions/26424769/javafx8-how-to-create-listener-for-selection-of-row-in-tableview
		this.tableViewSQLChangeListner = new ChangeListener<TablePosition>()
		{
			@Override
		    public void changed
		    (
		    	ObservableValue<? extends TablePosition> obs,
		        TablePosition oldVal, 
		        TablePosition newVal
		    ) 
			{
        		if ( newVal.getTableColumn() != null )
        		{
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
        		}
			}
		};
		
		this.setEditable(true);
		// Callback for CellEdit
		this.cellFactory =
			new Callback<TableColumn<List<String>,String>, TableCell<List<String>,String>>()
			{
				public TableCell<List<String>,String> call(TableColumn<List<String>,String> p)
				{
					return new EditingCell();
				}
			};
        
		this.setContextMenu();
	}
	
	private void setContextMenu()
	{
		ContextMenu contextMenu = new ContextMenu();
		
		MenuItem menuItemCopyTblNoHead = new MenuItem( langRB.getString("MENU_COPY_TABLE_NO_HEAD") );
		menuItemCopyTblNoHead.setOnAction( event->this.copyTableNoHead() );
		
		MenuItem menuItemCopyTblWithHead = new MenuItem( langRB.getString("MENU_COPY_TABLE_WITH_HEAD") );
		menuItemCopyTblWithHead.setOnAction( event->this.copyTableWithHead() );

		MenuItem menuItemSave2Excel = new MenuItem( langRB.getString("MENU_SAVE_2_EXCEL") );
		menuItemSave2Excel.setOnAction( event->this.save2Excel() );
		
		contextMenu.getItems().addAll
		( 
			menuItemCopyTblNoHead, 
			menuItemCopyTblWithHead,
			new SeparatorMenuItem(),
			menuItemSave2Excel
		);
		
		this.setOnContextMenuRequested( (event)->{ contextMenu.show( this, event.getScreenX(), event.getScreenY() ); } );
	}
	
	public synchronized int getRowSize()
	{
		// Horizontal
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			return this.getItems().size();
		}
		// Vertical
		else
		{
			return this.getColumns().size();
		}
	}
	
	/**************************************************
	 * Override from ToggleHorizontalVerticalInterface
	 ************************************************** 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public synchronized void switchDirection()
	{
		// Horizontal => Vertical
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			// Get ColumnName from TableView 
			/**/
			// https://stackoverflow.com/questions/41104798/javafx-simplest-way-to-get-cell-data-using-table-index
			int colSize = this.getColumns().size();
			List<String> headLst = new ArrayList<String>();
			// skip "No" column, so start from 1
			for ( int i = 1; i < colSize; i++ )
			{
				TableColumn<List<String>,?> tableColumn = this.getColumns().get(i);
				headLst.add( tableColumn.getText() );
			}
			/**/
			//List<String> headLst = this.getHeadList();
			
			/**/
			// Get Data from TableView
			int rowSize = this.getItems().size();
			List<List<String>> dataLst = new ArrayList<List<String>>();
			for ( int i = 0; i < rowSize; i++ )
			{
				List<String> dataRow = this.getItems().get(i);
				
				// remove "No" column
				dataRow.remove( 0 );
				dataLst.add( dataRow );
			}
			/**/
			//List<List<String>> dataLst = this.getDataList();
			
			
			// Switch Direction of tableVieSQL from Horizontal to Vertical
			this.tableViewSQLDirection = Orientation.VERTICAL;
			this.setTableViewSQL( headLst, dataLst );

			
	        // enable to select the whole column
	        // https://stackoverflow.com/questions/38012247/javafx-tableview-select-the-whole-tablecolumn-and-get-the-index
	        this.getSelectionModel().setCellSelectionEnabled( true );
	        this.getFocusModel().focusedCellProperty().addListener
	        ( 
	        	(ChangeListener<? super TablePosition>)this.tableViewSQLChangeListner 
	        );
		}
		// Vertical => Horizontal
		else
		{
			// Get ColumnName from TableView
			/**/
			int colSize = this.getItems().size();
			List<String> headLst = new ArrayList<String>();
			for ( int i = 0; i < colSize; i++ )
			{
				List<String> dataRow = this.getItems().get(i);
				// ColumnName
				headLst.add( dataRow.get(0) );
			}
			/**/
			//List<String> headLst = this.getHeadList();
			
			// Get Data from TableView 
			/**/
			int rowSize = this.getColumns().size();
			List<List<String>> dataLst = new ArrayList<List<String>>();
			for ( int i = 1; i < rowSize; i++ )
			{
				TableColumn<List<String>,?> tableColumn = this.getColumns().get(i);
				List<String> dataRow = new ArrayList<String>();
				for ( int j = 0; j < colSize; j++ )
				{
					String data = (String)tableColumn.getCellObservableValue(j).getValue();
					dataRow.add( data );
				}
				dataLst.add( dataRow );
			}
			/**/
			//List<List<String>> dataLst = this.getDataList();
			
			// Switch Direction of tableVieSQL from Vertical to Horizontal
			this.tableViewSQLDirection = Orientation.HORIZONTAL;
			this.setTableViewSQL( headLst, dataLst );
			
	        // disable to select the whole column
	        // https://stackoverflow.com/questions/38012247/javafx-tableview-select-the-whole-tablecolumn-and-get-the-index
	        this.getSelectionModel().setCellSelectionEnabled( false );
	        this.getFocusModel().focusedCellProperty().removeListener
	        ( 
	        	(ChangeListener<? super TablePosition>)this.tableViewSQLChangeListner 
	        );
		}
	}
		
	
	// Set ColumnName & Data to TableView Horizontally
	public synchronized void setTableViewSQL( List<String> headLst, List<List<String>> dataLst )
	{
		// Clear TableView
		this.getItems().clear();
		this.getColumns().clear();
		
		// horizontal
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			this.setTableViewSQLbyHorizontal( headLst, dataLst );
		}
		// vertical
		else
		{
			this.setTableViewSQLbyVertical( headLst, dataLst );
		}
	}
	
	// Set ColumnName & Data to TableView Horizontally
	// ---------------------------------------------------
	// HORIZONTAL  | column1        | column2        |
	// ---------------------------------------------------
	// data1       | data1(column1) | data2(column2) |
	// ---------------------------------------------------
	// data2       | data2(column1) | data2(column2) |
	// ---------------------------------------------------
	private void setTableViewSQLbyHorizontal( List<String> headLst, List<List<String>> dataLst )
	{
		// +1 for "HORIZONTAL"
		int headCnt = headLst.size() + 1;
		
		// Add ColumnName to TableView
		// https://stackoverflow.com/questions/12211822/populating-a-tableview-with-strings
		for ( int i = 1; i <= headCnt; i++ )
		{
			TableColumn<List<String>,String> tableCol = null;
			// set "VERTICAL" to the first column header 
			if ( i == 1 )
			{
				tableCol = new TableColumn<List<String>,String>( "-" );
				System.out.println( "tableCol TypeSelector:" + tableCol.getTypeSelector() );
				
				// https://stackoverflow.com/questions/16857031/javafx-table-column-different-font-with-css
				// tableCol:styleClass=table-column
				tableCol.getStyleClass().add("cell-first");
				
				// disable sort of the first column
				tableCol.setSortable(false);
			}
			// set "Column Name" to the second - last column header 
			else
			{
				tableCol = new TableColumn<List<String>,String>( headLst.get( i-2 ) );

				// To copy data, open an edit cell.
				// Do not change the cell data, whatever the user inputs
				tableCol.setCellFactory( this.cellFactory );
				tableCol.setOnEditCommit( e->{} );
			}
			
			final int index = i;
			tableCol.setCellValueFactory
			(
				new Callback<TableColumn.CellDataFeatures<List<String>,String>, ObservableValue<String>>()
				{
					@Override
					public ObservableValue<String> call( TableColumn.CellDataFeatures<List<String>, String> p )
					{
						List<String> x = p.getValue();
						if ( x != null )
						{
							return new SimpleStringProperty( x.get( index - 1 ) );
						}
						else
						{
							return new SimpleStringProperty( "<NULL>" );
						}
					}
				}
			);
			this.getColumns().add( tableCol );
		}

		// Reconstruct data list on TableView(horizontal mode)
		for ( int i = 0; i < dataLst.size(); i++ )
		{
			// add "No" on first column
			List<String> dataRow = dataLst.get( i );
			dataRow.add( 0, String.valueOf(i+1) );
		}
		
		// Add Data to TableView
		this.getItems().addAll( dataLst );
	}
	
	// Set ColumnName & Data to TableView Vertically
	// ---------------------------------------------------
	// VERTICAL | data1          | data2          |
	// ---------------------------------------------------
	// column1  | data1(column1) | data2(column1) |
	// ---------------------------------------------------
	// column2  | data1(column2) | data2(column2) |
	// ---------------------------------------------------
	private void setTableViewSQLbyVertical( List<String> headLst, List<List<String>> dataLst )
	{
		// +1 for "VERTICAL"
		int headCnt = dataLst.size()+1;
		
		// Add ColumnName to TableView
		// https://stackoverflow.com/questions/12211822/populating-a-tableview-with-strings
		for ( int i = 1; i <= headCnt; i++ )
		{
			TableColumn<List<String>,String> tableCol = null;
			// set "VERTICAL" to the first column header 
			if ( i == 1 )
			{
				tableCol = new TableColumn<List<String>,String>( "-" );
				ObservableList<String> styleClassList = tableCol.getStyleClass();
				for ( String styleClass : styleClassList )
				{
					// table-column
					System.out.println( "styleClass:" + styleClass );
				}
				
				// https://stackoverflow.com/questions/16857031/javafx-table-column-different-font-with-css
				// tableCol:styleClass=table-column
				tableCol.getStyleClass().add("cell-first");
				
				// disable sort of the first column
				tableCol.setSortable(false);
			}
			// set "Data No" to the second - last column header 
			else
			{
				tableCol = new TableColumn<List<String>,String>( Integer.toString( i-1 ) );
				// To copy data, open an edit cell.
				// Do not change the cell data, whatever the user inputs
				tableCol.setCellFactory( this.cellFactory );
				tableCol.setOnEditCommit( e->{} );
			}
			
			final int index = i;
			tableCol.setCellValueFactory
			(
				new Callback<TableColumn.CellDataFeatures<List<String>,String>, ObservableValue<String>>()
				{
					@Override
					public ObservableValue<String> call( TableColumn.CellDataFeatures<List<String>, String> p )
					{
						List<String> x = p.getValue();
						if ( x != null )
						{
							return new SimpleStringProperty( x.get( index - 1 ) );
						}
						else
						{
							return new SimpleStringProperty( "<NULL>" );
						}
					}
				}
			);
			this.getColumns().add( tableCol );
		}

		// Reconstruct data list on TableView(vertical mode)
		List<List<String>> dataLst2 = new ArrayList<List<String>>();
		for ( int i = 0; i < headLst.size(); i++ )
		{
			List<String> dataLst3 = new ArrayList<String>();
			for ( int j = 0; j < headCnt ; j++ )
			{
				// DB Column Name
				if ( j == 0 )
				{
					dataLst3.add( headLst.get( i ) );
				}
				// DB Data(1 shift behind)
				else
				{
					//List<String> tmp1 = dataLst.get(j - 1);
					//String tmp2 = tmp1.get(i);
					//System.out.println( "i=" + i + "/j=" + j + "/str=" + tmp2 );
					//dataLst3.add( tmp2 );
					
					// -----------------------------
					// j:row/i:col
					// -----------------------------
					dataLst3.add( dataLst.get(j-1).get(i) );
				}
			}
			dataLst2.add( dataLst3 );
		}
		
		// Add Data to TableView
		this.getItems().addAll( dataLst2 );
	}
	
	/**************************************************
	 * Override from CopyInterface
	 ************************************************** 
	 */
	@Override
	public synchronized void copyTableNoHead()
	{
		this.copyTable( 0 );
	}

	/**************************************************
	 * Override from CopyInterface
	 ************************************************** 
	 */
	@Override
	public synchronized void copyTableWithHead()
	{
		this.copyTable( 1 );
	}

	// @withHead
	//   1:Copy table data with heads
	//   0:Copy table data without heads
	@SuppressWarnings("rawtypes")
	private void copyTable( int withHead )
	{
		StringBuffer sbTmp = new StringBuffer();
		// Horizontal Mode
		// https://stackoverflow.com/questions/25170119/allow-user-to-copy-data-from-tableview
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			int colSize = this.getColumns().size();
			// Create Head Part
			if ( withHead == 1 )
			{
				// skip "No" column, so start from 1
				for ( int i = 1; i < colSize; i++ )
				{
					if ( i != 1 )
					{
						sbTmp.append( "\t" );
					}
					TableColumn<List<String>,?> tableColumn = this.getColumns().get(i);
					sbTmp.append( tableColumn.getText() );
				}
				sbTmp.append( "\n" );
			}
			
			// Create Data Part
			ObservableList<Integer> selectedIndices = this.getSelectionModel().getSelectedIndices();
			for ( Integer id : selectedIndices )
			{
				// skip "No" column, so start from 1
				for ( int i = 1; i < colSize; i++ )
				{
					if ( i != 1 )
					{
						sbTmp.append( "\t" );
					}
					String val = this.getItems().get( id ).get( i );
					if ( val != null )
					{
						sbTmp.append( val );
					}
				}
				sbTmp.append( "\n" );
			}
		}
		// Vertical Mode
		// https://stackoverflow.com/questions/24441175/how-detect-which-column-selected-in-javafx-tableview
		else
		{
			System.out.println( "---------------" );
			
			// --------------------------------------
			// Calculate which columns are selected.
			// --------------------------------------
			int minCol = -1;
			int maxCol = -1;
			ObservableList<TablePosition> selectedCells = this.getSelectionModel().getSelectedCells();
			for ( TablePosition tblPos : selectedCells )
			{
				//System.out.println( "tblPos(col):" + tblPos.getColumn() + "/tblPos(row):" + tblPos.getRow() );
				int col = tblPos.getColumn();
				if ( minCol == -1 )
				{
					minCol = col;
				}
				if ( maxCol == -1 )
				{
					maxCol = col;
				}
				if ( col < minCol )
				{
					minCol = col;
				}
				if ( col > maxCol )
				{
					maxCol = col;
				}
			}
			// skip "No" column, change to 1.
			if ( minCol == 0 )
			{
				minCol = 1;
			}

			int rowSize = this.getItems().size();
			for ( int i = 0; i < rowSize; i++ )
			{
				// Create Head Part
				if ( withHead == 1 )
				{
					String val = this.getItems().get( i ).get( 0 );
					if ( val != null )
					{
						sbTmp.append( val );
					}
					sbTmp.append( "\t" );
				}
				
				// Create Data Part
				for ( int j = minCol; j <= maxCol; j++ )
				{
					String val = this.getItems().get( i ).get( j );
					//System.out.println( "i:" + i + "/j:" + j + "/val:" + val );
					if ( val != null )
					{
						sbTmp.append( val );
					}
					if ( j != maxCol )
					{
						sbTmp.append( "\t" );
					}
				}
				sbTmp.append( "\n" );
			}
		}
		
		// Copy to clipboard
		final ClipboardContent content = new ClipboardContent();
		content.putString( sbTmp.toString() );
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent( content );
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
			MyFileAbstract myFileAbs = MyFileFactory.getInstance( file );
			try
			{
				myFileAbs.open( file );
				myFileAbs.save( this.getHeadList(), this.getDataList() );
			}
			catch( IOException ioEx )
			{
				MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
				alertDlg.setHeaderText( langRB.getString("TITLE_SAVE_ERROR") );
	    		alertDlg.setTxtExp( ioEx );
	    		alertDlg.showAndWait();
	    		alertDlg = null;
	    	}
			finally
			{
				myFileAbs.close();
				myFileAbs = null;
			}
		}
	}
	
	// Get ColumnName from TableView 
	private List<String> getHeadList()
	{
		List<String> headLst = new ArrayList<String>();
		// Horizontal Mode
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			// Get Data from TableView
			int colSize = this.getColumns().size();
			// skip "No" column, so start from 1
			for ( int i = 1; i < colSize; i++ )
			{
				TableColumn<List<String>,?> tableColumn = this.getColumns().get(i);
				headLst.add( tableColumn.getText() );
			}			
		}
		// Vertical Mode
		else
		{
			// Get ColumnName from TableView 
			int colSize = this.getItems().size();
			for ( int i = 0; i < colSize; i++ )
			{
				List<String> dataRow = this.getItems().get(i);
				// ColumnName
				headLst.add( dataRow.get(0) );
			}			
		}
		return headLst;
	}
	
	// Get Data from TableView 
	private List<List<String>> getDataList()
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		// Horizontal Mode
		if ( this.tableViewSQLDirection == Orientation.HORIZONTAL )
		{
			int rowSize = this.getItems().size();
			int colSize = this.getColumns().size();
			for ( int i = 0; i < rowSize; i++ )
			{
				List<String> dataRowOrg = this.getItems().get(i);
				List<String> dataRow = new ArrayList<String>();
				
				// remove "No" column
				for ( int j = 1; j < colSize; j++ )
				{
					dataRow.add( dataRowOrg.get(j) );
				}
				
				dataLst.add( dataRow );
			}
		}
		// Vertical Mode
		else
		{
			int rowSize = this.getColumns().size();
			int colSize = this.getItems().size();
			for ( int i = 1; i < rowSize; i++ )
			{
				TableColumn<List<String>,?> tableColumn = this.getColumns().get(i);
				List<String> dataRow = new ArrayList<String>();
				for ( int j = 0; j < colSize; j++ )
				{
					String data = (String)tableColumn.getCellObservableValue(j).getValue();
					dataRow.add( data );
				}
				dataLst.add( dataRow );
			}			
		}
		return dataLst;
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
		
		System.out.println( "TableViewSQL refreshing..." );
		// it doesn't seem to work well.
		// I hope "表に列はありません" => "No columns in table"
		this.refresh();
	}
	
	class EditingCell extends TableCell<List<String>, String>
	{
		private TextInputControl textInputCtrl = null;
		
		public EditingCell()
		{
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
		protected void updateItem( String item, boolean empty )
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
