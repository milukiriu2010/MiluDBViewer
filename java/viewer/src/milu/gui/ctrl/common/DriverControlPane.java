package milu.gui.ctrl.common;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.file.json.MyJsonHandleAbstract;
import milu.file.json.MyJsonHandleFactory;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.gui.dlg.MyAlertDialog;
import milu.main.AppConst;
import milu.main.MainController;

public class DriverControlPane extends Pane 
{
	private MainController    mainCtrl = null;
	
	private DriverShim        driverEdit = null;
	
	private PaneSwitchDriverInterface  psdInf = null;
	
	private ListView<String>  driverPathListView = new ListView<>();
	
	private Button  btnAddJar = new Button();
	
	private Button  btnDelJar = new Button();
	
	private TextField driverClassNameTxt = new TextField();
	
	private Button  btnLoad   = new Button();
	
	private Button  btnCancel = new Button();

	public DriverControlPane( MainController mainCtrl, PaneSwitchDriverInterface psdInf )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		this.psdInf   = psdInf;
		
		ResourceBundle  langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		Label lblDriverPath = new Label("JDBC Driver Path(.jar)");
		this.driverPathListView.setPrefWidth(500);
		this.driverPathListView.setPrefHeight(200);
		this.btnAddJar.setText(langRB.getString("BTN_ADD"));
		this.btnDelJar.setText(langRB.getString("BTN_DEL"));
		HBox hBoxDriverPathBtn = new HBox(2);
		hBoxDriverPathBtn.getChildren().addAll( this.btnAddJar, this.btnDelJar );
		
		this.driverPathListView.setEditable(true);
		
		Label lblDriverClassName = new Label("JDBC Driver Class Name");
		this.driverClassNameTxt.setPromptText("Class.forName");
		
		this.btnLoad.setText(langRB.getString("BTN_LOAD"));
		this.btnCancel.setText(langRB.getString("BTN_CANCEL"));
		HBox  hBoxNextBtn = new HBox(2);
		hBoxNextBtn.getChildren().addAll( this.btnLoad, this.btnCancel );
		
		VBox vBox = new VBox(2);
		vBox.setPadding( new Insets(10,10,10,10) );
		vBox.getChildren().addAll
		( 
			lblDriverPath,
			this.driverPathListView,
			hBoxDriverPathBtn,
			lblDriverClassName,
			this.driverClassNameTxt,
			hBoxNextBtn
		);

		this.getChildren().add( vBox );
		
		this.setAction();
	}
	
	public void setAddDriver()
	{
		this.driverEdit = null;
		
		this.driverClassNameTxt.setText("");
		this.driverClassNameTxt.setDisable(false);
		
		this.driverPathListView.getItems().removeAll(this.driverPathListView.getItems());
	}
	
	public void setEditDriver( DriverShim driverEdit )
	{
		this.driverEdit = driverEdit;
		
		this.driverClassNameTxt.setText( this.driverEdit.getDriverClazzName() );
		this.driverClassNameTxt.setDisable(true);
		
		this.driverPathListView.getItems().removeAll(this.driverPathListView.getItems());
		this.driverPathListView.getItems().addAll( this.driverEdit.getDriverPathLst() );
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
				// -------------------------------------------------
				// At first, check already registered to add driver
				// -------------------------------------------------
				if ( this.driverEdit == null )
				{
					/*
					Driver driver = 
						DriverManager.drivers()
							.filter( DriverShim.class::isInstance )
							.map( DriverShim.class::cast )
							.filter( d -> d.getDriverClazzName().contains(this.driverClassNameTxt.getText()) )
							.findAny()
							.orElse(null);
					*/
					
					// show alert, if already driver is loaded.
					//if ( driver != null )
					if (LoadDriver.isAlreadyLoadCheck(this.driverClassNameTxt.getText()))
					{
						MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
						ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
						alertDlg.setHeaderText( langRB.getString("TITLE_NOT_ALLOWED_DUPLICATE") );
						alertDlg.showAndWait();
						alertDlg = null;
						return;
					}
				}
				
				// -------------------------------------------------
				// At fist, delete driver to edit driver
				// -------------------------------------------------
				if ( this.driverEdit != null )
				{
					try
					{
						DriverManager.deregisterDriver(this.driverEdit);
					}
					catch ( SQLException sqlEx )
					{
						this.showException( sqlEx );
					}
				}
				
				// -----------------------------------------------
				// add driver
				// -----------------------------------------------
				DriverShim driver = null;
				try
				{
					driver = LoadDriver.loadDriver( this.driverClassNameTxt.getText(), this.driverPathListView.getItems() );
					MyJsonHandleAbstract myJsonAbs =
						new MyJsonHandleFactory().createInstance(DriverShim.class);
					myJsonAbs.open(AppConst.DRIVER_DIR.val()+driver.getDriverClassName()+".json");
					myJsonAbs.save(driver);
				}
				catch ( Exception ex )
				{
					this.showException(ex);
				}
				finally
				{
					if ( this.driverEdit != null )
					{
						this.psdInf.driverEdit(driver);
					}
					else
					{
						this.psdInf.driverAdd(driver);
					}
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
