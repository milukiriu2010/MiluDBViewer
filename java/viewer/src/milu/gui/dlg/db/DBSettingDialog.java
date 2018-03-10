package milu.gui.dlg.db;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.db.MyDBFactory;
import milu.gui.dlg.MyAlertDialog;
import milu.ctrl.MainController;

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
	
	private MainController mainCtrl = null; 
	
	// Thread Pool
	//private ExecutorService  service = Executors.newSingleThreadExecutor();
	
	// DB Type List
	List<MyDBAbstract> dbTypeList = null;
	
	// ComboBox for DB type(Oracle/MySQL/Postgresql...)
	private ComboBox<MyDBAbstract>  comboBoxDBType = new ComboBox<MyDBAbstract>();
	
	// field for user
	private TextField usernameTextField = new TextField();
	
	// field for password
	private PasswordField passwordTextField = new PasswordField();
	
	// pane for Dialog
	BorderPane brdPane = new BorderPane();
	
	// VBox
	VBox       vBox    = new VBox(2);
	
	// UrlPaneAbstract Map
	Map<MyDBAbstract,UrlPaneAbstract>  urlPaneAbsMap = new HashMap<>();
	
	// Button "OK"
	ButtonType okButtonType = null;
	
	public DBSettingDialog( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
		
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
		
		// pane for Dialog
		this.vBox.getChildren().add( paneDBOpt );
		this.brdPane.setCenter( this.vBox );
		
		// set pane on dialog
		this.getDialogPane().setContent( this.brdPane );
		
		// add button connect and cancel
		this.okButtonType            = new ButtonType( langRB.getString( "BTN_OK" )    , ButtonData.OK_DONE );
		ButtonType cancelButtonType  = new ButtonType( langRB.getString( "BTN_CANCEL" ), ButtonData.CANCEL_CLOSE );
		//this.getDialogPane().getButtonTypes().addAll( this.okButtonType, ButtonType.CANCEL );
		this.getDialogPane().getButtonTypes().addAll( this.okButtonType, cancelButtonType );

		// Set default selection on ComboBox(DB Type)
		this.comboBoxDBType.getSelectionModel().selectFirst();
		MyDBAbstract selectedMyDBAbs = this.comboBoxDBType.getSelectionModel().getSelectedItem();
		PaneFactory paneFactory = new UrlPaneFactory();
		UrlPaneAbstract urlPaneAbs = paneFactory.createPane( this, this.mainCtrl, selectedMyDBAbs, langRB, new HashMap<String,String>() );
		this.urlPaneAbsMap.put( selectedMyDBAbs, urlPaneAbs );
		this.vBox.getChildren().add( urlPaneAbs );
		
		// Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );
		
		// set css for this dialog
		Scene scene = this.getDialogPane().getScene();
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/dlg/DBSettingDialog.css").toExternalForm()
		);
		
		// set Action
		this.setAction();
		
		// set focus on ComboBox for DBType
		// http://krr.blog.shinobi.jp/javafx/javafx%20ui%E3%82%B3%E3%83%B3%E3%83%88%E3%83%AD%E3%83%BC%E3%83%AB%E3%81%AE%E9%81%B8%E6%8A%9E%E3%83%BB%E3%83%95%E3%82%A9%E3%83%BC%E3%82%AB%E3%82%B9
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		Platform.runLater( ()->{ this.comboBoxDBType.requestFocus(); } );
		
		// set size
		//this.setResizable( true );
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
				ListIterator<Node> nodeLstIterator = this.vBox.getChildren().listIterator();
				while ( nodeLstIterator.hasNext() )
				{
					Node node = nodeLstIterator.next();
					if ( node instanceof UrlPaneAbstract )
					{
						UrlPaneAbstract urlPaneAbs1 = (UrlPaneAbstract)node;
						Map<String, String> mapProp = urlPaneAbs1.getProp();
						this.vBox.getChildren().remove( node );
						
						UrlPaneAbstract urlPaneAbs2 = null;
						// ReUse object, if selected before.
						if ( this.urlPaneAbsMap.containsKey(newVal) )
						{
							urlPaneAbs2 = this.urlPaneAbsMap.get(newVal); 
						}
						// Create object, if never selected before.
						else
						{
							PaneFactory paneFactory = new UrlPaneFactory();
							urlPaneAbs2 = paneFactory.createPane( this, this.mainCtrl, newVal, langRB, mapProp );
							this.urlPaneAbsMap.put( newVal, urlPaneAbs2 );
						}
						this.vBox.getChildren().add( urlPaneAbs2 );
						break;
					}
				}
				
				// https://stackoverflow.com/questions/44675375/failure-to-get-the-stage-of-a-dialog
				Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
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
		ListIterator<Node> nodeLstIterator = this.vBox.getChildren().listIterator();
		while ( nodeLstIterator.hasNext() )
		{
			Node node = nodeLstIterator.next();
			if ( node instanceof UrlPaneAbstract )
			{
				UrlPaneAbstract urlPaneAbs1 = (UrlPaneAbstract)node;
				urlPaneAbs1.setUrl();
			}
		}
		
		MyDBAbstract myDBAbs = null;
		try
		{
			myDBAbs = this.comboBoxDBType.getValue();
			myDBAbs.setUsername( this.usernameTextField.getText() );
			myDBAbs.setPassword( this.passwordTextField.getText() );
			
			// Connect to DB
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
}
