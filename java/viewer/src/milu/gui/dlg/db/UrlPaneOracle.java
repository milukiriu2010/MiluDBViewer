package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;

import milu.db.MyDBAbstract;
import milu.ctrl.MainController;

public class UrlPaneOracle extends UrlPaneAbstract
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.dlg.db.UrlPaneOracle";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );	
	
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
	// Items for "Free Hand"
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
		this.tglBtnBasic.setText(langRB.getString("TOGGLE_BASIC"));
		this.tglBtnBasic.setToggleGroup( this.tglGroup );
		// ToggleButton for TNS
		this.tglBtnTNS.setText(langRB.getString("TOGGLE_TNS"));
		this.tglBtnTNS.setToggleGroup( this.tglGroup );
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(langRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnBasic, this.tglBtnTNS, this.tglBtnFreeHand );
		
		this.tglBtnBasic.setSelected(true);
		this.setPaneBasic();
		/*
		// set all objects on pane.
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
		vBox.getChildren().addAll( this.hBoxToggle, gridPane, this.urlTextArea );
		
		this.getChildren().addAll( vBox );
		*/
		
		
		// ----------------------------------------------------
		// Items for "Free Hand"
		// ----------------------------------------------------
		this.lblUrl.setText( "https://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm" );
		this.lblUrl.setCursor( Cursor.HAND );
		this.lblUrl.getStyleClass().add("url");
		
		this.setAction();
		
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
				if ( oldVal != null )
				{
					System.out.println( "obs[" + obs.getClass() + 
							"]oldVal" + oldVal.getClass() + 
							"]newVal[" + newVal.getClass() + "]" );
				}
				
				if ( newVal == this.tglBtnBasic )
				{
					this.setPaneBasic();
				}
				else if ( newVal == this.tglBtnTNS )
				{
					
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
		
		// set all objects on pane.
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
		vBox.getChildren().addAll( this.hBoxToggle, gridPane, this.urlTextArea );
		
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
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		return dbOptMap;
	}

	@Override
	public void setUrl() 
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		this.myDBAbs.getDriverUrl(dbOptMap);
	}

	/*
	@Override
	public String getUrl() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPort( int port )
	{
		this.portTextField.setText( String.valueOf(port) );
		this.setUrlTextArea();
	}
	*/
	
	private void setUrlTextArea()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		this.urlTextArea.setText( this.myDBAbs.getDriverUrl( dbOptMap ) );
	}
	
}
