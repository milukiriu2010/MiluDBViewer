package milu.gui.dlg.db;

import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
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
import milu.tool.MyGUITool;

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
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnFreeHand );
		
		this.tglBtnFreeHand.setSelected(true);
		this.setPaneFreeHand();
		
		if ( myDBAbs != null )
		{
			
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

			this.tmplBtn.setGraphic( MyGUITool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/copy.png") ));
			Tooltip tipCopy = new Tooltip(langRB.getString( "TOOLTIP_COPY" ));
			tipCopy.getStyleClass().add("Common_MyToolTip");
			this.tmplBtn.setTooltip( tipCopy );
			
			// ----------------------------------------------------
			// Items for "All"
			// ----------------------------------------------------
			//this.lblUrl.setText( "https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html" );
			this.lblUrl.setText( driverShim.getReferenceUrl() );
			this.lblUrl.setCursor( Cursor.HAND );
			this.lblUrl.getStyleClass().add("DBSettingDialog_URL");
		}

		
		this.setAction();
		
		this.setUrlTextArea();
	}
	
	void init()
	{
		System.out.println( "UrlPaneGeneral.init:" + this.myDBAbs );
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
		this.tglBtnFreeHand.setSelected(true);
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
		// sticky when resize dialog
		//this.tmplTextField.prefWidthProperty().bind( this.urlTextArea.widthProperty() );
		// it doesn't work.
		//this.tmplTextField.setPrefWidth( this.urlTextArea.getWidth() - this.tmplBtn.getWidth() );
		this.tmplTextField.setPrefWidth( 500 );
		
		HBox hBox = new HBox(2);
		hBox.getChildren().addAll( this.tmplTextField, this.tmplBtn );		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.hBoxToggle,
			this.urlTextArea,
			hBox,
			this.lblUrl
		);
		
		this.getChildren().addAll( vBox );
	}

	@Override
	public String setUrl( MyDBAbstract.UPDATE update ) 
	{
		String url = null;
		if ( this.tglBtnFreeHand.isSelected() )
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
		//this.setUrl();
		String url = this.setUrl(MyDBAbstract.UPDATE.WITHOUT);
		this.urlTextArea.setText( url );
	}
	
}
