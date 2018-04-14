package milu.gui.ctrl.common;

import java.io.File;
import java.sql.Driver;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;

public class DriverControlPane extends Pane 
{
	private MainController    mainCtrl = null;
	
	private PaneSwitchDriverInterface  psdInf = null;
	
	private ListView<String>  driverPathListView = new ListView<>();
	
	private Button  btnAddJar = new Button("Add Jars");
	
	private Button  btnDelJar = new Button("Remove Jar");
	
	private TextField driverClassNameTxt = new TextField();
	
	private Button  btnLoad   = new Button("Load");
	
	private Button  btnCancel = new Button("Cancel");

	public DriverControlPane( MainController mainCtrl, PaneSwitchDriverInterface psdInf )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		this.psdInf   = psdInf;
		
		Label lblDriverPath = new Label("JDBC Driver Path");
		VBox vBoxDriverPathBtn = new VBox(2);
		vBoxDriverPathBtn.getChildren().addAll( this.btnAddJar, this.btnDelJar );
		
		this.driverPathListView.setEditable(true);
		
		HBox  hBoxDriverPath = new HBox(2);
		hBoxDriverPath.getChildren().addAll( this.driverPathListView, vBoxDriverPathBtn );
		
		Label lblDriverClassName = new Label("JDBC Driver Class Name");
		
		HBox  hBoxNextBtn = new HBox(2);
		hBoxNextBtn.getChildren().addAll( this.btnLoad, this.btnCancel );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			lblDriverPath,
			hBoxDriverPath,
			lblDriverClassName,
			this.driverClassNameTxt,
			hBoxNextBtn
		);

		this.getChildren().add( vBox );
		
		this.setAction();
	}
	
	private void setAction()
	{
		
		this.driverPathListView.setCellFactory(	(callback)->new EditListCell() );
		
		
		this.btnAddJar.setOnAction
		(
			(event)->
			{
				FileChooser fc = new FileChooser();
				List<File> fileLst = fc.showOpenMultipleDialog(this.getScene().getWindow());
				if ( fileLst == null )
				{
					return;
				}
				fileLst.forEach( (file)->this.driverPathListView.getItems().add(file.getAbsolutePath()) );
			}
		);
		
		this.btnDelJar.setOnAction
		(
			(event)->
			{
				ObservableList<String>  selectedItems = this.driverPathListView.getSelectionModel().getSelectedItems();
				this.driverPathListView.getItems().removeAll( selectedItems );
			}
		);
		
		this.btnLoad.setOnAction
		(
			(event)->
			{
				DriverShim driver = null;
				try
				{
					driver = LoadDriver.loadDriver( this.driverClassNameTxt.getText(), this.driverPathListView.getItems() );
				}
				catch ( Exception ex )
				{
					this.showException(ex);
				}
				finally
				{
					this.driverPathListView.getItems().removeAll( this.driverPathListView.getItems() );
					this.driverClassNameTxt.setText("");
					this.psdInf.driverAdd(driver);
				}
			}
		);
		
		this.btnCancel.setOnAction( (event)->this.psdInf.driverCancel() );
	}
	
	private void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	// https://gist.github.com/skrb/6389257
	class EditListCell extends ListCell<String>
	{
		private TextField textField;
		
		@Override
		public void startEdit()
		{
			if (!isEditable() || !getListView().isEditable() )
			{
				return;
			}
			super.startEdit();
			
			if (isEditing())
			{
				if (textField == null)
				{
					textField = new TextField(getItem());
					textField.setOnAction(event->commitEdit(textField.getText()));
				}
			}
			// no else?
			//else
			//{
				textField.setText(getItem());
				setText(null);
				
				setGraphic(textField);
				textField.selectAll();
			//}
		}
		
		@Override
		public void updateItem( String item, boolean empty )
		{
			super.updateItem(item, empty);
			
			if (isEmpty())
			{
				setText(null);
				setGraphic(null);
			}
			else
			{
				if(!isEditing())
				{
					if ( textField != null )
					{
						setText(textField.getText());
					}
					else
					{
						setText( item );
					}
				}
				else
				{
					setText(item);
				}
				setGraphic(null);
			}
		}
	}
	
}
