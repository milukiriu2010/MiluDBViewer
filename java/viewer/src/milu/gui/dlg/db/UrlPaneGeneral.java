package milu.gui.dlg.db;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import milu.db.MyDBAbstract;
import milu.main.MainController;
import milu.tool.MyTool;

public class UrlPaneGeneral extends UrlPaneAbstract
{	
	private Dialog<?>      dlg          = null;
	
	private MainController mainCtrl     = null;
	
	private MyDBAbstract   myDBAbs      = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new ToggleGroup();
	
	// ToggleButton for Free Hand
	private ToggleButton   tglBtnFreeHand = new ToggleButton();
	
	// ----------------------------------------------------
	// Items for "All"
	// ----------------------------------------------------
	// field for URL
	private TextArea   urlTextArea = new TextArea();
	
	// Driver File Path
	private TextField  txtDriverPath  = new TextField();
	
	// Button to input "Driver File Path"
	private Button     btnDriverPath = new Button();
	
	// Driver Class Name
	private TextField  txtDriverClassName = new TextField();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnFreeHand );
		
		// "select driver path" button
		this.btnDriverPath.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ));
		
		this.tglBtnFreeHand.setSelected(true);
		this.setPaneFreeHand();
		
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
		
		this.btnDriverPath.setOnAction
		(
			(event)->
			{
				FileChooser fc = new FileChooser();
				File file = fc.showOpenDialog(null);
				if ( file != null )
				{
					this.txtDriverPath.setText( file.getAbsolutePath() );
				}
			}
		);
	}
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);
		
		// Fit width
		// ------------------------------------------
		// |  TextField                    | Button |
		// ------------------------------------------
		// |  TextArea                              |
		// ------------------------------------------
		this.txtDriverPath.setPrefWidth( this.urlTextArea.getWidth() - this.btnDriverPath.getWidth() );
		//this.txtDriverPath.setPrefWidth( -1 );
		HBox.setHgrow( this.txtDriverPath, Priority.ALWAYS );
		HBox.setHgrow( this.btnDriverPath, Priority.ALWAYS );
		
		Label lblDriverPath = new Label("JDBC Driver Path");
		
		HBox hBoxDriverPath = new HBox(2);
		hBoxDriverPath.getChildren().addAll( this.txtDriverPath, this.btnDriverPath );
		
		Label lblDriverClassName = new Label("JDBC Driver Class Name");
		
		Label lblUrl = new Label("JDBC URL");
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.hBoxToggle,
			lblDriverPath,
			hBoxDriverPath,
			lblDriverClassName,
			this.txtDriverClassName,
			lblUrl,
			this.urlTextArea 
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
			this.myDBAbs.addDriverPath( this.txtDriverPath.getText() );
			this.myDBAbs.setDriverClassName( this.txtDriverClassName.getText() );
			this.myDBAbs.setUrl( this.urlTextArea.getText() );
		}
	}
	
	private void setUrlTextArea()
	{
		this.setUrl();
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
	}
	
}
