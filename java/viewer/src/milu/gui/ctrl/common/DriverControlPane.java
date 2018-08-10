package milu.gui.ctrl.common;

import java.io.File;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.util.StringConverter;

import milu.db.driver.DriverClassConst;
import milu.db.driver.DriverNameConst;
import milu.db.driver.DriverInfo;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.file.json.MyJsonListAbstract;
import milu.file.json.MyJsonListFactory;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.main.AppConst;
import milu.main.AppConf;
import milu.main.MainController;
import milu.tool.MyFileTool;
import milu.tool.MyGUITool;

public class DriverControlPane extends Pane 
{
	private MainController    mainCtrl = null;
	
	private DriverShim        driverEdit = null;
	
	private PaneSwitchDriverInterface  psdInf = null;
	
	// ---------------------------------------------------------
	// Add & Delete "Driver Path"
	// ---------------------------------------------------------
	private ListView<String>  driverPathListView = new ListView<>();
	
	private Button  btnAddJar = new Button();
	
	private Button  btnDelJar = new Button();
	
	// ---------------------------------------------------------
	// Edit "Driver Class Name"
	// ---------------------------------------------------------
	private TextField driverClassNameTxt = new TextField();
	
	// ---------------------------------------------------------
	// ComboBox to choose "Driver Class Name"
	// ---------------------------------------------------------	
	private ComboBox<DriverNameConst>  driverClassNameCombo = new ComboBox<>();
	
	// ---------------------------------------------------------
	// Edit "Driver Template URL"
	// ---------------------------------------------------------
	private TextField driverTemplateUrlTxt = new TextField();
	
	// ---------------------------------------------------------
	// Edit "Driver Reference URL"
	// ---------------------------------------------------------
	private TextField driverReferenceUrlTxt = new TextField();
	
	// ---------------------------------------------------------
	// Load & Cancel
	// ---------------------------------------------------------
	private Button  btnLoad   = new Button();
	
	private Button  btnCancel = new Button();

	public DriverControlPane( MainController mainCtrl, PaneSwitchDriverInterface psdInf )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		this.psdInf   = psdInf;
		
		ResourceBundle  langRB  = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		ResourceBundle  langRBa = this.mainCtrl.getLangResource("conf.lang.gui.ctrl.common.DriverControlPane");
		
		// ---------------------------------------------------------
		// Add & Delete "Driver Path"
		// ---------------------------------------------------------
		Label lblDriverPath = new Label(langRBa.getString("LABEL_JDBC_DRIVER_PATH"));
		this.driverPathListView.setPrefWidth(500);
		this.driverPathListView.setPrefHeight(200);
		this.btnAddJar.setText(langRB.getString("BTN_ADD"));
		this.btnDelJar.setText(langRB.getString("BTN_DEL"));
		HBox hBoxDriverPathBtn = new HBox(2);
		hBoxDriverPathBtn.getChildren().addAll( this.btnAddJar, this.btnDelJar );
		
		this.driverPathListView.setEditable(true);
		
		// ---------------------------------------------------------
		// Edit "Driver Class Name"
		// ---------------------------------------------------------
		Label lblDriverClassName = new Label(langRBa.getString("LABEL_JDBC_DRIVER_CLASS_NAME"));
		this.driverClassNameTxt.setPromptText("Loaded by Class.forName");

		// ---------------------------------------------------------
		// ComboBox to choose "Driver Class Name"
		// ---------------------------------------------------------
		List<DriverNameConst> driverNameLst = 
				DriverShim.driverDBMap.values().stream()
					.sorted()
					.collect(Collectors.toList());
		ObservableList<DriverNameConst> driverNameObsLst = 
			FXCollections.observableArrayList( driverNameLst );
		this.driverClassNameCombo.setItems(driverNameObsLst);
		this.driverClassNameCombo.setConverter
		(
			new StringConverter<DriverNameConst>()
			{
				@Override
				public String toString( DriverNameConst obj )
				{
					if ( obj != null )
					{
						return obj.val();
					}
					else
					{
						return "";
					}
				}
				
				@Override
				public DriverNameConst fromString( String str )
				{
					return null;
				}
			}
		);
		
		HBox driverClassNameHBox = new HBox(2);
		driverClassNameHBox.getChildren().addAll( lblDriverClassName, this.driverClassNameCombo );
		
		// ---------------------------------------------------------
		// Edit "Driver Template URL"
		// ---------------------------------------------------------
		Label lblDriverTemplateUrl = new Label(langRBa.getString("LABEL_JDBC_URL_TEMPLATE"));
		this.driverTemplateUrlTxt.setPromptText("jdbc:driver://host:port/dbname");
		
		// ---------------------------------------------------------
		// Edit "Driver Reference URL"
		// ---------------------------------------------------------
		Label lblDriverReferenceUrl = new Label(langRBa.getString("LABEL_JDBC_URL_REFERENCE"));
		this.driverReferenceUrlTxt.setPromptText("Home Page for JDBC");
		
		// ---------------------------------------------------------
		// Load & Cancel
		// ---------------------------------------------------------
		this.btnLoad.setText(langRB.getString("BTN_LOAD"));
		this.btnCancel.setText(langRB.getString("BTN_CANCEL"));
		HBox  hBoxNextBtn = new HBox(2);
		hBoxNextBtn.getChildren().addAll( this.btnLoad, this.btnCancel );
		
		// ---------------------------------------------------------
		// Put all nodes on Pane
		// ---------------------------------------------------------
		VBox vBox = new VBox(2);
		vBox.setPadding( new Insets(10,10,10,10) );
		vBox.getChildren().addAll
		( 
			lblDriverPath,
			this.driverPathListView,
			hBoxDriverPathBtn,
			driverClassNameHBox,
			this.driverClassNameTxt,
			lblDriverTemplateUrl,
			this.driverTemplateUrlTxt,
			lblDriverReferenceUrl,
			this.driverReferenceUrlTxt,
			hBoxNextBtn
		);

		this.getChildren().add( vBox );
		
		this.setAction();
	}
	
	public void setAddDriver()
	{
		this.driverEdit = null;
		
		this.driverClassNameTxt.setText("");
		//this.driverClassNameTxt.setDisable(false);
		this.driverClassNameTxt.setEditable(true);
		this.driverClassNameCombo.setVisible(true);
		
		this.driverTemplateUrlTxt.setText("");
		this.driverReferenceUrlTxt.setText("");
		
		this.driverPathListView.getItems().removeAll(this.driverPathListView.getItems());
	}
	
	public void setEditDriver( DriverShim driverEdit )
	{
		this.driverEdit = driverEdit;
		
		this.driverClassNameTxt.setText( this.driverEdit.getDriverClassName() );
		//this.driverClassNameTxt.setDisable(true);
		this.driverClassNameTxt.setEditable(false);
		this.driverClassNameCombo.setVisible(false);

		this.driverTemplateUrlTxt.setText( this.driverEdit.getTemplateUrl() );
		this.driverReferenceUrlTxt.setText( this.driverEdit.getReferenceUrl() );
		
		this.driverPathListView.getItems().removeAll(this.driverPathListView.getItems());
		this.driverPathListView.getItems().addAll( this.driverEdit.getDriverPathLst() );
	}
	
	private void setAction()
	{		
		AppConf appConf = this.mainCtrl.getAppConf();
		
		this.driverPathListView.setCellFactory(	(callback)->new EditListCell() );
		
		this.btnAddJar.setOnAction((event)->{
			List<File> fileLst = MyGUITool.fileOpenMultiDialog( appConf.getInitDirJDBC(), null, this );
			if ( fileLst == null )
			{
				return;
			}
			fileLst.forEach
			((file)->{
				this.driverPathListView.getItems().add(file.getAbsolutePath());
				appConf.setInitDirJDBC(file.getParent());
			});
			
			// save AppConf to "the default configuration file".
			MyFileTool.save(mainCtrl, appConf);
		});
		
		this.btnDelJar.setOnAction((event)->{
			ObservableList<String>  selectedItems = this.driverPathListView.getSelectionModel().getSelectedItems();
			this.driverPathListView.getItems().removeAll( selectedItems );
		});
		
		URL url = getClass().getResource( "/conf/json/driver.json" );
		MyJsonListAbstract<DriverInfo> myJsonLst = MyJsonListFactory.<DriverInfo>getInstance(MyJsonListFactory.factoryType.DRIVER_INFO);
		List<DriverInfo> driverInfoLst = null;
		try
		{
			driverInfoLst = myJsonLst.load(url);
			/*
			System.out.println( "=== MyJsonLst =========================================" );
			driverInfoLst.forEach((driverInfo)->{
				System.out.println( "ClassName   :" + driverInfo.getDriverClassName() );
				System.out.println( "TemplateURL :" + driverInfo.getTemplateUrl() );
				System.out.println( "ReferenceURL:" + driverInfo.getReferenceUrl() );
				System.out.println( "***********************************************" );
			});
			System.out.println( "=======================================================" );
			*/
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			driverInfoLst = new ArrayList<>();
		}
		
		// switch key<=>val
		Map<DriverNameConst, DriverClassConst> inverseDriverMap =
				DriverShim.driverDBMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		// -------------------------------------------
		// set DriverClassName by DriverName
		// -------------------------------------------
		List<DriverInfo> driverInfoLstFinal = driverInfoLst; 
		this.driverClassNameCombo.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
			String driverClassName = inverseDriverMap.get(newVal).val();
			this.driverClassNameTxt.setText(driverClassName);
			
			DriverInfo driverInfo = driverInfoLstFinal.stream()
					.filter( di->di.getDriverClassName().equals(driverClassName) )
					.findFirst()
					.orElse(null);
			if ( driverInfo == null )
			{
				this.driverTemplateUrlTxt.setText("");
				this.driverReferenceUrlTxt.setText("");
			}
			else
			{
				this.driverTemplateUrlTxt.setText(driverInfo.getTemplateUrl());
				this.driverReferenceUrlTxt.setText(driverInfo.getReferenceUrl());
			}
		});
		
		this.btnLoad.setOnAction((event)->{
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
					MyGUITool.showMsg( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_NOT_ALLOWED_DUPLICATE" );
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
					MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", sqlEx );
				}
			}
			
			// -----------------------------------------------
			// add driver
			// -----------------------------------------------
			DriverShim driver = null;
			try
			{
				driver = LoadDriver.loadDriver( this.driverClassNameTxt.getText(), this.driverPathListView.getItems() );
				driver.setTemplateUrl(this.driverTemplateUrlTxt.getText());
				driver.setReferenceUrl(this.driverReferenceUrlTxt.getText());
				MyJsonEachAbstract<DriverShim> myJsonAbs =
						MyJsonEachFactory.<DriverShim>getInstance(MyJsonEachFactory.factoryType.DRIVER_SHIM);
				myJsonAbs.save(new File(AppConst.DRIVER_DIR.val()+driver.getDriverClassName()+".json"),driver);

				if ( this.driverEdit != null )
				{
					this.psdInf.driverEdit(driver);
				}
				else
				{
					this.psdInf.driverAdd(driver);
				}
			}
			catch ( Exception ex )
			{
				MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
			}
			/*
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
			*/
		});
		
		this.btnCancel.setOnAction( (event)->this.psdInf.driverCancel() );
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
