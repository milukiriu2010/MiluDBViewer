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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.stage.Stage;

import milu.db.driver.DriverShim;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.PersistentButtonToggleGroup;
import milu.main.MainController;
import milu.tool.MyGUITool;

public class UrlPaneCassandra extends UrlPaneAbstract
{
	private Dialog<?>      dlg            = null;
	
	private MainController mainCtrl       = null;
	
	private MyDBAbstract   myDBAbs        = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new PersistentButtonToggleGroup();
	
	// ToggleButton for Basic
	private ToggleButton   tglBtnBasic    = new ToggleButton();
	
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
	// Items for "FreeHand"
	// ----------------------------------------------------
	private TextField tmplTextField     = new TextField();
	
	private Button    tmplBtn           = new Button();
	
	
	// ----------------------------------------------------
	// Items for "All"
	// ----------------------------------------------------
	private Label     lblUrl            = new Label();
	
	// field for URL
	private TextArea  urlTextArea       = new TextArea();
	
	@Override
	void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// ToggleButton for Basic
		this.tglBtnBasic.setText(extLangRB.getString("TOGGLE_BASIC"));
		this.tglBtnBasic.setToggleGroup( this.tglGroup );
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnBasic, this.tglBtnFreeHand );
		
		this.tglBtnBasic.setSelected(true);
		this.setPaneBasic();
		
		// ----------------------------------------------------
		// Items for "Basic"
		// ----------------------------------------------------
		Map<String,String> dbOptsAux = this.myDBAbs.getDBOptsAux();
		this.dbnameTextField.setText( dbOptsAux.get("DBName") );
		this.hostTextField.setText( dbOptsAux.get("Host") );
		if ( dbOptsAux.containsKey("Port") )
		{
			this.portTextField.setText( dbOptsAux.get("Port") );
		}
		else
		{
			this.portTextField.setText( String.valueOf(myDBAbs.getDefaultPort()) );
		}

		// ----------------------------------------------------
		// get URL by Driver Info
		// ----------------------------------------------------
		DriverShim driverShim = myDBAbs.getDriveShim();

		// ----------------------------------------------------
		// Items for "Free hand"
		// ----------------------------------------------------
		this.tmplTextField.setText( driverShim.getTemplateUrl() );
		this.tmplTextField.setEditable(false);

		this.tmplBtn.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/copy.png") ) );
		Tooltip tipCopy = new Tooltip(langRB.getString( "TOOLTIP_COPY" ));
		tipCopy.getStyleClass().add("Common_MyToolTip");
		this.tmplBtn.setTooltip( tipCopy );		
		// ----------------------------------------------------
		// Items for "All"
		// ----------------------------------------------------
		this.lblUrl.setText( driverShim.getReferenceUrl() );
		this.lblUrl.setCursor( Cursor.HAND );
		this.lblUrl.getStyleClass().add("DBSettingDialog_URL");
		
		this.setAction();
		
		this.setUrlTextArea();
	}
	
	void init()
	{
		System.out.println( "UrlPaneCassandra.init." );
		Map<String,String> dbOptsAux = this.myDBAbs.getDBOptsAux();
		if ( this.myDBAbs.getUrl() == null )
		{
			this.tglBtnBasic.setSelected(true);
			this.setUrlTextArea();
		}		
		else if ( dbOptsAux.containsKey("DBName") )
		{
			this.dbnameTextField.setText( dbOptsAux.get("DBName") );
			this.hostTextField.setText( dbOptsAux.get("Host") );
			this.portTextField.setText( dbOptsAux.get("Port") );
			this.tglBtnBasic.setSelected(true);
			this.setUrlTextArea();
		}
		else
		{
			this.urlTextArea.setText( this.myDBAbs.getUrl() );
			//this.setUrlTextArea();
			this.tglBtnFreeHand.setSelected(true);
		}
		
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
				else if ( newVal == this.tglBtnFreeHand )
				{
					this.setPaneFreeHand();
				}
				Stage stage = (Stage)this.dlg.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
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
		
		this.tmplBtn.setOnAction
		(
			(event)->
			{
				this.urlTextArea.setText( this.tmplTextField.getText() );
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

		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
		// set objects on GridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap( 5 );
		gridPane.setVgap( 2 );
		gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridPane.add( new Label( extLangRB.getString( "LABEL_CASSANDRA_KEYSPACE" )) , 0, 0 );
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
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);

		// Fit width
		// ------------------------------------------
		// |  TextArea                              |
		// ------------------------------------------
		// |  TextField                    | Button |
		// ------------------------------------------
		//this.tmplTextField.setPrefWidth( this.urlTextArea.getWidth() - this.tmplBtn.getWidth() );
		this.tmplTextField.setPrefWidth( 500 );
		
		HBox hBox = new HBox(2);
		hBox.getChildren().addAll( this.tmplTextField, this.tmplBtn );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, this.urlTextArea, this.lblUrl, hBox );
		
		this.getChildren().addAll( vBox );
	}

	@Override
	public String setUrl( MyDBAbstract.UPDATE update ) 
	{
		String url = null;
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		if ( this.tglBtnBasic.isSelected() )
		{
			dbOptMap.put( "DBName"  , this.dbnameTextField.getText() );
			dbOptMap.put( "Host"    , this.hostTextField.getText() );
			dbOptMap.put( "Port"    , this.portTextField.getText() );
			url = this.myDBAbs.getDriverUrl(dbOptMap,update);
		}
		else if ( this.tglBtnFreeHand.isSelected() )
		{
			url = this.urlTextArea.getText();
			if ( MyDBAbstract.UPDATE.WITH.equals(update) )
			{
				this.myDBAbs.setUrl( url );
			}
		}
		return url;
	}

	private void setUrlTextArea()
	{
		String url = this.setUrl(MyDBAbstract.UPDATE.WITHOUT);
		this.urlTextArea.setText( url );
	}
}
