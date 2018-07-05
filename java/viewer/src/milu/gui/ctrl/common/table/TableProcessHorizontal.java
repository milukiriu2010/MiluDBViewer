package milu.gui.ctrl.common.table;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TablePosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.TableColumn;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.beans.value.ChangeListener;

import milu.gui.ctrl.common.table.ObjTableView.COPY_TYPE;

class TableProcessHorizontal extends TableProcessAbstract 
{
	@Override
	int getRowSize() 
	{
		return this.objTableView.getItems().size();
	}
	
	// Horizontal => Vertical
	@SuppressWarnings("rawtypes")
	@Override
	void switchDirection()
	{
		// Switch Direction of tableVieSQL from Horizontal to Vertical
		this.objTableView.setOrientation( Orientation.VERTICAL );
		this.objTableView.setTableViewData();
		
        // enable to select the whole column
        // https://stackoverflow.com/questions/38012247/javafx-tableview-select-the-whole-tablecolumn-and-get-the-index
        this.objTableView.getSelectionModel().setCellSelectionEnabled( true );
        this.objTableView.getFocusModel().focusedCellProperty().addListener
        ( 
        	(ChangeListener<? super TablePosition>)this.objTableView.tableViewChangeListner 
        );
	}

	// Set ColumnName & Data to TableView Horizontally
	// ---------------------------------------------------
	// HORIZONTAL  | column1        | column2        |
	// ---------------------------------------------------
	// data1       | data1(column1) | data2(column2) |
	// ---------------------------------------------------
	// data2       | data2(column1) | data2(column2) |
	// ---------------------------------------------------	
	@Override
	void setData( List<Object> headLst, List<List<Object>> dataLst )
	{
		// +1 for "HORIZONTAL"
		int headCnt = headLst.size() + 1;
		
		// Add ColumnName to TableView
		// https://stackoverflow.com/questions/12211822/populating-a-tableview-with-strings
		for ( int i = 1; i <= headCnt; i++ )
		{
			TableColumn<List<Object>,Object> tableCol = null;
			// set "VERTICAL" to the first column header 
			if ( i == 1 )
			{
				tableCol = new TableColumn<List<Object>,Object>( "-" );
				
				// https://stackoverflow.com/questions/16857031/javafx-table-column-different-font-with-css
				// tableCol:styleClass=table-column
				tableCol.getStyleClass().add("ObjTableView_Cell_First");
				
				// disable sort of the first column
				//tableCol.setSortable(false);
			}
			// set "Column Name" to the second - last column header 
			else
			{
				tableCol = new TableColumn<List<Object>,Object>( headLst.get( i-2 ).toString() );

				// To copy data, open an edit cell.
				// Do not change the cell data, whatever the user inputs
				tableCol.setCellFactory( this.objTableView.getCellFactory() );
				tableCol.setOnEditCommit( e->{} );
			}
			
			this.setTableColumnCellValueFactory( i, tableCol );
			this.objTableView.getColumns().add( tableCol );
		}

		// Reconstruct data list on TableView(horizontal mode)
		List<List<Object>> dataLst2 = new ArrayList<>();
		double divAssignedSize = this.objTableView.assignedSize/dataLst.size();
		for ( int i = 0; i < dataLst.size(); i++ )
		{
			List<Object>  dataRow  = dataLst.get(i);
			List<Object>  dataRow2 = new ArrayList<>();
			for ( int j = 0; j < dataRow.size(); j++ )
			{
				dataRow2.add(dataRow.get(j));
			}
			// add "No" on first column
			dataRow2.add( 0, Integer.valueOf(i+1) );
			dataLst2.add(dataRow2);
			if ( this.objTableView.progressInf != null )
			{
				this.objTableView.progressInf.addProgress(divAssignedSize);
				this.objTableView.progressInf.setMsg("...");
			}
		}
		
		// Add Data to TableView
		this.objTableView.setItems( FXCollections.observableArrayList(dataLst2) );
	}
	
	// https://stackoverflow.com/questions/25170119/allow-user-to-copy-data-from-tableview
	@Override
	void copyTable( COPY_TYPE copyType )
	{
		StringBuffer sbTmp = new StringBuffer();
		int colSize = this.objTableView.getColumns().size();
		// Create Head Part
		if ( COPY_TYPE.WITH_HEAD.equals(copyType) )
		{
			// skip "No" column, so start from 1
			for ( int i = 1; i < colSize; i++ )
			{
				if ( i != 1 )
				{
					sbTmp.append( "\t" );
				}
				TableColumn<List<Object>,?> tableColumn = this.objTableView.getColumns().get(i);
				sbTmp.append( tableColumn.getText() );
			}
			sbTmp.append( "\n" );
		}
		
		// Create Data Part
		ObservableList<Integer> selectedIndices = this.objTableView.getSelectionModel().getSelectedIndices();
		for ( Integer id : selectedIndices )
		{
			// skip "No" column, so start from 1
			for ( int i = 1; i < colSize; i++ )
			{
				if ( i != 1 )
				{
					sbTmp.append( "\t" );
				}
				Object val = this.objTableView.getItems().get( id ).get( i );
				if ( val != null )
				{
					sbTmp.append( val );
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
	/*
	@Override
	List<Object> getHeadList()
	{
		List<Object> headLst = new ArrayList<>();
		// Get Data from TableView
		int colSize = this.objTableView.getColumns().size();
		// skip "No" column, so start from 1
		for ( int i = 1; i < colSize; i++ )
		{
			TableColumn<List<Object>,?> tableColumn = this.objTableView.getColumns().get(i);
			headLst.add( tableColumn.getText() );
		}			
		return headLst;
	}
	*/
	/*
	@Override
	List<List<Object>> getDataList()
	{
		List<List<Object>> dataLst = new ArrayList<>();
		int rowSize = this.objTableView.getItems().size();
		int colSize = this.objTableView.getColumns().size();
		for ( int i = 0; i < rowSize; i++ )
		{
			List<Object> dataRowOrg = this.objTableView.getItems().get(i);
			List<Object> dataRow = new ArrayList<>();
			
			// remove "No" column
			for ( int j = 1; j < colSize; j++ )
			{
				dataRow.add( dataRowOrg.get(j) );
			}
			
			dataLst.add( dataRow );
		}
		return dataLst;
	}
	*/
}
