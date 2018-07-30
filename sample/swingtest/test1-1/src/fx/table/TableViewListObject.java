package fx.table;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class TableViewListObject extends Application 
{
	private TableView<List<Object>>  tableView = new TableView<>();
	private List<Object>       headLst = Arrays.asList( "No", "First Name", "Last Name", "Age", "HireDay" );
	private List<List<Object>> dataLst = new ArrayList<>();
	private ObservableList<List<Object>> obsDataLst = null;
	
	//private List<Integer>  selectedRowLst = new ArrayList<>();
	
	final HBox hb = new HBox();
	
    public static void main(String[] args) 
    {
        launch(args);
    }
    
	@Override
	public void start(Stage stage) 
	{
		this.initData();
		
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(600);
        stage.setHeight(500);
 
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
        
        this.tableView.setPrefWidth(500);
        this.tableView.setPrefHeight(300);
        this.tableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        this.tableView.getSelectionModel().setCellSelectionEnabled( true );
        this.tableView.setEditable(true);
        //this.tableView.setRowFactory(null);
        
        this.setTableColumnWithData();
        this.setContextMenu();
        
        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(100);
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(100);
        addLastName.setPromptText("Last Name");
        final TextField addAge = new TextField();
        addAge.setMaxWidth(100);
        addAge.setPromptText("Age");        
        
        final Button addButton = new Button("Add");
        addButton.setOnAction
        (
        	(event)->
        	{
        		List<Object> dataRow = new ArrayList<>();
        		dataRow.add( Integer.valueOf(this.obsDataLst.size()+1) );
        		dataRow.add( addFirstName.getText() );
        		dataRow.add( addLastName.getText() );
        		dataRow.add( addAge.getText() );
        		dataRow.add( new Date() );
        		/*
        		this.dataLst.add(dataRow);
        		this.obsDataLst = FXCollections.observableArrayList(this.dataLst);
        		*/
        		this.obsDataLst.add(dataRow);
        		
                addFirstName.clear();
                addLastName.clear();
                addAge.clear();        		
        	}
        );
        
        this.hb.getChildren().addAll(addFirstName, addLastName, addAge, addButton);
        this.hb.setSpacing(3);        
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, this.tableView, this.hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);        
        
        
        stage.setScene(scene);
        stage.show();
	}
	
	private void initData()
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2000, 0, 30);
		List<Object> dataRow1 = Arrays.asList(1,"Michael","Brown",30, cal1.getTime() );
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2004, 1, 29);
		List<Object> dataRow2 = Arrays.asList(2,"Jacob","Smith",30, cal2.getTime() );
		Calendar cal3 = Calendar.getInstance();
		cal3.set(2005, 2, 15);
		List<Object> dataRow3 = Arrays.asList(3,"Isabella","Johnson",100, cal3.getTime() );
		Calendar cal4 = Calendar.getInstance();
		cal4.set(2005, 3, 1);
		List<Object> dataRow4 = Arrays.asList(4,"Ethan","Williams",28, cal4.getTime() );
		Calendar cal5 = Calendar.getInstance();
		cal5.set(2006, 4, 10);
		List<Object> dataRow5 = Arrays.asList(5,"Emma","Jones",9, cal5.getTime() );
		Calendar cal6 = Calendar.getInstance();
		cal6.set(2008, 6, 20);
		List<Object> dataRow6 = Arrays.asList(6,"Patrik","Smith",30, cal6.getTime() );
		
		this.dataLst.add(dataRow1);
		this.dataLst.add(dataRow2);
		this.dataLst.add(dataRow3);
		this.dataLst.add(dataRow4);
		this.dataLst.add(dataRow5);
		this.dataLst.add(dataRow6);
		
		this.obsDataLst = FXCollections.observableArrayList(this.dataLst);
	}

	private void setTableColumnWithData()
	{
		for ( int i = 0; i < this.headLst.size(); i++ )
		{
			Object head = this.headLst.get(i);
			TableColumn<List<Object>,Object> tableCol = new TableColumn<List<Object>,Object>(head.toString());
			/*
			Image image = new Image("file:resources/images/madonna.png" );
	        ImageView iv = new ImageView( image );
	        iv.setFitHeight( 32 );
	        iv.setFitWidth( 32 );
			iv.addEventFilter
			(
				MouseEvent.MOUSE_CLICKED,
				(event)->
				{
					System.out.println("ImageView.clicked:"+head);
					this.tableView.getSelectionModel().selectRange
					(
						0,
						tableCol,
						this.obsDataLst.size()-1,
						tableCol
					);
					event.consume();
				}
			);
			//tableCol.setGraphic(iv);
			*/
			
			Label label = new Label("💛");
			label.setOnMouseClicked
			(
				(event)->
				{
					System.out.println("Label.clicked:"+head);
					TableView.TableViewSelectionModel<List<Object>>  selectionModel = this.tableView.getSelectionModel(); 
					boolean isSelectedAll = true;
					for ( int ii = 0; ii < this.obsDataLst.size(); ii++ )
					{
						if ( selectionModel.isSelected(ii,tableCol) == false )
						{
							isSelectedAll = false;
						}
					}
					
					if ( isSelectedAll == true )
					{
						for ( int ii = 0; ii < this.obsDataLst.size(); ii++ )
						{
							selectionModel.clearSelection( ii, tableCol );
						}
					}
					else
					{
						selectionModel.selectRange
						(
							0,
							tableCol,
							this.obsDataLst.size()-1,
							tableCol
						);
					}
				}
			);
			tableCol.setGraphic(label);
			final int ifinal = i;
			tableCol.setOnEditCommit
			((t)->{
				System.out.println( "setOnEditCommit:" + t.getNewValue() );
				//ObservableList<List<Object>> dataInLst = t.getTableView().getItems();
				int rowID = t.getTablePosition().getRow();
				List<Object> dataRow = this.obsDataLst.get(rowID);
				Object data = dataRow.get(ifinal);
				System.out.println( data.getClass().toString() );
				if ( data instanceof Integer )
				{
					dataRow.set( ifinal, Integer.valueOf(t.getNewValue().toString()) );
				}
				else if ( data instanceof String )
				{
					dataRow.set( ifinal, t.getNewValue() );
				}
				else if ( data instanceof java.util.Date )
				{
					DateFormat dateFmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
					try
					{
						dataRow.set( ifinal, dateFmt.parse(t.getNewValue().toString()) );
					}
					catch ( ParseException parseEx )
					{
						
					}
				}
				else
				{
					dataRow.set( ifinal, t.getNewValue() );
				}
				/*
				TableColumn<List<Object>,Object> tableColEdited = t.getTableColumn();
				tableColEdited.setCellValueFactory((p)->{
					List<Object> x = p.getValue();
					if ( x != null )
					{
						Object obj = x.get( ifinal );
						tableColEdited.setStyle( "-fx-background-color: #ffffcc;" );
						return new SimpleObjectProperty<Object>( obj );
					}
					else
					{
						return new SimpleObjectProperty<Object>( "<NULL>" );
					}
				});
				*/
			});
			
			//tableCol.setSortable(false);
			this.setContextMenu(tableCol);
			
			final int index = i;
			tableCol.setCellValueFactory
			(
				(p)->
				{
					List<Object> x = p.getValue();
					if ( x != null )
					{
						Object obj = x.get( index );
						if ( obj instanceof Number )
						{
							//tableCol.setStyle( "-fx-text-alignment: CENTER-RIGHT;" );
							tableCol.setStyle( "-fx-alignment: CENTER-RIGHT;" );
						}
						return new SimpleObjectProperty<Object>( obj );
					}
					else
					{
						return new SimpleObjectProperty<Object>( "<NULL>" );
					}					
				}
			);
			tableCol.setCellFactory( p->new EditingCell() );
			this.tableView.getColumns().add(tableCol);
		}
		
		//this.tableView.getItems().addAll(this.dataLst);
		//this.tableView.setItems(this.dataLst);
		//this.tableView.getItems().addAll(this.obsDataLst);
		this.tableView.setItems(this.obsDataLst);
	}
	
	private void setContextMenu()
	{
		ContextMenu tblContextMenu = new ContextMenu();
		
		tblContextMenu.getItems().addAll
		(
			new MenuItem("tblA"),
			new MenuItem("tblB")
		);
		
		this.tableView.setContextMenu(tblContextMenu);
	}
	
	private void setContextMenu( TableColumn<List<Object>,Object> tblColumn )
	{
		ContextMenu tblColumnContextMenu = new ContextMenu();
		
		tblColumnContextMenu.getItems().addAll
		(
			new MenuItem( tblColumn.getText()+":A" ),
			new MenuItem( tblColumn.getText()+":B" )
		);
		
		tblColumn.setContextMenu(tblColumnContextMenu);
	}
	/*
	public void selectRecord( int id, MouseEvent event )
	{
		if ( event.isShiftDown() )
		{
			System.out.println( "RowId Add." + this.selectedRowLst.size() );
			this.selectedRowLst.add(id);
		}
		else
		{
			System.out.println( "RowId Clear." + this.selectedRowLst.size() );
			this.selectedRowLst.clear();
			this.selectedRowLst.add(id);
		}
		tableView.getSelectionModel().clearSelection();
		this.selectedRowLst.forEach( (rowId)->{System.out.println("rowId:"+rowId);tableView.getSelectionModel().select(rowId);} );
	}
	*/
	
	// It seems "EditingCell" is created by every column.
	class EditingCell extends TableCell<List<Object>, Object>
	{
		private int changedCnt = 0;
		private Object objBak  = null;
		
		private TextInputControl textInputCtrl = null;
		
		public EditingCell()
		{
			//TableColumn<List<Object>,Object> tableCol = this.getTableColumn();
		}
		/*
		public EditingCell( TableViewListObject app )
		{
			this.addEventFilter
			(
				MouseEvent.MOUSE_CLICKED, 
				(event)->
				{
					System.out.println( "Clicked." );
					int id = this.getTableRow().getIndex();
					app.selectRecord(id, event);
					event.consume();
				}
			);
		}
		*/
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
			
			setText(getItem().toString());
			setGraphic(null);
		}
		
		/**
		 * Callback for Cell
		 */
		@Override
		protected void updateItem( Object item, boolean empty )
		{
			super.updateItem(item, empty);
			
			if ( this.changedCnt == 0 && this.objBak == null )
			{
				this.objBak = item;
			}
			
			if ( item != null )
			{
				System.out.println( "updateItem:" + this.hashCode() + ":" + getString() + ":" + item.toString() );
			}
			else
			{
				System.out.println( "updateItem:" + this.hashCode() + ":" + getString() + ":NULL" );
			}
			
            if (empty) 
            {
                setText(null);
                setGraphic(null);
            } 
            else 
            {
                if (isEditing()) 
                {
                    if (textInputCtrl != null) 
                    {
                    	textInputCtrl.setText(getString());
                    }
                    setText(null);
                    setGraphic(textInputCtrl);
                   	setStyle(null);
                } 
                else 
                {
                	String str = getString();
                    setText(str);
                    setGraphic(null);
                    if ( changedCnt > 0 )
                    {
                    	setStyle("-fx-background-color: #66ff66;");
                    }
                    if ( objBak != null && objBak.equals(item) == false )
                    {
                    	System.out.println( "Changed..." );
                    	this.changedCnt++;
                    }
                }
            }
		}
		
        private void createTextField()
        {
        	String str = getString();
        	int rtnStr = this.getCharCount( str, "\n" );
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
	        				// Do not change the cell data, no matter what a user inputs
	                        commitEdit(textInputCtrl.getText());
	                    }
	                }
        		}
        	);
        }
        
        private String getString()
        {
            return getItem() == null ? "" : getItem().toString();
        }
        
    	public int getCharCount( String strSrc, String strChk )
    	{
    		if ( strSrc == null || strChk == null )
    		{
    			return 0;
    		}
    		else
    		{
    			return strSrc.length() - strSrc.replace( strChk, "" ).length();
    		}
    	}
    }	
}
