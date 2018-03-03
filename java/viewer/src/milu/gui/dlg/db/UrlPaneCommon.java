package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import milu.db.MyDBAbstract;

public class UrlPaneCommon extends Pane implements UrlInterface 
{
	private MyDBAbstract  myDBAbs       = null;
	
	// field for DB Name
	private TextField dbnameTextField   = new TextField();
	
	// field for Host/IPAddress
	private TextField hostTextField     = new TextField();
	
	// field for Port
	private TextField portTextField     = new TextField();
	
	// field for URL
	private TextArea  urlTextArea       = new TextArea();
	
	public void createPane( MyDBAbstract myDBAbs, ResourceBundle langRB )
	{
		this.myDBAbs = myDBAbs;
		
		// set all objects on pane.
		GridPane gridPane = new GridPane();
		gridPane.setHgap( 5 );
		gridPane.setVgap( 2 );
		gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridPane.add( new Label( langRB.getString( "LABEL_DB_NAME" )) , 0, 0 );
		gridPane.add( this.dbnameTextField  , 1, 0 );
		gridPane.add( new Label( langRB.getString( "LABEL_HOST_OR_IPADDRESS" )), 0, 1 );
		gridPane.add( this.hostTextField    , 1, 1 );
		gridPane.add( new Label( langRB.getString( "LABEL_PORT" )), 0, 2 );
		gridPane.add( this.portTextField    , 1, 2 );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( gridPane, this.urlTextArea );
		
		this.getChildren().addAll( vBox );
		
		this.setAction();
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
	}

	@Override
	public void setUrl(String url) 
	{
		// TODO Auto-generated method stub

	}

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
	
	private void setUrlTextArea()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
		dbOptMap.put( "Host"    , this.hostTextField.getText() );
		dbOptMap.put( "Port"    , this.portTextField.getText() );
		
		this.urlTextArea.setText( this.myDBAbs.getDriverUrl( dbOptMap ) );
	}
	
}
