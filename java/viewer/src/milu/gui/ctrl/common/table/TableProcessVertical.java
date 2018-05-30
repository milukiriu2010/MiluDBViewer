package milu.gui.ctrl.common.table;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
//import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;

import milu.gui.ctrl.common.table.ObjTableView.COPY_TYPE;

class TableProcessVertical extends TableProcessAbstract 
{
	@Override
	int getRowSize() 
	{
		return this.objTableView.getColumns().size();
	}
	
	// Vertical => Horizontal
	@SuppressWarnings("rawtypes")
	@Override
	void switchDirection()
	{
		// Get ColumnName from TableView
		/**/
		int colSize = this.objTableView.getItems().size();
		List<Object> headLst = new ArrayList<>();
		for ( int i = 0; i < colSize; i++ )
		{
			List<Object> dataRow = this.objTableView.getItems().get(i);
			// ColumnName
			headLst.add( dataRow.get(0) );
		}
		/**/
		//List<String> headLst = this.getHeadList();
		
		// Get Data from TableView 
		/**/
		int rowSize = this.objTableView.getColumns().size();
		List<List<Object>> dataLst = new ArrayList<>();
		for ( int i = 1; i < rowSize; i++ )
		{
			TableColumn<List<Object>,?> tableColumn = this.objTableView.getColumns().get(i);
			List<Object> dataRow = new ArrayList<>();
			for ( int j = 0; j < colSize; j++ )
			{
				Object data = tableColumn.getCellObservableValue(j).getValue();
				dataRow.add( data );
			}
			dataLst.add( dataRow );
		}
		/**/
		//List<List<String>> dataLst = this.getDataList();
		
		// Switch Direction of tableVieSQL from Vertical to Horizontal
		this.objTableView.setTableViewDirection( Orientation.HORIZONTAL );
		this.objTableView.setTableViewData( headLst, dataLst );
		
        // disable to select the whole column
        // https://stackoverflow.com/questions/38012247/javafx-tableview-select-the-whole-tablecolumn-and-get-the-index
        this.objTableView.getSelectionModel().setCellSelectionEnabled( false );
        this.objTableView.getFocusModel().focusedCellProperty().removeListener
        ( 
        	(ChangeListener<? super TablePosition>)this.objTableView.tableViewChangeListner 
        );
	}

	// Set ColumnName & Data to TableView Vertically
	// ---------------------------------------------------
	// VERTICAL | data1          | data2          |
	// ---------------------------------------------------
	// column1  | data1(column1) | data2(column1) |
	// ---------------------------------------------------
	// column2  | data1(column2) | data2(column2) |
	// ---------------------------------------------------
	@Override
	void setData( List<Object> headLst, List<List<Object>> dataLst )
	{
		// +1 for "VERTICAL"
		int headCnt = dataLst.size()+1;
		
		// Add ColumnName to TableView
		// https://stackoverflow.com/questions/12211822/populating-a-tableview-with-strings
		for ( int i = 1; i <= headCnt; i++ )
		{
			TableColumn<List<Object>,Object> tableCol = null;
			// set "VERTICAL" to the first column header 
			if ( i == 1 )
			{
				tableCol = new TableColumn<List<Object>,Object>( "-" );
				ObservableList<String> styleClassList = tableCol.getStyleClass();
				for ( String styleClass : styleClassList )
				{
					// table-column
					System.out.println( "styleClass:" + styleClass );
				}
				
				// https://stackoverflow.com/questions/16857031/javafx-table-column-different-font-with-css
				// tableCol:styleClass=table-column
				tableCol.getStyleClass().add("ObjTableView_Cell_First");
				
			}
			// set "Data No" to the second - last column header 
			else
			{
				tableCol = new TableColumn<List<Object>,Object>( Integer.toString( i-1 ) );
				// To copy data, open an edit cell.
				// Do not change the cell data, whatever the user inputs
				tableCol.setCellFactory( this.objTableView.getCellFactory() );
				tableCol.setOnEditCommit( e->{} );
			}
			// disable sort by clicking "Column Header"
			tableCol.setSortable(false);
			
			this.setTableColumnCellValueFactory( i, tableCol );
			this.objTableView.getColumns().add( tableCol );
		}

		// Reconstruct data list on TableView(vertical mode)
		List<List<Object>> dataLst2 = new ArrayList<>();
		for ( int i = 0; i < headLst.size(); i++ )
		{
			List<Object> dataLst3 = new ArrayList<>();
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
		//this.objTableView.getItems().addAll( dataLst2 );
		this.objTableView.setItems( FXCollections.observableArrayList(dataLst2) );
	}
	
	// https://stackoverflow.com/questions/24441175/how-detect-which-column-selected-in-javafx-tableview
	@SuppressWarnings("rawtypes")
	@Override
	void copyTable( COPY_TYPE copyType )
	{
		StringBuffer sbTmp = new StringBuffer();
		System.out.println( "---------------" );
		
		// --------------------------------------
		// Calculate which columns are selected.
		// --------------------------------------
		int minCol = -1;
		int maxCol = -1;
		ObservableList<TablePosition> selectedCells = this.objTableView.getSelectionModel().getSelectedCells();
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

		int rowSize = this.objTableView.getItems().size();
		for ( int i = 0; i < rowSize; i++ )
		{
			// Create Head Part
			if ( COPY_TYPE.WITH_HEAD.equals(copyType) )
			{
				Object val = this.objTableView.getItems().get( i ).get( 0 );
				if ( val != null )
				{
					sbTmp.append( val );
				}
				sbTmp.append( "\t" );
			}
			
			// Create Data Part
			for ( int j = minCol; j <= maxCol; j++ )
			{
				Object val = this.objTableView.getItems().get( i ).get( j );
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
		
		// Copy to clipboard
		final ClipboardContent content = new ClipboardContent();
		content.putString( sbTmp.toString() );
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent( content );
	}
	
	@Override
	List<Object> getHeadList()
	{
		List<Object> headLst = new ArrayList<>();
		// Get ColumnName from TableView 
		int colSize = this.objTableView.getItems().size();
		for ( int i = 0; i < colSize; i++ )
		{
			List<Object> dataRow = this.objTableView.getItems().get(i);
			// ColumnName
			headLst.add( dataRow.get(0) );
		}			
		return headLst;
	}
	
	@Override
	List<List<Object>> getDataList()
	{
		List<List<Object>> dataLst = new ArrayList<>();
		int rowSize = this.objTableView.getColumns().size();
		int colSize = this.objTableView.getItems().size();
		for ( int i = 1; i < rowSize; i++ )
		{
			TableColumn<List<Object>,?> tableColumn = this.objTableView.getColumns().get(i);
			List<Object> dataRow = new ArrayList<>();
			for ( int j = 0; j < colSize; j++ )
			{
				Object data = tableColumn.getCellObservableValue(j).getValue();
				dataRow.add( data );
			}
			dataLst.add( dataRow );
		}			
		return dataLst;
	}
}
