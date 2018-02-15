package milu.gui.dlg;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Collections;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
//import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.image.Image;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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
			"conf.lang.dlg.DBSettingDialog";

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
	private TextField urlTextField      = new TextField();
	
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
		
		// set all objects on pane.
		GridPane paneDBOpt = new GridPane();
		paneDBOpt.setHgap( 5 );
		paneDBOpt.setVgap( 2 );
		paneDBOpt.setPadding( new Insets( 10, 10, 10, 10 ) );
		paneDBOpt.add( new Label( this.langRB.getString( "LABEL_DB_TYPE" )) , 0, 0 );
		paneDBOpt.add( this.comboBoxDBType   , 1, 0 );
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
		paneDlg.setCenter( paneDBOpt );
		paneDlg.setBottom( this.urlTextField );
		
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
		this.setUrlTextField();
		this.urlTextField.setEditable( false );
		
		// Window Icon
		try
		{
			InputStream inputStreamWinIcon = new FileInputStream( "resources" + File.separator + "images" + File.separator + "winicon.gif" );
			Image imgWinIcon = new Image( inputStreamWinIcon );
			Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
			stage.getIcons().add( imgWinIcon );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
		}
		
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
				this.setUrlTextField();
			}
		);
		
		// --------------------------------------------
		// Update urlTextField, when DBName is changed
		// --------------------------------------------
		this.dbnameTextField.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				this.setUrlTextField();
			}
		);
		
		// --------------------------------------------
		// Update urlTextField, when Host is changed
		// --------------------------------------------
		this.hostTextField.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				this.setUrlTextField();
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
				
				this.setUrlTextField();
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

	/*
	public void setFocus()
	{
		// set focus on ComboBox for DBType
		// http://krr.blog.shinobi.jp/javafx/javafx%20ui%E3%82%B3%E3%83%B3%E3%83%88%E3%83%AD%E3%83%BC%E3%83%AB%E3%81%AE%E9%81%B8%E6%8A%9E%E3%83%BB%E3%83%95%E3%82%A9%E3%83%BC%E3%82%AB%E3%82%B9
		Platform.runLater( ()->{ this.comboBoxDBType.requestFocus(); } );
	}
	*/
	
	// "OK" Button Event
	private void setActionBtnOK( ActionEvent event )
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBType"  , this.comboBoxDBType.getValue().toString() );
		dbOptMap.put( "UserName", this.usernameTextField.getText() );
		dbOptMap.put( "Password", this.passwordTextField.getText() );
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		MyDBAbstract myDBAbs = null;
		// It doesn't work well.
		//Platform.runLater( ()->{ this.getDialogPane().getScene().setCursor( Cursor.WAIT ); System.out.println("Cursor Wait."); } );
		//this.getDialogPane().getScene().setCursor( Cursor.WAIT );
		try
		{
			myDBAbs = this.comboBoxDBType.getValue();
			
			// Connect to DB
			myDBAbs.connect( dbOptMap );
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
		
		// It doesn't work well.
		//Platform.runLater( ()->{ this.getDialogPane().getScene().setCursor( Cursor.DEFAULT );  System.out.println("Cursor Default."); } );
		//this.getDialogPane().getScene().setCursor( Cursor.DEFAULT );
		
		// If DB connection is failed, this dialog keeps to open.
		if ( myDBAbs.isConnected() == false ) 
		{
			// The conditions are not fulfilled so we consume the event
			// to prevent the dialog to close
			event.consume();
		}
	}
	
	private void setUrlTextField()
	{
		MyDBAbstract myDBAbs = this.comboBoxDBType.getValue();
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		this.urlTextField.setText( myDBAbs.getDriverUrl( dbOptMap ) );
	}
	
	
	/*
	// "Connect" Button Event
	// https://stackoverflow.com/questions/38696053/prevent-javafx-dialog-from-closing
	// https://stackoverflow.com/questions/27523119/keep-showing-a-dialog-when-validation-of-input-fails/32287080
	//final Button btOk = (Button)this.getDialogPane().lookupButton(ButtonType.OK);
	//  => btOK is null.
	final Button           connectButton = (Button)this.getDialogPane().lookupButton( connectButtonType );
	final DBSettingDialog  dlg2 = this;
	connectButton.addEventFilter(
	    ActionEvent.ACTION, 
	    event -> 
	    {
			Map<String,String> dbOptMap = new HashMap<String,String>();
			
			dbOptMap.put( "DBType"  , this.comboBoxDBType.getValue() );
			dbOptMap.put( "UserName", this.usernameTextField.getText() );
			dbOptMap.put( "Password", this.passwordTextField.getText() );
			dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
			dbOptMap.put( "Host"    , this.hostTextField.getText() );
			dbOptMap.put( "Port"    , this.portTextField.getText() );

			// https://blog.idrsolutions.com/2014/05/tutorial-change-default-cursor-javafx/
			// Change Cursor to WAIT
			// doesn't work well.
			Scene scene = this.getDialogPane().getScene();
			scene.setCursor( Cursor.WAIT );
			
			// Disable "Connect" Button
			connectButton.setDisable( true );
			
			// Disable All Items on Dialog
			//dlg2.getDialogPane().setDisable( true );

			final ConnectDBInterface conDBInterface2 = this.conDBInterface;
			// Exec a task asynchronously
			Runnable task = new Runnable()
			{
				@Override
				public void run()
				{
					
					String errMsg = null;
					try
					{
						// Connect to DB
						myDBAbs = conDBInterface2.connectDB( dbOptMap );
					}
					catch ( ClassNotFoundException cnfEx )
					{
						errMsg = cnfEx.getMessage();
					}
					catch ( SQLException sqlEx )
					{
						System.out.println( "ERR:"+sqlEx.getMessage() );
						errMsg = sqlEx.getMessage();
					}
					
			        // If DB connection is failed, this dialog keeps to open.
			        if ( myDBAbs == null ) 
			        {
			        	System.out.println( "no connect:event consume" );
			        	
			        	
			            // The conditions are not fulfilled so we consume the event
			            // to prevent the dialog to close
			        	final String errMsgFinal = errMsg;
						Platform.runLater
						( 
							new Runnable()
							{
								@Override
								public void run()
								{
						    		Alert alert = new Alert(AlertType.WARNING);
						    		alert.setTitle( "DB Connection Error" );
						    		alert.setHeaderText(null);
						    		alert.setContentText( errMsgFinal );
						    		
						    		alert.showAndWait();
						    		
									System.out.println( "reset cursor." );
									// Reset Cursor to DEFAULT
									scene.setCursor( Cursor.DEFAULT );
									// Enable "Connect" Button
									connectButton.setDisable( false );

									// Enable All Items on Dialog
									//dlg2.getDialogPane().setDisable( false );
								}
							}
						);
			        }
			        else
			        {
			        	System.out.println( "connected." );
						// JavaFX Application Threadへのアクセス
						Platform.runLater
						( 
							new Runnable()
							{
								@Override
								public void run()
								{
									System.out.println( "reset cursor." );
									// Reset Cursor to DEFAULT
									scene.setCursor( Cursor.DEFAULT );
									// Enable "Connect" Button
									connectButton.setDisable( false );
									// Enable All Items on Dialog
									//dlg2.getDialogPane().setDisable( false );
									// Close Dialog by self.
									// https://stackoverflow.com/questions/28698106/why-am-i-unable-to-programmatically-close-a-dialog-on-javafx
									dlg2.setResult( myDBAbs );
									dlg2.close();
								}
							}
						);
			        }
					
				}
			};
			this.service.submit( task );
			
	        // If DB connection is failed, this dialog keeps to open.
	        if ( this.myDBAbs == null ) 
	        {
	            // The conditions are not fulfilled so we consume the event
	            // to prevent the dialog to close
	            event.consume();
	        }
	    }
	);
	*/
	
}
