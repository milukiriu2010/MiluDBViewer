package milu.gui.dlg.db;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Collections;

import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.image.Image;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.db.MyDBFactory;
import milu.gui.dlg.MyAlertDialog;

// Dialog sample
// http://code.makery.ch/blog/javafx-dialogs-official/
//public class DBSettingDialog extends Dialog<Map<String,String>>
public class DBSettingDialog extends Dialog<MyDBAbstract>
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.dlg.db.DBSettingDialog";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// Thread Pool
	//private ExecutorService  service = Executors.newSingleThreadExecutor();
	
	// DB Type List
	List<MyDBAbstract> dbTypeList = null;
	
	// ComboBox for DB type(Oracle/MySQL/Postgresql...)
	//private ComboBox<String>   comboBoxDBType = new ComboBox<String>();
	private ComboBox<MyDBAbstract>  comboBoxDBType = new ComboBox<MyDBAbstract>();
	
	// field for user
	private TextField usernameTextField = new TextField();
	
	// field for password
	private PasswordField passwordTextField = new PasswordField();
	
	// field for DB Name
	private TextField dbnameTextField   = new TextField();
	
	// field for Host/IPAddress
	private TextField hostTextField     = new TextField();
	
	// field for Port
	private TextField portTextField     = new TextField();
	
	// field for URL
	private TextArea  urlTextArea       = new TextArea();
	
	// Button "OK"
	ButtonType okButtonType = null;
	
	public DBSettingDialog()
	{
		// Set dialog title.
		this.setTitle( langRB.getString( "TITLE_DB_SETTING" ) );
		
		// ComboBox for DB type(Oracle/MySQL/Postgresql...)
		this.dbTypeList = new ArrayList<MyDBAbstract>();
		this.dbTypeList.add( MyDBFactory.getInstance( "Oracle" ) );
		this.dbTypeList.add( MyDBFactory.getInstance( "PostgreSQL" ) );
		this.dbTypeList.add( MyDBFactory.getInstance( "MySQL" ) );
		this.dbTypeList.add( MyDBFactory.getInstance( "Cassandra" ) );
		Collections.sort( this.dbTypeList );
		ObservableList<MyDBAbstract>  obsList = FXCollections.observableArrayList( this.dbTypeList );
		this.comboBoxDBType.setItems( obsList );
		
		// field for user
		this.usernameTextField.setPromptText( this.langRB.getString( "PROMPT_USERNAME" ) );
		
		// field for password
		this.passwordTextField.setPromptText( this.langRB.getString( "PROMPT_PASSWORD" ) );
		
		// field for DB Name
		this.dbnameTextField.setPromptText( this.langRB.getString( "PROMPT_DB_NAME" ) );
		
		// field for Host/IPAddress
		this.hostTextField.setPromptText( this.langRB.getString( "PROMPT_HOST_OR_IPADDRESS" ) );
		
		// field for Port
		this.portTextField.setPromptText( this.langRB.getString( "PROMPT_PORT" ) );
		
		HBox hBox = new HBox( 2 );
		hBox.getChildren().add( new Label( this.langRB.getString( "LABEL_DB_TYPE" )) );
		hBox.getChildren().add( this.comboBoxDBType );
		
		// set all objects on pane.
		GridPane paneDBOpt = new GridPane();
		paneDBOpt.setHgap( 5 );
		paneDBOpt.setVgap( 2 );
		paneDBOpt.setPadding( new Insets( 10, 10, 10, 10 ) );
		/*
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_DB_TYPE" )) , 0, 0 );
		paneDBOpt.add( this.comboBoxDBType   , 1, 0 );
		*/
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_USERNAME" )), 0, 1 );
		paneDBOpt.add( this.usernameTextField, 1, 1 );
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_PASSWORD" )), 0, 2 );
		paneDBOpt.add( this.passwordTextField, 1, 2 );
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_DB_NAME" )) , 0, 3 );
		paneDBOpt.add( this.dbnameTextField  , 1, 3 );
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_HOST_OR_IPADDRESS" )), 0, 4 );
		paneDBOpt.add( this.hostTextField    , 1, 4 );
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_PORT" )), 0, 5 );
		paneDBOpt.add( this.portTextField    , 1, 5 );
		
		// pane for Dialog
		BorderPane paneDlg = new BorderPane();
		paneDlg.setTop( hBox );
		paneDlg.setCenter( paneDBOpt );
		paneDlg.setBottom( this.urlTextArea );
		
		// set pane on dialog
		this.getDialogPane().setContent( paneDlg );
		
		// add button connect and cancel
		this.okButtonType            = new ButtonType( langRB.getString( "BTN_OK" )    , ButtonData.OK_DONE );
		ButtonType cancelButtonType  = new ButtonType( langRB.getString( "BTN_CANCEL" ), ButtonData.CANCEL_CLOSE );
		//this.getDialogPane().getButtonTypes().addAll( this.okButtonType, ButtonType.CANCEL );
		this.getDialogPane().getButtonTypes().addAll( this.okButtonType, cancelButtonType );
		
		// Set default selection on ComboBox(DB Type)
		this.comboBoxDBType.getSelectionModel().selectFirst();
		// Set default value on field for Port
		this.portTextField.setText( String.valueOf(this.comboBoxDBType.getValue().getDefaultPort()) );
		// Set default value on field for URL
		this.setUrlTextArea();
		this.urlTextArea.setEditable( false );
		this.urlTextArea.setWrapText(true);
		
		// Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( new Image( "file:resources/images/winicon.gif" ) );
		
		// set Action
		this.setAction();
		
		// set focus on ComboBox for DBType
		// http://krr.blog.shinobi.jp/javafx/javafx%20ui%E3%82%B3%E3%83%B3%E3%83%88%E3%83%AD%E3%83%BC%E3%83%AB%E3%81%AE%E9%81%B8%E6%8A%9E%E3%83%BB%E3%83%95%E3%82%A9%E3%83%BC%E3%82%AB%E3%82%B9
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		Platform.runLater( ()->{ this.comboBoxDBType.requestFocus(); } );
	}
	
	private void setAction()
	{
		// Change default port when DB Type is changed.
		// http://www.java2s.com/Code/Java/JavaFX/AddchangelistenertoComboBoxvalueProperty.htm
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
				this.portTextField.setText( String.valueOf(newVal.getDefaultPort()) );
				this.setUrlTextArea();
			}
		);
		
		// --------------------------------------------
		// Update urlTextField, when DBName is changed
		// --------------------------------------------
		this.dbnameTextField.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				this.setUrlTextArea();
			}
		);
		
		// --------------------------------------------
		// Update urlTextField, when Host is changed
		// --------------------------------------------
		this.hostTextField.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				this.setUrlTextArea();
			}
		);
		
		// --------------------------------------------
		// restriction for TextField "Port"
		// https://stackoverflow.com/questions/15615890/recommended-way-to-restrict-input-in-javafx-textfield
		// --------------------------------------------
		this.portTextField.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				// "Numeric" or "No Input" are allowed.
				if ( newVal.length() == 0 )
				{
				}
				// if alphabets or marks are input, back to previous input.
				else if ( newVal.matches( "^[0-9]+$" ) == false )
				{
					((StringProperty)obs).setValue( oldVal );
				}
				
				this.setUrlTextArea();
			}
		);
		
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
					
					return myDBAbs;
				}
				return null;
			}
		);
	}
	
	// "OK" Button Event
	private void setActionBtnOK( ActionEvent event )
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		//dbOptMap.put( "DBType"  , this.comboBoxDBType.getValue().toString() );
		//dbOptMap.put( "UserName", this.usernameTextField.getText() );
		//dbOptMap.put( "Password", this.passwordTextField.getText() );
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		MyDBAbstract myDBAbs = null;
		try
		{
			myDBAbs = this.comboBoxDBType.getValue();
			myDBAbs.setUsername( this.usernameTextField.getText() );
			myDBAbs.setPassword( this.passwordTextField.getText() );
			myDBAbs.getDriverUrl(dbOptMap);
			
			// Connect to DB
			//myDBAbs.connect( dbOptMap );
			myDBAbs.connect();
		}
		catch ( ClassNotFoundException cnfEx )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING );
    		alertDlg.setHeaderText( langRB.getString( "TITLE_DB_DRIVER_ERROR" ) );
    		alertDlg.setTxtExp( cnfEx );
    		alertDlg.showAndWait();
		}
		catch ( SQLException sqlEx )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING );
    		alertDlg.setHeaderText( langRB.getString( "TITLE_DB_CONNECT_ERROR" ) );
    		alertDlg.setTxtExp( sqlEx );
    		alertDlg.showAndWait();
		}
		
		// If DB connection is failed, this dialog keeps to open.
		if ( myDBAbs.isConnected() == false ) 
		{
			// The conditions are not fulfilled so we consume the event
			// to prevent the dialog to close
			event.consume();
		}
	}
	
	private void setUrlTextArea()
	{
		MyDBAbstract myDBAbs = this.comboBoxDBType.getValue();
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		this.urlTextArea.setText( myDBAbs.getDriverUrl( dbOptMap ) );
	}
	
}
