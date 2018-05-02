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
import javafx.scene.control.Tooltip;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
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
import milu.db.driver.DriverShim;
import milu.file.ext.MyFileExtAbstract;
import milu.file.ext.MyFileExtFactory;
import milu.gui.ctrl.common.PersistentButtonToggleGroup;
import milu.gui.dlg.MyAlertDialog;
import milu.main.AppConf;
import milu.main.MainController;
import milu.tool.MyTool;

public class UrlPaneOracle extends UrlPaneAbstract
{
	private Dialog<?>      dlg            = null;
	
	private MainController mainCtrl       = null;
	
	private MyDBAbstract   myDBAbs        = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new PersistentButtonToggleGroup();
	
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
	void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
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
		System.out.println( "###### constructor  ######" );
		System.out.println( "const url:" + this.myDBAbs.getUrl() );
		dbOptsAux.forEach( (k,v)->System.out.println("const DBOptsAux:k["+k+"]v["+v+"]") );
 		
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
		else if ( dbOptsAux.containsKey("DBAdmin") )
		{
			this.tnsAdminTextField.setText( dbOptsAux.get("DBAdmin") );
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
		this.tnsNamesCombo.setValue( dbOptsAux.get("TNSName") );
		
		String dirPath = this.tnsAdminTextField.getText();
		if ( dirPath.length() > 0 )
		{
			File dir = new File( dirPath );
			this.loadTnsNamesOra( dir );
		}
		
		// "select folder" button
		this.folderBtn.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ));
		this.folderBtn.setTooltip( new Tooltip(langRB.getString( "TOOLTIP_OPEN_FOLDER" )) );

		// ----------------------------------------------------
		// get URL by Driver Info
		// ----------------------------------------------------
		DriverShim driverShim = myDBAbs.getDriveShim();
		
		// ----------------------------------------------------
		// Items for "Free hand"
		// ----------------------------------------------------
		//this.tmplTextField.setText("jdbc:oracle:thin:@//<host>[:1521]/<service_name>[?internal_logon=sysdba|sysoper]");
		this.tmplTextField.setText( driverShim.getTemplateUrl() );
		this.tmplTextField.setEditable(false);

		this.tmplBtn.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/copy.png") ));
		this.tmplBtn.setTooltip( new Tooltip(langRB.getString( "TOOLTIP_COPY" )) );
		
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
		System.out.println( "UrlPaneOracle.init." );
		Map<String,String> dbOptsAux = this.myDBAbs.getDBOptsAux();
		System.out.println( "###### init  ######" );
		System.out.println( "init url:" + this.myDBAbs.getUrl() );
		dbOptsAux.forEach( (k,v)->System.out.println("init DBOptsAux:k["+k+"]v["+v+"]") );
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
		else if ( dbOptsAux.containsKey("TNSAdmin") )
		{
			this.tnsAdminTextField.setText( dbOptsAux.get("TNSAdmin") );
			this.tnsNamesCombo.setValue( dbOptsAux.get("TNSName") );
			this.tglBtnTNS.setSelected(true);
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
				File dir = dc.showDialog(this.dlg.getDialogPane().getScene().getWindow());
				if ( dir != null )
				{
					this.tnsAdminTextField.setText( dir.getAbsolutePath() );
					this.loadTnsNamesOra( dir );
				}
			}
		);
		
		/*
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
		*/
		
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
		gridPane.add( new Label( extLangRB.getString( "LABEL_ORACLE_SID" )) , 0, 0 );
		gridPane.add( this.dbnameTextField  , 1, 0 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_HOST_OR_IPADDRESS" )), 0, 1 );
		gridPane.add( this.hostTextField    , 1, 1 );
		gridPane.add( new Label( extLangRB.getString( "LABEL_PORT" )), 0, 2 );
		gridPane.add( this.portTextField    , 1, 2 );
		
		// Set default value on field for URL
		//this.setUrlTextArea();
		this.urlTextArea.setEditable( false );
		this.urlTextArea.setWrapText(true);		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.hBoxToggle, gridPane, this.urlTextArea, this.lblUrl );
		
		this.getChildren().addAll( vBox );
	}
	
	private void setPaneTNS()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
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
		//this.setUrlTextArea();
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
		else if ( this.tglBtnTNS.isSelected() )
		{
			dbOptMap.put( "TNSName"  , this.tnsNamesCombo.getValue() );
			dbOptMap.put( "TNSAdmin" , this.tnsAdminTextField.getText() );
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
		Map<String,String> dbOptsAux = this.myDBAbs.getDBOptsAux();
		System.out.println( "###### setUrlTextArea1  ######" );
		System.out.println( "setUrl1 url:" + this.myDBAbs.getUrl() );
		dbOptsAux.forEach( (k,v)->System.out.println("setUrl1 DBOptsAux:k["+k+"]v["+v+"]") );
		String url = this.setUrl(MyDBAbstract.UPDATE.WITHOUT);
		dbOptsAux = this.myDBAbs.getDBOptsAux();
		System.out.println( "###### setUrlTextArea2  ######" );
		System.out.println( "setUrl2 url:" + this.myDBAbs.getUrl() );
		dbOptsAux.forEach( (k,v)->System.out.println("setUrl2 DBOptsAux:k["+k+"]v["+v+"]") );
		//this.urlTextArea.setText( this.myDBAbs.getUrl() );
		this.urlTextArea.setText( url );
	}
	
	private List<String> loadTnsNamesOra( File dir )
	{
		// "UnsupportedOperationException"
		//ObservableList<String>  obsLst = this.tnsNamesCombo.getItems();
		//if ( obsLst != null && obsLst.size() > 0 )
		//{
		//	this.tnsNamesCombo.getItems().removeAll( obsLst );
		//}
		
		List<String> tnsNameLst = new ArrayList<>();
		
		File file = new File( dir.getAbsolutePath() + File.separator + "tnsnames.ora" );
		if ( file.exists() == false )
		{
			return tnsNameLst;
		}
		
		//MyFileAbstract myFileAbs = MyFileFactory.getInstance(file);
		MyFileExtAbstract<String> myFileAbs = MyFileExtFactory.getInstance(MyFileExtFactory.TYPE.TNSNAMES_ORACLE);
		
		try
		{
			//myFileAbs.open( file );
			//String strTNSFile = myFileAbs.load();
			String strTNSFile = myFileAbs.load( file, String.class );
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
			ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		alertDlg.setHeaderText( extLangRB.getString( "TITLE_FILE_NOT_FOUND" ) );
    		alertDlg.setTxtExp( ioEx );
    		alertDlg.showAndWait();
    	}
		catch ( Exception ex )
		{
			ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
    		alertDlg.setHeaderText( extLangRB.getString( "TITLE_FILE_NOT_FOUND" ) );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
		
		return tnsNameLst;
	}
}
