package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;

import milu.db.MyDBAbstract;
import milu.ctrl.MainController;

import milu.file.MyFileAbstract;
import milu.file.MyFileFactory;
import milu.gui.dlg.MyAlertDialog;

public class UrlPaneOracle extends UrlPaneAbstract
{
	/*
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.dlg.db.UrlPaneOracle";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	*/	
	
	// Language Resource(from External Class)
	private ResourceBundle extLangRB = null;
	
	private MainController mainCtrl       = null;
	
	private MyDBAbstract   myDBAbs        = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new ToggleGroup();
	
	// ToggleButton for Basic
	private ToggleButton   tglBtnBasic    = new ToggleButton();
	
	// ToggleButton for TNS
	private ToggleButton   tglBtnTNS      = new ToggleButton();
	
	// ToggleButton for Free Hand
	private ToggleButton   tglBtnFreeHand = new ToggleButton();
	
	// ----------------------------------------------------
	// Items for "Basic"
	// ----------------------------------------------------
	// field for DB Name
	private TextField dbnameTextField   = new TextField();
	
	// field for Host/IPAddress
	private TextField hostTextField     = new TextField();
	
	// field for Port
	private TextField portTextField     = new TextField();
	
	// ----------------------------------------------------
	// Items for "TNS"
	// ----------------------------------------------------
	private ComboBox<String> tnsNamesCombo = new ComboBox<>();
	
	private TextField tnsAdminTextField    = new TextField();
	
	private Button    folderBtn            = new Button();
	
	// ----------------------------------------------------
	// Items for "FreeHand"
	// ----------------------------------------------------
	private Label     lblUrl            = new Label();
	
	// field for URL
	private TextArea  urlTextArea       = new TextArea();
	
	public void createPane( MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp )
	{
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		this.extLangRB = extLangRB;
		
		// ToggleButton for Basic
		this.tglBtnBasic.setText(extLangRB.getString("TOGGLE_BASIC"));
		this.tglBtnBasic.setToggleGroup( this.tglGroup );
		// ToggleButton for TNS
		this.tglBtnTNS.setText(extLangRB.getString("TOGGLE_TNS"));
		this.tglBtnTNS.setToggleGroup( this.tglGroup );
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnBasic, this.tglBtnTNS, this.tglBtnFreeHand );
		
		this.tglBtnBasic.setSelected(true);
		this.setPaneBasic();
		
		// ----------------------------------------------------
		// Items for "Basic"
		// ----------------------------------------------------
		String dbName = mapProp.get("DBName");
		if ( dbName != null )
		{
			this.dbnameTextField.setText( dbName );
		}
		String host = mapProp.get("Host");
		if ( host != null )
		{
			this.hostTextField.setText( host );
		}
		this.portTextField.setText( String.valueOf(myDBAbs.getDefaultPort()) );
		
		// ----------------------------------------------------
		// Items for "TNS"
		// ----------------------------------------------------
		this.tnsNamesCombo.setEditable(true);
		
		if ( System.getenv("TNS_ADMIN") != null )
		{
			String tns_admin = System.getenv("TNS_ADMIN");
			if ( new File(tns_admin).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin );
			}
		}
		else if ( System.getenv("ORACLE_HOME") != null )
		{
			String oracle_home = System.getenv("ORACLE_HOME");
			String tns_admin1 = oracle_home + java.io.File.separator + "network" + java.io.File.separator + "admin";
			String tns_admin2 = oracle_home + java.io.File.separator + "NETWORK" + java.io.File.separator + "ADMIN";
			if ( new File(tns_admin1).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin1 );
			}
			else if ( new File(tns_admin2).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin2 );
			}
		}
		
		ImageView   ivFolder = new ImageView( this.mainCtrl.getImage("file:resources/images/folder.png") );
		ivFolder.setFitWidth(16);
		ivFolder.setFitHeight(16);
		this.folderBtn.setGraphic( ivFolder );
		
		// ----------------------------------------------------
		// Items for "Freehand"
		// ----------------------------------------------------
		this.lblUrl.setText( "https://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm" );
		this.lblUrl.setCursor( Cursor.HAND );
		this.lblUrl.getStyleClass().add("url");
		
		this.setAction();
		
		this.setUrlTextArea();
	}
	
	private void setAction()
	{
		// --------------------------------------------
		// Selected Toggle Button is changed
		// --------------------------------------------
		this.tglGroup.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal == this.tglBtnBasic )
				{
					this.setPaneBasic();
				}
				else if ( newVal == this.tglBtnTNS )
				{
					this.setPaneTNS();
				}
				else if ( newVal == this.tglBtnFreeHand )
				{
					this.setPaneFreeHand();
				}
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
				if ( newVal == null )
				{
				}
				// "Numeric" or "No Input" are allowed.
				else if ( newVal.length() == 0 )
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
		
		this.tnsNamesCombo.valueProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "TNSName:ComboBox:" + newVal );
				this.setUrlTextArea();
			}
		);
		
		// https://stackoverflow.com/questions/37923502/how-to-get-entered-value-in-editable-combobox-in-javafx
		this.tnsNamesCombo.getEditor().focusedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				// lose focus
				if ( newVal == false )
				{
					System.out.println( "TNSName:ComboBox:lose focus." );
					this.tnsNamesCombo.setValue( this.tnsNamesCombo.getEditor().getText() );
				}
			}
		);
		
		this.folderBtn.setOnAction
		(
			(event)->
			{
				DirectoryChooser dc = new DirectoryChooser();
				File dir = dc.showDialog(null);
				if ( dir != null )
				{
					this.tnsAdminTextField.setText( dir.getAbsolutePath() );
					this.loadTnsNamesOra( dir );
				}
			}
		);
		
		this.tnsAdminTextField.focusedProperty().addListener
		(
			(event)->
			{
				System.out.println( "TNSAdmin:lose focus." );
				String dirPath = this.tnsAdminTextField.getText();
				File dir = new File( dirPath );
				this.loadTnsNamesOra( dir );
			}
		);
		
		this.lblUrl.setOnMouseClicked
		(
			(event)->
			{
				Application application = this.mainCtrl.getApplication();
				application.getHostServices().showDocument( this.lblUrl.getText() );
			}
		);
	}
	
	private void setPaneBasic()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		// set objects on GridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap( 5 );
		gridPane.setVgap( 2 );
		gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridPane.add( new Label( extLangRB.getString( "LABEL_DB_NAME" )) , 0, 0 );
		gridPane.add( this.dbnameTextField  , 1, 0 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_HOST_OR_IPADDRESS" )), 0, 1 );
		gridPane.add( this.hostTextField    , 1, 1 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_PORT" )), 0, 2 );
		gridPane.add( this.portTextField    , 1, 2 );
		
		// Set default value on field for URL
		this.setUrlTextArea();
		this.urlTextArea.setEditable( false );
		this.urlTextArea.setWrapText(true);		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, gridPane, this.urlTextArea, this.lblUrl );
		
		this.getChildren().addAll( vBox );
	}
	
	private void setPaneTNS()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		// set objects on GridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap( 5 );
		gridPane.setVgap( 2 );
		gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridPane.add( new Label( extLangRB.getString( "LABEL_TNS_ADMIN" )), 0, 0 );
		gridPane.add( this.tnsAdminTextField, 1, 0 );
		gridPane.add( this.folderBtn, 2, 0 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_TNS_NAMES" )), 0, 1 );
		gridPane.add( this.tnsNamesCombo    , 1, 1 );
		
		// Set default value on field for URL
		this.setUrlTextArea();
		this.urlTextArea.setEditable( false );
		this.urlTextArea.setWrapText(true);			
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, gridPane, this.urlTextArea, this.lblUrl );
		
		this.getChildren().addAll( vBox );
	}
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, this.urlTextArea, this.lblUrl );
		
		this.getChildren().addAll( vBox );
	}
	
	@Override
	public Map<String,String> getProp()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		if ( this.tglBtnBasic.isSelected() )
		{
			dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
			dbOptMap.put( "Host"    , this.hostTextField.getText() );
			dbOptMap.put( "Port"    , this.portTextField.getText() );
		}
		else if ( this.tglBtnTNS.isSelected() )
		{
			dbOptMap.put( "TNSName"  , this.tnsNamesCombo.getValue() );
			dbOptMap.put( "TNSAdmin" , this.tnsAdminTextField.getText() );
		}		
		
		return dbOptMap;
	}

	@Override
	public void setUrl() 
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		if ( this.tglBtnBasic.isSelected() )
		{
			dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
			dbOptMap.put( "Host"    , this.hostTextField.getText() );
			dbOptMap.put( "Port"    , this.portTextField.getText() );
			this.myDBAbs.getDriverUrl(dbOptMap);
		}
		else if ( this.tglBtnTNS.isSelected() )
		{
			dbOptMap.put( "TNSName"  , this.tnsNamesCombo.getValue() );
			dbOptMap.put( "TNSAdmin" , this.tnsAdminTextField.getText() );
			this.myDBAbs.getDriverUrl(dbOptMap);
		}
		else if ( this.tglBtnFreeHand.isSelected() )
		{
			this.myDBAbs.setUrl( this.urlTextArea.getText() );
		}
	}

	private void setUrlTextArea()
	{
		this.setUrl();
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
	}
	
	private List<String> loadTnsNamesOra( File dir )
	{
		List<String> tnsNameLst = new ArrayList<>();
		
		File file = new File( dir.getAbsolutePath() + File.separator + "tnsnames.ora" );
		if ( file.exists() == false )
		{
			return tnsNameLst;
		}
		
		MyFileAbstract myFileAbs = MyFileFactory.getInstance(file);
		
		try
		{
			myFileAbs.open( file );
			String strTNSFile = myFileAbs.load();
			// ^([^#()\W ][a-zA-Z.]*(?:[.][a-zA-Z]*\s?=)?)
			System.out.println( "=== tnsnames.ora ===" );
			System.out.println( strTNSFile );
		}
		catch ( IOException ioEx )
		{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING );
    		alertDlg.setHeaderText( extLangRB.getString( "TITLE_FILE_NOT_FOUND" ) );
    		alertDlg.setTxtExp( ioEx );
    		alertDlg.showAndWait();
    	}
		
		return tnsNameLst;
	}
}
