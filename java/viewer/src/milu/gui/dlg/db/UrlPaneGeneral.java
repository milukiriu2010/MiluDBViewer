package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import milu.db.MyDBAbstract;
import milu.db.driver.DriverShim;
import milu.gui.ctrl.common.PersistentButtonToggleGroup;
import milu.main.MainController;

public class UrlPaneGeneral extends UrlPaneAbstract
{	
	private Dialog<?>      dlg          = null;
	
	private MainController mainCtrl     = null;
	
	private MyDBAbstract   myDBAbs      = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new PersistentButtonToggleGroup();
	
	// ToggleButton for Free Hand
	private ToggleButton   tglBtnFreeHand = new ToggleButton();
	
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
	private TextArea   urlTextArea = new TextArea();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, Map<String,String> mapProp )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnFreeHand );
		
		this.tglBtnFreeHand.setSelected(true);
		this.setPaneFreeHand();

		// ----------------------------------------------------
		// get URL by Driver Info
		// ----------------------------------------------------
		DriverShim driverShim = myDBAbs.getDriveShim();
		
		// ----------------------------------------------------
		// Items for "Free hand"
		// ----------------------------------------------------
		//this.tmplTextField.setText("jdbc:mysql://[host1][:3306][,[host2][:port2]]...[/[database]][?autoReconnect=true][&autoClosePStmtStreams=true]");
		this.tmplTextField.setText( driverShim.getTemplateUrl() );
		this.tmplTextField.setEditable(false);

		ImageView   ivCopy = new ImageView( this.mainCtrl.getImage("file:resources/images/copy.png") );
		ivCopy.setFitWidth(16);
		ivCopy.setFitHeight(16);
		this.tmplBtn.setGraphic(ivCopy);
		
		// ----------------------------------------------------
		// Items for "All"
		// ----------------------------------------------------
		//this.lblUrl.setText( "https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html" );
		this.lblUrl.setText( driverShim.getReferenceUrl() );
		this.lblUrl.setCursor( Cursor.HAND );
		this.lblUrl.getStyleClass().add("DBSettingDialog_URL");
		
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
				if ( newVal == this.tglBtnFreeHand )
				{
					this.setPaneFreeHand();
				}
				Stage stage = (Stage)this.dlg.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
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
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);
		
		//Label lblUrl = new Label("JDBC URL");
		
		// Fit width
		// ------------------------------------------
		// |  TextArea                              |
		// ------------------------------------------
		// |  TextField                    | Button |
		// ------------------------------------------
		this.tmplTextField.prefWidthProperty().bind( this.urlTextArea.widthProperty() );
		// it doesn't work.
		//this.tmplTextField.setPrefWidth( this.urlTextArea.getWidth() - this.tmplBtn.getWidth() );
		
		HBox hBox = new HBox(2);
		hBox.getChildren().addAll( this.tmplTextField, this.tmplBtn );		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.hBoxToggle,
			this.urlTextArea,
			this.lblUrl,
			hBox
		);
		
		this.getChildren().addAll( vBox );
	}
	
	@Override
	public Map<String,String> getProp()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		return dbOptMap;
	}

	@Override
	public void setUrl() 
	{
		if ( this.tglBtnFreeHand.isSelected() )
		{
			this.myDBAbs.setUrl( this.urlTextArea.getText() );
		}
	}
	
	private void setUrlTextArea()
	{
		this.setUrl();
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
	}
	
}
