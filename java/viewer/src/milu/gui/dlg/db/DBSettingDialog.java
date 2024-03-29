package milu.gui.dlg.db;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Collections;
import java.util.Enumeration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import javafx.beans.value.ChangeListener;

import milu.db.driver.DriverShim;
import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.db.MyDBAbstract;
import milu.db.MyDBFactory;
import milu.gui.ctrl.common.ButtonOrderNoneDialogPane;
import milu.gui.ctrl.common.DriverControlPane;
import milu.gui.ctrl.common.PathTreeView;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.gui.dlg.MyAlertDialog;
import milu.gui.ctrl.common.inf.ActionInterface;
import milu.gui.ctrl.common.inf.ChangePathInterface;
import milu.main.AppConst;
import milu.main.MainController;
import milu.tool.MyGUITool;

// Dialog sample
// http://code.makery.ch/blog/javafx-dialogs-official/
public class DBSettingDialog extends Dialog<MyDBAbstract>
	implements
		PaneSwitchDriverInterface,
		ActionInterface,
		ChangePathInterface
{
	private MainController mainCtrl = null;
	
	private ChangeListener<? super MyDBAbstract>  changeListener = null;
	
	// ----------------------------------------
	// [Pane on Dialog(1)]
	// ----------------------------------------
	// pane for Dialog
	private BorderPane brdPane = new BorderPane();
	
	// ----------------------------------------
	// [Pane on Dialog(2)]
	// ----------------------------------------
	// Pane for Driver
	private Pane  driverCtrlPane = null; 
	
	// ----------------------------------------
	// [Pane on Dialog(1)]-[Center]
	// ----------------------------------------
	private PathTreeView  pathTreeView = new PathTreeView();
	
	private Button  btnNewFolder     = new Button();
	
	private Button  btnNewConnection = new Button();
	
	private Button  btnEditFolder    = new Button();
	
	private Button  btnDelFolder     = new Button();
	
	// ----------------------------------------
	// [Pane on Dialog(1)]-[Right]
	// ----------------------------------------
	// DB Type List
	private ObservableList<MyDBAbstract>  dbTypeLst = null;
	
	// ComboBox for DB type(Oracle/MySQL/PostgreSQL...)
	private ComboBox<MyDBAbstract>  comboBoxDBType = new ComboBox<MyDBAbstract>();
	
	// field for user
	private TextField usernameTextField = new TextField();
	
	// field for password
	private PasswordField passwordTextField = new PasswordField();
	
	// VBox for DB Connection Nodes
	private VBox       vBoxConnection    = new VBox(2);
	
	// UrlPaneAbstract Map
	private Map<MyDBAbstract,UrlPaneAbstract>  urlPaneAbsMap = new HashMap<>();
	
	// ----------------------------------------
	// [Pane on Dialog(1)]-[Top]
	// ----------------------------------------
	
	// Button to add "New Driver"
	private Button    btnAddDriver  = new Button();
	
	// Button to add "Edit Driver"
	private Button    btnEditDriver = new Button();
	
	// ----------------------------------------
	// [Button on Dialog]
	// ----------------------------------------
	
	// Button "OK"
	ButtonType okButtonType = null;
	
	public DBSettingDialog( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
		
		// set DialogPane which has no button order.
		this.setDialogPane( new ButtonOrderNoneDialogPane() );
		
		// Set dialog title.
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		ResourceBundle cmnLangRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		this.setTitle( langRB.getString( "TITLE_DB_SETTING" ) );
		
		// ----------------------------------------
		// [Pane on Dialog(1)]-[Center]
		// ----------------------------------------
		// create DB setting folder, if not exist.
		File rootDir = new File( AppConst.DB_DIR.val() );
		if ( rootDir.exists() == false )
		{
			rootDir.mkdirs();
		}
		this.pathTreeView.setMainController(mainCtrl);
		this.pathTreeView.setChangePathInterface(this);
		this.pathTreeView.setRootDir(AppConst.DB_DIR.val());
		this.pathTreeView.setFileExt("json");
		try
		{
			this.pathTreeView.init();
			// Add Listener
			this.pathTreeView.registActionInf(this);
		}
		catch( IOException ioEx )
		{
			ioEx.printStackTrace();
		}
		
		this.btnNewFolder.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage( "file:resources/images/folder_new.png" ) ) );
		Tooltip tipNewFolder = new Tooltip(cmnLangRB.getString( "TOOLTIP_NEW_FOLDER" ));
		tipNewFolder.getStyleClass().add("Common_MyToolTip");
		this.btnNewFolder.setTooltip( tipNewFolder );
		
		this.btnNewConnection.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage( "file:resources/images/file_new.png" ) ) );
		Tooltip tipNewConnection = new Tooltip(cmnLangRB.getString( "TOOLTIP_NEW_CONNECTION" ));
		tipNewConnection.getStyleClass().add("Common_MyToolTip");
		this.btnNewConnection.setTooltip( tipNewConnection );
		
		this.btnEditFolder.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage( "file:resources/images/edit.png" ) ) );
		Tooltip tipEditFolder = new Tooltip(cmnLangRB.getString( "TOOLTIP_EDIT" ));
		tipEditFolder.getStyleClass().add("Common_MyToolTip");
		this.btnEditFolder.setTooltip( tipEditFolder );
		
		this.btnDelFolder.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage( "file:resources/images/delete.png" ) ) );
		Tooltip tipDelFolder = new Tooltip(cmnLangRB.getString( "TOOLTIP_DEL" ));
		tipDelFolder.getStyleClass().add("Common_MyToolTip");
		this.btnDelFolder.setTooltip( tipDelFolder );
		
		HBox hBoxBtn = new HBox(2);
		hBoxBtn.getChildren().addAll( this.btnNewFolder, this.btnNewConnection, this.btnEditFolder, this.btnDelFolder );
		
		VBox vBoxPathTreeView = new VBox(2);
		vBoxPathTreeView.getChildren().addAll( hBoxBtn, this.pathTreeView );
		
		// ----------------------------------------
		// [Pane on Dialog(1)]-[Right]
		// ----------------------------------------
		// ComboBox for DB type(Oracle/MySQL/PostgreSQL...)
		List<MyDBAbstract> myDBAbsLst = new ArrayList<>();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			myDBAbsLst.add( MyDBFactory.getInstance( drivers.nextElement() ) );
		}
		Collections.sort( myDBAbsLst );
		this.dbTypeLst = FXCollections.observableArrayList( myDBAbsLst );
		this.comboBoxDBType.setItems( this.dbTypeLst );
		this.comboBoxDBType.setConverter
		(
			new StringConverter<MyDBAbstract>()
			{
				@Override
				public String toString(MyDBAbstract myDBAbs)
				{
					if ( myDBAbs == null )
					{
						return "";
					}
					else
					{
						DriverShim driverShim = myDBAbs.getDriveShim();
						if ( driverShim == null )
						{
							return "";
						}
						else
						{
							return driverShim.getDBName();
						}
					}
				}
				
				@Override
				public MyDBAbstract fromString( String str )
				{
					return null;
				}
			}
		);
		
		// set all objects on pane.
		GridPane paneDBOpt = new GridPane();
		paneDBOpt.setHgap( 5 );
		paneDBOpt.setVgap( 2 );
		paneDBOpt.setPadding( new Insets( 10, 10, 10, 10 ) );
		paneDBOpt.add( new Label( langRB.getString( "LABEL_DB_TYPE" )) , 0, 1 );
		paneDBOpt.add( this.comboBoxDBType   , 1, 1 );
		paneDBOpt.add( new Label( langRB.getString( "LABEL_USERNAME" )), 0, 2 );
		paneDBOpt.add( this.usernameTextField, 1, 2 );
		paneDBOpt.add( new Label( langRB.getString( "LABEL_PASSWORD" )), 0, 3 );
		paneDBOpt.add( this.passwordTextField, 1, 3 );

		this.vBoxConnection.getChildren().add( paneDBOpt );
		
		// -------------------------------------
		// Top on BorderPane
		// -------------------------------------
		this.btnAddDriver.setText( langRB.getString("BTN_ADD_DRIVER") );
		this.btnEditDriver.setText( langRB.getString("BTN_EDIT_DRIVER") );
		HBox hBoxTop = new HBox(2);
		hBoxTop.setSpacing(10);
		hBoxTop.getChildren().addAll( this.btnAddDriver, this.btnEditDriver );
		
		// pane for Dialog
		BorderPane.setMargin(vBoxPathTreeView,new Insets(10,10,10,10));
		this.brdPane.setCenter(vBoxPathTreeView);
		this.brdPane.setRight( this.vBoxConnection );
		this.brdPane.setTop( hBoxTop );
		
		// set pane on dialog
		this.getDialogPane().setContent( this.brdPane );
		
		// add button connect and cancel
		this.okButtonType            = new ButtonType( langRB.getString( "BTN_OK" )    , ButtonData.OK_DONE );
		ButtonType cancelButtonType  = new ButtonType( cmnLangRB.getString( "BTN_CANCEL" ), ButtonData.CANCEL_CLOSE );
		//this.getDialogPane().getButtonTypes().addAll( this.okButtonType, ButtonType.CANCEL );
		this.getDialogPane().getButtonTypes().addAll( this.okButtonType, cancelButtonType );

		// Set default selection on ComboBox(DB Type)
		this.comboBoxDBType.getSelectionModel().selectFirst();
		MyDBAbstract selectedMyDBAbs = this.comboBoxDBType.getSelectionModel().getSelectedItem();
		AbsPaneFactory paneFactory = new UrlPaneFactory();
		UrlPaneAbstract urlPaneAbs = paneFactory.createPane( this, this.mainCtrl, selectedMyDBAbs );
		this.urlPaneAbsMap.put( selectedMyDBAbs, urlPaneAbs );
		this.vBoxConnection.getChildren().add( urlPaneAbs );
		
		// Create Pane for DriverControl
		this.driverCtrlPane = new DriverControlPane( this.mainCtrl, this );

		// -------------------------------------------------------------------------------------------
		// call this listener when selected "JDBC" is changed.
		// -------------------------------------------------------------------------------------------
		//	new ChangeListener<String>()
		//	
		//	@Override 
		//	public void changed( ObservableValue<? extends MyDBAbstract> ov, MyDBAbstract oldVal, MyDBAbstract newVal )
		// -------------------------------------------------------------------------------------------
		this.changeListener =
			( ov, oldVal, newVal )->
			{
				this.setUrlPane(newVal);
			};
		
		
		// Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );
		
		// set CSS for this dialog
		Scene scene = this.getDialogPane().getScene();
		// load css on DBSettingDialog elements
		String [] cssLst =
			{
				"/conf/css/ctrl/common/Common.css",
				"/conf/css/dlg/DBSettingDialog.css"
			};
        for ( String css : cssLst )
        {
    		scene.getStylesheets().add(	getClass().getResource(css).toExternalForm() );
        }		
		
		// set Action
		this.setAction();
		
		// set focus on ComboBox for DBType
		// http://krr.blog.shinobi.jp/javafx/javafx%20ui%E3%82%B3%E3%83%B3%E3%83%88%E3%83%AD%E3%83%BC%E3%83%AB%E3%81%AE%E9%81%B8%E6%8A%9E%E3%83%BB%E3%83%95%E3%82%A9%E3%83%BC%E3%82%AB%E3%82%B9
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		Platform.runLater( this.pathTreeView::requestFocus );
		
		// set location
		Platform.runLater( ()->MyGUITool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );
		
		// set size
		this.setResizable( true );
	}
	
	private void setAction()
	{
		ResourceBundle cmnLangRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// ----------------------------------------
		// [Pane on Dialog(1)]-[Left]
		// ----------------------------------------
		this.btnNewFolder.addEventHandler
		( 
			ActionEvent.ACTION,
			(event)->
			{
				try
				{
					this.pathTreeView.addNewFolder();
					//event.consume();
				}
				catch ( IOException ioEx )
				{
					//this.showException(ioEx);
					MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
				}
			}
		);
		
		this.btnNewConnection.addEventHandler
		( 
			ActionEvent.ACTION,
			(event)->
			{
				try
				{
					this.pathTreeView.addNewFile();
				}
				catch ( IOException ioEx )
				{
					MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
				}
			}
		);
		
		this.btnEditFolder.addEventHandler
		( 
			ActionEvent.ACTION,
			(event)->{ this.pathTreeView.editItem(); }
		);

		this.btnDelFolder.setOnAction((event)->{
			MyAlertDialog myAlertDlg = 
				new MyAlertDialog
				( 
					Alert.AlertType.CONFIRMATION, 
					this.mainCtrl,
					new ButtonType( cmnLangRB.getString("BTN_YES"), ButtonData.YES ),
					new ButtonType( cmnLangRB.getString("BTN_NO") , ButtonData.NO )
				);
			myAlertDlg.setTitle(cmnLangRB.getString("TITLE_DELETE"));
			myAlertDlg.setTxtMsg(cmnLangRB.getString("MSG_DELETE"));
			myAlertDlg.showAndWait().ifPresent((type)->{
				if ( ButtonType.YES.getButtonData().equals(type.getButtonData()) )
				{
					try
					{
						this.pathTreeView.delFolder();
					}
					catch ( IOException ioEx )
					{
						MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
					}
				}
			});
		});
		
		// ----------------------------------------
		// [Pane on Dialog(1)]-[Center]
		// ----------------------------------------
		// Change pane when DB Type is changed.
		// http://www.java2s.com/Code/Java/JavaFX/AddchangelistenertoComboBoxvalueProperty.htm
		/*
		this.comboBoxDBType.valueProperty().addListener
		(
			// -------------------------------------------------------------------------------------------
			//	new ChangeListener<String>()
			//	
			//	@Override 
			//	public void changed( ObservableValue<? extends String> ov, String oldVal, String newVal )
			// -------------------------------------------------------------------------------------------
			( ov, oldVal, newVal )->
			{
				this.setUrlPane(newVal);
			}
		);
		*/
		this.comboBoxDBType.valueProperty().addListener( this.changeListener );
		
		
		// --------------------------------------------
		// "OK" Button Event
		// https://stackoverflow.com/questions/38696053/prevent-javafx-dialog-from-closing
		// https://stackoverflow.com/questions/27523119/keep-showing-a-dialog-when-validation-of-input-fails/32287080
		// --------------------------------------------
		final Button okButton = (Button)this.getDialogPane().lookupButton( this.okButtonType );
		okButton.addEventFilter(
		    ActionEvent.ACTION, 
		    event->{ this.setActionBtnOK( event ); }
		);
		
		// --------------------------------------------
		// result when clicking on "Connect"
		// return selected item
		// --------------------------------------------
		this.setResultConverter
		( 
			dialogButton ->
			{
				if ( dialogButton == this.okButtonType )
				{
					// selected item
					MyDBAbstract myDBAbs = this.comboBoxDBType.getValue();
					
					// close all connections except selected item.
					ObservableList<MyDBAbstract>  objList = this.comboBoxDBType.getItems();
					for ( MyDBAbstract obj : objList )
					{
						try
						{
							if ( obj != myDBAbs )
							{
								obj.close();
							}
						}
						catch ( SQLException sqlEx )
						{
							// suppress error
						}
					}
					
					// get selected item on PathTreeView
					TreeItem<Path>  treeItem = this.pathTreeView.getSelectionModel().getSelectedItem();
					if ( treeItem != null )
					{
						Path path = treeItem.getValue();
						if ( Files.isRegularFile(path) )
						{
							MyJsonEachAbstract<MyDBAbstract> myJsonAbs =
									MyJsonEachFactory.<MyDBAbstract>getInstance(MyJsonEachFactory.factoryType.MY_DB_ABS);
							try
							{
								myDBAbs.setPasswordEnc(this.mainCtrl.getSecretKey());
								myJsonAbs.save(new File(path.toString()),myDBAbs);
							}
							catch ( IOException ioEx )
							{
								MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
							}
							catch ( Exception ex )
							{
								MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
							}
						}
					}
					
					
					return myDBAbs;
				}
				return null;
			}
		);
		
		// ------------------------------
		// Add JDBC Driver 
		// ------------------------------
		this.btnAddDriver.setOnAction
		( 
			(event)->
			{ 
				// set pane on dialog
				this.getDialogPane().setContent( this.driverCtrlPane );
				((DriverControlPane)this.driverCtrlPane).setAddDriver();
				this.setDisableAllButton(true);
				// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
				Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
			} 
		);
		
		// ------------------------------
		// Edit JDBC Driver 
		// ------------------------------
		this.btnEditDriver.setOnAction
		( 
			(event)->
			{ 
				// set pane on dialog
				this.getDialogPane().setContent( this.driverCtrlPane );
				MyDBAbstract selectedMyDBAbs = this.comboBoxDBType.getSelectionModel().getSelectedItem();
				DriverShim driverEdit = selectedMyDBAbs.getDriveShim();
				((DriverControlPane)this.driverCtrlPane).setEditDriver( driverEdit );
				this.setDisableAllButton(true);
				// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
				Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
			} 
		);
		
	}
	
	private void setUrlPane( MyDBAbstract newVal )
	{
		System.out.println( "@@@@@@@@@@ setUrlPane @@@@@@@@@" );
		System.out.println( "newVal1:" + newVal );
		System.out.println( "url1:" + newVal.getUrl() + "|" );
		ListIterator<Node> nodeLstIterator = this.vBoxConnection.getChildren().listIterator();
		while ( nodeLstIterator.hasNext() )
		{
			Node node = nodeLstIterator.next();
			if ( node instanceof UrlPaneAbstract )
			{
				this.vBoxConnection.getChildren().remove( node );
				
				UrlPaneAbstract urlPaneAbs2 = null;
				// ReUse object, if selected before.
				if ( this.urlPaneAbsMap.containsKey(newVal) )
				{
					urlPaneAbs2 = this.urlPaneAbsMap.get(newVal); 
				}
				// Create object, if never selected before.
				else
				{
					AbsPaneFactory paneFactory = new UrlPaneFactory();
					urlPaneAbs2 = paneFactory.createPane( this, this.mainCtrl, newVal );
					this.urlPaneAbsMap.put( newVal, urlPaneAbs2 );
				}
				System.out.println( "newVal2:" + newVal );
				System.out.println( "url2:" + newVal.getUrl() + "|" );
				urlPaneAbs2.init();
				this.vBoxConnection.getChildren().add( urlPaneAbs2 );
				break;
			}
		}
		
		// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}
	
	// "OK" Button Event
	private void setActionBtnOK( ActionEvent event )
	{
		ListIterator<Node> nodeLstIterator = this.vBoxConnection.getChildren().listIterator();
		while ( nodeLstIterator.hasNext() )
		{
			Node node = nodeLstIterator.next();
			if ( node instanceof UrlPaneAbstract )
			{
				UrlPaneAbstract urlPaneAbs1 = (UrlPaneAbstract)node;
				urlPaneAbs1.setUrl(MyDBAbstract.UPDATE.WITH);
			}
		}
		
		MyDBAbstract myDBAbs = null;
		try
		{
			myDBAbs = this.comboBoxDBType.getValue();
			myDBAbs.setUsername( this.usernameTextField.getText() );
			myDBAbs.setPassword( this.passwordTextField.getText() );
			myDBAbs.setAppConf(this.mainCtrl.getAppConf());
			
			// Connect to DB
			myDBAbs.connect();
		}
		catch ( SQLException sqlEx )
		{
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_DB_CONNECT_ERROR", sqlEx );
		}
		catch ( Exception ex )
		{
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		
		// If DB connection is failed, this dialog keeps to open.
		if ( myDBAbs.isConnected() == false ) 
		{
			// The conditions are not fulfilled so we consume the event
			// to prevent the dialog to close
			event.consume();
		}
	}
	
	private void setDisableAllButton( boolean disable )
	{
		ObservableList<ButtonType>  btnTypeLst = this.getDialogPane().getButtonTypes();
		btnTypeLst.forEach
		(
			(btnType)->
			{
				Button btn = (Button)this.getDialogPane().lookupButton(btnType);
				btn.setDisable(disable);
			}
		);
	}
	
	@Override
	public void driverAdd( DriverShim driver )
	{
		// set pane on dialog
		this.getDialogPane().setContent( this.brdPane );
		this.setDisableAllButton(false);
		
		MyDBAbstract myDBAbs = MyDBFactory.getInstance( driver );
		this.dbTypeLst.add( myDBAbs );
		this.comboBoxDBType.getSelectionModel().select(myDBAbs);
		
		// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}
	
	@Override
	public void driverEdit( DriverShim driver )
	{
		// set pane on dialog
		this.getDialogPane().setContent( this.brdPane );
		this.setDisableAllButton(false);
		
		MyDBAbstract myDBAbs = this.comboBoxDBType.getSelectionModel().getSelectedItem();
		myDBAbs.setDriverShim(driver);
		this.comboBoxDBType.getSelectionModel().select(myDBAbs);
		
		// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}
	
	@Override
	public void driverCancel()
	{
		// set pane on dialog
		this.getDialogPane().setContent( this.brdPane );
		this.setDisableAllButton(false);
		
		// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}
	
	// Call when pressed "Enter Key" on PathTreeView
	// ActionInterface
	@Override
	public void setAction(Object obj) {
		// Connect to DB
		//this.setActionBtnOK( null );
		final Button okButton = (Button)this.getDialogPane().lookupButton( this.okButtonType );
		okButton.fire();
	}

	
	// ChangePathInterface
	@Override
	public void changePath( Path path )
	{
		System.out.println( "changePath[" + path.toString() + "]" );
		System.out.println( "AppCons.DB_DIR[" + AppConst.DB_DIR.val() + "]" );
		// "Bookmark Folder" is selected
		if ( AppConst.DB_DIR.val().equals(path.toString()+File.separator) )
		{
			this.btnNewFolder.setDisable(false);
			this.btnNewConnection.setDisable(false);
			this.btnEditFolder.setDisable(true);
			this.btnDelFolder.setDisable(true);
			return;
		}
		// "Folder" is selected.
		else if ( Files.isRegularFile(path) == false )
		{
			this.btnNewFolder.setDisable(false);
			this.btnNewConnection.setDisable(false);
			this.btnEditFolder.setDisable(false);
			this.btnDelFolder.setDisable(false);
			return;
		}
		// "File" is selected.
		else
		{
			this.btnNewFolder.setDisable(true);
			this.btnNewConnection.setDisable(true);
			this.btnEditFolder.setDisable(false);
			this.btnDelFolder.setDisable(false);
		}
		
		try
		{
			// create "MyDBAbstract" from Json
			MyJsonEachAbstract<MyDBAbstract> myJsonAbs =
				MyJsonEachFactory.<MyDBAbstract>getInstance(MyJsonEachFactory.factoryType.MY_DB_ABS);
			MyDBAbstract myDBAbsTmp = myJsonAbs.load(new File(path.toString()));
			if ( myDBAbsTmp == null )
			{
				return;
			}
			
			try
			{
				myDBAbsTmp.setPassword(this.mainCtrl.getSecretKey());
			}
			catch ( NoSuchAlgorithmException 			| 
					NoSuchPaddingException   			|
					InvalidKeyException      			|
					InvalidAlgorithmParameterException  |
					BadPaddingException                 |
					IllegalBlockSizeException  decryptEx )
			{
				MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_PASSWORD_DECRYPT_ERROR", decryptEx );
			}
			
			System.out.println( "Class[" + myDBAbsTmp.getClass().toString() + "]" );
			System.out.println( "User [" + myDBAbsTmp.getUsername() + "]" );
			System.out.println( "URL  [" + myDBAbsTmp.getUrl() + "]" );
			System.out.println( "JDBC [" + myDBAbsTmp.getDriveShim().getDriverClassName() + "]" );
			myDBAbsTmp.getDBOpts().forEach( (k,v)->System.out.println("DBOpts:k["+k+"]v["+v+"]") );
			myDBAbsTmp.getDBOptsSpecial().forEach( (k,v)->System.out.println("DBOptsSpeicial:k["+k+"]v["+v+"]") );
			myDBAbsTmp.getDBOptsAux().forEach( (k,v)->System.out.println("DBOptsAux:k["+k+"]v["+v+"]") );
			
			// select "MyDBAbstract" of the same "DriverShim" in the "DBType 'ComboBox'"
			MyDBAbstract myDBAbsCandidate =
				this.comboBoxDBType.getItems().stream()
					.filter( item -> item.getDriveShim().getDriverClassName().equals(myDBAbsTmp.getDriveShim().getDriverClassName()) )
					.findAny()
					.orElse(null);
					
			if ( myDBAbsCandidate != null )
			{
				this.comboBoxDBType.valueProperty().removeListener( this.changeListener );
				// select DBType
				this.comboBoxDBType.getSelectionModel().select(myDBAbsCandidate);
				// set "User Name"
				this.usernameTextField.setText(myDBAbsTmp.getUsername());
				// set "Password"
				if ( myDBAbsTmp.getPassword() != null )
				{
					this.passwordTextField.setText(myDBAbsTmp.getPassword());
				}
				else
				{
					this.passwordTextField.setText("");
				}
				// set "URL"
				myDBAbsCandidate.setUrl(myDBAbsTmp.getUrl(),false);
				// set "dbOpts"
				myDBAbsCandidate.setDBOpts(myDBAbsTmp.getDBOpts());
				// set "dbOptsSpecial"
				myDBAbsCandidate.setDBOptsSpecial(myDBAbsTmp.getDBOptsSpecial());
				// set "dbOptsAux"
				myDBAbsCandidate.setDBOptsAux(myDBAbsTmp.getDBOptsAux());
				System.out.println( "myDBAbsCandidate:" + myDBAbsCandidate );
				System.out.println( "myDBAbsCandidate:url:" + myDBAbsCandidate.getUrl() + "|" );
				myDBAbsCandidate.getDBOpts().forEach( (k,v)->System.out.println("myDBAbsCandidate:DBOpts:k["+k+"]v["+v+"]") );
				
				this.setUrlPane(myDBAbsCandidate);
				this.comboBoxDBType.valueProperty().addListener( this.changeListener );
			}
		}
		catch ( Exception ex )
		{
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
	}
}
