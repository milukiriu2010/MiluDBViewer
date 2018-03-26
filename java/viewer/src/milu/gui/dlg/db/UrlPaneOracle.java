package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import milu.db.MyDBAbstract;
import milu.conf.AppConf;
import milu.ctrl.MainController;

import milu.file.MyFileAbstract;
import milu.file.MyFileFactory;
import milu.gui.dlg.MyAlertDialog;

public class UrlPaneOracle extends UrlPaneAbstract
{
	// Language Resource(from External Class)
	private ResourceBundle extLangRB = null;
	
	private Dialog<?>      dlg            = null;
	
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
	
	private final ObservableList<String>  hints = FXCollections.observableArrayList();
	
	private FilteredList<String>  filteredItems = null;
	
	private TextField tnsAdminTextField    = new TextField();
	
	private Button    folderBtn            = new Button();
	
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
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		this.extLangRB = extLangRB;
		
		AppConf  appConf   = this.mainCtrl.getAppConf();
		
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
		// TNS_ADMIN(Default Value)
		if ( appConf.getOracleTnsAdmin().length() > 0 )
		{
			String tns_admin = appConf.getOracleTnsAdmin();
			this.tnsAdminTextField.setText( tns_admin );
		}
		else if ( System.getProperty("oracle.net.tns_admin") != null )
		{
			String tns_admin = System.getProperty("oracle.net.tns_admin");
			if ( new File(tns_admin).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin );
			}
		}
		else if ( System.getenv("TNS_ADMIN") != null )
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
		
		// TNS_ADMIN(Default Prompt)
		if ( System.getProperty("os.name").contains("Windows") )
		{
			this.tnsAdminTextField.setPromptText("%ORACLE_HOME%\\network\\admin");
		}
		else
		{
			this.tnsAdminTextField.setPromptText("$ORACLE_HOME/network/admin");
		}
		
		// TNS Name
		this.tnsNamesCombo.setEditable(true);
		this.filteredItems = new FilteredList<String>( this.hints, pre->true );
		this.tnsNamesCombo.setItems( this.filteredItems );
		
		String dirPath = this.tnsAdminTextField.getText();
		if ( dirPath.length() > 0 )
		{
			File dir = new File( dirPath );
			this.loadTnsNamesOra( dir );
		}
		
		// "select folder" button
		ImageView   ivFolder = new ImageView( this.mainCtrl.getImage("file:resources/images/folder.png") );
		ivFolder.setFitWidth(16);
		ivFolder.setFitHeight(16);
		this.folderBtn.setGraphic( ivFolder );
		
		// ----------------------------------------------------
		// Items for "Freehand"
		// ----------------------------------------------------
		this.tmplTextField.setText("jdbc:oracle:thin:@//<host>[:1521]/<service_name>[?internal_logon=sysdba|sysoper]");
		this.tmplTextField.setEditable(false);

		ImageView   ivCopy = new ImageView( this.mainCtrl.getImage("file:resources/images/copy.png") );
		ivCopy.setFitWidth(16);
		ivCopy.setFitHeight(16);
		this.tmplBtn.setGraphic(ivCopy);
		
		// ----------------------------------------------------
		// Items for "All"
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
					this.tnsNamesCombo.hide();
				}
				// get focus
				else if ( newVal == true )
				{
					this.tnsNamesCombo.show();
				}
			}
		);
		
		// https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
		this.tnsNamesCombo.getEditor().textProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				final TextField editor   = this.tnsNamesCombo.getEditor();
				final String    selected = this.tnsNamesCombo.getSelectionModel().getSelectedItem();
				
				// This needs run on the GUI thread to avoid the error described
				// here: https://bugs.openjdk.java.net/browse/JDK-8081700.
				Platform.runLater
				(
					()->
					{
						// If the no item in the list is selected 
						// or 
						// the selected item isn't equal to the current input, we refilter the list.
						if ( selected == null || !selected.equals(editor.getText()) )
						{
							this.filteredItems.setPredicate
							(
								(item)->
								{
			                        // We return true for any items that starts with the
			                        // same letters as the input. We use toUpperCase to
			                        // avoid case sensitivity.
			                        if ( item.toUpperCase().startsWith(newVal.toUpperCase()) ) 
			                        {
			                            return true;
			                        } 
			                        else
			                        {
			                            return false;
			                        }									
								}
							);
						}
					}
				);
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
		
		// set objects on GridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap( 5 );
		gridPane.setVgap( 2 );
		gridPane.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridPane.add( new Label( extLangRB.getString( "LABEL_ORACLE_SID" )) , 0, 0 );
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
		Label lblTnsAdmin = new Label( extLangRB.getString( "LABEL_TNS_ADMIN" )); 
		gridPane.add( lblTnsAdmin, 0, 0 );
		gridPane.add( this.tnsAdminTextField, 1, 0 );
		gridPane.add( this.folderBtn, 2, 0 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_TNS_NAMES" )), 0, 1 );
		gridPane.add( this.tnsNamesCombo    , 1, 1 );
		
		// Set default value on field for URL
		this.setUrlTextArea();
		this.urlTextArea.setEditable( false );
		this.urlTextArea.setWrapText(true);
		
		// Fit width
		// ------------------------------------------
		// |  TextArea                              |
		// ------------------------------------------
		// |  Label | TextField            | Button |
		// ------------------------------------------
		this.tnsAdminTextField.setPrefWidth( this.urlTextArea.getWidth() - lblTnsAdmin.getWidth() - this.folderBtn.getWidth() );
		
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
		this.tmplTextField.setPrefWidth( this.urlTextArea.getWidth() - this.tmplBtn.getWidth() );
		
		HBox hBox = new HBox(2);
		hBox.getChildren().addAll( this.tmplTextField, this.tmplBtn );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, this.urlTextArea, this.lblUrl, hBox );
		
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
		this.tnsNamesCombo.getItems().removeAll( this.tnsNamesCombo.getItems() );
		
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
			//System.out.println( "=== tnsnames.ora ===" );
			//System.out.println( strTNSFile );
			
			// ^([^#()\W ][a-zA-Z.]*(?:[.][a-zA-Z]*\s?=)?)
			Pattern p = Pattern.compile("^(\\w+)\\s*=", Pattern.MULTILINE|Pattern.DOTALL);
			Matcher m = p.matcher(strTNSFile);
			List<String> itemLst = new ArrayList<>();
			while ( m.find() )
			{
				itemLst.add( m.group().replaceAll("^(\\w+)\\s*=", "$1") );
				//this.tnsNamesCombo.getItems().addAll( m.group().replaceAll("^(\\w+)\\s*=", "$1") );
			}
			ObservableList<String> obsItemLst = FXCollections.observableList( itemLst );
			Collections.sort( obsItemLst );
			
			this.hints.removeAll( this.hints );
			this.hints.addAll( obsItemLst );
			
			// list back to full
			this.filteredItems.setPredicate( pre->true );
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
