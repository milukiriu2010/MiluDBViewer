package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import milu.db.MyDBAbstract;
import milu.ctrl.MainController;

public class UrlPaneCommon extends UrlPaneAbstract
{
	private MainController mainCtrl     = null;
	
	private MyDBAbstract   myDBAbs      = null;
	
	// field for DB Name
	private TextField dbnameTextField   = new TextField();
	
	// field for Host/IPAddress
	private TextField hostTextField     = new TextField();
	
	// field for Port
	private TextField portTextField     = new TextField();
	
	// field for URL
	private TextArea  urlTextArea       = new TextArea();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp )
	{
		this.myDBAbs = myDBAbs;
		
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
		vBox.getChildren().addAll( gridPane, this.urlTextArea );
		this.getChildren().addAll( vBox );
		
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
