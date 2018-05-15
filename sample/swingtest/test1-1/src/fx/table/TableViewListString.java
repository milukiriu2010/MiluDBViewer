package fx.table;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class TableViewListString extends Application 
{
	private TableView<List<String>>  tableView = new TableView<>();
	private List<String>       headLst = Arrays.asList( "-", "First Name", "Last Name", "Email" );
	private List<List<String>> dataLst = new ArrayList<>();
	private ObservableList<List<String>> obsDataLst = null;
	
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
        stage.setWidth(450);
        stage.setHeight(500);
 
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
        
        this.tableView.setPrefWidth(300);
        this.tableView.setPrefHeight(200);
        this.tableView.getSelectionModel().setCellSelectionEnabled( true );
        
        this.setTableColumnWithData();
        this.setContextMenu();
        
        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(100);
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(100);
        addLastName.setPromptText("Last Name");
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(100);
        addEmail.setPromptText("Email");        
        
        final Button addButton = new Button("Add");
        addButton.setOnAction
        (
        	(event)->
        	{
        		List<String> dataRow = new ArrayList<>();
        		dataRow.add( String.valueOf(this.dataLst.size()+1) );
        		dataRow.add( addFirstName.getText() );
        		dataRow.add( addLastName.getText() );
        		dataRow.add( addEmail.getText() );
        		//this.dataLst.add(dataRow);
        		this.obsDataLst.add(dataRow);
        		
                addFirstName.clear();
                addLastName.clear();
                addEmail.clear();        		
        	}
        );
        
        this.hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton);
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
		List<String> dataRow1 = Arrays.asList("1","Michael","Brown","michael.brown@example.com");
		List<String> dataRow2 = Arrays.asList("2","Jacob","Smith","jacob.smith@example.com");
		List<String> dataRow3 = Arrays.asList("3","Isabella","Johnson","isabella.johnson@example.com");
		List<String> dataRow4 = Arrays.asList("4","Ethan","Williams","ethan.williams@example.com");
		List<String> dataRow5 = Arrays.asList("5","Emma","Jones","emma.jones@example.com");
		
		this.dataLst.add(dataRow1);
		this.dataLst.add(dataRow2);
		this.dataLst.add(dataRow3);
		this.dataLst.add(dataRow4);
		this.dataLst.add(dataRow5);
		
		this.obsDataLst = FXCollections.observableArrayList(this.dataLst);
	}

	private void setTableColumnWithData()
	{
		for ( int i = 0; i < this.headLst.size(); i++ )
		{
			String head = this.headLst.get(i);
			TableColumn<List<String>,String> tableCol = new TableColumn<List<String>,String>();
			Label label = new Label(head);
			label.setOnMouseClicked
			(
				(event)->
				{
					System.out.println("Label.clicked:"+head);
					this.tableView.getSelectionModel().selectRange
					(
						0,
						tableCol,
						this.obsDataLst.size()-1,
						tableCol
					);
				}
			);
			tableCol.setGraphic(label);
			tableCol.setSortable(false);
			this.setContextMenu(tableCol);
			
			final int index = i;
			tableCol.setCellValueFactory
			(
				(p)->
				{
					List<String> x = p.getValue();
					if ( x != null )
					{
						return new SimpleStringProperty( x.get( index ) );
					}
					else
					{
						return new SimpleStringProperty( "<NULL>" );
					}					
				}
			);
			
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
	
	private void setContextMenu( TableColumn<List<String>,String> tblColumn )
	{
		ContextMenu tblColumnContextMenu = new ContextMenu();
		
		tblColumnContextMenu.getItems().addAll
		(
			new MenuItem( tblColumn.getText()+":A" ),
			new MenuItem( tblColumn.getText()+":B" )
		);
		
		tblColumn.setContextMenu(tblColumnContextMenu);
	}
}
